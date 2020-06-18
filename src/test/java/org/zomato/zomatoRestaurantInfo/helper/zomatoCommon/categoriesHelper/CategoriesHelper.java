package org.zomato.zomatoRestaurantInfo.helper.zomatoCommon.categoriesHelper;


import io.restassured.response.ValidatableResponse;
import org.zomato.BasePackage.BaseClass;

public class CategoriesHelper extends BaseClass {

    public ValidatableResponse getCategories(String uri) {
        response = restaurantRequestBuilder.sendGetRequest(uri);
        logger.logResponse(response.extract().body().jsonPath());
        return response;
    }
}
