import React, { useEffect } from "react"
import { useNavigate } from "react-router-dom"
import Layout from "./Layout"

const Home = () => {
  const navigate = useNavigate()
  const userRole = localStorage.getItem("role")
  const token = localStorage.getItem("token")

  useEffect(() => {
    if (!token) {
      navigate("/login")
    }
  }, [token, navigate])
/* 
  const handleLogout = () => {
    localStorage.removeItem("token")
    localStorage.removeItem("role")
    navigate("/login")
  } */

  const menuItems =
    userRole === "student"
      ? [
          { title: "Apply Leave", path: "/applyleave" },
          { title: "Check Leave History", path: "/leave-history" },
          { title: "Assign Trainer", path: "/assign-trainer" },
        ]
      : userRole === "employee"
        ? [
            { title: "Update Leave", path: "/update-leave" },
            { title: "Assign Trainees", path: "/assign-trainees" },
          ]
        : []

  return (
    <Layout>
      <div className="flex flex-col items-center min-h-screen bg-gray-50">
        <div className="w-full max-w-md p-8">
          <h1 className="text-2xl font-light text-gray-800 mb-6 text-center">
            Welcome, {userRole?.charAt(0).toUpperCase() + userRole?.slice(1)}
          </h1>
          <div className="space-y-4">
            {menuItems.map((item, index) => (
              <button
                key={index}
                onClick={() => navigate(item.path)}
                className="w-full p-3 bg-white text-gray-800 rounded-md shadow-sm hover:shadow-md transition duration-300 ease-in-out border border-gray-200"
              >
                {item.title}
              </button>
            ))}
            {/* <button
              onClick={handleLogout}
              className="w-full p-3 bg-gray-800 text-white rounded-md shadow-sm hover:bg-gray-700 transition duration-300 ease-in-out mt-8"
            >
              Logout
            </button> */}
          </div>
        </div>
      </div>
    </Layout>
  )
}

export default Home
