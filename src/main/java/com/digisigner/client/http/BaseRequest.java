package com.digisigner.client.http;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
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

    protected WebTarget getWebResource(String url) {
        // Create Jersey client
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);

        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder()
                .credentials(getApiKey(), "").build();
        target.register(feature);
        return target;
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

    protected <T> T handleResponse(Class<T> responseClass, Response response) {

        int code = response.getStatus();
        if (code == HttpURLConnection.HTTP_OK) {
            return response.readEntity(responseClass);
        }

        Message message = response.readEntity(Message.class);
        throwDigisignerException(message, code);
        return null;
    }

    private void throwDigisignerException(Message message, int code) {
        try {
            List<String> detailsErrors = new ArrayList<>();
            if (message.getErrors() != null) {
                for (Message detailsMessage : message.getErrors()) {
                    detailsErrors.add(detailsMessage.getMessage());
                }
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
