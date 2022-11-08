package com.blm.qiubopay.k206.login;

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
public class TEST_K206_1231 extends HBase {

    @Test
    public void TEST_K206_1231() {

        HTest.start(this.getClass());

        HTest.setValueInput(new HItem("Correo electrónico", DATA.USUARIO.CORREO_ELECTRONICO, 0));

        HTest.clickText(new HItem("He olvidado mi contraseña",2));

        HTest.setValueInput(new HItem("Correo electrónico", DATA.USUARIO.CORREO_ELECTRONICO, 0));

        HTest.setValueInput(new HItem("Número celular a 10 dígitos", DATA.USUARIO.NUMERO_CELULAR, 1));

        HTest.clickButton(new HItem("CONFIRMAR",0));

        HTest.finish();

    }}
