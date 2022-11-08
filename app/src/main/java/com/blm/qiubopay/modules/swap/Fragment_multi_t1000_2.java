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
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.listeners.IMultiDevice;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.swap.QPAY_LinkT1000Request;
import com.blm.qiubopay.modules.Fragment_ubicacion;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import mx.devapps.utils.interfaces.IFunction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_multi_t1000_2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_multi_t1000_2 extends HFragment {

    private static final String TAG = "multi_t1000_2";

    private ArrayList<CViewEditText> campos;
    private Button btn_continuar;

    private QPAY_LinkT1000Request linkRequest;

    public Fragment_multi_t1000_2() {
        // Required empty public constructor
    }


    public static Fragment_multi_t1000_2 newInstance(Object data) {
        Fragment_multi_t1000_2 fragment = new Fragment_multi_t1000_2();
        Bundle args = new Bundle();

        args.putString("Fragment_multi_t1000_2", new Gson().toJson(data));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            linkRequest = new Gson().fromJson(getArguments().getString("Fragment_multi_t1000_2"), QPAY_LinkT1000Request.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_multi_t1000_2, container, false), R.drawable.background_splash_header_1);
    }

    public void initFragment(){

        campos = new ArrayList();

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate();
            }
        };

        btn_continuar = getView().findViewById(R.id.btn_continuar);

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_folio))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(15)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_multi_t1000_2_folio_hint)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        TextView btn_cancelar = getView().findViewById(R.id.btn_cancelar);
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
                linkRequest.setQpay_folio(campos.get(0).getText());

                validateLinkT1000(linkRequest, new IFunction() {
                    @Override
                    public void execute(Object[] rdata) {
                        AppPreferences.setSwapT1000(null);
                        AppPreferences.setLinkT1000(linkRequest);
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

    private void validateLinkT1000(QPAY_LinkT1000Request data, IFunction function) {

        Log.d(TAG,"Peticion validateLinkT1000: " + new Gson().toJson(data));

        getContext().loading(true);

        IMultiDevice validateLinkListener = null;
        try {
            validateLinkListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        Log.d(TAG,"Response validateLinkT1000: " + new Gson().toJson(json));
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

        validateLinkListener.validateLinkT1000(data);

    }

}