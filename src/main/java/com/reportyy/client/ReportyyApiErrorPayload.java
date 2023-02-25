package com.reportyy.client;

public class ReportyyApiErrorPayload {
    public int status;
    public int code;
    public String message;

    public ReportyyApiErrorPayload() {
    }

    public ReportyyApiErrorPayload(int status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
