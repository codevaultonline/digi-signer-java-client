package com.digisigner.client.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Content that can be added to a document.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentContent {

    private List<Signature> signatures;

    private DocumentContent() { /* JAXB requires it */ }

    public DocumentContent(List<Signature> signatures) {
        this.signatures = signatures;
    }

    public List<Signature> getSignatures() {
        return signatures;
    }
}
