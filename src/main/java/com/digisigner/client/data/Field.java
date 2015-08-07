package com.digisigner.client.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Field {

    private final int page;

    private final int[] rectangle;

    private final FieldType type;

    private String label;

    private boolean required = true; // field is required by default

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
}
