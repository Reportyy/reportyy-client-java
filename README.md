# reportyy-client-java

Java 11+ client for Reportyy API.

## Installation

### Prerequisites
* Java 11+

### Gradle
Add this dependency to your project's build file:
```groovy
implementation "com.reportyy:reportyy-client-java:1.0.0"
```

### Maven
Add this dependency to your project's POM:
```xml
<dependency>
  <groupId>com.reportyy</groupId>
  <artifactId>reportyy-client-java</artifactId>
  <version>1.0.0</version>
</dependency>
```

### Others
You'll need to manually install the following JARs:
* [Reportyy Client JAR](https://reportyy.com)
* [Jackson JAR](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.14.2/jackson-databind-2.14.2.jar)

## Documentation
Please see the [Java API docs](https://docs.reportyy.com/quickstart) for the most up-to-date documentation.

You can also refer to the online Javadoc.

## Usage

```java
import com.reportyy.client.ReportyyApiClient;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ReportyyExample {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ReportyyApiClient client = new ReportyyApiClient("rpty_ktS0rU...");

        Map<string, string> data = new HashMap<>();
        data.put("date", "February 23, 2023");
        data.put("total", "£14.00");

        CompletableFuture<InputStream> result = client.generatePdf("cleakim7c00129882ha9ct56d", data);
        InputStream generatedPdf = result.get();
    }
}
```