package com.emplyeeMgtSystem.EmpMgtSys.service;

import com.emplyeeMgtSystem.EmpMgtSys.model.User;

public interface UserService {
    public User findUserByEmailAndRole(String email,String role);
    public User getUserByEmail(String email);
}