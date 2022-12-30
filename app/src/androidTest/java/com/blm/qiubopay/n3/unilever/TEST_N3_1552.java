package com.blm.qiubopay.n3.unilever;

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
public class TEST_N3_1552 extends HBase {

    @Test
    public void TEST_N3_1552() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.scrollRecycler(new HItem(7));

        HTest.clickTextElement(new HItem("Haz tu Pedido",3));

        HTest.checkLoading();

        HTest.clickTextElement(new HItem("Tía Rosa"));

        HTest.clickButton(new HItem("Añadir",5));

        HTest.clickTextElement(new HItem("+"));

        HTest.clickButton(new HItem("Confirmar pedido",4));

        HTest.clickButton(new HItem("Enviar pedido",5));

        HTest.checkLoading();

        HTest.clickButton(new HItem("Enviar pedido",3));

        HTest.checkLoading();

        HTest.finish();

    }

}
