package org.zomato.zomatoRestaurantInfo.testRestaurantInfo.zomatoRestaurantTest;

import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.zomato.commonUtils.BaseClass;
import org.zomato.commonUtils.CSVParametersProvider;
import org.zomato.commonUtils.DataFileParameters;
import org.zomato.zomatoRestaurantInfo.helper.zomatoRestaurant.restaurantHelper.RestaurantConstants;
import org.zomato.zomatoRestaurantInfo.helper.zomatoRestaurant.restaurantHelper.RestaurantHelper;

import java.util.HashMap;

public class RestaurantTest extends BaseClass {
    private static ValidatableResponse response;
    RestaurantHelper restaurantHelper = new RestaurantHelper();

    @Test(alwaysRun = true,
            description = "This test returns details of restaurants, if restaurantID is passed as query param",
            groups = "Regression",
            priority = 1,
            dataProvider = "csv",
            dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "restaurants.csv", path = "/resources/input-data/Zomato/")
    public void getRestaurantDetailsTest(String restId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("res_id", restId);

        String expectedName = "Ninth Street Espresso";

        response = restaurantHelper.getRestaurantDetails(RestaurantConstants.URI_GET_RESTAURANT, map);
        Assert.assertEquals(response.extract().statusCode(), 200);

        responseJson = response.extract().jsonPath();
        int actualRestId = responseJson.get("R.res_id");
        softAssert.assertEquals(actualRestId, Integer.parseInt(restId));
        softAssert.assertEquals(responseJson.get("name"), expectedName);
        softAssert.assertAll();
    }
}
