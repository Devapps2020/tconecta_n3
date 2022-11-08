package com.blm.qiubopay.k206.registro;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.blm.qiubopay.utils.HBase;
import com.blm.qiubopay.utils.HItem;
import com.blm.qiubopay.utils.HTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TEST_K206_893 extends HBase {

    @Test
    public void TEST_K206_893() {

        HTest.start(this.getClass());

        HTest.clickButton(new HItem("CREAR UNA CUENTA",3));

        HTest.finish();

    }

}
