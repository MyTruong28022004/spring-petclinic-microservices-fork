package org.springframework.samples.petclinic.customers.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.config.MeterFilter;
import org.slf4j.MDC;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class MetricConfig {

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", "petclinic");
    }

    @Bean
    public MeterFilter traceIdMeterFilter() {
        return MeterFilter.commonTags(() -> {
            String traceId = MDC.get("traceId");
            return Collections.singletonList(Tag.of("traceId", traceId != null ? traceId : "unknown"));
        });
    }

    @Bean
    TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
