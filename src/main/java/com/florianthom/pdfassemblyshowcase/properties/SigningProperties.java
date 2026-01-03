package com.florianthom.pdfassemblyshowcase.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "pdfassemblyshowcase.signing")
public record SigningProperties (
        boolean checkRevocationForUntrustedChains,
        String tspSourceUrl,
        String pkcs12FileName,
        String tspPolicyOid
) {}