package com.emplyeeMgtSystem.EmpMgtSys.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emplyeeMgtSystem.EmpMgtSys.config.JwtProvider;
import com.emplyeeMgtSystem.EmpMgtSys.exception.UserException;
import com.emplyeeMgtSystem.EmpMgtSys.model.Employee;
import com.emplyeeMgtSystem.EmpMgtSys.model.Student;
import com.emplyeeMgtSystem.EmpMgtSys.model.User;
import com.emplyeeMgtSystem.EmpMgtSys.repository.UserRepository;
import com.emplyeeMgtSystem.EmpMgtSys.request.EmployeeRequest;
import com.emplyeeMgtSystem.EmpMgtSys.request.LoginRequest;
import com.emplyeeMgtSystem.EmpMgtSys.request.StudentRequest;
import com.emplyeeMgtSystem.EmpMgtSys.response.AuthResponse;
import com.emplyeeMgtSystem.EmpMgtSys.service.EmployeeService;
import com.emplyeeMgtSystem.EmpMgtSys.service.StudentService;
import com.emplyeeMgtSystem.EmpMgtSys.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

//  /login -> student
//  /emp/login  -> employee

//  /signup -> student
//  /emp/signup  -> employee

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody StudentRequest student) throws UserException{
		if (student.getName() == null || student.getName().isEmpty()) {
            throw new UserException("Name cannot be null or empty.");
        }
        if (student.getEmail() == null || student.getEmail().isEmpty()) {
            throw new UserException("Email cannot be null or empty.");
        }
        if (!student.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new UserException("Invalid email format.");
        }
        if (student.getMobile() == null || student.getMobile().isEmpty()) {
            throw new UserException("Mobile number cannot be null or empty.");
        }
        if (!student.getMobile().matches("\\d{10}")) {
            throw new UserException("Mobile number must be 10 digits.");
        }
        
        String email = student.getEmail();
		String mobile = student.getMobile();
        String name = student.getName();
        Long trainerId = student.getTrainerId();
        

        Employee trainer = null; 
        if (trainerId != null && trainerId > 0) { 
            trainer = employeeService.getEmployeeById(trainerId); 
            if (trainer == null) {
                throw new UserException("Trainer not found with id -> " + trainerId);
            }
        } else {
            System.out.println("Trainer ID is null or invalid, setting trainer to null.");
        }

        Student studentAlreadyExist = studentService.getStudentByEmail(email);
        if(studentAlreadyExist!=null) {
            throw new UserException("Student already exists with email -> "+ email);
        }

        Student newStudent = new Student();
        newStudent.setTrainer(trainer);
        newStudent.setName(name);
        newStudent.setEmail(email);
        newStudent.setMobile(mobile);

        studentService.saveStudent(newStudent);

        Long studentId = newStudent.getId();

		User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(mobile));
		newUser.setRole("STUDENT");
        newUser.setName(name);
		
		User savedUser = userRepository.save(newUser);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(),savedUser.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtProvider.generateToken(authentication);
		
		AuthResponse authResponse = new AuthResponse(token, "Signup Success");
		return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);
	}	
	
    @PostMapping("/emp/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody EmployeeRequest employee) throws UserException{
		System.out.println(employee);
        if (employee.getName() == null || employee.getName().isEmpty()) {
            throw new UserException("Name cannot be null or empty.");
        }
        if (employee.getEmail() == null || employee.getEmail().isEmpty()) {
            throw new UserException("Email cannot be null or empty.");
        }
        if (employee.getMobile() == null || employee.getMobile().isEmpty()) {
            throw new UserException("Mobile number cannot be null or empty.");
        }
        if (!employee.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new UserException("Invalid email format.");
        }
        if (!employee.getMobile().matches("\\d{10}")) {
            throw new UserException("Mobile number must be 10 digits.");
        }
		String email = employee.getEmail();
		String mobile = employee.getMobile();
        String name = employee.getName();
        String designation = employee.getDesignation();
        List<Long> trainees = employee.getTrainees();

        if(trainees.isEmpty()){
            trainees= new ArrayList<>();
        }

        // check if already exists
        Employee employeeAlreadyExist = employeeService.getEmployeeByEmail(email); 
        if(employeeAlreadyExist != null) {
            throw new UserException("Employee already exists with email -> "+ email);
        }

        Employee newEmployee = new Employee();
        newEmployee.setName(name);
        newEmployee.setEmail(email);
        newEmployee.setMobile(mobile);
        newEmployee.setDesignation(designation);

        List<Student> newTrainees = new ArrayList<>();
        for(Long trainee: trainees){
            newTrainees.add(studentService.getStudentById(trainee));
            studentService.getStudentById(trainee).setTrainer(newEmployee);
        }
        newEmployee.setTrainees(newTrainees);

        employeeService.saveEmployee(newEmployee);

        Long employeeId = newEmployee.getId();

		User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(mobile));
		newUser.setRole("EMPLOYEE");
        newUser.setName(name);
		
		User savedUser = userRepository.save(newUser);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(),savedUser.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtProvider.generateToken(authentication);
		
		AuthResponse authResponse = new AuthResponse(token, "Signup Success");
		return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);
	}	
	
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) {
            throw new UserException("Email cannot be null or empty.");
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            throw new UserException("Password cannot be null or empty.");
        }
        System.out.println(loginRequest);
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        User user = userService.findUserByEmailAndRole(email,"STUDENT");
        System.out.println(user);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtProvider.generateToken(authentication);
            return ResponseEntity.ok(new AuthResponse(token, "Login Success"));
        }          
        throw new UserException("Invalid Credentials");
        }
       


    @PostMapping("/emp/login")
    public ResponseEntity<AuthResponse> empLogin(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        System.out.println(loginRequest);
        if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) {
            throw new UserException("Email cannot be null or empty.");
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            throw new UserException("Password cannot be null or empty.");
        }
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        User user = userService.findUserByEmailAndRole(email,"EMPLOYEE");
        System.out.println(user);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtProvider.generateToken(authentication);
            return ResponseEntity.ok(new AuthResponse(token, "Login Success"));
        }else{
            System.out.println("Invalid Credentials");
        }

        throw new UserException("Invalid Credentials");
    }
}
