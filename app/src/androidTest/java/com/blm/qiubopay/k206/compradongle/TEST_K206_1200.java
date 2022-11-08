package com.blm.qiubopay.k206.compradongle;

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
public class TEST_K206_1200 extends HBase {

    @Test
    public void TEST_K206_1200() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickTextElement(new HItem("Adquiere tu\nLector de Tarjetas",3, true));

        HTest.checkLoading();

        HTest.clickButton(new HItem("COMPRAR",1));

        HTest.clickButton(new HItem("SI",4));

        HTest.finish();
    }

}
