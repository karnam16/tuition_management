import React, { useState, useEffect } from 'react';
import { studentAPI, feeAPI, testConnection } from '../services/api';

const Dashboard = () => {
  const [loading, setLoading] = useState(true);
  const [apiStatus, setApiStatus] = useState('Testing...');
  const [studentCount, setStudentCount] = useState(0);
  const [dueFees, setDueFees] = useState(0);
  const [thisMonthFees, setThisMonthFees] = useState(0);
  const [error, setError] = useState(null);

  // Function to fetch all dashboard data
  const fetchDashboardData = async () => {
    setLoading(true);
    setError(null);
    
    try {
      // Test API connection first
      const connectionTest = await testConnection();
      setApiStatus(connectionTest.success ? '✅ Connected' : '❌ Disconnected');
      
      if (connectionTest.success) {
        // Fetch students data
        const students = await studentAPI.getAllStudents();
        setStudentCount(students.length);
        
        // Fetch due fees data
        const dueFeeRecords = await feeAPI.getDueFees();
        const totalDue = dueFeeRecords.reduce((sum, fee) => sum + (fee.amount || 0), 0);
        setDueFees(totalDue);
        
        // Fetch all fee records for this month calculation
        const allFeeRecords = await feeAPI.getAllFeeRecords();
        const currentMonth = new Date().getMonth();
        const currentYear = new Date().getFullYear();
        
        const thisMonthTotal = allFeeRecords
          .filter(fee => {
            const feeDate = new Date(fee.paymentDate || fee.dueDate);
            return feeDate.getMonth() === currentMonth && feeDate.getFullYear() === currentYear;
          })
          .reduce((sum, fee) => sum + (fee.amount || 0), 0);
        
        setThisMonthFees(thisMonthTotal);
      }
    } catch (err) {
      console.error('Dashboard data fetch error:', err);
      setError(err.message);
      setApiStatus('❌ Error');
    } finally {
      setLoading(false);
    }
  };

  // Fetch data when component mounts
  useEffect(() => {
    fetchDashboardData();
  }, []);

  // Auto-refresh data every 30 seconds for real-time updates
  useEffect(() => {
    const interval = setInterval(() => {
      fetchDashboardData();
    }, 30000); // 30 seconds

    return () => clearInterval(interval); // Cleanup on unmount
  }, []);

  if (loading) {
    return (
      <div className="card">
        <h2>📊 Dashboard</h2>
        <p>Loading dashboard data...</p>
      </div>
    );
  }

  return (
    <div className="card">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
        <h2>📊 Dashboard</h2>
        <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
          <div style={{ 
            padding: '8px 16px', 
            borderRadius: '20px', 
            backgroundColor: apiStatus.includes('✅') ? '#e8f5e8' : '#ffebee',
            color: apiStatus.includes('✅') ? '#2e7d32' : '#d32f2f',
            fontSize: '14px',
            fontWeight: 'bold'
          }}>
            API: {apiStatus}
          </div>
          <button 
            className="btn btn-primary"
            onClick={fetchDashboardData}
            style={{ fontSize: '12px', padding: '8px 12px' }}
          >
            🔄 Refresh
          </button>
        </div>
      </div>

      {error && (
        <div style={{ 
          backgroundColor: '#ffebee', 
          color: '#d32f2f', 
          padding: '1rem', 
          borderRadius: '4px', 
          marginBottom: '1rem',
          border: '1px solid #f5c6cb'
        }}>
          <strong>Error:</strong> {error}
        </div>
      )}
      
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(250px, 1fr))', gap: '1rem', marginTop: '2rem' }}>
        <div className="card" style={{ backgroundColor: '#e3f2fd', border: '1px solid #1976d2' }}>
          <h3>👥 Total Students</h3>
          <p style={{ fontSize: '2rem', fontWeight: 'bold', color: '#1976d2' }}>
            {studentCount}
          </p>
          <small style={{ color: '#666' }}>Live from database</small>
        </div>
        
        <div className="card" style={{ backgroundColor: '#ffebee', border: '1px solid #d32f2f' }}>
          <h3>💰 Due Fees</h3>
          <p style={{ fontSize: '2rem', fontWeight: 'bold', color: '#d32f2f' }}>
            ₹{dueFees.toLocaleString()}
          </p>
          <small style={{ color: '#666' }}>Pending payments</small>
        </div>
        
        <div className="card" style={{ backgroundColor: '#e8f5e8', border: '1px solid #2e7d32' }}>
          <h3>📈 This Month</h3>
          <p style={{ fontSize: '2rem', fontWeight: 'bold', color: '#2e7d32' }}>
            ₹{thisMonthFees.toLocaleString()}
          </p>
          <small style={{ color: '#666' }}>Collected fees</small>
        </div>
      </div>

      <div style={{ marginTop: '2rem', padding: '1rem', backgroundColor: '#f0f7ff', borderRadius: '8px', border: '1px solid #bee5eb' }}>
        <h4>🔄 Real-Time Updates</h4>
        <p>
          ✅ **Data refreshes automatically every 30 seconds**<br/>
          ✅ **Manual refresh button available**<br/>
          ✅ **Live connection to your Spring Boot backend**
        </p>
        <p><small>Last updated: {new Date().toLocaleTimeString()}</small></p>
      </div>
    </div>
  );
};

export default Dashboard;
