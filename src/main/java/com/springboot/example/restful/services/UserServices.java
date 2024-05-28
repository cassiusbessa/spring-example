package com.springboot.example.restful.services;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.example.restful.repositories.UserRepository;

@Service
public class UserServices implements UserDetailsService {

    private Logger logger = Logger.getLogger(UserServices.class.getName());

    @Autowired
    private UserRepository repository;

    public UserServices(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Finding user by username: " + username);
        var user = repository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        return user;
        
    }

}
