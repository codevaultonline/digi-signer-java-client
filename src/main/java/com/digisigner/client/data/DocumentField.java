package com.digisigner.client.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is used to return document field data in the API request 'documents/{documentId}/fields'.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentField {

    private String role;

    private FieldType type;

    private int page;  // starts with 0

    private Integer[] rectangle;

    private DocumentFieldStatus status;

    private String content;

    @XmlElement(name = "submitted_content")
    private String submittedContent;

    private String label;

    private boolean required;

    private String name;

    private Integer index;  // relevant only for check boxes

    @XmlElement(name = "read_only")
    private boolean readOnly = false;

    @XmlElement(name = "pdf_field")
    private boolean pdfField = false;

    @XmlElement(name = "font_size")
    private Float fontSize;

    @XmlElement(name = "max_length")
    private Integer maxLength;

    private DocumentFieldAlignment alignment;

    public String getRole() {
        return role;
    }

    public FieldType getType() {
        return type;
    }

    public int getPage() {
        return page;
    }

    public Integer[] getRectangle() {
        return rectangle;
    }

    public DocumentFieldStatus getStatus() {
        return status;
    }

    public String getContent() {
        return content;
    }

    public String getSubmittedContent() {
        return submittedContent;
    }

    public String getLabel() {
        return label;
    }

    public boolean isRequired() {
        return required;
    }

    public String getName() {
        return name;
    }

    public Integer getIndex() {
        return index;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean isPdfField() {
        return pdfField;
    }

    public Float getFontSize() {
        return fontSize;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public DocumentFieldAlignment getAlignment() {
        return alignment;
    }

}
