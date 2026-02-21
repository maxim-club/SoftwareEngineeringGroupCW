import { InputGroup, FormControl, Button } from "react-bootstrap";
import { Funnel } from "react-bootstrap-icons";

function SearchBar({ value, onChange, onFilterClick, placeholder = "Search...", onSubmit }) {
  const handleKeyDown = (e) => {
    if (e.key === "Enter" && onSubmit) onSubmit();
  };

  return (
    <InputGroup>
      <FormControl
        placeholder={placeholder}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        onKeyDown={handleKeyDown}
      />
      <Button variant="outline-secondary" onClick={onFilterClick}>
        <Funnel />
      </Button>
    </InputGroup>
  );
}

export default SearchBar;