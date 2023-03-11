package com.digisigner.client.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Signature {

    private int page;

    private int[] rectangle;

    private byte[] image;

    @XmlElement(name = "draw_coordinates")
    private String drawCoordinates;

    // required for jaxb init.
    @SuppressWarnings("unused")
    private Signature() {}

    public Signature(int page, int[] rectangle, byte[] image) {
        this.page = page;
        this.rectangle = rectangle;
        this.image = image;
    }

    public int getPage() {
        return page;
    }

    public int[] getRectangle() {
        return rectangle;
    }

    public byte[] getImage() {
        return image;
    }

    public String getDrawCoordinates() {
        return drawCoordinates;
    }

    public void setDrawCoordinates(String drawCoordinates) {
        this.drawCoordinates = drawCoordinates;
    }
}
