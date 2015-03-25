package com.digisigner.client.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.digisigner.client.data.Document;
import org.apache.log4j.Logger;

public class SendDocument {

    private static final Logger log = Logger.getLogger(SendDocument.class);

    private static final String FILE_PART_NAME = "file";
    private static final String ENCODING = "UTF-8";
    private static final String CRLF = "\r\n";
    private static final String HYPHEN = "--";

    // ############################ UPLOAD DOCUMENT METHODS ################################

    private int sendDocumentToServer(Document document) {
        return sendDocumentToServer(document, getParamsToSendToServer());
    }

    private int sendDocumentToServer(Document document, Map<String, String> parameters) {

        String serverUrl = "getParameter(DigiOptions.OUTPUT_URL)";
        if (serverUrl == null) {
            return -1;
        }

        PrintWriter writer = null;
        try {
            // for boundary we just generate some unique random value
            String boundary = Long.toHexString(System.currentTimeMillis());

            // open connection to server
            HttpURLConnection connection = (HttpURLConnection) new URL(serverUrl).openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            OutputStream out = connection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(out, ENCODING), true); // true = autoFlush, important!

            // send document content
            outputDocument(document, writer, out, boundary);

            // send parameters
            if (parameters != null) {
                outputParameters(parameters, writer, boundary);
            }

            // end of multipart/form-data
            writer.append(HYPHEN).append(boundary).append(HYPHEN).append(CRLF).flush();

            return connection.getResponseCode();
        } catch (Exception e) {
            System.err.println("Failed to send signed document to " + serverUrl);
            e.printStackTrace();

            // open dialog and show error message
            String message = "Failed to send signed document to the server!";

            return -1;
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private void outputDocument(Document document, PrintWriter writer,
                                OutputStream out, String boundary) throws IOException {
        String filename = document.getDocumentFilename();
        writer.append(HYPHEN).append(boundary).append(CRLF);
        writer.append("Content-Disposition: form-data; ");
        writer.append("name=\"" + FILE_PART_NAME + "\"; ");
        writer.append("filename=\"").append(filename).append("\"").append(CRLF);
        writer.append("Content-Type: application/pdf").append(CRLF);
        writer.append("Content-Transfer-Encoding: binary").append(CRLF);
        writer.append(CRLF).flush();

        // write document content to output stream
        document.writeToOutputStream(out);

        // flush and end of binary section
        out.flush();
        writer.append(CRLF).flush();
    }

    private void outputParameters(Map<String, String> parameters, PrintWriter writer, String boundary) {
        for (String parameter : parameters.keySet()) {
            writer.append(HYPHEN).append(boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"").append(parameter).append("\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + ENCODING).append(CRLF);
            writer.append(CRLF);
            writer.append(parameters.get(parameter));
            writer.append(CRLF).flush();
        }
    }


    // ############################ SERVICE METHODS ################################

    private Map<String, String> getParamsToSendToServer() {
        Map<String, String> paramsMap = new HashMap<String, String>();
        String paramsStr = "getParameter(DigiOptions.SEND_PARAMS_TO_SERVER)";

        if (paramsStr == null) {
            return paramsMap;
        }

        String[] params = paramsStr.split(",");
        for (String param : params) {
            String value = "getParameter(param.trim())";
            if (value != null) {
                paramsMap.put(param.trim(), value);
            }
        }

        return paramsMap;
    }


}
