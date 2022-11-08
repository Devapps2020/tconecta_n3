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
public class TEST_K206_1763 extends HBase {

    @Test
    public void TEST_K206_1763() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickTextElement(new HItem("¡Responde y Gana!",3));

        HTest.clickTextElement(new HItem("Chambitas Nuevas",3));

        HTest.clickTextElement(new HItem(DATA.CHAMBITAS.OPCION_1));

        HTest.clickButton(new HItem("CONTINUAR",4));

        HTest.clickTextElement(new HItem(DATA.CHAMBITAS.RETO_4,0));

        HTest.clickTextElement(new HItem(DATA.CHAMBITAS.RESPUESTA_4));

        HTest.setValueInput(new HItem("Especifique su respuesta","Chips"));

        HTest.clickButton(new HItem("Aceptar",0));

        HTest.clickTextElement(new HItem(DATA.CHAMBITAS.RETO_3,0));

        HTest.clickTextElement(new HItem(DATA.CHAMBITAS.RESPUESTA_3));

        HTest.clickButton(new HItem("Aceptar",0));

        HTest.clickTextElement(new HItem(DATA.CHAMBITAS.RETO_1,0));

        HTest.setValueInput(new HItem("Respuesta",DATA.CHAMBITAS.RESPUESTA_1));

        HTest.clickButton(new HItem("Aceptar",0));


        HTest.finish();

    }

}
