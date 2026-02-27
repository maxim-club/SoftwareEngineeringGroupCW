import { Container, Card } from "react-bootstrap";
import LineChartComponent from "../components/OccupancyLineChart";
import './Line-trial.css';


function LineTrial() {
  return (
    <Container className="mt-5">

      <Card className="mt-4 shadow-sm">
        <Card.Body>
          <LineChartComponent />
        </Card.Body>
      </Card>
    </Container>
  );
}

export default LineTrial;
