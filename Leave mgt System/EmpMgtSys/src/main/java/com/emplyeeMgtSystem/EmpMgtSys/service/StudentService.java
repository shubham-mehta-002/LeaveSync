package com.emplyeeMgtSystem.EmpMgtSys.service;

import java.util.List;

import com.emplyeeMgtSystem.EmpMgtSys.exception.ResourceNotFoundException;
import com.emplyeeMgtSystem.EmpMgtSys.exception.UserNotFoundException;
import com.emplyeeMgtSystem.EmpMgtSys.model.Student;

public interface StudentService {
    public Student saveStudent(Student student);
    public Student getStudentById(Long id);
    public List<Student> getAllStudents();
    public Student getStudentByEmail(String email);
    public List<Student>getUnAssignedStudents();
    public Student assignTrainer(String email,Long trainerId) throws UserNotFoundException,ResourceNotFoundException;

}
