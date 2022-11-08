package com.blm.qiubopay.models.bimbo;

import com.google.gson.Gson;
import com.blm.qiubopay.helpers.interfaces.IModelDTO;

public class SoporteDTO implements IModelDTO {

    private String qpay_seed;
    private String case_issue_category;
    private String case_issue_type;
    private String case_issue_detail;
    private String source;

    private String user_type;
    private String commerce_name;
    private String commerce_cp;

    private String qpay_response;
    private String qpay_code;
    private String qpay_description;

    public SoporteDTO(){

    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public String getCase_issue_category() {
        return case_issue_category;
    }

    public void setCase_issue_category(String case_issue_category) {
        this.case_issue_category = case_issue_category;
    }

    public String getCase_issue_type() {
        return case_issue_type;
    }

    public void setCase_issue_type(String case_issue_type) {
        this.case_issue_type = case_issue_type;
    }

    public String getCase_issue_detail() {
        return case_issue_detail;
    }

    public void setCase_issue_detail(String case_issue_detail) {
        this.case_issue_detail = case_issue_detail;
    }

    public String getQpay_response() {
        return qpay_response;
    }

    public void setQpay_response(String qpay_response) {
        this.qpay_response = qpay_response;
    }

    public String getQpay_code() {
        return qpay_code;
    }

    public void setQpay_code(String qpay_code) {
        this.qpay_code = qpay_code;
    }

    public String getQpay_description() {
        return qpay_description;
    }

    public void setQpay_description(String qpay_description) {
        this.qpay_description = qpay_description;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getCommerce_name() {
        return commerce_name;
    }

    public void setCommerce_name(String commerce_name) {
        this.commerce_name = commerce_name;
    }

    public String getCommerce_cp() {
        return commerce_cp;
    }

    public void setCommerce_cp(String commerce_cp) {
        this.commerce_cp = commerce_cp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public Class getType(){
        return SoporteDTO.class;
    }

    @Override
    public String getGson() {
        return new Gson().toJson(this);
    }

}
