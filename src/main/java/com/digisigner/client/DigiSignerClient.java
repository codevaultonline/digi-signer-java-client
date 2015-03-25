package com.digisigner.client;

import com.digisigner.client.requests.SignatureRequest;

public class DigiSignerClient {

    public DigiSignerClient(String apiKey) {
    }

    public SignatureRequest sendSignatureRequest(SignatureRequest signatureRequest) {
        return new SignatureRequest();
    }

}
