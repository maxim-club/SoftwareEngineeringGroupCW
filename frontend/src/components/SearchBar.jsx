import { InputGroup, FormControl, Button } from "react-bootstrap";
import { Funnel } from "react-bootstrap-icons"; // icon library

function SearchBar({ value, onChange, onFilterClick }) {
  return (
    <InputGroup>
      <FormControl
        placeholder="Search..."
        value={value}
        onChange={(e) => onChange(e.target.value)}
      />

      <Button
        variant="outline-secondary"
        onClick={onFilterClick}
      >
        <Funnel />
      </Button>
    </InputGroup>
  );
}

export default SearchBar;