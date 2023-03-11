package com.digisigner.client.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.digisigner.client.DigiSignerException;
import com.digisigner.client.data.Document;

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

        WebTarget webResourcePost = getWebResource(url);
        MediaType mediaType = MediaType.APPLICATION_JSON_TYPE.withCharset(ENCODING);
        Entity<Object> entity = Entity.entity(object, mediaType);
		Response response = webResourcePost.request(JSON_TYPE).post(entity);

        return handleResponse(responseClass, response);
    }
    // ############################ UPLOAD DOCUMENT METHODS ################################

    public ClientResponse sendDocumentToServer(String url, Document document) {

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
    	InputStream inputStream = null;
    	try {
	        inputStream = document.getInputStream();
	        byte[] buffer = new byte[4096];
	        int bytesRead;
	
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            out.write(buffer, 0, bytesRead);
	        }
    	}
    	finally {
    		if (inputStream != null) inputStream.close();
    	}
    }

    private ClientResponse callService(HttpURLConnection connection) throws IOException {
        int code = connection.getResponseCode();
        String responseStr;
        InputStream response;
        if (code == HttpURLConnection.HTTP_OK) {
            response = connection.getInputStream();
            responseStr = convertStreamToString(response);
            return new ClientResponse(code, responseStr);
        }

        response = connection.getErrorStream();
        responseStr = convertStreamToString(response);
        throwDigisignerException(responseStr, code);
        return null;
    }
}
