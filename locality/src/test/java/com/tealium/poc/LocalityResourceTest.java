package com.tealium.poc;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class LocalityResourceTest {

    @Test
    public void testLocalitiesEndpoint() {
        given()
          .when().get("/localities")
          .then()
             .statusCode(200)
             .body(is("Hello from RESTEasy Reactive"));
    }

}