package org.zomato.zomatoRestaurantInfo.testRestaurantInfo.zomatoCommonTest;

import com.jayway.jsonpath.ReadContext;
import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.zomato.commonUtils.BaseClass;
import org.zomato.zomatoRestaurantInfo.helper.zomatoCommon.categoriesHelper.CategoriesConstants;
import org.zomato.zomatoRestaurantInfo.helper.zomatoCommon.categoriesHelper.CategoriesHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CategoriesTest extends BaseClass {
    private static ValidatableResponse response;
    CategoriesHelper categoriesHelper = new CategoriesHelper();

    @Test(alwaysRun = true,
            description = "This test checks that all categories of restaurants are retrieved",
            groups = "Regression", priority = 1)
    public void getCategoriesTest() {
        String categoriesList = "Delivery,Dine-out,Nightlife,Catching-up,Takeaway,Cafes,Daily Menus,Breakfast," +
                "Lunch,Dinner,Pubs & Bars,Pocket Friendly Delivery,Clubs & Lounges";
        ArrayList<String> expectedList = new ArrayList<>();
        String [] catArray = categoriesList.split(",");
        for (String item : catArray) {
            expectedList.add(item);
        }
        Collections.sort(expectedList);

        response = categoriesHelper.getCategories(CategoriesConstants.URI_GET_CATEGORY);
        //Assertion for success
        Assert.assertEquals(response.extract().statusCode(), 200);

        ReadContext readContext = com.jayway.jsonpath.JsonPath.parse(response.extract().body().asString());
        List<String> actualList = Arrays.asList(readContext.read("$.categories[*].categories.name").toString().replace("[", "").replace("]", "").replace("\"", "").split(","));
        Collections.sort(actualList);

        //Assertion for list of categories retrieved
        softAssert.assertEquals(actualList, expectedList);
        softAssert.assertAll();
    }
}
