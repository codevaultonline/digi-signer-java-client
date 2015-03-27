package com.digisigner.client.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 *
 */
public class Document {

    private static final Logger log = Logger.getLogger(Document.class);

    private String fileName;

    private String documentId;

    private File file;

    private InputStream inputStream;

    private String subject;
    private String message;

    private List<Signer> signers = new ArrayList<>();

    public Document(File file) {
        this.file = file;
    }

    public Document(File file, String fileName) {
        this.fileName = fileName;
        this.file = file;
    }

    public Document(InputStream inputStream, String fileName) {
        this.fileName = fileName;
        this.inputStream = inputStream;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        if (fileName != null) {
            return fileName;
        }
        if (file != null) {
            return file.getName();
        }
        return null;
    }

    public InputStream getInputStream() {
        if (inputStream != null) {
            return inputStream;
        }
        if (file != null) {
            return getInputStreamFromFile();
        }
        return null;
    }

    private InputStream getInputStreamFromFile() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            log.error("File was not found!", e);
        }
        return null;
    }

    public File getFile() {
        return file;
    }

    public void addSigner(Signer signer) {
        signers.add(signer);
    }

}
