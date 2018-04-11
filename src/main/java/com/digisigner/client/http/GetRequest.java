package com.digisigner.client.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.digisigner.client.DigiSignerException;

/**
 * The wrapper class for the get request.
 */
public class GetRequest extends BaseRequest {

    private static final Logger log = Logger.getLogger(GetRequest.class);
    private static final String POINT = ".";

    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String DEFAULT_FILE_NAME = "DEFAULT_FILE_NAME";

    public GetRequest(String apiKey) {
        super(apiKey);
    }

    // ############################ GET DATA AS JSON ################################

    public <T> T getAsJson(Class<T> responseClass, String url) {

        WebTarget webResource = getWebResource(url);
        MediaType mediaType = MediaType.APPLICATION_JSON_TYPE.withCharset(ENCODING);
        Response response = webResource.request(mediaType).get();

        return handleResponse(responseClass, response);
    }

    // ############################ GET FILE RESPONSE ################################

    public File getFileResponse(String url, String fileName) {
        InputStream inputStream = null;
        int code;
        String responseStr;

        HttpURLConnection connection = get(url);
        try {
            code = connection.getResponseCode();

            if (code == HttpURLConnection.HTTP_OK) {
                String name = fileName == null ? getFileName(connection) : fileName;
                File file = createTemporaryFile(name);
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

    private String getFileName(HttpURLConnection connection) {
        String raw = connection.getHeaderField(CONTENT_DISPOSITION);
        // raw = "attachment; filename=Document.pdf"
        if (raw != null && raw.indexOf("=") != -1) {
            return raw.split("=")[1]; //getting value after '='
        }
        return DEFAULT_FILE_NAME;
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
     * @return {@code HttpURLConnection} instance.
     */
    private HttpURLConnection get(String url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("Accept-Charset", ENCODING);

            addAuthentication(connection);
        } catch (Exception e) {
            log.error("Exception occurred create url.", e);
        }
        return connection;
    }
}
