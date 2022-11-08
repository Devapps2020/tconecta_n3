package com.blm.qiubopay.k206.informacion;

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
public class TEST_K206_1254 extends HBase {

    @Test
    public void TEST_K206_1254() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickLinearLayout(new HItem(R.id.btn_user + "",22));

        HTest.clickTextElement(new HItem("Restablecer contraseña"));

        HTest.clickButton(new HItem( "SI", 4));

        HTest.checkLoading();

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 1));

        HTest.setValueInput(new HItem("Contraseña", DATA.USUARIO.CONTRASENA, 0));

        HTest.setValueInput(new HItem("Confirmar contraseña", DATA.USUARIO.CONTRASENA, 1));

        HTest.clickImage(new HItem(R.id.text_input_layout_icon+"",22));

        HTest.clickImage(new HItem(R.id.text_input_layout_icon+"",24));

        HTest.finish();
    }

}
