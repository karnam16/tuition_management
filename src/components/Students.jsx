import React, { useState, useEffect } from "react";
import { studentAPI } from "../services/api";

const Students = () => {
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showAddForm, setShowAddForm] = useState(false);
  const [editingStudent, setEditingStudent] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [filterStatus, setFilterStatus] = useState("all");

  // Form state
  const [formData, setFormData] = useState({
    name: "",
    rollNumber: "",
    email: "",
    phone: "",
    className: "",
    department: "",
    parentName: "",
    parentPhone: "",
    joiningDate: new Date().toISOString().split("T")[0],
    monthlyFee: 1000,
    discountPercent: 0,
  });

  // Fetch students on component mount
  useEffect(() => {
    fetchStudents();
  }, []);

  const fetchStudents = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await studentAPI.getAllStudents();
      setStudents(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const resetForm = () => {
    setFormData({
      name: "",
      rollNumber: "",
      email: "",
      phone: "",
      className: "",
      department: "",
      parentName: "",
      parentPhone: "",
      joiningDate: new Date().toISOString().split("T")[0],
      monthlyFee: 1000,
      discountPercent: 0,
    });
    setEditingStudent(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      if (editingStudent) {
        await studentAPI.updateStudent(editingStudent.id, formData);
      } else {
        await studentAPI.addStudent(formData);
      }

      await fetchStudents();
      setShowAddForm(false);
      resetForm();
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleEdit = (student) => {
    setEditingStudent(student);
    setFormData({
      name: student.name || "",
      rollNumber: student.rollNumber || "",
      email: student.email || "",
      phone: student.phone || "",
      className: student.className || "",
      department: student.department || "",
      parentName: student.parentName || "",
      parentPhone: student.parentPhone || "",
      joiningDate:
        student.joiningDate || new Date().toISOString().split("T")[0],
      monthlyFee: student.monthlyFee || 1000,
      discountPercent: student.discountPercent || 0,
    });
    setShowAddForm(true);
  };

  const handleDelete = async (studentId) => {
    if (window.confirm("Are you sure you want to delete this student?")) {
      setLoading(true);
      setError(null);
      try {
        await studentAPI.deleteStudent(studentId);
        await fetchStudents();
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    }
  };

  const filteredStudents = students.filter((student) => {
    const matchesSearch =
      student.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      student.email?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      student.phone?.includes(searchTerm);

    if (filterStatus === "all") return matchesSearch;
    if (filterStatus === "active")
      return matchesSearch && student.status === "ACTIVE";
    if (filterStatus === "inactive")
      return matchesSearch && student.status === "INACTIVE";

    return matchesSearch;
  });

  if (loading && students.length === 0) {
    return (
      <div className="card">
        <h2>👥 Students Management</h2>
        <p>Loading students...</p>
      </div>
    );
  }

  return (
    <div className="card">
      <div className="card-header">
        <div>
          <h2 className="card-title">👥 Student Management</h2>
          <p style={{ color: "#6c757d", margin: "0.5rem 0 0 0" }}>
            Manage all your students, track their progress, and maintain their
            records
          </p>
        </div>
        <button
          className="btn btn-primary"
          onClick={() => {
            resetForm();
            setShowAddForm(!showAddForm);
          }}
        >
          {showAddForm ? "❌ Cancel" : "➕ Add New Student"}
        </button>
      </div>

      {error && (
        <div className="error-message">
          <strong>⚠️ Error:</strong> {error}
        </div>
      )}

      {/* Add/Edit Form */}
      {showAddForm && (
        <div className="form-section">
          <h3>{editingStudent ? "✏️ Edit Student" : "➕ Add New Student"}</h3>
          <form onSubmit={handleSubmit}>
            <div className="form-grid">
              <div>
                <label>Name *</label>
                <input
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handleInputChange}
                  required
                  className="form-input"
                  placeholder="Enter student's full name"
                />
              </div>
              <div>
                <label>Email</label>
                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleInputChange}
                  className="form-input"
                  placeholder="student@email.com"
                />
              </div>
              <div>
                <label>Phone *</label>
                <input
                  type="tel"
                  name="phone"
                  value={formData.phone}
                  onChange={handleInputChange}
                  required
                  className="form-input"
                  placeholder="+91 98765 43210"
                />
              </div>
              {/* Roll Number */}
              <div>
                <label>Roll Number *</label>
                <input
                  type="text"
                  name="rollNumber"
                  value={formData.rollNumber}
                  onChange={handleInputChange}
                  required
                  className="form-input"
                  placeholder="Enter unique roll number"
                />
              </div>
              {/* Department */}
              <div>
                <label>Department *</label>
                <select
                  name="department"
                  value={formData.department}
                  onChange={handleInputChange}
                  required
                  className="form-input"
                >
                  <option value="">Select Department</option>
                  <option value="Science">Science</option>
                  <option value="Commerce">Commerce</option>
                  <option value="Arts">Arts</option>
                  <option value="General">General</option>
                </select>
              </div>
              {/* Class */}
              <div>
                <label>Class *</label>
                <select
                  name="className"
                  value={formData.className}
                  onChange={handleInputChange}
                  required
                  className="form-input"
                >
                  <option value="">Select Class</option>
                  {[...Array(12)].map((_, i) => (
                    <option key={i + 1} value={`Class ${i + 1}`}>
                      Class {i + 1}
                    </option>
                  ))}
                </select>
              </div>
              {/* Joining Date */}
              <div>
                <label>Joining Date *</label>
                <input
                  type="date"
                  name="joiningDate"
                  value={formData.joiningDate}
                  onChange={handleInputChange}
                  required
                  className="form-input"
                />
              </div>

              {/* Monthly Fee & Discount */}
              <div>
                <label>Monthly Fee (₹)</label>
                <input
                  type="number"
                  name="monthlyFee"
                  value={formData.monthlyFee}
                  onChange={handleInputChange}
                  className="form-input"
                />
              </div>
              <div>
                <label>Discount %</label>
                <input
                  type="number"
                  name="discountPercent"
                  value={formData.discountPercent}
                  onChange={handleInputChange}
                  className="form-input"
                />
              </div>
              {/* Parent Name */}
              <div>
                <label>Parent Name *</label>
                <input
                  type="text"
                  name="parentName"
                  value={formData.parentName}
                  onChange={handleInputChange}
                  required
                  className="form-input"
                  placeholder="Parent's full name"
                />
              </div>

              {/* Parent Phone */}
              <div>
                <label>Parent Phone *</label>
                <input
                  type="tel"
                  name="parentPhone"
                  value={formData.parentPhone}
                  onChange={handleInputChange}
                  required
                  className="form-input"
                  placeholder="+91 98765 43210"
                />
              </div>
            </div>
            <div className="form-actions">
              <button
                type="submit"
                className="btn btn-primary"
                disabled={loading}
              >
                {loading
                  ? "Saving..."
                  : editingStudent
                  ? "Update Student"
                  : "Add Student"}
              </button>
              <button
                type="button"
                className="btn btn-secondary"
                onClick={() => {
                  setShowAddForm(false);
                  resetForm();
                }}
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      )}

      {/* Search and Filter */}
      <div className="search-filter-container">
        <div className="search-input">
          <input
            type="text"
            placeholder="Search students by name, email, or phone..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="form-input"
            style={{ width: "100%" }}
          />
        </div>
        <select
          value={filterStatus}
          onChange={(e) => setFilterStatus(e.target.value)}
          className="form-input filter-select"
        >
          <option value="all">All Students</option>
          <option value="active">Active</option>
          <option value="inactive">Inactive</option>
        </select>
        <button
          className="btn btn-secondary"
          onClick={fetchStudents}
          disabled={loading}
        >
          🔄 Refresh
        </button>
      </div>

      {/* Students List */}
      <div>
        <div className="card-header">
          <h3>📋 Students List ({filteredStudents.length})</h3>
        </div>

        {filteredStudents.length === 0 ? (
          <div className="empty-state">
            <h4>
              {searchTerm ? "No students found" : "No students registered yet"}
            </h4>
            <p>
              {searchTerm
                ? "Try adjusting your search terms or filters to find the student you're looking for."
                : "Start building your student database by adding your first student to the system."}
            </p>
            {!searchTerm && (
              <button
                className="btn btn-primary"
                onClick={() => setShowAddForm(true)}
                style={{ marginTop: "1rem" }}
              >
                ➕ Add Your First Student
              </button>
            )}
          </div>
        ) : (
          <div className="grid grid-2">
            {filteredStudents.map((student) => (
              <div key={student.id} className="item-card">
                <div className="header">
                  <div className="title">{student.name}</div>
                  <div className="actions">
                    <button
                      className="btn action-btn edit-btn"
                      onClick={() => handleEdit(student)}
                    >
                      ✏️ Edit
                    </button>
                    <button
                      className="btn action-btn delete-btn"
                      onClick={() => handleDelete(student.id)}
                    >
                      🗑️ Delete
                    </button>
                  </div>
                </div>

                <div className="details">
                  <p>
                    <strong>📧 Email:</strong> {student.email || "Not provided"}
                  </p>
                  <p>
                    <strong>📞 Phone:</strong> {student.phone || "Not provided"}
                  </p>
                  <p>
                    <strong>📚 Grade:</strong>{" "}
                    {student.grade || "Not specified"}
                  </p>
                  <p>
                    <strong>👨‍👩‍👧‍👦 Parent:</strong>{" "}
                    {student.parentName || "Not provided"}
                  </p>
                  <p>
                    <strong>📱 Parent Phone:</strong>{" "}
                    {student.parentPhone || "Not provided"}
                  </p>
                  {student.joinDate && (
                    <p>
                      <strong>📅 Joined:</strong>{" "}
                      {new Date(student.joinDate).toLocaleDateString()}
                    </p>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Students;
