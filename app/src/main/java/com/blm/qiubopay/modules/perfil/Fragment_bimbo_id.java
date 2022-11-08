package com.blm.qiubopay.modules.perfil;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.IUpdateUserInformation;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_NewUser;
import com.blm.qiubopay.models.QPAY_UserProfile;
import com.blm.qiubopay.models.profile.QPAY_Profile;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_bimbo_id#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_bimbo_id extends HFragment implements IMenuContext {

    private static final String TAG = "bimbo_id";

    private ArrayList<CViewEditText> campos;
    private Button btn_continuar;

    private RadioButton rd_gb_bimboId;

    private boolean bimboID;


    public Fragment_bimbo_id() {
        // Required empty public constructor
    }

    public static Fragment_bimbo_id newInstance() {
        Fragment_bimbo_id fragment = new Fragment_bimbo_id();
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

        return super.onCreated(inflater.inflate(R.layout.fragment_bimbo_id, container, false), R.drawable.background_splash_header_1);
    }

    public void initFragment(){

        campos = new ArrayList();

        btn_continuar = getView().findViewById(R.id.btn_continuar);
        rd_gb_bimboId = getView().findViewById(R.id.rd_gb_bimboId);

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate();
            }
        };

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_bimbo_id))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(rd_gb_bimboId.isChecked() ? 28 : 30)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.placeholderParameterMerchantBimboId)
                .setAlert(R.string.text_input_required)
                .setTextChanged(
                        new CViewEditText.ITextChanged() {
                            @Override
                            public void onChange(String text) {
                                if (campos.get(0) != null){
                                    validateBimboID(rd_gb_bimboId.isChecked() ? "GB" + text : text);
                                }
                            }
                        }));


        rd_gb_bimboId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bimboID){
                    bimboID = false;
                }else{
                    bimboID = true;
                }
                campos.get(0).setPrefix(bimboID ? "GB" : null);
                campos.get(0).setMaximum(bimboID ? 28 : 30);
                rd_gb_bimboId.setChecked(bimboID);


            }
        });


        btn_continuar.setEnabled(false);

        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_continuar.setClickable(false);

                QPAY_NewUser updateUser = new QPAY_NewUser();
                updateUser.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
                String bimbo_id =  bimboID ? "GB" + campos.get(0).getText() : campos.get(0).getText();

                updateUser.setQpay_bimbo_id(bimbo_id.isEmpty() ? null : bimbo_id.toUpperCase());

                updateUserInformation(updateUser, new IFunction() {
                    @Override
                    public void execute(Object[] rdata) {
                        getContext().alert("Actualización exitosa!", new IAlertButton() {
                            @Override
                            public String onText() {
                                return getString(R.string.accept_button);
                            }

                            @Override
                            public void onClick() {
                                getContextMenu().initHome();
                            }
                        });
                    }
                });
            }
        });

    }
    private void validateBimboID(String text){
        String regexpGB = "(GB|gb)[0-9]{1,28}"; // GB + 28 números
        String regexNum = "[0-9]{1,30}"; // 30 números

        if (text.matches(rd_gb_bimboId.isChecked() ? regexpGB : regexNum)){
            validate();
        }else{
            campos.get(0).setError("Código bimbo invalido",false);
            campos.get(0).activeError();
            btn_continuar.setEnabled(false);
        }
    }
    private void validate(){

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                btn_continuar.setEnabled(false);
                return;
            }

        btn_continuar.setEnabled(true);
    }

    /**
     * Método para actualizar los datos del usuario donde le será entregado el dongle
     */
    public void updateUserInformation(QPAY_NewUser userToUpdate, final IFunction function){

        getContext().loading(true);

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(userToUpdate);
        Log.d(TAG,"Request: " + json);

        IUpdateUserInformation registerUserService = null;
        try {
            registerUserService = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        btn_continuar.setClickable(true);
                        getContext().alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_Profile.QPAY_ProfileExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_UserProfile userProfile = gson.fromJson(json, QPAY_UserProfile.class);

                        if(userProfile.getQpay_response().equals("true")) {
                            Log.d(TAG,"Actualizacion exitosa: " + new Gson().toJson(userProfile));
                            AppPreferences.setUserProfile(userProfile);

                            if(function != null)
                                function.execute();

                        } else {
                            btn_continuar.setClickable(true);
                            getContextMenu().validaSesion(userProfile.getQpay_code(), userProfile.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    btn_continuar.setClickable(true);
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());
        } catch (Exception e) {
            getContext().alert(R.string.general_error_catch);
        }

        registerUserService.updateUserInformation(userToUpdate);
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}