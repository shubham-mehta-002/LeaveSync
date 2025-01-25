package com.emplyeeMgtSystem.EmpMgtSys.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.emplyeeMgtSystem.EmpMgtSys.exception.EmployeeNotFoundException;
import com.emplyeeMgtSystem.EmpMgtSys.exception.ResourceNotFoundException;
import com.emplyeeMgtSystem.EmpMgtSys.exception.UserNotFoundException;
import com.emplyeeMgtSystem.EmpMgtSys.model.Employee;
import com.emplyeeMgtSystem.EmpMgtSys.model.Student;
import com.emplyeeMgtSystem.EmpMgtSys.repository.EmployeeRepository;
import com.emplyeeMgtSystem.EmpMgtSys.request.AssignTraineesRequest;
import com.emplyeeMgtSystem.EmpMgtSys.service.EmployeeService;
import com.emplyeeMgtSystem.EmpMgtSys.service.StudentService;

@Service
public class EmployeeServiceImplementation implements EmployeeService{
    private EmployeeRepository employeeRepository;
    private StudentService studentService;

    public EmployeeServiceImplementation(EmployeeRepository employeeRepository,StudentService studentService) {
        this.employeeRepository = employeeRepository;
        this.studentService = studentService;
    }


    @Override
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override   
    public Employee getEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isEmpty()) {
            throw new EmployeeNotFoundException("Employee not found with id -> "+ id);
        }
        return employee.get();
    }

    @Override
    public List<Employee> getAllEmployees() {       
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    @Override
    public List<Employee> getEmployeesWithoutTrainees() {
       List<Employee> employees = employeeRepository.findAll();
       return employees.stream().filter(employee -> employee.getTrainees()==null || employee.getTrainees().isEmpty()).toList();
    }

    @Override
    public List<Student> assignTrainees(String email,AssignTraineesRequest assignTraineesRequest) throws UserNotFoundException,ResourceNotFoundException {
        if (assignTraineesRequest == null) {
            throw new IllegalArgumentException("Request Object can't cannot be null.");
        }
        if (email == null || email.isEmpty()) {
            throw new UserNotFoundException("Email cannot be null or empty.");
        }

        Employee employee = getEmployeeByEmail(email);
        System.out.println(employee);
        if(employee == null){
            throw new UserNotFoundException("Employee not found with email -> "+ email);       
        }
        List<Student>traineesAlreadyPresent = employee.getTrainees();
        System.out.println(traineesAlreadyPresent);
        List<Long> traineeIds = assignTraineesRequest.getTraineeIds();
        System.out.println(traineeIds);
        if(traineeIds.isEmpty()){
            return traineesAlreadyPresent;
        }
        for(Long traineeId : traineeIds){
            Student student = studentService.getStudentById(traineeId);
            if(student == null){
                throw new ResourceNotFoundException("Student not found with id -> "+ traineeId);
            }
            student.setTrainer(employee);
            traineesAlreadyPresent.add(student);
        }
        employee.setTrainees(traineesAlreadyPresent);
        employeeRepository.save(employee);
        return employee.getTrainees();
    }

    @Override
    public List<Student> getAllTrainees(String email) throws UserNotFoundException{
        if(email == null || email.isEmpty()){
            throw new UserNotFoundException("Email cannot be null or empty.");
        }
        Employee employee = getEmployeeByEmail(email);
        if(employee == null){
            throw new UserNotFoundException("Employee not found with email -> "+ email);       
        }
        return employee.getTrainees();
    }
}
