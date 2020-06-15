package org.zomato.zomatoRestaurantInfo.common;


import org.framework.commonUtils.RequestBuilder;

/**
 * // TODO Comment
 */
public class RestaurantRequestBuilder extends RequestBuilder {

  public RestaurantRequestBuilder(String filePath){
    super(filePath);
    System.out.println("---->>>>>> File is: "+ filePath + " <<<<<<----");
  }

}

