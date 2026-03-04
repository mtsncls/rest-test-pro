package com.quality.api.tests;

import com.quality.api.clients.PetClient;
import com.quality.api.clients.StoreClient;
import com.quality.api.clients.UserClient;
import com.quality.api.specs.BaseSpec;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public abstract class BaseTest {

    protected final PetClient petClient = new PetClient();
    protected final UserClient userClient = new UserClient();
    protected final StoreClient storeClient = new StoreClient();

    protected RequestSpecification request() {
        return RestAssured.given()
                .spec(BaseSpec.getRequestSpec());
    }

    protected RequestSpecification requestWithKey(String key) {
        return RestAssured.given()
                .spec(BaseSpec.getRequestSpecWithKey(key));
    }

    protected RequestSpecification requestWithOAuth(String token) {
        return RestAssured.given()
                .spec(BaseSpec.getOAuthRequestSpec(token));
    }

    protected ResponseSpecification responseSpec() {
        return BaseSpec.getResponseSpec();
    }
}
