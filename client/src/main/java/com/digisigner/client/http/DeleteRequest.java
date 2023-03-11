package com.digisigner.client.http;

import javax.ws.rs.core.Response;

/**
 * Class sends delete request to the server.
 */
public class DeleteRequest extends BaseRequest {

    public DeleteRequest(String apiKey) {
        super(apiKey);
    }

    public <T> T delete(Class<T> responseClass, String url) {

        Response response = getWebResource(url).request(JSON_TYPE).delete();

        return handleResponse(responseClass, response);
    }
}
