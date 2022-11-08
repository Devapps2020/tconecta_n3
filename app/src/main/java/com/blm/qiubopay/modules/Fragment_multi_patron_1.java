package com.blm.qiubopay.modules;


import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blm.qiubopay.components.CViewMenuTop;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.LoginActivity;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMultiUserListener;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_Seed;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.utils.WSHelper;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;


public class Fragment_multi_patron_1 extends HFragment {

    private View view;
    private MenuActivity context;


    public Fragment_multi_patron_1() {
        // Required empty public constructor
    }

    public static Fragment_multi_patron_1 newInstance(Object... data) {
        Fragment_multi_patron_1 fragment = new Fragment_multi_patron_1();
        /*Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_multi_patron_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = (MenuActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if (getArguments() != null)
        //data = new Gson().fromJson(getArguments().getString("Fragment_multi_patron_1"), QPAY_NewUser.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_multi_patron_1, container, false);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(view)
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });



        final TextView nombrePatron = (TextView) view.findViewById(R.id.text_nombre_patron);

        Button btn_eliminar = view.findViewById(R.id.btn_eliminar);

        nombrePatron.setText(Html.fromHtml("Patron<br><b>" + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_administrator_name() + "</b>"));

        btn_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String confirmMessage = getString(R.string.patron_1_confirm)
                        .replace("**patron**",AppPreferences.getUserProfile().getQpay_object()[0].getQpay_administrator_name());
                context.alert(confirmMessage, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "Aceptar";
                    }

                    @Override
                    public void onClick() {
                        unlinkUser();

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
        });

    }

    /**
     * Desligar cuenta
     */
    private void unlinkUser(){
        context.loading(true);

        QPAY_Seed qpaySeed = new QPAY_Seed();
        qpaySeed.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

        try {
            IMultiUserListener request = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {

                        context.alert(R.string.general_error);

                    } else{

                        Gson gson =  new GsonBuilder().create();
                        String json =  gson.toJson(result);
                        QPAY_BaseResponse response = gson.fromJson(json, QPAY_BaseResponse.class);

                        if (response.getQpay_response().compareTo("true")==0) {

                            AppPreferences.setCloseSessionFlag("1",getString(R.string.patron_1_success));

                            context.alert(context.getResources().getString(R.string.patron_1_success), new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "Aceptar";
                                }

                                @Override
                                public void onClick() {

                                    AppPreferences.Logout(context);
                                    context.startActivity(LoginActivity.class, true);

                                }
                            });

                        } else {

                            if(response.getQpay_code().equals("017")
                                    || response.getQpay_code().equals("018")
                                    || response.getQpay_code().equals("019")
                                    || response.getQpay_code().equals("020")){
                                //Semilla erronea, se cierra la sesión.
                                context.alert(context.getResources().getString(R.string.message_logout), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {

                                        AppPreferences.Logout(context);
                                        context.startActivity(LoginActivity.class, true);

                                    }
                                });

                            } else if(response.getQpay_description().contains("UNAUTHORIZED:HTTP")){
                               // context.showAlertAEONBlockedUser();
                            } else {
                                context.alert(response.getQpay_description(), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        context.initHome();

                                    }
                                });

                            }


                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    context.loading(false);

                    context.alert(R.string.general_error);

                }
            }, context);

            request.unlinkUser(qpaySeed);

        } catch (Exception e) {

        }
    }

}
