import React from "react";
import { InputGroup, Form, Button } from "react-bootstrap";
import { Funnel } from "react-bootstrap-icons";

function SearchBar({ value, onChange, onFilterClick, placeholder, onSubmit, onFocus, onBlur }) {
  return (
    <Form
      onSubmit={(e) => {
        e.preventDefault();
        onSubmit?.();
      }}
    >
      <InputGroup>
        <Form.Control
          value={value}
          onChange={(e) => onChange(e.target.value)}
          placeholder={placeholder || "Search..."}
          onFocus={onFocus}
          onBlur={onBlur}
        />

        <Button
          variant="outline-secondary"
          onClick={onFilterClick}
          type="button"
        >
          <Funnel />
        </Button>
      </InputGroup>
    </Form>
  );
}

export default SearchBar;