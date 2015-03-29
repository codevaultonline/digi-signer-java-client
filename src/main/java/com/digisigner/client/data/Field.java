package com.digisigner.client.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Field {

    private final int page;

    private final int[] rectangle;

    private final FieldType type;

    private boolean required = true; // field is required by default

    public Field(int page, int[] rectangle, FieldType type) {
        this.page = page;
        this.rectangle = rectangle;
        this.type = type;
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

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
