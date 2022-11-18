package com.blm.qiubopay.n3.login;

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
public class TEST_N3_299 extends HBase {

    @Test
    public void TEST_N3_299() {

        HTest.start(this.getClass());

        HTest.setValueInput(new HItem("Dirección de email", DATA.USUARIO.CORREO_ELECTRONICO, 0));

        HTest.clickText(new HItem("He olvidado mi contraseña",3));

        HTest.setValueInput(new HItem("Dirección de email", DATA.USUARIO.CORREO_ELECTRONICO, 0));

        HTest.setValueInput(new HItem("Número celular", DATA.USUARIO.NUMERO_CELULAR, 1));

        HTest.clickButton(new HItem("CONFIRMAR",0));

        HTest.checkLoading();

        HTest.finish();

    }

}
