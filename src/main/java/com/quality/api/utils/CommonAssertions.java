package com.quality.api.utils;

import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;

public final class CommonAssertions {

    private CommonAssertions() {
    }

    public static void assertSuccess(Response response) {
        response.then()
                .statusCode(allOf(greaterThanOrEqualTo(200), lessThan(300)))
                .statusLine(containsString("OK"))
                .time(lessThan(5000L));
    }

    public static void assertStatusLine(Response response, String expectedStatusLine) {
        response.then()
                .statusLine(containsString(expectedStatusLine));
    }

    public static void assertNotFound(Response response, String expectedMessage) {
        response.then()
                .statusCode(404)
                .body("message", is(expectedMessage));
    }

    @io.qameta.allure.Step("Verify response message is: {expectedMessage}")
    public static void assertMessage(Response response, String expectedMessage) {
        response.then()
                .body("message", is(expectedMessage));
    }

    public static void assertSchema(Response response, String schemaPath) {
        response.then()
                .body(io.restassured.module.jsv.
                    JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
    }

}
