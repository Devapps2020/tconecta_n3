package com.blm.qiubopay.models.proceedings;

import java.io.Serializable;

public class QPAY_AllDataRecord_Object implements Serializable {

    User UserObject;
    Account AccountObject;
    DataRecord DataRecordObject;
    FcIdentification FcIdentificationObject;
    FcPersonalData FcPersonalDataObject;


    // Getter Methods

    public User getUser() {
        return UserObject;
    }

    public Account getAccount() {
        return AccountObject;
    }

    public DataRecord getDataRecord() {
        return DataRecordObject;
    }

    public FcIdentification getFcIdentification() {
        return FcIdentificationObject;
    }

    public FcPersonalData getFcPersonalData() {
        return FcPersonalDataObject;
    }

    // Setter Methods

    public void setUser(User userObject) {
        this.UserObject = userObject;
    }

    public void setAccount(Account accountObject) {
        this.AccountObject = accountObject;
    }

    public void setDataRecord(DataRecord dataRecordObject) {
        this.DataRecordObject = dataRecordObject;
    }

    public void setFcIdentification(FcIdentification fcIdentificationObject) {
        this.FcIdentificationObject = fcIdentificationObject;
    }

    public void setFcPersonalData(FcPersonalData fcPersonalDataObject) {
        this.FcPersonalDataObject = fcPersonalDataObject;
    }
}

