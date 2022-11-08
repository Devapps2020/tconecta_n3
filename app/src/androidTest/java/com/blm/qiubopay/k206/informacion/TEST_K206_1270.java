package com.blm.qiubopay.k206.informacion;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.blm.qiubopay.R;
import com.blm.qiubopay.k206.DATA;
import com.blm.qiubopay.utils.HBase;
import com.blm.qiubopay.utils.HItem;
import com.blm.qiubopay.utils.HTest;

import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TEST_K206_1270 extends HBase {
    
    @Test
    public void TEST_K206_1270() {

        HTest.start(this.getClass());

        HTest.setValuePin(new HItem( "****", DATA.USUARIO.PIN,0));

        HTest.clickButton(new HItem( "CONFIRMAR", 0));

        HTest.clickLinearLayout(new HItem(R.id.btn_user + "",22));

        HTest.clickTextElement(new HItem("Compra de Material"));

        HTest.checkLoading();

        HTest.setValueSpinner(new HItem("Productos disponibles",+0));

        HTest.clickText(new HItem("producto",0));

        HTest.clickButton(new HItem( "COMPRAR CON MI SALDO", 0));

        HTest.clickButton(new HItem( "Aceptar", 0));

        HTest.setValueInput(new HItem("Correo electrónico", DATA.USUARIO.CORREO_ELECTRONICO, 2));

        HTest.setValueInput(new HItem("Confirmar correo electrónico", DATA.USUARIO.CORREO_ELECTRONICO, 3));

        HTest.setValueInput(new HItem("Número celular a 10 dígitos", DATA.USUARIO.NUMERO_CELULAR, 4));

        HTest.setValueInput(new HItem("Nombre", DATA.USUARIO.NOMBRE, 5));

        HTest.setValueInput(new HItem("Apellido paterno", DATA.USUARIO.APELLIDO_PATERNO, 6));

        HTest.setValueInput(new HItem("Apellido materno", DATA.USUARIO.APELLIDO_MATERNO, 7));

        HTest.setValueInput(new HItem("Número de colaborador", DATA.USUARIO.COLABORADOR, 8));

        HTest.setValueInput(new HItem("Código Bimbo", DATA.USUARIO.CODIGO_BIMBO, 9));

        HTest.finish();
    }

}
