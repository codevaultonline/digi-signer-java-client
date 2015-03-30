package com.digisigner.client.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The class for the event callback notification.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Event {
    @XmlElement(name = "event_time")
    private long eventTime;
    @XmlElement(name = "event_type")
    private String eventType;
    @XmlElement(name = "signature_request")
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
