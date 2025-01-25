package com.emplyeeMgtSystem.EmpMgtSys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emplyeeMgtSystem.EmpMgtSys.model.User;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);
    List<User> findByRole(String role);
    User findByEmailAndRole(String email, String role);
}
