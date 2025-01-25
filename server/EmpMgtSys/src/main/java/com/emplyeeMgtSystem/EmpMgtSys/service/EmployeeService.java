package com.emplyeeMgtSystem.EmpMgtSys.service;

import java.util.List;

import com.emplyeeMgtSystem.EmpMgtSys.exception.ResourceNotFoundException;
import com.emplyeeMgtSystem.EmpMgtSys.exception.UserNotFoundException;
import com.emplyeeMgtSystem.EmpMgtSys.model.Employee;
import com.emplyeeMgtSystem.EmpMgtSys.model.Student;
import com.emplyeeMgtSystem.EmpMgtSys.request.AssignTraineesRequest;

public interface EmployeeService {
    public Employee saveEmployee(Employee employee);
    public Employee getEmployeeById(Long id);
    public List<Employee> getAllEmployees();
    public Employee getEmployeeByEmail(String email);
    public List<Employee> getEmployeesWithoutTrainees();
    public List<Student> assignTrainees(String email,AssignTraineesRequest assignTraineesRequest) throws UserNotFoundException,ResourceNotFoundException;
    public List<Student> getAllTrainees(String email) throws UserNotFoundException;

}
