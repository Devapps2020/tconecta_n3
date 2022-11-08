package com.blm.qiubopay.k206.mitienda;

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
public class TEST_K206_1695 extends HBase {

    @Test
    public void TEST_K206_1695() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickTextElement(new HItem("Abonar Saldo",3));

        HTest.clickTextElement(new HItem("Cargo a tarjetas",3));

        HTest.setValueInput(new HItem("Monto", DATA.DEPOSITO.TARJETA.MONTO, 0));

        HTest.clickButton(new HItem("CONFIRMAR",1));

        HTest.checkLoading();

        HTest.setValueInput(new HItem("Nombre en tarjeta", DATA.DEPOSITO.TARJETA.NOMBRE, 3));

        HTest.setValueInput(new HItem("Número de tarjeta", DATA.DEPOSITO.TARJETA.NUMERO, 4));

        HTest.setValueInput(new HItem("Expiración", DATA.DEPOSITO.TARJETA.EXPIRACION, 5));

        HTest.setValueInput(new HItem("CVV", DATA.DEPOSITO.TARJETA.CVV, 6));

        HTest.finish();
    }

}
