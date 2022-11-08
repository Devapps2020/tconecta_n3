package com.blm.qiubopay.modules.swap;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.LoginActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.helpers.N3Helper;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.ISwapN3;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_NewUser;
import com.blm.qiubopay.models.swap.QPAY_SwapN3OutResponse;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.interfaces.IFunction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_swapn3_in#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_swapn3_in extends HFragment {

    private static final String TAG = "swapn3_in";

    private ArrayList<CViewEditText> campos;
    private Button btn_continuar;

    private String promotor;

    public Fragment_swapn3_in() {
        // Required empty public constructor
    }

    public static Fragment_swapn3_in newInstance(Object... data) {
        Fragment_swapn3_in fragment = new Fragment_swapn3_in();

        Bundle args = new Bundle();
        args.putString("Fragment_swapn3_in", (String) data[0]);
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

        if (getArguments() != null)
            promotor = getArguments().getString("Fragment_swapn3_in");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_swapn3_in, container, false), R.drawable.background_splash_header_1);
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

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_colaborador))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(15)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_swapn3_in_folio)
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
                buildForSwapIn();
            }
        });

    }

    private void validate(){

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()) {
                btn_continuar.setEnabled(false);
                return;
            }

        btn_continuar.setEnabled(true);
    }

    private void buildForSwapIn(){

        QPAY_NewUser dataRequest = new QPAY_NewUser();
        dataRequest.setQpay_promotor(promotor);
        dataRequest.setQpay_folio(campos.get(0).getText());

        if(CApplication.getLastLocation() != null) {
            dataRequest.getQpay_device_info()[0].setQpay_geo_x(String.valueOf(CApplication.getLastLocation().getLongitude()));
            dataRequest.getQpay_device_info()[0].setQpay_geo_y(String.valueOf(CApplication.getLastLocation().getLatitude()));
        }

        if(Tools.isN3Terminal()){
            //Se sobre escribe el número de serie.
            dataRequest.getQpay_device_info()[0].setNewSN(N3Helper.getSn(getContext()));

            //IccId
            dataRequest.getQpay_device_info()[0].setQpay_icc_id(Tools.getIccId(getContext()));
        }

        swapInN3(dataRequest, new IFunction() {
            @Override
            public void execute(Object[] data) {
                getContext().setFragment(Fragment_swapn3_out_ticket.newInstance(false,(QPAY_SwapN3OutResponse) data[0]));
            }
        });

    }

    /**
     * Rest para swap in
     * @param data
     * @param function
     */
    private void swapInN3(QPAY_NewUser data, IFunction function) {

        Log.d(TAG,"Request: " + new Gson().toJson(data));

        getContext().loading(true);

        ISwapN3 loginSwapListener = null;
        try {
            loginSwapListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        Log.d(TAG,"Response: " +json);
                        QPAY_SwapN3OutResponse response = gson.fromJson(json, QPAY_SwapN3OutResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            if(function != null)
                                function.execute(response);

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

        loginSwapListener.swapInN3(data);

    }

}