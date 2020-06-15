package org.framework.commonUtils;

/**
 * // TODO Comment
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.gson.JsonArray;
import com.jayway.jsonpath.ReadContext;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.testng.Reporter;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;


public class Utils {

    static String regex = "[0-9]";
    static Date datetoday = new Date();
    static SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
    static SchemaLoader schemaLoader;
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