package com.quality.api.tests;

import com.quality.api.models.Pet;
import com.quality.api.utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

@Epic("petstore API Automation")
@Feature("Pet Management")
public class PetTests extends BaseTest {

    private final List<Long> petsToCleanup = new ArrayList<>();
    private static final String PET_SCHEMA = "schemas/pet-schema.json";
    private static final String PET_IMAGE_PATH = "src/test/resources/images/test_pet_image.png";

    @AfterMethod(description = "Cleanup created pets")
    public void cleanup() {
        petsToCleanup.forEach(
                petId -> petClient.deletePet(petId)
                .then()
                .statusCode(anyOf(is(200), is(404))));
        petsToCleanup.clear();
    }

    @Test(description = "Verify pet schema validation")
    @Severity(SeverityLevel.NORMAL)
    @Story("As an API consumer, I expect that the pet objects match the JSON schema contract")
    public void verifyPetSchema() {
        Pet payload = TestDataGenerator.generateRandomPet();
        Long petId = petClient.createPet(payload)
                .jsonPath()
                .getLong("id");
        petsToCleanup.add(petId);

        petClient.getPet(petId)
                .then()
                .spec(responseSpec())
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath(PET_SCHEMA));
    }

    @Test(description = "Create a new pet")
    @Severity(SeverityLevel.BLOCKER)
    @Story("As a store admin, I want to add new pets to the inventory")
    public void createNewPet() {
        Pet payload = TestDataGenerator.generateRandomPet();

        Response response = petClient.createPet(payload)
                .then()
                .spec(responseSpec())
                .statusCode(200)
                .body("name", is(payload.getName()))
                .extract().response();

        petsToCleanup.add(response.jsonPath().getLong("id"));
    }

    @Test(description = "Retrieve pet by ID")
    @Severity(SeverityLevel.CRITICAL)
    @Story("As a user, I want to see details of a specific pet")
    public void shouldGetPetById() {
        Pet payload = TestDataGenerator.generateRandomPet();

        Long petId = petClient
                .createPet(payload)
                .jsonPath()
                .getLong("id");

        petsToCleanup.add(petId);

        petClient.getPet(petId)
                .then()
                .spec(responseSpec())
                .statusCode(200)
                .body("id", anyOf(is(petId), is(petId.intValue())))
                .body("name", is(payload.getName()));
    }

    @Test(description = "Update an existing pet")
    @Severity(SeverityLevel.CRITICAL)
    @Story("As a store admin, I want to update pet details")
    public void shouldUpdatePet() {
        Pet payload = TestDataGenerator.generateRandomPet();

        Long petId = petClient
                .createPet(payload)
                .jsonPath()
                .getLong("id");
        petsToCleanup.add(petId);

        payload.setId(petId);
        payload.setStatus("sold");
        payload.setName("UpdatedName");

        petClient.updatePet(payload)
                .then()
                .spec(responseSpec())
                .statusCode(200)
                .body("id", anyOf(is(petId), is(petId.intValue())))
                .body("status", is("sold"));
    }

    @Test(description = "Find pets by status")
    @Severity(SeverityLevel.NORMAL)
    @Story("As a customer, I want to filter pets by status")
    public void shouldFindByStatus() {
        String status = "available";

        petClient.findByStatus(status)
                .then()
                .spec(responseSpec())
                .statusCode(200)
                .body("status", everyItem(is(status)));
    }

    @Test(description = "Delete a pet")
    @Severity(SeverityLevel.CRITICAL)
    @Story("As a store admin, I want to remove pets from the inventory")
    public void shouldDeletePet() {
        Pet payload = TestDataGenerator.generateRandomPet();
        Long petId = petClient.createPet(payload)
                .jsonPath()
                .getLong("id");

        petClient.deletePet(petId)
                .then()
                .spec(responseSpec())
                .statusCode(200);

        petClient.getPet(petId)
                .then()
                .statusCode(404);
    }
    @Test(description = "Upload an image for a pet")
    @Severity(SeverityLevel.NORMAL)
    @Story("As a user, I want to upload an image for my pet")
    public void shouldUploadPetImage() throws IOException {
        Pet payload = TestDataGenerator.generateRandomPet();
        Long petId = petClient.createPet(payload)
                .jsonPath()
                .getLong("id");
        petsToCleanup.add(petId);

        byte[] imageBytes = Files.readAllBytes(Paths.get(PET_IMAGE_PATH));

        petClient.uploadImage(petId, imageBytes)
                .then()
                .spec(responseSpec())
                .statusCode(200)
                .body("message", containsString("test_pet_image.png"));
    }

    @Test(description = "Upload an image for a pet with metadata")
    @Severity(SeverityLevel.NORMAL)
    @Story("As a user, I want to upload an image with additional metadata for my pet")
    public void shouldUploadPetImageWithMetadata() throws IOException {
        Pet payload = TestDataGenerator.generateRandomPet();
        Long petId = petClient.createPet(payload)
                .jsonPath()
                .getLong("id");
        petsToCleanup.add(petId);

        byte[] imageBytes = Files.readAllBytes(Paths.get(PET_IMAGE_PATH));
        String metadata = "This is a puppy";

        petClient.uploadImage(petId, imageBytes, metadata)
                .then()
                .spec(responseSpec())
                .statusCode(200)
                .body("message", containsString(metadata))
                .body("message", containsString("test_pet_image.png"));
    }
}
