package com.blm.qiubopay.models;

public class HOcrMatch {

    private String match;

    private String value;

    private Boolean required;

    public HOcrMatch(String match) {
        this.match = match;
    }

    public HOcrMatch(String match, boolean required) {
        this.match = match;
        this.required = required;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getValue() {
        return value != null ? value.trim() : "";
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isRequired() {
        return required != null ? required : false;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isValid() {
        return value != null && !value.isEmpty();
    }

}
