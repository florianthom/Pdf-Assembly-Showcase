package com.florianthom.pdfassemblyshowcase.config;

import com.florianthom.pdfassemblyshowcase.properties.SigningProperties;
import eu.europa.esig.dss.alert.LogOnStatusAlert;
import eu.europa.esig.dss.alert.StatusAlert;
import eu.europa.esig.dss.spi.validation.CommonCertificateVerifier;
import org.slf4j.event.Level;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CertificateVerifierConfig {

    private final SigningProperties signingProperties;

    public CertificateVerifierConfig(SigningProperties signingProperties) {
        this.signingProperties = signingProperties;
    }

    @Bean
    public CommonCertificateVerifier certificateVerifier() {
        CommonCertificateVerifier verifier = new CommonCertificateVerifier();
        verifier.setCheckRevocationForUntrustedChains(signingProperties.getCheckRevocationForUntrustedChains());

        // MUST NOT GO IN PRODUCTION
        verifier.setAlertOnMissingRevocationData(new LogOnStatusAlert(Level.INFO));
        return verifier;
    }
}
