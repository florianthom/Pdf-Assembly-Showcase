package com.florianthom.pdfassemblyshowcase.config;

import com.florianthom.pdfassemblyshowcase.properties.SigningProperties;
import eu.europa.esig.dss.service.tsp.OnlineTSPSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TspConfig {

    private final SigningProperties signingProperties;

    public TspConfig(SigningProperties signingProperties) {
        this.signingProperties = signingProperties;
    }

    @Bean
    public OnlineTSPSource tsaSource() {
        System.out.println(signingProperties);
        OnlineTSPSource tsp = new OnlineTSPSource(signingProperties.getTspSourceUrl());
        if (signingProperties.getTspPolicyOid() != null && !signingProperties.getTspPolicyOid().isEmpty()) {
            tsp.setPolicyOid(signingProperties.getTspPolicyOid());
        }
        return tsp;
    }
}