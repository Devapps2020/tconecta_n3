
package com.blm.qiubopay.models.fincomun;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TipoCredito {

    @SerializedName("id_tipo_producto")
    @Expose
    private Integer idTipoProducto;
    @SerializedName("nombre_tipo_credito")
    @Expose
    private String nombreTipoCredito;

    public TipoCredito(){

    }

    public Integer getIdTipoProducto() {
        return idTipoProducto;
    }

    public void setIdTipoProducto(Integer idTipoProducto) {
        this.idTipoProducto = idTipoProducto;
    }

    public String getNombreTipoCredito() {
        return nombreTipoCredito;
    }

    public void setNombreTipoCredito(String nombreTipoCredito) {
        this.nombreTipoCredito = nombreTipoCredito;
    }

}
