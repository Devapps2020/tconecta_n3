package com.blm.qiubopay.models;

import java.io.Serializable;

public class RegisterUserResponse extends GenericResponse implements Serializable {

    public DynamicDataRegisterUser dynamicData;

    public DynamicDataRegisterUser getDynamicData() {
        return dynamicData;
    }

    public void setDynamicData(DynamicDataRegisterUser dynamicData) {
        this.dynamicData = dynamicData;
    }

}
