package com.blm.qiubopay.k206.notificaciones;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.blm.qiubopay.R;
import com.blm.qiubopay.k206.DATA;
import com.blm.qiubopay.utils.HBase;
import com.blm.qiubopay.utils.HItem;
import com.blm.qiubopay.utils.HTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TEST_K206_1713 extends HBase {

    @Test
    public void TEST_K206_1713() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickLinearLayout(new HItem(R.id.btn_user + "",22));

        HTest.clickText(new HItem("NOTIFICACIONES",7));

        HTest.clickImage(new HItem(""+R.id.img_delete));

        HTest.finish();

    }

}
