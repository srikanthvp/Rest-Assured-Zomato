package org.zomato.zomatoRestaurantInfo.helper.zomatoLocation.locationDetailsHelper;


import io.restassured.response.ValidatableResponse;
import org.zomato.BasePackage.BaseClass;

import java.util.HashMap;

public class LocationDetailsHelper extends BaseClass {

    public ValidatableResponse getLocationDetails(String uri, HashMap<String, String> queryParam) {
        response = restaurantRequestBuilder.sendGetRequest(uri, queryParam);
        logger.logResponse(response.extract().body().jsonPath());
        return response;
    }
}
