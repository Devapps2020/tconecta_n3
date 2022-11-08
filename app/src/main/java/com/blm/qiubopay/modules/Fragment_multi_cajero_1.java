package com.blm.qiubopay.modules;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.blm.qiubopay.components.CViewMenuTop;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMultiUserListener;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.LinkCode;
import com.blm.qiubopay.models.LinkedUser;
import com.blm.qiubopay.models.QPAY_LinkCodeResponse;
import com.blm.qiubopay.models.QPAY_LinkedUser;
import com.blm.qiubopay.models.QPAY_LinkedUsersResponse;
import com.blm.qiubopay.models.QPAY_Seed;
import com.blm.qiubopay.models.QPAY_UserProfile;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_multi_cajero_1 extends HFragment {

    private static final String STATUS_PENDIENTE = "3";

    private View view;
    private MenuActivity context;
    private ListView list_cajeros;
    private ArrayList<LinkedUser> cajerosArray;

    public Fragment_multi_cajero_1() {
        // Required empty public constructor
    }

    public static Fragment_multi_cajero_1 newInstance() {
        Fragment_multi_cajero_1 fragment = new Fragment_multi_cajero_1();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_multi_cajero_1, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CApplication.setAnalytics(CApplication.ACTION.CB_AGREGAR_CAJERO_inician);

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        Button btnAgregarCajero = view.findViewById(R.id.btn_nuevo_cajero);

        list_cajeros = view.findViewById(R.id.list_cajeros);

        obtieneCajeros();

        btnAgregarCajero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLinkCode(new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        context.setFragment(Fragment_multi_2.newInstance(data[0]));
                    }
                });
            }
        });

        list_cajeros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                final LinkedUser selectedUser = cajerosArray.get(position);
                final Integer id = selectedUser.getQpay_id();

                //20200610 RSB. Quick Fix sin cajeros
                final boolean lastCashier = (cajerosArray.size() == 1 ? true : false);


                QPAY_LinkedUser linkedUser = new QPAY_LinkedUser();
                linkedUser.setQpay_id(id);
                linkedUser.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

                if (cajerosArray.get(position).getQpay_link_status().compareTo("3")==0) {
                    context.alert(getString(R.string.cajero_1_confirm), new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Aceptar";
                        }

                        @Override
                        public void onClick() {

                                approveUser(linkedUser, new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {
                                        context.alert(R.string.cajero_1_link_success, new IAlertButton() {
                                            @Override
                                            public String onText() {
                                                return "Aceptar";
                                            }

                                            @Override
                                            public void onClick() {
                                                context.setFragment(Fragment_multi_cajero_2.newInstance(selectedUser,lastCashier));
                                            }
                                        }, new IAlertButton() {
                                            @Override
                                            public String onText() {
                                                return "Cancelar";
                                            }

                                            @Override
                                            public void onClick() {

                                                context.initHome();

                                            }
                                        });
                                    }
                                });
                        }

                    }, new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Rechazar";
                        }

                        @Override
                        public void onClick() {

                            unlinkUserByAdmin(linkedUser, new IFunction() {
                                @Override
                                public void execute(Object[] data) {
                                    context.alert(R.string.cajero_1_unlink_success, new IAlertButton() {
                                        @Override
                                        public String onText() {
                                            return "Aceptar";
                                        }

                                        @Override
                                        public void onClick() {

                                            obtieneCajeros();

                                        }
                                    });
                                }
                            });


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

                } else {
                    context.setFragment(Fragment_multi_cajero_2.newInstance(selectedUser,lastCashier));
                }
            }
        });
    }

    private void obtieneCajeros()
    {
        QPAY_Seed linkedUsers = new QPAY_Seed();
        linkedUsers.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

        context.loading(true);

        try {
            IMultiUserListener request = new WSHelper(new IGenericConnectionDelegate() {

                @Override
                public void onConnectionEnded(Object result) {
                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);

                    } else {

                        Gson gson =  new GsonBuilder().create();
                        String json =  gson.toJson(result);
                        QPAY_LinkedUsersResponse response = gson.fromJson(json, QPAY_LinkedUsersResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            cajerosArray = new ArrayList<>();

                            for (int x = 0; x < response.getQpay_object().length; x++) {
                                cajerosArray.add(response.getQpay_object()[x]);

                            }

                            ArrayAdapter adapter = new CajerosAdapter(context, cajerosArray);
                            list_cajeros.setAdapter(adapter);

                            if (cajerosArray.size() == 0) {
                                context.alert(R.string.cajero_1_sin_cajeros, new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {

                                        QPAY_UserProfile profile = AppPreferences.getUserProfile();
                                        profile.getQpay_object()[0].setQpay_user_type(Globals.ROL_NORMAL);
                                        AppPreferences.setUserProfile(profile);
                                        context.initHome();

                                    }
                                });
                            }
                        }else {

                            context.validaSesion(response.getQpay_code(), response.getQpay_description());

                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    context.loading(false);

                    context.alert(R.string.general_error);
                }
            }, context);

            request.getLinkedUsers(linkedUsers);

        }catch (Exception e) {
            e.printStackTrace();
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

    }


    public class CajerosAdapter extends ArrayAdapter<LinkedUser> {


        public CajerosAdapter(Context context, List<LinkedUser> cajeros) {
            super(context, 0, cajeros);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LinkedUser cajero = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_cajero, parent, false);
            }

            TextView text_titulo = convertView.findViewById(R.id.text_titulo);

            TextView text_estatus = convertView.findViewById(R.id.text_estatus);

            if(cajero.getQpay_link_alias()!= null && !cajero.getQpay_link_alias().isEmpty()) {
                text_titulo.setText(cajero.getQpay_link_alias());
            } else {
                text_titulo.setText(cajero.getQpay_name() + " " + cajero.getQpay_father_surname() + " " + cajero.getQpay_mother_surname());
            }

            String statusName = (cajero.getQpay_link_status().compareTo(STATUS_PENDIENTE)==0 ?
                    getString(R.string.cajero_1_status_pending) : getString(R.string.cajero_1_status_linked));

            text_estatus.setText(statusName);

            return convertView;
        }
    }


    /**
     * Método para desligar un cajero de la cuenta de administrador
     * @param function
     */
    private void approveUser(QPAY_LinkedUser linkedUser, final IFunction function) {

        context.loading(true);

        IMultiUserListener approveListener = null;
        try {
            approveListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_BaseResponse response = gson.fromJson(json, QPAY_BaseResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            if(function != null)
                                function.execute();

                        } else {
                            context.validaSesion(response.getQpay_code(), response.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }
            }, context);
        } catch (Exception e) {
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

        approveListener.linkUser(linkedUser);

    }

    /**
     * Método para desligar un cajero de la cuenta de administrador
     * @param function
     */
    private void unlinkUserByAdmin(QPAY_LinkedUser linkedUser, final IFunction function) {

        context.loading(true);

        IMultiUserListener unlinkListener = null;
        try {
            unlinkListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_BaseResponse response = gson.fromJson(json, QPAY_BaseResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            if(function != null)
                                function.execute();

                        } else {
                            context.validaSesion(response.getQpay_code(), response.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }
            }, context);
        } catch (Exception e) {
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

        unlinkListener.unlinkUserByAdmin(linkedUser);

    }

    /**
     * Método para llamar servicio para generar código
     * @param function
     */
    public void getLinkCode(final IFunction function) {

        context.loading(true);

        QPAY_Seed data = new QPAY_Seed();
        data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

        IMultiUserListener linkCodeListener = null;
        try {
            linkCodeListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_LinkCodeResponse linkCodeResponse = gson.fromJson(json, QPAY_LinkCodeResponse.class);

                        if (linkCodeResponse.getQpay_response().equals("true")) {

                            LinkCode linkCode = linkCodeResponse.getQpay_object()[0];

                            if(function != null)
                                function.execute(linkCode);

                        } else {
                            context.validaSesion(linkCodeResponse.getQpay_code(), linkCodeResponse.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }
            }, context);
        } catch (Exception e) {
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

        linkCodeListener.getLinkCode(data);

    }


}
