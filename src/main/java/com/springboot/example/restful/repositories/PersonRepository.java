package com.springboot.example.restful.repositories;

import org.springframework.stereotype.Repository;

import com.springboot.example.restful.model.Person;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{

    @Modifying
    @Query("update Person p set p.enabled = FALSE where p.id =:id")
    void disablePerson(@Param("id")Long id);

    @Query("select p from Person p where p.firstName LIKE LOWER(CONCAT('%', :firstName, '%'))")
    Page<Person> findPersonsByName(@Param("firstName")String name, Pageable pageable);


    

}
