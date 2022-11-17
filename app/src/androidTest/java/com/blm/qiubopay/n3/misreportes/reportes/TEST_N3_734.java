package com.blm.qiubopay.n3.misreportes.reportes;

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
public class TEST_N3_734 extends HBase {

    @Test
    public void TEST_N3_734() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickTextElement(new HItem("Reportes",1, true));

        HTest.clickTextElement(new HItem("Operaciones con tarjeta"));

        HTest.clickTextElement(new HItem("PLAN MOVISTAR"));

        HTest.clickButton(new HItem("IMPRIMIR",2));


        HTest.finish();
    }

}
