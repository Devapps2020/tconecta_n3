package com.blm.qiubopay.models.proceedings;

import java.io.Serializable;

public class QPAY_UserFcPersonalData implements Serializable {

    private String qpay_seed;
    private String folio;
    private String num_cliente;
    private String genero;
    private String estado_civil;
    private String grado_estudios;
    private String pais;
    private String nacionalidad;
    private String ocupacion;
    private String lugar_nacimiento;
    private String usuario;
    private String sucursal;
    private float dependientes_economicos;
    private float tipo_vivienda;
    private float horario_contacto;
    private String tokenJwt;


    // Getter Methods

    public String getQpay_seed() {
        return qpay_seed;
    }

    public String getFolio() {
        return folio;
    }

    public String getNum_cliente() {
        return num_cliente;
    }

    public String getGenero() {
        return genero;
    }

    public String getEstado_civil() {
        return estado_civil;
    }

    public String getGrado_estudios() {
        return grado_estudios;
    }

    public String getPais() {
        return pais;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public String getLugar_nacimiento() {
        return lugar_nacimiento;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getSucursal() {
        return sucursal;
    }

    public float getDependientes_economicos() {
        return dependientes_economicos;
    }

    public float getTipo_vivienda() {
        return tipo_vivienda;
    }

    public float getHorario_contacto() {
        return horario_contacto;
    }

    public String getTokenJwt() {
        return tokenJwt;
    }

    // Setter Methods

    public void setQpay_seed(String qpay_seed) {
        this.qpay_seed = qpay_seed;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public void setNum_cliente(String num_cliente) {
        this.num_cliente = num_cliente;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setEstado_civil(String estado_civil) {
        this.estado_civil = estado_civil;
    }

    public void setGrado_estudios(String grado_estudios) {
        this.grado_estudios = grado_estudios;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public void setLugar_nacimiento(String lugar_nacimiento) {
        this.lugar_nacimiento = lugar_nacimiento;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public void setDependientes_economicos(float dependientes_economicos) {
        this.dependientes_economicos = dependientes_economicos;
    }

    public void setTipo_vivienda(float tipo_vivienda) {
        this.tipo_vivienda = tipo_vivienda;
    }

    public void setHorario_contacto(float horario_contacto) {
        this.horario_contacto = horario_contacto;
    }

    public void setTokenJwt(String tokenJwt) {
        this.tokenJwt = tokenJwt;
    }

}