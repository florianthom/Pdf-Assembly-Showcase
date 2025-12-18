package com.florianthom.pdfassemblyshowcase.services;

import com.florianthom.pdfassemblyshowcase.properties.SigningProperties;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.model.SignatureValue;
import eu.europa.esig.dss.model.ToBeSigned;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.pades.signature.PAdESService;
import eu.europa.esig.dss.service.tsp.OnlineTSPSource;
import eu.europa.esig.dss.spi.validation.CommonCertificateVerifier;
import eu.europa.esig.dss.token.DSSPrivateKeyEntry;
import eu.europa.esig.dss.token.Pkcs12SignatureToken;
import eu.europa.esig.dss.token.SignatureTokenConnection;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

@Service
public class PdfSigningService implements Closeable {
    private final CommonCertificateVerifier verifier;
    private final OnlineTSPSource tspSource;

    private final SignatureTokenConnection token;
    private final DSSPrivateKeyEntry signingKey;

    private final SigningProperties signingProperties;

    public PdfSigningService(
            CommonCertificateVerifier verifier,
            OnlineTSPSource tspSource, SigningProperties signingProperties
    ) {
        this.verifier = verifier;
        this.tspSource = tspSource;
        this.signingProperties = signingProperties;

        try {
            this.token = new Pkcs12SignatureToken(
                    new FileInputStream(signingProperties.getPkcs12FileName()),
                    new KeyStore.PasswordProtection("password".toCharArray())
            );
            this.signingKey = token.getKeys().getFirst();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot initialize signing token", e);
        }
    }

    public byte[] signWithPadesLt(byte[] pdfBytes) {

        PAdESService dssPAdESService = new PAdESService(verifier);
        dssPAdESService.setTspSource(tspSource);

        PAdESSignatureParameters params = new PAdESSignatureParameters();
        params.setSignatureLevel(SignatureLevel.PAdES_BASELINE_LT);
        params.setSignaturePackaging(SignaturePackaging.ENVELOPED);
        params.setDigestAlgorithm(DigestAlgorithm.SHA256);
        params.setSigningCertificate(
                signingKey.getCertificate()
        );
        params.setCertificateChain(
                signingKey.getCertificateChain()
        );

        DSSDocument document = new InMemoryDocument(pdfBytes);

        ToBeSigned dataToSign =
                dssPAdESService.getDataToSign(document, params);

        SignatureValue signatureValue = token.sign(dataToSign, params.getDigestAlgorithm(), signingKey);

        DSSDocument signedDocument =
                dssPAdESService.signDocument(document, params, signatureValue);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            signedDocument.writeTo(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        token.close();
    }
}