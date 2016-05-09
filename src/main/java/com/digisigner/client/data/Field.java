package com.digisigner.client.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Field {

    private int page;

    private int[] rectangle;

    private FieldType type;

    private String name;

    private String content;

    private String label;

    private boolean required = true; // field is required by default

    @XmlElement(name = "read_only")
    private Boolean readOnly;

    // required for jaxb init.
    @SuppressWarnings("unused")
	private Field() {}

    public Field(int page, int[] rectangle, FieldType type) {
        this.page = page;
        this.rectangle = rectangle;
        this.type = type;
    }

    public Field(int page, int[] rectangle, FieldType type, String label, boolean required) {
    	this.page = page;
    	this.rectangle = rectangle;
    	this.type = type;
        this.label = label;
    	this.required = required;
    }
    
    public int getPage() {
        return page;
    }

    public int[] getRectangle() {
        return rectangle;
    }

    public FieldType getType() {
        return type;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
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
    
    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }
}
