package com.telefonica.architecture.tokenmanager.manager;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class TokenManagerResourceTest {

    @Test
    public void testTokenEndpoint() {
        given()
          .when().get("/token?provider=openshift")
          .then()
             .statusCode(200);
    }

}