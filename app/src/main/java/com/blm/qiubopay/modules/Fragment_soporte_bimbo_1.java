package com.blm.qiubopay.modules;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.components.CViewOption;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.listeners.IGetCommonsError;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_Seed;
import com.blm.qiubopay.models.soporte.QPAY_CommonErrorResponse;
import com.blm.qiubopay.modules.home.Fragment_browser;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IRequestPermissions;

public class Fragment_soporte_bimbo_1 extends HFragment implements IMenuContext {

    private View view;
    private HActivity context;
    private Object data;

    public static boolean soporte;

    private static final String URL_Chatbot = "https://embed.agentbot.net/4acf349ebf1b5a254c05f432bcfa38c5?send_message=inicio";

    public static Fragment_soporte_bimbo_1 newInstance(Object... data) {
        Fragment_soporte_bimbo_1 fragment = new Fragment_soporte_bimbo_1();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_soporte_bimbo_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = (HActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_soporte_1"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_soporte_bimbo_1, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(getView())
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        LinearLayout view_menu_support = view.findViewById(R.id.view_menu_support);
        Button btn_pedir_ayuda = view.findViewById(R.id.btn_pedir_ayuda);
        ImageView img_close = view.findViewById(R.id.img_close);

        btn_pedir_ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_menu_support.setVisibility(View.VISIBLE);
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_menu_support.setVisibility(View.GONE);
            }
        });

        CViewMenuTop.create(view).showLogo().onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().onBackPressed();
            }
        });

        CViewOption.create(view.findViewById(R.id.layout_preguntas_frecuentes)).setText(R.string.text_soporte_14).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {
                getContext().setFragment(Fragment_browser.newInstance(Globals.URL_FAQS));
            }
        });

        CViewOption.create(view.findViewById(R.id.layout_whatsapp)).setText(R.string.text_soporte_20).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {

                context.alert(getContext().getResources().getString(R.string.text_soporte_23).replace("*phone*", Globals.PHONE_WHATSAPP), new IAlertButton() {
                    @Override
                    public String onText() {
                        return "Aceptar";
                    }

                    @Override
                    public void onClick() {

                    }
                });

            }
        });

        CViewOption.create(view.findViewById(R.id.layout_chat)).setText(R.string.text_soporte_21).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {
                context.setFragment(Fragment_browser.newInstanceChatbot(URL_Chatbot));
            }
        });

        CViewOption.create(view.findViewById(R.id.layout_llamada)).setText(R.string.text_soporte_19).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {
                context.alert(getContext().getResources().getString(R.string.text_soporte_22).replace("*phone*", Globals.PHONE_SUPPORT), new IAlertButton() {
                    @Override
                    public String onText() {
                        return "Aceptar";
                    }

                    @Override
                    public void onClick() {

                    }
                });
            }
        });

        CViewOption.create(view.findViewById(R.id.layout_formulario)).setText(R.string.text_soporte_17).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {
                getCommonsErrorList();
            }
        }).setVisible(AppPreferences.isPinRegistered());

        CViewOption.create(view.findViewById(R.id.layout_manual)).setText(R.string.text_soporte_24).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {
                getContext().setFragment(Fragment_browser.newInstance(Globals.URL_MANUAL));
            }
        });


        TextView text_formulario = view.findViewById(R.id.text_formulario);
        TextView text_chat = view.findViewById(R.id.text_chat);
        TextView text_llamada = view.findViewById(R.id.text_llamada);

        text_formulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_menu_support.setVisibility(View.GONE);
                context.setFragment(Fragment_soporte_bimbo_2.newInstance());
            }
        });

        text_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_menu_support.setVisibility(View.GONE);
                context.setFragment(Fragment_browser.newInstance(URL_Chatbot));
            }
        });

        text_llamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_menu_support.setVisibility(View.GONE);

                context.requestPermissions(new IRequestPermissions() {
                    @Override
                    public void onPostExecute() {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + "5546319740"));
                        startActivity(intent);
                    }
                }, new String[]{Manifest.permission.CALL_PHONE});

            }
        });

    }


    public void getCommonsErrorList(){

        QPAY_Seed authSeed = new QPAY_Seed();
        authSeed.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

        getContext().loading(true);

        try {

            IGetCommonsError petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                    } else {

                        //Respuesta correcta
                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_CommonErrorResponse.QPAY_CommonErrorResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_CommonErrorResponse response = gson.fromJson(json, QPAY_CommonErrorResponse.class);

                        if(response.getQpay_response().equals("true")){
                            getContext().setFragment(Fragment_reportar_problema.newInstance(response));
                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.doGetErrorList(authSeed);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

}

