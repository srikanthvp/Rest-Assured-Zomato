package org.zomato.zomatoRestaurantInfo.testRestaurantInfo.zomatoLocationTest;

import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.zomato.commonUtils.BaseClass;
import org.zomato.commonUtils.CSVParametersProvider;
import org.zomato.commonUtils.DataFileParameters;
import org.zomato.zomatoRestaurantInfo.helper.zomatoLocation.locationsHelper.LocationsConstants;
import org.zomato.zomatoRestaurantInfo.helper.zomatoLocation.locationsHelper.LocationsHelper;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class LocationsTest extends BaseClass {
    private static ValidatableResponse response;
    LocationsHelper locationsHelper = new LocationsHelper();

    @Test(alwaysRun = true,
            description = "This test returns details of Locations passed as query param",
            groups = "Regression",
            priority = 1,
            dataProvider = "csv",
            dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "locations.csv", path = "/resources/input-data/Zomato/")
    public void getLocationsTest(String query, String lat, String lon) throws FileNotFoundException {
        HashMap<String, String> map = new HashMap<>();
        map.put("query", query);
        map.put("lat", lat);
        map.put("lon", lon);

        String expectedCountryName = "India";
        int expectedCountryId = 1;

        response = locationsHelper.getLocations(LocationsConstants.URI_GET_LOCATIONS, map);
        Assert.assertEquals(response.extract().statusCode(), 200);

        responseJson = response.extract().jsonPath();
        softAssert.assertEquals(responseJson.get("location_suggestions[0].title"), query);
        softAssert.assertEquals(responseJson.get("location_suggestions[0].city_name"), query);
        softAssert.assertEquals(responseJson.get("location_suggestions[0].country_name"), expectedCountryName);
        int actualCountryId = responseJson.get("location_suggestions[0].country_id");
        softAssert.assertEquals(actualCountryId, expectedCountryId);
        float actualLat = responseJson.get("location_suggestions[0].latitude");
        softAssert.assertEquals(actualLat, Float.parseFloat(lat));
        float actualLon = responseJson.get("location_suggestions[0].longitude");
        softAssert.assertEquals(actualLon, Float.parseFloat(lon));
        softAssert.assertEquals(responseJson.get("status"), "success");
        softAssert.assertEquals(responseJson.get("user_has_addresses"), Boolean.TRUE);
        softAssert.assertAll();
    }
}
