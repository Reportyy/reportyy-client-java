package com.reportyy.client;

public class ReportyyApiException extends Exception {
    protected ReportyyApiErrorPayload _errorPayload;

    public ReportyyApiException(String errorMessage) {
        super(errorMessage);
    }

    public ReportyyApiException(String errorMessage, ReportyyApiErrorPayload errorPayload)
    {
        super(errorMessage);

        this._errorPayload = errorPayload;
    }

    public ReportyyApiErrorPayload getErrorPayload() {
        return this._errorPayload;
    }
}
