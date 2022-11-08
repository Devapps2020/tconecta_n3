package com.blm.qiubopay.k206.registrofinanciero;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.blm.qiubopay.R;
import com.blm.qiubopay.k206.DATA;
import com.blm.qiubopay.utils.HBase;
import com.blm.qiubopay.utils.HItem;
import com.blm.qiubopay.utils.HTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TEST_K206_1209 extends HBase {

    @Test
    public void TEST_K206_1209() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickTextElement(new HItem("Cobrar con Tarjeta",3));

        HTest.clickTextElement(new HItem("Cobro con tarjeta",3));

        HTest.clickButton(new HItem("ACTIVAR",0));

        HTest.setValueInput(new HItem("CLABE","1234567890000000000",0));

        HTest.clickImage(new HItem(""+R.id.img_add_photo_estado_cuenta));

        HTest.clickButton(new HItem("Continuar",0));

        HTest.finish();

    }

}
