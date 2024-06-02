package com.springboot.example.restful.integrationTests.controller.withxml; 


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;

import static io.restassured.RestAssured.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.springboot.example.restful.config.TestConfigs;
import com.springboot.example.restful.dto.v1.TokenDTO;
import com.springboot.example.restful.integrationTests.AccountCredentialsDTO;
import com.springboot.example.restful.integrationTests.PersonDTO;
import com.springboot.example.restful.integrationTests.testcontainers.AbstractIntegrationTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerXmlTest extends AbstractIntegrationTest {
	
	private static RequestSpecification specification;
	private static XmlMapper objectMapper;

	private static PersonDTO person;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new XmlMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		person = new PersonDTO();
	}
	
	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		
		AccountCredentialsDTO user = new AccountCredentialsDTO("leandro", "admin123");
		
		var accessToken = given()
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_XML)
                    .accept(TestConfigs.CONTENT_TYPE_XML)
				.body(user)
					.when()
				.post()
					.then()
						.statusCode(200)
							.extract()
							.body()
								.as(TokenDTO.class)
							.getAccessToken();
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/person")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	
	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockPerson();
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
					.body(person)
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();
		
		PersonDTO persistedPerson = objectMapper.readValue(content, PersonDTO.class);
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		
        assertTrue(persistedPerson.getId() > 0);
        assertEquals("John", persistedPerson.getFirstName());
        assertEquals("Doe", persistedPerson.getLastName());
        assertEquals("1234 Main St", persistedPerson.getAddress());
        assertEquals("male", persistedPerson.getGender());
	}

    @Test
    @Order(2)
    public void testFindById() throws JsonMappingException, JsonProcessingException {
        // mockPerson();
        
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                    .pathParam("id", person.getId())
                    .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .asString();
        
        PersonDTO persistedPerson = objectMapper.readValue(content, PersonDTO.class);
        person = persistedPerson;
        
        assertNotNull(persistedPerson);
        
        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
        
        assertTrue(persistedPerson.getId() > 0);
        assertEquals("John", persistedPerson.getFirstName());
        assertEquals("Doe", persistedPerson.getLastName());
        assertEquals("1234 Main St", persistedPerson.getAddress());
        assertEquals("male", persistedPerson.getGender());
    }

    @Test
    @Order(3)
    public void update() throws JsonMappingException, JsonProcessingException {
        
        person.setFirstName("Rubens");
        person.setLastName("Barichello");
        person.setAddress("1234 Main St");
        
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                    .body(person)
                    .when()
                    .put()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .asString();

        PersonDTO persistedPerson = objectMapper.readValue(content, PersonDTO.class);
        person = persistedPerson;

        assertNotNull(persistedPerson);
        assertEquals(
            "Rubens",
            persistedPerson.getFirstName()
        );
        assertEquals(
            "Barichello",
            persistedPerson.getLastName()
        );
    }

    @Test
    @Order(4)
    public void delete() throws JsonMappingException, JsonProcessingException {
        
        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                    .pathParam("id", person.getId())
                    .when()
                    .delete("{id}")
                .then()
                    .statusCode(204)
                        .extract()
                        .body()
                            .asString();

    }

    @Test
    @Order(5)
    public void testFindAll() throws JsonMappingException, JsonProcessingException {
        mockPerson();
        
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                    .when()
                    .get()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .asString();
        
        PersonDTO[] persons = objectMapper.readValue(content, PersonDTO[].class);
        
        assertTrue(persons.length > 0);
    }

    @Test
    @Order(6)
    public void testWithInvalidToken() throws JsonMappingException, JsonProcessingException {
        RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
        .setBasePath("/person")
        .setPort(TestConfigs.SERVER_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
        .build();
        
        given().spec(specificationWithoutToken)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                    .body(person)
                    .when()
                    .get()
                .then()
                    .statusCode(401)
                        .extract()
                        .body()
                            .asString();
        
    }


    @Test
    @Order(7)
    public void testNotAllowedCORS()  throws JsonMappingException, JsonProcessingException {
        mockPerson();
        
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                    .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_NOT_ALLOWED)
                    .body(person)
                    .when()
                    .post()
                .then()
                    .statusCode(403)
                        .extract()
                        .body()
                            .asString();
        
        assertTrue(content.contains("Invalid CORS request"));
    }

   

    private void mockPerson() {
        person.setId(1L);
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("1234 Main St");
        person.setGender("male");
        person.setEnabled(true);
    }

}

