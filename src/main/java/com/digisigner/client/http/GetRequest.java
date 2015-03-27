package com.digisigner.client.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.digisigner.client.DigiSignerException;

/**
 * The wrapper class for the get request.
 */
public class GetRequest extends BaseRequest {

    private static final Logger log = Logger.getLogger(GetRequest.class);
    private static final String POINT = ".";
    private static final String EMPTY = "";

    private Map<String, String> parameters;
    private String url;

    public GetRequest(String apiKey, String url) {
        this(apiKey, url, null);
    }

    public GetRequest(String apiKey, String url, Map<String, String> parameters) {
        super(apiKey);
        this.url = url;
        this.parameters = parameters;
    }

    public Response getResponse() {
        try {
            HttpURLConnection connection = get(url, parameters);
            int code = connection.getResponseCode();
            InputStream inputStream;
            if (HttpURLConnection.HTTP_OK == code) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
            }
            return new Response(code, convertStreamToString(inputStream));
        } catch (Exception ex) {
            throw new DigiSignerException(ex.getMessage());
        }
    }

    private File createTemporaryFile(String filename) {
        String prefix = filename.substring(0, filename.indexOf(POINT));
        String postfix = filename.substring(filename.indexOf(POINT) + 1, filename.length());
        try {
            return File.createTempFile(prefix, POINT + postfix);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new DigiSignerException(ex.getMessage());
        }
    }

    /**
     * Builds the GET http request.
     *
     * @param parameters included for url.
     * @return {@code HttpURLConnection} instance.
     */
    private HttpURLConnection get(String url, Map<String, String> parameters) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url + getParametersForUrl(parameters)).openConnection();
            connection.setRequestProperty("Accept-Charset", ENCODING);

            addAuthentication(connection);
        } catch (Exception e) {
            log.error("Exception occurred create url.", e);
        }
        return connection;
    }

    private String getParametersForUrl(Map<String, String> parameters) {
        StringBuilder url = new StringBuilder();
        try {
            if (parameters != null) {
                url.append("?");
                Iterator<String> keys = parameters.keySet().iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    url.append(URLEncoder.encode(key, ENCODING)).append("=")
                            .append(URLEncoder.encode(parameters.get(key), ENCODING));
                    if (keys.hasNext()) {
                        url.append("&");
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            log.error(e);
            return EMPTY;
        }
        return url.toString();
    }

    public File getFileResponse(String documentId, String filename) {
        InputStream inputStream = null;
        int code;
        String responseStr;

        HttpURLConnection connection = get(url + "/" + documentId, null);
        try {
            code = connection.getResponseCode();

            if (code == HttpURLConnection.HTTP_OK) {
                File file = createTemporaryFile(filename);
                Files.copy(connection.getInputStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                return file;
            }
            inputStream = connection.getErrorStream();
            responseStr = convertStreamToString(inputStream);

        } catch (Exception e) {
            throw new DigiSignerException(e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    log.error("Failed to close input stream.", e);
                }
            }
        }
        // if errors occur
        throwDigisignerException(responseStr, code);
        return null;
    }
}
