package org.springframework.samples.petclinic.customers.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.micrometer.core.instrument.Tag;

@Configuration
public class MetricConfig {

  @Bean
  MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
    return registry -> registry.config().commonTags("application", "petclinic");
  }

  @Bean
  TimedAspect timedAspect(MeterRegistry registry) {
    return new TimedAspect(registry);
  }

  @Bean
  public MeterFilter traceIdMeterFilter() {
    return MeterFilter.commonTags(() -> {
      Span span = Span.current();
      SpanContext context = span.getSpanContext();
      if (context.isValid()) {
        return Tag.of("traceId", context.getTraceId());
      }
      return Tag.empty();
    });
  }
}
