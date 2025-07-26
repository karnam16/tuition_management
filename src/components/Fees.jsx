import React from 'react';

const Fees = () => {
  return (
    <div className="card">
      <h2>💰 Fee Management</h2>
      <p>Track and manage student fees.</p>
      
      <div style={{ marginTop: '2rem' }}>
        <button className="btn btn-primary" style={{ marginRight: '1rem' }}>
          💳 Record Payment
        </button>
        <button className="btn" style={{ backgroundColor: '#f5f5f5' }}>
          📊 View Due Fees
        </button>
      </div>

      <div style={{ marginTop: '2rem', backgroundColor: '#fff3cd', padding: '1rem', borderRadius: '4px', border: '1px solid #ffeaa7' }}>
        <p><strong>Features to Add:</strong></p>
        <ul>
          <li>Fee collection tracking</li>
          <li>Due fees notifications</li>
          <li>WhatsApp integration for reminders</li>
          <li>Payment history</li>
        </ul>
      </div>
    </div>
  );
};

export default Fees;
