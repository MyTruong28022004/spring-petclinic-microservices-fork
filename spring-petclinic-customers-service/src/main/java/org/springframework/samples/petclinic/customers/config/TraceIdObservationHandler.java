package org.springframework.samples.petclinic.customers.config;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.Observation.Context;
import brave.Tracer;
import org.springframework.stereotype.Component;

@Component
public class TraceIdObservationHandler implements ObservationHandler<Observation.Context> {

    private final Tracer tracer;

    public TraceIdObservationHandler(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void onStart(Context context) {
        String traceId = tracer.currentSpan() != null
                ? tracer.currentSpan().context().traceIdString()
                : "unknown";
        context.addLowCardinalityKeyValue(io.micrometer.common.KeyValue.of("trace_id", traceId));
    }

    @Override
    public boolean supportsContext(Context context) {
        return true;
    }
}

