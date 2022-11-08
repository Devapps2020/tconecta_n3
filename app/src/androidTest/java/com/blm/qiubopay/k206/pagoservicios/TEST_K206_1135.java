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
public class TEST_K206_1135 extends HBase {



    @Test
    public void TEST_K206_1135() {

        HTest.start(this.getClass());

        HTest.setValueInput(new HItem("Correo electrónico", DATA.USUARIO.CORREO_ELECTRONICO, 0));

        HTest.setValueInput(new HItem("Contraseña", DATA.USUARIO.CONTRASENA, 1));

        HTest.clickButton(new HItem("ACCEDER",0));

        HTest.checkLoading();

        HTest.clickTextElement(new HItem("Pago de Servicios",3));

        HTest.scrollRecycler(new HItem(17));
        
        HTest.clickTextElement(new HItem("Plan AT&T",1, true));

        HTest.setValueInput(new HItem("Número celular",DATA.SERVICIOS.NUMERO_PLAN_ATT,0));

        HTest.clickButton(new HItem("CONTINUAR",1));

        HTest.clickButton(new HItem("CONFIRMAR",2));

        HTest.clickButton(new HItem("REALIZAR PAGO",3));

        HTest.finish();
    }

}
