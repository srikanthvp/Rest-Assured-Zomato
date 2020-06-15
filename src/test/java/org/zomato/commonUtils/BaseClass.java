package org.zomato.commonUtils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import org.framework.Reporting.Logger;
import org.testng.Reporter;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;
import org.zomato.zomatoRestaurantInfo.common.RestaurantRequestBuilder;


public class BaseClass {

    public static ValidatableResponse response;
    public static JsonPath responseJson;
    public static JSONFileReader jReader = new JSONFileReader();
    public static String env, authToken;
    public static Logger logger;
    public static RestaurantRequestBuilder restaurantRequestBuilder;
    public static SoftAssert softAssert = new SoftAssert();

    @BeforeSuite
    public void BaseClass() {
        env = System.getProperty("Environment");
        authToken = System.getProperty("AuthenticationToken");

        Reporter.log("---->>>>>> Env name: " + env + " <<<<<<----", 1, true);
        try {
            if (env.equalsIgnoreCase("Staging")) {
                restaurantRequestBuilder = new RestaurantRequestBuilder("/resources/config-files/staging/zomato-stage.properties");
            } else if (env.equalsIgnoreCase("Production")) {
            }
            jReader = new JSONFileReader();
            logger = new Logger();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}