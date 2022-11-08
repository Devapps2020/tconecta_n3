package com.blm.qiubopay.modules.campania;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IGetCategories;
import com.blm.qiubopay.listeners.IGetOrganization;
import com.blm.qiubopay.listeners.IGetRollProducts;
import com.blm.qiubopay.listeners.IViewTipsAdvertising;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.pedidos.QPAY_GetCategories;
import com.blm.qiubopay.models.pedidos.QPAY_GetCategoriesResponse;
import com.blm.qiubopay.models.pedidos.QPAY_GetOrganization;
import com.blm.qiubopay.models.pedidos.QPAY_GetOrganizationResponse;
import com.blm.qiubopay.models.publicity.QPAY_TipsAdvertising_object;
import com.blm.qiubopay.models.publicity.QPAY_TipsAdvertising_publicities;
import com.blm.qiubopay.models.questions.QPAY_CampaignAnswers;
import com.blm.qiubopay.models.rolls.QPAY_RollsCostResponse;
import com.blm.qiubopay.modules.Fragment_compra_dongle_1;
import com.blm.qiubopay.modules.Fragment_compra_material_1;
import com.blm.qiubopay.modules.Fragment_compra_rollos_1;
import com.blm.qiubopay.modules.chambitas.Fragment_chambitas_menu;
import com.blm.qiubopay.modules.fincomun.enrolamiento.Fragment_enrolamiento_fincomun_0;
import com.blm.qiubopay.modules.fincomun.originacion.Fragment_fincomun_oferta;
import com.blm.qiubopay.modules.home.Fragment_browser;
import com.blm.qiubopay.modules.pedidos.Fragment_pedidos_bimbo_1;
import com.blm.qiubopay.printers.FormattedLine;
import com.blm.qiubopay.printers.PrinterLineType;
import com.blm.qiubopay.printers.PrinterManager;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.LocalRedirectUtils;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nexgo.oaf.apiv3.device.printer.AlignEnum;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_tips extends HFragment {

    private static QPAY_TipsAdvertising_object data;
    private static ViewPager viewPager;
    private static IFunction continuar;
    private TabLayout indicator;
    private Button btn_acceder;
    private Button btn_omitir;
    private ImageView ivSharePrint;

    public static Fragment_tips newInstance() {
        return new Fragment_tips();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_tips, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        /*getContext().setDefaultBack(new IFunction() {
            @Override
            public void execute(Object[] data) {
                getContext().backFragment();
            }
        });*/

        configCarrusel();

    }

    public void configCarrusel() {

        List<HFragment> fragments = new ArrayList();

        int position = 0;
        for (QPAY_TipsAdvertising_publicities tip : data.getPublicities()) {
            tip.setPosition(position++);
            Logger.d(tip.getImage());
            fragments.add(Fragment_item_tip.newInstance(tip));
        }

        CFragmentAdapter adapter = new CFragmentAdapter(getContext().getSupportFragmentManager(), fragments);

        viewPager = getView().findViewById(R.id.view_pager);
        indicator = getView().findViewById(R.id.tab_layout);
        viewPager.setAdapter(adapter);
        btn_acceder = getView().findViewById(R.id.btn_acceder);
        btn_omitir = getView().findViewById(R.id.btn_omitir);
        ivSharePrint = getView().findViewById(R.id.iv_share_print);
        indicator.setupWithViewPager(viewPager);
        indicator.setVisibility(View.GONE);
        setContent();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {

            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                if(position == adapter.getCount() -1)
                    btn_omitir.setText("FINALIZAR");
            }
        });

        // TODO: Remove
        btn_acceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = data.getPublicities()[viewPager.getCurrentItem()].getLink();
                if(url.contains(Globals.APPLINK_URL)) {
                    try {
                        Uri appLinkData = Uri.parse(url);
                        String lastSegment = appLinkData.getLastPathSegment();
                        Logger.d("Promocion: " + lastSegment);
                        LocalRedirectUtils redirectUtils = new LocalRedirectUtils(getContext());
                        redirectUtils.redirectInApp(lastSegment);
//                                redirectInApp(lastSegment);
                    } catch (Exception e) {
                        getContext().alert(R.string.general_error_url);
                    }
                } else {
                    try {
                        Uri parseUrl = Uri.parse(url);
                        getContext().setFragment(Fragment_browser.newInstance(url));
                    } catch (Exception e) {
                        getContext().alert(R.string.general_error_url);
                    }
                }
            }
        });

        ivSharePrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QPAY_TipsAdvertising_publicities tip = data.getPublicities()[viewPager.getCurrentItem()];
                printTicket(tip, new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                    }
                });
            }
        });

    }

    private void setContent() {
        if (data.getPublicities()[0].getLink() != null) {
            if (!data.getPublicities()[0].getLink().isEmpty()) {
                if (data.getPublicities()[0].getButtonText() != null) {
                    if (!data.getPublicities()[0].getButtonText().isEmpty()) {
                        btn_acceder.setVisibility(View.VISIBLE);
                        btn_acceder.setText(data.getPublicities()[0].getButtonText());
                    }
                }
            }
        }
    }

    private void enableGetCountPublicity(){
        try{
            //2021-12-16 RSB. Se habilita la consulta de publicidad para el badge
            ((MenuActivity)getContext()).setCountPublicity(true);
        } catch (Exception e){
            Logger.e(e.getMessage());
        }
    }


    public class CFragmentAdapter extends FragmentStatePagerAdapter {

        private List<HFragment> fragments;

        public CFragmentAdapter(FragmentManager fm, List<HFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public HFragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }


    }

    public static void setData(QPAY_TipsAdvertising_object data) {
        Fragment_tips.data = data;
    }

    public static void setContinue(IFunction continuar) {
        Fragment_tips.continuar = continuar;
    }

    public static void setPositionPager(HActivity context, Integer position) {

        position++;

        if(position < data.getPublicities().length)
            viewPager.setCurrentItem(position);
        else {
            if(continuar != null)
                continuar.execute();
        }

    }

    /**
     * Metodo para redireccionar inApp
     * @param linkToGo
     */
    public void redirectInApp(String linkToGo){
        if(linkToGo==null){ linkToGo = ""; }

        switch (linkToGo){
            case Globals.F_CHAMBITAS:
                getContext().setFragment(Fragment_chambitas_menu.newInstance());
                break;
            case Globals.F_FINCOMUN:
                try{
                    validateData();
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
                break;
            case Globals.F_COMPRA_ROLLOS:
                getRollProducts();
                break;
            case Globals.F_COMPRA_LECTOR:
                //getContext().setFragment(Fragment_compra_dongle_1.newInstance());
                break;
            case Globals.F_COMPRA_MATERIAL:
                getContext().setFragment(Fragment_compra_material_1.newInstance());
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
            default:
                getContext().alert(R.string.general_error_url);
                break;
        }
    }

    private void validateData() {
        ((MenuActivity)getContext()).validatePerfil(new IFunction(){
            @Override
            public void execute(Object[] data) {
                String value = String.valueOf(data[0]);
                switch (value){
                    case "OFERTA":
                        if (TextUtils.isEmpty(SessionApp.getInstance().getFcBanderas().getNumCliente())) {
                            getContext().setFragment(Fragment_fincomun_oferta.newInstance());
                        }else {
                            getContext().setFragment(Fragment_enrolamiento_fincomun_0.newInstance());
                        }
                        break;
                    default:
                        getContext().setFragment(Fragment_enrolamiento_fincomun_0.newInstance());
                        break;
                }

            }
        });
    }

    //2021-12-15 RSB. Se agregan estos métodos para el link hacia Unilever

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
                        getContext().loading(false);
                        getContext().alert(R.string.general_error);

                    } else {

                        getContext().loading(false);

                        String json = new Gson().toJson(result);
                        QPAY_GetCategoriesResponse response = new Gson().fromJson(json, QPAY_GetCategoriesResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            function.execute(response);
                        } else {
                            ((MenuActivity) getContext()).validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

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
                        getContext().loading(false);
                        getContext().alert(R.string.general_error);

                    } else {

                        getContext().loading(false);

                        String json = new Gson().toJson(result);
                        QPAY_GetOrganizationResponse response = new Gson().fromJson(json, QPAY_GetOrganizationResponse.class);

                        if (response.getQpay_response().equals("true")) {
                            function.execute(response);
                        } else {
                            ((MenuActivity) getContext()).validaSesion(response.getQpay_code(), response.getQpay_description());
                        }

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


    private void getRollProducts(){

        getContext().loading(true);

        try {

            IGetRollProducts petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);
                    } else {

                        //Respuesta correcta

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_RollsCostResponse.QPAY_RollsCostResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_RollsCostResponse response = gson.fromJson(json, QPAY_RollsCostResponse.class);

                        if(response.getQpay_response().equals("true")){
                            getContext().setFragment(Fragment_compra_rollos_1.newInstance(response));
                        } else {
                            try {
                                ((MenuActivity) getContext()).validaSesion(response.getQpay_code(), response.getQpay_description());
                            } catch (Exception ex) {
                                getContext().alert(response.getQpay_description());
                            }
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    getContext().loading(false);

                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.getRollProduts();

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }


    private void printTicket(QPAY_TipsAdvertising_publicities tip,final IFunction function){
        //N3_FLAG_COMMENT
        List<FormattedLine> lines = new ArrayList<FormattedLine>();
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.CENTER, PrinterLineType.IMAGE,""));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT, "PROMOCIÓN"));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, tip.getTitle()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, tip.getDescription()));
        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, ""));
        if (tip.getLink() != null && !tip.getLink().isEmpty()) {
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, tip.getLink()));
        } else {
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, tip.getImage()));
        }

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        String blmid = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id();
        blmid = (blmid != null && !blmid.isEmpty() ? blmid : "na");

        String bimboId = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id();
        bimboId = (bimboId != null && !bimboId.isEmpty() ? bimboId : "");

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.LEFT, PrinterLineType.TEXT, ""));
        lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, "BLM ID " + blmid));

        if(!bimboId.isEmpty())
            lines.add(new FormattedLine(Globals.FONT_SIZE_NORMAL, Globals.fontNormal, AlignEnum.LEFT, PrinterLineType.TEXT, "BIMBO ID " + bimboId));

        lines.add(new FormattedLine(Globals.FONT_SIZE_BIG, Globals.fontBold, AlignEnum.CENTER, PrinterLineType.TEXT," "));

        PrinterManager printerManager = new PrinterManager(getActivity());
        printerManager.printFormattedTicket(function, lines, getContext());

    }

}