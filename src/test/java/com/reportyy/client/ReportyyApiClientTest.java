package com.reportyy.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.*;

public class ReportyyApiClientTest {
    URI baseUri = URI.create(System.getenv("REPORTYY_BASE_URL"));
    String apiKey = System.getenv("REPORTYY_API_KEY");

    private class Address {
        public String line1;
        public String line2;
        public String town;
        public String postCode;
    }

    public class Merchant {
        public String id;
        public String name;
        public Address address;
    }

    public class SaleItem {
        public String date;
        public String value;
    }

    public class LineItem {
        public String title;
        public String value;
    }

    private class SalesReportTemplateData {
        public String date;
        public String reportType;
        public String reportHeader;
        public Merchant merchant;
        public List<SaleItem> saleItems;
        public List<LineItem> lineItems;
    }

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

    @Test
    public void shouldReturnAnInputStream() throws IOException, ExecutionException, InterruptedException {
        ReportyyApiClient client = new ReportyyApiClient(apiKey, baseUri);

        Address address = new Address();
        address.line1 = "Line 1";
        address.town = "London";
        address.postCode = "E2 000";

        Merchant merchant = new Merchant();
        merchant.id = "abddefg";
        merchant.name = "Reportyy";
        merchant.address = address;

        SaleItem saleItem = new SaleItem();
        saleItem.date = "February 7, 2023";
        saleItem.value = "£14.00";

        List<SaleItem> saleItems = new ArrayList<>();
        saleItems.add(saleItem);

        LineItem grossSales = new LineItem();
        grossSales.title = "Gross sales";
        grossSales.value = "£14.00";

        LineItem netSales = new LineItem();
        netSales.title = "Net sales";
        netSales.value = "£14.00";

        LineItem costOfGoods = new LineItem();
        costOfGoods.title = "Cost of goods";
        costOfGoods.value = "£-2.00";

        LineItem fees = new LineItem();
        fees.title = "Fees";
        fees.value = "£0.00";

        LineItem grossProfit = new LineItem();
        grossProfit.title = "Gross profit";
        grossProfit.value = "£12.00";

        List<LineItem> lineItems = new ArrayList<>();
        lineItems.add(grossSales);
        lineItems.add(netSales);
        lineItems.add(costOfGoods);
        lineItems.add(fees);
        lineItems.add(grossProfit);

        SalesReportTemplateData data = new SalesReportTemplateData();
        data.date = "February 18th 2023";
        data.reportType = "Daily Report";
        data.reportHeader = "Day";
        data.merchant = merchant;
        data.saleItems = saleItems;
        data.lineItems = lineItems;

        CompletableFuture<InputStream> future = client.generatePdf("cleakim7c00129882ha9ct56d", data);
        InputStream result = future.get();

        assertThat(result).isNotNull();
    }
}
