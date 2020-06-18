package org.zomato.zomatoRestaurantInfo.testRestaurantInfo.zomatoLocationTest;

import io.restassured.response.ValidatableResponse;
import org.framework.commonUtils.CSVParametersProvider;
import org.framework.commonUtils.DataFileParameters;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.zomato.BasePackage.BaseClass;
import org.zomato.zomatoRestaurantInfo.helper.zomatoLocation.locationDetailsHelper.LocationDetailsConstants;
import org.zomato.zomatoRestaurantInfo.helper.zomatoLocation.locationDetailsHelper.LocationDetailsHelper;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class LocationDetailsTest extends BaseClass {
    private static ValidatableResponse response;
    LocationDetailsHelper locationDetailsHelper = new LocationDetailsHelper();

    @Test(alwaysRun = true,
            description = "This test returns details of a Location passed as query param",
            groups = "Regression",
            priority = 1,
            dataProvider = "csv",
            dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "locationDetails.csv", path = "/resources/input-data/Zomato/")
    public void getLocationDetailsTest(String entId, String entType) throws FileNotFoundException {
        HashMap<String, String> map = new HashMap<>();
        map.put("entity_id", entId);
        map.put("entity_type", entType);

        int expectedEntId = 36932;
        int expectedCityId = 280;
        int expectedCountryId = 216;

        response = locationDetailsHelper.getLocationDetails(LocationDetailsConstants.URI_GET_LOCATIONDETAILS,
                map);
        Assert.assertEquals(response.extract().statusCode(), 200);

        responseJson = response.extract().jsonPath();
        softAssert.assertEquals(responseJson.get("location.entity_type"), "group");
        int actualEntity = responseJson.get("location.entity_id");
        softAssert.assertEquals(actualEntity, expectedEntId);
        softAssert.assertEquals(responseJson.get("location.title"), "Chelsea Market, Chelsea, New York City");
        int actualCityId = responseJson.get("location.city_id");
        softAssert.assertEquals(actualCityId, expectedCityId);
        softAssert.assertEquals(responseJson.get("location.city_name"), "New York City");
        int actualCountryId = responseJson.get("location.country_id");
        softAssert.assertEquals(actualCountryId, expectedCountryId);
        softAssert.assertEquals(responseJson.get("location.country_name"), "United States");
        softAssert.assertAll();
    }
}
