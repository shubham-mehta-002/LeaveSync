import React, { useState } from "react"
import axios from "../config/axiosConfig"
import { useNavigate } from "react-router-dom"
import { toast } from "react-hot-toast"
import Layout from "./Layout"    

const ApplyLeave = () => {
  const [fromDate, setFromDate] = useState("")
  const [toDate, setToDate] = useState("")
  const [reason, setReason] = useState("")

  const navigate = useNavigate()

  const handleSubmit = (e) => {
    e.preventDefault()

    const formattedFromDate = new Date(fromDate).toISOString().split("T")[0]
    const formattedToDate = new Date(toDate).toISOString().split("T")[0]
    const data = { reason, from: formattedFromDate, endDate: formattedToDate }

    // Validations
    if (!data.reason) {
      toast.error("Reason is required!")
      return
    }
    if (!data.from) {
      toast.error("From date is required!")
      return
    }
    if (!data.endDate) {
      toast.error("To date is required!")
      return
    }
    if (new Date(data.from) > new Date(data.endDate)) {
      toast.error("From date cannot be greater than to date!")
      return
    }

    console.log(data)
    axios
      .post("http://localhost:8080/api/leaveRequest/apply", data)
      .then((response) => {
        console.log("Leave application submitted:", response.data)
        toast.success("Leave applied successfully!")
        navigate("/leave-history")
      })
      .catch((error) => {
        console.error("Error applying for leave:", error)
        toast.error("Failed to apply for leave. Please try again.")
      })
  }

  return (
    <Layout>
      <div className="flex flex-col items-center justify-center min-h-screen bg-gray-50 p-4">
        <div className="w-full max-w-md">
          <h1 className="text-2xl font-light text-gray-800 mb-6 text-center">Apply for Leave</h1>
          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label htmlFor="fromDate" className="block text-sm font-medium text-gray-700 mb-1">
                From Date
              </label>
              <input
                id="fromDate"
                type="date"
                value={fromDate}
                onChange={(e) => setFromDate(e.target.value)}
                min={new Date().toISOString().split("T")[0]}
                className="w-full p-3 bg-white text-gray-800 rounded-md shadow-sm border border-gray-200 focus:border-gray-400 focus:ring focus:ring-gray-200 focus:ring-opacity-50 transition duration-300 ease-in-out"
                required
              />
            </div>
            <div>
              <label htmlFor="toDate" className="block text-sm font-medium text-gray-700 mb-1">
                To Date
              </label>
              <input
                id="toDate"
                type="date"
                value={toDate}
                onChange={(e) => setToDate(e.target.value)}
                min={fromDate || new Date().toISOString().split("T")[0]}
                className="w-full p-3 bg-white text-gray-800 rounded-md shadow-sm border border-gray-200 focus:border-gray-400 focus:ring focus:ring-gray-200 focus:ring-opacity-50 transition duration-300 ease-in-out"
                required
              />
            </div>
            <div>
              <label htmlFor="reason" className="block text-sm font-medium text-gray-700 mb-1">
                Reason
              </label>
              <textarea
                id="reason"
                value={reason}
                onChange={(e) => setReason(e.target.value)}
                className="w-full p-3 bg-white text-gray-800 rounded-md shadow-sm border border-gray-200 focus:border-gray-400 focus:ring focus:ring-gray-200 focus:ring-opacity-50 transition duration-300 ease-in-out"
                rows="4"
                required
              ></textarea>
            </div>
            <button
              type="submit"
              className="w-full p-3 bg-gray-800 text-white rounded-md shadow-sm hover:bg-gray-700 transition duration-300 ease-in-out"
            >
              Submit
            </button>
          </form>
        </div>
      </div>
    </Layout>
  )
}

export default ApplyLeave

