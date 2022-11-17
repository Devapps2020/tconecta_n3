package com.blm.qiubopay.n3.registro;

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
public class TEST_N3_306 extends HBase {

    @Test
    public void TEST_N3_306() {

        HTest.start(this.getClass());

        HTest.clickButton(new HItem("CREAR UNA CUENTA",3));

        HTest.setValueInput(new HItem("Nombre", DATA.USUARIO.NOMBRE, 5));

        HTest.finish();

    }

}
