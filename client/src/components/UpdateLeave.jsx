import React, { useEffect, useState } from "react"
import axios from "../config/axiosConfig"
import { toast } from "react-hot-toast"
import Layout from "./Layout"

const UpdateLeave = () => {
  const [leaveRequests, setLeaveRequests] = useState([])
  const [remarks, setRemarks] = useState({})
  const [statuses, setStatuses] = useState({})

  useEffect(() => {
    const fetchLeaveRequests = async () => {
      try {
        const response = await axios.get("/api/leaveRequest/unapproved")
        console.log({ response: response.data })
        // Sort leave requests: pending first, then rejected, then approved
        const sortedRequests = response.data.sort((a, b) => {
          if (a.status === "PENDING" && b.status !== "PENDING") return -1
          if (a.status === "REJECTED" && b.status === "PENDING") return 1
          if (a.status === "APPROVED" && b.status !== "APPROVED") return 1
          return 0
        })
        setLeaveRequests(sortedRequests)
      } catch (error) {
        console.error("Error fetching leave requests:", error)
      }
    }

    fetchLeaveRequests()
  }, [])

  const handleStatusChange = (id, newStatus) => {
    setStatuses((prev) => ({ ...prev, [id]: newStatus }))
  }

  const handleRemarkChange = (id, remark) => {
    setRemarks((prev) => ({ ...prev, [id]: remark }))
  }

  const handleUpdateLeave = async (id) => {
    try {
      const data = { action: statuses[id], remark: remarks[id] }
      await axios.post(`/api/leaveRequest/update/${id}`, data)
      toast.success("Leave status updated successfully!")
      const response = await axios.get("/api/leaveRequest/unapproved")
      console.log({ response: response.data })
      // Sort leave requests: pending first, then rejected, then approved
      const sortedRequests = response.data.sort((a, b) => {
        if (a.status === "PENDING" && b.status !== "PENDING") return -1
        if (a.status === "REJECTED" && b.status === "PENDING") return 1
        if (a.status === "APPROVED" && b.status !== "APPROVED") return 1
        return 0
      })
      setLeaveRequests(sortedRequests)
    } catch (error) {
      console.error("Error updating leave status:", error)
      toast.error("Failed to update leave status.")
    }
  }

  return (
    <Layout>
      <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
        <h1 className="text-2xl font-light text-gray-800 mb-6 text-center">Update Leave Requests</h1>
        {leaveRequests.length === 0 ? (
          <p className="text-center text-gray-600">No pending leave requests</p>
        ) : (
          <div className="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3">
            {leaveRequests.map((request) => (
              <div key={request.id} className="bg-white rounded-lg shadow-sm p-6 flex flex-col space-y-4">
                <div className="space-y-2">
                  <h2 className="text-xl font-medium text-gray-800">Leave Request</h2>
                  <p className="text-sm text-gray-600">
                    <span className="font-medium">From:</span> {request.startDate}
                  </p>
                  <p className="text-sm text-gray-600">
                    <span className="font-medium">To:</span> {request.endDate}
                  </p>
                  <p className="text-sm text-gray-600">
                    <span className="font-medium">Student:</span> {request.student.name}
                  </p>
                  <p className="text-sm">
                    <span className="font-medium text-gray-600">Status:</span>
                    <span
                      className={`ml-1 font-medium ${
                        request.status === "APPROVED"
                          ? "text-green-600"
                          : request.status === "REJECTED"
                          ? "text-red-600"
                          : "text-yellow-600"
                      }`}
                    >
                      {request.status}
                    </span>
                  </p>
                </div>
                <div className="space-y-4">
                  <div>
                    <label htmlFor={`remarks-${request.id}`} className="block text-sm font-medium text-gray-700 mb-1">
                      Remarks
                    </label>
                    <input
                      id={`remarks-${request.id}`}
                      type="text"
                      placeholder="Enter remarks"
                      className="w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-1 focus:ring-gray-500 focus:border-gray-500"
                      onChange={(e) => handleRemarkChange(request.id, e.target.value)}
                    />
                  </div>
                  <div>
                    <label htmlFor={`status-${request.id}`} className="block text-sm font-medium text-gray-700 mb-1">
                      Update Status
                    </label>
                    <select
                      id={`status-${request.id}`}
                      onChange={(e) => handleStatusChange(request.id, e.target.value)}
                      className="w-full p-2 border border-gray-300 rounded-md shadow-sm focus:ring-1 focus:ring-gray-500 focus:border-gray-500"
                      value={statuses[request.id] || ""}
                    >
                      <option value="">Select Status</option>
                      {request.status === "PENDING" && (
                        <>
                          <option value="REJECTED">Reject</option>
                          <option value="APPROVED">Accept</option>
                        </>
                      )}
                      {request.status === "REJECTED" && (
                        <>
                          <option value="PENDING">Pending</option>
                          <option value="APPROVED">Accept</option>
                        </>
                      )}
                      {request.status === "APPROVED" && (
                        <>
                          <option value="PENDING">Pending</option>
                          <option value="REJECTED">Reject</option>
                        </>
                      )}
                    </select>
                  </div>
                  <button
                    className="w-full p-2 bg-gray-800 text-white rounded-md hover:bg-gray-700 transition duration-200 focus:outline-none focus:ring-2 focus:ring-gray-500"
                    onClick={() => handleUpdateLeave(request.id)}
                  >
                    Update Status
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </Layout>
  )
}

export default UpdateLeave
