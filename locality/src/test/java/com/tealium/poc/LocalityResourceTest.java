package com.tealium.poc;

import static io.restassured.RestAssured.given;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;

@QuarkusTest
public class LocalityResourceTest {

    @Test
    public void testPersonalitiesEndpoint() {
        Response response = given()
          .when().get("/localities");
        
        List<Locality> personalities = response.as(new TypeRef<List<Locality>>() {});
        
        response.then().statusCode(200);
        Assertions.assertEquals(personalities.size(), 4, "Incorrect amount of Locality rows");
    }

}