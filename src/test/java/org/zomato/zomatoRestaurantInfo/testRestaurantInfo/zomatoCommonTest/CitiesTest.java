package org.zomato.zomatoRestaurantInfo.testRestaurantInfo.zomatoCommonTest;

import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.zomato.commonUtils.BaseClass;
import org.zomato.commonUtils.CSVParametersProvider;
import org.zomato.commonUtils.DataFileParameters;
import org.zomato.zomatoRestaurantInfo.helper.zomatoCommon.citiesHelper.CitiesConstants;
import org.zomato.zomatoRestaurantInfo.helper.zomatoCommon.citiesHelper.CitiesHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CitiesTest extends BaseClass {
    private static ValidatableResponse response;
    CitiesHelper citiesHelper = new CitiesHelper();

    @Test(alwaysRun = true,
            description = "This test returns details of cities passed as query param",
            groups = "Regression",
            priority = 1,
            dataProvider = "csv",
            dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "cities.csv", path = "/resources/input-data/Zomato/")
    public void getCityDetailsTest(String cityNames) {
        String cityList = cityNames.replace("/", ",");
        String expectedCountry = "India";
        int expectedCountryId = 1;

        List<String> expectedNamesList = new ArrayList<>();
        expectedNamesList.add("Mumbai");
        expectedNamesList.add("Bengaluru");
        Collections.sort(expectedNamesList);

        List<String> actualNamesList = new ArrayList<>();

        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("q", cityList);

        response = citiesHelper.getCityDetails(CitiesConstants.URI_GET_CITIES, queryParams);
        Assert.assertEquals(response.extract().statusCode(), 200);

        responseJson = response.extract().jsonPath();
        for(int i=0; i  < responseJson.getList("location_suggestions").size(); i++){
            actualNamesList.add(responseJson.get("location_suggestions["+i+"].name"));
        }
        softAssert.assertEquals(actualNamesList, expectedNamesList);
        for(int i=0; i  < responseJson.getList("location_suggestions").size(); i++){
            softAssert.assertEquals(responseJson.get("location_suggestions["+i+"].country_name"),
                    expectedCountry);
        }
        for(int i=0; i  < responseJson.getList("location_suggestions").size(); i++){
            int actualCountryId = responseJson.get("location_suggestions["+i+"].country_id");
            softAssert.assertEquals(actualCountryId, expectedCountryId);
        }
        softAssert.assertAll();
    }
}
