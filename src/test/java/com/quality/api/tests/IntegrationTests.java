package com.quality.api.tests;

import com.quality.api.models.Order;
import com.quality.api.models.Pet;
import com.quality.api.models.User;
import com.quality.api.utils.TestDataGenerator;
import io.qameta.allure.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;

@Epic("petstore API Automation")
@Feature("End-to-End Business Workflows")
public class IntegrationTests extends BaseTest {

    private final List<Long> petsToCleanup = new ArrayList<>();
    private final List<Long> ordersToCleanup = new ArrayList<>();
    private String usernameToCleanup;

    @AfterMethod(description = "Business flow cleanup")
    public void cleanup() {
        ordersToCleanup.forEach(storeClient::deleteOrder);
        petsToCleanup.forEach(petClient::deletePet);
        
        if (usernameToCleanup != null) {
            userClient.deleteUser(usernameToCleanup);
        }

        ordersToCleanup.clear();
        petsToCleanup.clear();
        usernameToCleanup = null;
    }

    @Test(description = "End-to-End: User Registration, Pet Creation, and Order Placement")
    @Severity(SeverityLevel.BLOCKER)
    @Story("As a business customer, I want to register, add my inventory, and place orders")
    public void fullBusinessScenarioTest() {
        User user = TestDataGenerator.generateRandomUser();
        usernameToCleanup = user.getUsername();
        userClient.createUser(user)
                .then()
                .statusCode(200);

        userClient.login(user.getUsername(), user.getPassword())
                .then()
                .statusCode(200)
                .statusLine(containsString("OK"))
                .body("message", containsString("logged in user session"));

        Pet pet = TestDataGenerator.generateRandomPet();
        pet.setStatus("available");
        Long petId = petClient.createPet(pet)
                .then().statusCode(200)
                .extract().jsonPath().getLong("id");
        petsToCleanup.add(petId);

        petClient.findByStatus("available")
                .then().statusCode(200)
                .body("id", hasItem(petId));

        Order order = Order.builder()
                .id(System.currentTimeMillis() % 100000)
                .petId(petId)
                .quantity(1)
                .status("placed")
                .complete(false)
                .build();
        
        Long orderId = storeClient.placeOrder(order)
                .then().statusCode(200)
                .extract().jsonPath().getLong("id");
        ordersToCleanup.add(orderId);

        storeClient.getOrder(orderId)
                .then()
                .statusCode(200)
                .body("petId", is(petId))
                .body("status", is("placed"));

        storeClient.getInventory()
                .then()
                .statusCode(200)
                .statusLine(containsString("OK"))
                .body("available", greaterThanOrEqualTo(1));

        userClient.logout()
                .then()
                .statusCode(200)
                .statusLine(containsString("OK"));
    }

    @Test(description = "Verify data consistency across multiple updates")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Ensuring data remains consistent after multiple state changes")
    public void dataConsistencyIntegrationTest() {
        Pet pet = TestDataGenerator.generateRandomPet();
        Long petId = petClient.createPet(pet)
                .then().statusCode(200)
                .extract().jsonPath().getLong("id");
        petsToCleanup.add(petId);

        pet.setId(petId);
        pet.setStatus("pending");
        petClient.updatePet(pet).then().statusCode(200);

        pet.setStatus("sold");
        petClient.updatePet(pet).then().statusCode(200);

        petClient.getPet(petId)
                .then()
                .statusCode(200)
                .body("status", is("sold"));
    }
}
