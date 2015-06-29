package com.digisigner.client.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * The notification message of Json format.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

    private String message;

    private List<Message> errors;

    public List<Message> getErrors() {
        return errors;
    }

    public void setErrors(List<Message> errors) {
        this.errors = errors;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
