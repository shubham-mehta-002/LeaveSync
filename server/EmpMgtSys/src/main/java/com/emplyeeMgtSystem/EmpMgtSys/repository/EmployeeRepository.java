package com.emplyeeMgtSystem.EmpMgtSys.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emplyeeMgtSystem.EmpMgtSys.model.Employee;
import com.emplyeeMgtSystem.EmpMgtSys.model.User;

public interface EmployeeRepository  extends JpaRepository<Employee,Long>
{
    public Optional<Employee> findById(Long id); 
    public Employee findByEmail(String email);
}
