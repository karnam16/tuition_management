import React from 'react';
import { Link, useLocation } from 'react-router-dom';

const Navigation = () => {
  const location = useLocation();
  
  const navItems = [
    { id: 'dashboard', label: '📊 Dashboard', icon: '🏠', path: '/' },
    { id: 'students', label: '👥 Students', icon: '🎓', path: '/students' },
    { id: 'fees', label: '💰 Fees', icon: '💳', path: '/fees' }
  ];

  return (
    <nav style={{
      backgroundColor: 'white',
      padding: '1rem 2rem',
      boxShadow: '0 2px 4px rgba(0,0,0,0.05)',
      borderBottom: '1px solid #e0e0e0'
    }}>
      <div style={{
        display: 'flex',
        gap: '2rem',
        alignItems: 'center'
      }}>
        {navItems.map((item) => (
          <Link
            key={item.id}
            to={item.path}
            style={{
              background: location.pathname === item.path ? '#e3f2fd' : 'transparent',
              border: 'none',
              padding: '12px 20px',
              borderRadius: '8px',
              cursor: 'pointer',
              fontSize: '16px',
              color: location.pathname === item.path ? '#1976d2' : '#666',
              fontWeight: location.pathname === item.path ? 'bold' : 'normal',
              textDecoration: 'none',
              transition: 'all 0.3s ease',
              display: 'flex',
              alignItems: 'center',
              gap: '8px'
            }}
          >
            <span>{item.icon}</span>
            {item.label}
          </Link>
        ))}
      </div>
    </nav>
  );
};

export default Navigation;
