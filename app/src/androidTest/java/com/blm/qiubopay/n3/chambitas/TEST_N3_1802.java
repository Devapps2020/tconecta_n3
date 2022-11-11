package com.blm.qiubopay.n3.chambitas;

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
public class TEST_N3_1802 extends HBase {

    @Test
    public void TEST_N3_1802() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.scrollRecycler(new HItem(6));

        HTest.clickTextElement(new HItem("¡Responde y Gana!",3));

        HTest.clickTextElement(new HItem("Chambitas Nuevas",3));

        HTest.clickTextElement(new HItem(DATA.CHAMBITAS.OPCION_1, 3));

        HTest.clickTextElement(new HItem(DATA.CHAMBITAS.RETO_1, 3));

        HTest.clickButton(new HItem( "CONTINUAR", 0));

        HTest.setValueInput(new HItem("Respuesta",DATA.CHAMBITAS.RESPUESTA_1_C));

        HTest.clickButton(new HItem( "CONTINUAR", 1));

        HTest.clickButton(new HItem( "Aceptar", 2));


        HTest.finish();

    }

}
