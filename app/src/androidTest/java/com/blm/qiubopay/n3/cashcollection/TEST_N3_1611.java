package com.blm.qiubopay.n3.cashcollection;

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
public class TEST_N3_1611 extends HBase {

    @Before
    public void beforeTest() {
        HTest.logout();
        HTest.launch(mActivityScenarioRule.getActivity());
    }

    @Test
    public void TEST_K206_1586() {

        HTest.start(this.getClass());

        HTest.setValueInput(new HItem("Correo electrónico", DATA.USUARIO.CORREO_ELECTRONICO,0));

        HTest.setValueInput(new HItem("Contraseña", DATA.USUARIO.CONTRASENA,1));

        HTest.clickButton(new HItem("ACCEDER",0));

        HTest.clickTextElement(new HItem("Mi Tienda",1, true));

        HTest.clickText(new HItem("Ya soy cliente Bimbo"));

        HTest.setValueInput(new HItem("Código bimbo",0));

        HTest.finish();

    }

}
