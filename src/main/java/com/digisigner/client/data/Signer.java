package com.digisigner.client.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Signer {

    private String email;

    private final List<Field> fields = new ArrayList<>();

    @XmlElement(name = "is_signature_completed")
    private Boolean signatureCompleted;

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

    public Boolean getSignatureCompleted() {
        return signatureCompleted;
    }
}
