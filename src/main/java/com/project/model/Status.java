package com.project.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum Status {
    AVAILABLE("available"), PENDING("pending"), SOLD("sold");

    private final String status;

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(status);
    }

    /*
    @JsonCreator
    public static Status getStatusFromValue(String value) {
        for(Status status : Status.values()) {
            if(status.toString().equals(value)) {
                return status;
            }
        }
        return null;
    }
    */

    @JsonCreator
    public static Status getStatusFromValue(String value) {
        return Arrays.stream(Status.values()).filter(status -> status.toString().equals(value)).findFirst().orElse(null);
    }
}
