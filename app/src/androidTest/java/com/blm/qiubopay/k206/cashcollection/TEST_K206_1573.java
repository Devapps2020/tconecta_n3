package com.blm.qiubopay.k206.cashcollection;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.blm.qiubopay.R;
import com.blm.qiubopay.k206.DATA;
import com.blm.qiubopay.utils.HBase;
import com.blm.qiubopay.utils.HItem;
import com.blm.qiubopay.utils.HTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TEST_K206_1573 extends HBase {

    @Test
    public void TEST_K206_1573() {

        HTest.start(this.getClass());

        HTest.clickButton(new HItem("CREAR UNA CUENTA",1));

        HTest.clickButton(new HItem( "CORREO ELECTRÓNICO", 0));

        HTest.setValuePin(new HItem("Correo electrónico", DATA.USUARIO.CORREO_ELECTRONICO,0));

        HTest.setValuePin(new HItem("Confirmar correo electrónico", DATA.USUARIO.CORREO_ELECTRONICO,1));

        HTest.setValuePin(new HItem("Número celular a 10 dígitos", DATA.USUARIO.NUMERO_CELULAR,2));

        HTest.setValuePin(new HItem("Nombre", DATA.USUARIO.NOMBRE,3));

        HTest.setValuePin(new HItem("Apellido paterno", DATA.USUARIO.APELLIDO_PATERNO,4));

        HTest.setValuePin(new HItem("Apellido materno", DATA.USUARIO.APELLIDO_MATERNO,5));

        HTest.setValuePin(new HItem("Código bimbo", DATA.USUARIO.CODIGO_BIMBO,6));

        HTest.clickRadio(new HItem(""+R.id.rd_gb_bimboId,0));

        HTest.clickRadio(new HItem(""+R.id.rad_aceptar_tyc,1));

        HTest.clickButton(new HItem("ENVIAR DATOS"));

        HTest.setValueInput(new HItem("Contraseña", DATA.USUARIO.CONTRASENA,0));

        HTest.setValueInput(new HItem("Confirma contraseña", DATA.USUARIO.CONTRASENA,1));

        HTest.clickButton(new HItem("ENVIAR",0));

        HTest.checkLoading();

        HTest.clickButton(new HItem("ACEPTAR",0));

        HTest.setValuePin(new HItem("****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem("CONFIRMAR",0));

        HTest.checkLoading();

        HTest.setValueInput(new HItem("Correo electrónico", DATA.USUARIO.CORREO_ELECTRONICO,0));

        HTest.setValueInput(new HItem("Contraseña", DATA.USUARIO.CONTRASENA,1));

        HTest.clickButton(new HItem("ACCEDER",0));

        HTest.finish();

    }

}
