package com.springboot.example.restful.integrationTests.repositories;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.springboot.example.restful.integrationTests.testcontainers.AbstractIntegrationTest;
import com.springboot.example.restful.model.Person;
import com.springboot.example.restful.repositories.PersonRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest{

    @Autowired
    private PersonRepository personRepository;

    private static Person person;

    public static void setup() {
        person = new Person();
    }

    @Test
    @Order(1)
    public void testFindByName() throws JsonMappingException, JsonProcessingException {

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.ASC, "firstName"));

        person = personRepository.findPersonsByName("Cassius", pageable).getContent().get(0);
        assertNotNull(person);
        assertEquals("Cássius", person.getFirstName());
    }

    @Test
    @Order(2)
    public void testDisablePerson() {
        personRepository.disablePerson(person.getId());
        Person disabledPerson = personRepository.findById(person.getId()).get();
        assertNotNull(disabledPerson);
        assertEquals(false, disabledPerson.getEnabled());
    }
}
