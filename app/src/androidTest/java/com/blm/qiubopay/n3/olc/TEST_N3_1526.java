package com.blm.qiubopay.n3.olc;

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
public class TEST_N3_1526 extends HBase {

    @Test
    public void TEST_N3_1526() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickTextElement(new HItem("Abonar Saldo",3));

        HTest.clickTextElement(new HItem("Línea de crédito",3));

        HTest.clickButton(new HItem("Consulta adeudo de la línea de crédito",2));
        
        HTest.clickButton(new HItem("CONSULTAR",1));

        HTest.checkLoading();

        HTest.finish();
    }

}
