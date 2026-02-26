import React from "react";
import { InputGroup, Form, Button } from "react-bootstrap";
import { Funnel } from "react-bootstrap-icons";

function SearchBar({ value, onChange, onFilterClick, placeholder }) {
  return (
    <InputGroup>
      <Form.Control
        value={value}
        onChange={(e) => onChange(e.target.value)}
        placeholder={placeholder || "Search..."}
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