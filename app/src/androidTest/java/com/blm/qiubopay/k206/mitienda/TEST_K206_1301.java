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
public class TEST_K206_1301 extends HBase {

    @Test
    public void TEST_K206_1301() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickTextElement(new HItem("Mi Tienda",1, true));

        HTest.clickTextElement(new HItem("Fiado",3));

        HTest.clickTextElement(new HItem("Nuevo fiado",3));

        HTest.clickTextElement(new HItem("Cliente neuvo",3));

        HTest.setValueInput(new HItem("Nombre", DATA.FIADO.NOMBRE, 2));

        HTest.setValueInput(new HItem("Email", DATA.FIADO.CORREO_ELECTRONICO, 3));

        HTest.setValueInput(new HItem("Número celular", DATA.FIADO.NUMERO_CELULAR, 4));

        HTest.clickButton(new HItem("CONFIRMAR",0));

        HTest.clickButton(new HItem("Si",0));

        HTest.setValueInput(new HItem("", DATA.FIADO.LIMITE_MONTO, 2));

        HTest.setValueInput(new HItem("", DATA.FIADO.LIMITE_TIEMPO, 3));

        HTest.setValueInput(new HItem("", DATA.FIADO.INTERESES, 4));

        HTest.clickButton(new HItem("Guardar cambios",0));

        HTest.finish();
    }

}
