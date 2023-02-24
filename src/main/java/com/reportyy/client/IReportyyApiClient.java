package com.reportyy.client;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface IReportyyApiClient {
    CompletableFuture<InputStream> generatePdf(String templateId, Map<String, Object> data) throws JsonProcessingException;
}
