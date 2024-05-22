package com.springboot.example.restful.mapper;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.springboot.example.restful.dto.v2.PersonDTOV2;
import com.springboot.example.restful.model.Person;

@Service
public class PersonMapper {

    public PersonDTOV2 map(Person person) {
        return new PersonDTOV2(person.getId(), person.getFirstName(), person.getLastName(), person.getAddress(), person.getGender(), new Date());
    }

    public Person map(PersonDTOV2 person) {
        return new Person(person.getId(), person.getFirstName(), person.getLastName(), person.getAddress(), person.getGender());
    }

}
