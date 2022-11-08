
package com.blm.qiubopay.models.bimbo;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.blm.qiubopay.models.fincomun.Afiliado;
import com.blm.qiubopay.models.fincomun.Banco;
import com.blm.qiubopay.models.fincomun.ClientePremium;
import com.blm.qiubopay.models.fincomun.EstadoCivil;
import com.blm.qiubopay.models.fincomun.EstatusSolicitud;
import com.blm.qiubopay.models.fincomun.Genero;
import com.blm.qiubopay.models.fincomun.GiroNegocio;
import com.blm.qiubopay.models.fincomun.GradoEstudio;
import com.blm.qiubopay.models.fincomun.LugarNacimiento;
import com.blm.qiubopay.models.fincomun.Matriz;
import com.blm.qiubopay.models.fincomun.ModCredito;
import com.blm.qiubopay.models.fincomun.Municipio;
import com.blm.qiubopay.models.fincomun.Nacionalidad;
import com.blm.qiubopay.models.fincomun.Ocupacion;
import com.blm.qiubopay.models.fincomun.Perentesco;
import com.blm.qiubopay.models.fincomun.ProductoCredito;
import com.blm.qiubopay.models.fincomun.Puesto;
import com.blm.qiubopay.models.fincomun.Respuesta;
import com.blm.qiubopay.models.fincomun.Sector;
import com.blm.qiubopay.models.fincomun.Solicitud;
import com.blm.qiubopay.models.fincomun.TipoCredito;
import com.blm.qiubopay.models.fincomun.Vivienda;
import com.blm.qiubopay.utils.Globals;

import java.util.List;

public class FcCatalog {

    @SerializedName("node_description")
    @Expose
    private String nodeDescription;

    @SerializedName("node_hash")
    @Expose
    private String nodeHash;

    @SerializedName("node_data")
    @Expose
    private String nodeData = null;

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
    private List<LugarNacimiento> estados = null;

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

    @SerializedName("destino")
    @Expose
    private List<ModelItem> destinos = null;

    @SerializedName("actividadGeneral")
    @Expose
    private List<ModelItem> actividadGeneral = null;

    @SerializedName("localidad")
    @Expose
    private List<ModelItem> localidad = null;

    @SerializedName("actividadEconomica")
    @Expose
    private List<ModelItem> actividadEconomica = null;

    @SerializedName("horarios")
    @Expose
    private List<ModelItem> horarios = null;

    @SerializedName("parentesco")
    @Expose
    private List<Perentesco> parentesco = null;

    @SerializedName("banco")
    @Expose
    private List<Banco> banco = null;

    public FcCatalog() {}

    public FcCatalog(String nodeDescription) {
        this.nodeDescription = nodeDescription;
    }

    public String getNodeDescription() {
        return nodeDescription;
    }

    public void setNodeDescription(String nodeDescription) {
        this.nodeDescription = nodeDescription;
    }

    public String getNodeHash() {
        return nodeHash;
    }

    public void setNodeHash(String nodeHash) {
        this.nodeHash = nodeHash;
    }

    public String getNodeData() {
        return nodeData;
    }

    public void setNodeData(String nodeData) {
        this.nodeData = nodeData;
    }

    public void parserData( ) {

        String json = null;

        switch (getType(nodeDescription.trim())) {
            case tipoCredito:
                json = "{\"tipoCredito\":[" + nodeData + "]}";
                tipoCredito = new Gson().fromJson(json, FcCatalog.class).getTipoCredito();
                break;
            case productoCredito:
                json = "{\"productoCredito\":[" + nodeData + "]}";
                productoCredito = new Gson().fromJson(json, FcCatalog.class).getProductoCredito();
                break;
            case modCredito:
                json = "{\"modCredito\":[" + nodeData + "]}";
                modCredito = new Gson().fromJson(json, FcCatalog.class).getModCredito();
                break;
            case matriz:
                json = "{\"matriz\":[" + nodeData + "]}";
                matriz = new Gson().fromJson(json, FcCatalog.class).getMatriz();
                break;
            case genero:
                json = "{\"genero\":[" + nodeData + "]}";
                genero = new Gson().fromJson(json, FcCatalog.class).getGenero();
                break;
            case estado_civil:
                json = "{\"estado_civil\":[" + nodeData + "]}";
                estadoCivil = new Gson().fromJson(json, FcCatalog.class).getEstadoCivil();
                break;
            case ocupacion:
                json = "{\"ocupacion\":[" + nodeData + "]}";
                ocupacion = new Gson().fromJson(json, FcCatalog.class).getOcupacion();
                break;
            case nacionalidad:
                json = "{\"nacionalidad\":[" + nodeData + "]}";
                nacionalidad = new Gson().fromJson(json, FcCatalog.class).getNacionalidad();
                break;
            case solicitud:
                json = "{\"solicitud\":[" + nodeData + "]}";
                solicitud = new Gson().fromJson(json, FcCatalog.class).getSolicitud();
                break;
            case lugarNacimiento:
                json = "{\"lugarNacimiento\":[" + nodeData + "]}";
                estados = new Gson().fromJson(json, FcCatalog.class).getEstados();
                break;
            case respuesta:
                json = "{\"respuesta\":" + nodeData + "}";
                respuesta = new Gson().fromJson(json, FcCatalog.class).getRespuesta();
                break;
            case gradoEstudios:
                json = "{\"gradoEstudios\":[" + nodeData + "]}";
                gradoEstudios = new Gson().fromJson(json, FcCatalog.class).getGradoEstudios();
                break;
            case estatusSolicitud:
                json = "{\"estatusSolicitud\":[" + nodeData + "]}";
                estatusSolicitud = new Gson().fromJson(json, FcCatalog.class).getEstatusSolicitud();
                break;
            case vivienda:
                json = "{\"vivienda\":[" + nodeData + "]}";
                vivienda = new Gson().fromJson(json, FcCatalog.class).getVivienda();
                break;
            case giroNegocio:
                json = "{\"giroNegocio\":[" + nodeData + "]}";
                giroNegocio = new Gson().fromJson(json, FcCatalog.class).getGiroNegocio();
                break;
            case sector:
                json = "{\"sector\":[" + nodeData + "]}";
                sector = new Gson().fromJson(json, FcCatalog.class).getSector();
                break;
            case municipio:
                json = "{\"municipio\":[" + nodeData + "]}";
                municipio = new Gson().fromJson(json, FcCatalog.class).getMunicipio();
                break;
            case puesto:
                json = "{\"puesto\":[" + nodeData + "]}";
                puesto = new Gson().fromJson(json, FcCatalog.class).getPuesto();
                break;
            case cliente_premium:
                json = "{\"cliente_premium\":[" + nodeData + "]}";
                clientePremium = new Gson().fromJson(json, FcCatalog.class).getClientePremium();
                break;
            case afiliados:
                json = "{\"afiliados\":[" + nodeData + "]}";
                afiliados = new Gson().fromJson(json, FcCatalog.class).getAfiliados();
                break;
            case localidad:
                json = "{\"localidad\":[" + nodeData + "]}";
                localidad = new Gson().fromJson(json, FcCatalog.class).getLocalidad();
                break;
            case actividadGeneral:
                json = "{\"actividadGeneral\":[" + nodeData + "]}";
                actividadGeneral = new Gson().fromJson(json, FcCatalog.class).getActividadGeneral();
                break;
            case actividadEconomica:
                json = "{\"actividadEconomica\":[" + nodeData + "]}";
                actividadEconomica = new Gson().fromJson(json, FcCatalog.class).getActividadEconomica();
                break;
            case horarios:
                json = "{\"horarios\":[" + nodeData + "]}";
                horarios = new Gson().fromJson(json, FcCatalog.class).getHorarios();
                break;
            case parentesco:
                json = "{\"parentesco\":[" + nodeData + "]}";
                parentesco = new Gson().fromJson(json, FcCatalog.class).getParentesco();
                break;
            case destino:
                json = "{\"destino\":[" + nodeData + "]}";
                destinos = new Gson().fromJson(json, FcCatalog.class).getDestinos();
                break;
            case banco:
                json = "{\"banco\":[" + nodeData + "]}";
                banco = new Gson().fromJson(json, FcCatalog.class).getBanco();
                break;
            case none:
                break;
        }

    }

    public Globals.CATFC getType(String name) {

        for (Globals.CATFC type : Globals.CATFC.values()) {
            if(name.equals(type.name()))
                return type;
        }

        return Globals.CATFC.none;
    }

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

    public List<LugarNacimiento> getEstados() {
        return estados;
    }

    public void setEstados(List<LugarNacimiento> estados) {
        this.estados = estados;
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

    public List<ModelItem> getDestinos() {
        return destinos;
    }

    public void setDestinos(List<ModelItem> destinos) {
        this.destinos = destinos;
    }

    public List<ModelItem> getActividadGeneral() {
        return actividadGeneral;
    }

    public void setActividadGeneral(List<ModelItem> actividadGeneral) {
        this.actividadGeneral = actividadGeneral;
    }

    public List<ModelItem> getLocalidad() {
        return localidad;
    }

    public void setLocalidad(List<ModelItem> localidad) {
        this.localidad = localidad;
    }

    public List<ModelItem> getActividadEconomica() {
        return actividadEconomica;
    }

    public void setActividadEconomica(List<ModelItem> actividadEconomica) {
        this.actividadEconomica = actividadEconomica;
    }

    public List<ModelItem> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<ModelItem> horarios) {
        this.horarios = horarios;
    }

    public List<Perentesco> getParentesco() {
        return parentesco;
    }

    public void setParentesco(List<Perentesco> parentesco) {
        this.parentesco = parentesco;
    }

    public List<Banco> getBanco() {
        return banco;
    }

    public void setBanco(List<Banco> banco) {
        this.banco = banco;
    }
}
