package org.zomato.commonUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

public class PropertyReader {

  public Properties prop;

  public PropertyReader(String filePath) {
    prop = new Properties();
    InputStream input = null;

    try {
      String filename = System.getProperty("user.dir") + filePath;
      prop.load(new FileInputStream(filename.trim()));

    } catch (IOException ex) {
      ex.printStackTrace();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public String get(String key){
    try {
      String env = prop.getProperty("env");

      if (key.trim().equalsIgnoreCase("ENV"))
        return env;
      else
        return prop.getProperty(env + "." + key);
    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }

}
