import { ResponsiveContainer, LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip } from "recharts";
import { useMemo, useState } from "react";

export default function LineChartComponent() {
    const [mode, setMode] = useState("day");

    const busiestTime = [12,24,22]
    const busiestDay = ["Mon", "Tue", "Sun"]

    // dummy data - replaced with simulated/ real
    const dailyData = useMemo(
        () => [
        { day: "Mon", "Average Occupancy": 64 },
        { day: "Tue", "Average Occupancy": 34 },
        { day: "Wed", "Average Occupancy": 25 },
        { day: "Thu", "Average Occupancy": 45 },
        { day: "Fri", "Average Occupancy": 82 },
        { day: "Sat", "Average Occupancy": 60 },
        { day: "Sun", "Average Occupancy": 50 },
        ],
        []
    );

    const hourlyData = useMemo(
        () => [
        { hour: 0,  "Average Occupancy": 8 },
        { hour: 1,  "Average Occupancy": 6 },
        { hour: 2,  "Average Occupancy": 5 },
        { hour: 3,  "Average Occupancy": 4 },
        { hour: 4,  "Average Occupancy": 6 },
        { hour: 5,  "Average Occupancy": 10 },
        { hour: 6,  "Average Occupancy": 16 },
        { hour: 7,  "Average Occupancy": 28 },
        { hour: 8,  "Average Occupancy": 45 },
        { hour: 9,  "Average Occupancy": 52 },
        { hour: 10, "Average Occupancy": 60 },
        { hour: 11, "Average Occupancy": 70 },
        { hour: 12, "Average Occupancy": 78 },
        { hour: 13, "Average Occupancy": 72 },
        { hour: 14, "Average Occupancy": 68 },
        { hour: 15, "Average Occupancy": 60 },
        { hour: 16, "Average Occupancy": 55 },
        { hour: 17, "Average Occupancy": 60 },
        { hour: 18, "Average Occupancy": 65 },
        { hour: 19, "Average Occupancy": 58 },
        { hour: 20, "Average Occupancy": 42 },
        { hour: 21, "Average Occupancy": 30 },
        { hour: 22, "Average Occupancy": 20 },
        { hour: 23, "Average Occupancy": 15 },
        ],
        []
    );

    
    const chartData = mode === "day" ? dailyData : hourlyData;
    const xKey = mode === "day" ? "day" : "hour";
    const xLabel = mode === "day" ? "Day of Week" : "Time (0-24)";
    const xTickFormatter = mode === "day" ? undefined : (v) => `${v}:00`;
    const formatHour = (h) => (h === 24 ? "00:00" : `${h}:00`);


    return (
        <div>
            <div className="chart-container">
                <ResponsiveContainer width="100%" height= "100%">
                    <LineChart data={chartData} margin={{ top: 10, right: 20, left: 20, bottom: 30 }}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis
                        dataKey={xKey}
                        type={mode === "hour" ? "number" : "category"}
                        domain={mode === "hour" ? [0, 23] : undefined}
                        ticks={
                            mode === "hour"
                            ? [0,2,4,6,8,10,12,14,16,18,20,22]
                            : undefined
                        }
                        tickFormatter={xTickFormatter}
                        label={{ value: xLabel, position: "insideBottom", offset: -5 }}
                    />
                    <YAxis
                        domain={[0, 100]}
                        tickCount={11}
                        label={{ 
                            value: "Occupancy Level", 
                            angle: -90, 
                            dx: -20,
                            position: "outsideLeft" 
                        }}/>
                    <Tooltip
                        labelFormatter={(label) =>
                            mode === "day" ? label : `Hour: ${label}:00`
                        }
                    />
                    <Line type="monotone" dataKey="Average Occupancy" stroke="#82ca9d" />
                    </LineChart>
            </ResponsiveContainer>
            </div>

            <div className="ui-row-center" style={{ justifyContent: "center", marginTop: 12 }}>
                <button
                    type="button"
                    onClick={() => setMode("day")}
                    className={`ui-button ${mode === "day" ? "active" : ""}`}
                    >
                    By Day
                    </button>

                    <button
                    type="button"
                    onClick={() => setMode("hour")}
                    className={`ui-button ${mode === "hour" ? "active" : ""}`}
                    >
                    By Time
                    </button>
            </div>

            <div className="ui-row-wrap">
                <strong className="text-sm-strong">Busiest Days:</strong>
                {busiestDay.map((d) => (
                    <span key={d} className="ui-chip">{d}</span>
                ))}
                </div>

                <div style={{ width: 1, height: 18, background: "#eee" }} />

                <div style={{ display: "flex", gap: 8, alignItems: "center", flexWrap: "wrap" }}>
                <strong style={{ fontSize: 13 }}>Busiest Times:</strong>
                {busiestTime.map((t, i) => (
                    <span key={`${t}-${i}`} className="ui-chip">
                        {formatHour(t)}
                    </span>
                ))}
                </div>
            </div>
        
  );
}


