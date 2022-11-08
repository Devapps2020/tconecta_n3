package com.blm.qiubopay.k206.pagoservicios;

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
public class TEST_K206_1064 extends HBase {



    @Test
    public void TEST_K206_1064() {

        HTest.start(this.getClass());


        HTest.setValueInput(new HItem("Correo electrónico", DATA.USUARIO.CORREO_ELECTRONICO, 0));

        HTest.setValueInput(new HItem("Contraseña", DATA.USUARIO.CONTRASENA, 1));

        HTest.clickButton(new HItem("ACCEDER",0));

        HTest.checkLoading();

        HTest.clickTextElement(new HItem("Pago de Servicios",3));

        HTest.clickTextElement(new HItem("Naturgy",1, true));


        HTest.setValueInput(new HItem("Naturgy 1 (14 dígitos)", DATA.SERVICIOS.NUMERO_NATURGY1, 0));

        HTest.setValueInput(new HItem("Naturgy 2 (14 dígitos)", DATA.SERVICIOS.NUMERO_NATURGY2, 1));

        HTest.finish();
    }

}
