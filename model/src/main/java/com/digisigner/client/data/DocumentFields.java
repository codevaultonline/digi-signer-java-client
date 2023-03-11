package com.digisigner.client.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is used to return all document fields in the API request 'documents/{documentId}/fields'.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentFields {

    @XmlElement(name = "document_fields")
    private List<DocumentField> documentFields;

    public List<DocumentField> getDocumentFields() {
        return documentFields;
    }
}
