package com.blm.qiubopay.models.bimbo;

public class QrCodi{

    private Integer id;

    private String CRY;

    private String TYP;

    private String monto;

    private String concepto;

    private String referencia;

    private String cuenta;

    private String estatus;

    private String fechaOperacion;

    private String ordenante;

    private IcCodi ic;

    private VCodi v;

    public String getCRY() {
        return CRY;
    }

    public void setCRY(String CRY) {
        this.CRY = CRY;
    }

    public String getTYP() {
        return TYP;
    }

    public void setTYP(String TYP) {
        this.TYP = TYP;
    }

    public IcCodi getIc() {
        return ic;
    }

    public void setIc(IcCodi ic) {
        this.ic = ic;
    }

    public VCodi getV() {
        return v;
    }

    public void setV(VCodi v) {
        this.v = v;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getFechaOperacion() {
        return fechaOperacion;
    }

    public void setFechaOperacion(String fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public String getOrdenante() {
        return ordenante;
    }

    public void setOrdenante(String ordenante) {
        this.ordenante = ordenante;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
