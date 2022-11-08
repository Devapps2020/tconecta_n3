package com.blm.qiubopay.evo;

import com.blm.qiubopay.R;
import com.blm.qiubopay.helpers.interfaces.ICallback;
import com.mastercard.gateway.android.sdk.Gateway;
import com.mastercard.gateway.android.sdk.GatewayCallback;
import com.mastercard.gateway.android.sdk.GatewayMap;

import java.util.UUID;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.interfaces.IAlertButton;

public class EvoController {

    private Gateway gateway;
    private String sessionId, apiVersion, threeDSecureId, orderId, transactionId, amount;
    private ApiController apiController;
    private HActivity context;

    public EvoController(HActivity context) {
        this.context = context;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getThreeDSecureId() {
        return threeDSecureId;
    }

    public void setThreeDSecureId(String threeDSecureId) {
        this.threeDSecureId = threeDSecureId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public ApiController getApiController() {
        return apiController;
    }

    public void setApiController(ApiController apiController) {
        this.apiController = apiController;
    }

    public void createSession(ICallback callback) {

        context.loading(true);

        apiController.createSession(new ApiController.CreateSessionCallback() {
            @Override
            public void onSuccess(String sessionId, String apiVersion) {
                context.loading(false);
                setSessionId(sessionId);
                setApiVersion(apiVersion);
                callback.onSuccess(sessionId, apiVersion);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                context.loading(false);
                context.alert(R.string.error_general_evo, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "Aceptar";
                    }

                    @Override
                    public void onClick() {
                        callback.onError();

                    }
                });
            }
        });
    }

    public void updateSession(ICallback callback, String name, String number, String expirym, String expiryy, String code) {

        context.loading(true);

        // build the gateway request
        GatewayMap request = new GatewayMap()
                .set("sourceOfFunds.provided.card.nameOnCard", name)
                .set("sourceOfFunds.provided.card.number", number)
                .set("sourceOfFunds.provided.card.securityCode", code)
                .set("sourceOfFunds.provided.card.expiry.month", expirym)
                .set("sourceOfFunds.provided.card.expiry.year", expiryy);

        getGateway().updateSession(sessionId, apiVersion, request, new GatewayCallback() {
            @Override
            public void onSuccess(GatewayMap response) {
                callback.onSuccess(sessionId);
            }
            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                context.loading(false);
                context.alert("Hubo un error al procesar su petición\n" + throwable.getMessage(), new IAlertButton() {
                    @Override
                    public String onText() {
                        return "Aceptar";
                    }

                    @Override
                    public void onClick() {
                        callback.onError();

                    }
                });
            }
        });
    }

    public void check3dsEnrollment(ICallback callback) {

        // generate a random 3DSecureId for testing
        String threeDSId = UUID.randomUUID().toString();
        threeDSId = threeDSId.substring(0, threeDSId.indexOf('-'));

        apiController.check3DSecureEnrollment(sessionId, amount, Config.CURRENCY.getValue(context), threeDSId, new ApiController.Check3DSecureEnrollmentCallback() {
            @Override
            public void onSuccess(GatewayMap response) {

                int apiVersionInt = Integer.valueOf(apiVersion);
                String threeDSecureId = (String) response.get("gatewayResponse.3DSecureId");

                String html = null;
                if (response.containsKey("gatewayResponse.3DSecure.authenticationRedirect.simple.htmlBodyContent")) {
                    html = (String) response.get("gatewayResponse.3DSecure.authenticationRedirect.simple.htmlBodyContent");
                }

                // for API versions <= 46, you must use the summary status field to determine next steps for 3DS
                if (apiVersionInt <= 46) {
                    String summaryStatus = (String) response.get("gatewayResponse.3DSecure.summaryStatus");

                    if ("CARD_ENROLLED".equalsIgnoreCase(summaryStatus)) {
                        Gateway.start3DSecureActivity(context, html, "Autenticación 3DS");
                        return;
                    }

                    setThreeDSecureId(null);

                    // for these 2 cases, you still provide the 3DSecureId with the pay operation
                    if ("CARD_NOT_ENROLLED".equalsIgnoreCase(summaryStatus) || "AUTHENTICATION_NOT_AVAILABLE".equalsIgnoreCase(summaryStatus)) {
                        setThreeDSecureId(threeDSecureId);
                    }

                    processPayment(callback);
                }

                // for API versions >= 47, you must look to the gateway recommendation and the presence of 3DS info in the payload
                else {
                    String gatewayRecommendation = (String) response.get("gatewayResponse.response.gatewayRecommendation");

                    setThreeDSecureId(threeDSecureId);

                    // if DO_NOT_PROCEED returned in recommendation, should stop transaction
                    if ("DO_NOT_PROCEED".equalsIgnoreCase(gatewayRecommendation)) {
                        context.loading(false);
                        context.alert(R.string.error_general_evo, new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Aceptar";
                            }

                            @Override
                            public void onClick() {
                                callback.onError();

                            }
                        });

                        return;
                    }

                    // if PROCEED in recommendation, and we have HTML for 3ds, perform 3DS
                    if (html != null) {
                        Gateway.start3DSecureActivity(context, html, "Autenticación 3DS");
                        return;
                    }


                    processPayment(callback);
                }

            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                context.loading(false);
                context.alert("Hubo un error al procesar su petición\n" + throwable.getMessage(), new IAlertButton() {
                    @Override
                    public String onText() {
                        return "Aceptar";
                    }

                    @Override
                    public void onClick() {
                        callback.onError();

                    }
                });
            }
        });
    }

    public void processPayment(ICallback callback) {

        apiController.completeSession(sessionId, orderId, transactionId, amount, Config.CURRENCY.getValue(context), threeDSecureId, false, new ApiController.CompleteSessionCallback() {
            @Override
            public void onSuccess(String result) {
                context.loading(false);
                context.alert("Abono realizado con éxito\nEn breve se verá tu saldo reflejado.", new IAlertButton() {
                    @Override
                    public String onText() {
                        return "Aceptar";
                    }

                    @Override
                    public void onClick() {
                        callback.onSuccess(result);

                    }
                });

            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                context.loading(false);
                context.alert("Hubo un error al procesar su petición\n" + throwable.getMessage(), new IAlertButton() {
                    @Override
                    public String onText() {
                        return "Aceptar";
                    }

                    @Override
                    public void onClick() {
                        callback.onError();

                    }
                });
            }
        });
    }

}
