
package com.blm.qiubopay.models.fincomun;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModCredito {

    @SerializedName("id_mod_credito")
    @Expose
    private Integer idModCredito;
    @SerializedName("nombre_mod_credito")
    @Expose
    private String nombreModCredito;

    public Integer getIdModCredito() {
        return idModCredito;
    }

    public void setIdModCredito(Integer idModCredito) {
        this.idModCredito = idModCredito;
    }

    public String getNombreModCredito() {
        return nombreModCredito;
    }

    public void setNombreModCredito(String nombreModCredito) {
        this.nombreModCredito = nombreModCredito;
    }

}
