package com.digisigner.client.callback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.digisigner.client.data.Event;

/**
 * The test example callback servlet. The main points to show:
 * - The incoming is json representation of com.digisigner.client.data.Event object.
 * - The successful result of reading information must be "DIGISIGNER_EVENT_ACCEPTED" value.
 */
@SuppressWarnings("serial")
public class TestApiCallbackServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(TestApiCallbackServlet.class);

    private static final String CONFIRMATION_TEXT = "DIGISIGNER_EVENT_ACCEPTED";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = null;
        try {
            String json = getJsonValue(request);
            JSONObject jsonEvent = new JSONObject(json);
            log.info("==============================================================");
            log.info("Got Event object type in JSON format:");
            log.info(jsonEvent.get(Event.EVENT_TIME_NAME));
            log.info(jsonEvent.get(Event.EVENT_TYPE_NAME));
            log.info("------------------- Signature request information ------------");
            log.info(jsonEvent.get(Event.SIGNATURE_REQUEST_NAME));
            log.info("==============================================================");

            out = new PrintWriter(response.getOutputStream());
            out.write(CONFIRMATION_TEXT);
        } catch (Exception e) {
            log.error("Failed to process callback", e);
        } finally {
            if (out != null) out.close();
        }
    }

    private String getJsonValue(HttpServletRequest request) {
        StringBuilder value = new StringBuilder();
        try {
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                value.append(line);
            }
        } catch (Exception e) {
            log.error("Error occurs during parse request.");
        }

        return value.toString();
    }

}
