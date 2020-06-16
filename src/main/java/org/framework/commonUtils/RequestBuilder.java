package org.framework.commonUtils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.framework.Reporting.Logger;
import org.testng.Reporter;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * // TODO Comment
 */
public class RequestBuilder {

    public RequestSpecification request;
    public PropertyReader pReader;
    String baseUrl;
    String filePath;
    Logger logger = new Logger();
    public static String authToken;
    public static ValidatableResponse response;

    public RequestBuilder() {
    }

    public RequestBuilder(String filePath) {
        pReader = new PropertyReader(filePath);
        String server = pReader.get("url");
        baseUrl = "https://" + server;
        String userKey = (String) pReader.get("key");
        //String baseUrl= "http://"+server;
        request = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("user-key", userKey)
                .setBaseUri(baseUrl)
                .build();
    }

    public ValidatableResponse sendGetRequest(String pathURI) {
        ValidatableResponse response = null;
        logger.log("BaseURl -----> " + baseUrl);
        logger.log("Final URl -----> " + baseUrl + pathURI);
        try {
            response = given()
                    .spec(request)
                    .get(pathURI)
                    .then().log().all();
            Reporter.log("Response ------>" + response.extract().body().asString(), true);
            assertThat(response.statusCode(200));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public ValidatableResponse sendGetRequest(String pathURI, HashMap<String, String> queryParams) {
        ValidatableResponse response = null;
        logger.log("BaseURl -----> " + baseUrl);
        logger.log("Final URl -----> " + baseUrl + pathURI);
        try {
            if (queryParams.size() > 0) {
                request.queryParams(queryParams);
            }
            response = given()
                    .spec(request)
                    .get(pathURI)
                    .then().log().all();
            Reporter.log("Response ------>" + response.extract().body().asString(), true);
            assertThat(response.statusCode(200));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}


