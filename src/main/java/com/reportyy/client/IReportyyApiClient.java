package com.reportyy.client;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

public interface IReportyyApiClient {
    CompletableFuture<InputStream> generatePdf(String templateId, Object data) throws JsonProcessingException;
}
