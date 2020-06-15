package org.zomato.zomatoRestaurantInfo.helper.zomatoCommon.categoriesHelper;


import io.restassured.response.ValidatableResponse;
import org.zomato.commonUtils.BaseClass;

import java.util.HashMap;

public class CategoriesHelper extends BaseClass {

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
