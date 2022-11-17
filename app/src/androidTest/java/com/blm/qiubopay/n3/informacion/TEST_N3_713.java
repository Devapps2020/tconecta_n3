package com.blm.qiubopay.n3.informacion;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.blm.qiubopay.R;
import com.blm.qiubopay.n3.DATA;
import com.blm.qiubopay.utils.HBase;
import com.blm.qiubopay.utils.HItem;
import com.blm.qiubopay.utils.HTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TEST_N3_713 extends HBase {



    @Test
    public void TEST_N3_713() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickLinearLayout(new HItem(R.id.btn_user + "",22));

        HTest.clickTextElement(new HItem("Compra de Rollos"));

        HTest.setValueESpinner(new HItem("Productos disponibles","Compra paquete rollos 30", 0));

        HTest.clickButton(new HItem("COMPRAR CON MI SALDO",0));

        HTest.clickButton(new HItem("SI",0));

        HTest.setValueInput(new HItem("Entre calles",DATA.USUARIO.ENTRE_CALLES));

        HTest.setValueInput(new HItem("Entre calles",DATA.USUARIO.REFERENCIAS));

        HTest.clickButton(new HItem("CONTINUAR",0));

        HTest.finish();
    }

}
