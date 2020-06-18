package org.zomato.zomatoRestaurantInfo.common;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Reporter;
import org.zomato.BasePackage.BaseClass;

public class RestaurantUtil extends BaseClass {

    public String createRandomName(String prefix, int count){
        String result;
        result = prefix+RandomStringUtils.randomAlphanumeric(count);
        Reporter.log("random generated string is "+result,1,true);
        return result;
    }
}

