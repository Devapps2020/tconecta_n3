package com.blm.qiubopay.k206.mitienda;

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
public class TEST_K206_1307 extends HBase {

    @Test
    public void TEST_K206_1307() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickTextElement(new HItem("Mi Tienda",1, true));

        HTest.clickTextElement(new HItem("Fiado",3));

        HTest.clickTextElement(new HItem("Clientes con adeudos",3));

        HTest.checkLoading();

        HTest.setValueInput(new HItem("Buscar","ab",0));

        HTest.finish();
    }

}
