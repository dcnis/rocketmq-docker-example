package de.dcnis.order.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Topic {
    ORDER_ADD_TOPIC("order-add-topic"),
    INITIATE_PAYMENT_TOPIC("initiate-payment-topic");

    private final String value;

    Topic(String value){
        this.value = value;
    }

    @JsonValue
    public String getValue(){
        return this.value;
    }
}
