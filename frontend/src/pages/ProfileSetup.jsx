import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Container } from 'react-bootstrap';
import { IoChevronBack, IoCalendarOutline, IoCheckmark } from 'react-icons/io5';
import { MdKeyboardArrowDown } from 'react-icons/md';
import './ProfileSetup.css';

function ProfileSetup() {
  const navigate = useNavigate();
  const [profilePhoto, setProfilePhoto] = useState(null);
  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    gender: '',
    dateOfBirth: ''
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handlePhotoUpload = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setProfilePhoto(reader.result);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // TODO: Connect to backend API
    console.log('Profile Setup:', formData);
    // Navigate to profile page after profile setup
    navigate('/profile');
  };

  return (
    <div className="profile-setup-page">
      <Container className="profile-setup-container">
        {/* Header */}
        <div className="profile-setup-header">
          <button className="back-button" onClick={() => navigate(-1)}>
            <IoChevronBack size={24} />
          </button>
        </div>

        {/* Content */}
        <div className="profile-setup-content">
          <h1 className="profile-title">Complete your profile</h1>
          <p className="profile-subtitle">
            Don't worry, only you can see your personal data. No one else will be able to see it.
          </p>

          {/* Profile Photo Upload */}
          <div className="photo-upload-section">
            <div className="photo-circle">
              {profilePhoto ? (
                <img src={profilePhoto} alt="Profile" className="profile-photo" />
              ) : (
                <div className="photo-placeholder">
                  <svg width="60" height="60" viewBox="0 0 60 60" fill="none">
                    <circle cx="30" cy="22" r="10" fill="#9CA3AF" />
                    <path d="M10 50C10 40 18 35 30 35C42 35 50 40 50 50" fill="#9CA3AF" />
                  </svg>
                </div>
              )}
              <label htmlFor="photo-upload" className="photo-edit-button">
                <IoCheckmark size={16} color="white" />
              </label>
              <input
                type="file"
                id="photo-upload"
                accept="image/*"
                onChange={handlePhotoUpload}
                style={{ display: 'none' }}
              />
            </div>
          </div>

          {/* Form */}
          <form onSubmit={handleSubmit} className="profile-form">
            {/* Full Name */}
            <div className="form-group">
              <label htmlFor="fullName">Full Name</label>
              <input
                type="text"
                id="fullName"
                name="fullName"
                className="form-input"
                placeholder="Full Name"
                value={formData.fullName}
                onChange={handleChange}
                required
              />
            </div>

            {/* Email */}
            <div className="form-group">
              <label htmlFor="email">Email</label>
              <input
                type="email"
                id="email"
                name="email"
                className="form-input"
                placeholder="Email"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </div>

            {/* Gender */}
            <div className="form-group">
              <label htmlFor="gender">Gender</label>
              <div className="select-wrapper">
                <select
                  id="gender"
                  name="gender"
                  className="form-select"
                  value={formData.gender}
                  onChange={handleChange}
                  required
                >
                  <option value="">Gender</option>
                  <option value="male">Male</option>
                  <option value="female">Female</option>
                  <option value="non-binary">Non-binary</option>
                  <option value="prefer-not-to-say">Prefer not to say</option>
                </select>
                <MdKeyboardArrowDown className="select-arrow" size={24} />
              </div>
            </div>

            {/* Date of Birth */}
            <div className="form-group">
              <label htmlFor="dateOfBirth">Date of Birth</label>
              <div className="date-input-wrapper">
                <input
                  type="date"
                  id="dateOfBirth"
                  name="dateOfBirth"
                  className="form-input date-input"
                  placeholder="MM/DD/YYYY"
                  value={formData.dateOfBirth}
                  onChange={handleChange}
                  required
                />
                <IoCalendarOutline className="calendar-icon" size={20} />
              </div>
            </div>

            {/* Continue Button */}
            <button type="submit" className="continue-button">
              Continue
            </button>
          </form>
        </div>
      </Container>
    </div>
  );
}

export default ProfileSetup;