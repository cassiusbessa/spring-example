package com.springboot.example.restful.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.springboot.example.restful.dto.v1.PersonDTO;
import com.springboot.example.restful.exceptions.RequiredObjectIsNullException;
import com.springboot.example.restful.model.Person;
import com.springboot.example.restful.repositories.PersonRepository;
import com.springboot.example.restful.services.PersonServices;
import com.springboot.example.restful.unittests.mock.MockPerson;



@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

	MockPerson input;
	
	@InjectMocks
	private PersonServices service;
	
	@Mock
	PersonRepository repository;
	
	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockPerson();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindById() {
		Person entity = input.mockEntity(1); 
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		
		var result = service.findById(1L);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
        
		assertTrue(result.toString().contains("links: [</person/1>;rel=\"self\"]"));
		assertEquals("Addres Test1", result.getAddress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
	}
	
	@Test
	void testCreate() {
		Person entity = input.mockEntity(1); 
		entity.setId(1L);
		
		Person persisted = entity;
		persisted.setId(1L);
		
		PersonDTO dto = input.mockDTO();
		dto.setKey(1L);
		
		when(repository.save(any(Person.class))).thenReturn(persisted);
		
		var result = service.create(dto);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		
		assertTrue(result.toString().contains("links: [</person/1>;rel=\"self\"]"));
		assertEquals("Addres Test1", result.getAddress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
	}
	
	@Test
	void testCreateWithNullPerson() {

        PersonDTO dto = null;
        
        Exception excepetion = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(dto);
        });


        String expectedMessage = "It is not allowed to persist a null object.";
        String actualMessage = excepetion.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
	}


	@Test
	void testUpdate() {

        Person entity = input.mockEntity(1); 
        entity.setId(1L);
        
        Person persisted = entity;
        persisted.setId(1L);
        
        PersonDTO dto = input.mockDTO(1);
        dto.setKey(1L);
        
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(any(Person.class))).thenReturn(persisted);
        
        var result = service.update(dto);
        
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        
        assertTrue(result.toString().contains("links: [</person/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
		
	}
	

	
	@Test
	void testUpdateWithNullPerson() {

        PersonDTO dto = null;
        
        Exception excepetion = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(dto);
        });

        String expectedMessage = "It is not allowed to persist a null object.";
        String actualMessage = excepetion.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testDelete() {

        Person entity = input.mockEntity(1);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);
		
	}
	
	@Test
	void testFindAll() {
        List<Person> entities = input.mockEntityList();
        
        when(repository.findAll()).thenReturn(entities);
        
        var result = service.findAll();
        
        assertNotNull(result);
        assertEquals(14, result.size());

        for (PersonDTO dto : result) {
            assertNotNull(dto.getKey());
            assertNotNull(dto.getLinks());
            assertTrue(dto.toString().contains("links: [</person/" + dto.getKey() + ">;rel=\"self\"]"));
        }

	}

}


