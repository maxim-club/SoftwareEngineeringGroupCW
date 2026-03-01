import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Container } from 'react-bootstrap';
import { IoEyeOutline, IoEyeOffOutline } from 'react-icons/io5';
import { FaApple, FaGoogle, FaFacebookF } from 'react-icons/fa';
import './Login.css';

function Login() {
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const [formData, setFormData] = useState({
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
    console.log('Login:', formData);
    navigate('/');
  };

  const handleSocialLogin = (provider) => {
    console.log(`Login with ${provider}`);
  };

  return (
    <div className="login-page">
      <Container className="login-container">
        <div className="login-card">
          <h1 className="login-title">Welcome Back</h1>
          <p className="login-subtitle">Sign in to continue</p>

          <form onSubmit={handleSubmit} className="login-form">
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
              </div>
            </div>

            <div className="forgot-password">
              <span onClick={() => navigate('/forgot-password')} className="link">
                Forgot password?
              </span>
            </div>

            <button type="submit" className="login-button">
              Sign in
            </button>
          </form>

          <div className="social-login">
            <p className="divider-text">or sign in with</p>
            
            <div className="social-buttons">
              <button
                className="social-button"
                onClick={() => handleSocialLogin('Apple')}
              >
                <FaApple size={24} />
              </button>
              <button
                className="social-button"
                onClick={() => handleSocialLogin('Google')}
              >
                <FaGoogle size={20} />
              </button>
              <button
                className="social-button"
                onClick={() => handleSocialLogin('Facebook')}
              >
                <FaFacebookF size={20} />
              </button>
            </div>
          </div>

          <p className="signup-link">
            Don't have an account?{' '}
            <span onClick={() => navigate('/signup')} className="link">
              Sign up
            </span>
          </p>
        </div>
      </Container>
    </div>
  );
}

export default Login;