package com.blm.qiubopay.models;

import java.io.Serializable;
import java.util.List;

public class DynamicDataRegisterUser implements Serializable {


    private List<Entry> entry;

    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }

    /*List<Map.Entry<String,String>> entry;

    public List<Map.Entry<String, String>> getEntry() {
        return entry;
    }

    public void setEntry(List<Map.Entry<String, String>> entry) {
        this.entry = entry;
    }*/
}
