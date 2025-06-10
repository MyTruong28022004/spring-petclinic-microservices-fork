package org.springframework.samples.petclinic.customers.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.config.MeterFilter;
import org.slf4j.MDC;

@Configuration
public class MetricConfig {

    // Gắn tag cố định "application=petclinic" cho tất cả metrics
    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags("application", "petclinic");
    }

    // Gắn thêm traceId từ MDC vào mỗi metric nếu có (dành cho Sleuth / OpenTelemetry)
    @Bean
    public MeterFilter traceIdMeterFilter() {
        return MeterFilter.commonTags(() -> {
            String traceId = MDC.get("traceId");
            return Tags.of("traceId", traceId != null ? traceId : "unknown");
        });
    }

    // Cho phép sử dụng @Timed để đo thời gian thực thi method
    @Bean
    TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
