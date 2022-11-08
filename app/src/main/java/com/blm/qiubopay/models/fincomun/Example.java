
package com.blm.qiubopay.models.fincomun;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Example {

    @SerializedName("tipoCredito")
    @Expose
    private List<TipoCredito> tipoCredito = null;
    @SerializedName("productoCredito")
    @Expose
    private List<ProductoCredito> productoCredito = null;
    @SerializedName("modCredito")
    @Expose
    private List<ModCredito> modCredito = null;
    @SerializedName("matriz")
    @Expose
    private List<Matriz> matriz = null;
    @SerializedName("genero")
    @Expose
    private List<Genero> genero = null;
    @SerializedName("estado_civil")
    @Expose
    private List<EstadoCivil> estadoCivil = null;
    @SerializedName("ocupacion")
    @Expose
    private List<Ocupacion> ocupacion = null;
    @SerializedName("nacionalidad")
    @Expose
    private List<Nacionalidad> nacionalidad = null;
    @SerializedName("solicitud")
    @Expose
    private List<Solicitud> solicitud = null;
    @SerializedName("lugarNacimiento")
    @Expose
    private List<LugarNacimiento> lugarNacimiento = null;
    @SerializedName("respuesta")
    @Expose
    private Respuesta respuesta;
    @SerializedName("gradoEstudios")
    @Expose
    private List<GradoEstudio> gradoEstudios = null;
    @SerializedName("estatusSolicitud")
    @Expose
    private List<EstatusSolicitud> estatusSolicitud = null;
    @SerializedName("vivienda")
    @Expose
    private List<Vivienda> vivienda = null;
    @SerializedName("giroNegocio")
    @Expose
    private List<GiroNegocio> giroNegocio = null;
    @SerializedName("sector")
    @Expose
    private List<Sector> sector = null;
    @SerializedName("municipio")
    @Expose
    private List<Municipio> municipio = null;
    @SerializedName("puesto")
    @Expose
    private List<Puesto> puesto = null;
    @SerializedName("cliente_premium")
    @Expose
    private List<ClientePremium> clientePremium = null;
    @SerializedName("afiliados")
    @Expose
    private List<Afiliado> afiliados = null;

    public List<TipoCredito> getTipoCredito() {
        return tipoCredito;
    }

    public void setTipoCredito(List<TipoCredito> tipoCredito) {
        this.tipoCredito = tipoCredito;
    }

    public List<ProductoCredito> getProductoCredito() {
        return productoCredito;
    }

    public void setProductoCredito(List<ProductoCredito> productoCredito) {
        this.productoCredito = productoCredito;
    }

    public List<ModCredito> getModCredito() {
        return modCredito;
    }

    public void setModCredito(List<ModCredito> modCredito) {
        this.modCredito = modCredito;
    }

    public List<Matriz> getMatriz() {
        return matriz;
    }

    public void setMatriz(List<Matriz> matriz) {
        this.matriz = matriz;
    }

    public List<Genero> getGenero() {
        return genero;
    }

    public void setGenero(List<Genero> genero) {
        this.genero = genero;
    }

    public List<EstadoCivil> getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(List<EstadoCivil> estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public List<Ocupacion> getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(List<Ocupacion> ocupacion) {
        this.ocupacion = ocupacion;
    }

    public List<Nacionalidad> getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(List<Nacionalidad> nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public List<Solicitud> getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(List<Solicitud> solicitud) {
        this.solicitud = solicitud;
    }

    public List<LugarNacimiento> getLugarNacimiento() {
        return lugarNacimiento;
    }

    public void setLugarNacimiento(List<LugarNacimiento> lugarNacimiento) {
        this.lugarNacimiento = lugarNacimiento;
    }

    public Respuesta getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Respuesta respuesta) {
        this.respuesta = respuesta;
    }

    public List<GradoEstudio> getGradoEstudios() {
        return gradoEstudios;
    }

    public void setGradoEstudios(List<GradoEstudio> gradoEstudios) {
        this.gradoEstudios = gradoEstudios;
    }

    public List<EstatusSolicitud> getEstatusSolicitud() {
        return estatusSolicitud;
    }

    public void setEstatusSolicitud(List<EstatusSolicitud> estatusSolicitud) {
        this.estatusSolicitud = estatusSolicitud;
    }

    public List<Vivienda> getVivienda() {
        return vivienda;
    }

    public void setVivienda(List<Vivienda> vivienda) {
        this.vivienda = vivienda;
    }

    public List<GiroNegocio> getGiroNegocio() {
        return giroNegocio;
    }

    public void setGiroNegocio(List<GiroNegocio> giroNegocio) {
        this.giroNegocio = giroNegocio;
    }

    public List<Sector> getSector() {
        return sector;
    }

    public void setSector(List<Sector> sector) {
        this.sector = sector;
    }

    public List<Municipio> getMunicipio() {
        return municipio;
    }

    public void setMunicipio(List<Municipio> municipio) {
        this.municipio = municipio;
    }

    public List<Puesto> getPuesto() {
        return puesto;
    }

    public void setPuesto(List<Puesto> puesto) {
        this.puesto = puesto;
    }

    public List<ClientePremium> getClientePremium() {
        return clientePremium;
    }

    public void setClientePremium(List<ClientePremium> clientePremium) {
        this.clientePremium = clientePremium;
    }

    public List<Afiliado> getAfiliados() {
        return afiliados;
    }

    public void setAfiliados(List<Afiliado> afiliados) {
        this.afiliados = afiliados;
    }

}
