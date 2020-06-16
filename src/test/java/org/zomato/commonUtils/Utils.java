package org.zomato.commonUtils;

/**
 * // TODO Comment
 */

import com.fasterxml.jackson.databind.JsonNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils extends BaseClass {

    static JsonNode rs;
    private static Utils instance = new Utils();
    private String sheetName;
    private String columnNames = "*";
    private boolean debug = false;
    private boolean loadEmptyColumns = true;
    com.jayway.jsonpath.JsonPath responseJsonPath = null;

    private Utils() {
    }

    public static Utils getInstance() {
        return instance;
    }

    public static String resolveEnvVars(String input) {
        if (null == input) {
            return null;
        }
        Pattern pattern = Pattern.compile("\\$(?:\\{(\\w+)\\}|(\\w+))");
        Matcher matcher = pattern.matcher(input);
        StringBuffer stringBuilder = new StringBuffer();
        while (matcher.find()) {
            String envVarName = null == matcher.group(1) ? matcher.group(2)
                    : matcher.group(1);
            String envVarValue = getPropertyEnv(envVarName, null);
            matcher.appendReplacement(stringBuilder,
                    null == envVarValue ? "" : envVarValue.replace("\\", "\\\\"));
        }
        matcher.appendTail(stringBuilder);
        return stringBuilder.toString();
    }

    public static String getPropertyEnv(String name, String defaultValue) {
        String value = System.getProperty(name);
        if (value == null) {
            value = System.getenv(name);
            if (value == null) {
                value = defaultValue;
            }
        }
        return value;
    }

}