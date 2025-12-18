package com.florianthom.pdfassemblyshowcase.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "signing")
public class SigningProperties {

    private boolean checkRevocationForUntrustedChains = false;
    private String tspSourceUrl;
    private String pkcs12FileName;
    private String tspPolicyOid;

    public boolean getCheckRevocationForUntrustedChains() {
        return checkRevocationForUntrustedChains;
    }

    public String getTspSourceUrl() {
        return tspSourceUrl;
    }

    public String getPkcs12FileName() {
        return pkcs12FileName;
    }

    public void setTspSourceUrl(String tspSourceUrl) {
        this.tspSourceUrl = tspSourceUrl;
    }

    public void setPkcs12FileName(String pkcs12FileName) {
        this.pkcs12FileName = pkcs12FileName;
    }

    public void setCheckRevocationForUntrustedChains(boolean checkRevocationForUntrustedChains) {
        this.checkRevocationForUntrustedChains = checkRevocationForUntrustedChains;
    }

    public String getTspPolicyOid() {
        return tspPolicyOid;
    }

    public void setTspPolicyOid(String tspPolicyOid) {
        this.tspPolicyOid = tspPolicyOid;
    }

    @Override
    public String toString() {
        return "SigningProperties{" +
                "checkRevocationForUntrustedChains=" + checkRevocationForUntrustedChains +
                ", tspSourceUrl='" + tspSourceUrl + '\'' +
                ", pkcs12FileName='" + pkcs12FileName + '\'' +
                ", tspPolicyOid='" + tspPolicyOid + '\'' +
                '}';
    }
}