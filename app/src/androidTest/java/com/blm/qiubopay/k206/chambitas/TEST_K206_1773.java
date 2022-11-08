package com.blm.qiubopay.k206.chambitas;

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
public class TEST_K206_1773 extends HBase {

    @Test
    public void TEST_K206_1773() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickTextElement(new HItem("¡Responde y Gana!",3));

        HTest.clickTextElement(new HItem("Chambitas Nuevas",3));

        HTest.clickTextElement(new HItem(DATA.CHAMBITAS.OPCION_3));

        HTest.clickButton(new HItem("CONTINUAR",4));

        HTest.clickTextElement(new HItem(DATA.CHAMBITAS.RETO_5));

        HTest.setValueInput(new HItem("",DATA.CHAMBITAS.RESPUESTA_5_MAYOR,0));

        HTest.clickButton(new HItem("ACEPTAR",0));

        HTest.finish();

    }

}