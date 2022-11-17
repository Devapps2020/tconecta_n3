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
public class TEST_N3_332 extends HBase {

    @Test
    public void TEST_N3_332() {

        HTest.start(this.getClass());

        HTest.clickButton(new HItem("CREAR UNA CUENTA",3));

        HTest.clickButton(new HItem("Correo electrónico",6));

        HTest.setValueInput(new HItem("Dirección de email", DATA.USUARIO.CORREO_ELECTRONICO, 2));

        HTest.setValueInput(new HItem("Confirmar dirección de correo electronico", DATA.USUARIO.CORREO_ELECTRONICO, 3));

        HTest.setValueInput(new HItem("Número celular", DATA.USUARIO.NUMERO_CELULAR, 12));

        HTest.setValueInput(new HItem("Nombre", DATA.USUARIO.NOMBRE, 13));

        HTest.setValueInput(new HItem("Primer apellido", DATA.USUARIO.APELLIDO_PATERNO, 14));

        HTest.setValueInput(new HItem("Segundo apellido", DATA.USUARIO.APELLIDO_MATERNO, 15));

        HTest.setValueSpinner(new HItem("Canal", DATA.USUARIO.CANAL,16));

        HTest.setValueInput(new HItem("Número de colaborador", DATA.USUARIO.COLABORADOR, 17));

        HTest.setValueInput(new HItem("Código Bimbo", DATA.USUARIO.CODIGO_BIMBO, 19));

        HTest.clickRadio(new HItem(R.id.rad_aceptar_tyc + "",3));

        HTest.clickButton(new HItem("ENVIAR DATOS",1));

        HTest.finish();

    }

}
