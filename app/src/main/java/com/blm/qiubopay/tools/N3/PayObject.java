package com.blm.qiubopay.tools.N3;

import com.nexgo.oaf.api.cardReader.CardInfoEntity;
import com.blm.qiubopay.models.visa.request.QPAY_VisaEmvRequest;

public class PayObject {

    public static QPAY_VisaEmvRequest getK206VisaObject(boolean isMagneticStripeCard, boolean isFallbackTxn, boolean isCashbackTransaction, CardInfoEntity magneticStripeInfo, float cashbackTotalAmount){

        QPAY_VisaEmvRequest visaEmvRequest = new QPAY_VisaEmvRequest();

        visaEmvRequest.getCspBody().setTxId(null);

        /*if(isMagneticStripeCard || isFallbackTxn)
        {
            //Es una transacción Banda
            visaEmvRequest.getCspBody().setEmv(null);

            //cspHeader
            visaEmvRequest.getCspHeader().setQpay_entryMode("2");
            visaEmvRequest.getCspHeader().setQpay_tdc(magneticStripeInfo.getCardNumber().replace("f","*"));
            visaEmvRequest.getCspHeader().setQpay_cardHolder("CLIENTE");
            visaEmvRequest.getCspHeader().setQpay_latitude("0.0");
            visaEmvRequest.getCspHeader().setQpay_longitude("0.0");
            visaEmvRequest.getCspHeader().setQpay_pin("0");
            pinFlag = "0";

            //cspBody
            //Se checa si es una transacción de cashback
            if (isCashbackTransaction) {
                visaEmvRequest.getCspHeader().setProductId("13005");
                visaEmvRequest.getCspBody().setAmt(Utils.paserCurrency(cashbackTotalAmount + "").replace("$","").replace(",",""));
                visaEmvRequest.getCspBody().setCashbackAmt(getImporte());
                visaEmvRequest.getCspBody().setCashbackFee(cashBackBin.getFee());
            } else {
                visaEmvRequest.getCspHeader().setProductId("13001");
                visaEmvRequest.getCspBody().setAmt(getImporte());
                visaEmvRequest.getCspBody().setCashbackAmt("0");
                visaEmvRequest.getCspBody().setCashbackFee("0");
            }

            visaEmvRequest.getCspBody().setKeyId("51");
            visaEmvRequest.getCspBody().setEncryptionMode("2");
            visaEmvRequest.getCspBody().setTrack2(magneticStripeInfo.getTrack2());
            visaEmvRequest.getCspBody().setPosCondCode("0");
            visaEmvRequest.getCspBody().setCardholderIdMet("2");
            visaEmvRequest.getCspBody().setPinEntryMode("0");
            visaEmvRequest.getCspBody().setExpYear(magneticStripeInfo.getExpiryYearEnc());
            visaEmvRequest.getCspBody().setExpMonth(magneticStripeInfo.getExpiryMonthEnc());
            //visaEmvRequest.getCspBody().setIssuer("13");
            visaEmvRequest.getCspBody().setIssuer("7");
            if(isFallbackTxn)
                visaEmvRequest.getCspBody().setCapture("17");
            else
                visaEmvRequest.getCspBody().setCapture("1");
        }
        else {
            //Es una transacción EMV
            tlvList = TLVUtils.builderTLVMap(emvCardInfo.getIcDataFull().toLowerCase());

            String track2 = "";
            String expMonth = "";
            String expYear = "";
            String cipherMode = "";

            emvTlvList = TLVUtils.builderTLVMap(tlvList.get("c2").getValue());


            if (tlvList.containsKey("c3") && tlvList.containsKey("c4") && tlvList.containsKey("c5")) {
                //Información cifrada
                track2 = tlvList.get("c3").getValue();
                expYear = tlvList.get("c4").getValue();
                expMonth = tlvList.get("c5").getValue();
                cipherMode = "2";
                visaEmvRequest.getCspBody().setKeyId("51");
            } else {
                //Información en claro
                //cspBody
                track2 = getFormatedTrack2(emvTlvList.get("57").getValue());
                expMonth = "20";
                expYear = "08";
                cipherMode = "0";
                visaEmvRequest.getCspBody().setKeyId(null);
            }

            //cspHeader
            visaEmvRequest.getCspHeader().setQpay_entryMode("1");
            visaEmvRequest.getCspHeader().setQpay_tdc(emvTlvList.get("5a").getValue().replace("f", "*"));
            if (emvTlvList.containsKey("5f20"))
                visaEmvRequest.getCspHeader().setQpay_cardHolder(Tools.hexStringToString(emvTlvList.get("5f20").getValue()));
            else
                visaEmvRequest.getCspHeader().setQpay_cardHolder("CLIENTE");
            visaEmvRequest.getCspHeader().setQpay_latitude("0.0");
            visaEmvRequest.getCspHeader().setQpay_longitude("0.0");
            visaEmvRequest.getCspHeader().setQpay_application_label(Tools.hexStringToString(emvTlvList.get("50").getValue()));

            //cspBody
            //Se checa si es una transacción de cashback
            if (isCashbackTransaction) {
                visaEmvRequest.getCspHeader().setProductId("13005");
                visaEmvRequest.getCspBody().setAmt(Utils.paserCurrency(cashbackTotalAmount + "").replace("$", "").replace(",", ""));
                visaEmvRequest.getCspBody().setCashbackAmt(getImporte());
                visaEmvRequest.getCspBody().setCashbackFee(cashBackBin.getFee());
            } else {
                visaEmvRequest.getCspHeader().setProductId("13001");
                visaEmvRequest.getCspBody().setAmt(getImporte());
                visaEmvRequest.getCspBody().setCashbackAmt("0");
                visaEmvRequest.getCspBody().setCashbackFee("0");
            }

            visaEmvRequest.getCspBody().setEncryptionMode(cipherMode);//"0");//0
            visaEmvRequest.getCspBody().setTrack2(track2);//getFormatedTrack2(emvTlvList.get("57").getValue()));
            visaEmvRequest.getCspBody().setPosCondCode("0");
            visaEmvRequest.getCspBody().setCardholderIdMet("2");

            pinFlag = emvTlvList.get("9f34").getValue().substring(4, 6);

            if (pinFlag.equals("00")) {
                visaEmvRequest.getCspBody().setPinEntryMode("0");
                isPinAuthentication = false;
                visaEmvRequest.getCspHeader().setQpay_pin("0");
                pinFlag = "0";
            } else if (pinFlag.equals("02")) {
                visaEmvRequest.getCspBody().setPinEntryMode("1");
                isPinAuthentication = true;
                visaEmvRequest.getCspHeader().setQpay_pin("1");
                pinFlag = "1";
            }
            visaEmvRequest.getCspBody().setExpYear(expYear);//"20");
            visaEmvRequest.getCspBody().setExpMonth(expMonth);//"08");
            //visaEmvRequest.getCspBody().setIssuer("13");
            visaEmvRequest.getCspBody().setIssuer("7");
            visaEmvRequest.getCspBody().setCapture("9");

            //emv
            visaEmvRequest.getCspBody().getEmv().setAid(emvTlvList.get("4f").getValue());//A0000000031010
            visaEmvRequest.getCspBody().getEmv().setApplicationInterchangeProfile(emvTlvList.get("82").getValue());//1c00
            visaEmvRequest.getCspBody().getEmv().setDedicatedFileName(emvTlvList.get("84").getValue());//a0000000031010
            visaEmvRequest.getCspBody().getEmv().setTerminalVerificationResults(emvTlvList.get("95").getValue());//8000008000
            visaEmvRequest.getCspBody().getEmv().setTransactionDate(emvTlvList.get("9a").getValue());//151007
            visaEmvRequest.getCspBody().getEmv().setTsi(emvTlvList.get("9b").getValue());//6800
            visaEmvRequest.getCspBody().getEmv().setTransactionType(emvTlvList.get("9c").getValue());//0
            visaEmvRequest.getCspBody().getEmv().setIssuerCountryCode(emvTlvList.get("5f28").getValue());//840

            visaEmvRequest.getCspBody().getEmv().setTransactionCurrencyCode(emvTlvList.get("5f2a").getValue());//0484
            visaEmvRequest.getCspBody().getEmv().setCardSequenceNumber(emvTlvList.get("5f34").getValue());// Se cambia de 9f41 a 5f34
            visaEmvRequest.getCspBody().getEmv().setTransactionAmount(emvTlvList.get("9f02").getValue());//txtAmount.getText().toString().replace("$", "").replace(".", ""));//Se cambia el monto por el regresado en el tag 9f02
            visaEmvRequest.getCspBody().getEmv().setAmountOther(emvTlvList.get("9f03").getValue());//"0");//100
            visaEmvRequest.getCspBody().getEmv().setApplicationVersionNumber(emvTlvList.get("9f09").getValue());//008c

            visaEmvRequest.getCspBody().getEmv().setIssuerApplicationData(emvTlvList.get("9f10").getValue());//06010a03a40000
            visaEmvRequest.getCspBody().getEmv().setTerminalCountryCode(emvTlvList.get("9f1a").getValue());//484
            visaEmvRequest.getCspBody().getEmv().setInterfaceDeviceSerialNumber(emvTlvList.get("9f1e").getValue());//324b353534323833
            visaEmvRequest.getCspBody().getEmv().setApplicationCryptogram(emvTlvList.get("9f26").getValue());//543a534f738c3993
            visaEmvRequest.getCspBody().getEmv().setCryptogramInformationData(emvTlvList.get("9f27").getValue());//80
            visaEmvRequest.getCspBody().getEmv().setTerminalCapabilities(emvTlvList.get("9f33").getValue());//e0b0c8
            visaEmvRequest.getCspBody().getEmv().setCardholderVerificationMethodResults(emvTlvList.get("9f34").getValue());//410302
            visaEmvRequest.getCspBody().getEmv().setTerminalType(emvTlvList.get("9f35").getValue());//22
            visaEmvRequest.getCspBody().getEmv().setApplicationTransactionCounter(emvTlvList.get("9f36").getValue());//0100
            visaEmvRequest.getCspBody().getEmv().setUnpredictableNumber(emvTlvList.get("9f37").getValue());//1eb0b54e
            visaEmvRequest.getCspBody().getEmv().setTransactionSequenceCounterId(emvTlvList.get("9f41").getValue());//17794
            visaEmvRequest.getCspBody().getEmv().setApplicationCurrencyCode(emvTlvList.get("9f1a").getValue());//484//"0484");//840
        }*/

        return visaEmvRequest;
    }
}
