import React, { useEffect, useState } from "react"
import axios from "../config/axiosConfig"
import { toast } from "react-toastify"
import { useNavigate } from "react-router-dom"
import Layout from "./Layout"

const AssignTrainees = () => {
  const [students, setStudents] = useState([])
  const [selectedTrainees, setSelectedTrainees] = useState([])
  const [assignedTrainees, setAssignedTrainees] = useState([])
  const navigate = useNavigate()

  useEffect(() => {
    const fetchStudents = async () => {
      try {
        const response = await axios.get("/get/students/available")
        setStudents(response.data)
      } catch (error) {
        console.error("Error fetching students:", error)
      }
    }

    const fetchAssignedTrainees = async () => {
      try {
        const response = await axios.get("/api/trainees")
        setAssignedTrainees(response.data)
      } catch (error) {
        console.error("Error fetching assigned trainees:", error)
      }
    }

    fetchStudents()
    fetchAssignedTrainees()
  }, [])

  const handleTraineeSelect = (studentId) => {
    setSelectedTrainees((prev) => {
      if (prev.includes(studentId)) {
        return prev.filter((id) => id !== studentId)
      } else {
        return [...prev, studentId]
      }
    })
  }

  const handleSubmit = async () => {
    try {
      const data = { traineeIds: selectedTrainees }
      await axios.post("/api/assign/trainees", data)
      navigate("/")
      toast.success("Trainees assigned successfully!")
    } catch (error) {
      console.error("Error assigning trainees:", error)
      toast.error("Failed to assign trainees.")
    }
  }

  return (
    <Layout>
      <div className="max-w-4xl mx-auto">
        <h1 className="text-2xl font-light text-gray-800 mb-6 text-center">Assign Trainees</h1>
        {students.length === 0 ? (
          <p className="text-center text-gray-600">All students are assigned with trainers.</p>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {students.map((student) => (
              <div
                key={student.id}
                className="bg-white rounded-lg shadow-sm p-4 flex justify-between items-center hover:shadow-md transition duration-200"
              >
                <div className="flex-1">
                  <p className="font-medium text-gray-800">{student.name}</p>
                  <p className="text-sm text-gray-600">{student.email}</p>
                  <p className="text-sm text-gray-500">
                    Status: {assignedTrainees.some((t) => t.id === student.id) ? "Assigned" : "Available"}
                  </p>
                </div>
                <div className="flex items-center">
                  <input
                    type="checkbox"
                    onChange={() => handleTraineeSelect(student.id)}
                    className="h-5 w-5 text-gray-600 border-gray-300 rounded focus:ring-gray-500"
                  />
                </div>
              </div>
            ))}
          </div>
        )}
        {students.length > 0 && (
          <button
            className="w-full mt-6 p-3 bg-gray-800 text-white rounded-md hover:bg-gray-700 transition duration-200 focus:outline-none focus:ring-2 focus:ring-gray-500"
            onClick={handleSubmit}
          >
            Assign Trainees
          </button>
        )}
        {assignedTrainees.length > 0 && (
          <div className="mt-8">
            <h2 className="text-xl font-light text-gray-800 mb-4 text-center">Assigned Trainees</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {assignedTrainees.map((student) => (
                <div
                  key={student.id}
                  className="bg-white rounded-lg shadow-sm p-4 flex justify-between items-center hover:shadow-md transition duration-200"
                >
                  <div className="flex-1">
                    <p className="font-medium text-gray-800">{student.name}</p>
                    <p className="text-sm text-gray-600">{student.email}</p>
                    <p className="text-sm text-gray-500">Status: Assigned</p>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </Layout>
  )
}

export default AssignTrainees

