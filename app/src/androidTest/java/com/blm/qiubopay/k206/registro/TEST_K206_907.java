package com.blm.qiubopay.k206.registro;

import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.blm.qiubopay.k206.DATA;
import com.blm.qiubopay.utils.HBase;
import com.blm.qiubopay.utils.HItem;
import com.blm.qiubopay.utils.HTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TEST_K206_907 extends HBase {

    @Test
    public void TEST_K206_907() {

        HTest.start(this.getClass());

        HTest.setValueInput(new HItem("Correo electrónico", DATA.USUARIO.CORREO_ELECTRONICO,0));

        HTest.finish();

    }
}
