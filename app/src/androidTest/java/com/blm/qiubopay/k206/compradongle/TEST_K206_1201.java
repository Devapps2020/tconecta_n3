package com.blm.qiubopay.k206.compradongle;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.blm.qiubopay.k206.DATA;
import com.blm.qiubopay.utils.HBase;
import com.blm.qiubopay.utils.HItem;
import com.blm.qiubopay.utils.HTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TEST_K206_1201 extends HBase {

    @Test
    public void TEST_K206_1201() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickTextElement(new HItem("Adquiere tu\nLector de Tarjetas",3, true));

        HTest.checkLoading();

        HTest.clickButton(new HItem("COMPRAR",1));

        HTest.clickButton(new HItem("SI",4));

        HTest.checkLoading();

        HTest.setValueInput(new HItem("Calle", DATA.USUARIO.CALLE, 0));

        HTest.setValueInput(new HItem("Entre calles", DATA.USUARIO.ENTRE_CALLES, 8));

        HTest.setValueInput(new HItem("Referencias", DATA.USUARIO.REFERENCIAS, 9));

        HTest.setValueInput(new HItem("Exterior #", DATA.USUARIO.EXTERIOR, 10));

        HTest.setValueInput(new HItem("Código postal", DATA.USUARIO.CODIGO_POSTAL, 1));

        HTest.setValueSpinner(new HItem("Estado", DATA.USUARIO.ESTADO,2));

        HTest.setValueInput(new HItem("Municipio", DATA.USUARIO.MUNICIPIO, 3));

        HTest.setValueInput(new HItem("Población", DATA.USUARIO.POBLACION, 4));

        HTest.finish();
    }

}
