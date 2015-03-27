package com.digisigner.client.data;

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
