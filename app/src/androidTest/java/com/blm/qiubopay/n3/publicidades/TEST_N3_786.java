package com.blm.qiubopay.n3.publicidades;

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
public class TEST_N3_786 extends HBase {

    @Test
    public void TEST_N3_786() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.scrollRecycler(new HItem(6));

        HTest.clickTextElement(new HItem("Publicidad y Promociones",3));

        HTest.clickTextElement(new HItem("In APP"));

        HTest.clickTextElement(new HItem("Imprimir"));

        HTest.finish();

    }

}
