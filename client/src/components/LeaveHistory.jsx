import React, { useEffect, useState } from "react"
import axios from "../config/axiosConfig"
import Layout from "./Layout"

const LeaveHistory = () => {
  const [leaveRequests, setLeaveRequests] = useState([])

  useEffect(() => {
    const fetchLeaveHistory = async () => {
      try {
        const response = await axios.get("/api/leaveRequest")
        setLeaveRequests(response.data)
      } catch (error) {
        console.error("Error fetching leave history:", error)
      }
    }

    fetchLeaveHistory()
  }, [])

  return (
    <Layout>
      <div className="max-w-4xl mx-auto">
        <h1 className="text-2xl font-light text-gray-800 mb-6 text-center">Leave History</h1>

        {leaveRequests.length === 0 ? (
          <p className="text-center text-gray-600">No leave history present.</p>
        ) : (
          <>
            {/* Mobile View - Card Layout */}
            <div className="block md:hidden space-y-4">
              {leaveRequests.map((request) => (
                <div key={request.id} className="bg-white p-4 rounded-lg shadow-sm">
                  <p className="font-medium text-gray-800">
                    From: <span className="font-normal">{request.startDate}</span>
                  </p>
                  <p className="font-medium text-gray-800">
                    To: <span className="font-normal">{request.endDate}</span>
                  </p>
                  <p className="mt-2">
                    <span className="font-medium text-gray-600">Reason:</span> {request.reason}
                  </p>
                  <p>
                    <span className="font-medium text-gray-600">Status:</span>
                    <span
                      className={`font-medium ${
                        request.status === "Approved"
                          ? "text-green-600"
                          : request.status === "Rejected"
                            ? "text-red-600"
                            : "text-yellow-600"
                      }`}
                    >
                      {request.status}
                    </span>
                  </p>
                  <p>
                    <span className="font-medium text-gray-600">Remark:</span>{" "}
                    {request.status !== "Pending" ? request.remark : "-"}
                  </p>
                  <p>
                    <span className="font-medium text-gray-600">Accepted By:</span>{" "}
                    {request.status !== "Pending" ? request.trainer?.name : "-"}
                  </p>
                </div>
              ))}
            </div>

            {/* Tablet & Desktop View - Table Layout */}
            <div className="hidden md:block overflow-x-auto">
              <table className="min-w-full bg-white rounded-lg shadow-sm">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="py-3 px-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      From
                    </th>
                    <th className="py-3 px-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      To
                    </th>
                    <th className="py-3 px-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Reason
                    </th>
                    <th className="py-3 px-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Status
                    </th>
                    <th className="py-3 px-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Remark
                    </th>
                    <th className="py-3 px-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Accepted By
                    </th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200">
                  {leaveRequests.map((request) => (
                    <tr key={request.id} className="hover:bg-gray-50 transition duration-200">
                      <td className="py-4 px-4 whitespace-nowrap">{request.startDate}</td>
                      <td className="py-4 px-4 whitespace-nowrap">{request.endDate}</td>
                      <td className="py-4 px-4">{request.reason}</td>
                      <td className="py-4 px-4 whitespace-nowrap">
                        <span
                          className={`font-medium ${
                            request.status === "Approved"
                              ? "text-green-600"
                              : request.status === "Rejected"
                                ? "text-red-600"
                                : "text-yellow-600"
                          }`}
                        >
                          {request.status}
                        </span>
                      </td>
                      <td className="py-4 px-4">{request.status !== "Pending" ? request.remark : "-"}</td>
                      <td className="py-4 px-4">{request.status !== "Pending" ? request.trainer?.name : "-"}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </>
        )}
      </div>
    </Layout>
  )
}

export default LeaveHistory

