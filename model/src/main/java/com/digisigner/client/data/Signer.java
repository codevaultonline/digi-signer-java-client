package com.digisigner.client.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Signer {

    private String email;

    private String role;
    
    private Integer order;

    private final List<Field> fields = new ArrayList<>();

    @XmlElement(name = "pdf_fields")
    private List<PdfField> pdfFields = new ArrayList<>();

    @XmlElement(name = "use_all_pdf_fields")
    private boolean useAllPdfFields = false;

    @XmlElement(name = "existing_fields")
    private List<ExistingField> existingFields = new ArrayList<>();

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

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public void addField(Field field) {
        fields.add(field);
    }

    public void addPdfField(PdfField pdfField) {
        pdfFields.add(pdfField);
    }

    public void addExistingField(ExistingField existingField) {
        existingFields.add(existingField);
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<ExistingField> getExistingFields() {
        return existingFields;
    }

    public String getEmail() {
        return email;
    }

    public boolean isUseAllPdfFields() {
        return useAllPdfFields;
    }

    public void setUseAllPdfFields(boolean useAllPdfFields) {
        this.useAllPdfFields = useAllPdfFields;
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
