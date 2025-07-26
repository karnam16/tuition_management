import React from 'react';

const Students = () => {
  return (
    <div className="card">
      <h2>👥 Students Management</h2>
      <p>Manage all your students here.</p>
      
      <div style={{ marginTop: '2rem' }}>
        <button className="btn btn-primary" style={{ marginRight: '1rem' }}>
          ➕ Add New Student
        </button>
        <button className="btn" style={{ backgroundColor: '#f5f5f5' }}>
          📋 View All Students
        </button>
      </div>

      <div style={{ marginTop: '2rem', backgroundColor: '#f9f9f9', padding: '1rem', borderRadius: '4px' }}>
        <p><strong>Coming Soon:</strong></p>
        <ul>
          <li>Student registration form</li>
          <li>Student list with search</li>
          <li>Student profile management</li>
        </ul>
      </div>
    </div>
  );
};

export default Students;
