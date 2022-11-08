package com.blm.qiubopay.k206.fincomun;

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
public class TEST_K206_1339 extends HBase {

    @Test
    public void TEST_K206_1339() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.checkLoading();

        HTest.clickTextElement(new HItem("Prestámo personal",3));

        HTest.clickButton(new HItem("Consúltalo sin compromiso",0));

        //slider

        HTest.clickButton(new HItem("Continuar sin compromiso",0));

        HTest.finish();

    }

}
