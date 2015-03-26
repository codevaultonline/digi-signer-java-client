package com.digisigner.client.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

import com.digisigner.client.DigiSignerException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The base class for the http requests.
 */
public class BaseRequest {
    public static final String ENCODING = "UTF-8";
    private final String apiKey;

    public BaseRequest(String apiKey) {
        this.apiKey = apiKey;
    }

    protected void addAuthentication(HttpURLConnection httpConn) {
        String key = getApiKey() + ":";
        String authorization = "Basic " + DatatypeConverter.printBase64Binary(key.getBytes()).trim();
        httpConn.setRequestProperty("authorization", authorization);
    }

    protected Response callService(HttpURLConnection connection) throws IOException {
        int code = connection.getResponseCode();
        String responseStr;
        InputStream response;
        if (code == HttpURLConnection.HTTP_OK) {
            response = connection.getInputStream();
            responseStr = convertStreamToString(response);
            return new Response(code, responseStr);
        }

        response = connection.getErrorStream();
        responseStr = convertStreamToString(response);
        throwDigisignerException(responseStr, code);
        return null;
    }

    protected static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    protected void throwDigisignerException(String message, int code) {
        try {
            JSONObject error = new JSONObject(message);
            String errorMessage = error.getString("message");
            List<String> detailsErrors = new ArrayList<String>();
            if (error.has("errors")) {

            }
            throw new DigiSignerException(errorMessage, detailsErrors, code);
        } catch (JSONException ex) {
            throw new DigiSignerException(ex.getMessage());
        }
    }

    public String getApiKey() {
        return apiKey;
    }
}
