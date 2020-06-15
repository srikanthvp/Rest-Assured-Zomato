package org.zomato.commonUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.framework.Reporting.Logger;
import org.testng.Reporter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class RequestBuilder extends BaseClass {

    public RequestSpecification request;
    public PropertyReader pReader;
    String baseUrl;
    String filePath;
    Logger logger = new Logger();

    public RequestBuilder(){}

    public RequestBuilder(String filePath) {
        pReader = new PropertyReader(filePath);
        String server = pReader.get("url");
        baseUrl = "https://" + server;
        String origin = (String) pReader.get("origin");
        //String baseUrl= "http://"+server;
        request = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("Origin", origin)
                .setBaseUri(baseUrl)
                .build();
    }

    public RequestBuilder(String filePath, Boolean https) {
        pReader = new PropertyReader(filePath);
        String server = pReader.get("url");
        baseUrl =  (https==true)?"https://" + server : "http://" + server;
        String origin = (String) pReader.get("origin");
        //String baseUrl= "http://"+server;
        request = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("Origin", origin)
                .setBaseUri(baseUrl)
                .build();
    }

    public RequestBuilder(String filePath, String locale){
        pReader = new PropertyReader(filePath);
        baseUrl = pReader.get("weburl");
        String origin = pReader.get("weborigin");
        String xrouteprefix = locale;
        String xdomain = pReader.get("x-domain");
        String xnonce = pReader.get("x-nonce");
        String Referer = pReader.get("Referer")+"/"+locale+"/";
        String Cookie = pReader.get("Cookie");
        String Connection = pReader.get("Connection");

        request = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("Origin", origin)
                .addHeader("x-route-prefix",xrouteprefix)
                .addHeader("x-domain",xdomain)
                .addHeader("x-nonce",xnonce)
                .addHeader("Referer",Referer)
                .addHeader("Cookie",Cookie)
                .addHeader("Connection",Connection)
                .setBaseUri(baseUrl)
                .build();
    }


    /**
     * @info  Method creates Request body by reading json and replacing dynamic values (starting with $) with one provided by test/testUtils
     * @param relativePathToRequestJsonFile  Path to request json file
     * @param additionalBody  This HashMap is sent by test/testUtil containing entries to be replaced by this method
     * @param featureProperties Hint: PropertyReader featureProperties = new PropertyReader(propertyFileRelativePath);
     * @return
     */
    public String createRequestBody(String relativePathToRequestJsonFile, HashMap<String,String> additionalBody, PropertyReader featureProperties) {
        JsonObject requestBody = null;
        String bodyOfRequest = "";
        try {
            JSONFileReader jsonFileReader = new JSONFileReader();
            requestBody = jsonFileReader.readJSONFiles(relativePathToRequestJsonFile);
            bodyOfRequest = requestBody.toString();

            if (additionalBody != null) {
                for (String key : additionalBody.keySet()) {
                    bodyOfRequest = bodyOfRequest.replaceFirst("[$]" + key, additionalBody.get(key)); //replacing dynamic values with one supplied by test/testUtil
                }
            }

            //handling dynamic tvLifetime,nonce,tvSession,clientInterface of json request body.
            if (bodyOfRequest.contains("$tvLifetime"))
                bodyOfRequest = bodyOfRequest.replaceFirst("[$]tvLifetime", featureProperties.get("tvLifetime"));
            if (bodyOfRequest.contains("$nonce"))
                bodyOfRequest = bodyOfRequest.replaceFirst("[$]nonce", featureProperties.get("nonce"));
            if (bodyOfRequest.contains("$tvSession"))
                bodyOfRequest = bodyOfRequest.replaceFirst("[$]tvSession", featureProperties.get("tvSession"));
            if (bodyOfRequest.contains("$clientInterface"))
                bodyOfRequest = bodyOfRequest.replaceFirst("[$]clientInterface", featureProperties.get("clientInterface"));

            //Logic: checking if still some dynamic values (starting with $) left in requestBody and removing that property as it was not provided by test
            Matcher matcherOfPattern = Pattern.compile("\"[$][A-Za-z0-9]{1,}").matcher(bodyOfRequest);
            ArrayList<String> dynamicValuesInRequestBody= new ArrayList<String>();
            while (matcherOfPattern.find()){
                String match = matcherOfPattern.group();
                dynamicValuesInRequestBody.add(match.substring(2,match.length())); //removing starting $ from matched string
            }
            Gson gson=new Gson();
            JsonObject jsonObject=gson.fromJson(bodyOfRequest,JsonObject.class);
            if(dynamicValuesInRequestBody.size()!=0){
                for (String keyToRemove:dynamicValuesInRequestBody){
                    jsonObject.getAsJsonObject("data").remove(keyToRemove);
                }
            }
            bodyOfRequest = jsonObject.toString();

            /** for debug System.out.println(" <*** \n request body below\n" + bodyOfRequest + " \n***>"); **/
        } catch ( FileNotFoundException e) {
            logger.log("API call needs request body. Exception caught while reading " + relativePathToRequestJsonFile + ". Exception caught :" + e);
            e.printStackTrace();
        }

        return bodyOfRequest;
    }


    public JsonObject createRequestBody(JsonObject jsonFileObject) {

        String env = pReader.prop.getProperty("env");
        String tvLifetime = (String) pReader.prop.get(env + ".tvLifetime");
        String tvSession = (String) pReader.prop.get(env + ".tvSession");
        JsonObject context = new JsonObject();
        context.addProperty("tvLifetime", tvLifetime);
        context.addProperty("tvSession", tvSession);
        jsonFileObject.add("context", context);
        jsonFileObject.addProperty("clientInterface", "mobile-iOS");
        return jsonFileObject;
    }



    public JsonObject addPropertyToRequest(JsonObject jsonReqBody, String propertyName, String propertyValue) {
        JsonObject data = jsonReqBody.get("data").getAsJsonObject();
        data.addProperty(propertyName, propertyValue);
        return jsonReqBody;
    }

    public JsonObject addPropertyToRequest(JsonObject jsonReqBody, String propertyName, boolean propertyValue) {
        JsonObject data = jsonReqBody.get("data").getAsJsonObject();
        data.addProperty(propertyName, propertyValue);
        return jsonReqBody;
    }

    public JsonObject addJsonObjectToRequest(JsonObject jsonReqBody, String propertyName, JsonObject propertyValue) {
        JsonObject data = jsonReqBody.get("data").getAsJsonObject();
        data.add(propertyName, propertyValue);
        return jsonReqBody;
    }

    public JsonObject addJsonArrayToRequest(JsonObject jsonReqBody, String propertyName, JsonArray propertyValue) {
        JsonObject data = jsonReqBody.get("data").getAsJsonObject();
        data.add(propertyName, propertyValue);
        return jsonReqBody;

    }


    public JsonObject addJSONArrayToRequest(JsonObject jsonReqBody, String propertyName, JsonArray propertyValue)  {
        JsonObject data = jsonReqBody.get("data").getAsJsonObject();


        data.add(propertyName, propertyValue);
        return jsonReqBody;
    }


    public JsonObject removePropertyFromRequest(JsonObject jsonReqBody, String propertyName) {
        JsonObject data = jsonReqBody.get("data").getAsJsonObject();
        data.remove(propertyName);
        return jsonReqBody;
    }


    public ValidatableResponse sendRequest(JsonObject jsonReqBody, String pathUri) {
        ValidatableResponse responseData=null;
        logger.log("Base URL: "+ baseUrl);
        logger.log("Final URL: "+ baseUrl+""+pathUri);
        logger.logRequest(jsonReqBody);
        try {
            response =
                    given()
                            .spec(request)
                            .body(jsonReqBody.toString())
                            .when()
                            .post(pathUri)
                            .then().log().all();
            Reporter.log("Response ------>"+response.extract().body().asString(),true);
            logger.log("Response time  :"+ response.extract().response().timeIn(TimeUnit.MILLISECONDS) + " ms");

            try {
                if(System.getProperty("debug").equalsIgnoreCase("true"))
                {
                    logger.logResponse(response.extract().body().jsonPath());
                }
            }catch (Exception e)
            {

            }

            logger.log("");
            assertThat(response.statusCode(200));
        }catch(Exception e){
            e.printStackTrace();
            logger.log("RESPONSE : ");
            logger.log(response.extract().body().asString());
        }
        return response;
    }

    public ValidatableResponse potSendRequest(JsonObject jsonReqBody, String baseUrl, String endpoint) {
        ValidatableResponse responseData;
        RequestSpecification request;
        logger.log("Final URL: "+baseUrl+endpoint);
        logger.logRequest(jsonReqBody);
        request = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(baseUrl)
                .build();
        responseData = given()
                .spec(request)
                .body(jsonReqBody.toString())
                .when()
                .post(endpoint)
                .then().log().all();
        Reporter.log(responseData.extract().body().asString(),true);
        assertThat(responseData.statusCode(200));
        return responseData;
    }

    public ValidatableResponse postRequestWithHeaders(JsonObject jsonReqBody, String uri, HashMap<String,String> headers) {
        ValidatableResponse responseData;
        RequestSpecification request;
        logger.log("Final URL: "+uri);
        logger.logRequest(jsonReqBody);
        request = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeaders(headers)
                .build();
        responseData = given()
                .spec(request)
                .body(jsonReqBody.toString())
                .when()
                .post(uri)
                .then().log().all();
        Reporter.log(responseData.extract().body().asString(),true);
        return responseData;
    }

    public ValidatableResponse potSendGetRequest( String baseUrl, String endpoint) {
        ValidatableResponse responseData;
        logger.log("Final URL: "+baseUrl+endpoint);
        responseData = given()
                .when()
                .get(baseUrl+ endpoint)
                .then().log().all();
        Reporter.log(responseData.extract().body().asString(),true);
        assertThat(responseData.statusCode(200));
        return responseData;
    }

    public ValidatableResponse potSendGetRequest( String url) {
        ValidatableResponse responseData;
        logger.log("Final URL: "+url);
        responseData = given()
                .when()
                .get(url)
                .then().log().all();
        Reporter.log(responseData.extract().body().asString(),true);
        assertThat(responseData.statusCode(200));
        return responseData;
    }

    public ValidatableResponse potSendGetRequestWithoutStatusCode( String uri,HashMap<String,String> header) {
        ValidatableResponse responseData;
        logger.log("Final URL: "+uri);
        responseData = given()
                .when()
                .headers(header)
                .get(uri)
                .then().log().all();
        Reporter.log(responseData.extract().body().asString(),true);
        return responseData;
    }

    public ValidatableResponse potSendRequestWithoutStatusCode(JsonObject jsonReqBody, String baseUrl, String endpoint) {
        ValidatableResponse responseData;
        RequestSpecification request;
        logger.log("Final URL: "+baseUrl+endpoint);
        logger.logRequest(jsonReqBody);
        request = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(baseUrl)
                .build();
        responseData = given()
                .spec(request)
                .body(jsonReqBody.toString())
                .when()
                .post(endpoint)
                .then().log().all();
        Reporter.log(responseData.extract().body().asString(),true);
        return responseData;
    }

    public JsonObject addGeoToJson(JsonObject jsonObject, String lat, String lon) {
        JsonObject geo = new JsonObject();
        geo.addProperty("lat", lat);
        geo.addProperty("lon", lon);
        return geo;
    }

    public JsonObject createRequestBodyWithAuth(JsonObject jsonFileObject) {

        JsonObject context = new JsonObject();
        context.addProperty("authServiceToken", authToken);
        jsonFileObject.add("context", context);
        jsonFileObject.addProperty("clientInterface", "desktop");
        return jsonFileObject;
    }

    public ValidatableResponse postRequest(String uri, String  requestBody){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("accept","application/json");
        header.put("content-type","application/json");
        logger.log("Base url = "+baseUrl);
        logger.log("Base path = "+uri);
        logger.log("Final url = "+baseUrl+uri);
        logger.log("Request body :\n"+requestBody);
        return RestAssured.given().headers(header).body(requestBody).when().post(baseUrl+uri).then().log().all();
    }

    public ValidatableResponse upFunPostRequest(String uri, String  requestBody, HashMap<String ,String> header){
        header.put("accept","application/json");
        header.put("content-type","application/json");
        if(System.getProperty("Environment").equalsIgnoreCase("staging"))
            header.put("origin","");
        logger.log("Final url = "+baseUrl+uri);
        return RestAssured.given().headers(header).body(requestBody).when().post(baseUrl+uri).then().log().all();
    }

    public ValidatableResponse getRequest(String pathUri, HashMap<String,String> header) {

        ValidatableResponse responseData=null;
        logger.log("Base URL: "+ baseUrl);
        logger.log("Final URL: "+ baseUrl+""+pathUri);
        header.put("Content-Type","application/json");
        try {
            responseData =
                    given()
                            .when()
                            .headers(header)
                            .get(baseUrl+ pathUri)
                            .then().log().all();
            Reporter.log("Response ------>"+responseData.extract().body().asString(),true);

            try {
                if(System.getProperty("debug").equalsIgnoreCase("true"))
                {
                    logger.logResponse(responseData.extract().body().jsonPath());
                }
            }catch (Exception e)
            {

            }

            assertThat(responseData.statusCode(200));
        }catch(Exception e){
            e.printStackTrace();
            logger.log("RESPONSE : "+ responseData.extract().body().asString());
            logger.log(response.extract().body().asString());
        }
        return responseData;
    }
}