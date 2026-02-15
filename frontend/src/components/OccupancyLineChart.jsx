import { ResponsiveContainer, LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip } from "recharts";
import { useMemo, useState } from "react";

export default function LineChartComponent() {
    const [mode, setMode] = useState("day");

    // dummy data - replaced with simulated/ real
    const dailyData = useMemo(
        () => [
        { day: "Mon", "Peak Occupancy": 70, "Average Occupancy": 64 },
        { day: "Tue", "Peak Occupancy": 89, "Average Occupancy": 34 },
        { day: "Wed", "Peak Occupancy": 43, "Average Occupancy": 25 },
        { day: "Thu", "Peak Occupancy": 56, "Average Occupancy": 45 },
        { day: "Fri", "Peak Occupancy": 93, "Average Occupancy": 82 },
        { day: "Sat", "Peak Occupancy": 78, "Average Occupancy": 60 },
        { day: "Sun", "Peak Occupancy": 65, "Average Occupancy": 50 },
        ],
        []
    );

    const hourlyData = useMemo(
        () => [
        { hour: 0,  "Peak Occupancy": 10, "Average Occupancy": 8 },
        { hour: 1,  "Peak Occupancy": 8,  "Average Occupancy": 6 },
        { hour: 2,  "Peak Occupancy": 6,  "Average Occupancy": 5 },
        { hour: 3,  "Peak Occupancy": 5,  "Average Occupancy": 4 },
        { hour: 4,  "Peak Occupancy": 7,  "Average Occupancy": 6 },
        { hour: 5,  "Peak Occupancy": 12, "Average Occupancy": 10 },
        { hour: 6,  "Peak Occupancy": 20, "Average Occupancy": 16 },
        { hour: 7,  "Peak Occupancy": 35, "Average Occupancy": 28 },
        { hour: 8,  "Peak Occupancy": 55, "Average Occupancy": 45 },
        { hour: 9,  "Peak Occupancy": 65, "Average Occupancy": 52 },
        { hour: 10, "Peak Occupancy": 75, "Average Occupancy": 60 },
        { hour: 11, "Peak Occupancy": 82, "Average Occupancy": 70 },
        { hour: 12, "Peak Occupancy": 90, "Average Occupancy": 78 },
        { hour: 13, "Peak Occupancy": 85, "Average Occupancy": 72 },
        { hour: 14, "Peak Occupancy": 80, "Average Occupancy": 68 },
        { hour: 15, "Peak Occupancy": 70, "Average Occupancy": 60 },
        { hour: 16, "Peak Occupancy": 65, "Average Occupancy": 55 },
        { hour: 17, "Peak Occupancy": 72, "Average Occupancy": 60 },
        { hour: 18, "Peak Occupancy": 78, "Average Occupancy": 65 },
        { hour: 19, "Peak Occupancy": 68, "Average Occupancy": 58 },
        { hour: 20, "Peak Occupancy": 50, "Average Occupancy": 42 },
        { hour: 21, "Peak Occupancy": 35, "Average Occupancy": 30 },
        { hour: 22, "Peak Occupancy": 25, "Average Occupancy": 20 },
        { hour: 23, "Peak Occupancy": 18, "Average Occupancy": 15 },
        { hour: 24, "Peak Occupancy": 12, "Average Occupancy": 10 },
        ],
        []
    );

    
    const chartData = mode === "day" ? dailyData : hourlyData;
    const xKey = mode === "day" ? "day" : "hour";
    const xLabel = mode === "day" ? "Day of Week" : "Time (0-24)";
    const xTickFormatter = mode === "day" ? undefined : (v) => `${v}:00`;


    return (
        <div>
            <div style={{ width: "100%", height: "clamp(240px, 35vh, 420px)"}}>
                <ResponsiveContainer width="100%" height="100%">
                    <LineChart data={chartData} margin={{ top: 10, right: 20, left: 20, bottom: 30 }}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis
                        dataKey={xKey}
                        type={mode === "hour" ? "number" : "category"}
                        domain={mode === "hour" ? [0, 24] : undefined}
                        ticks={
                            mode === "hour"
                            ? [0,2,4,6,8,10,12,14,16,18,20,22,24]
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
                        itemSorter={(item) =>
                            item.dataKey === "Peak Occupancy" ? -1 : 1
                        }
                        labelFormatter={(label) =>
                            mode === "day" ? label : `Hour: ${label}:00`
                        }
                    />
                    <Line type="monotone" dataKey="Peak Occupancy" stroke="#8884d8" activeDot={{ r: 8 }} />
                    <Line type="monotone" dataKey="Average Occupancy" stroke="#82ca9d" />
                    </LineChart>
            </ResponsiveContainer>
            </div>

            <div style={{ display: "flex", gap: 8, justifyContent: "center", marginTop: 12 }}>
                <button
                type="button"
                onClick={() => setMode("day")}
                style={{
                    padding: "8px 12px",
                    borderRadius: 8,
                    border: "1px solid #ccc",
                    background: mode === "day" ? "#e9ecef" : "white",
                    cursor: "pointer",
                }}
                >
                By Day
                </button>

                <button
                type="button"
                onClick={() => setMode("hour")}
                style={{
                    padding: "8px 12px",
                    borderRadius: 8,
                    border: "1px solid #ccc",
                    background: mode === "hour" ? "#e9ecef" : "white",
                    cursor: "pointer",
                }}
                >
                By Time
                </button>
            </div>
        </div>
  );
}


