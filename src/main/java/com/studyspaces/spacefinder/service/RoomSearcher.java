package com.studyspaces.spacefinder.service;
import com.studyspaces.spacefinder.repository.StudySpaceRepository;
import com.studyspaces.spacefinder.model.*;
import com.studyspaces.spacefinder.dto.SearchQueryRequest;
import org.springframework.data.util.Pair;

import java.util.*;
import java.lang.Float;
//This service will handle receiving a query from frontend and getting the best results

//initialising:
//Fetch ALL rooms from DB and for each room construct a vector corresponding to the parameters. Store in memory.

//search query:
//


public class RoomSearcher {

    private static StudySpaceRepository repository = null;
    private static HashMap<String, List<Pair<Integer, Float>>> searchSpace;
    public RoomSearcher(StudySpaceRepository repository) {
        RoomSearcher.repository = repository;
    }

    public static void initialiseSearchSpace(){
        searchSpace = new HashMap<>();
        //fetch everything from study space profile DB.

        List<StudySpaceProfile> allRoomData = repository.findAll();

        for (StudySpaceProfile room : allRoomData) {

            //Convert all study spaces into 2 components:
            //Filter query data & id

            //vectorise the filter query data from study space

            //Then make a HUGE hash map: HashMap<QueryVector, roomID>

            searchSpace.put(room.getId(), Vectorise(room.toFilterQuery()));
        }
    };
    //for testing
    public static HashMap<String, List<Pair<Integer, Float>>> getSearchSpace() {
        return searchSpace;
    }

    public static ArrayList<Pair<Integer, Float>> Vectorise(FilterQuery query){
        //Pair holds a weight 0 or 1 which determines if that parameter has any influence on KNN
        //Take a filter query and make it a vector of floats from 0-1.
        //If user is indifferent to an option then set the weight of the pair to 0

        ArrayList<Pair<Integer, Float>> output = new ArrayList<>();
        
        //first normalize all enums... 
        output.add(normalizeEnum(query.preferredNoiseLevel));
        output.add(normalizeEnum(query.preferredOccupancy));

        //normalize bool
        output.add(normalizeBool(query.preferredGroupSpace));

        if (query.preferredAmenities == null){
            //Make a list of 8 dummy values. 8 Since that is amount of ammenities.

            Pair<Integer, Float> dummy = Pair.of(0, 0f);
            for(int i = 0; i < 8; i++){
                output.add(dummy);
            }

        }else {
            List<Boolean> boolList = query.preferredAmenities.toList();

            //iterate through all amenities booleans
            for (Boolean b : boolList) {
                output.add(normalizeBool(b));
            }
        }

        //Normalize the group size int
        output.add(query.preferredGroupSize == null ? Pair.of(0, 0f) : Pair.of(1, query.preferredGroupSize / 10f));

        return output;
    }

    private static Pair<Integer, Float> normalizeEnum(Enum<?> e){
        Pair<Integer, Float> pair;
        //enum is null so user is indifferent. Make the weight zero
        if (e == null) {
            pair = Pair.of(0, 0f);
            return pair;
        }
        //normalize the int to range from 0 to 1.
        int max = e.getDeclaringClass().getEnumConstants().length - 1;
        if (max == 0){
            pair = Pair.of(1, 0f);
        } else{
            pair = Pair.of(1, (e.ordinal() / (float) max));
        }
        return pair;
    }
    
    private static Pair<Integer, Float> normalizeBool(Boolean b){
        Pair<Integer, Float> pair; 
        if (b == null){
            pair = Pair.of(0, 0f);
        } else if (b) {
            pair = Pair.of(1, 1f);
        }else{
            pair = Pair.of(1,0f);
        }
        return pair;
    }

    //returns 5 best recommendations as roomIds
    public static List<String> getKRecommended(FilterQuery query, int k){
        if(searchSpace == null){
            initialiseSearchSpace();
        }

        //Iterate through the search space map

        List<Pair<Integer, Float>> queryVector = Vectorise(query);

        PriorityQueue<Pair<String, Float>> heap =
                new PriorityQueue<>(
                        (a, b) -> Float.compare(b.getSecond(), a.getSecond())
                ); // max heap organised by (roomID, distanceFromSearchVector)

        for (HashMap.Entry<String, List<Pair<Integer, Float>>> entry : searchSpace.entrySet()){

            float dist = distance(queryVector, entry.getValue());
            heap.add(Pair.of(entry.getKey(), dist));


            if (heap.size() > k) {
                heap.poll(); // remove worst
            }

        }

        List<String> result = new ArrayList<>();

        while (!heap.isEmpty()) {
            result.add(heap.poll().getFirst());
        }

        Collections.reverse(result); // closest first
        return result;

    }

    private static float distance(List<Pair<Integer, Float>> vecA, List<Pair<Integer, Float>> vecB){
        float sum = 0f;

        for(int i = 0; i < vecA.size(); i++){
            Pair<Integer, Float> p1 = vecA.get(i);
            Pair<Integer, Float> p2 = vecB.get(i);

            // ignore dimension if either side disabled it
            if (p1.getFirst() == 0 || p2.getFirst() == 0)
                continue;

            float diff = p1.getSecond() - p2.getSecond();
            sum += diff * diff;
        }

        return (float) sum; //no need to waste resources computing sqrt
    }

}
