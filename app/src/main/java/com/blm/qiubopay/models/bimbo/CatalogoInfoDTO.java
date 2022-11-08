package com.blm.qiubopay.models.bimbo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CatalogoInfoDTO {

    private String qpay_seed;

    private String qpay_response;
    private String qpay_code;
    private String qpay_description;

    private CatalogoInfo[] qpay_object;
    private FcCatalog fcCatalog;

    public CatalogoInfoDTO(){

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

    public CatalogoInfo[] getQpay_object() {
        return qpay_object;
    }

    public void setQpay_object(CatalogoInfo[] qpay_object) {
        this.qpay_object = qpay_object;
    }

    public FcCatalog getFcCatalog() {

        if(fcCatalog == null) {
            setCatalogoInfo();
        }

        return fcCatalog;
    }

    private void setCatalogoInfo() {
        fcCatalog = new FcCatalog();

        qpay_object[0].intiData();

        for (FcCatalog cat : qpay_object[0].getFcCatalog()) {

            if(cat.getClientePremium() != null)
                fcCatalog.setClientePremium(cat.getClientePremium());

            if(cat.getTipoCredito() != null)
                fcCatalog.setTipoCredito(cat.getTipoCredito());

            if(cat.getAfiliados() != null)
                fcCatalog.setAfiliados(cat.getAfiliados());

            if(cat.getEstadoCivil() != null)
                fcCatalog.setEstadoCivil(cat.getEstadoCivil());

            if(cat.getEstatusSolicitud() != null)
                fcCatalog.setEstatusSolicitud(cat.getEstatusSolicitud());

            if(cat.getGenero() != null)
                fcCatalog.setGenero(cat.getGenero());

            if(cat.getGiroNegocio() != null)
                fcCatalog.setGiroNegocio(cat.getGiroNegocio());

            if(cat.getGradoEstudios() != null)
                fcCatalog.setGradoEstudios(cat.getGradoEstudios());

            if(cat.getEstados() != null)
                fcCatalog.setEstados(cat.getEstados());

            if(cat.getMatriz() != null)
                fcCatalog.setMatriz(cat.getMatriz());

            if(cat.getModCredito() != null)
                fcCatalog.setModCredito(cat.getModCredito());

            if(cat.getMunicipio() != null)
                fcCatalog.setMunicipio(cat.getMunicipio());

            if(cat.getNacionalidad() != null)
                fcCatalog.setNacionalidad(cat.getNacionalidad());

            if(cat.getOcupacion() != null)
                fcCatalog.setOcupacion(cat.getOcupacion());

            if(cat.getProductoCredito() != null)
                fcCatalog.setProductoCredito(cat.getProductoCredito());

            if(cat.getPuesto() != null)
                fcCatalog.setPuesto(cat.getPuesto());

            if(cat.getSector() != null)
                fcCatalog.setSector(cat.getSector());

            if(cat.getRespuesta() != null)
                fcCatalog.setRespuesta(cat.getRespuesta());

            if(cat.getSolicitud() != null)
                fcCatalog.setSolicitud(cat.getSolicitud());

            if(cat.getVivienda() != null)
                fcCatalog.setVivienda(cat.getVivienda());

            if(cat.getDestinos() != null)
                fcCatalog.setDestinos(cat.getDestinos());

            if(cat.getParentesco() != null)
                fcCatalog.setParentesco(cat.getParentesco());

            if(cat.getLocalidad() != null)
                fcCatalog.setLocalidad(cat.getLocalidad());

            if(cat.getHorarios() != null)
                fcCatalog.setHorarios(cat.getHorarios());

            if(cat.getActividadEconomica() != null)
                fcCatalog.setActividadEconomica(cat.getActividadEconomica());

            if(cat.getActividadGeneral() != null)
                fcCatalog.setActividadGeneral(cat.getActividadGeneral());

            if(cat.getActividadGeneral() != null)
                fcCatalog.setActividadGeneral(cat.getActividadGeneral());

            if(cat.getBanco() != null)
                fcCatalog.setBanco(cat.getBanco());

        }

    }

    public class CatalogoInfo {

        @SerializedName("fc_catalog")
        @Expose
        private List<FcCatalog> fcCatalog;

        public CatalogoInfo() {

        }

        public List<FcCatalog> getFcCatalog() {
            intiData();
            return fcCatalog;
        }

        public void setFcCatalog(List<FcCatalog> fcCatalog) {
            this.fcCatalog = fcCatalog;
        }

        public void intiData() {

            if (fcCatalog != null && !fcCatalog.isEmpty()) {
                for (FcCatalog cat : fcCatalog)
                    cat.parserData();
            }

        }
    }

}
