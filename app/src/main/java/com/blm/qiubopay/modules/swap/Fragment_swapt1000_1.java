package com.blm.qiubopay.modules.swap;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.blm.qiubopay.R;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.N3Helper;
import com.blm.qiubopay.listeners.ISwapT1000;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_NewUser;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.swap.SwapData;
import com.blm.qiubopay.modules.Fragment_ubicacion;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

/**
 * {@link Fragment} para swap de las T1000
 *
 */
public class Fragment_swapt1000_1 extends HFragment {

    private static final String TAG = "swapt1000_1";

    private ArrayList<CViewEditText> campos;
    private Button btn_continuar;

    public Fragment_swapt1000_1() {
        // Required empty public constructor
    }

    public static Fragment_swapt1000_1 newInstance() {
        Fragment_swapt1000_1 fragment = new Fragment_swapt1000_1();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        return super.onCreated(inflater.inflate(R.layout.fragment_swapt1000_1, container, false), R.drawable.background_splash_header_1);
    }

    public void initFragment(){

        CViewMenuTop.create(getView()).setColorBack(R.color.clear_blue).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        campos = new ArrayList();

        btn_continuar = getView().findViewById(R.id.btn_continuar);

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate();
            }
        };

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_folio))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(15)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_swapt1000_login_folio)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_colaborador))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(15)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_swapt1000_login_colaborador)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));


        final TextView btn_cancelar = getView().findViewById(R.id.btn_cancelar);

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().backFragment();
            }
        });

        btn_continuar.setEnabled(false);

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_continuar.setEnabled(false);

                QPAY_NewUser data = new QPAY_NewUser();
                data.setQpay_folio(campos.get(0).getText());
                data.setQpay_promotor(campos.get(1).getText());

                if(Tools.isN3Terminal()){
                    //Se sobre escribe el número de serie.
                    data.getQpay_device_info()[0].setNewSN(N3Helper.getSn(getContext()));
                    //IccId
                    data.getQpay_device_info()[0].setQpay_icc_id(Tools.getIccId(getContext()));
                }

                checkMigration(data, new IFunction() {
                    @Override
                    public void execute(Object[] rdata) {
                        AppPreferences.setSwapT1000(new SwapData(data.getQpay_folio(),data.getQpay_promotor()));
                        getContext().setFragment(Fragment_ubicacion.newInstance(Fragment_ubicacion.T1000SWAP_ADDRESS));
                    }
                });
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

    private void checkMigration(QPAY_NewUser data, IFunction function) {

        Log.d(TAG,"Peticion: " + new Gson().toJson(data));

        getContext().loading(true);

        ISwapT1000 beginSwapListener = null;
        try {
            beginSwapListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_BaseResponse response = gson.fromJson(json, QPAY_BaseResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            if(function != null)
                                function.execute();

                        } else {
                            getContext().alert(response.getQpay_description());
                            btn_continuar.setEnabled(true);
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                    btn_continuar.setEnabled(true);
                }
            }, getContext());
        } catch (Exception e) {
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
            btn_continuar.setEnabled(true);
        }

        beginSwapListener.beginSwap(data);

    }

}
