package com.blm.qiubopay.n3.pagoservicios;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.blm.qiubopay.n3.DATA;
import com.blm.qiubopay.utils.HBase;
import com.blm.qiubopay.utils.HItem;
import com.blm.qiubopay.utils.HTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TEST_N3_130 extends HBase {
    
    @Test
    public void TEST_N3_130() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickTextElement(new HItem("Pago de Servicios",3));

        HTest.clickTextElement(new HItem("VeTV",1, true));

        HTest.setValueInput(new HItem("Número de referencia",DATA.SERVICIOS.NUMERO_REFERENCIA_VETV));

        HTest.clickButton(new HItem("CONTINUAR",1));

        HTest.setValueInput(new HItem("Monto",DATA.SERVICIOS.MONTO_VETV,0));

        HTest.clickButton(new HItem("CONFIRMAR",2));

        HTest.clickButton(new HItem("REALIZAR PAGO",3));

        HTest.finish();
    }

}