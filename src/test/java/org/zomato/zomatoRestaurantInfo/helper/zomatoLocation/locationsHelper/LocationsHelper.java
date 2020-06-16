package org.zomato.zomatoRestaurantInfo.helper.zomatoLocation.locationsHelper;


import io.restassured.response.ValidatableResponse;
import org.zomato.commonUtils.BaseClass;

import java.util.HashMap;

public class LocationsHelper extends BaseClass {

    public ValidatableResponse getLocations(String uri, HashMap<String, String> queryParam) {
        response = restaurantRequestBuilder.sendGetRequest(uri, queryParam);
        logger.logResponse(response.extract().body().jsonPath());
        return response;
    }
}
