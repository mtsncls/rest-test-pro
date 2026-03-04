package com.quality.api.clients;

import com.quality.api.models.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class UserClient extends BaseClient {

    private static final String USERNAME_PARAM = "username";

    public static final String USER = "/user";
    public static final String USER_BY_USERNAME = USER + "/{username}";
    public static final String USER_LOGIN = USER + "/login";
    public static final String USER_LOGOUT = USER + "/logout";

    @Step("Create user: {user.username}")
    public Response createUser(User user) {
        return request()
                .body(user)
                .post(USER);
    }

    @Step("Get user by username: {username}")
    public Response getUser(String username) {
        return request()
                .pathParam(USERNAME_PARAM, username)
                .get(USER_BY_USERNAME);
    }

    @Step("Update user: {username}")
    public Response updateUser(String username, User user) {
        return request()
                .pathParam(USERNAME_PARAM, username)
                .body(user)
                .put(USER_BY_USERNAME);
    }

    @Step("Delete user by username: {username}")
    public Response deleteUser(String username) {
        return request()
                .pathParam(USERNAME_PARAM, username)
                .delete(USER_BY_USERNAME);
    }

    @Step("Login user: {username}")
    public Response login(String username, String password) {
        return request()
                .queryParam(USERNAME_PARAM, username)
                .queryParam("password", password)
                .get(USER_LOGIN);
    }

    @Step("Logout current user")
    public Response logout() {
        return request()
                .get(USER_LOGOUT);
    }
}
