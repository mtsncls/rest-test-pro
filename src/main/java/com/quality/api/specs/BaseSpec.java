package com.quality.api.specs;

import com.quality.api.utils.ConfigReader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.*;
import io.restassured.specification.*;
import com.quality.api.filters.CustomLoggingFilter;

import static io.restassured.RestAssured.oauth2;
import static io.restassured.http.ContentType.JSON;

public class BaseSpec {

    private static final String BASE_URL = ConfigReader.getBaseUrl();
    private static final String TOKEN = ConfigReader.getToken();
    private static final String API_KEY_HEADER = "api_key";

    private BaseSpec() {
    }

    public static RequestSpecification getRequestSpec() {
        return getBaseRequestSpecBuilder()
                .build();
    }

    public static RequestSpecification getRequestSpecWithKey(String key) {
        return getBaseRequestSpecBuilder()
                .addHeader(API_KEY_HEADER, key)
                .build();
    }

    public static RequestSpecification getOAuthRequestSpec(String accessToken) {
        return getBaseRequestSpecBuilder()
                .setAuth(oauth2(accessToken))
                .build();
    }

    public static RequestSpecification getMultipartRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .addHeader(API_KEY_HEADER, TOKEN)
                .addFilter(new CustomLoggingFilter())
                .addFilter(new AllureRestAssured())
                .setContentType("multipart/form-data")
                .build();
    }

    private static RequestSpecBuilder getBaseRequestSpecBuilder() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .addHeader(API_KEY_HEADER, TOKEN)
                .addFilter(new CustomLoggingFilter())
                .addFilter(new AllureRestAssured())
                .setContentType(JSON)
                .setAccept(JSON);
    }

    public static ResponseSpecification getResponseSpec() {
        return new ResponseSpecBuilder()
                .build();
    }
}
