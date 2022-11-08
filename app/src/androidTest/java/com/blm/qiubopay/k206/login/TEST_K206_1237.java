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
public class TEST_K206_1237 extends HBase {

    @Test
    public void TEST_K206_1237() {

        HTest.start(this.getClass());

        HTest.setValueInput(new HItem("Correo electrónico", DATA.USUARIO.CORREO_ELECTRONICO, 0));

        HTest.setValueInput(new HItem("Contraseña", DATA.CONTRASENA_CARACTERES, 1));

        HTest.finish();

    }}
