package com.blm.qiubopay.k206.fincomun;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.blm.qiubopay.k206.DATA;
import com.blm.qiubopay.utils.HBase;
import com.blm.qiubopay.utils.HItem;
import com.blm.qiubopay.utils.HTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TEST_K206_1348 extends HBase {

    @Test
    public void TEST_K206_1348() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.checkLoading();

        HTest.clickTextElement(new HItem("Prestámo personal",3));

        HTest.clickButton(new HItem("Consúltalo sin compromiso",0));

        //slider

        HTest.clickButton(new HItem("Continuar sin compromiso",0));

        HTest.clickButton(new HItem("Pedir Prestámo",0));

        //camera

        //check_edoCuenta

        HTest.setValueInput(new HItem("N° de celular","",0));

        HTest.setValueInput(new HItem("teléfono adicional","",1));

        HTest.setValueInput(new HItem("Correo electrónico",2));

        HTest.setValueInput(new HItem("Nombre","",3));

        HTest.setValueInput(new HItem("Género","",4));

        HTest.setValueInput(new HItem("Lugar de nacimiento","",5));

        HTest.setValueInput(new HItem("Fecha de nacimiento","",6));

        HTest.setValueInput(new HItem("Calle","",7));

        HTest.setValueInput(new HItem("Número interior","",8));

        HTest.setValueInput(new HItem("Número exterior","",9));

        HTest.setValueInput(new HItem("Entidad federativa/Estado","",10));

        HTest.setValueInput(new HItem("Alcaldía/Municipio","",11));

        HTest.setValueInput(new HItem("Ciudad/Población","",12));

        HTest.setValueInput(new HItem("Código Postal","",13));

        HTest.setValueInput(new HItem("Colonia","",14));

        HTest.setValueInput(new HItem("País","",15));

        HTest.setValueInput(new HItem("Nombre del negocio","",16));

        HTest.setValueInput(new HItem("Apellido Paterno","",17));

        HTest.setValueInput(new HItem("Apellido Materno","",18));

        HTest.setValueInput(new HItem("CURP","",19));

        HTest.setValueInput(new HItem("RFC","",20));

        HTest.clickButton(new HItem("Continuar",0));

        HTest.setValueInput(new HItem("Nombre","",0));

        HTest.setValueInput(new HItem("Apellido Paterno","",1));

        HTest.setValueInput(new HItem("Apellido Materno","",2));

        HTest.setValueInput(new HItem("Género","",3));

        HTest.setValueInput(new HItem("Fecha de nacimiento","",4));

        HTest.setValueInput(new HItem("Parentesco con el titular","",5));

        HTest.setValueInput(new HItem("Calle","",6));

        HTest.setValueInput(new HItem("Número interior","",7));

        HTest.setValueInput(new HItem("Número exterior","",8));

        HTest.setValueInput(new HItem("Entidad federativa/Estado","",9));

        HTest.setValueInput(new HItem("Alcaldía/Municipio","",10));

        HTest.setValueInput(new HItem("Ciudad/Población","",11));

        HTest.setValueInput(new HItem("Código Postal","",12));

        HTest.setValueInput(new HItem("Colonia","",13));

        HTest.setValueInput(new HItem("País","",14));

        HTest.clickButton(new HItem("Aceptar",0));

        HTest.setValueInput(new HItem("Token","",0));

        HTest.finish();

    }

}
