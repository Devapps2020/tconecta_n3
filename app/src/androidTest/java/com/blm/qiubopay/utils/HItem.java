package com.blm.qiubopay.utils;

public class HItem {

    private String name;
    private String value;
    private String text = "";
    private String tag;
    private Integer index = 0;
    private Boolean check = false;
    private Boolean option = false;
    private Integer position = 0;

    public HItem(String name) {
        this.name = name;
    }

    public HItem(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public HItem(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    public HItem(String name, String value, Integer index) {
        this.name = name;
        this.value = value;
        this.index = index;
    }

    public HItem(String name, String value, Integer index, Boolean option) {
        this.name = name;
        this.value = value;
        this.index = index;
        this.option = option;
    }

    public HItem(String name, Integer index, String tag) {
        this.name = name;
        this.tag = tag;
        this.index = index;
    }

    public HItem(String name, Integer index, Boolean option) {
        this.name = name;
        this.value = value;
        this.index = index;
        this.option = option;
    }

    public HItem(Integer position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {

        if(value != null) {
            return value.replaceAll(",", "").replace("$", "");
        }

        return null;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Boolean getOption() {
        return option;
    }

    public void setOption(Boolean option) {
        this.option = option;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
