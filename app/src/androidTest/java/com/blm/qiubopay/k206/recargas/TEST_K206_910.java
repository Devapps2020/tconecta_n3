package com.blm.qiubopay.k206.recargas;

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
public class TEST_K206_910 extends HBase {

    @Test
    public void TEST_K206_910() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.checkLoading();

        HTest.clickTextElement(new HItem("Venta Tiempo Aire",3));

        HTest.clickTextElement(new HItem("Llamadas Telcel",1, true));

        HTest.clickTextElement(new HItem("$10",3));

        HTest.finish();
    }

}
