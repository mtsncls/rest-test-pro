package com.quality.api.clients;

import com.quality.api.models.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class StoreClient extends BaseClient {

    private static final String ORDER_ID_PARAM = "orderId";

    public static final String STORE = "/store";
    public static final String INVENTORY = STORE + "/inventory";
    public static final String ORDER = STORE + "/order";
    public static final String ORDER_BY_ID = ORDER + "/{orderId}";

    @Step("Place a new order for a pet")
    public Response placeOrder(Order order) {
        return request()
                .body(order)
                .post(ORDER);
    }

    @Step("Get order by id: {orderId}")
    public Response getOrder(Long orderId) {
        return request()
                .pathParam(ORDER_ID_PARAM, orderId)
                .get(ORDER_BY_ID);
    }

    @Step("Delete order by id: {orderId}")
    public Response deleteOrder(Long orderId) {
        return request()
                .pathParam(ORDER_ID_PARAM, orderId)
                .delete(ORDER_BY_ID);
    }

    @Step("Get inventory status")
    public Response getInventory() {
        return request()
                .get(INVENTORY);
    }
}
