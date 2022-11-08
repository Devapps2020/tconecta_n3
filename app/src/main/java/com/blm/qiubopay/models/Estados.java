package com.blm.qiubopay.models;

import com.blm.qiubopay.models.sepomex.StateInfo;

import java.util.ArrayList;
import java.util.List;

public class Estados {

    private ArrayList<StateInfo> listEstados;

    public Estados() {
        listEstados = new ArrayList<StateInfo>();

        listEstados.add(new StateInfo("Aguascalientes"        ,"01"));
        listEstados.add(new StateInfo("Baja California"       ,"02"));
        listEstados.add(new StateInfo("Baja California Sur"   ,"03"));
        listEstados.add(new StateInfo("Campeche"              ,"04"));
        listEstados.add(new StateInfo("Chiapas"               ,"05"));
        listEstados.add(new StateInfo("Chihuahua"             ,"06"));
        listEstados.add(new StateInfo("Coahuila"              ,"07"));
        listEstados.add(new StateInfo("Colima"                ,"08"));
        listEstados.add(new StateInfo("Ciudad de Mexico"      ,"09"));
        listEstados.add(new StateInfo("Durango"               ,"10"));
        listEstados.add(new StateInfo("Guanajuato"            ,"11"));
        listEstados.add(new StateInfo("Guerrero"              ,"12"));
        listEstados.add(new StateInfo("Hidalgo"               ,"13"));
        listEstados.add(new StateInfo("Jalisco"               ,"14"));
        listEstados.add(new StateInfo("Mexico"                ,"15"));
        listEstados.add(new StateInfo("Michoacan"             ,"16"));
        listEstados.add(new StateInfo("Morelos"               ,"17"));
        listEstados.add(new StateInfo("Nayarit"               ,"18"));
        listEstados.add(new StateInfo("Nuevo Leon"            ,"19"));
        listEstados.add(new StateInfo("Oaxaca"                ,"20"));
        listEstados.add(new StateInfo("Puebla"                ,"21"));
        listEstados.add(new StateInfo("Queretaro"             ,"22"));
        listEstados.add(new StateInfo("Quintana Roo"          ,"23"));
        listEstados.add(new StateInfo("San Luis Potosi"       ,"24"));
        listEstados.add(new StateInfo("Sinaloa"               ,"25"));
        listEstados.add(new StateInfo("Sonora"                ,"26"));
        listEstados.add(new StateInfo("Tabasco"               ,"27"));
        listEstados.add(new StateInfo("Tamaulipas"            ,"28"));
        listEstados.add(new StateInfo("Tlaxcala"              ,"29"));
        listEstados.add(new StateInfo("Veracruz"              ,"30"));
        listEstados.add(new StateInfo("Yucatan"               ,"31"));
        listEstados.add(new StateInfo("Zacatecas"             ,"32"));
    }

    public List<StateInfo> getListEstados() {
        return listEstados;
    }
}
