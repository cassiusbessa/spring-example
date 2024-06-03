package com.springboot.example.restful.services;

import java.util.logging.Logger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import com.springboot.example.restful.controller.PersonController;
import com.springboot.example.restful.dto.v1.PersonDTO;
import com.springboot.example.restful.dto.v2.PersonDTOV2;
import com.springboot.example.restful.exceptions.RequiredObjectIsNullException;
import com.springboot.example.restful.exceptions.ResourceNotFoundException;
import com.springboot.example.restful.mapper.PersonMapper;
import com.springboot.example.restful.mapper.v1.Mapper;
import com.springboot.example.restful.model.Person;
import com.springboot.example.restful.repositories.PersonRepository;

import jakarta.transaction.Transactional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    PersonMapper mapper;

    @Autowired
    PagedResourcesAssembler<PersonDTO> assembler;


    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {
        logger.info("Finding all persons!");

        var persons = repository.findAll(pageable).map(entity -> {
            var dto = Mapper.map(entity, PersonDTO.class);
            dto.add(linkTo(methodOn(PersonController.class).findById(dto.getKey())).withSelfRel());
            return dto;
        });
        Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().toString())).withSelfRel();
        return assembler.toModel(persons, link);
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one person!");

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

    @Transactional
    public PersonDTO disablePerson(Long id) {
        logger.info("Disabling a person!");

        repository.disablePerson(id);

        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No person found with id: " + id));

        var person = Mapper.map(entity, PersonDTO.class);

        person.add(linkTo(methodOn(PersonController.class).findById(person.getKey())).withSelfRel());
        return person;
    }

}
 