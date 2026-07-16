package com.reduceco2now.ingestion.internal.off;

/**
 * Thrown when the Open Food Facts API responds with an unexpected (non-2xx)
 * HTTP status. This is a batch-level failure — unlike malformed individual
 * products, which are skipped rather than thrown.
 */
public class OffApiException extends RuntimeException {

    private final int statusCode;

    public OffApiException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int statusCode() {
        return statusCode;
    }
}
