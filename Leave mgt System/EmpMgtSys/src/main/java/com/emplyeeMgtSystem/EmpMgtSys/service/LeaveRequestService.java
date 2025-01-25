package com.emplyeeMgtSystem.EmpMgtSys.service;

import java.util.List;

import com.emplyeeMgtSystem.EmpMgtSys.exception.ResourceNotFoundException;
import com.emplyeeMgtSystem.EmpMgtSys.exception.UserNotFoundException;
import com.emplyeeMgtSystem.EmpMgtSys.model.LeaveRequest;
import com.emplyeeMgtSystem.EmpMgtSys.request.ApplyLeaveRequest;

public interface LeaveRequestService {
    public List<LeaveRequest>  applyLeaveRequest(ApplyLeaveRequest leaveRequest,String email);
    public List<LeaveRequest> getLeaveRequests(String email);
    public LeaveRequest updateLeaveRequest(String email,String action,Long id,String remark) throws UserNotFoundException,ResourceNotFoundException;
    public List<LeaveRequest> getUnapprovedLeaveRequests(String email);
    public List<LeaveRequest> getStudentLeaveHistory(String email);
}
