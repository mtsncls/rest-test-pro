package com.quality.api.tests;

import com.quality.api.utils.CommonAssertions;
import com.quality.api.clients.UserClient;
import com.quality.api.clients.PetClient;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

import com.quality.api.utils.ConfigReader;

@Epic("petstore API Automation")
@Feature("Authentication & Authorization")
public class AuthTests extends BaseTest {

    private static final String VALID_API_KEY = ConfigReader.getValidApiKey();
    private static final String INVALID_API_KEY = ConfigReader.getInvalidApiKey();

    @Test(description = "Verify API Key Authentication with a valid key")
    @Severity(SeverityLevel.BLOCKER)
    @Story("As a secure API, I should allow access with a valid API key")
    public void shouldAuthorizeWithValidApiKey() {
        var response = requestWithKey(VALID_API_KEY)
        .when()
                .get(UserClient.USER_LOGIN + "?username=test&password=test");
        
        CommonAssertions.assertSuccess(response);
        response.then().body("message", containsString("logged in user session"));
    }

    @Test(description = "Verify API Key behavior with an invalid key")
    @Severity(SeverityLevel.CRITICAL)
    @Story("As a secure API, I should handle requests with invalid keys appropriately")
    public void shouldHandleInvalidApiKey() {
        var response = requestWithKey(INVALID_API_KEY)
        .when()
                .get(UserClient.USER_LOGOUT);
        
        CommonAssertions.assertSuccess(response);
    }

    @Test(description = "Demonstrate OAuth2 Authentication (Implicit Flow)")
    @Severity(SeverityLevel.NORMAL)
    @Story("As an API consumer with write:pets scope, I should be able to use OAuth2")
    public void demonstrateOAuth2Authentication() {
        String mockToken = "mock-oauth2-token-with-read-pets-scope";

        var response = requestWithOAuth(mockToken)
        .when()
                .get(PetClient.FIND_BY_STATUS + "?status=available");
        
        CommonAssertions.assertSuccess(response);
        response.then().body("size()", greaterThan(0));
    }

    @Test(description = "Verify Request without Authentication")
    @Severity(SeverityLevel.MINOR)
    @Story("Some endpoints might be public")
    public void shouldVerifyPublicEndpointAccess() {
        var response = io.restassured.RestAssured.given()
                .baseUri(com.quality.api.utils.ConfigReader.getBaseUrl())
                .contentType("application/json")
        .when()
                .get(PetClient.PET + "/1");
        
        response.then().statusCode(anyOf(is(200), is(404)));
    }
}
