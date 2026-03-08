import { ResponsiveContainer, LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from "recharts";
import { useMemo, useState } from "react";

export default function LineChartComponent() {
    const [mode, setMode] = useState("day");

    const busiestTime = [12,24,22]
    const busiestDay = ["Mon", "Tue", "Sun"]

    // dummy data - replaced with simulated/ real
    const dailyData = useMemo(
    () => [
        { day: "Mon", actual: 64, predicted: null },
        { day: "Tue", actual: 34, predicted: null },
        { day: "Wed", actual: 25, predicted: null },
        { day: "Thu", actual: null, predicted: 48 },
        { day: "Fri", actual: null, predicted: 76 },
        { day: "Sat", actual: null, predicted: 66 },
        { day: "Sun", actual: null, predicted: 58 },
    ],
    []
    );

    const hourlyData = useMemo(
    () => [
        { hour: 0, actual: 8, predicted: null },
        { hour: 1, actual: 6, predicted: null },
        { hour: 2, actual: 5, predicted: null },
        { hour: 3, actual: 4, predicted: null },
        { hour: 4, actual: 6, predicted: null },
        { hour: 5, actual: 10, predicted: null },
        { hour: 6, actual: 16, predicted: null },
        { hour: 7, actual: 28, predicted: null },
        { hour: 8, actual: 45, predicted: null },
        { hour: 9, actual: 52, predicted: null },
        { hour: 10, actual: 60, predicted: null },
        { hour: 11, actual: 70, predicted: null },
        { hour: 12, actual: 78, predicted: null },
        { hour: 13, actual: 72, predicted: null },
        { hour: 14, actual: 68, predicted: null },
        { hour: 15, actual: null, predicted: 62 },
        { hour: 16, actual: null, predicted: 58 },
        { hour: 17, actual: null, predicted: 64 },
        { hour: 18, actual: null, predicted: 69 },
        { hour: 19, actual: null, predicted: 61 },
        { hour: 20, actual: null, predicted: 47 },
        { hour: 21, actual: null, predicted: 35 },
        { hour: 22, actual: null, predicted: 24 },
        { hour: 23, actual: null, predicted: 18 },
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
                <ResponsiveContainer width="100%" height="100%">
                    <LineChart data={chartData} margin={{ top: 10, right: 20, left: 20, bottom: 80 }}>
                    <CartesianGrid strokeDasharray="3 3" stroke="#bfc5cc"/>
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
                        label={{ value: xLabel, position: "insideBottom", offset: 0 }}
                        axisLine={{ stroke: "#666", strokeWidth: 1 }}
                        tickLine={{ stroke: "#666", strokeWidth: 1 }}
                    />
                    <YAxis
                        domain={[0, 100]}
                        tickCount={11}
                        label={{ 
                            value: "Occupancy Level", 
                            angle: -90, 
                            dx: -20,
                            position: "outsideLeft" 
                        }}
                        axisLine={{ stroke: "#666", strokeWidth: 1.5 }}
                        tickLine={{ stroke: "#666", strokeWidth: 1 }}
                    />
                    <Tooltip
                        labelFormatter={(label) =>
                            mode === "day" ? label : `Hour: ${label}:00`
                        }
                    />
                    <Legend />
                    <Line
                        type="monotone"
                        dataKey="actual"
                        name="Actual Occupancy"
                        stroke="#82ca9d"
                        strokeWidth={3}
                        dot={{ r: 5, fill: "#fff", stroke: "#82ca9d", strokeWidth: 3 }}
                        activeDot={{ r: 6, fill: "#fff", stroke: "#82ca9d", strokeWidth: 3 }}
                        />

                    <Line
                        type="monotone"
                        dataKey="predicted"
                        name="Predicted Occupancy"
                        stroke="#ff7300"
                        strokeWidth={3}
                        strokeDasharray="7 5"
                        connectNulls={false}
                        dot={{
                            r: 6,
                            fill: "#ff7300",
                            stroke: "#f5f5f5",   // same as chart/card background
                            strokeWidth: 4
                        }}
                        activeDot={{
                            r: 7,
                            fill: "#ff7300",
                            stroke: "#f5f5f5",
                            strokeWidth: 4
                        }}
                        />
                    </LineChart>
            </ResponsiveContainer>
            </div>

            <div className="ui-row-center" style={{ justifyContent: "center", marginTop: 12 }}>
                <button
                    type="button"
                    onClick={() => setMode("day")}
                    className={`ui-button ${mode === "day" ? "active" : ""}`}
                    >
                    By Week
                    </button>

                    <button
                    type="button"
                    onClick={() => setMode("hour")}
                    className={`ui-button ${mode === "hour" ? "active" : ""}`}
                    >
                    By Day
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


