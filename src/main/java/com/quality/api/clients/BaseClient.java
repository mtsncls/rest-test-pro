package com.quality.api.clients;

import com.quality.api.specs.BaseSpec;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public abstract class BaseClient {

    protected RequestSpecification request() {
        return RestAssured.given()
                .spec(BaseSpec.getRequestSpec());
    }

    protected RequestSpecification request(String token) {
        return RestAssured.given()
                .spec(BaseSpec.getOAuthRequestSpec(token));
    }

    protected RequestSpecification requestWithKey(String key) {
        return RestAssured.given()
                .spec(BaseSpec.getRequestSpecWithKey(key));
    }

    protected RequestSpecification requestMultipart() {
        return RestAssured.given()
                .spec(BaseSpec.getMultipartRequestSpec());
    }
}
