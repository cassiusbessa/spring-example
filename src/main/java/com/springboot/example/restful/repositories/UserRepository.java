package com.springboot.example.restful.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springboot.example.restful.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{


    @Query("SELECT u FROM User u WHERE u.username = :userName")
    User findByUsername(@Param("userName") String username);

}
