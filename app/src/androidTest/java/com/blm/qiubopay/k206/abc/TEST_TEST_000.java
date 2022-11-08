package com.blm.qiubopay.k206.abc;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.blm.qiubopay.utils.HBase;
import com.blm.qiubopay.utils.HItem;
import com.blm.qiubopay.utils.HTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TEST_TEST_000 extends HBase {

    @Test
    public void TEST_01() {

        HTest.start(this.getClass());

        /**
         * Tomar fotos ejemplo
         * ORC
         * QR
         * BARRAS
         */
        HTest.clickButton(new HItem("TOMAR FOTO",0));

        HTest.finish();

    }

}
