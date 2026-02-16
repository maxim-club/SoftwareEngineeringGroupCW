import { Container } from "react-bootstrap";
import UserOccupancyPercent from "../components/UserOccupancy";

export default function StaffDashboard() {
  return (
    <Container className="mt-5">
      <h1>Staff Dashboard</h1>
      <UserOccupancyPercent />
    </Container>
  );
}