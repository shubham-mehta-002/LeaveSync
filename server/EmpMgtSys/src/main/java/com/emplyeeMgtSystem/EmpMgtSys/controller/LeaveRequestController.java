package com.emplyeeMgtSystem.EmpMgtSys.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emplyeeMgtSystem.EmpMgtSys.config.JwtProvider;
import com.emplyeeMgtSystem.EmpMgtSys.exception.ResourceNotFoundException;
import com.emplyeeMgtSystem.EmpMgtSys.exception.UserNotFoundException;
import com.emplyeeMgtSystem.EmpMgtSys.model.LeaveRequest;
import com.emplyeeMgtSystem.EmpMgtSys.request.ApplyLeaveRequest;
import com.emplyeeMgtSystem.EmpMgtSys.service.LeaveRequestService;
import com.emplyeeMgtSystem.EmpMgtSys.service.UserService;

@RestController
@RequestMapping("/api/leaveRequest")
public class LeaveRequestController {

    private LeaveRequestService leaveRequestService;
    private UserService userService;
    private JwtProvider jwtProvider;
    public LeaveRequestController(LeaveRequestService leaveRequestService,UserService userService,JwtProvider jwtProvider) {
        this.leaveRequestService = leaveRequestService;
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/apply")    
    public ResponseEntity<List<LeaveRequest>> applyLeaveRequest(@RequestBody ApplyLeaveRequest leaveRequest, @RequestHeader("Authorization") String token) { 
        System.out.println(leaveRequest);
        String email = jwtProvider.getEmailFromToken(token);
        List<LeaveRequest> leaves = leaveRequestService.applyLeaveRequest(leaveRequest,email);
        return new ResponseEntity<List<LeaveRequest>>(leaves,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LeaveRequest>> getLeaveRequests(@RequestHeader("Authorization") String token) {
        String email = jwtProvider.getEmailFromToken(token);
        List<LeaveRequest> leaves = leaveRequestService.getLeaveRequests(email);
        return new ResponseEntity<List<LeaveRequest>>(leaves,HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<LeaveRequest> updateLeaveRequest(@RequestHeader("Authorization") String token , @PathVariable Long id,@RequestBody Map<String,Object> requestBody) throws UserNotFoundException,ResourceNotFoundException {
        String action = (String) requestBody.get("action");
        String remark = (String) requestBody.get("remark");
        String email = jwtProvider.getEmailFromToken(token);
        LeaveRequest leaveRequest = leaveRequestService.updateLeaveRequest(email,action,id,remark);
        return new ResponseEntity<LeaveRequest>(leaveRequest,HttpStatus.OK);
    }

    @GetMapping("/unapproved")
    public ResponseEntity<List<LeaveRequest>> getUnapprovedLeaveRequests(@RequestHeader("Authorization") String token) {
        String email = jwtProvider.getEmailFromToken(token);
        List<LeaveRequest> leaves = leaveRequestService.getUnapprovedLeaveRequests(email);
        return new ResponseEntity<List<LeaveRequest>>(leaves,HttpStatus.OK);
    }

    @GetMapping("/student")
    public ResponseEntity<List<LeaveRequest>> getStudentLeaveHistory(@RequestHeader("Authorization") String token) {
        String email = jwtProvider.getEmailFromToken(token);
        List<LeaveRequest> leaves = leaveRequestService.getStudentLeaveHistory(email);
        return new ResponseEntity<List<LeaveRequest>>(leaves,HttpStatus.OK);
    }

}