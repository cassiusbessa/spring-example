package com.springboot.example.restful;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.example.restful.dto.v1.PersonDTO;
import com.springboot.example.restful.dto.v2.PersonDTOV2;
import com.springboot.example.restful.services.PersonServices;
import com.springboot.example.restful.util.MediaType;

@RestController
@RequestMapping("/person")
public class PersonController {
    

    @Autowired
    private PersonServices service;

    @GetMapping(produces = {MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public List<PersonDTO> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public PersonDTO findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public PersonDTO create(@RequestBody PersonDTO person) {
        return service.create(person);
    }

    @RequestMapping(value = "v2", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public PersonDTOV2 createV2(@RequestBody PersonDTOV2 person) {
        return service.createV2(person);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public PersonDTO update(@RequestBody PersonDTO person) {
        return service.update(person);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

       

}
