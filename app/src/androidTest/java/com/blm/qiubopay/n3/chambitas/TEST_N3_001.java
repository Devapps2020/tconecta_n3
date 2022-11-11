package com.blm.qiubopay.n3.chambitas;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.blm.qiubopay.n3.DATA;
import com.blm.qiubopay.utils.HBase;
import com.blm.qiubopay.utils.HItem;
import com.blm.qiubopay.utils.HTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TEST_N3_001 extends HBase {

    @Before
    public void beforeTest() {
        HTest.logout();
        HTest.launch(mActivityScenarioRule.getActivity());
    }

    @Test
    public void TEST_N3_001() {

        HTest.start(this.getClass());

        HTest.setValueInput(new HItem("Dirección de email", DATA.USUARIO.CORREO_ELECTRONICO, 0));

        HTest.setValueInput(new HItem("Contraseña", DATA.USUARIO.CONTRASENA, 1));

        HTest.clickButton(new HItem("ACCEDER",0));

        HTest.setValueInput(new HItem("Código PIN", DATA.USUARIO.PIN, 0));

        HTest.setValueInput(new HItem("Confirmar PIN", DATA.USUARIO.PIN, 1));

        HTest.clickButton(new HItem("CONTINUAR",0));

        HTest.checkLoading();

        HTest.finish();
    }

}
