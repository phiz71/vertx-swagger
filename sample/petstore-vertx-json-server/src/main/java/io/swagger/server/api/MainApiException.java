package io.swagger.server.api;

import io.vertx.core.MultiMap;

public class MainApiException extends RuntimeException {
    private MultiMap headers;
    private int statusCode;
    private String statusMessage;

    public MainApiException(int statusCode, String statusMessage) {
        super();
        this.headers = MultiMap.caseInsensitiveMultiMap();
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public MainApiException addHeader(MainApiHeader header) {
        if (header.getValue() != null)
            this.headers.add(header.getName(), header.getValue());
        else
            this.headers.add(header.getName(), header.getValues());
        return this;
    }

    public MainApiException addHeader(String name, String value) {
        this.headers.add(name, value);
        return this;
    }

    public MainApiException addHeaders(String name, Iterable<String> values) {
        this.headers.add(name, values);
        return this;
    }

    public MultiMap getHeaders() {
        return headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
    
    public static final MainApiException INTERNAL_SERVER_ERROR = new MainApiException(500, "Internal Server Error"); 
}