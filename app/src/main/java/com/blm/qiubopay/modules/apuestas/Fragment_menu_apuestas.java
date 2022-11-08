package com.blm.qiubopay.modules.apuestas;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.components.CViewOption;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IApuestasDeportivas;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.apuestas.QPAY_GetUrlPetition;
import com.blm.qiubopay.models.apuestas.QPAY_GetUrlResponse;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_menu_apuestas extends HFragment implements IMenuContext {

    ImageView icon;
    TextView title;

    public static Fragment_menu_apuestas newInstance() {
        return new Fragment_menu_apuestas();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_menu_reportes, container, false), R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        icon = getView().findViewById(R.id.module_icon);
        title = getView().findViewById(R.id.module_title);
        title.setText(getResources().getText(R.string.text_apuestas_title));
        icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_apuestas_deportivas));
        CViewMenuTop.create(getView()).showLogo().onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        CViewOption.create(getView().findViewById(R.id.layout_recargas)).setText(R.string.text_apuestas_deportivas).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {

                getUrl(new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        getContext().setFragment(Fragment_apuestas_webview.newInstance(data[0]));

                    }
                });

            }
        });

        CViewOption.create(getView().findViewById(R.id.layout_servicios)).setText(R.string.text_consultar_premio).onClick(new CViewOption.IClick() {
            @Override
            public void onClick(View v) {

                getContext().setFragment(Fragment_apuestas_folio.newInstance());

            }
        });

        getView().findViewById(R.id.layout_tarjetas).setVisibility(View.GONE);
        getView().findViewById(R.id.layout_cargos_abonos).setVisibility(View.GONE);
        getView().findViewById(R.id.layout_comisiones_dia).setVisibility(View.GONE);
        getView().findViewById(R.id.layout_financiero).setVisibility(View.GONE);
        getView().findViewById(R.id.layout_no_financiero).setVisibility(View.GONE);
        getView().findViewById(R.id.layout_reporte_financiero).setVisibility(View.GONE);

        Button btn_corte_caja = getView().findViewById(R.id.btn_corte_caja);
        btn_corte_caja.setVisibility(View.GONE);


    }

    public void getUrl(final IFunction function){


        getContext().loading(true);

        try {

            QPAY_GetUrlPetition petition = new QPAY_GetUrlPetition();
            petition.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            IApuestasDeportivas service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {
                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);

                    } else {
                        getContext().loading(false);

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_GetUrlResponse.QPAY_GetUrlResponseExcluder()).create();
                        String json = new Gson().toJson(result).replace("\"\"","");
                        Log.d("ApuestasDeportivas",json);
                        QPAY_GetUrlResponse response = new Gson().fromJson(json, QPAY_GetUrlResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            if(function != null)
                                function.execute(response);
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

            service.getUrl(petition);

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
