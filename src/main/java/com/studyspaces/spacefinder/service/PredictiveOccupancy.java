package com.studyspaces.spacefinder.service;

import com.studyspaces.spacefinder.model.Occupancy;
import com.studyspaces.spacefinder.model.OccupancyRecord;
import com.studyspaces.spacefinder.model.RoomOccupancyRecord;
import com.studyspaces.spacefinder.repository.HistoricOccupancyRepository;
import com.studyspaces.spacefinder.repository.RealTimeOccupancyRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.tribuo.*;
import org.tribuo.datasource.ListDataSource;
import org.tribuo.provenance.SimpleDataSourceProvenance;
import org.tribuo.regression.*;
import org.tribuo.regression.ensemble.AveragingCombiner;
import org.tribuo.regression.rtree.*;
import org.tribuo.impl.ArrayExample;
import org.tribuo.regression.Regressor;
import org.tribuo.common.tree.RandomForestTrainer;
import org.tribuo.regression.rtree.impurity.MeanSquaredError;

import java.time.*;
import java.util.*;
import java.util.Map;

/**
 * PredictiveOccupancy
 */

@Service
public class PredictiveOccupancy {

    private final HistoricOccupancyRepository historicRepo;
    private final RealTimeOccupancyRepository repo;
    private Map<String, Model<Regressor>> models = new HashMap<>();

    public PredictiveOccupancy(RealTimeOccupancyRepository repo, HistoricOccupancyRepository historicRepo){
        this.historicRepo = historicRepo;
        this.repo = repo;

    }

    public void trainModel(String roomId){

        // 1. Fetch Historical records
        Optional<RoomOccupancyRecord> optionalRoom = historicRepo.findById(roomId);

        if(optionalRoom.isEmpty()){return;}

        RoomOccupancyRecord room = optionalRoom.get();

        if(room.getRecords() == null || room.getRecords().isEmpty()) return;

        List<OccupancyRecord> records = room.getRecords();

        long[] timestamps = records.stream()
                .mapToLong(OccupancyRecord::getTimestamp)
                .toArray();


        // 2. Map occupancy to a float value

        double[] y = records.stream()
                .map(OccupancyRecord::getOccupancyLevel)
                .mapToDouble(this::mapOccupancyToRatio)
                .toArray();


        // 3. Extract temporal features

        double[][] X = new double[timestamps.length][6];

        for (int i = 0; i < timestamps.length; i++) {

            LocalDateTime time = Instant.ofEpochMilli(timestamps[i]).atZone(ZoneId.systemDefault()).toLocalDateTime();

            int hour = time.getHour();
            int dayOfWeek = time.getDayOfWeek().getValue();

            // Cyclical Encoding
            double hourSin = Math.sin(2 * Math.PI * hour / 24.0);
            double hourCos = Math.cos(2 * Math.PI * hour / 24.0);

            double daySin = Math.sin(2 * Math.PI * dayOfWeek / 7.0);
            double dayCos = Math.cos(2 * Math.PI * dayOfWeek / 7.0);

            double isWeekend = (dayOfWeek >= 6) ? 1.0 : 0.0;

            X[i][0] = hourSin;
            X[i][1] = hourCos;
            X[i][2] = daySin;
            X[i][3] = dayCos;
            X[i][4] = isWeekend;
            X[i][5] = time.getMonthValue();
        }

        // 4. Build Tribuo Dataset

        List<Example<Regressor>> examples = new ArrayList<>();

        for (int i = 0; i < X.length; i++) {

            // Target value
            Regressor target = new Regressor("occupancy", y[i]);

            // Create example
            ArrayExample<Regressor> example = new ArrayExample<>(target);

            // Add features
            example.add(new Feature("hourSin", X[i][0]));
            example.add(new Feature("hourCos", X[i][1]));
            example.add(new Feature("daySin", X[i][2]));
            example.add(new Feature("dayCos", X[i][3]));
            example.add(new Feature("isWeekend", X[i][4]));
            example.add(new Feature("month", X[i][5]));

            examples.add(example);
        }

        Model<Regressor> model = getRegressorModel(examples);

        models.put(roomId, model);
    }

    private static Model<Regressor> getRegressorModel(List<Example<Regressor>> examples) {
        RegressionFactory factory = new RegressionFactory();

        ListDataSource<Regressor> source =
                new ListDataSource<>(
                        examples,
                        factory,
                        new SimpleDataSourceProvenance("occupancy-training", factory)
                );

        MutableDataset<Regressor> dataset = new MutableDataset<>(source);

        // 5. Train model
        // Create a regression tree trainer for RandomForest
        int maxDepth = 5;                     // max depth of each tree
        RandomForestTrainer<Regressor> rfTrainer = getRegressorRandomForestTrainer(maxDepth);

        return rfTrainer.train(dataset);
    }

    private static RandomForestTrainer<Regressor> getRegressorRandomForestTrainer(int maxDepth) {
        float minChildWeight = 1.0f;          // minimum weight for child node
        float minImpurityDecrease = 0.0f;     // no minimum impurity decrease
        CARTRegressionTrainer treeTrainer = getCartRegressionTrainer(maxDepth, minChildWeight, minImpurityDecrease);

        AveragingCombiner combiner = new AveragingCombiner();
        int numTrees = 50;

        return new RandomForestTrainer<>(treeTrainer, combiner, numTrees);
    }

    private static CARTRegressionTrainer getCartRegressionTrainer(int maxDepth, float minChildWeight, float minImpurityDecrease) {
        float fractionFeaturesInSplit = 0.5f; // <- crucial for RandomForest
        boolean useRandomSplitPoints = false;  // use random split points
        long seed = 123L;
        MeanSquaredError mse = new MeanSquaredError();
        return new CARTRegressionTrainer(
                maxDepth,
                minChildWeight,
                minImpurityDecrease,
                fractionFeaturesInSplit,
                useRandomSplitPoints,
                mse,
                seed
        );
    }

    public Map<Integer, Map<Integer, Double>> forecastNext7Days(String roomId) {
        Map<Integer, Map<Integer, Double>> forecast = new LinkedHashMap<>();

        // 1. Fetch last 7 days of real-time records
        Optional<RoomOccupancyRecord> optionalRoom = repo.findById(roomId);

        if (optionalRoom.isEmpty() || !models.containsKey(roomId)) {
            return forecast; // empty if no data or model
        }

        Model<Regressor> model = models.get(roomId);

        RoomOccupancyRecord recentRoomRecords = optionalRoom.get();

        // 2. Get the actual list of records
        List<OccupancyRecord> recentRecords = recentRoomRecords.getRecords();
        if (recentRecords == null || recentRecords.isEmpty()) {
            return forecast;
        }

        // 2. Loop through each record to predict occupancy
        for (OccupancyRecord record : recentRecords) {

            LocalDateTime time = Instant.ofEpochMilli(record.getTimestamp())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            int hour = time.getHour();
            int dayOfWeek = time.getDayOfWeek().getValue();

            double hourSin = Math.sin(2 * Math.PI * hour / 24.0);
            double hourCos = Math.cos(2 * Math.PI * hour / 24.0);

            double daySin = Math.sin(2 * Math.PI * dayOfWeek / 7.0);
            double dayCos = Math.cos(2 * Math.PI * dayOfWeek / 7.0);

            double isWeekend = (dayOfWeek >= 6) ? 1.0 : 0.0;

            double month = time.getMonthValue();

            // 3. Build a Tribuo example for prediction
            Regressor dummyTarget = new Regressor("occupancy", 0.0); // value not used
            ArrayExample<Regressor> example = new ArrayExample<>(dummyTarget);

            example.add(new Feature("hourSin", hourSin));
            example.add(new Feature("hourCos", hourCos));
            example.add(new Feature("daySin", daySin));
            example.add(new Feature("dayCos", dayCos));
            example.add(new Feature("isWeekend", isWeekend));
            example.add(new Feature("month", month));

            // 4. Predict occupancy
            Regressor prediction = model.predict(example).getOutput();
            double predictedOccupancy = prediction.getValues()[0]; // only target "occupancy"

            // 5. Store prediction by day -> hour
            forecast.computeIfAbsent(dayOfWeek, _ -> new LinkedHashMap<>())
                    .put(hour, predictedOccupancy);
        }

        return forecast;
    }


    private double mapOccupancyToRatio(Occupancy occupancy){
        return switch (occupancy) {
            case EMPTY -> 0.0;
            case LOW -> 0.25;
            case MEDIUM -> 0.5;
            case HIGH -> 0.85;
        };
    }
}
