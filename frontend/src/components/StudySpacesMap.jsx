import { useEffect, useMemo, useState } from "react";
import { MapContainer, TileLayer, Marker, Popup, useMap } from "react-leaflet";
import L from "leaflet";

// object can be changed according to the real data
const dummyStudySpaces = [
    { id: 1, name: "Library 1st Floor", lat: 51.3781, lng: -2.3273, tags: ["quiet", "power sockets"] },
    { id: 2, name: "Student Hub", lat: 51.3775, lng: -2.3282, tags: ["group", "food nearby"] },
];

const studyIcon = L.icon({
    iconUrl: "/icons/study.png",
    iconSize: [32, 32],
    iconAnchor: [16, 32],
    popupAnchor: [0, -32],
});

const userIcon = L.icon({
    iconUrl: "/icons/user.png",
    iconSize: [32, 32],
    iconAnchor: [16, 32],
    popupAnchor: [0, -32],
});

function Recenter({ center }) {
    const map = useMap();
    useEffect(() => {
        if (center) map.setView(center, map.getZoom(), { animate: true });
    }, [center, map]);
    return null;
}

function InvalidateSizeOnMount() {
    const map = useMap();
    useEffect(() => {
        const t = setTimeout(() => map.invalidateSize(), 0);
        return () => clearTimeout(t);
    }, [map]);
    return null;
}

export default function StudySpacesMap() {
    const [studySpaces, setStudySpaces] = useState([]);
    const [userLoc, setUserLoc] = useState(null);
    
    const defaultCenter = useMemo(() => ({ lat: 51.3780, lng: -2.3270 }), []);
    
    useEffect(() => {
        setStudySpaces(dummyStudySpaces);
    }, []);

    useEffect(() => {
        if (!navigator.geolocation) return;
            navigator.geolocation.getCurrentPosition(
                (pos) => setUserLoc({ lat: pos.coords.latitude, lng: pos.coords.longitude }),
                () => {},
                { enableHighAccuracy: true, timeout: 8000 }
        );
    }, []);

    const center = userLoc ?? defaultCenter;


    return (
        <div className="ui-card">
            <div className="ui-row-between">
                <div>
                    <div className="text-title">Study Spaces Map</div>
                    <div className="text-muted">
                        Map shows your location and study spaces
                    </div>
                </div>

            <div className="ui-row-center">
                <button
                    className="ui-button"
                    onClick={() => {
                        if (!navigator.geolocation) return;
                        navigator.geolocation.getCurrentPosition((pos) => {
                        setUserLoc({ lat: pos.coords.latitude, lng: pos.coords.longitude });
                        });
                    }}
                    >
                    Recenter to me
                </button>
                <span className="ui-chip">
                    {studySpaces.length} spaces
                </span>

                <span className="ui-chip">
                    {userLoc ? "Your Location On" : "Your Location Off"}
                </span>
            </div>
        </div>

        <div className="map-container">
            <MapContainer center={center} zoom={16} style={{ height: "100%", width: "100%" }}>
            <InvalidateSizeOnMount />
            <Recenter center={center} />

            <TileLayer
                attribution='&copy; OpenStreetMap contributors'
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />

            {userLoc && (
                <Marker position={userLoc} icon={userIcon}>
                <Popup>You are here</Popup>
                </Marker>
            )}

            {studySpaces.map((s) => (
                <Marker key={s.id} position={{ lat: s.lat, lng: s.lng }} icon={studyIcon}>
                <Popup>
                    <strong>{s.name}</strong>
                    {Array.isArray(s.tags) && s.tags.length > 0 && (
                    <div className="ui-row-wrap" style={{ marginTop: 8 }}>
                        {s.tags.map((t) => (
                        <span key={t} className="ui-chip">
                            {t}
                        </span>

                        ))}
                    </div>
                    )}
                </Popup>
                </Marker>
            ))}
            </MapContainer>
        </div>

        <div className="ui-row-wrap" style={{ marginTop: 12 }}>
            <strong className="text-sm-strong">Tips:</strong>
            <span className="ui-chip">Click a pin for details</span>
            <span className="ui-chip">Allow location to show “Your” pin</span>
        </div>
    </div>
  );
}
