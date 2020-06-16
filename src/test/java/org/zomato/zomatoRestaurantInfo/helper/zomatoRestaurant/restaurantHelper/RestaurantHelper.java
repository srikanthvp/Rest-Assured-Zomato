package org.zomato.zomatoRestaurantInfo.helper.zomatoRestaurant.restaurantHelper;


import io.restassured.response.ValidatableResponse;
import org.zomato.commonUtils.BaseClass;

import java.util.HashMap;

public class RestaurantHelper extends BaseClass {

    public ValidatableResponse getRestaurantDetails(String uri, HashMap<String, String> queryParam) {
        response = restaurantRequestBuilder.sendGetRequest(uri, queryParam);
        logger.logResponse(response.extract().body().jsonPath());
        return response;
    }
}
