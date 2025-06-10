package org.springframework.samples.petclinic.customers.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.config.MeterFilterReply;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.aop.TimedAspect;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MetricConfig {

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> {
            registry.config().commonTags("application", "petclinic");

            // Gắn trace_id vào từng metric
            registry.config().meterFilter(new MeterFilter() {
                @Override
                public io.micrometer.core.instrument.Meter.Id map(io.micrometer.core.instrument.Meter.Id id) {
                    Span span = Span.current();
                    SpanContext ctx = span.getSpanContext();
                    if (ctx.isValid()) {
                        List<Tag> newTags = new ArrayList<>(id.getTags());
                        newTags.add(Tag.of("trace_id", ctx.getTraceId()));
                        return id.withTags(newTags);
                    }
                    return id;
                }
            });
        };
    }

    @Bean
    TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
