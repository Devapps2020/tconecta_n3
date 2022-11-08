package com.blm.qiubopay.models.bimbo;

import java.util.ArrayList;
import java.util.List;

public class CatProducts {

    private List<String> marcas;

    private List<String> categorias;

    private List<ProductoDTO> productos;

    public CatProducts(List<ProductoDTO> productos) {
        this.productos = productos;
    }

    public void setProductos(List<ProductoDTO> productos) {
        this.productos = productos;
    }

    public List<String> getCategorias(Integer marca) {

        List<String> resusult = new ArrayList();

        for (ProductoDTO pro : productos)
            if(pro.getId_brand().equals(marca + ""))
                if(!resusult.contains(pro.getCategory_name()))
                        resusult.add(pro.getCategory_name());

        return resusult;
    }

    public List<ProductoDTO> getProductos(Integer marca) {

        List<ProductoDTO> resusult = new ArrayList();

        for (ProductoDTO pro : productos)
            if(pro.getId_brand().equals(marca + ""))
                resusult.add(pro);

        return  resusult;
    }

    public List<ProductoDTO> getProductos(Integer marca, String categoria) {

        List<ProductoDTO> resusult = new ArrayList();

        for (ProductoDTO pro : productos)
            if(pro.getId_brand().equals(marca + "") && pro.getCategory_name().toLowerCase().contains(categoria.toLowerCase()))
                resusult.add(pro);

        return  resusult;
    }

    public List<ProductoDTO> getProductos() {
        return  productos;
    }

}
