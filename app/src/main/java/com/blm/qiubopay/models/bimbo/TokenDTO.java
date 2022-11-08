package com.blm.qiubopay.models.bimbo;

import com.google.gson.Gson;
import com.blm.qiubopay.helpers.interfaces.IModelDTO;

public class TokenDTO implements IModelDTO {

    private String qpay_seed;

    private String qpay_response;
    private String qpay_code;
    private String qpay_description;

    private TokenDTO[] qpay_object;

    private String codigo;
    private String mensaje;
    private String token;

    private String access_token;

    public TokenDTO(){

    }

    public String getQpay_seed() {
        return qpay_seed;
    }

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenDTO[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(TokenDTO[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    @Override
    public Class getType(){
        return TokenDTO.class;
    }

    @Override
    public String getGson() {
        return new Gson().toJson(this);
    }

}
