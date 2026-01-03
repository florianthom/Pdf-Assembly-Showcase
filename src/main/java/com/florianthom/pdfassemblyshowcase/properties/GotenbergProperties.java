package com.florianthom.pdfassemblyshowcase.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gotenberg")
public record GotenbergProperties(
        String gotenbergUrl,
        String username,
        String password
) {}