package com.digisigner.client;

import java.io.File;
import java.util.List;

import org.json.JSONObject;

import com.digisigner.client.data.Document;
import com.digisigner.client.data.DocumentContent;
import com.digisigner.client.data.DocumentFields;
import com.digisigner.client.data.Signature;
import com.digisigner.client.data.SignatureRequest;
import com.digisigner.client.http.ClientResponse;
import com.digisigner.client.http.Config;
import com.digisigner.client.http.DeleteRequest;
import com.digisigner.client.http.GetRequest;
import com.digisigner.client.http.PostRequest;

/**
 * Main class for DigiSigner client.
 */
public class DigiSignerClient {
    private final String apiKey;
    private final String serverUrl;

    public DigiSignerClient(String apiKey) {
        this.apiKey = apiKey;
        serverUrl = Config.DEFAULT_SERVER_URL;
    }

    public DigiSignerClient(String serverUrl, String apiKey) {
        this.apiKey = apiKey;
        this.serverUrl = serverUrl;
    }

    /**
     * Sends the signature request to the server.
     *
     * @param signatureRequest filled signature request with required data.
     * @return result with sent signature request ID.
     */
    public SignatureRequest sendSignatureRequest(SignatureRequest signatureRequest) {
        for (Document document : signatureRequest.getDocuments()) {
            if (document.getId() == null) {
                document.setId(callUploadDocument(document));
            }
        }
        return new PostRequest(apiKey).postAsJson(SignatureRequest.class, signatureRequest,
                Config.getSignatureRequestsUrl(serverUrl));
    }

    /**
     * The get signature request to check information about signature such as signature is completed
     * and IDs of signature request and documents.
     *
     * @param signatureRequestId ID of the signature request.
     * @return {@link com.digisigner.client.data.SignatureRequest} with filled IDs and signature request data.
     */
    public SignatureRequest getSignatureRequest(String signatureRequestId) {
        String url = Config.getSignatureRequestsUrl(serverUrl) + "/" + signatureRequestId;
        return new GetRequest(apiKey).getAsJson(SignatureRequest.class, url);
    }

    /**
     * Uploads current document to the server.
     *
     * @param document to upload.
     * @return document with ID.
     */
    public Document uploadDocument(Document document) {
        document.setId(callUploadDocument(document));
        return document;
    }

    /**
     * Upload document and returns ID of document.
     *
     * @param document to upload.
     * @return ID of uploaded document.
     */
    private String callUploadDocument(Document document) {
        ClientResponse clientResponse = new PostRequest(apiKey).sendDocumentToServer(Config.getDocumentsUrl(serverUrl),
                document);
        return new JSONObject(clientResponse.getContent()).getString(Config.PARAM_DOC_ID);
    }

    /**
     * Download the document by ID.
     *
     * @param documentId ID of document.
     * @param fileName   the name of the document file to be saved.
     * @return retrieved document.
     */
    public Document getDocumentById(String documentId, String fileName) {
        File file = new GetRequest(apiKey).getFileResponse(Config.getDocumentUrl(serverUrl, documentId), fileName);

        Document document = new Document(file);
        document.setId(documentId);
        return document;
    }

    /**
     * Returns document fields for a document.
     */
    public DocumentFields getDocumentFields(String documentId) {

        String url = Config.getFieldsUrl(serverUrl, documentId);
        return new GetRequest(apiKey).getAsJson(DocumentFields.class, url);
    }

    /**
     * Adds content to the document after given document ID.
     *
     * @param documentId to insert content.
     * @param signatures will be rendered on the document.
     */
    public void addContentToDocument(String documentId, List<Signature> signatures) {

        String url = Config.getContentUrl(serverUrl, documentId);
        new PostRequest(apiKey).postAsJson(Object.class, new DocumentContent(signatures), url);
    }

    /**
     * Deletes document by document ID.
     */
    public void deleteDocument(String documentId) {

        String url = Config.getDeleteDocumentUrl(serverUrl, documentId);
        new DeleteRequest(apiKey).delete(Object.class, url);
    }


    /**
     * Downloads a file for given document and attachment field by IDs.
     *
     * @param documentId ID of document.
     * @param fieldApiId ID of field containing attachment.
     * @return retrieved document.
     */
    public File getDocumentAttachment(String documentId, String fieldApiId, String fileName) {
        String fileAttachmentUrl = Config.getDocumentAttachmentUrl(serverUrl, documentId, fieldApiId);
        return new GetRequest(apiKey).getFileResponse(fileAttachmentUrl, fileName);
    }
}
