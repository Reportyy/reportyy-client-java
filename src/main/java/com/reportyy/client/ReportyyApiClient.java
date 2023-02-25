package com.reportyy.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * Client for the Reportyy API.
 */
public class ReportyyApiClient implements IReportyyApiClient {
    private final static String DEFAULT_BASE_URL = "https://api.reportyy.com";

    private final URI _baseUri;
    private final String _apiKey;
    private final HttpClient _httpClient;
    private final ObjectMapper _mapper;

    /**
     * Initiate a ReportyyApiClient with an apiKey obtained from
     * the Reportyy Dashboard.
     *
     * @param apiKey API key retrieved from the Reportyy Dashboard.
     */
    public ReportyyApiClient(String apiKey) {
        this(apiKey, URI.create(ReportyyApiClient.DEFAULT_BASE_URL));
    }

    /**
     * Initiate a ReportyyApiClient with an apiKey and a baseUri. This
     * isn't meant to be used publicly.
     *
     * @param apiKey  API key retrieved from the Reportyy Dashboard.
     * @param baseUri A custom API URI.
     */
    public ReportyyApiClient(String apiKey, URI baseUri) {
        this._apiKey = apiKey;
        this._baseUri = baseUri;
        this._mapper = new ObjectMapper();
        this._httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public CompletableFuture<InputStream> generatePdf(String templateId, String dataJson) {
        URI uri = this._baseUri
                .resolve("/api/v1/templates/" + templateId + "/generate-sync");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(dataJson))
                .uri(uri)
                .setHeader("Authorization", "API-Key " + this._apiKey)
                .setHeader("User-Agent", "Reportyy Java API Client")
                .setHeader("Content-Type", "application/json")
                .build();

        CompletableFuture<HttpResponse<InputStream>> response =
                this._httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream());

        return response.thenCompose(r ->
        {
            int statusCode = r.statusCode();
            CompletableFuture<InputStream> future = new CompletableFuture<>();

            if (statusCode != 200) {
                try {
                    ReportyyApiErrorPayload error = this._mapper.readValue(r.body(), ReportyyApiErrorPayload.class);
                    future.completeExceptionally(new ReportyyApiException("API Exception", error));
                } catch (Exception ex) {
                    future.completeExceptionally(new ReportyyApiException("Unknown error", new ReportyyApiErrorPayload(statusCode, statusCode, "Unknown error")));
                }
            } else {
                future.complete(r.body());
            }

            return future;
        });
    }

    /**
     * Render the given template specified by its ID with specified data, generate
     * a PDF. The result is returned as an CompletableFuture.
     *
     * @param templateId Template ID to render
     * @param data       A JSON serialisable object to use as the template data.
     * @return CompletableFuture
     * @throws JsonProcessingException
     */
    public CompletableFuture<InputStream> generatePdf(String templateId, Object data) throws JsonProcessingException {
        String json = this._mapper.writer()
                .writeValueAsString(data);

        return this.generatePdf(templateId, json);
    }
}
