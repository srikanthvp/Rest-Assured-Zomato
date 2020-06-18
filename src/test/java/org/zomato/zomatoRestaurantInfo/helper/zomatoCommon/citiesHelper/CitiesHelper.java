package org.zomato.zomatoRestaurantInfo.helper.zomatoCommon.citiesHelper;


import io.restassured.response.ValidatableResponse;
import org.zomato.BasePackage.BaseClass;

import java.util.HashMap;

public class CitiesHelper extends BaseClass {

    public ValidatableResponse getCityDetails(String uri, HashMap<String, String> queryParam) {
        response = restaurantRequestBuilder.sendGetRequest(uri, queryParam);
        logger.logResponse(response.extract().body().jsonPath());
        return response;
    }
}
