package org.springframework.samples.petclinic.customers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class RequestLoggingConfig {

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludeClientInfo(true);
        filter.setIncludePayload(true); // Log nội dung body (POST/PUT)
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false); // Có thể bật nếu bạn cần log header
        return filter;
    }
}
