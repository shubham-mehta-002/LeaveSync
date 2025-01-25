package com.emplyeeMgtSystem.EmpMgtSys.service.implementation;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.emplyeeMgtSystem.EmpMgtSys.exception.ResourceNotFoundException;
import com.emplyeeMgtSystem.EmpMgtSys.exception.UserNotFoundException;
import com.emplyeeMgtSystem.EmpMgtSys.model.Employee;
import com.emplyeeMgtSystem.EmpMgtSys.model.Student;
import com.emplyeeMgtSystem.EmpMgtSys.repository.EmployeeRepository;
import com.emplyeeMgtSystem.EmpMgtSys.repository.StudentRepository;
import com.emplyeeMgtSystem.EmpMgtSys.service.EmployeeService;
import com.emplyeeMgtSystem.EmpMgtSys.service.StudentService;

@Service
public class StudentServiceImplementation implements StudentService{

    private StudentRepository studentRepository;
    private EmployeeService employeeService;
    private EmployeeRepository employeeRepository;

    public StudentServiceImplementation(StudentRepository studentRepository,@Lazy EmployeeService employeeService,EmployeeRepository employeeRepository) {
        this.studentRepository = studentRepository;
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }   
    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).get();
    }   
    @Override
    public List<Student> getAllStudents() {
       return studentRepository.findAll();
      
    }   
    @Override
    public Student getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
    @Override
    public List<Student> getUnAssignedStudents() {
        List<Student> students = getAllStudents();
        return students.stream().filter(student -> student.getTrainer() == null).toList();
    }

    @Override
    public Student assignTrainer(String email,Long trainerId) throws UserNotFoundException,ResourceNotFoundException {
        if (email == null || email.isEmpty()) {
            throw new UserNotFoundException("Email cannot be null or empty.");
        }
        if (trainerId == null) {
            throw new IllegalArgumentException("Trainer ID cannot be null.");
        }
        Student student = getStudentByEmail(email);
        Employee trainer = employeeService.getEmployeeById(trainerId);
        if(trainer == null ){
            throw new ResourceNotFoundException("Trainer not found with id -> "+ trainerId);
        }
        if(student == null){
            throw new UserNotFoundException("Student not found with email -> "+ email);
        }
        student.setTrainer(trainer);
        List<Student> trainees = trainer.getTrainees();
        if (!trainees.contains(student)) {
            trainees.add(student);
        }
        employeeRepository.save(trainer);

        return studentRepository.findById(student.getId()).orElse(null);
    }
}
