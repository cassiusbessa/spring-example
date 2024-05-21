package com.springboot.example.restful.repositories;

import org.springframework.stereotype.Repository;

import com.springboot.example.restful.model.Person;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{

    

}
