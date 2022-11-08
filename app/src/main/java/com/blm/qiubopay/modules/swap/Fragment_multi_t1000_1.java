package com.blm.qiubopay.modules.swap;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.blm.qiubopay.R;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.listeners.IMultiDevice;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.swap.QPAY_LinkT1000Request;
import com.blm.qiubopay.modules.home.Fragment_browser;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import mx.devapps.utils.interfaces.IFunction;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_multi_t1000_1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_multi_t1000_1 extends HFragment {

    private static final String TAG = "multi_t1000_1";

    private static final String URL_PDF = "https://drive.google.com/viewerng/viewer?embedded=true&url=http://docs.qiubo.mx/ManualBLMID.pdf";

    private ArrayList<CViewEditText> campos;
    private Button btn_continuar;

    public Fragment_multi_t1000_1() {
        // Required empty public constructor
    }

    public static Fragment_multi_t1000_1 newInstance() {
        Fragment_multi_t1000_1 fragment = new Fragment_multi_t1000_1();
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
        return super.onCreated(inflater.inflate(R.layout.fragment_multi_t1000_1, container, false), R.drawable.background_splash_header_1);
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

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_blm_id))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(15)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_multi_t1000_1_blmid_hint)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));


        btn_continuar.setEnabled(false);

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_continuar.setEnabled(false);
                QPAY_LinkT1000Request data = new QPAY_LinkT1000Request();
                data.setQpay_blm_id(campos.get(0).getText());

                startLinkT1000(data, new IFunction() {
                    @Override
                    public void execute(Object[] rdata) {
                        getContext().setFragment(Fragment_multi_t1000_2.newInstance(data));
                    }
                });
            }
        });

        TextView btn_cancelar = getView().findViewById(R.id.btn_cancelar);
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().backFragment();
            }
        });

        TextView img_help = getView().findViewById(R.id.img_help);
        img_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().setFragment(Fragment_browser.newInstance(URL_PDF));
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


    private void startLinkT1000(QPAY_LinkT1000Request data, IFunction function) {

        Log.d(TAG,"Peticion startLinkT1000: " + new Gson().toJson(data));

        getContext().loading(true);

        IMultiDevice startLinkListener = null;
        try {
            startLinkListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        Log.d(TAG,"Response startLinkT1000: " + new Gson().toJson(json));
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

        startLinkListener.startLinkT1000(data);

    }

}