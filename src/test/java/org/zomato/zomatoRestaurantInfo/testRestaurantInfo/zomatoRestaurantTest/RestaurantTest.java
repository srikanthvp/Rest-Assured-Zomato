package org.zomato.zomatoRestaurantInfo.testRestaurantInfo.zomatoRestaurantTest;

import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.zomato.commonUtils.BaseClass;
import org.zomato.zomatoRestaurantInfo.common.RestaurantUtil;
import org.zomato.zomatoRestaurantInfo.helper.zomatoCommon.categoriesHelper.CategoriesConstants;
import org.zomato.zomatoRestaurantInfo.helper.zomatoCommon.categoriesHelper.CategoriesHelper;

import java.io.FileNotFoundException;

public class RestaurantTest extends BaseClass {
    private static ValidatableResponse response;
    RestaurantUtil restaurantUtil = new RestaurantUtil();
    String collectionId, collectionName;
    CategoriesHelper categoriesHelper = new CategoriesHelper();

    @Test(description = "Create Search Based Collection of Landmark Category")
    public void createLandmarkSearchCollectionTest() throws FileNotFoundException {

        response = categoriesHelper.getCategories(CategoriesConstants.URI_GET_CATEGORY);
        Assert.assertEquals(response.extract().statusCode(), 200);
    }
}
