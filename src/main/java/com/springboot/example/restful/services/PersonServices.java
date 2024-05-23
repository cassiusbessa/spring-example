package com.springboot.example.restful.services;

import java.util.logging.Logger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.example.restful.PersonController;
import com.springboot.example.restful.dto.v1.PersonDTO;
import com.springboot.example.restful.dto.v2.PersonDTOV2;
import com.springboot.example.restful.exceptions.RequiredObjectIsNullException;
import com.springboot.example.restful.exceptions.ResourceNotFoundException;
import com.springboot.example.restful.mapper.PersonMapper;
import com.springboot.example.restful.mapper.v1.Mapper;
import com.springboot.example.restful.model.Person;
import com.springboot.example.restful.repositories.PersonRepository;

import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    PersonMapper mapper;


    public List<PersonDTO> findAll() {
        logger.info("Finding all persons!");

        var persons = Mapper.mapList(repository.findAll(), PersonDTO.class);
        
        for (PersonDTO dto : persons) {
            dto.add(linkTo(methodOn(PersonController.class).findById(dto.getKey())).withSelfRel());
        }

        return persons;
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one person!");

        new Person(counter.incrementAndGet(), "John", "Doe", "Some address", "Male");
        var dto =  Mapper.map(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No person found with id: " + id)), PersonDTO.class);

        dto.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return dto;
    }

    public PersonDTO create(PersonDTO person) {

        if (person == null) {
            throw new RequiredObjectIsNullException( );
        }

        logger.info("Creating a person!");

        var newPerson = Mapper.map(repository.save(Mapper.map(person, Person.class)), PersonDTO.class);
        
        newPerson.add(linkTo(methodOn(PersonController.class).findById(newPerson.getKey())).withSelfRel());
        return newPerson;
    }

    public PersonDTOV2 createV2(PersonDTOV2 person) {
        logger.info("Creating a person!");

        return mapper.map(repository.save(mapper.map(person)));
    }

    public PersonDTO update(PersonDTO person) {

        if (person == null) {
            throw new RequiredObjectIsNullException();
        }

        logger.info("Updating a person!");

        var entity = repository.findById(person.getKey()).orElseThrow(() -> new ResourceNotFoundException("No person found with id: " + person.getKey()));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var updatedPerson = Mapper.map(repository.save(Mapper.map(person, Person.class)), PersonDTO.class);

        updatedPerson.add(linkTo(methodOn(PersonController.class).findById(updatedPerson.getKey())).withSelfRel());
        return updatedPerson;
    }

    public void delete(Long id) {
        logger.info("Deleting a person!");

        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No person found with id: " + id));

        repository.delete(entity);        
    }

}
 