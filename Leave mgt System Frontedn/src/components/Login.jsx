import React, { useState } from "react"
import axios from "../config/axiosConfig"
import { useNavigate } from "react-router-dom"
import { toast } from "react-hot-toast"
import Layout from "./Layout"

const Login = () => {
  const [userType, setUserType] = useState("student")
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [validationErrors, setValidationErrors] = useState({})
  const navigate = useNavigate()
    
  const handleSubmit = (e) => {
    e.preventDefault()
    setValidationErrors({})
    const errors = {}
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
    if (!emailRegex.test(email)) {
      errors.email = "Please enter a valid email address."
    }

    if (Object.keys(errors).length > 0) {
      setValidationErrors(errors)
      return
    }

    const url = userType === "student" ? "/auth/login" : "/auth/emp/login"
    const data = { email, password }

    axios
      .post(url, data)
      .then((response) => {
        localStorage.setItem("token", response.data.jwt)
        localStorage.setItem("role", userType)
        toast.success("Login successful!")
        navigate("/")
      })
      .catch((error) => {
        console.error("Login error:", error)
        toast.error("Login failed. Please check your credentials.")
      })
  }

      const token = localStorage.getItem('token');
      React.useEffect(() => {
        if (token) {
            navigate('/');
        }
    }, [token, navigate]);


  return (
    <Layout>
      <div className="max-w-md mx-auto">
        <h1 className="text-2xl font-light text-gray-800 mb-6 text-center">Login</h1>
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
            <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">
              Email
            </label>
            <input
              type="email"
              id="email"
              placeholder="Enter your email"
              className="w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-1 focus:ring-gray-500 focus:border-gray-500"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            {validationErrors.email && <p className="text-red-500 text-sm mt-1">{validationErrors.email}</p>}
          </div>

          <div>
            <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-1">
              Password
            </label>
            <input
              type="password"
              id="password"
              placeholder="Enter your password"
              className="w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-1 focus:ring-gray-500 focus:border-gray-500"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <button
            type="submit"
            className="w-full p-2 bg-gray-800 text-white rounded-md hover:bg-gray-700 transition duration-200 focus:outline-none focus:ring-2 focus:ring-gray-500"
          >
            Login
          </button>
        </form>

        <p className="mt-4 text-center text-sm text-gray-600">
          Don't have an account?{" "}
          <span className="text-gray-800 cursor-pointer hover:underline" onClick={() => navigate("/signup")}>
            Sign Up
          </span>
        </p>
      </div>
    </Layout>
  )
}

export default Login

