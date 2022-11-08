package com.blm.qiubopay.modules.registro;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.blm.qiubopay.R;
import com.blm.qiubopay.components.CLoginOption;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.listeners.IGetChannels;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_NewUser;
import com.blm.qiubopay.models.questions.QPAY_Channels;
import com.blm.qiubopay.models.questions.QPAY_ChannelsResponse;
import com.blm.qiubopay.utils.WSHelper;

import mx.devapps.utils.components.HFragment;

public class Fragment_registro_option extends HFragment {

    private Button btn_bimbo = null;

    public static Fragment_registro_option newInstance() {
        return new Fragment_registro_option();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_registro_option, container, false),R.drawable.background_splash_header_3);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        btn_bimbo = getView().findViewById(R.id.btn_bimbo);

        btn_bimbo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_registro_1.newUser = new QPAY_NewUser();
                getChannels();
            }
        });

        CLoginOption.create(getContext()).onGmail(getView().findViewById(R.id.btn_gmail), new CLoginOption.IClick() {
            @Override
            public void onClick(CLoginOption.LoginDTO data) {

                Fragment_registro_1.newUser = new QPAY_NewUser();
                Fragment_registro_1.newUser.setSocial_token(data.getId());
                Fragment_registro_1.newUser.setQpay_mail(data.getEmail());
                Fragment_registro_1.newUser.setQpay_name(data.getFirstName());
                Fragment_registro_1.newUser.setQpay_father_surname(data.getLastName());
                Fragment_registro_1.newUser.setQpay_mother_surname("");
                Fragment_registro_1.newUser.setLogin_type(data.getType());

                getChannels();
            }
        }).onFacebook(getView().findViewById(R.id.btn_facebook), new CLoginOption.IClick() {
            @Override
            public void onClick(CLoginOption.LoginDTO data) {

                Fragment_registro_1.newUser = new QPAY_NewUser();
                Fragment_registro_1.newUser.setSocial_token(data.getId());
                Fragment_registro_1.newUser.setQpay_mail(data.getEmail());
                Fragment_registro_1.newUser.setQpay_name(data.getFirstName());
                Fragment_registro_1.newUser.setQpay_father_surname(data.getLastName());
                Fragment_registro_1.newUser.setQpay_mother_surname("");
                Fragment_registro_1.newUser.setLogin_type(data.getType());

                getChannels();
            }
        });

    }

    public void getChannels(){

        getContext().loading(true);

        try {

            QPAY_Channels channels = new QPAY_Channels();

            IGetChannels channel = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    String gson = new Gson().toJson(result);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    } else {

                        QPAY_ChannelsResponse response = new Gson().fromJson(gson, QPAY_ChannelsResponse.class);

                        if("000".equals(response.getQpay_code())) {
                            Fragment_registro_1.channels = response.getQpay_object().get(0);
                            getContext().setFragment(Fragment_registro_1.newInstance());
                        } else {
                            getContext().alert(response.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }

            }, getContext());

            Log.e("request", "" + new Gson().toJson(channels));

            channel.getChannels(channels);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

}