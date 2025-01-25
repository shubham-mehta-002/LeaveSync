package com.emplyeeMgtSystem.EmpMgtSys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emplyeeMgtSystem.EmpMgtSys.model.Employee;
import com.emplyeeMgtSystem.EmpMgtSys.model.LeaveRequest;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByStatusAndTrainer(String status,Employee trainer);
    List<LeaveRequest> findByStatusIn(List<String> status);
}
