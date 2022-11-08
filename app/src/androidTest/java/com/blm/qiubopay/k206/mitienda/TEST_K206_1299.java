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
public class TEST_K206_1299 extends HBase {

    

    @Test
    public void TEST_K206_1299() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickTextElement(new HItem("Mi Tienda",1, true));

        HTest.clickTextElement(new HItem("Fiado",3));

        HTest.clickTextElement(new HItem("Nuevo fiado",3));

        HTest.clickTextElement(new HItem("Nuevo cliente",3));

        HTest.setValueInput(new HItem("Nombre", "",0));

        HTest.finish();
    }

}
