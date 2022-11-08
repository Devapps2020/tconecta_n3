package com.blm.qiubopay.models.sepomex;

public class SepomexInformation {

    private String d_codigo;//Código Postal asentamiento
    private String d_asenta;//Nombre asentamiento
    private String d_tipo_asenta;//Tipo de asentamiento (Catálogo SEPOMEX)
    private String D_mnpio;//Nombre Municipio (INEGI, Marzo 2013)
    private String d_estado;//Nombre Entidad (INEGI, Marzo 2013)
    private String d_ciudad;//Nombre Ciudad (Catálogo SEPOMEX)
    private String d_CP;//Código Postal de la Administración Postal que reparte al asentamiento
    private String c_estado;//Clave Entidad (INEGI, Marzo 2013)
    private String c_oficina;//Código Postal de la Administración Postal que reparte al asentamiento
    private String c_CP;//Campo Vacio
    private String c_tipo_asenta;//Clave Tipo de asentamiento (Catálogo SEPOMEX)
    private String c_mnpio;//Clave Municipio (INEGI, Marzo 2013)
    private String id_asenta_cpcons;//Identificador único del asentamiento (nivel municipal)
    private String d_zona;//Zona en la que se ubica el asentamiento (Urbano/Rural)
    private String c_cve_ciudad;//Clave Ciudad (Catálogo SEPOMEX)

    public String getD_codigo() {
        return d_codigo;
    }

    public void setD_codigo(String d_codigo) {
        this.d_codigo = d_codigo;
    }

    public String getD_asenta() {
        return d_asenta;
    }

    public void setD_asenta(String d_asenta) {
        this.d_asenta = d_asenta;
    }

    public String getD_tipo_asenta() {
        return d_tipo_asenta;
    }

    public void setD_tipo_asenta(String d_tipo_asenta) {
        this.d_tipo_asenta = d_tipo_asenta;
    }

    public String getD_mnpio() {
        return D_mnpio;
    }

    public void setD_mnpio(String d_mnpio) {
        D_mnpio = d_mnpio;
    }

    public String getD_estado() {
        return d_estado;
    }

    public void setD_estado(String d_estado) {
        this.d_estado = d_estado;
    }

    public String getD_ciudad() {
        return d_ciudad;
    }

    public void setD_ciudad(String d_ciudad) {
        this.d_ciudad = d_ciudad;
    }

    public String getD_CP() {
        return d_CP;
    }

    public void setD_CP(String d_CP) {
        this.d_CP = d_CP;
    }

    public String getC_estado() {
        return c_estado;
    }

    public void setC_estado(String c_estado) {
        this.c_estado = c_estado;
    }

    public String getC_oficina() {
        return c_oficina;
    }

    public void setC_oficina(String c_oficina) {
        this.c_oficina = c_oficina;
    }

    public String getC_CP() {
        return c_CP;
    }

    public void setC_CP(String c_CP) {
        this.c_CP = c_CP;
    }

    public String getC_tipo_asenta() {
        return c_tipo_asenta;
    }

    public void setC_tipo_asenta(String c_tipo_asenta) {
        this.c_tipo_asenta = c_tipo_asenta;
    }

    public String getC_mnpio() {
        return c_mnpio;
    }

    public void setC_mnpio(String c_mnpio) {
        this.c_mnpio = c_mnpio;
    }

    public String getId_asenta_cpcons() {
        return id_asenta_cpcons;
    }

    public void setId_asenta_cpcons(String id_asenta_cpcons) {
        this.id_asenta_cpcons = id_asenta_cpcons;
    }

    public String getD_zona() {
        return d_zona;
    }

    public void setD_zona(String d_zona) {
        this.d_zona = d_zona;
    }

    public String getC_cve_ciudad() {
        return c_cve_ciudad;
    }

    public void setC_cve_ciudad(String c_cve_ciudad) {
        this.c_cve_ciudad = c_cve_ciudad;
    }
}
