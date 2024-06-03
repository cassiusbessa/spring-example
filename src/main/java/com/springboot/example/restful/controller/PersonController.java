package com.springboot.example.restful.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.example.restful.dto.v1.PersonDTO;
import com.springboot.example.restful.dto.v2.PersonDTOV2;
import com.springboot.example.restful.services.PersonServices;
import com.springboot.example.restful.util.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

// @CrossOrigin
@RestController
@RequestMapping("/person")
@Tag(name = "Person", description = "Endpoints for Person")
public class PersonController {
    

    @Autowired
    private PersonServices service;

    @Operation(summary = "Find all people recorded")
    @Description("This endpoint returns all people recorded in the database")
    @ApiResponse(responseCode = "200", description = "Success", 
                content = @Content(mediaType = MediaType.APPLICATION_JSON, array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))))
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @GetMapping(produces = {MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
        @RequestParam(value = "size", defaultValue = "12") Integer size, @RequestParam(value = "direction", defaultValue = "asc") String direction){

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
        return ResponseEntity.ok(service.findAll(pageable));
    }

    // @CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8081"})
    @Operation(summary = "Find a people by id")
    @Description("This endpoint returns all people recorded in the database")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = PersonDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public PersonDTO findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }


    @Operation(summary = "Create a new person")
    @Description("This endpoint creates a new person in the database")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = PersonDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public PersonDTO create(@RequestBody PersonDTO person) {
        return service.create(person);
    }

    @RequestMapping(value = "v2", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public PersonDTOV2 createV2(@RequestBody PersonDTOV2 person) {
        return service.createV2(person);
    }

    @Operation(summary = "Update a person")
    @Description("This endpoint updates a person in the database")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = PersonDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @RequestMapping(method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public PersonDTO update(@RequestBody PersonDTO person) {
        return service.update(person);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Disable a person")
    @Description("This endpoint disables a person in the database")
    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = PersonDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public PersonDTO disablePerson(@PathVariable(value = "id") Long id) {
        return service.disablePerson(id);
    }

       

}
