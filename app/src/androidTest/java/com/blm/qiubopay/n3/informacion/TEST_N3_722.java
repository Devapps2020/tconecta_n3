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
public class TEST_N3_722 extends HBase {



    @Test
    public void TEST_N3_722() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickLinearLayout(new HItem(R.id.btn_user + "",22));

        HTest.clickTextElement(new HItem("Compra de Material"));

        HTest.setValueESpinner(new HItem("Elige un producto de la lista","Básico Oro", 0));

        HTest.clickButton(new HItem( "COMPRAR CON MI SALDO", 1));

        HTest.clickButton(new HItem( "SI", 3));

        HTest.setValueInput(new HItem("Entre calles",DATA.USUARIO.ENTRE_CALLES,8));

        HTest.setValueInput(new HItem("Referencias",DATA.USUARIO.REFERENCIAS,9));

        HTest.clickButton(new HItem("CONTINUAR",1));


        HTest.finish();
    }

}
