package org.zomato.zomatoRestaurantInfo.helper.zomatoLocation.locationsHelper;


import io.restassured.response.ValidatableResponse;
import org.zomato.commonUtils.BaseClass;

import java.util.HashMap;

public class LocationsHelper extends BaseClass {

    public ValidatableResponse getCategories(String uri) {
        HashMap<String, String> queryParam = new HashMap<>();
        response = restaurantRequestBuilder.sendGetRequest(uri, getQueryParam());
        logger.logResponse(response.extract().body().jsonPath());
        return response;
    }

    public HashMap<String, String> getQueryParam(){
        HashMap<String, String> map = new HashMap<>();
        return map;
    }
}
