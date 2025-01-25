package com.emplyeeMgtSystem.EmpMgtSys.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emplyeeMgtSystem.EmpMgtSys.model.Employee;
import com.emplyeeMgtSystem.EmpMgtSys.model.Student;

public interface StudentRepository  extends JpaRepository<Student,Long>
{
    public Optional<Student> findById(Long id); 
    public Student findByEmail(String email);
}
