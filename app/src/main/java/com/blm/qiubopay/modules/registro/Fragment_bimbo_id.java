package com.blm.qiubopay.modules.registro;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.RegisterActivity;
import com.blm.qiubopay.activities.ScanActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.proceedings.QPAY_DataRecordOnline;
import com.blm.qiubopay.models.proceedings.QPAY_DataRecordOnlineResponse;

import java.util.ArrayList;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.IRequestPermissions;

public class Fragment_bimbo_id extends HFragment implements IMenuContext{


    private ArrayList<CViewEditText> campos;
    private Object data;

    Button btn_continuar;

    public static Fragment_bimbo_id newInstance(Object... data) {
       Fragment_bimbo_id fragment = new Fragment_bimbo_id();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_select_proveedor, container, false),R.drawable.background_splash_header_3);
    }

    @Override
    public void initFragment(){

        CApplication.setAnalytics(CApplication.ACTION.CB_capturar_informacion_personal);

        btn_continuar = getView().findViewById(R.id.btn_continuar);

        campos = new ArrayList();

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate();
            }
        };

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_id_bimbo))
                .setRequired(false)
                .setMinimum(7)
                .setMaximum(14)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_register_50)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        btn_continuar = getView().findViewById(R.id.btn_continuar);

        TextView text_what_is_code = getView().findViewById(R.id.text_what_is_code);
        text_what_is_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().alert(R.string.text_mi_codigo);
            }
        });

        ImageView btn_scaner = getView().findViewById(R.id.btn_scaner);
        btn_scaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().requestPermissions(new IRequestPermissions() {
                    @Override
                    public void onPostExecute() {

                        ScanActivity.action = new ZBarScannerView.ResultHandler() {
                            @Override
                            public void handleResult(Result result) {

                                if(result == null || result.getContents().isEmpty())
                                    return;

                                String resultado = result.getContents().trim();
                                resultado = resultado.replaceAll("[^0-9]","").trim();
                                campos.get(0).setText(resultado);

                            }
                        };

                        getContext().startActivity(ScanActivity.class);
                    }
                }, new String[]{Manifest.permission.CAMERA});
            }
        });

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarId(campos.get(0).getText());
            }
        });

        Button btn_omitir = getView().findViewById(R.id.btn_omitir);
        btn_omitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().setFragment(Fragment_registro_1.newInstance());
            }
        });

    }

    public void consultarId(String id) {

        QPAY_DataRecordOnline data = new QPAY_DataRecordOnline();
        data.setBimbo_id(id);

        RegisterActivity.getDataRecordOnline(getContext(), data, new IFunction<QPAY_DataRecordOnlineResponse>() {
            @Override
            public void execute(QPAY_DataRecordOnlineResponse... data) {
                getContext().setFragment(Fragment_registro_1.newInstance());
            }
        });

    }

    private void validate(){

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                btn_continuar.setEnabled(false);
                return;
            }

        btn_continuar.setEnabled(true);

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}
