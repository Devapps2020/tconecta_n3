package com.blm.qiubopay.n3.mitienda;

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
public class TEST_N3_752 extends HBase {
    
    @Test
    public void TEST_N3_752() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickTextElement(new HItem("Mi Tienda",1, true));

        HTest.clickTextElement(new HItem("Fiado",3));

        HTest.clickTextElement(new HItem("Nuevo fiado",1));

        HTest.clickTextElement(new HItem("Nuevo cliente",1));

        HTest.setValueInput(new HItem("Nombre", DATA.FIADO.NOMBRE, 0));

        HTest.setValueInput(new HItem("Email", DATA.FIADO.CORREO_ELECTRONICO, 1));

        HTest.setValueInput(new HItem("Número celular", DATA.FIADO.NUMERO_CELULAR, 2));

        HTest.clickButton(new HItem("CONFIRMAR",2));

        HTest.finish();
    }

}
