package com.blm.qiubopay.utils;

import android.content.Context;
import android.text.TextUtils;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IGetCategories;
import com.blm.qiubopay.listeners.IGetOrganization;
import com.blm.qiubopay.listeners.IGetRollProducts;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.pedidos.QPAY_GetCategories;
import com.blm.qiubopay.models.pedidos.QPAY_GetCategoriesResponse;
import com.blm.qiubopay.models.pedidos.QPAY_GetOrganization;
import com.blm.qiubopay.models.pedidos.QPAY_GetOrganizationResponse;
import com.blm.qiubopay.models.rolls.QPAY_RollsCostResponse;
import com.blm.qiubopay.modules.Fragment_compra_dongle_1;
import com.blm.qiubopay.modules.Fragment_compra_material_1;
import com.blm.qiubopay.modules.Fragment_compra_rollos_1;
import com.blm.qiubopay.modules.chambitas.Fragment_chambitas_menu;
import com.blm.qiubopay.modules.fincomun.enrolamiento.Fragment_enrolamiento_fincomun_0;
import com.blm.qiubopay.modules.fincomun.originacion.Fragment_fincomun_oferta;
import com.blm.qiubopay.modules.pedidos.Fragment_pedidos_bimbo_1;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import java.util.Arrays;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.interfaces.IFunction;

public class LocalRedirectUtils {

    private HActivity context;

    public LocalRedirectUtils(Context context) {
        this.context = (HActivity) context;
    }

    public void redirectInApp(String linkToGo) {
        if(linkToGo==null){ linkToGo = ""; }
        switch (linkToGo){
            case Globals.F_CHAMBITAS:
                context.setFragment(Fragment_chambitas_menu.newInstance());
                break;
            case Globals.F_FINCOMUN:
                try{
                    ((MenuActivity)context).validatePerfil(new IFunction(){
                        @Override
                        public void execute(Object[] data) {
                            String value = String.valueOf(data[0]);
                            switch (value){
                                case "OFERTA":
                                    if (TextUtils.isEmpty(SessionApp.getInstance().getFcBanderas().getNumCliente())) {
                                        context.setFragment(Fragment_fincomun_oferta.newInstance());
                                    }else {
                                        context.setFragment(Fragment_enrolamiento_fincomun_0.newInstance());
                                    }
                                    break;
                                default:
                                    context.setFragment(Fragment_enrolamiento_fincomun_0.newInstance());
                                    break;
                            }

                        }
                    });
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
                break;
            case Globals.F_COMPRA_ROLLOS:
                getRollProducts(false);
                break;
//            case Globals.F_COMPRA_LECTOR:
//                context.setFragment(Fragment_compra_dongle_1.newInstance());
//                break;
            case Globals.F_COMPRA_MATERIAL:
                context.setFragment(Fragment_compra_material_1.newInstance());
                break;
            case Globals.F_HAZ_TU_PEDIDO:
                //2021-12-15 RSB. App Link unilever es raro que el método llama 2 veces al mismo servicio
                getOrganization(new IFunction<QPAY_GetOrganizationResponse>() {
                    @Override
                    public void execute(QPAY_GetOrganizationResponse... data) {
                        Fragment_pedidos_bimbo_1.organizations = Arrays.asList(data[0].getQpay_object());
                        getCategories(new IFunction<QPAY_GetCategoriesResponse>() {
                            @Override
                            public void execute(QPAY_GetCategoriesResponse... data) {
                                Fragment_pedidos_bimbo_1.list = Arrays.asList(data[0].getQpay_object());
                                if(Fragment_pedidos_bimbo_1.list == null || Fragment_pedidos_bimbo_1.list.isEmpty()){
                                    context.alert("No se encontraron categorias");
                                    return;
                                }
                                //2021-12-21 RSB. Analytics unilever
                                CApplication.setAnalytics(CApplication.ACTION.Market_HazTuPedido);
                                context.setFragment(Fragment_pedidos_bimbo_1.newInstance());
                            }
                        });
                    }
                });
                break;
            default:
                context.alert(R.string.general_error_url);
                break;
        }
    }
    public void redirectInAppFromCarousel(String linkToGo) {
        if(linkToGo==null){ linkToGo = ""; }
        switch (linkToGo){
            case Globals.F_CHAMBITAS:
                context.getSupportFragmentManager().beginTransaction().add(R.id.flFullScreen, Fragment_chambitas_menu.newInstance()).commit();
                break;
            case Globals.F_FINCOMUN:
                try{
                    ((MenuActivity)context).validateVersion(new IFunction() {
                        @Override
                        public void execute(Object[] data) {
                            ((MenuActivity)context).validatePerfil(new IFunction(){
                                @Override
                                public void execute(Object[] data) {
                                    String value = String.valueOf(data[0]);
                                    switch (value){
                                        case "OFERTA":
                                            if (TextUtils.isEmpty(SessionApp.getInstance().getFcBanderas().getNumCliente())) {
                                                context.getSupportFragmentManager().beginTransaction().add(R.id.flFullScreen, Fragment_fincomun_oferta.newInstance()).commit();
                                            }else {
                                                context.getSupportFragmentManager().beginTransaction().add(R.id.flFullScreen, Fragment_enrolamiento_fincomun_0.newInstance()).commit();
                                            }
                                            break;
                                        default:
                                            context.getSupportFragmentManager().beginTransaction().add(R.id.flFullScreen, Fragment_enrolamiento_fincomun_0.newInstance()).commit();
                                            break;
                                    }
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
                break;
            case Globals.F_COMPRA_ROLLOS:
                getRollProducts(true);
                break;
//            case Globals.F_COMPRA_LECTOR:
//                context.getSupportFragmentManager().beginTransaction().add(R.id.flFullScreen, Fragment_compra_dongle_1.newInstance()).commit();
//                break;
            case Globals.F_COMPRA_MATERIAL:
                context.getSupportFragmentManager().beginTransaction().add(R.id.flFullScreen, Fragment_compra_material_1.newInstance()).commit();
                break;
            case Globals.F_HAZ_TU_PEDIDO:
                //2021-12-15 RSB. App Link unilever es raro que el método llama 2 veces al mismo servicio
                getOrganization(new IFunction<QPAY_GetOrganizationResponse>() {
                    @Override
                    public void execute(QPAY_GetOrganizationResponse... data) {
                        Fragment_pedidos_bimbo_1.organizations = Arrays.asList(data[0].getQpay_object());
                        getCategories(new IFunction<QPAY_GetCategoriesResponse>() {
                            @Override
                            public void execute(QPAY_GetCategoriesResponse... data) {
                                Fragment_pedidos_bimbo_1.list = Arrays.asList(data[0].getQpay_object());
                                if(Fragment_pedidos_bimbo_1.list == null || Fragment_pedidos_bimbo_1.list.isEmpty()){
                                    context.alert("No se encontraron categorias");
                                    return;
                                }
                                //2021-12-21 RSB. Analytics unilever
                                CApplication.setAnalytics(CApplication.ACTION.Market_HazTuPedido);
                                context.getSupportFragmentManager().beginTransaction().add(R.id.flFullScreen, Fragment_pedidos_bimbo_1.newInstance()).commit();
                            }
                        });
                    }
                });
                break;
            default:
                if (((MenuActivity)context).fullScreenFragment != null) {
                    ((MenuActivity)context).fullScreenFragment.getContext().getSupportFragmentManager().popBackStack();
                }
                context.alert(R.string.general_error_url);
                break;
        }
    }

    private void getOrganization(IFunction<QPAY_GetOrganizationResponse> function){
        context.loading(true);
        try {
            QPAY_GetOrganization data = new QPAY_GetOrganization();
            data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            IGetOrganization petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {
                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.loading(false);
                        context.alert(R.string.general_error);
                    } else {
                        context.loading(false);
                        String json = new Gson().toJson(result);
                        QPAY_GetOrganizationResponse response = new Gson().fromJson(json, QPAY_GetOrganizationResponse.class);
                        if (response.getQpay_response().equals("true")) {
                            function.execute(response);
                        } else {
                            ((MenuActivity) context).validaSesion(response.getQpay_code(), response.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }
            }, context);
            petition.getOrganization(data);

        } catch (Exception e) {
            e.printStackTrace();
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }
    }

    private void getCategories(IFunction<QPAY_GetCategoriesResponse> function){
        context.loading(true);
        try {
            QPAY_GetCategories data = new QPAY_GetCategories();
            data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            IGetCategories petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {
                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.loading(false);
                        context.alert(R.string.general_error);
                    } else {
                        context.loading(false);
                        String json = new Gson().toJson(result);
                        QPAY_GetCategoriesResponse response = new Gson().fromJson(json, QPAY_GetCategoriesResponse.class);
                        if (response.getQpay_response().equals("true")) {
                            function.execute(response);
                        } else {
                            ((MenuActivity) context).validaSesion(response.getQpay_code(), response.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }
            }, context);
            petition.getCategories(data);
        } catch (Exception e) {
            e.printStackTrace();
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }
    }

    private void getRollProducts(Boolean comesFromCarousel){
        context.loading(true);
        try {
            IGetRollProducts petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {
                    context.loading(false);
                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);
                    } else {
                        //Respuesta correcta
                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_RollsCostResponse.QPAY_RollsCostResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_RollsCostResponse response = gson.fromJson(json, QPAY_RollsCostResponse.class);
                        if(response.getQpay_response().equals("true")){
                            if (comesFromCarousel) {
                                context.getSupportFragmentManager().beginTransaction().add(R.id.flFullScreen, Fragment_compra_rollos_1.newInstance(response)).commit();
                            } else {
                                context.setFragment(Fragment_compra_rollos_1.newInstance(response));
                            }
                        } else {
                            try {
                                ((MenuActivity) context).validaSesion(response.getQpay_code(), response.getQpay_description());
                            } catch (Exception ex) {
                                context.alert(response.getQpay_description());
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
            petition.getRollProduts();
        } catch (Exception e) {
            e.printStackTrace();
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }
    }

}
