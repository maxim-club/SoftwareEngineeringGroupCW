import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Container } from 'react-bootstrap';
import { IoEyeOutline, IoEyeOffOutline } from 'react-icons/io5';
import { FaApple, FaGoogle, FaFacebookF } from 'react-icons/fa';
import './Signup.css';

function Signup() {
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: ''
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // TODO: Connect to backend API
    console.log('Signup:', formData);
    // Navigate to home after signup
    navigate('/');
  };

  const handleSocialSignup = (provider) => {
    // TODO: Implement social login
    console.log(`Signup with ${provider}`);
  };

  return (
    <div className="signup-page">
      <Container className="signup-container">
        <div className="signup-card">
          {/* Header */}
          <h1 className="signup-title">Create Account</h1>

          {/* Form */}
          <form onSubmit={handleSubmit} className="signup-form">
            {/* Name Input */}
            <div className="form-group">
              <label htmlFor="name">Name</label>
              <input
                type="text"
                id="name"
                name="name"
                className="form-input"
                placeholder="Marta"
                value={formData.name}
                onChange={handleChange}
                required
              />
            </div>

            {/* Email Input */}
            <div className="form-group">
              <label htmlFor="email">Email</label>
              <input
                type="email"
                id="email"
                name="email"
                className="form-input"
                placeholder="Example@gmail.com"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </div>

            {/* Password Input */}
            <div className="form-group">
              <label htmlFor="password">Password</label>
              <div className="password-input-wrapper">
                <input
                  type={showPassword ? 'text' : 'password'}
                  id="password"
                  name="password"
                  className="form-input"
                  placeholder="Password"
                  value={formData.password}
                  onChange={handleChange}
                  required
                />
                <button
                  type="button"
                  className="password-toggle"
                  onClick={() => setShowPassword(!showPassword)}
                >
                  {showPassword ? (
                    <IoEyeOffOutline size={20} />
                  ) : (
                    <IoEyeOutline size={20} />
                  )}
                </button>
                {/* Password strength dots */}
                <div className="password-dots">
                  <span className="dot"></span>
                  <span className="dot"></span>
                  <span className="dot"></span>
                </div>
              </div>
            </div>

            {/* Sign Up Button */}
            <button type="submit" className="signup-button">
              Sign up
            </button>
          </form>

          {/* Social Sign Up */}
          <div className="social-signup">
            <p className="divider-text">or sign up with</p>
            
            <div className="social-buttons">
              <button
                className="social-button"
                onClick={() => handleSocialSignup('Apple')}
              >
                <FaApple size={24} />
              </button>
              <button
                className="social-button"
                onClick={() => handleSocialSignup('Google')}
              >
                <FaGoogle size={20} />
              </button>
              <button
                className="social-button"
                onClick={() => handleSocialSignup('Facebook')}
              >
                <FaFacebookF size={20} />
              </button>
            </div>
          </div>

          {/* Sign In Link */}
          <p className="signin-link">
            Already have an account?{' '}
            <span onClick={() => navigate('/login')} className="link">
              Sign in
            </span>
          </p>
        </div>
      </Container>
    </div>
  );
}

export default Signup;