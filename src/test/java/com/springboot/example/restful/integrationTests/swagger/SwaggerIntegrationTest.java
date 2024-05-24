package com.springboot.example.restful.integrationTests.swagger;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.given;

import com.springboot.example.restful.config.TestConfigs;
import com.springboot.example.restful.integrationTests.testcontainers.AbstractIntegrationTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {

	@Test
	void shouldDisplaySwaggerUiPage() {
		var content = given()
			.basePath("/swagger-ui/index.html")
			.port(TestConfigs.SERVER_PORT)
			.when()
			.get()
			.then()
			.statusCode(200)
			.extract()
			.body()
			.asString();

			assertTrue(content.contains("Swagger UI"));
	}

}
