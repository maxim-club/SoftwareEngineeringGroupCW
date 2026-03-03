import React, { useMemo } from "react";
import { Card, Button } from "react-bootstrap";
import { FaStar } from "react-icons/fa";
import { FiChevronRight } from "react-icons/fi";
import { MdOutlineAccessible } from "react-icons/md";

const AmenityIcon = ({ k }) => {
  const style = { fontSize: 20, color: "#6c757d" };


  if (k === "wheelchairAccess") return <MdOutlineAccessible style={style} />;
  if (k === "monitor") return <span style={style}>🖥️</span>;
  if (k === "desks") return <span style={style}>🪑</span>;
  if (k === "powerOutlets") return <span style={style}>🔌</span>;
  if (k === "printer") return <span style={style}>🖨️</span>;
  if (k === "whiteboard") return <span style={style}>🧾</span>;
  if (k === "projectors") return <span style={style}>📽️</span>;
  if (k === "quietZone") return <span style={style}>🤫</span>;
  if (k === "naturalLights") return <span style={style}>🌤️</span>;

  return <span style={style}>•</span>;
};

function statusPillStyle(statusRaw) {
  const status = String(statusRaw || "").toLowerCase();


  if (status.includes("busy")) {
    return { background: "#FF5A5F", color: "#fff", border: "none" };
  }


  if (status.includes("available") || status.includes("available")) {
    return { background: "#22C55E", color: "#fff", border: "none" };
  }

 
  return {
    background: "#EEF2F7",
    color: "#495057",
    border: "1px solid #E9ECEF",
  };
}

export default function StudySpaceCard(props) {

  const space = props.space ?? null;

  const onCheckIn = props.onCheckIn ?? (() => {});

  const data = useMemo(() => {
    const title =
      space?.roomLocation ??
      space?.title ??
      space?.name ??
      props.title ??
      "Study space";
    const subtitle =
      space?.building ?? space?.subtitle ?? props.subtitle ?? "";

    const rating = space?.rating ?? props.rating ?? null;
    const reviewCount = space?.reviewCount ?? props.reviewCount ?? null;

    const status = space?.occupancy ?? space?.status ?? props.status ?? null;
    const distance = space?.distance ?? props.distance ?? null;
    const walkTime = space?.walkTime ?? props.walkTime ?? null;

    const imageUrl = space?.imageUrl ?? props.imageUrl ?? null;

    // amenities 
    let amenitiesKeys = [];
    const am = space?.amenities ?? props.amenities;
    if (Array.isArray(am)) amenitiesKeys = am;
    else if (am && typeof am === "object")
      amenitiesKeys = Object.keys(am).filter((k) => !!am[k]);

    return {
      title,
      subtitle,
      rating,
      reviewCount,
      status,
      distance,
      walkTime,
      imageUrl,
      amenitiesKeys,
    };
  }, [space, props]);


  const onViewInfo = props.onViewInfo ?? (() => {});
  const onBookRoom = props.onBookRoom ?? (() => {});

  const pill = statusPillStyle(data.status);


  const topAmenities = data.amenitiesKeys.slice(0, 4);

  return (
    <Card
      className="mb-3"
      style={{
        borderRadius: 18,
        border: "1px solid #E9ECEF",
        overflow: "hidden",
        boxShadow: "0 0 0 rgba(0,0,0,0)", 
      }}
    >
      <Card.Body style={{ padding: 16 }}>
        <div style={{ display: "flex", gap: 14, alignItems: "flex-start" }}>
          <div style={{ flex: 1, minWidth: 0 }}>
            <div style={{ fontSize: 18, fontWeight: 600, lineHeight: 1.2 }}>
              {data.title}
            </div>
            <div style={{ fontSize: 13, color: "#6c757d", marginTop: 4 }}>
              {data.subtitle}
            </div>

            {/* rating row */}
            <div
              style={{
                display: "flex",
                alignItems: "center",
                gap: 8,
                marginTop: 10,
              }}
            >
              {data.rating != null ? (
                <>
                  <div
                    style={{
                      fontSize: 13,
                      fontWeight: 700,
                      color: "#212529",
                    }}
                  >
                    {Number(data.rating).toFixed(1)}
                  </div>

                  <div style={{ display: "flex", gap: 3, color: "#F4B400" }}>
                    {Array.from({ length: 5 }).map((_, i) => (
                      <FaStar
                        key={i}
                        size={13}
                        style={{
                          opacity:
                            i < Math.floor(Number(data.rating)) ? 1 : 0.25,
                        }}
                      />
                    ))}
                  </div>

                  {data.reviewCount != null ? (
                    <div style={{ fontSize: 13, color: "#6c757d" }}>
                      ({data.reviewCount} reviews)
                    </div>
                  ) : null}
                </>
              ) : null}
            </div>

            {/* status + distance + walk */}
            <div
              style={{
                display: "flex",
                alignItems: "center",
                gap: 10,
                marginTop: 10,
              }}
            >
              {data.status ? (
                <span
                  style={{
                    fontSize: 12,
                    fontWeight: 500,
                    padding: "4px 10px",
                    borderRadius: 10,
                    ...pill,
                  }}
                >
                  {data.status}
                </span>
              ) : null}

              {data.distance ? (
                <span
                  style={{
                    fontSize: 12,
                    color: "#6c757d",
                    display: "inline-flex",
                    gap: 6,
                    alignItems: "center",
                  }}
                >
                  <span aria-hidden>📍</span> {data.distance}
                </span>
              ) : null}

              {data.walkTime ? (
                <span
                  style={{
                    fontSize: 12,
                    color: "#6c757d",
                    display: "inline-flex",
                    gap: 6,
                    alignItems: "center",
                  }}
                >
                  <span aria-hidden>🚶</span> {data.walkTime}
                </span>
              ) : null}
            </div>
          </div>

          {/* image */}
          <div
            style={{
              width: 110,
              height: 82,
              borderRadius: 14,
              overflow: "hidden",
              background: "#F1F3F5",
              flexShrink: 0,
            }}
          >
            {data.imageUrl ? (
              <img
                src={data.imageUrl}
                alt={data.title}
                style={{ width: "100%", height: "100%", objectFit: "cover" }}
              />
            ) : null}
          </div>
        </div>

        {/* divider */}
        <div style={{ height: 1, background: "#E9ECEF", margin: "14px 0" }} />

        {/* amenities row + book a room */}
        <div
          style={{
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
            gap: 12,
          }}
        >
          <div style={{ display: "flex", gap: 14, alignItems: "center" }}>
            {topAmenities.length > 0 ? (
              topAmenities.map((k) => <AmenityIcon key={k} k={k} />)
            ) : (
              <>
                <MdOutlineAccessible style={{ fontSize: 20, color: "#6c757d" }} />
                <span style={{ color: "#6c757d", fontSize: 12 }}>
                  No amenities
                </span>
              </>
            )}
          </div>

          <button
            type="button"
            onClick={onBookRoom}
            style={{
              border: "none",
              background: "transparent",
              color: "#0B5ED7",
              fontWeight: 500,
              fontSize: 13,
              display: "inline-flex",
              alignItems: "center",
              gap: 8,
              cursor: "pointer",
              padding: 0,
            }}
          >
            book a room <FiChevronRight />
          </button>
        </div>

        {/* bottom buttons: View info & Check-in */}
        <div style={{ display: "flex", gap: 12, marginTop: 14 }}>
          <Button
            type="button"
            variant="outline-primary"
            onClick={onViewInfo} 
            style={{
              flex: 1,
              borderRadius: 999,
              height: 44,
              fontWeight: 600,
            }}
          >
            View info
          </Button>

          <Button
            type="button"
            variant="primary"
            onClick={onCheckIn}
            style={{
              flex: 1,
              borderRadius: 999,
              height: 44,
              fontWeight: 600,
            }}
          >
            Check-in
          </Button>
        </div>
      </Card.Body>
    </Card>
  );
}