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
public class TEST_N3_1807 extends HBase {

    @Test
    public void TEST_N3_1807() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.scrollRecycler(new HItem(6));

        HTest.clickTextElement(new HItem("¡Responde y Gana!",3));

        HTest.clickTextElement(new HItem("Chambitas Nuevas",3));

        HTest.clickTextElement(new HItem(DATA.CHAMBITAS.OPCION_3, 3));

        HTest.clickTextElement(new HItem(DATA.CHAMBITAS.RETO_5, 3));

        HTest.setValueInput(new HItem("",DATA.CHAMBITAS.RESPUESTA_5_1,0));

        HTest.setValueInput(new HItem("",DATA.CHAMBITAS.RESPUESTA_5_2,1));

        HTest.setValueInput(new HItem("",DATA.CHAMBITAS.RESPUESTA_5_3,2));

        HTest.setValueInput(new HItem("",DATA.CHAMBITAS.RESPUESTA_5_4,3));

        HTest.setValueInput(new HItem("",DATA.CHAMBITAS.RESPUESTA_5_5,4));

        HTest.setValueInput(new HItem("",DATA.CHAMBITAS.RESPUESTA_5_6,5));

        HTest.setValueInput(new HItem("",DATA.CHAMBITAS.RESPUESTA_5_MAYOR,6));

        HTest.finish();

    }

}
