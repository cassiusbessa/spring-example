package com.springboot.example.restful.services;

import java.util.logging.Logger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.example.restful.dto.v1.PersonDTO;
import com.springboot.example.restful.exceptions.ResourceNotFoundException;
import com.springboot.example.restful.mapper.v1.DozerMapper;
import com.springboot.example.restful.model.Person;
import com.springboot.example.restful.repositories.PersonRepository;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;


    public List<PersonDTO> findAll() {
        logger.info("Finding all persons!");

        return  DozerMapper.mapList(repository.findAll(), PersonDTO.class); 
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one person!");

        new Person(counter.incrementAndGet(), "John", "Doe", "Some address", "Male");
        return DozerMapper.map(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No person found with id: " + id)), PersonDTO.class);
    }

    public PersonDTO create(PersonDTO person) {
        logger.info("Creating a person!");

        return DozerMapper.map(repository.save(DozerMapper.map(person, Person.class)), PersonDTO.class);
    }

    public PersonDTO update(PersonDTO person) {
        logger.info("Updating a person!");

        var entity = repository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No person found with id: " + person.getId()));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return DozerMapper.map(repository.save(DozerMapper.map(person, Person.class)), PersonDTO.class);
    }

    public void delete(Long id) {
        logger.info("Deleting a person!");

        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No person found with id: " + id));

        repository.delete(entity);        
    }

}
 