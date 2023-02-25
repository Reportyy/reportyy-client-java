package com.reportyy.client;

/**
 * Reportyy API error
 */
public class ReportyyApiErrorPayload {
    /** HTTP status code */
    public int status;

    /** Reportyy API code */
    public int code;

    /** Error message */
    public String message;

    public ReportyyApiErrorPayload() {
    }

    public ReportyyApiErrorPayload(int status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
