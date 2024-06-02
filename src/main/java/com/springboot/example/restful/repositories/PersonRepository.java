package com.springboot.example.restful.repositories;

import org.springframework.stereotype.Repository;

import com.springboot.example.restful.model.Person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{

    @Modifying
    @Query("update Person p set p.enabled = FALSE where p.id =:id")
    void disablePerson(@Param("id")Long id);


    

}
