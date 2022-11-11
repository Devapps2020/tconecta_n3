package com.blm.qiubopay.n3.registro;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.blm.qiubopay.R;
import com.blm.qiubopay.n3.DATA;
import com.blm.qiubopay.utils.HBase;
import com.blm.qiubopay.utils.HItem;
import com.blm.qiubopay.utils.HTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TEST_N3_338 extends HBase {

    @Test
    public void TEST_N3_338() {

        HTest.start(this.getClass());

        HTest.clickButton(new HItem("CREAR UNA CUENTA",3));

        HTest.clickButton(new HItem("Correo electrónico",6));

        HTest.setValueInput(new HItem("Dirección de email", DATA.USUARIO.CORREO_ELECTRONICO, 2));

        HTest.setValueInput(new HItem("Confirmar dirección de correo electronico", DATA.USUARIO.CORREO_ELECTRONICO, 3));

        HTest.setValueInput(new HItem("Número celular", DATA.USUARIO.NUMERO_CELULAR, 12));

        HTest.setValueInput(new HItem("Nombre", DATA.USUARIO.NOMBRE, 13));

        HTest.setValueInput(new HItem("Primer apellido", DATA.USUARIO.APELLIDO_PATERNO, 14));

        HTest.setValueInput(new HItem("Segundo apellido", DATA.USUARIO.APELLIDO_MATERNO, 15));

        HTest.setValueSpinner(new HItem("Canal", DATA.USUARIO.CANAL,8));

        HTest.setValueInput(new HItem("Número de colaborador", DATA.USUARIO.COLABORADOR, 17));

        HTest.setValueInput(new HItem("Código Bimbo", DATA.USUARIO.CODIGO_BIMBO, 19));

        HTest.clickRadio(new HItem(R.id.rad_aceptar_tyc + "",2));

        HTest.clickButton(new HItem("ENVIAR DATOS",0));

        HTest.clickButton(new HItem("ACEPTAR",4));

        HTest.setValueInput(new HItem("Nombre del negocio", DATA.NEGOCIO.NOMBRE,27));

        HTest.setValueInput(new HItem("Calle", DATA.NEGOCIO.CALLE,20));

        HTest.setValueInput(new HItem("Exterior #", DATA.NEGOCIO.EXTERIOR,31));

        HTest.setValueInput(new HItem("Código postal", DATA.NEGOCIO.CODIGO_POSTAL,21));

        HTest.setValueSpinner(new HItem("Estado", DATA.NEGOCIO.ESTADO,22));

        HTest.setValueInput(new HItem("Municipio", DATA.NEGOCIO.MUNICIPIO,23));

        HTest.setValueInput(new HItem("Población", DATA.NEGOCIO.POBLACION,24));

        HTest.clickButton(new HItem("Continuar",13));

        HTest.setValueInput(new HItem("Contraseña", DATA.USUARIO.CONTRASENA,0));

        HTest.finish();

    }

}
