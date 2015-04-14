package com.digisigner.client.http;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.json.JSONConfiguration;
import org.json.JSONException;
import org.json.JSONObject;

import com.digisigner.client.DigiSignerException;
import com.digisigner.client.data.Message;

/**
 * The base class for the http requests.
 */
public class BaseRequest {
    public static final String ENCODING = "UTF-8";
    public static final String JSON_TYPE = "application/json";
    private final String apiKey;

    public BaseRequest(String apiKey) {
        this.apiKey = apiKey;
    }

    protected void addAuthentication(HttpURLConnection httpConn) {
        String key = getApiKey() + ":";
        String authorization = "Basic " + DatatypeConverter.printBase64Binary(key.getBytes()).trim();
        httpConn.setRequestProperty("authorization", authorization);
    }

    protected WebResource getWebResource(String url) {
        // Create Jersey client
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        client.addFilter(new HTTPBasicAuthFilter(getApiKey(), ""));

        return client.resource(url);
    }

    protected static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    protected void throwDigisignerException(String message, int code) {
        try {
            JSONObject error = new JSONObject(message);
            String errorMessage = error.getString("message");
            throw new DigiSignerException(errorMessage, null, code);
        } catch (JSONException ex) {
            throw new DigiSignerException(ex.getMessage());
        }
    }

    protected <T> T handleResponse(Class<T> responseClass, ClientResponse response) {

        int code = response.getStatus();
        if (code == HttpURLConnection.HTTP_OK) {
            return response.getEntity(responseClass);
        }

        Message message = response.getEntity(Message.class);
        throwDigisignerException(message, code);
        return null;
    }

    private void throwDigisignerException(Message message, int code) {
        try {
            List<String> detailsErrors = new ArrayList<>();
            for (Message detailsMessage : message.getErrors()) {
                detailsErrors.add(detailsMessage.getMessage());
            }
            throw new DigiSignerException(message.getMessage(), detailsErrors, code);
        } catch (JSONException ex) {
            throw new DigiSignerException(ex.getMessage());
        }
    }
    
    protected String getApiKey() {
        return apiKey;
    }
}
