package com.emplyeeMgtSystem.EmpMgtSys.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emplyeeMgtSystem.EmpMgtSys.exception.UserNotFoundException;
import com.emplyeeMgtSystem.EmpMgtSys.model.User;
import com.emplyeeMgtSystem.EmpMgtSys.repository.UserRepository;
import com.emplyeeMgtSystem.EmpMgtSys.service.UserService;

@Service
public class UserServiceImplementation implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Override
    public User findUserByEmailAndRole(String email, String role) throws UserNotFoundException {
        if(email == null || email.isEmpty()){
            throw new UserNotFoundException("Email cannot be null or empty.");
        }
        if(role == null || role.isEmpty()){
            throw new UserNotFoundException("Role cannot be null or empty.");
        }
        User user =  userRepository.findByEmailAndRole(email, role);
        if(user == null){
            throw new UserNotFoundException("User not found with email -> "+ email);
        }
        return user;
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        if(email == null || email.isEmpty()){
            throw new UserNotFoundException("Email cannot be null or empty.");
        }
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserNotFoundException("User not found with email -> "+ email);
        }
        return user;
    }
}
