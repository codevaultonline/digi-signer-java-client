package com.digisigner.client.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Signer {

    private String email;

    private String role;

    private final List<Field> fields = new ArrayList<>();

    @XmlElement(name = "is_signature_completed")
    private Boolean signatureCompleted;

    @XmlElement(name = "sign_document_url")
    private String signDocumentUrl;

    @XmlElement(name = "access_code")
    private String accessCode;

    @SuppressWarnings("unused")
    private Signer() { /* JAXB requires it */ }

    public Signer(String email) {
        this.email = email;
    }

    public void addField(Field field) {
        fields.add(field);
    }

    public String getEmail() {
        return email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public Boolean getSignatureCompleted() {
        return signatureCompleted;
    }

    public String getSignDocumentUrl() {
        return signDocumentUrl;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
}
