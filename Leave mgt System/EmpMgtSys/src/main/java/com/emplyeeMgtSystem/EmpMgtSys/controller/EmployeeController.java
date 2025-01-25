package com.emplyeeMgtSystem.EmpMgtSys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.emplyeeMgtSystem.EmpMgtSys.config.JwtProvider;
import com.emplyeeMgtSystem.EmpMgtSys.exception.ResourceNotFoundException;
import com.emplyeeMgtSystem.EmpMgtSys.exception.UserNotFoundException;
import com.emplyeeMgtSystem.EmpMgtSys.model.Employee;
import com.emplyeeMgtSystem.EmpMgtSys.model.Student;
import com.emplyeeMgtSystem.EmpMgtSys.request.AssignTraineesRequest;
import com.emplyeeMgtSystem.EmpMgtSys.service.EmployeeService;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtProvider jwtProvider;

    @GetMapping("/get/employees")
    public List<Employee> getEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/get/employees/available")
    public List<Employee> getAvailableEmployees() {
        return employeeService.getEmployeesWithoutTrainees();
    }

    @PostMapping("/api/assign/trainees")
    public List<Student> assignTrainees(@RequestHeader("Authorization") String token,@RequestBody AssignTraineesRequest assignTraineesRequest) throws UserNotFoundException,ResourceNotFoundException {
        String email = jwtProvider.getEmailFromToken(token);
        return employeeService.assignTrainees(email,assignTraineesRequest);
    }

    @GetMapping("/api/trainees")
    public List<Student> getAllTrainees(@RequestHeader("Authorization") String token) throws UserNotFoundException {
        String email = jwtProvider.getEmailFromToken(token);
        return employeeService.getAllTrainees(email);
    }
}
