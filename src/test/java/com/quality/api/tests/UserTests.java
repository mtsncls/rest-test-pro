package com.quality.api.tests;

import com.quality.api.models.User;
import com.quality.api.utils.CommonAssertions;
import com.quality.api.utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Epic("Petstore API Automation")
@Feature("User Management")
public class UserTests extends BaseTest {

    private static final String USER_SCHEMA = "schemas/user-schema.json";

    @Test(description = "Create a new user")
    @Severity(SeverityLevel.BLOCKER)
    @Story("As an admin, I want to create new users in the petstore")
    public void createNewUser() {
        User payload = TestDataGenerator.generateRandomUser();

        Response response = userClient.createUser(payload);

        CommonAssertions.assertSuccess(response);
        CommonAssertions.assertMessage(response, payload.getId().toString());
    }

    @Test(description = "Retrieve user by username")
    @Severity(SeverityLevel.CRITICAL)
    @Story("As a user, I want to see my profile data")
    public void shouldGetUserByUsername() {
        User payload = TestDataGenerator.generateRandomUser();

        userClient.createUser(payload);

        Response response = userClient.getUser(payload.getUsername());
        CommonAssertions.assertSuccess(response);

        response.then().body("username", is(payload.getUsername()),
                "email", is(payload.getEmail()));
    }

    @Test(description = "Verify user schema validation")
    @Severity(SeverityLevel.NORMAL)
    @Story("Verify that user objects match the Swagger contract")
    public void verifyUserSchema() {
        User payload = TestDataGenerator.generateRandomUser();
        userClient.createUser(payload);

        Response response = userClient.getUser(payload.getUsername());

        CommonAssertions.assertSuccess(response);
        CommonAssertions.assertSchema(response, USER_SCHEMA);
    }

    @Test(description = "Delete a user")
    @Severity(SeverityLevel.MINOR)
    @Story("As a user, I want to be able to delete my account")
    public void deleteUser() {
        User payload = TestDataGenerator.generateRandomUser();
        userClient.createUser(payload);

        userClient.deleteUser(payload.getUsername())
                .then()
                .spec(responseSpec())
                .statusCode(200)
                .statusLine(containsString("OK"))
                .body("message", is(payload.getUsername()));
    }

    @Test(description = "Login and logout session")
    @Severity(SeverityLevel.NORMAL)
    @Story("As a user, I want to manage my session")
    public void shouldLoginAndLogout() {
        User payload = TestDataGenerator.generateRandomUser();
        userClient.createUser(payload);

        userClient.login(payload.getUsername(), payload.getPassword())
                .then()
                .statusCode(200)
                .statusLine(containsString("OK"));

        userClient.logout()
                .then()
                .statusCode(200)
                .statusLine(containsString("OK"));
    }
}
