package com.digisigner.client.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.json.JSONConfiguration;
import org.apache.log4j.Logger;

import com.digisigner.client.DigiSignerException;
import com.digisigner.client.data.Document;
import com.digisigner.client.data.Message;

/**
 * Class sends post requests to the server.
 */
public class PostRequest extends BaseRequest {

    private static final Logger log = Logger.getLogger(PostRequest.class);

    private static final String FILE_PART_NAME = "file";
    private static final String CRLF = "\r\n";
    private static final String HYPHEN = "--";

    public PostRequest(String apiKey) {
        super(apiKey);
    }

    // ############################ POST DATA AS JSON ################################

    public <T> T postAsJson(Class<T> responseClass, Object object, String url) {

        // Create Jersey client
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        client.addFilter(new HTTPBasicAuthFilter(getApiKey(), ""));


        WebResource webResourcePost = client.resource(url);
        ClientResponse response = webResourcePost.type("application/json").entity(object).post(ClientResponse.class,
                object);
        int code = response.getStatus();
        if (code == HttpURLConnection.HTTP_OK) {
            return response.getEntity(responseClass);
        }

        Message message = response.getEntity(Message.class);
        throwDigisignerException(message, code);
        return null;
    }
    // ############################ UPLOAD DOCUMENT METHODS ################################

    public Response sendDocumentToServer(String url, Document document) {

        PrintWriter writer = null;
        try {
            // for boundary we just generate some unique random value
            String boundary = Long.toHexString(System.currentTimeMillis());

            // open connection to server
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            addAuthentication(connection);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            OutputStream out = connection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(out, ENCODING), true); // true = autoFlush, important!

            // send document content
            outputDocument(document, writer, out, boundary);

            // end of multipart/form-data
            writer.append(HYPHEN).append(boundary).append(HYPHEN).append(CRLF).flush();

            return callService(connection);
        } catch (Exception e) {
            log.error("Failed to send signed document to " + url, e);

            throw new DigiSignerException(e.getMessage(), null, -1);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private void outputDocument(Document document, PrintWriter writer,
                                OutputStream out, String boundary) throws IOException {
        writer.append(HYPHEN).append(boundary).append(CRLF);
        writer.append("Content-Disposition: form-data; ");
        writer.append("name=\"" + FILE_PART_NAME + "\"; ");
        writer.append("filename=\"").append(document.getFileName()).append("\"").append(CRLF);
        writer.append("Content-Type: application/pdf").append(CRLF);
        writer.append("Content-Transfer-Encoding: binary").append(CRLF);
        writer.append(CRLF).flush();

        // write document content to output stream
        writeToOutputStream(document, out);

        // flush and end of binary section
        out.flush();
        writer.append(CRLF).flush();
    }

    private void writeToOutputStream(Document document, OutputStream out) throws IOException {
        InputStream inputStream = document.getInputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
    }

    private Response callService(HttpURLConnection connection) throws IOException {
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
}
