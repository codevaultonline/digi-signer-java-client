package com.digisigner.client.data;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class Document {

    public Document(File file) {
    }

    public Document(String filename) {
    }

    public Document(InputStream inputStream) {
    }

    public Document(byte[] content) {
    }

    public void addSigner(Signer signer) {
    }

    public String getDocumentFilename() {

        return "";
    }

    public void writeToOutputStream(OutputStream out) {

    }
}
