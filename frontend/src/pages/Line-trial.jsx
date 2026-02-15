import { Container, Card } from "react-bootstrap";
import LineChartComponent from "../components/OccupancyLineChart";

function LineTrial() {
  return (
    <Container className="mt-5">
      <h1 className="text-center">Line Chart Demo</h1>
      <p className="lead text-center mt-3">This is a dummy-data chart preview</p>

      <Card className="mt-4 shadow-sm">
        <Card.Body>
          <LineChartComponent />
        </Card.Body>
      </Card>
    </Container>
  );
}

export default LineTrial;
