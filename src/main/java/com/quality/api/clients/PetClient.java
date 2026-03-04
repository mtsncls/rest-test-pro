package com.quality.api.clients;

import com.quality.api.models.Pet;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class PetClient extends BaseClient {

    private static final String PET_ID_PARAM = "petId";

    public static final String PET = "/pet";
    public static final String PET_BY_ID = PET + "/{petId}";
    public static final String FIND_BY_STATUS = PET + "/findByStatus";
    public static final String UPLOAD_IMAGE = PET + "/{petId}/uploadImage";

    @Step("Create a new pet")
    public Response createPet(Pet pet) {
        return request()
                .body(pet)
                .post(PET);
    }

    @Step("Retrieve pet by id: {petId}")
    public Response getPet(Long petId) {
        return request()
                .pathParam(PET_ID_PARAM, petId)
                .get(PET_BY_ID);
    }

    @Step("Update an existing pet's info")
    public Response updatePet(Pet pet) {
        return request()
                .body(pet)
                .put(PET);
    }

    @Step("Delete pet by id: {petId}")
    public Response deletePet(Long petId) {
        return request()
                .pathParam(PET_ID_PARAM, petId)
                .delete(PET_BY_ID);
    }

    @Step("Find pets with status: {status}")
    public Response findByStatus(String status) {
        return request()
                .queryParam("status", status)
                .get(FIND_BY_STATUS);
    }

    @Step("Upload an image for pet id: {petId}")
    public Response uploadImage(Long petId, byte[] imageFile) {
        return uploadImage(petId, imageFile, null);
    }

    @Step("Upload an image for pet id: {petId} with metadata: {additionalMetadata}")
    public Response uploadImage(Long petId, byte[] imageFile, String additionalMetadata) {
        var spec = requestMultipart()
                .pathParam(PET_ID_PARAM, petId)
                .multiPart("file", "test_pet_image.png", imageFile);
        
        if (additionalMetadata != null) {
            spec.multiPart("additionalMetadata", additionalMetadata);
        }
        
        return spec.post(UPLOAD_IMAGE);
    }
}
