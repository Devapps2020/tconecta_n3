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
public class TEST_N3_301 extends HBase {

    @Test
    public void TEST_N3_298() {

        HTest.start(this.getClass());

        HTest.setValueInput(new HItem("Dirección de email", DATA.USUARIO.CORREO_ELECTRONICO, 0));

        HTest.setValueInput(new HItem("Contraseña", DATA.CONTRASENA_TEMPORAL, 1));

        HTest.clickButton(new HItem("ACCEDER",0));

        HTest.checkLoading();

        HTest.setValueInput(new HItem("Contraseña", DATA.USUARIO.CONTRASENA, 0));

        HTest.setValueInput(new HItem("Confirma contraseña", DATA.USUARIO.CONTRASENA, 1));

        HTest.clickButton(new HItem("CAMBIAR CONTRASEÑA",0));

        HTest.checkLoading();

        HTest.finish();

    }

}
