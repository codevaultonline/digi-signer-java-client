package com.digisigner.client.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Contains data of build-in PDF field information.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PdfField {

    private String name; // field name as specified in PDF document

    private String content;

    private String label;

    /**
     * If nothing is specified the default value for check boxes is false, but for all other fields it is true.
     */
    private Boolean required;

    @XmlElement(name = "read_only")
    private boolean readOnly = false;

    // required for jaxb init.
    @SuppressWarnings("unused")
	private PdfField() {}    
    
    public PdfField(String name) {
    	this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

}
