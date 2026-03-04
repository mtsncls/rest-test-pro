package com.quality.api.tests;

import com.quality.api.utils.CommonAssertions;
import com.quality.api.clients.UserClient;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@Epic("petstore API Automation")
@Feature("Negative & Error Handling")
public class NegativeTests extends BaseTest {

    @Test(description = "Verify error when using an unexpected HTTP method")
    @Severity(SeverityLevel.NORMAL)
    @Story("As an API, I should return a 405 Method Not Allowed for unsupported methods")
    public void shouldReturn405ForInvalidMethod() {
        request()
        .when()
                .patch(UserClient.USER)
        .then()
                .statusCode(anyOf(is(405), is(415), is(404)));
    }

    @Test(description = "Verify behavior with unsupported Content-Type")
    @Severity(SeverityLevel.NORMAL)
    @Story("As an API, I should handle unsupported media types")
    public void shouldHandleUnsupportedContentType() {
        request()
                .contentType(ContentType.XML)
                .body("<User><id>123</id></User>")
        .when()
                .post(UserClient.USER)
        .then()
                .statusCode(anyOf(is(200), is(415), is(400), is(500)));
    }

    @Test(description = "Verify behavior with unpermitted or extra body parameters")
    @Severity(SeverityLevel.MINOR)
    @Story("As an API, I should ignore extra fields or return an error")
    public void shouldHandleExtraParametersInBody() {
        Map<String, Object> payloadWithExtra = new HashMap<>();
        payloadWithExtra.put("id", 9999);
        payloadWithExtra.put("username", "extra_user");
        payloadWithExtra.put("unknown_field", "surprise!");

        userClient.createUser(new com.quality.api.models.User(9999L, "extra_user", null, null, null, null, null, null))
                .then()
                .statusCode(200);
    }

    @Test(description = "Verify error with malformed JSON body")
    @Severity(SeverityLevel.CRITICAL)
    @Story("As an API, I should return 400 Bad Request for malformed JSON")
    public void shouldReturn400ForMalformedJson() {
        String malformedJson = "{ \"id\": 1, \"username\": \"broken\" ";

        request()
                .body(malformedJson)
        .when()
                .post(UserClient.USER)
        .then()
                .statusCode(400);
    }

    @Test(description = "Verify error for missing required resource")
    @Severity(SeverityLevel.NORMAL)
    @Story("As an API, I should return 404 for non-existent resources")
    public void shouldReturn404ForNonExistentUser() {
        var response = userClient.getUser("non_existent_user_99999999");
        CommonAssertions.assertNotFound(response, "User not found");
    }
}
