package com.emplyeeMgtSystem.EmpMgtSys.service.implementation;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.emplyeeMgtSystem.EmpMgtSys.exception.ResourceNotFoundException;
import com.emplyeeMgtSystem.EmpMgtSys.exception.UserNotFoundException;
import com.emplyeeMgtSystem.EmpMgtSys.model.Employee;
import com.emplyeeMgtSystem.EmpMgtSys.model.LeaveRequest;
import com.emplyeeMgtSystem.EmpMgtSys.model.Student;
import com.emplyeeMgtSystem.EmpMgtSys.repository.LeaveRequestRepository;
import com.emplyeeMgtSystem.EmpMgtSys.request.ApplyLeaveRequest;
import com.emplyeeMgtSystem.EmpMgtSys.service.EmployeeService;
import com.emplyeeMgtSystem.EmpMgtSys.service.LeaveRequestService;
import com.emplyeeMgtSystem.EmpMgtSys.service.StudentService;

@Service
public class LeaveRequestServiceImplementation implements LeaveRequestService {
    private LeaveRequestRepository leaveRequestRepository;
    private StudentService studentService;
    private EmployeeService employeeService;
    
    public LeaveRequestServiceImplementation(LeaveRequestRepository leaveRequestRepository,StudentService studentService,EmployeeService employeeService) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.studentService = studentService;
        this.employeeService = employeeService;
    }
    @Override
    public List<LeaveRequest> applyLeaveRequest(ApplyLeaveRequest leaveRequest,String email) throws UserNotFoundException {
        if (email == null || email.isEmpty()) {
            throw new UserNotFoundException("Email cannot be null or empty.");
        }

        if (leaveRequest == null) {
            throw new IllegalArgumentException("LeaveRequest object cannot be null.");
        }
    
        if (leaveRequest.getFrom() == null) {
            throw new IllegalArgumentException("Start date cannot be null.");
        }
        if (leaveRequest.getEndDate() == null) {
            throw new IllegalArgumentException("End date cannot be null.");
        }

        if(leaveRequest.getFrom().isAfter(leaveRequest.getEndDate())){
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }
        if(leaveRequest.getFrom().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Start date cannot be before current date.");
        }
        if(leaveRequest.getEndDate().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("End date cannot be before current date.");
        }

        Student student = studentService.getStudentByEmail(email);
        if(student == null){
            throw new UserNotFoundException("Student not found with email -> "+ email);       
        }
        Long studentId = student.getId();

        // create a new Leave Request
        LeaveRequest leave = new LeaveRequest();
        leave.setStudent(student);
        leave.setReason(leaveRequest.getReason());
        leave.setStartDate(leaveRequest.getFrom());
        leave.setEndDate(leaveRequest.getEndDate());
        leave.setStatus("PENDING");
        leave.setTrainer(null);
        leave.setRemark(null);

        List<LeaveRequest> leaveRequests = student.getLeaveRequests();
        leaveRequests.add(leave);

        student.setLeaveRequests(leaveRequests);
        studentService.saveStudent(student);
        return student.getLeaveRequests();
    }   

    @Override
    public List<LeaveRequest> getLeaveRequests(String email) throws UserNotFoundException {
        if (email == null || email.isEmpty()) {
            throw new UserNotFoundException("Email cannot be null or empty.");
        }
        Student student = studentService.getStudentByEmail(email);
        if(student == null){
               throw new UserNotFoundException("Student not found with email -> "+ email);       }
        return student.getLeaveRequests();
    }
    
    @Override
    public LeaveRequest updateLeaveRequest(String email,String action,Long id,String remark) throws ResourceNotFoundException, UserNotFoundException {
        if (email == null || email.isEmpty()) {
            throw new UserNotFoundException("Email cannot be null or empty.");
        }
        if (action == null || action.isEmpty()) {
            throw new IllegalArgumentException("Action cannot be null or empty.");
        }
        if (id == null) {
            throw new IllegalArgumentException("Leave request ID cannot be null.");
        }
    
        if (!(action.equals("PENDING") || action.equals("APPROVED") || action.equals("REJECTED"))) {
            throw new ResourceNotFoundException("Invalid action. Allowed actions: [PENDING, APPROVED, REJECTED]");
        }
        Employee employee = employeeService.getEmployeeByEmail(email);
        if(employee == null){
            throw new UserNotFoundException("You are not authorized to do this");       
        }
        
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id).orElse(null);
        if(leaveRequest == null){
            throw new ResourceNotFoundException("Leave request not found with id -> "+ id);
        }
        
        if(!(action.equals("PENDING") || action.equals("APPROVED") || action.equals("REJECTED"))){
            throw new ResourceNotFoundException("Invalid action allowed actions : [PENDING,APPROVED,REJECTED]");
        }

        Student student = leaveRequest.getStudent();
        List<Student>trainees = employee.getTrainees();
        if(!trainees.contains(student)){
            throw new ResourceNotFoundException("UnAuthorized !! You can only update request of your trainees");
        }
        
        leaveRequest.setRemark(remark);
        leaveRequest.setStatus(action);
        leaveRequest.setTrainer(employee);
        LeaveRequest updateLeaveRequest = leaveRequestRepository.save(leaveRequest);
        return updateLeaveRequest;
    }

    @Override
    public List<LeaveRequest> getUnapprovedLeaveRequests(String email) throws UserNotFoundException {
        if (email == null || email.isEmpty()) {
            throw new UserNotFoundException("Email cannot be null or empty.");
        }
        Employee employee = employeeService.getEmployeeByEmail(email);
        if(employee == null){
            throw new UserNotFoundException("Employee not found with email -> "+ email);       
        }
        List<Student>trainees = employee.getTrainees();
        List<LeaveRequest> unapprovedLeaveRequests = leaveRequestRepository.findByStatusIn(Arrays.asList("PENDING","REJECTED"));
        List<LeaveRequest> unapprovedLeaveRequestsForTrainees = unapprovedLeaveRequests.stream().filter(leaveRequest -> trainees.contains(leaveRequest.getStudent())).toList();

        return unapprovedLeaveRequestsForTrainees;
    }

    @Override
    public List<LeaveRequest> getStudentLeaveHistory(String email) throws UserNotFoundException {
        if (email == null || email.isEmpty()) {
            throw new UserNotFoundException("Email cannot be null or empty.");
        }
        Student student = studentService.getStudentByEmail(email);
        if(student == null){
               throw new UserNotFoundException("Student not found with email -> "+ email);       }
        return student.getLeaveRequests();
    }
}