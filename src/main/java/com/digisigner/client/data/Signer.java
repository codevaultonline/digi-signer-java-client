package com.digisigner.client.data;

import java.util.ArrayList;
import java.util.List;

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
