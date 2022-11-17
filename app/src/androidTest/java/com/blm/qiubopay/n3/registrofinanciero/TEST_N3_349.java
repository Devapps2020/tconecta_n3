package com.blm.qiubopay.n3.registrofinanciero;


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
public class TEST_N3_349 extends HBase {

    @Test
    public void TEST_N3_349() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickTextElement(new HItem("Cobrar con Tarjeta",3));

        HTest.clickTextElement(new HItem("Cobro con tarjeta",3));

        HTest.clickButton(new HItem("YA TENGO UNO",4));

        HTest.setValueInput(new HItem("CLABE","1234567890000000000",0));

        HTest.finish();

    }

}
