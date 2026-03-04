import React, { useState, useEffect, useRef, useMemo } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Badge, Nav, Collapse, Spinner } from 'react-bootstrap';
import useGoToCheckin from "../hooks/useGoToCheckin";
import { SPACES } from "../spacesDummy";
import './SpaceDetailPage.css';
import 'leaflet/dist/leaflet.css';

import { 
  IoChevronBack,
  IoBookmarkOutline, 
  IoShareSocialOutline, 
  IoEllipsisHorizontal,
  IoRestaurantOutline,
  IoWifiOutline,
  IoPrintOutline,
  IoSunnyOutline,
  IoAccessibilityOutline,
  IoPeopleOutline,
  IoLocationOutline,
  IoTimeOutline,
  IoGridOutline,
  IoPersonOutline,
  IoWalkOutline
} from 'react-icons/io5';

import { 
  MdMeetingRoom 
} from 'react-icons/md';

import OccupancyLineChart from './Line-trial';

function SpaceDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const goToCheckin = useGoToCheckin();
  const reviews = [
    {
        id: 1,
        name: "Sophie Henderson",
        course: "BSc Computer Science",
        date: "Today",
        time: "07:20 AM",
        comment: "Love studying here in the mornings! Quiet atmosphere, good coffee, and plenty of outlets by the window seats ☕💻"
    },
    {
        id: 2,
        name: "James Chen",
        course: "MSc International Business",
        date: "12-28-2026",
        time: "15:34 PM",
        comment: "The place is very nice and comfortable. Great for group project meetings!"
    },
    {
        id: 3,
        name: "Emily Watson",
        course: "BA Architecture",
        date: "12-28-2026",
        time: "20:03 PM",
        comment: "Bit noisy today but the natural light is amazing for sketching. Soy latte is ✨"
    },
    {
        id: 4,
        name: "Merrill Kervin",
        course: "BSc Management and marketing",
        date: "12-27-2026",
        time: "10:29 AM",
        comment: "Perfect spot between lectures. Grabbed a bacon roll and caught up on readings. Only downside - gets loud when it's packed 📚"
    },
    {
        id: 5,
        name: "Priya Patel",
        course: "BSc Economics",
        date: "12-27-2026",
        time: "14:52 PM",
        comment: "Early morning study session here is the best!"
    },
    {
        id: 6,
        name: "Oliver Thompson",
        course: "BSc Accounting and Finance",
        date: "12-26-2026",
        time: "17:40 PM",
        comment: "Tried to work on my dissertation here but a bit too chatty for deep focus work 😅"
    },
    {
        id: 7,
        name: "Maya Johnson",
        course: "MSc Management",
        date: "12-24-2026",
        time: "16:47 PM",
        comment: "The vibe here is immaculate ✨ Not too quiet that you feel awkward, perfect background noise for productivity"
    },
    {
        id: 8,
        name: "Lucas Martinez",
        course: "BSc Management and marketing",
        date: "12-24-2026",
        time: "16:47 PM",
        comment: "The vibe here is immaculate ✨"
    }
    ];

  const [spaces, setSpaces] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('info');
  const [hoursExpanded, setHoursExpanded] = useState(true);
  const [userLocation, setUserLocation] = useState(null);

    useEffect(() => {
        setLoading(true);
        fetch('http://localhost:8080/api/spaces')
            .then((res) => res.json())
            .then((data) => {
                setSpaces(data);
            })
            .catch((err) => console.error("Error fetching detail:", err))
            .finally(() => setLoading(false));
    }, []);  const space = useMemo(
        () => spaces.find((s) => String(s.id) === String(id) || String(s._id) === String(id)),
        [id, spaces]
    );

  // Get user location
  useEffect(() => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          setUserLocation({
            lat: position.coords.latitude,
            lng: position.coords.longitude
          });
        },
        (error) => {
          console.log('Location access denied');
        }
      );
    }
  }, []);

    if (loading) {
        return (
            <Container className="d-flex justify-content-center align-items-center" style={{ height: '100vh' }}>
                <Spinner animation="border" variant="primary" />
            </Container>
        );
    }

    if (!space) {
        return <Container><h1>Space not found</h1></Container>;
    }
    const currentSpace = {
        id: space._id || space.id,
        name: space.roomLocation,
        building: space.building || "Campus Building",
        address: space.address ?? "Claverton Down",
        // FIX: Map from the nested coordinates object
        coordinates: {
            latitude: space.coordinates?.latitude || 51.3782,
            longitude: space.coordinates?.longitude || -2.3264,
        },
        rating: space.rating || 4.5,
        reviewCount: space.reviewCount || 12,
        distance: space.distance || "5 min",
        walkTime: space.walkTime || "400m",
        about: space.notes || space.about || "No description yet.",
        occupancy: space.occupancy,
        amenities: space.amenities,
        // FIX: Match the casing from your JSON (imageURL)
        imageUrl: space.imageURL,
    };

  return (
    <div className="space-detail-page">
      {/* Header with Image */}
      <div className="detail-header">
        <button className="back-button" onClick={() => navigate(-1)}>
          <IoChevronBack size={24} />
        </button>
        <div className="header-actions">
          <button className="icon-button">
            <IoBookmarkOutline size={20} />
          </button>
          <button className="icon-button">
            <IoShareSocialOutline size={20} />
          </button>
          <button className="icon-button">
            <IoEllipsisHorizontal size={20} />
          </button>
        </div>
        
        {/* HEADER IMAGE - using space ID to load corresponding image, with fallback to default */}
        <img
          src={currentSpace.imageUrl || `/images/spaces/${id}.jpg`}
          alt={currentSpace.name}
          className="header-image"
          onError={(e) => {
            e.currentTarget.src = "/images/spaces/default-space.jpg";
          }}
        />
      </div>

      <Container className="detail-content">
        <div className="title-section">
          <h1 className="space-title">{currentSpace.name}</h1>
          <p className="space-location">{currentSpace.building}, {currentSpace.address}</p>
        </div>

        <div className="stats-section">
          <div className="rating">
            <span className="rating-number">{currentSpace.rating}</span>
            <span className="stars">⭐⭐⭐⭐⭐</span>
            <span className="review-count">({currentSpace.reviewCount} reviews)</span>
          </div>
          <div className="badges-row">
            <Badge bg="primary">{currentSpace.occupancy || "Available"}</Badge>
            <span className="stat-item">
              <IoLocationOutline size={16} />
              {currentSpace.distance}
            </span>
            <span className="stat-item">
              <IoWalkOutline size={16} />
              {currentSpace.walkTime}
            </span>
          </div>
        </div>

        <Nav variant="tabs" className="detail-tabs">
          <Nav.Item>
            <Nav.Link active={activeTab === 'info'} onClick={() => setActiveTab('info')}>
              Info
            </Nav.Link>
          </Nav.Item>
          <Nav.Item>
            <Nav.Link active={activeTab === 'reviews'} onClick={() => setActiveTab('reviews')}>
              Reviews
            </Nav.Link>
          </Nav.Item>
          <Nav.Item>
            <Nav.Link active={activeTab === 'checkins'} onClick={() => setActiveTab('checkins')}>
              My Check-ins
            </Nav.Link>
          </Nav.Item>
        </Nav>

        {activeTab === 'info' && (
          <div className="info-tab">
            <section className="about-section">
              <h3>About</h3>
              <p className="about-text">
                {currentSpace.about}
                <span className="read-more"> Read more...</span>
              </p>
            </section>

            <section className="chart-section">
              <h3>Typical Occupancy</h3>
              <OccupancyLineChart spaceId={id} />
            </section>

            <section className="hours-section">
              <div className="hours-header" onClick={() => setHoursExpanded(!hoursExpanded)}>
                <div className="hours-title">
                  <IoTimeOutline size={20} />
                  <span className="open-status">Open</span>
                  <span className="open-days">Weekdays</span>
                </div>
                <span>{hoursExpanded ? '▲' : '▼'}</span>
              </div>
              <Collapse in={hoursExpanded}>
                <div className="hours-list">
                  {['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'].map(day => (
                    <div className="hour-row" key={day}>
                      <span className="day">{day}</span>
                      <span className="time">08:00 - 17:00</span>
                    </div>
                  ))}
                  {['Saturday', 'Sunday'].map(day => (
                    <div className="hour-row" key={day}>
                      <span className="day">{day}</span>
                      <span className="time closed">closed</span>
                    </div>
                  ))}
                </div>
              </Collapse>
            </section>

            <section className="amenities-section">
              <h3>Amenities</h3>
              <div className="amenities-grid">
                <div className="amenity-item">
                  <MdMeetingRoom className="amenity-icon" size={24} />
                  <span>Restrooms</span>
                </div>
                <div className="amenity-item">
                  <IoPeopleOutline className="amenity-icon" size={24} />
                  <span>Group area</span>
                </div>
                <div className="amenity-item">
                  <IoRestaurantOutline className="amenity-icon" size={24} />
                  <span>Restaurant</span>
                </div>
                <div className="amenity-item">
                  <IoWifiOutline className="amenity-icon blue" size={24} />
                  <span>Wi-Fi</span>
                </div>
                <div className="amenity-item">
                  <IoPrintOutline className="amenity-icon" size={24} />
                  <span>Printer</span>
                </div>
                <div className="amenity-item">
                  <IoSunnyOutline className="amenity-icon blue" size={24} />
                  <span>Natural light</span>
                </div>
                <div className="amenity-item">
                    <IoGridOutline className="amenity-icon" size={24} />
                    <span>Whiteboard</span>
                </div>
                <div className="amenity-item">
                  <IoAccessibilityOutline className="amenity-icon blue" size={24} />
                  <span>Wheelchair</span>
                </div>
              </div>
            </section>

            <section className="location-section">
              <h3>Location</h3>
              <p className="location-address">
                <IoLocationOutline className="location-pin" size={20} />
                {currentSpace.building}, {currentSpace.address}
              </p>
              <SingleLocationMap space={currentSpace} userLocation={userLocation} />
            </section>
          </div>
        )}

        {activeTab === 'reviews' && (
        <div className="reviews-tab" style={{ padding: '20px 0' }}>
            {reviews.length === 0 ? (
            <p>No reviews yet</p>
            ) : (
            <div className="reviews-list">
                {reviews.map((review) => (
                <div 
                    className="review-item" 
                    key={review.id}
                    style={{
                    padding: '20px 0',
                    borderBottom: '1px solid #f0f0f0'
                    }}
                >
                    <div style={{ display: 'flex', gap: '12px', marginBottom: '12px' }}>
                    <div 
                        style={{
                        width: '44px',
                        height: '44px',
                        borderRadius: '50%',
                        background: '#5B9FED',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center'
                        }}
                    >
                        <IoPersonOutline size={24} color="#fff" />
                    </div>
                    <div style={{ flex: 1 }}>
                        <div style={{ fontSize: '15px', fontWeight: '600' }}>
                        {review.name}
                        </div>
                        <div style={{ fontSize: '13px', color: '#6c757d' }}>
                        {review.course}
                        </div>
                    </div>
                    <div style={{ textAlign: 'right' }}>
                        <div style={{ fontSize: '13px', fontWeight: '500' }}>
                        {review.date}
                        </div>
                        <div style={{ fontSize: '12px', color: '#6c757d' }}>
                        {review.time}
                        </div>
                    </div>
                    </div>
                    <div style={{ fontSize: '14px', paddingLeft: '56px' }}>
                    {review.comment}
                    </div>
                </div>
                ))}
            </div>
            )}
        </div>
        )}

        {activeTab === 'checkins' && (
          <div className="checkins-tab">
            <p className="text-muted">Your check-ins will appear here</p>
          </div>
        )}
      </Container>

      <div className="fixed-bottom-button">
        <button
          className="check-in-button"
          onClick={() => goToCheckin(currentSpace)}
        >
          Check-in now
        </button>
      </div>
    </div>
  );
}



function SingleLocationMap({ space, userLocation }) {
  const mapRef = useRef(null);
  const mapInstanceRef = useRef(null);
  const spaceMarkerRef = useRef(null);
  const userMarkerRef = useRef(null);

  useEffect(() => {
    if (!mapInstanceRef.current && mapRef.current) {
      const L = require('leaflet');
      
      delete L.Icon.Default.prototype._getIconUrl;
      L.Icon.Default.mergeOptions({
        iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
        iconUrl: require('leaflet/dist/images/marker-icon.png'),
        shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
      });

      const map = L.map(mapRef.current).setView(
        [space.coordinates.latitude, space.coordinates.longitude], 
        15
      );

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap'
      }).addTo(map);

      const marker = L.marker([space.coordinates.latitude, space.coordinates.longitude])
        .addTo(map)
        .bindPopup(`<b>${space.name}</b>`)
        .openPopup();

      mapInstanceRef.current = map;
      spaceMarkerRef.current = marker;
    }

    return () => {
      if (mapInstanceRef.current) {
        mapInstanceRef.current.remove();
        mapInstanceRef.current = null;
      }
    };
  }, [space.coordinates.latitude, space.coordinates.longitude, space.name]);

  useEffect(() => {
    if (mapInstanceRef.current && userLocation) {
      const L = require('leaflet');
      
      if (userMarkerRef.current) {
        mapInstanceRef.current.removeLayer(userMarkerRef.current);
      }

      const marker = L.circleMarker([userLocation.lat, userLocation.lng], {
        radius: 8,
        fillColor: '#4285F4',
        color: '#fff',
        weight: 2,
        opacity: 1,
        fillOpacity: 0.8
      }).addTo(mapInstanceRef.current).bindPopup('You are here');

      userMarkerRef.current = marker;

      const bounds = L.latLngBounds([
        [space.coordinates.latitude, space.coordinates.longitude],
        [userLocation.lat, userLocation.lng]
      ]);
      mapInstanceRef.current.fitBounds(bounds, { padding: [50, 50] });
    }
  }, [userLocation, space.coordinates.latitude, space.coordinates.longitude]);

  return (
    <div 
      ref={mapRef}
      style={{ 
        width: '100%', 
        height: '300px', 
        borderRadius: '12px',
        overflow: 'hidden'
      }}
    />
  );
}

export default SpaceDetailPage;