
package com.blm.qiubopay.models.fincomun;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductoCredito {

    @SerializedName("id_producto_credito")
    @Expose
    private Integer idProductoCredito;
    @SerializedName("nombre_producto")
    @Expose
    private String nombreProducto;

    public Integer getIdProductoCredito() {
        return idProductoCredito;
    }

    public void setIdProductoCredito(Integer idProductoCredito) {
        this.idProductoCredito = idProductoCredito;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

}
