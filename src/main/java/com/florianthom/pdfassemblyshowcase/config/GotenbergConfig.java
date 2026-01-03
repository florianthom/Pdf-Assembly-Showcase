package com.florianthom.pdfassemblyshowcase.config;

import com.florianthom.pdfassemblyshowcase.properties.GotenbergProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GotenbergConfig {

    private final GotenbergProperties gotenbergProps;

    public GotenbergConfig(GotenbergProperties gotenbergProps) {
        this.gotenbergProps = gotenbergProps;
    }

    @Bean
    public WebClient gotenbergWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(gotenbergProps.gotenbergUrl())
                .defaultHeaders(headers -> headers.setBasicAuth(gotenbergProps.username(), gotenbergProps.password()))
                .build();
    }
}
