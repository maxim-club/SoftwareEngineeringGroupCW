import { useEffect, useMemo, useState, useRef } from "react";
import { MapContainer, TileLayer, Marker, Popup, useMap, useMapEvents } from "react-leaflet";
import L from "leaflet";
import { SPACES } from "../spacesDummy";

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

const selectedStudyIcon = L.icon({
    iconUrl: "/icons/study-selected.png", 
    iconSize: [36, 36],                  
    iconAnchor: [18, 36],
    popupAnchor: [0, -36],
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

function FlyToSelected({ selectedSpace }) {
    const map = useMap();

    useEffect(() => {
        if (!selectedSpace?.lat || !selectedSpace?.lng) return;
        map.flyTo([selectedSpace.lat, selectedSpace.lng], 18, { animate: true });
    }, [selectedSpace, map]);

    return null;
}

function MapClickCatcher({ onMapClick }) {
  useMapEvents({
    click: () => onMapClick?.(),
  });
  return null;
}

export default function StudySpacesMap({ spaces, selectedSpace, onSelectSpace, onMapClick }) {
    const [userLoc, setUserLoc] = useState(null);
    const markerById = useRef(new Map());

    const defaultCenter = useMemo(() => ({ lat: 51.3780, lng: -2.3270 }), []);
    const center = userLoc ?? defaultCenter;

    const studySpaces = (spaces && spaces.length > 0) ? spaces : SPACES;

    useEffect(() => {
        if (!navigator.geolocation) {
        console.error("Geolocation not supported");
        return;
        }

        navigator.geolocation.getCurrentPosition(
        (pos) => {
            setUserLoc({
            lat: pos.coords.latitude,
            lng: pos.coords.longitude,
            });
        },
        (err) => {
            console.error("Geolocation error:", err);
        },
        { enableHighAccuracy: true, timeout: 8000 }
        );
    }, []);

    useEffect(() => {
        if (!selectedSpace?.id) return;
        const marker = markerById.current.get(selectedSpace.id);
        if (marker) marker.openPopup();
    }, [selectedSpace]);

    return (
        <MapContainer center={center} zoom={16} style={{ height: "100%", width: "100%" }}>
        <InvalidateSizeOnMount />

        <MapClickCatcher onMapClick={() => {
            onMapClick?.();
        }} />

        {/* if user location updates, recenter only when no search selection */}
        {!selectedSpace && <Recenter center={center} />}

        {/* when selectedSpace changes, fly to it */}
        <FlyToSelected selectedSpace={selectedSpace} />

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
            <Marker 
                key={s.id} 
                position={{ lat: s.lat, lng: s.lng }} 
                icon={s.id === selectedSpace?.id ? selectedStudyIcon : studyIcon}
                ref={(m) => {
                    if (m) markerById.current.set(s.id, m);
                }}
                eventHandlers={{
                    click: () => onSelectSpace?.(s), 
                }}
            >
                <Popup>
                    <strong>{s.roomLocation}</strong>
                    {!!s.building && <div style={{ fontSize: 12, color: "#6c757d" }}>{s.building}</div>}

                    {Array.isArray(s.amenities) && s.amenities.length > 0 && (
                    <div style={{ marginTop: 8, display: "flex", gap: 6, flexWrap: "wrap" }}>
                        {s.amenities.slice(0, 6).map((t) => (
                        <span
                            key={t}
                            style={{
                            fontSize: 12,
                            padding: "4px 8px",
                            borderRadius: 999,
                            background: "#EEF2F7",
                            }}
                        >
                            {t}
                        </span>
                        ))}
                    </div>
                    )}
                </Popup>
            </Marker>
        ))}
    </MapContainer>
  );
}