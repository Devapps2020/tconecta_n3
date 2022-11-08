package com.blm.qiubopay.models.bimbo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelItem {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("value")
    @Expose
    private String value;

    public ModelItem(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public ModelItem(String name) {
        this.name = name;
        this.value = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
