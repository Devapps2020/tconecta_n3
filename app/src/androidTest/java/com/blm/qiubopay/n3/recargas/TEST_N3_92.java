package com.blm.qiubopay.n3.recargas;

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
public class TEST_N3_92 extends HBase {

    

    @Test
    public void TEST_N3_92() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickTextElement(new HItem("Venta Tiempo Aire",3));

        HTest.scrollRecycler(new HItem(16));

        HTest.clickTextElement(new HItem("Fobo",1, true));

        HTest.checkLoading();

        HTest.clickTextElement(new HItem("$500",3));

        HTest.setValueInput(new HItem("Número de teléfono",DATA.RECARGAS.NUMERO_FOBO,0));

        HTest.setValueInput(new HItem("Confirma número de teléfono",DATA.RECARGAS.NUMERO_FOBO,1));

        HTest.finish();
    }

}