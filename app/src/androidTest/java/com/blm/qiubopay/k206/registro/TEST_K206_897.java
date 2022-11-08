package com.blm.qiubopay.k206.registro;

import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.blm.qiubopay.R;
import com.blm.qiubopay.k206.DATA;
import com.blm.qiubopay.utils.HBase;
import com.blm.qiubopay.utils.HItem;
import com.blm.qiubopay.utils.HTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TEST_K206_897 extends HBase {

    @Test
    public void TEST_K206_897() {

        HTest.start(this.getClass());

        HTest.clickButton(new HItem("CREAR UNA CUENTA",3));

        HTest.clickButton(new HItem("Correo electrónico",4));

        HTest.setValueInput(new HItem("Correo electrónico", DATA.USUARIO.CORREO_ELECTRONICO, 2));

        HTest.setValueInput(new HItem("Confirmar correo electrónico", DATA.USUARIO.CORREO_ELECTRONICO, 3));

        HTest.setValueInput(new HItem("Número celular a 10 dígitos", DATA.USUARIO.NUMERO_CELULAR, 4));

        HTest.setValueInput(new HItem("Nombre", DATA.USUARIO.NOMBRE, 5));

        HTest.setValueInput(new HItem("Apellido paterno", DATA.USUARIO.APELLIDO_PATERNO, 6));

        HTest.setValueInput(new HItem("Apellido materno", DATA.USUARIO.APELLIDO_MATERNO, 7));
        
        HTest.clickRadio(new HItem(R.id.rad_aceptar_tyc+"",1));

        HTest.clickButton(new HItem("ENVIAR DATOS",0));

        HTest.clickButton(new HItem("ACEPTAR",5));

        HTest.finish();

    }
}
