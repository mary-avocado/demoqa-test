package org.example.metrics;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.exporter.PushGateway;
import org.junit.jupiter.api.extension.*;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class TestMetricsExtension implements BeforeAllCallback, AfterAllCallback, TestWatcher {

    private static final CollectorRegistry registry = new CollectorRegistry();

    private static final Gauge testsTotal = Gauge.build()
            .name("ui_tests_total")
            .help("Total number of UI tests in this run.")
            .register(registry);

    private static final Gauge testsFailed = Gauge.build()
            .name("ui_tests_failed")
            .help("Failed UI tests in this run.")
            .register(registry);

    private static final Gauge testsSucceeded = Gauge.build()
            .name("ui_tests_succeeded")
            .help("Succeeded UI tests in this run.")
            .register(registry);

    private static final Histogram runDuration = Histogram.build()
            .name("ui_tests_run_seconds")
            .help("Total duration of the test run in seconds.")
            .register(registry);

    private Histogram.Timer timer;
    private int passed = 0;
    private int failed = 0;
    private int total = 0;

    private String pushgatewayAddress() {
        return System.getProperty("pushgateway.url", "localhost:9091"); // host:port
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        timer = runDuration.startTimer();
    }

    @Override
    public void testSuccessful(ExtensionContext context) { passed++; total++; }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) { failed++; total++; }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        double elapsed = timer.observeDuration();
        testsTotal.set(total);
        testsFailed.set(failed);
        testsSucceeded.set(passed);

        Map<String, String> groupingKey = new HashMap<>();
        groupingKey.put("project", "demoqa-tests");
        groupingKey.put("branch", System.getProperty("branch", "local"));
        groupingKey.put("build_id", System.getProperty("buildId",
                String.valueOf(Instant.now().getEpochSecond())));
        groupingKey.put("class", context.getRequiredTestClass().getSimpleName());

        PushGateway pg = new PushGateway(pushgatewayAddress());
        pg.pushAdd(registry, "ui_test_run", groupingKey);
    }

}
