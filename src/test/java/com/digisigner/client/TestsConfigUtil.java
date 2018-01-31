package com.digisigner.client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads properties from {user.dir}/digisigner.test.properties file.
 * File structure:
 * server.url=http://localhost:8080/online/api
 * api.key=YOUR_API_KEY
 * template.id=YOUR_TEMPLATE_ID
 */
public final class TestsConfigUtil {

    private static final Properties prop = new Properties();
    // file location
    private static final String USER_DIR = "user.dir";
    private static final String DIGISIGNER_TEST_PROPERTIES = "/digisigner.test.properties";

    // property keys
    private static final String SERVER_URL = "server.url";
    private static final String API_KEY = "api.key";
    private static final String TEMPLATE_ID = "template.id";

    static {
        String dir = System.getProperty(USER_DIR);
        InputStream in;
        try {
            in = new FileInputStream(dir + DIGISIGNER_TEST_PROPERTIES);
            prop.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private TestsConfigUtil() {
    }

    public static String getServerUrl() {
        return prop.getProperty(SERVER_URL);
    }

    public static String getApiKey() {
        return prop.getProperty(API_KEY);
    }

    public static String getTemplateId() {
        return prop.getProperty(TEMPLATE_ID);
    }
}
