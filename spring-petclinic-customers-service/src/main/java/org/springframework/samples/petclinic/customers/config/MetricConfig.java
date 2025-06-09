import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricConfig {

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config()
            .commonTags("application", "petclinic")
            .meterFilter(addTraceIdTag());
    }

    private MeterFilter addTraceIdTag() {
        return MeterFilter.commonTags(
            "trace_id", () -> {
                SpanContext context = Span.current().getSpanContext();
                return context.isValid() ? context.getTraceId() : "unknown";
            });
    }

    @Bean
    TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
