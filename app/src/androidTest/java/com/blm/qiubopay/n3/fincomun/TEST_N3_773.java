package com.blm.qiubopay.n3.fincomun;

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
public class TEST_N3_773 extends HBase {

    @Test
    public void TEST_N3_773() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.checkLoading();

        HTest.scrollRecycler(new HItem(8));

        HTest.clickTextElement(new HItem("Préstamo personal"));

        HTest.clickButton(new HItem("Consúltalo sin compromiso",0));

        //slider

        HTest.clickButton(new HItem("Continuar sin compromiso",0));

        HTest.finish();

    }

}
