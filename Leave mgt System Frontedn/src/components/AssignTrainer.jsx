import React, { useEffect, useState } from "react"
import axios from "../config/axiosConfig"
import { useNavigate } from "react-router-dom"
import { toast } from "react-hot-toast"
import Layout from "./Layout"

const AssignTrainer = () => {
  const [trainers, setTrainers] = useState([])
  const [selectedTrainer, setSelectedTrainer] = useState("")
  const [assignedTrainer, setAssignedTrainer] = useState(null)
  const navigate = useNavigate()

  useEffect(() => {
    const fetchUserProfile = async () => {
      try {
        const response = await axios.get("http://localhost:8080/get/student/profile")
        if (response.data.trainer) {
          setAssignedTrainer(response.data.trainer)
        } else {
          fetchTrainers()
        }
      } catch (error) {
        console.error("Error fetching user profile:", error)
      }
    }

    const fetchTrainers = async () => {
      try {
        const response = await axios.get("/get/employees")
        setTrainers(response.data)
      } catch (error) {
        console.error("Error fetching trainers:", error)
      }
    }

    fetchUserProfile()
  }, [])

  const handleSubmit = (e) => {
    e.preventDefault()
    const data = { trainerId: selectedTrainer }

    if (!selectedTrainer) {
      toast.error("Trainer selection is required!")
      return
    }

    axios
      .post("/api/assign/trainer", data)
      .then((response) => {
        console.log("Trainer assigned successfully:", response.data)
        toast.success("Trainer assigned successfully!")
        navigate("/")
      })
      .catch((error) => {
        console.error("Error assigning trainer:", error)
      })
  }

  return (
    <Layout>
      <div className="max-w-3xl mx-auto">
        <h1 className="text-2xl font-light text-gray-800 mb-6 text-center">Assign Trainer</h1>
        {assignedTrainer ? (
          <div className="bg-white p-6 rounded-lg shadow-sm">
            <h2 className="text-xl font-medium text-gray-800 mb-4">Assigned Trainer Details:</h2>
            <p className="mb-2"><span className="font-medium text-gray-600">Name:</span> {assignedTrainer.name}</p>
            <p className="mb-2"><span className="font-medium text-gray-600">Designation:</span> {assignedTrainer.designation}</p>
            <p className="mb-2"><span className="font-medium text-gray-600">Mobile:</span> {assignedTrainer.mobile}</p>
            <p><span className="font-medium text-gray-600">Email:</span> {assignedTrainer.email}</p>
          </div>
        ) : (
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="overflow-x-auto bg-white rounded-lg shadow-sm">
              <table className="min-w-full">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="py-3 px-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                    <th className="py-3 px-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Designation</th>
                    <th className="py-3 px-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Mobile</th>
                    <th className="py-3 px-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Email</th>
                    <th className="py-3 px-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Select</th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {trainers.map((trainer) => (
                    <tr key={trainer.id} className="hover:bg-gray-50 transition duration-200">
                      <td className="py-4 px-4 whitespace-nowrap">{trainer.name}</td>
                      <td className="py-4 px-4 whitespace-nowrap">{trainer.designation}</td>
                      <td className="py-4 px-4 whitespace-nowrap">{trainer.mobile}</td>
                      <td className="py-4 px-4 whitespace-nowrap">{trainer.email}</td>
                      <td className="py-4 px-4 whitespace-nowrap">
                        <input
                          type="radio"
                          name="trainer"
                          value={trainer.id.toString()}
                          onChange={() => setSelectedTrainer(trainer.id.toString())}
                          className="h-4 w-4 text-gray-600 border-gray-300 focus:ring-gray-500"
                        />
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            <button
              className="w-full p-3 bg-gray-800 text-white rounded-md hover:bg-gray-700 transition duration-200 focus:outline-none focus:ring-2 focus:ring-gray-500"
              type="submit"
            >
              Assign Trainer
            </button>
          </form>
        )}
      </div>
    </Layout>
  )
}

export default AssignTrainer
