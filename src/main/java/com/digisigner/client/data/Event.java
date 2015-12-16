package com.digisigner.client.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The class for the event callback notification.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    public static final String EVENT_TIME_NAME = "event_time";
    public static final String EVENT_TYPE_NAME = "event_type";
    public static final String SIGNATURE_REQUEST_NAME = "signature_request";

    @XmlElement(name = EVENT_TIME_NAME)
    private long eventTime;
    @XmlElement(name = EVENT_TYPE_NAME)
    private String eventType;
    @XmlElement(name = SIGNATURE_REQUEST_NAME)
    private SignatureRequest signatureRequest;

    public long getEventTime() {
        return eventTime;
    }

    public String getEventType() {
        return eventType;
    }

    public SignatureRequest getSignatureRequest() {
        return signatureRequest;
    }
}
