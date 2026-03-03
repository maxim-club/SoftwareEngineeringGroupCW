import React from "react";
import "./SearchBar.css";

export default function SearchBar({
  value,
  onChange,
  placeholder = "Search...",
  onFilterClick,
  onSubmit,
  onFocus,
  onBlur,
}) {
  const handleKeyDown = (e) => {
    if (e.key === "Enter") {
      e.preventDefault();
      onSubmit?.();
    }
  };

  const clear = () => onChange("");

  return (
    <div className="ui-searchbar">
      <div className="ui-searchfield">
        <svg
          width="18"
          height="18"
          viewBox="0 0 24 24"
          fill="none"
          className="ui-searchicon"
        >
          <path
            d="M10.5 18a7.5 7.5 0 1 1 0-15 7.5 7.5 0 0 1 0 15Z"
            stroke="currentColor"
            strokeWidth="2"
          />
          <path
            d="M16.5 16.5 21 21"
            stroke="currentColor"
            strokeWidth="2"
            strokeLinecap="round"
          />
        </svg>

        <input
          className="ui-searchinput"
          value={value}
          onChange={(e) => onChange(e.target.value)}
          placeholder={placeholder}
          onKeyDown={handleKeyDown}
          onFocus={onFocus}
          onBlur={onBlur}
          autoComplete="off"
        />

        {value && (
          <button
            type="button"
            className="ui-clearbtn"
            onClick={clear}
            aria-label="Clear"
          >
            ✕
          </button>
        )}
      </div>

      <button
        type="button"
        className="ui-filterbtn"
        onClick={onFilterClick}
        aria-label="Filters"
      >
        <svg width="22" height="22" viewBox="0 0 24 24" fill="none">
          <path
            d="M4 6h10M18 6h2M8 12h14M4 12h2M4 18h10M18 18h2"
            stroke="white"
            strokeWidth="2"
            strokeLinecap="round"
          />
          <circle cx="16" cy="6" r="2" fill="white" />
          <circle cx="6" cy="12" r="2" fill="white" />
          <circle cx="16" cy="18" r="2" fill="white" />
        </svg>
      </button>
    </div>
  );
}