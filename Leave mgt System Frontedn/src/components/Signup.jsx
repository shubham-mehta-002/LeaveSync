import React, { useState, useEffect } from "react"
import axios from "../config/axiosConfig"
import { useNavigate } from "react-router-dom"
import { toast } from "react-hot-toast"
import Layout from "./Layout"

const Signup = () => {
  const [userType, setUserType] = useState("student")
  const [name, setName] = useState("")
  const [email, setEmail] = useState("")
  const [mobile, setMobile] = useState("")
  const [designation, setDesignation] = useState("")
  const [employees, setEmployees] = useState([])
  const [students, setStudents] = useState([])
  const [selectedEmployee, setSelectedEmployee] = useState("")
  const [selectedStudent, setSelectedStudent] = useState("")
  const [validationErrors, setValidationErrors] = useState({})
  const navigate = useNavigate()

  useEffect(() => {
    axios
      .get("/get/employees")
      .then((response) => setEmployees(response.data))
      .catch((error) => console.error("Error fetching employees:", error))

    axios
      .get("/get/students/available")
      .then((response) => setStudents(response.data))
      .catch((error) => console.error("Error fetching students:", error))
  }, [])

  const handleSubmit = (e) => {
    e.preventDefault()
    setValidationErrors({})

    const errors = {}
    const nameRegex = /^[a-zA-Z ]{2,30}$/
    if (!nameRegex.test(name)) {
      errors.name = "Name must be 2-30 characters long and contain only letters."
    }

    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
    if (!emailRegex.test(email)) {
      errors.email = "Please enter a valid email address."
    }

    const mobileRegex = /^[0-9]{10}$/
    if (!mobileRegex.test(mobile)) {
      errors.mobile = "Mobile number must be 10 digits long."
    }

    if (userType === "employee" && designation.trim() === "") {
      errors.designation = "Please enter a designation."
    }

    if (Object.keys(errors).length > 0) {
      setValidationErrors(errors)
      return
    }

    const url = userType === "student" ? "/auth/signup" : "/auth/emp/signup"
    const data =
      userType === "student"
        ? { name, email, mobile, trainerId: Number(selectedEmployee) > 0 ? Number(selectedEmployee) : null }
        : { name, email, mobile, designation, trainerId: Number(selectedStudent) > 0 ? Number(selectedStudent) : null }

    axios
      .post(url, data)
      .then((response) => {
        localStorage.setItem("token", response.data.jwt)
        localStorage.setItem("role", userType)
        toast.success("Signup successful!")
        navigate("/")
      })
      .catch((error) => {
        console.error("Signup error:", error)
        toast.error("Signup failed. Please try again.")
      })
  }

  return (
    <Layout>
      <div className="max-w-md mx-auto">
        <h1 className="text-2xl font-light text-gray-800 mb-6 text-center">Create Your Account</h1>
        <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow-sm space-y-4">
          <div>
            <label htmlFor="userType" className="block text-sm font-medium text-gray-700 mb-1">
              User Type
            </label>
            <select
              id="userType"
              className="w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-1 focus:ring-gray-500 focus:border-gray-500"
              onChange={(e) => setUserType(e.target.value)}
            >
              <option value="student">Student</option>
              <option value="employee">Employee</option>
            </select>
          </div>

          <div>
            <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">
              Name
            </label>
            <input
              id="name"
              className="w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-1 focus:ring-gray-500 focus:border-gray-500"
              type="text"
              placeholder="Enter your name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
            {validationErrors.name && <p className="text-red-500 text-sm mt-1">{validationErrors.name}</p>}
          </div>

          <div>
            <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">
              Email
            </label>
            <input
              id="email"
              className="w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-1 focus:ring-gray-500 focus:border-gray-500"
              type="email"
              placeholder="Enter your email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            {validationErrors.email && <p className="text-red-500 text-sm mt-1">{validationErrors.email}</p>}
          </div>

          <div>
            <label htmlFor="mobile" className="block text-sm font-medium text-gray-700 mb-1">
              Mobile
            </label>
            <input
              id="mobile"
              className="w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-1 focus:ring-gray-500 focus:border-gray-500"
              type="text"
              placeholder="Enter your mobile number"
              value={mobile}
              onChange={(e) => setMobile(e.target.value)}
              required
            />
            {validationErrors.mobile && <p className="text-red-500 text-sm mt-1">{validationErrors.mobile}</p>}
          </div>

          {userType === "student" ? (
            <div>
              <label htmlFor="trainer" className="block text-sm font-medium text-gray-700 mb-1">
                Select Trainer
              </label>
              <select
                id="trainer"
                className="w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-1 focus:ring-gray-500 focus:border-gray-500"
                value={selectedEmployee}
                onChange={(e) => setSelectedEmployee(e.target.value)}
              >
                <option value="">Select Trainer</option>
                {employees.map((employee) => (
                  <option key={employee.id} value={employee.id}>
                    {employee.name} ({employee.email})
                  </option>
                ))}
              </select>
            </div>
          ) : (
            <>
              <div>
                <label htmlFor="designation" className="block text-sm font-medium text-gray-700 mb-1">
                  Designation
                </label>
                <input
                  id="designation"
                  className="w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-1 focus:ring-gray-500 focus:border-gray-500"
                  type="text"
                  placeholder="Enter your designation"
                  value={designation}
                  onChange={(e) => setDesignation(e.target.value)}
                  required
                />
                {validationErrors.designation && (
                  <p className="text-red-500 text-sm mt-1">{validationErrors.designation}</p>
                )}
              </div>
              <div>
                <label htmlFor="student" className="block text-sm font-medium text-gray-700 mb-1">
                  Select Student
                </label>
                <select
                  id="student"
                  className="w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-1 focus:ring-gray-500 focus:border-gray-500"
                  value={selectedStudent}
                  onChange={(e) => setSelectedStudent(e.target.value)}
                >
                  <option value="">Select Student</option>
                  {students.map((student) => (
                    <option key={student.id} value={student.id}>
                      {student.name} ({student.email})
                    </option>
                  ))}
                </select>
              </div>
            </>
          )}

          <button
            type="submit"
            className="w-full p-2 bg-gray-800 text-white rounded-md hover:bg-gray-700 transition duration-200 focus:outline-none focus:ring-2 focus:ring-gray-500"
          >
            Sign Up
          </button>
        </form>

        <p className="mt-4 text-center text-sm text-gray-600">
          Already have an account?{" "}
          <span className="text-gray-800 cursor-pointer hover:underline" onClick={() => navigate("/login")}>
            Login
          </span>
        </p>
      </div>
    </Layout>
  )
}

export default Signup

