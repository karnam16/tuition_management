import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navigation from './components/Navigation';
import Dashboard from './components/Dashboard';
import Students from './components/Students';
import Fees from './components/Fees';
import './styles/App.css';

function App() {
  return (
    <Router>
      <div className="app-container">
        <header style={{
          backgroundColor: '#1976d2',
          color: 'white',
          padding: '1rem 2rem',
          boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
        }}>
          <h1>🎓 Tuition Management System</h1>
        </header>
        
        <Navigation />
        
        <main className="main-content">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/students" element={<Students />} />
            <Route path="/fees" element={<Fees />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
