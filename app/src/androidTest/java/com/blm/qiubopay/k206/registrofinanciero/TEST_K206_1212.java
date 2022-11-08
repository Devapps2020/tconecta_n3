package com.blm.qiubopay.k206.registrofinanciero;


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
public class TEST_K206_1212 extends HBase {

    @Test
    public void TEST_K206_1212() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickTextElement(new HItem("Cobrar con Tarjeta",3));

        HTest.clickTextElement(new HItem("Cobro con tarjeta",3));

        HTest.clickButton(new HItem("YA TENGO UNO",4));

        HTest.setValueInput(new HItem("CLABE",DATA.COBROT.CLABE,0));

        HTest.checkLoading();

        HTest.clickTextElement(new HItem("Fotografía estado de cuenta", 1, true));

        HTest.clickButton(new HItem("TOMAR FOTO",0));

        HTest.pressBack();

        HTest.clickTextElement(new HItem("Fotografía comprobante domicilio", 1, true));

        HTest.clickButton(new HItem("TOMAR FOTO",0));

        HTest.pressBack();

        HTest.clickButton(new HItem("ENVIAR DATOS",0));

        HTest.finish();

    }

}