package com.emplyeeMgtSystem.EmpMgtSys.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.emplyeeMgtSystem.EmpMgtSys.config.JwtProvider;
import com.emplyeeMgtSystem.EmpMgtSys.exception.ResourceNotFoundException;
import com.emplyeeMgtSystem.EmpMgtSys.exception.UserNotFoundException;
import com.emplyeeMgtSystem.EmpMgtSys.model.Student;
import com.emplyeeMgtSystem.EmpMgtSys.service.StudentService;


@RestController
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private JwtProvider jwtProvider;

    @GetMapping("/get/students")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("get/students/available")
    public List<Student> getUnAssignedStudents() {
        return studentService.getUnAssignedStudents();
    }
    
    @PostMapping("api/assign/trainer")
    public Student assignTrainer(@RequestHeader("Authorization") String token,@RequestBody Map<String,Long> requestBody) throws UserNotFoundException,ResourceNotFoundException {
        String email = jwtProvider.getEmailFromToken(token);
        return studentService.assignTrainer(email, requestBody.get("trainerId"));
    }

    @GetMapping("get/student/profile")
    public Student getStudentProfile(@RequestHeader("Authorization") String token) {
        String email = jwtProvider.getEmailFromToken(token);
        return studentService.getStudentByEmail(email);
    }

}
