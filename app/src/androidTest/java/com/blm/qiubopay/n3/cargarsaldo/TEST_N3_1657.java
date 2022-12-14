package com.blm.qiubopay.n3.cargarsaldo;

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
public class TEST_N3_1657 extends HBase {

    @Test
    public void TEST_N3_1657() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickTextElement(new HItem("Abonar Saldo",3));

        HTest.clickTextElement(new HItem("Depósito Bancario",3));

        HTest.clickTextElement(new HItem("Santander",1, true));

        HTest.setValueInput(new HItem("Monto", DATA.DEPOSITO.SANTANDER.MONTO, 0));

        HTest.setValueInput(new HItem("Referencia", DATA.DEPOSITO.SANTANDER.REFERENCIA, 1));

        HTest.setValueInputDate(new HItem("Fecha transacción", DATA.DEPOSITO.SANTANDER.FECHA_TRANSACCION, 3));

        HTest.clickButton(new HItem("CONFIRMAR",1));

        HTest.checkLoading();

        HTest.finish();

    }

}
