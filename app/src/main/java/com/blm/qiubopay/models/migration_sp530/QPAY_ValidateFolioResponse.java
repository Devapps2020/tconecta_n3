package com.blm.qiubopay.models.migration_sp530;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import java.io.Serializable;

public class QPAY_ValidateFolioResponse implements Serializable {
    private String qpay_mail;
    private String qpay_seed;
    private String qpay_name;
    private String qpay_mother_surname;
    private String qpay_father_surname;

    public String getQpay_mail() {
        return qpay_mail;
    }

    public void setQpay_mail(String qpay_mail) {
        this.qpay_mail = qpay_mail;
    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getQpay_name() {
        return qpay_name;
    }

    public void setQpay_name(String qpay_name) {
        this.qpay_name = qpay_name;
    }

    public String getQpay_mother_surname() {
        return qpay_mother_surname;
    }

    public void setQpay_mother_surname(String qpay_mother_surname) {
        this.qpay_mother_surname = qpay_mother_surname;
    }

    public String getQpay_father_surname() {
        return qpay_father_surname;
    }

    public void setQpay_father_surname(String qpay_father_surname) {
        this.qpay_father_surname = qpay_father_surname;
    }

    public static class QPAY_ValidateFolioResponseExcluder implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaredType().equals(QPAY_ValidateFolioResponse.class);
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }
}
