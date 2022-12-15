package com.blm.qiubopay.n3.mitienda;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.blm.qiubopay.R;
import com.blm.qiubopay.n3.DATA;
import com.blm.qiubopay.utils.HBase;
import com.blm.qiubopay.utils.HItem;
import com.blm.qiubopay.utils.HTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TEST_N3_755 extends HBase {
    
    @Test
    public void TEST_N3_755() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickTextElement(new HItem("Mi Tienda",1, true));

        HTest.clickTextElement(new HItem("Fiado",3));

        HTest.clickTextElement(new HItem("Nuevo fiado",3));

        HTest.clickTextElement(new HItem("Nuevo cliente",3));

        HTest.clickTextElement(new HItem("Nuevo cliente",1));

        HTest.setValueInput(new HItem("Nombre", DATA.FIADO.NOMBRE, 0));

        HTest.setValueInput(new HItem("Email", DATA.FIADO.CORREO_ELECTRONICO, 1));

        HTest.setValueInput(new HItem("Número celular", DATA.FIADO.NUMERO_CELULAR, 2));

        HTest.clickButton(new HItem("CONFIRMAR",2));

        HTest.clickButton(new HItem("Si",3));

        HTest.clickRadio(new HItem(R.id.radLimitarMonto+"", 0));

        HTest.setValueInput(new HItem("", DATA.FIADO.LIMITE_MONTO, 5));

        HTest.clickRadio(new HItem(R.id.radLimitarTiempo+"", 1));

        HTest.setValueInput(new HItem("", DATA.FIADO.LIMITE_TIEMPO, 6));

        HTest.clickRadio(new HItem(R.id.radCobrarInteres+"", 2));

        HTest.setValueInput(new HItem("", DATA.FIADO.INTERESES, 7));

        HTest.clickButton(new HItem("Guardar cambios",6));

        HTest.checkLoading();

        HTest.setValueInput(new HItem("Monto a fiar", DATA.FIADO.MONTO, 2));

        HTest.clickButton(new HItem("CONFIRMAR",0));

        HTest.setValueInput(new HItem("Detalle", DATA.FIADO.DETALLE, 3));

        HTest.clickButton(new HItem("CONFIRMAR",0));

        HTest.checkLoading();

        HTest.finish();
    }

}
