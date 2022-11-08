package com.blm.qiubopay.activities;

import android.os.Bundle;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HLocActivity;
import com.blm.qiubopay.helpers.HLocation;
import com.blm.qiubopay.modules.login.Fragment_login_pin;
import com.blm.qiubopay.modules.login.Fragment_login_user;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class LoginActivity extends HLocActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceso);

        getContext().setHostFragmentId(R.id.nav_host_fragment);

        CApplication.setAnalytics(CApplication.ACTION.CB_acceder_cuenta);

        if(AppPreferences.isPinRegistered()){

            Fragment_login_pin.login = true;

            Fragment_login_pin.authPIN = new IFunction() {
                @Override
                public void execute(Object[] data) {
                    getContext().startActivity(MenuActivity.class, true);
                }
            };

            getContext().setDefaultBack(new IFunction() {
                @Override
                public void execute(Object[] data) {
                    getContext().finish();
                }
            });

            getContext().setFragment(Fragment_login_pin.newInstance(), true);

        } else {
            CApplication.setAnalytics("Fragment_login_user");
        }

        //HLocation.start(true, getContext());

        getContext().setFinishBack(new IFunction() {
            @Override
            public void execute(Object[] data) {
                //20210427 RSB. Homologacion. Valida salir n3
                if(getContext().validateCloseTaps()){
                    getContext().alert(R.string.alert_message_close_app, new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Cerrar";
                        }
                        @Override
                        public void onClick() {
                            getContext().finish();
                        }
                    }, new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Cancelar";
                        }
                        @Override
                        public void onClick() {

                        }
                    });
                }

            }
        });

    }

}
