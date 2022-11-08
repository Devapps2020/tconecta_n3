package com.blm.qiubopay.models.visa.request;

import java.io.Serializable;

public class QPAY_VisaMagneticStripeRequest implements Serializable {

    private CspHeader cspHeader;

    private CspBody cspBody;

    public QPAY_VisaMagneticStripeRequest()
    {
        this.cspHeader = new CspHeader();
        this.cspBody = new CspBody();
        this.cspBody.setEmv(null);
    }

    public CspHeader getCspHeader() {
        return cspHeader;
    }

    public void setCspHeader(CspHeader cspHeader) {
        this.cspHeader = cspHeader;
    }

    public CspBody getCspBody() {
        return cspBody;
    }

    public void setCspBody(CspBody cspBody) {
        this.cspBody = cspBody;
    }
}
