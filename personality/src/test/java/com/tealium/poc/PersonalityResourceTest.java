package com.tealium.poc;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class PersonalityResourceTest {

    @Test
    public void testPersonalitiesEndpoint() {
        given()
          .when().get("/personalities")
          .then()
             .statusCode(200)
             .body(is("Hello from RESTEasy Reactive"));
    }

}