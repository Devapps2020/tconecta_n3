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
public class TEST_N3_204 extends HBase {
    
    @Test
    public void TEST_N3_204() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickTextElement(new HItem("Pago de Servicios",3));

        HTest.scrollRecycler(new HItem(12));

        HTest.clickTextElement(new HItem("Star TV",1, true));

        HTest.setValueInput(new HItem("Número de contrato - 819",DATA.SERVICIOS.NUMERO_STAR_TV,0));

        HTest.clickButton(new HItem("CONTINUAR",1));

        HTest.checkLoading();

        HTest.clickButton(new HItem("CONFIRMAR",2));

        HTest.finish();
    }

}
