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

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.helpers.N3Helper;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.ISwapN3;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_NewUser;
import com.blm.qiubopay.models.swap.QPAY_SwapN3LoginRequest;
import com.blm.qiubopay.models.swap.QPAY_SwapN3LoginResponse;
import com.blm.qiubopay.models.swap.QPAY_SwapN3OutResponse;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_swapn3_login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_swapn3_login extends HFragment implements IMenuContext  {

    private static final String TAG = "swapn3_login";

    private ArrayList<CViewEditText> campos;
    private Button btn_continuar;

    private boolean pass;
    private String promotor;

    //Variable para distinguir si va a ser login de swap out o swap in
    private boolean isSwapOut;

    public Fragment_swapn3_login() {
        // Required empty public constructor
    }

    public static Fragment_swapn3_login newInstance(Object... data) {
        Fragment_swapn3_login fragment = new Fragment_swapn3_login();

        Bundle args = new Bundle();
        args.putBoolean("Fragment_swapn3_login", (Boolean) data[0]);
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
            isSwapOut = getArguments().getBoolean("Fragment_swapn3_login");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_swapn3_login, container, false), R.drawable.background_splash_header_1);
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    public void initFragment(){

        campos = new ArrayList();

        CViewMenuTop.create(getView()).setColorBack(R.color.clear_blue).onClickBack(new CViewMenuTop.IClick() {
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

        btn_continuar = getView().findViewById(R.id.btn_continuar);

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_colaborador))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(15)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_swapn3_out_colaborador)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_pass))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(15)
                .setType(CViewEditText.TYPE.TEXT_PASS)
                .setHint(R.string.text_swapn3_out_pass)
                .setAlert(R.string.text_input_required)
                .setIconPass(R.drawable.show, R.drawable.show_off)
                .setTextChanged(validate));

        btn_continuar.setEnabled(false);
        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_continuar.setEnabled(false);
                buildForLogin();
            }
        });

        if(!isSwapOut){
            final TextView tv_description = getView().findViewById(R.id.tv_description);
            tv_description.setText(R.string.text_swapn3_out_in_description);

            final TextView btn_cancelar = getView().findViewById(R.id.btn_cancelar);
            btn_cancelar.setVisibility(View.VISIBLE);
            btn_cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getContext().backFragment();
                }
            });
        }

    }

    private void validate(){

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()) {
                btn_continuar.setEnabled(false);
                return;
            }

        btn_continuar.setEnabled(true);
    }

    private void buildForLogin(){
        QPAY_SwapN3LoginRequest dataRequest = new QPAY_SwapN3LoginRequest();
        dataRequest.setPromotorNumber(campos.get(0).getText());
        //dataRequest.setPassword(Tools.encodeSHA256(campos.get(1).getText()));
        dataRequest.setPassword(campos.get(1).getText());

        loginSwap(dataRequest, new IFunction() {
            @Override
            public void execute(Object[] data) {

                if(isSwapOut){
                    //Swap Out
                    getContext().alert(R.string.text_swapn3_out_confirm, new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Aceptar";
                        }

                        @Override
                        public void onClick() {
                            buildForSwapOut();
                        }
                    });
                } else {
                    //Swap In
                    getContext().setFragment(Fragment_swapn3_in.newInstance(promotor));
                }

            }
        });
    }

    private void buildForSwapOut(){

        QPAY_NewUser dataRequest = new QPAY_NewUser();
        dataRequest.setQpay_promotor(promotor);
        dataRequest.setQpay_mail(AppPreferences.getUserCredentials().getUser());
        dataRequest.setQpay_password(Tools.encodeSHA256(AppPreferences.getUserCredentials().getPwd()));

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

        swapOutN3(dataRequest, new IFunction() {
            @Override
            public void execute(Object[] data) {
                getContext().setFragment(Fragment_swapn3_out_ticket.newInstance(true,(QPAY_SwapN3OutResponse) data[0]));
            }
        });

    }

    /**
     * Rest para login de promotor
     * @param data
     * @param function
     */
    private void loginSwap(QPAY_SwapN3LoginRequest data, IFunction function) {

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
                        QPAY_SwapN3LoginResponse response = gson.fromJson(json, QPAY_SwapN3LoginResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            promotor = response.getQpay_object()[0].getPromotorNumber();

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

        loginSwapListener.loginSwap(data);

    }

    /**
     * Rest para swap out
     * @param data
     * @param function
     */
    private void swapOutN3(QPAY_NewUser data, IFunction function) {

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

        loginSwapListener.swapOutN3(data);

    }

}