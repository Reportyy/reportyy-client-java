package com.reportyy.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.*;

public class ReportyyApiClientTest {
    URI baseUri = URI.create(System.getenv("REPORTYY_BASE_URL"));

    @Test
    public void shouldThrowAnErrorWithInvalidApiKey() throws JsonProcessingException {
        ReportyyApiClient client = new ReportyyApiClient("invalid_key", baseUri);

        Throwable throwable = catchThrowable(() -> {
            CompletableFuture<InputStream> result = client.generatePdf("cleakim7c00129882ha9ct56d", new HashMap<String, Object>());
            result.join();
        });

        assertThat(throwable).getRootCause().isInstanceOf(ReportyyApiException.class);

        ReportyyApiException error = (ReportyyApiException) throwable.getCause();
        assertThat(error.getErrorPayload().status).isEqualTo(401);
        assertThat(error.getErrorPayload().code).isEqualTo(401);
        assertThat(error.getErrorPayload().message).isEqualTo("Unauthorised");
    }
}
