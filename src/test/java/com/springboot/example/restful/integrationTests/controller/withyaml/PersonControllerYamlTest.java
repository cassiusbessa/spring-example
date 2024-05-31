package com.springboot.example.restful.integrationTests.controller.withyaml;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;

import static io.restassured.RestAssured.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.springboot.example.restful.config.TestConfigs;
import com.springboot.example.restful.dto.v1.TokenDTO;
import com.springboot.example.restful.integrationTests.AccountCredentialsDTO;
import com.springboot.example.restful.integrationTests.PersonDTO;
import com.springboot.example.restful.integrationTests.testcontainers.AbstractIntegrationTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerYamlTest extends AbstractIntegrationTest {
	
	private static RequestSpecification specification;
	private static MapperYaml objectMapper;

	private static PersonDTO person;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new MapperYaml();
		
		person = new PersonDTO();
	}
	
	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		
		AccountCredentialsDTO user = new AccountCredentialsDTO("leandro", "admin123");
		
		var accessToken = given()
                .config(
                    RestAssuredConfig.config().encoderConfig(
                        EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)
                    )
                )
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_YAML)
                    .accept(TestConfigs.CONTENT_TYPE_YAML)
				.body(user, objectMapper)
					.when()
				.post()
					.then()
						.statusCode(200)
							.extract()
							.body()
								.as(TokenDTO.class, objectMapper)
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
		
		var persistedPerson = given().spec(specification)
                .config(
                    RestAssuredConfig.config().encoderConfig(
                        EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)
                    )
                )
				.contentType(TestConfigs.CONTENT_TYPE_YAML)
                .accept(TestConfigs.CONTENT_TYPE_YAML)
					.body(person, objectMapper)
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.as(PersonDTO.class, objectMapper);
		
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
        
        var persistedPerson = given().spec(specification)
                .config(
                    RestAssuredConfig.config().encoderConfig(
                        EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)
                    )
                )
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .accept(TestConfigs.CONTENT_TYPE_YAML)
                    .pathParam("id", person.getId())
                    .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
							.as(PersonDTO.class, objectMapper);
        
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
        
        var persistedPerson = given().spec(specification)
                .config(
                    RestAssuredConfig.config().encoderConfig(
                        EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)
                    )
                )
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .accept(TestConfigs.CONTENT_TYPE_YAML)
                    .body(person, objectMapper)
                    .when()
                    .put()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
							.as(PersonDTO.class, objectMapper);

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
                .config(
                    RestAssuredConfig.config().encoderConfig(
                        EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)
                    )
                )
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .accept(TestConfigs.CONTENT_TYPE_YAML)
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
        
        var persons = given().spec(specification)
                .config(
                    RestAssuredConfig.config().encoderConfig(
                        EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)
                    )
                )
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .accept(TestConfigs.CONTENT_TYPE_YAML)
                    .when()
                    .get()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
							.as(PersonDTO[].class, objectMapper);        
        assertTrue(persons.length > 0);
    }

    public void testWithInvalidToken() throws JsonMappingException, JsonProcessingException {
        mockPerson();
        
        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .config(
                    RestAssuredConfig.config().encoderConfig(
                        EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)
                    )
                )
                .accept(TestConfigs.CONTENT_TYPE_YAML)
                    .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + "invalid_token")
                    .body(person)
                    .when()
                    .post()
                .then()
                    .statusCode(403)
                        .extract()
                        .body()
                            .asString();
        
    }


    @Test
    @Order(7)
    public void testNotAllowedCORS()  throws JsonMappingException, JsonProcessingException {
        mockPerson();
        
        var content = given().spec(specification)
                .config(
                    RestAssuredConfig.config().encoderConfig(
                        EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)
                    )
                )
                .contentType(TestConfigs.CONTENT_TYPE_YAML)
                .accept(TestConfigs.CONTENT_TYPE_YAML)
                    .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_NOT_ALLOWED)
                    .body(person, objectMapper)
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
    }

}

