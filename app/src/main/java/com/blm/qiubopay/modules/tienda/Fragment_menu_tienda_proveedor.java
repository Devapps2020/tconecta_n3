package com.blm.qiubopay.modules.tienda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.CalcularActivity;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IGetCategories;
import com.blm.qiubopay.listeners.IGetOrganization;
import com.blm.qiubopay.listeners.IGetSalesByRetailerId;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.IPromotions;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.bimbo.PromotionRequest;
import com.blm.qiubopay.models.bimbo.PromotionResponse;
import com.blm.qiubopay.models.nubity.QPAY_SalesRetailer;
import com.blm.qiubopay.models.nubity.QPAY_SalesRetailerResponse;
import com.blm.qiubopay.models.pedidos.QPAY_GetCategories;
import com.blm.qiubopay.models.pedidos.QPAY_GetCategoriesResponse;
import com.blm.qiubopay.models.pedidos.QPAY_GetOrganization;
import com.blm.qiubopay.models.pedidos.QPAY_GetOrganizationResponse;
import com.blm.qiubopay.modules.Fragment_credito_bimbo_1;
import com.blm.qiubopay.modules.Fragment_ofertas_bimbo_1;
import com.blm.qiubopay.modules.Fragment_proveedor_bimbo_1;
import com.blm.qiubopay.modules.home.Fragment_browser;
import com.blm.qiubopay.modules.pedidos.Fragment_pedidos_bimbo_1;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.WSHelper;
import com.google.android.gms.vision.text.Line;
import com.google.gson.Gson;
import com.liveperson.infra.ConversationViewParams;
import com.liveperson.infra.InitLivePersonProperties;
import com.liveperson.infra.auth.LPAuthenticationParams;
import com.liveperson.infra.auth.LPAuthenticationType;
import com.liveperson.infra.callbacks.InitLivePersonCallBack;
import com.liveperson.messaging.sdk.api.LivePerson;
import com.liveperson.messaging.sdk.api.model.ConsumerProfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_menu_tienda_proveedor extends HFragment implements IMenuContext {

    private List<String> marcas = new ArrayList();

    public static Fragment_menu_tienda_proveedor newInstance() {
        return new Fragment_menu_tienda_proveedor();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_menu_tienda_proveedor, container, false), R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView()).showLogo().onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        CardView card_compras = getView().findViewById(R.id.card_compras);
        CardView card_ofertas_promociones = getView().findViewById(R.id.card_ofertas_promociones);
        CardView card_credito_pesito = getView().findViewById(R.id.card_credito_pesito);
        CardView card_proveedores = getView().findViewById(R.id.card_proveedores);
        CardView card_comprar_ricolino = getView().findViewById(R.id.card_comprar_ricolino);
        CardView card_productos = getView().findViewById(R.id.card_productos);


        card_productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrganization(new IFunction<QPAY_GetOrganizationResponse>() {
                    @Override
                    public void execute(QPAY_GetOrganizationResponse... data) {

                        getOrganization(new IFunction<QPAY_GetOrganizationResponse>() {
                            @Override
                            public void execute(QPAY_GetOrganizationResponse... data) {

                                Fragment_pedidos_bimbo_1.organizations = Arrays.asList(data[0].getQpay_object());

                                getCategories(new IFunction<QPAY_GetCategoriesResponse>() {
                                    @Override
                                    public void execute(QPAY_GetCategoriesResponse... data) {
                                        Fragment_pedidos_bimbo_1.list = Arrays.asList(data[0].getQpay_object());
                                        if(Fragment_pedidos_bimbo_1.list == null || Fragment_pedidos_bimbo_1.list.isEmpty()){
                                            getContext().alert("No se encontraron categorias");
                                            return;
                                        }
                                        getContext().setFragment(Fragment_pedidos_bimbo_1.newInstance());
                                    }
                                });
                            }
                        });
                    }
                });

            }
        });


        card_compras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCompras(new IFunction<QPAY_SalesRetailerResponse>() {
                    @Override
                    public void execute(QPAY_SalesRetailerResponse... data) {
                        Fragment_mis_compras.list = Arrays.asList(data[0].getQpay_object());

                        if(Fragment_mis_compras.list == null || Fragment_mis_compras.list.isEmpty()){
                            getContext().alert("No se encontraron compras");
                            return;
                        }

                        getContext().setFragment(Fragment_mis_compras.newInstance());
                    }
                });
            }
        });

        card_ofertas_promociones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPromotions(new IFunction<PromotionResponse>() {
                    @Override
                    public void execute(PromotionResponse[] data) {
                        Fragment_ofertas_bimbo_1.promotionResponse = data[0];

                        if(Fragment_ofertas_bimbo_1.promotionResponse == null || Fragment_ofertas_bimbo_1.promotionResponse.getQpay_object() == null ||  Fragment_ofertas_bimbo_1.promotionResponse.getQpay_object().isEmpty()){
                            getContext().alert("No se encontraron promociones");
                            return;
                        }

                        getContext().setFragment(Fragment_ofertas_bimbo_1.newInstance());
                    }
                });
            }
        });

        card_credito_pesito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContextMenu().getProveedores(new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        getContext().setFragment(Fragment_credito_bimbo_1.newInstance());
                    }
                });
            }
        });

        card_proveedores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContextMenu().getProveedores(new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        getContext().setFragment(Fragment_proveedor_bimbo_1.newInstance());
                    }
                });
            }
        });

        card_comprar_ricolino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LivePerson.initialize(getContext(), new InitLivePersonProperties(Globals.RICOLINO_ID, getContext().getPackageName(), new InitLivePersonCallBack() {
                    @Override
                    public void onInitSucceed() {
                        LPAuthenticationParams lpAuthenticationParams = new LPAuthenticationParams(LPAuthenticationType.SIGN_UP);
                        ConversationViewParams conversationViewParams = new ConversationViewParams();
                        ConsumerProfile consumerProfile = new ConsumerProfile.Builder()
                                .setFirstName(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_name())
                                .setLastName(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_father_surname())
                                .setNickname(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name())
                                .setPhoneNumber(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone())
                                .build();
                        LivePerson.setUserProfile(consumerProfile);
                        LivePerson.showConversation(getContext(), lpAuthenticationParams, conversationViewParams);
                    }
                    @Override
                    public void onInitFailed(Exception e) {
                        getContext().alert(e.getMessage());
                    }
                }));

            }
        });

        LinearLayout layout_comprar_ricolino = getView().findViewById(R.id.layout_comprar_ricolino);

        if(AppPreferences.getUserProfile().getQpay_object()[0].getChatbotEnabled() != null &&
                !AppPreferences.getUserProfile().getQpay_object()[0].getChatbotEnabled().isEmpty() &&
                "1".equals(AppPreferences.getUserProfile().getQpay_object()[0].getChatbotEnabled()))
                    layout_comprar_ricolino.setVisibility(View.VISIBLE);


        if((null != AppPreferences.getUserProfile().getQpay_object()[0].getBo_order() && AppPreferences.getUserProfile().getQpay_object()[0].getBo_order().equals("1"))){
            //2021-12-15 RSB. Ocultar ofertas y promociones e incrementa tus ventas
            card_productos.setVisibility(View.GONE);
        } else {
            card_productos.setVisibility(View.GONE);
        }

        //2021-12-15 RSB. Ocultar ofertas y promociones e incrementa tus ventas
        //card_ofertas_promociones.setVisibility(View.GONE);

    }

    public void getPromotions(IFunction<PromotionResponse> function){

        getContext().loading(true);

        try {

            PromotionRequest promotionRequest = new PromotionRequest();
            promotionRequest.setRetailer_id(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());
            //sellerUserRequest.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IPromotions petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContext().loading(false);

                        String json = new Gson().toJson(result);
                        PromotionResponse response = new Gson().fromJson(json, PromotionResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            function.execute(response);
                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                        getContext().loading(false);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.getPromotions(promotionRequest);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    public void getCompras(IFunction<QPAY_SalesRetailerResponse> function){

        getContext().loading(true);

        try {

            QPAY_SalesRetailer data = new QPAY_SalesRetailer();
            data.setRetailer_id(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());

            IGetSalesByRetailerId petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContext().loading(false);

                        String json = new Gson().toJson(result);
                        QPAY_SalesRetailerResponse response = new Gson().fromJson(json, QPAY_SalesRetailerResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            function.execute(response);
                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                        getContext().loading(false);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.getSalesByRetailerId(data);

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

    public void getCategories(IFunction<QPAY_GetCategoriesResponse> function){

        getContext().loading(true);

        try {

            QPAY_GetCategories data = new QPAY_GetCategories();
            data.getQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IGetCategories petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContext().loading(false);

                        String json = new Gson().toJson(result);
                        QPAY_GetCategoriesResponse response = new Gson().fromJson(json, QPAY_GetCategoriesResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            function.execute(response);
                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                        getContext().loading(false);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.getCategories(data);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    public void getOrganization(IFunction<QPAY_GetOrganizationResponse> function){

        getContext().loading(true);

        try {

            QPAY_GetOrganization data = new QPAY_GetOrganization();
            data.getQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IGetOrganization petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContext().loading(false);

                        String json = new Gson().toJson(result);
                        QPAY_GetOrganizationResponse response = new Gson().fromJson(json, QPAY_GetOrganizationResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            function.execute(response);
                        } else {
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

                        getContext().loading(false);

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    getContext().loading(false);
                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.getOrganization(data);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }



}