#!/bin/bash

echo "=== TUITION MANAGEMENT SYSTEM - DATABASE OUTPUT DEMO ==="
echo "Starting Spring Boot application in background..."

# Start the application in background
./mvnw spring-boot:run -Dspring-boot.run.profiles=demo > app.log 2>&1 &
APP_PID=$!

# Wait for application to start
echo "Waiting for application to start..."
sleep 15

echo ""
echo "🔥 APPLICATION STARTED - Now testing database operations..."
echo ""

# Test 1: Create a new student
echo "📝 TEST 1: Creating a new student..."
curl -X POST http://localhost:8080/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "rollNumber": "2024001",
    "email": "john@example.com",
    "phone": "9876543210",
    "department": "Computer Science",
    "className": "Class 12",
    "monthlyFee": 5000.00,
    "discountPercent": 10.00,
    "joiningDate": "2024-01-15",
    "parentName": "Robert Doe",
    "parentPhone": "9876543211",
    "parentEmail": "robert@example.com"
  }'

echo -e "\n"

# Test 2: Create another student
echo "📝 TEST 2: Creating another student..."
curl -X POST http://localhost:8080/students \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith",
    "rollNumber": "2024002",
    "email": "jane@example.com",
    "phone": "9876543220",
    "department": "Mathematics",
    "className": "Class 11",
    "monthlyFee": 4500.00,
    "discountPercent": 5.00,
    "joiningDate": "2024-02-01",
    "parentName": "Mary Smith",
    "parentPhone": "9876543221",
    "parentEmail": "mary@example.com"
  }'

echo -e "\n"

# Test 3: Get all students
echo "📋 TEST 3: Retrieving all students..."
curl -X GET http://localhost:8080/students

echo -e "\n"

# Test 4: Generate fee record
echo "💰 TEST 4: Generating fee record for student ID 1..."
curl -X POST http://localhost:8080/students/1/generate-fee

echo -e "\n"

# Test 5: Get students with due fees
echo "⚠️ TEST 5: Getting students with due fees..."
curl -X GET http://localhost:8080/students/due-fees

echo -e "\n"

# Test 6: Search student by name
echo "🔍 TEST 6: Searching student by name 'John'..."
curl -X GET "http://localhost:8080/students/search?name=John"

echo -e "\n"

# Test 7: Update student
echo "✏️ TEST 7: Updating student ID 1..."
curl -X PUT http://localhost:8080/students/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe Updated",
    "rollNumber": "2024001",
    "email": "john.updated@example.com",
    "phone": "9876543210",
    "department": "Computer Science",
    "className": "Class 12",
    "monthlyFee": 5500.00,
    "discountPercent": 15.00,
    "joiningDate": "2024-01-15",
    "parentName": "Robert Doe",
    "parentPhone": "9876543211",
    "parentEmail": "robert@example.com"
  }'

echo -e "\n"

echo "🏁 TESTS COMPLETED!"
echo ""
echo "📊 Check app.log for detailed SQL queries and database operations:"
echo "==================== DATABASE SQL OUTPUT ===================="
grep -A 5 -B 2 "Hibernate:" app.log | tail -50

# Stop the application
kill $APP_PID
echo ""
echo "✅ Application stopped. Demo completed!"