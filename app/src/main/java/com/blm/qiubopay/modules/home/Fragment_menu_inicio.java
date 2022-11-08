package com.blm.qiubopay.modules.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.activities.DetailsActivity;
import com.blm.qiubopay.activities.ImageDetailActivity;
import com.blm.qiubopay.activities.LoginActivity;
import com.blm.qiubopay.activities.orders.OrderActivity;
import com.blm.qiubopay.adapters.carousel.CarouselPagerAdapter;
import com.blm.qiubopay.components.CViewSaldo;
import com.blm.qiubopay.components.CompleteDialog;
import com.blm.qiubopay.components.CompleteDialogCallback;
import com.blm.qiubopay.fragments.Dots;
import com.blm.qiubopay.fragments.FullScreenFragment;
import com.blm.qiubopay.helpers.N3Helper;
import com.blm.qiubopay.listeners.IGetCategories;
import com.blm.qiubopay.listeners.IGetOrganization;
import com.blm.qiubopay.models.QPAY_UserProfile;
import com.blm.qiubopay.models.base.BaseResponse;
import com.blm.qiubopay.models.chambitas.campañas.CampaignsActiveNewCount;
import com.blm.qiubopay.models.order.OrderNotification;
import com.blm.qiubopay.models.publicity.CampaignsActiveCount;
import com.blm.qiubopay.models.publicity.QPAY_TipsAdvertising_object;
import com.blm.qiubopay.models.publicity.QPAY_TipsAdvertising_publicities;
import com.blm.qiubopay.models.transactions.TransactionsModel;
import com.blm.qiubopay.modules.Fragment_notificaciones_1;
import com.blm.qiubopay.modules.campania.Fragment_repo_tips;
import com.blm.qiubopay.modules.campania.Fragment_tips;
import com.blm.qiubopay.models.carousel.CarouselData;
import com.blm.qiubopay.models.carousel.FullScreenData;
import com.blm.qiubopay.models.carousel.PublicityResponse;
import com.blm.qiubopay.models.carousel.QPayBaseResponse;
import com.blm.qiubopay.modules.chambitas.Fragment_chambitas_menu;
import com.blm.qiubopay.models.pedidos.QPAY_GetCategories;
import com.blm.qiubopay.models.pedidos.QPAY_GetCategoriesResponse;
import com.blm.qiubopay.models.pedidos.QPAY_GetOrganization;
import com.blm.qiubopay.models.pedidos.QPAY_GetOrganizationResponse;
import com.blm.qiubopay.modules.apuestas.Fragment_menu_apuestas;
import com.blm.qiubopay.modules.fiado.Fragment_fiado_3;
import com.blm.qiubopay.modules.fiado.Fragment_fiado_6;
import com.blm.qiubopay.modules.home.viewmodels.MenuInicioVM;
import com.blm.qiubopay.modules.pedidos.Fragment_pedidos_bimbo_1;
import com.blm.qiubopay.modules.fincomun.enrolamiento.Fragment_enrolamiento_fincomun_0;
import com.blm.qiubopay.modules.fincomun.originacion.Fragment_fincomun_oferta;
import com.blm.qiubopay.modules.reportes.Fragment_menu_reportes_detalle;
import com.blm.qiubopay.modules.tienda.Fragment_menu_tienda;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.DateUtils;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.ICreateUserSaving;
import com.blm.qiubopay.listeners.IDeleteUserSaving;
import com.blm.qiubopay.listeners.IGetUserSaving;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.IUpdateUserSaving;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.ahorros.QPAY_CreateUserSaving;
import com.blm.qiubopay.models.ahorros.QPAY_CreateUsingSavingResponse;
import com.blm.qiubopay.models.ahorros.QPAY_DeleteUserSavingResponse;
import com.blm.qiubopay.models.ahorros.QPAY_GetUserSavingResponse;
import com.blm.qiubopay.models.ahorros.QPAY_UpdateUserSavingResponse;
import com.blm.qiubopay.models.pagos_qiubo.QPAY_QiuboPaymentItem;
import com.blm.qiubopay.models.questions.QPAY_DynamicQuestions_answers;
import com.blm.qiubopay.modules.Fragment_credito_bimbo_1;
import com.blm.qiubopay.modules.Fragment_depositos_1;
import com.blm.qiubopay.modules.perfil.Fragment_cuenta_1;
import com.blm.qiubopay.modules.recarga.Fragment_pago_recarga_menu;
import com.blm.qiubopay.modules.regional.Fragment_pago_regional_menu;
import com.blm.qiubopay.modules.servicio.Fragment_pago_servicio_menu;
import com.blm.qiubopay.utils.WSHelper;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_menu_inicio extends HFragment implements IMenuContext {

    private final String TAG = "Fragment_menu_inicio";
    private QPAY_DynamicQuestions_answers result;

    // Carousel section
    public CarouselPagerAdapter carouselAdapter;
    public ViewPager carouselPager;
    private RelativeLayout rlCarousel;
    private LinearLayout llStaticCarousel;
    private ImageView ivStaticCarousel;
    private RelativeLayout rlDots;
    private int counter = 0;
    private Dots dots;
    private ArrayList<PublicityResponse> publicityResponses = new ArrayList<>();
    private boolean isTimerRunning = false;
    private ArrayList<CarouselData> carouselDatas = new ArrayList<>();
    // -------------------------------------------

    private MenuInicioVM viewModel;

    MenuAdapter adapter;
    RecyclerView recyclerView;
    public static Fragment_menu_inicio newInstance() {
        return new Fragment_menu_inicio();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_menu_inicio, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new MenuInicioVM(getContext());
        init();
        setListeners();
        getData();
        setObservers();
    }

    @Override
    public void onResume() {
        super.onResume();
        int campaignId = AppPreferences.readCampaignId(getContext());
        ArrayList<OrderNotification> orders = AppPreferences.readNewOrders();
        if (campaignId > 0) {
            viewModel.getCampaignById(campaignId + "");
            AppPreferences.saveCampaignId(-1);
        }
        if (orders.size() > 0) {
            AppPreferences.removeOrders();
            Intent intent = new Intent(getContext(), OrderActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("orderId", orders.get(0).getNewOrderId());
            bundle.putString("orderType", orders.get(0).getOrderType());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void init() {
        carouselPager = (ViewPager) getContext().findViewById(R.id.myviewpager);
        llStaticCarousel = getContext().findViewById(R.id.llStaticCarousel);
        ivStaticCarousel = getContext().findViewById(R.id.ivStaticCarousel);
        rlCarousel = getContext().findViewById(R.id.rlCarousel);
        rlDots = getContext().findViewById(R.id.rlDots);
        rlCarousel.setVisibility(View.GONE);
        rlDots.setVisibility(View.GONE);
    }

    private void setListeners() {
        ivStaticCarousel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToFullScreen(0);
            }
        });
    }

    private void sendDataToFullScreen(QPayBaseResponse qPayBaseResponse) {
        Fragment_tips.setContinue(new IFunction() {
            @Override
            public void execute(Object[] data) {
                getContext().backFragment();
            }
        });
        if (qPayBaseResponse.getQpayObject() != null) {
            QPAY_TipsAdvertising_object campaignNew;
            //Creamos una campaña y asignamos valores
            campaignNew = new QPAY_TipsAdvertising_object();
            campaignNew.setId(qPayBaseResponse.getQpayObject().get(0).getId());
            campaignNew.setName(qPayBaseResponse.getQpayObject().get(0).getName());
            campaignNew.setActivation_date(qPayBaseResponse.getQpayObject().get(0).getActivationDate());
            campaignNew.setExpiration_date(qPayBaseResponse.getQpayObject().get(0).getExpirationDate());
            campaignNew.setStatus(qPayBaseResponse.getQpayObject().get(0).getStatus());
            //Se coloca solo esa publicidad dentro de la campaña
            if (qPayBaseResponse.getQpayObject().get(0).getPublicities() != null) {
                QPAY_TipsAdvertising_publicities publicity = new QPAY_TipsAdvertising_publicities(
                        qPayBaseResponse.getQpayObject().get(0).getPublicities().get(0).getId(),
                        qPayBaseResponse.getQpayObject().get(0).getPublicities().get(0).getTitle(),
                        qPayBaseResponse.getQpayObject().get(0).getPublicities().get(0).getImage(),
                        qPayBaseResponse.getQpayObject().get(0).getPublicities().get(0).getCarrouselImage(),
                        qPayBaseResponse.getQpayObject().get(0).getPublicities().get(0).getButtonText(),
                        qPayBaseResponse.getQpayObject().get(0).getPublicities().get(0).getDescription(),
                        qPayBaseResponse.getQpayObject().get(0).getPublicities().get(0).getLink(),
                        qPayBaseResponse.getQpayObject().get(0).getPublicities().get(0).getCreationDate(),
                        qPayBaseResponse.getQpayObject().get(0).getPublicities().get(0).getCompanyId(),
                        null);
                QPAY_TipsAdvertising_publicities publicities[] = { publicity };
                campaignNew.setPublicities(publicities);
                //Se asigna la campaña
                Fragment_tips.setData(campaignNew);
                getContext().setFragment(Fragment_tips.newInstance());
            }
        }
    }

    private void sendDataToFullScreen(int position) {
        String title = publicityResponses.get(position).getTitle();
        String description = publicityResponses.get(position).getDescription();
        String image = publicityResponses.get(position).getImage();
        String linkText = publicityResponses.get(position).getLink();
        String textButton = publicityResponses.get(position).getButtonText();
        Integer campaignId = publicityResponses.get(position).getCampaignId();
        FullScreenData fullScreenData = new FullScreenData(title, description, image, linkText, textButton, campaignId);
//        getContext().setFragment(FullScreenFragment.newInstance(fullScreenData, getContext()));
        getContextMenu().fullScreenFragment = FullScreenFragment.newInstance(fullScreenData, getContext());
        getContext().setFragment(getContextMenu().fullScreenFragment);
    }

    private void getData() {
        String blmId = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id();
        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();

        if (blmId != null && !blmId.isEmpty()) {
            crashlytics.setCustomKey("user", blmId);
            crashlytics.setUserId(blmId);
        }

        DateUtils dateUtils = new DateUtils(getContext());
        ArrayList<Integer> date = AppPreferences.readDate(getContext());
        Boolean areDateEquals = dateUtils.areDatesEquals(date.get(0), date.get(1), date.get(2));
        if (!areDateEquals) {
            getContext().loading(true);
            getContextMenu().setCountChambitas(true);
            getContextMenu().setCountPublicity(true);
            //TODO TRANSACTION COUNTER. OTHERS TRANSACTION SUM TOTAL
            TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
            transactionsModel.getRsTransactions().setTotales(transactionsModel.getRsTransactions().getTotales() + 1);
            AppPreferences.setTodayTransactions(transactionsModel);
            viewModel.getCarrouselCampaigns();
            AppPreferences.saveTodayDate(dateUtils.getTodayDate());
        } else {
            prepareCarouselData();
        }
    }

    private void setObservers() {
        viewModel._carouselResponse.observe(getViewLifecycleOwner(), new Observer<QPayBaseResponse>() {
            @Override
            public void onChanged(QPayBaseResponse qPayBaseResponse) {
                if (qPayBaseResponse.getQpayObject() != null) {
                    if (!qPayBaseResponse.getQpayObject().isEmpty()) {
                        rlCarousel.setVisibility(View.VISIBLE);
                        rlDots.setVisibility(View.VISIBLE);
                        prepareCarouselData();
                    }
                }
            }
        });
        viewModel._campaingnResponse.observe(getViewLifecycleOwner(), new Observer<QPayBaseResponse>() {
            @Override
            public void onChanged(QPayBaseResponse qPayBaseResponse) {
                if (qPayBaseResponse.getQpayObject() != null && !qPayBaseResponse.getQpayObject().isEmpty()) {
                    sendDataToFullScreen(qPayBaseResponse);
                }
            }
        });
    }

    private void prepareCarouselData() {
        publicityResponses = new ArrayList<>();
        ArrayList<PublicityResponse> data = AppPreferences.readPublicity(getContext());
        if (data != null) {
            if (!data.isEmpty()) {
                rlCarousel.setVisibility(View.VISIBLE);
                rlDots.setVisibility(View.VISIBLE);
                for (int i = 0; i < data.size(); i++) {
                    if (!data.get(i).getCarrouselImage().isEmpty()) {
                        publicityResponses.add(data.get(i));
                    }
                }
                showPublicity();
            }
        }
    }

    private void showPublicity() {
        if (publicityResponses.size() == 1) {
            carouselPager.setVisibility(View.INVISIBLE);
            rlDots.setVisibility(View.GONE);
            Glide.with(getContext())
                    .load(publicityResponses.get(0).getCarrouselImage())
                    .centerInside()
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(6)))
                    .into(ivStaticCarousel);  // imageview object
        } else {
            llStaticCarousel.setVisibility(View.INVISIBLE);
            carouselReady();
        }
    }

    private void carouselReady() {
        try {
            loadCarousel();
            loadCarouselAdapter();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void loadCarouselAdapter() {
        dots.numberOfItems(publicityResponses.size(), getContext());
        dots.setDotColor();
        carouselAdapter.setTimer(Globals.SCROLLING_TIME);
    }

    private void loadCarousel() {
        getContext().getFragmentManager().beginTransaction().add(R.id.content, dots = new Dots()).commit();

        //set page margin between pages for viewpager
        DisplayMetrics metrics = new DisplayMetrics();
        getContext().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int pageMargin = (metrics.widthPixels / 10) * 3;
//        int pageMargin = (metrics.widthPixels / 2);
        carouselPager.setPageMargin(-pageMargin);
        carouselAdapter = new CarouselPagerAdapter(getContextMenu(), getContextMenu().getSupportFragmentManager(), publicityResponses.size(), carouselPager, ImageDetailActivity.class,
                DetailsActivity.class, publicityResponses, carouselDatas);
        carouselPager.setAdapter(carouselAdapter);
        carouselAdapter.notifyDataSetChanged();
        carouselPager.addOnPageChangeListener(carouselAdapter);
        carouselPager.addOnPageChangeListener(listener);

        // Set current item to the middle page so we can fling to both
        // directions left and right
        carouselPager.setCurrentItem(publicityResponses.size()/*FIRST_PAGE*/);
        carouselPager.setOffscreenPageLimit(3);
    }

    ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (counter > 0) {
                dots.refreshDots(position);
            }
            counter++;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };

    @Override
    public void initFragment() {

        Fragment_menu_reportes_detalle.nullObjects();

        if(!AppPreferences.isPinRegistered()){
            CApplication.generateNewFCMToken();
            AppPreferences.Logout(getContext());
            getContext().startActivity(LoginActivity.class, true);
        }

        CViewMenuTop.create(getView())
                .showTitle("Bienvenid@ <b>" + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_name() + "</b>")
                .showLogoStart()
                .onClickUser(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContext().setFragment(Fragment_cuenta_1.newInstance());
                    }
                });

        CViewSaldo.create(getContextMenu(), getView(), false);
        CViewSaldo.isSaldoHidden = false;

        final LinearLayout layout_mi_saldo = getView().findViewById(R.id.layout_mi_saldo);
        layout_mi_saldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContextMenu().cargarSaldo(true, false, false, new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        getContextMenu().initHome();
                    }
                });
            }
        });
        /*layout_mi_saldo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });*/

        final GridLayoutManager manager = new GridLayoutManager(getContext(), 2);

        recyclerView = getView().findViewById(R.id.recycler_view_items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);

        adapter = new MenuAdapter(configMenu());
        recyclerView.setAdapter(adapter);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.getItemViewType(position) ==  R.layout.item_servicio_conecta ? 1 : manager.getSpanCount();
            }
        });

        getContextMenu().setNotificacions();

        getContextMenu().setNotificacions(getView().findViewById(R.id.numero_notification));

        //Si la seccion llego de mano de una notificacion cuando se estaba fuera del app
        if(AppPreferences.getAppLink()!=null && !AppPreferences.getAppLink().isEmpty()){
            MenuActivity.appLink = AppPreferences.getAppLink();
            AppPreferences.setAppLink(null);
        }

        //RSB 211015. Applink
        //Si hay
        if(MenuActivity.appLink!=null && !MenuActivity.appLink.isEmpty()){
            gotoAppLink(MenuActivity.appLink.trim().toLowerCase());
        } else {
            //Si no hay redireccion a otro modulo valida hacer login telemétrico y saldo vas agotandose
            getContextMenu().validateTelemetryLogin();
            checkVas();
        }

        clear();
    }


    //RSB 211015. Applink
    private void gotoAppLink(String link){

        switch (link){
            case Globals.F_CHAMBITAS:
                getContext().setFragment(Fragment_chambitas_menu.newInstance());
                break;
            case Globals.F_NOTIFICACIONES:
                getContext().setFragment(Fragment_notificaciones_1.newInstance());
                break;
        }

        MenuActivity.appLink=null;
    }


    private void checkVas(){
        Tools.initialVasInfo();

        //Se checa que haya actualizado el saldo.

        if(AppPreferences.getFinancialVasInfo().getUpdateBalanceFlag().equals("1")
                && !AppPreferences.isCashier()
                && !Tools.userIsOnlyVAS()
                && !Tools.isFinancialVasUsedToday()
                && Tools.isNeedToOfferFinancialVasWithTheNewBalance()
                && AppPreferences.isVASFinancieroActive()) {  //20200813 RSB. Validar VAS con Financiero

            getContext().alert(R.string.alert_message_financial_vas, new IAlertButton() {
                @Override
                public String onText() {
                    return "Si";
                }

                @Override
                public void onClick() {

                    Tools.setVasInfo(false,false, -1.0, false);

                    //context.clearFragment();
                    getContext().setFragment(Fragment_depositos_1.newInstance(true));

                }
            }, new IAlertButton() {
                @Override
                public String onText() {
                    return "No";
                }

                @Override
                public void onClick() {

                    Tools.setVasInfo(false,false, -1.0, false);

                }
            });
        }
    }

    public List<MenuoDTO> configMenu() {

        List<MenuoDTO> result = new ArrayList();

        String apuestas = AppPreferences.getUserPrivileges() != null ? (AppPreferences.getUserPrivileges().getJazzDeportes() != null ? AppPreferences.getUserPrivileges().getJazzDeportes().trim() : "0") : "0";

        //result.add(new MenuoDTO("999", "", "", R.drawable.tiempo_aire,R.layout.item_text));
        result.add(new MenuoDTO("6", "Venta Tiempo Aire", "", R.drawable.tiempo_aire,R.layout.item_servicio_conecta));
        result.add(new MenuoDTO("9", "Pago de Servicios", "", R.drawable.tiempo_servicios,R.layout.item_servicio_conecta));
        result.add(new MenuoDTO("0", "Cobrar con Tarjeta", "Cobra con tarjeta de\nforma rápida y segura", R.drawable.illustrations_operaciones_con_tarjeta_100_x_100,R.layout.item_servicio_conecta));
        result.add(new MenuoDTO("1", "Abonar Saldo", "", R.drawable.illustrations_cargar_saldo_100_x_100,R.layout.item_servicio_conecta));

        result.add(new MenuoDTO("17", "¡Responde y Gana!", "", R.drawable.illustration_chambitas,R.layout.item_servicio_conecta));
        result.add(new MenuoDTO("18", "Publicidad y Promociones", "", R.drawable.illustration_publicidad_y_promociones,R.layout.item_servicio_conecta));

        if((null != AppPreferences.getUserProfile().getQpay_object()[0].getB2c_activation() && AppPreferences.getUserProfile().getQpay_object()[0].getB2c_activation().equals("1"))) {
            result.add(new MenuoDTO("19", "Pedidos de mis clientes", "", R.drawable.illustrations_mis_compras_56_x_56,R.layout.item_servicio_conecta));
        }

        if((null != AppPreferences.getUserProfile().getQpay_object()[0].getBo_order() && AppPreferences.getUserProfile().getQpay_object()[0].getBo_order().equals("1")))
            result.add(new MenuoDTO("16", "Haz tu Pedido", "", R.drawable.comprar_2,R.layout.item_servicio_conecta));


        result.add(new MenuoDTO("2", "Préstamo personal", "Hasta $16,000 en efectivo", R.drawable.illustrations_cr_dito_personal_56_x_56,R.layout.item_menu_fincomun));        //result.add(new MenuoDTO("11", "", "Adquiere tu\nLector de Tarjetas", R.drawable.illustrations_contratar_dispositivo_100_x_100,R.layout.item_servicio_conecta_2));

        //result.add(new MenuoDTO("3", "Mi crédito Pesito", "Consulta el estado de\ntu crédito", R.drawable.illustrations_cr_dito_pesito_115_x_95,R.layout.item_servicio_conecta));
        //result.add(new MenuoDTO("4", "Retiro de Efectivo", "Ofrece retiros de efectivo\nde forma sencilla", R.drawable.illustrations_retirada_efectivo_100_x_100,R.layout.item_servicio_conecta));
        //result.add(new MenuoDTO("5", "", "Pide tu\nCrédito\nPesito", R.drawable.illustrations_cr_dito_pesito_115_x_95,R.layout.item_servicio_conecta_2));

        if("1".equals(apuestas) || (null != AppPreferences.getUserProfile().getQpay_object()[0].getQpay_activation_jazz_deportes() && AppPreferences.getUserProfile().getQpay_object()[0].getQpay_activation_jazz_deportes().equals("1")))
            result.add(new MenuoDTO("15", "", "Juegos", R.drawable.ic_apuestas_deportivas,R.layout.item_servicio_conecta_2));

        //result.add(new MenuoDTO("8", "", "¡Logra todos\ntus sueños!", R.drawable.icons_total_dinero_56_x_56,R.layout.item_servicio_conecta_2));
        //result.add(new MenuoDTO("14", "Consultar Ofertas", "Descubre nuestras\nofertas", R.drawable.illustrations_mi_tienda_100_x_100,R.layout.item_servicio_conecta));
        //result.add(new MenuoDTO("7", "Pagos Regionales", "Realiza el pago de los servicios de\ntu región", R.drawable.illustrations_pagos_regionales_100_x_100,R.layout.item_servicio_conecta));
        //result.add(new MenuoDTO("10", "Cancela operaciones", "Cancela operaciones", R.drawable.illustrations_cancelaci_n_pagos_100_x_100,R.layout.item_servicio_conecta));

        if(AppPreferences.getCodiRegister()) {
            //result.add(new MenuoDTO("12", "Pagos con QR", "Pagos CODI", R.drawable.illustrations_codi_100_x_100,R.layout.item_servicio_conecta));
            //result.add(new MenuoDTO("13", "Cobros con QR", "Cobros CODI", R.drawable.illustrations_codi_100_x_100,R.layout.item_servicio_conecta));
        }

        return result;
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }



    public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ItemViewHolder> {


        private List<MenuoDTO> items;

        public MenuAdapter(List<MenuoDTO> items) {
            this.items = items;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(viewType, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ItemViewHolder holder, final int position) {
            MenuoDTO item = items.get(position);

            if(holder.txt_title != null)
                holder.txt_title.setText(item.getName());

            if(holder.text_description != null)
                holder.text_description.setText(item.getDescription());

            if (holder.img_item != null)
                holder.img_item.setImageDrawable(getContext().getResources().getDrawable(item.getImage()));


            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    directAccess(Integer.parseInt(item.getId()));
                }
            });

            //2021-12-14 RSB. Notificacion chambitas nuevas
            if(item.getName().contains("¡Responde y Gana!")){
                if(getContextMenu().isCountChambitas()){
                    if (AppPreferences.isTooltipChambitasActive()) {
                        QPAY_UserProfile user = AppPreferences.getUserProfile();
                        user.getQpay_object()[0].setTooltip_chambitas("0");
                        AppPreferences.setUserProfile(user);
                        CompleteDialog dialog = new CompleteDialog(new CompleteDialogCallback() {
                            @Override
                            public void closeButton(Dialog dialog) {
                                dialog.dismiss();
                            }
                        },false);

                        dialog.show(requireActivity().getFragmentManager(),"dialog");

                    }
                    viewModel.getChambitasCount();
                    viewModel._chambitasResponse.observe(getViewLifecycleOwner(), new Observer<BaseResponse<CampaignsActiveNewCount>>() {
                        @Override
                        public void onChanged(BaseResponse<CampaignsActiveNewCount> campaignsActiveNewCountBaseResponse) {
                            getContextMenu().setCountChambitas(false);

                            if (campaignsActiveNewCountBaseResponse.getQpayResponse().equals("true")) {
                                if (campaignsActiveNewCountBaseResponse.getQpayObject() != null) {
                                    String chambitasCount = campaignsActiveNewCountBaseResponse.getQpayObject().get(0).getActiveCampaigns();
                                    String chambitasCompletedCount = campaignsActiveNewCountBaseResponse.getQpayObject().get(0).getCompleteCampaigns();
                                    String chambitasCurrentCount = campaignsActiveNewCountBaseResponse.getQpayObject().get(0).getCurrentCampaigns();
                                        if(chambitasCount != null && !chambitasCount.isEmpty() && chambitasCompletedCount != null && !chambitasCompletedCount.isEmpty() &&  chambitasCurrentCount != null && !chambitasCurrentCount.isEmpty()){

                                            AppPreferences.setChambitasCounter(chambitasCount);
                                            AppPreferences.setChambitasCompletedCounter(chambitasCompletedCount);
                                            AppPreferences.setChambitasCurrentCounter(chambitasCurrentCount);
                                            holder.iv_bell.setVisibility(!chambitasCount.equals("0") ? View.VISIBLE : View.GONE);
                                            holder.text_notifications.setVisibility(!chambitasCount.equals("0") ? View.VISIBLE : View.GONE);
                                            holder.text_notifications.setText(!chambitasCount.equals("0") ? chambitasCount : "0");
                                        }
                                } else {
                                    AppPreferences.setChambitasCounter("");
                                    AppPreferences.setChambitasCompletedCounter("");
                                    AppPreferences.setChambitasCurrentCounter("");
                                    holder.text_notifications.setVisibility(View.GONE);
                                    holder.iv_bell.setVisibility(View.GONE);
                                }
                            } else {
                                AppPreferences.setChambitasCounter("");
                                AppPreferences.setChambitasCompletedCounter("");
                                AppPreferences.setChambitasCurrentCounter("");
                                holder.text_notifications.setVisibility(View.GONE);
                                holder.iv_bell.setVisibility(View.GONE);
                                Logger.e("Error en QTC para obtener las chambitas: " + campaignsActiveNewCountBaseResponse.getQpayDescription());
                            }
                        }
                    });

                } else if(!AppPreferences.getChambitasCounter().isEmpty()) {
                    //Si no estaba habilitada la consulta pinta lo que tiene en preference siempre y que haya un valor para pintar
                    holder.iv_bell.setVisibility(!AppPreferences.getChambitasCounter().equals("0") ? View.VISIBLE : View.GONE);
                    holder.text_notifications.setVisibility(!AppPreferences.getChambitasCounter().equals("0") ? View.VISIBLE : View.GONE);
                    holder.text_notifications.setText(!AppPreferences.getChambitasCounter().equals("0") ? AppPreferences.getChambitasCounter() : "0");
                }
            }

            //2021-12-14 RSB. Notificacion nueva publicidad
            if(item.getName().contains("Publicidad y Promociones")){
                if(getContextMenu().isCountPublicity()){
                    viewModel.getPublicidadCount();
                    viewModel._publicityResponse.observe(getViewLifecycleOwner(), new Observer<BaseResponse<CampaignsActiveCount>>() {
                        @Override
                        public void onChanged(BaseResponse<CampaignsActiveCount> campaignsActiveCountBaseResponse) {
                            getContextMenu().setCountPublicity(false);
                            holder.text_notifications.setVisibility(View.VISIBLE);
                            holder.iv_bell.setVisibility(View.VISIBLE);
                            if (campaignsActiveCountBaseResponse.getQpayResponse().equals("true")) {
                                if (campaignsActiveCountBaseResponse.getQpayObject() != null) {
                                    String publicidadCount = campaignsActiveCountBaseResponse.getQpayObject().get(0).getActivePublicities();
                                    if(publicidadCount != null && !publicidadCount.isEmpty()){
                                        AppPreferences.setPublicidadCounter(publicidadCount);
                                        holder.text_notifications.setText(publicidadCount);
                                    }
                                } else {
                                    AppPreferences.setPublicidadCounter("");
                                    holder.text_notifications.setVisibility(View.GONE);
                                    holder.iv_bell.setVisibility(View.GONE);
//                                    holder.text_notifications.setText(AppPreferences.getPublicidadCounter());
                                }
                            } else {
                                AppPreferences.setPublicidadCounter("");
                                holder.text_notifications.setVisibility(View.GONE);
                                holder.iv_bell.setVisibility(View.GONE);
                                Logger.e("Error en QTC para obtener las campañas: " + campaignsActiveCountBaseResponse.getQpayDescription());
                            }
                        }
                    });

                    //Valida si esta habilitada la consulta de chambitas, de ser asi consume servicio
//                    getContextMenu().getCountPublicidad(new IFunction() {
//                        @Override
//                        public void execute(Object[] data) {
//                            holder.text_notifications.setVisibility(View.VISIBLE);
//                            holder.iv_bell.setVisibility(View.VISIBLE);
//                            holder.text_notifications.setText(data[0].toString());
//                        }
//                    }, new IFunction() {
//                        @Override
//                        public void execute(Object[] data) {
//                            if(!AppPreferences.getPublicidadCounter().isEmpty()) {
//                                //Si no estaba habilitada la consulta pinta lo que tiene en preference siempre y que haya un valor para pintar
//                                holder.text_notifications.setVisibility(View.VISIBLE);
//                                holder.iv_bell.setVisibility(View.VISIBLE);
//                                holder.text_notifications.setText(AppPreferences.getPublicidadCounter());
//                            }
//                        }
//                    });
                } else if(!AppPreferences.getPublicidadCounter().isEmpty()) {
                    //Si no estaba habilitada la consulta pinta lo que tiene en preference siempre y que haya un valor para pintar
                    holder.text_notifications.setVisibility(View.VISIBLE);
                    holder.iv_bell.setVisibility(View.VISIBLE);
                    holder.text_notifications.setText(AppPreferences.getPublicidadCounter());
                }
            }

        }

        @Override
        public int getItemViewType(int position) {
            return items.get(position).getLayout();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public List<MenuoDTO> getItems(){
            return items;
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            public View view;
            public TextView txt_title;
            public TextView text_description;
            public ImageView img_item;
            public ImageView iv_bell;
            public TextView text_notifications;

            public ItemViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                txt_title = itemView.findViewById(R.id.txt_title);
                text_description = itemView.findViewById(R.id.text_description);
                img_item = itemView.findViewById(R.id.img_item);
                iv_bell = itemView.findViewById(R.id.bell_notifications);
                text_notifications = itemView.findViewById(R.id.text_notifications);
            }
        }
    }

    public class MenuoDTO {

        private String id;
        private String title;
        private String description;
        private String description2;
        private Integer image;
        private Integer layout;

        public MenuoDTO(String id, String name, String description, Integer image,Integer layout) {
            this.id = id;
            this.title = name;
            this.description = description;
            this.image = image;
            this.layout = layout;

        }

        public MenuoDTO(Integer layout) {
            this.layout = layout;
        }

        public String getName() {
            return title;
        }

        public void setName(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription2() {
            return description2;
        }

        public void setDescription2(String description2) {
            this.description2 = description2;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getImage() {
            return image;
        }

        public void setImage(Integer image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getLayout() {
            return layout;
        }

        public void setLayout(Integer layout) {
            this.layout = layout;
        }
    }


    public void directAccess(int option) {

        switch(option) {
            case 0:
                getContext().setFragment(Fragment_menu_pagos_financieros.newInstance());
                break;
            case 1:
                getContext().setFragment(Fragment_depositos_1.newInstance());
                break;
            case 2:
                validateData();
                break;
            case 3:
            case 5:
                getContextMenu().getProveedores(new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        getContext().setFragment(Fragment_credito_bimbo_1.newInstance());
                    }
                });
                break;
            case 4:
                getContextMenu().servicioFinanciero(2);
                break;
            case 6:
                getContext().setFragment(Fragment_pago_recarga_menu.newInstance());
                break;
            case 7:
                getContextMenu().getQiuboPaymentList(new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        Fragment_pago_regional_menu.proveedores = (List<QPAY_QiuboPaymentItem>) data[0];
                        getContext().setFragment(Fragment_pago_regional_menu.newInstance());
                    }
                });
                break;
            case 8:
                getContextMenu().validatePerfil(null);
                break;
            case 9:
                getContext().setFragment(Fragment_pago_servicio_menu.newInstance());
                break;
            case 10:
                getContextMenu().servicioFinanciero(0);
                break;
            case 11:
                getContextMenu().comprarDongle();
                break;
            case 12:
                getContextMenu().validatePerfil(null);
                break;
            case 13:
                getContextMenu().validatePerfil(null);
                break;
            case 14:
                getContextMenu().validaBimboId(new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        getContext().setFragment(Fragment_menu_tienda.newInstance());
                    }
                });
                break;
            case 15:
                getContext().setFragment(Fragment_menu_apuestas.newInstance());
                break;
            case 16:
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
                                //2021-12-21 RSB. Analytics unilever
                                CApplication.setAnalytics(CApplication.ACTION.Market_HazTuPedido);
                                getContext().setFragment(Fragment_pedidos_bimbo_1.newInstance());
                            }
                        });
                    }
                });
                break;
            case 17:
                getContext().setFragment(Fragment_chambitas_menu.newInstance());
                break;
            case 18:
                getContext().setFragment(Fragment_repo_tips.newInstance());
                break;
            case 19:
                startActivity(new Intent(getContext(), OrderActivity.class));
                break;
            default:

        }
    }

    public void getCategories(IFunction<QPAY_GetCategoriesResponse> function){

        getContext().loading(true);

        try {

            QPAY_GetCategories data = new QPAY_GetCategories();
            data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IGetCategories petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContextMenu().loading(false);

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
            data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IGetOrganization petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContextMenu().loading(false);

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

    private void validateData() {
        getContextMenu().validateVersion(new IFunction() {
            @Override
            public void execute(Object[] data) {
                getContextMenu().validatePerfil(new IFunction(){
                    @Override
                    public void execute(Object[] data) {
                        String value = String.valueOf(data[0]);
                        switch (value){
                            case "OFERTA":
                                if (TextUtils.isEmpty(SessionApp.getInstance().getFcBanderas().getNumCliente())) {
                                    getContextMenu().setFragment(Fragment_fincomun_oferta.newInstance());
                                }else {
                                    getContextMenu().setFragment(Fragment_enrolamiento_fincomun_0.newInstance());
                                }
                                break;
                            default:
                                getContextMenu().setFragment(Fragment_enrolamiento_fincomun_0.newInstance());
                                break;
                        }

                    }
                });
            }
        });
    }

    public void createUserSaving(String savingPercentage,String newPercentageDate, IFunction<QPAY_CreateUsingSavingResponse> function){

        getContext().loading(true);

        try {

            QPAY_CreateUserSaving data = new QPAY_CreateUserSaving();
            data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            data.setNewPercentageDate(newPercentageDate);
            data.setSavingPercentage(savingPercentage);

            ICreateUserSaving petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContextMenu().loading(false);

                        String json = new Gson().toJson(result);
                        QPAY_CreateUsingSavingResponse response = new Gson().fromJson(json, QPAY_CreateUsingSavingResponse.class);

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

            petition.createUserSaving(data);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    public void updateUserSaving(String savingPercentage,String newPercentageDate, IFunction<QPAY_UpdateUserSavingResponse> function){

        getContext().loading(true);

        try {

            QPAY_CreateUserSaving data = new QPAY_CreateUserSaving();
            data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            data.setNewPercentageDate(newPercentageDate);
            data.setSavingPercentage(savingPercentage);

            IUpdateUserSaving petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContextMenu().loading(false);

                        String json = new Gson().toJson(result);
                        QPAY_UpdateUserSavingResponse response = new Gson().fromJson(json, QPAY_UpdateUserSavingResponse.class);

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

            petition.updateUserSaving(data);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    public void getUserSaving(String savingPercentage,String newPercentageDate, IFunction<QPAY_GetUserSavingResponse> function){

        getContext().loading(true);

        try {

            QPAY_CreateUserSaving data = new QPAY_CreateUserSaving();
            data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            data.setNewPercentageDate(newPercentageDate);
            data.setSavingPercentage(savingPercentage);

            IGetUserSaving petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContextMenu().loading(false);

                        String json = new Gson().toJson(result);
                        QPAY_GetUserSavingResponse response = new Gson().fromJson(json, QPAY_GetUserSavingResponse.class);

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

            petition.getUserSaving(data);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    public void deleteUserSaving(IFunction<QPAY_DeleteUserSavingResponse> function){

        getContext().loading(true);

        try {

            QPAY_CreateUserSaving data = new QPAY_CreateUserSaving();
            data.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IDeleteUserSaving petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                        getContext().loading(false);

                    } else {

                        getContextMenu().loading(false);

                        String json = new Gson().toJson(result);
                        QPAY_DeleteUserSavingResponse response = new Gson().fromJson(json, QPAY_DeleteUserSavingResponse.class);

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

            petition.deleteUserSaving(data);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    public void clear() {

        Fragment_fiado_3.list = null;
        Fragment_fiado_3.selection =null;
        Fragment_fiado_6.fiado =null;
        getContextMenu().pagoTarjeta = null;
        getContextMenu().despuesPago = null;

    }


}