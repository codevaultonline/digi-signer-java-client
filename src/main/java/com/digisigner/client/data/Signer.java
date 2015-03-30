package com.digisigner.client.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Signer {

    private final String email;

    private final List<Field> fields = new ArrayList<>();

    public Signer(String email) {
        this.email = email;
    }

    public void addField(Field field) {
        fields.add(field);
    }

    public String getEmail() {
        return email;
    }
}
