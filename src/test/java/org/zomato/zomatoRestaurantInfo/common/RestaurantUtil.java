package org.zomato.zomatoRestaurantInfo.common;

import com.jayway.jsonpath.JsonPath;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Reporter;
import org.zomato.commonUtils.BaseClass;
import org.zomato.commonUtils.JSONFileReader;

public class RestaurantUtil extends BaseClass {

    ValidatableResponse response;
    JsonPath responseJsonSections = null;
    JSONFileReader jReader = new JSONFileReader();

    public String createRandomName(String prefix, int count){
        String result;
        result = prefix+RandomStringUtils.randomAlphanumeric(count);
        Reporter.log("random generated string is "+result,1,true);
        return result;
    }
}

