package org.springframework.samples.petclinic.customers.config;

import brave.Tracing;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import io.micrometer.tracing.reporter.zipkin.ZipkinSpanHandler;
import io.zipkin.reporter2.Reporter;
import io.zipkin.reporter2.SpanBytesEncoder;
import io.zipkin.reporter2.okhttp3.OkHttpSender;
import io.zipkin.reporter2.Sender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfig {

    @Bean
    public Sender zipkinSender() {
        return OkHttpSender.create("http://localhost:9411/api/v2/spans");
    }

    @Bean
    public Reporter<zipkin2.Span> spanReporter(Sender sender) {
        return Reporter.builder(sender)
                       .build(SpanBytesEncoder.JSON_V2);
    }

    @Bean
    public ZipkinSpanHandler zipkinSpanHandler(Reporter<zipkin2.Span> reporter) {
        return ZipkinSpanHandler.create(reporter);
    }

    @Bean
    public Tracing braveTracing(ZipkinSpanHandler zipkinSpanHandler) {
        return Tracing.newBuilder()
                      .addSpanHandler(zipkinSpanHandler)
                      .build();
    }

    @Bean
    public Tracer micrometerTracer(Tracing braveTracing) {
        return new BraveTracer(braveTracing.tracer(), braveTracing.currentTraceContext());
    }
}
