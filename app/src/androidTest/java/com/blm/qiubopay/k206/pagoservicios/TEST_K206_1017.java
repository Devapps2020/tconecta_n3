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
public class TEST_K206_1017 extends HBase {

    

    @Test
    public void TEST_K206_1017() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickTextElement(new HItem("Pago de Servicios",3));

        HTest.clickTextElement(new HItem("Telmex",1,true));

        HTest.setValueInput(new HItem("Número de teléfono",DATA.SERVICIOS.NUMERO_TELMEX,0));

        HTest.finish();
    }

}
