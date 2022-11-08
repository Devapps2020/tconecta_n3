
package com.blm.qiubopay.models.fincomun;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Respuesta {

    @SerializedName("codigo")
    @Expose
    private Integer codigo;
    @SerializedName("descripcion")
    @Expose
    private List<String> descripcion = null;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public List<String> getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(List<String> descripcion) {
        this.descripcion = descripcion;
    }

}
