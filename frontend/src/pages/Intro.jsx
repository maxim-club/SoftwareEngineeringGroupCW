import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Container } from 'react-bootstrap';
import './Intro.css';

function Intro() {
  const navigate = useNavigate();

  const handleNext = () => {
    navigate('/signup');
  };

  return (
    <div className="intro-page">
      <Container className="intro-container">
        {/* Illustration */}
        <div className="intro-illustration">
          <img 
            src="/images/intro/intro2.jpg" 
            alt="Find your perfect study space"
            className="intro-image"
          />
        </div>

        {/* Content */}
        <div className="intro-content">
          <h1 className="intro-title">Find your perfect study space nearby</h1>
          <p className="intro-description">
            Stop wandering around campus. Find the perfect study environment in seconds and make every study session count.
          </p>
        </div>

        {/* Next button */}
        <button className="intro-next-button" onClick={handleNext}>
          Next
        </button>
      </Container>
    </div>
  );
}

export default Intro;