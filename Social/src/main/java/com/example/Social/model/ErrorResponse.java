package com.example.Social.model;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponse {
    private Map<String, String> error;

    public ErrorResponse(String errorMessage) {
        this.error = new HashMap<>();
        this.error.put("Error", errorMessage);
    }

    public Map<String, String> getError() {
        return error;
    }

    public void setError(Map<String, String> error) {
        this.error = error;
    }
}
