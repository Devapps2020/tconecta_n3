package com.blm.qiubopay.modules.fincomun.enrolamiento;

import android.Manifest;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.blm.qiubopay.modules.fincomun.login.Fragment_fincomun_login;
import com.blm.qiubopay.modules.fincomun.originacion.Fragment_fincomun_contratacion_2;
import com.blm.qiubopay.modules.fincomun.originacion.Fragment_fincomun_oferta;
import com.blm.qiubopay.modules.fincomun.prestamo.Fragment_prestamos_fincomun_1;
import com.blm.qiubopay.modules.fincomun.prestamo.Fragment_prestamos_solicitud_fincomun_1;
import com.blm.qiubopay.utils.SessionApp;
import com.orhanobut.logger.Logger;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.RegisterActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.FileUtils;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.proceedings.QPAY_AllDataRecordResponse;
import com.blm.qiubopay.modules.ahorros.Fragment_ahorros_1;
import com.blm.qiubopay.modules.fincomun.basica.Fragment_cuenta_basica_fincomun_1;
import com.blm.qiubopay.modules.fincomun.basica.Fragment_cuenta_basica_fincomun_2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mx.com.fincomun.origilib.Http.Response.Originacion.FCBuscarSolicitudesResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.Solicitud.FCConsultaCreditosResponse;
import mx.com.fincomun.origilib.Objects.ConsultaCredito.DHListaCreditos;
import mx.com.fincomun.origilib.Objects.Recompras.DHMovimientosRecompras;
import mx.com.fincomun.origilib.Objects.Solicitud.DHSolicitud;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.IRequestPermissions;

public class Fragment_enrolamiento_fincomun_0 extends HFragment implements IMenuContext {

    Uri uri = null;
    private CardView card_oferta,card_recompra, card_credit, card_request,card_codi,card_adelanto_venta;
    private TextView txt_title_offer, text_description_offer, text_description2_offer,tv_pending,text_description_oferta,tv_folio;
    public static Fragment_enrolamiento_fincomun_0 newInstance(Object... data) {
        Fragment_enrolamiento_fincomun_0 fragment = new Fragment_enrolamiento_fincomun_0();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_menu_fincomun, container, false),R.drawable.background_splash_header_1);
    }

    @Override

    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        card_oferta = getView().findViewById(R.id.card_oferta);
        text_description_oferta = getView().findViewById(R.id.text_description_oferta);
        tv_folio = getView().findViewById(R.id.tv_folio);

        card_adelanto_venta = getView().findViewById(R.id.card_adelanto_venta);
        card_recompra = getView().findViewById(R.id.card_recompra);
        card_credit = getView().findViewById(R.id.card_credit);
        card_request = getView().findViewById(R.id.card_request);
        card_codi = getView().findViewById(R.id.card_codi);
        card_codi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SessionApp.getInstance().getFcBanderas().getBanderaUsuario().equalsIgnoreCase("0") ||
                        TextUtils.isEmpty(SessionApp.getInstance().getFcBanderas().getBanderaUsuario())){
                    Fragment_fincomun_login.type = Fragment_fincomun_login.TypeLogin.APERTURA;
                }else {
                    Fragment_fincomun_login.type = Fragment_fincomun_login.TypeLogin.CODI;
                }
                getContext().setFragment(Fragment_fincomun_login.newInstance());

            }
        });
        card_oferta.setOnClickListener(v -> {
            if (SessionApp.getInstance().getFcRequestDTO() != null){
                if (SessionApp.getInstance().getFcRequestDTO().getReferencias() != null){
                    SessionApp.getInstance().getFcRequestDTO().setReferencias(null);
                    SessionApp.getInstance().getFcRequestDTO().setBeneficiarios(null);
                }
            }
            Fragment_fincomun_oferta.type = Fragment_fincomun_oferta.Type.OFFER;

            getContextMenu().setFragment(Fragment_fincomun_oferta.newInstance());
        });

        txt_title_offer = getView().findViewById(R.id.txt_title_offer);
        text_description_offer = getView().findViewById(R.id.text_description_offer);
        text_description2_offer = getView().findViewById(R.id.text_description2_offer);
        tv_pending = getView().findViewById(R.id.tv_pending);

        CardView card_contratar_cuenta = getView().findViewById(R.id.card_contratar_cuenta);
        card_contratar_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RegisterActivity.getAllDataRecord(getContext(), new IFunction<QPAY_AllDataRecordResponse>() {
                    @Override
                    public void execute(QPAY_AllDataRecordResponse... data) {

                        getContextMenu().catalogs(new IFunction() {
                            @Override
                            public void execute(Object[] data) {

                                getContext().requestPermissions(new IRequestPermissions() {
                                    @Override
                                    public void onPostExecute() {
                                        getContext().setFragment(Fragment_cuenta_basica_fincomun_1.newInstance());
                                    }
                                }, new String[]{ Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE });

                            }
                        });

                    }
                });



            }
        });

        Button btn_empezar = getView().findViewById(R.id.btn_empezar);

        btn_empezar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().setFragment(Fragment_ahorros_1.newInstance());
            }
        });

        CardView card_contratar_credito = getView().findViewById(R.id.card_contratar_credito);
        card_contratar_credito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RegisterActivity.getAllDataRecord(getContext(), new IFunction<QPAY_AllDataRecordResponse>() {
                    @Override
                    public void execute(QPAY_AllDataRecordResponse... data) {
                       // getContextMenu().initFC(null);
                       // getContext().setFragment(Fragment_prestamos_solicitud_fincomun_1.newInstance());
                        if (SessionApp.getInstance().getFcConsultaOfertaBimboResponse() == null || SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo() == null) {
                            getContextMenu().alert("Por el momento no contamos con información para ti");
                        }else {
                            getContext().setFragment(Fragment_prestamos_fincomun_1.newInstance());
                        }

                    }
                });

            }
        });

        CardView card_login = getView().findViewById(R.id.card_login);
        card_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().setFragment(Fragment_enrolamiento_fincomun_3.newInstance());
            }
        });

        CardView codi_pago = getView().findViewById(R.id.codi_pago);
        codi_pago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().setFragment(Fragment_enrolamiento_fincomun_3.newInstance());
            }
        });

        CardView codi_cobro = getView().findViewById(R.id.codi_cobro);
        codi_cobro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().setFragment(Fragment_enrolamiento_fincomun_3.newInstance());
            }
        });

        card_adelanto_venta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_fincomun_oferta.type = Fragment_fincomun_oferta.Type.ADELANTO_VENTAS;
                getContext().setFragment(Fragment_fincomun_oferta.newInstance());
            }
        });

        if(!AppPreferences.getCodiRegister()) {
            codi_pago.setVisibility(View.GONE);
            codi_cobro.setVisibility(View.GONE);
        }

        configView();


    }

    private void configView() {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###");

        if (SessionApp.getInstance().getFcConsultaOfertaBimboResponse() != null){
            text_description_oferta.setText("Hasta $"
                    +formatter.format(Double.parseDouble(SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo().get(0).getMonto()))
                    + " en efectivo");
            card_oferta.setVisibility(View.VISIBLE);
        }else{
            card_oferta.setVisibility(View.GONE);
        }

        if (SessionApp.getInstance().getListaRecompras() != null){
            List<DHMovimientosRecompras> listaRecompras = SessionApp.getInstance().getListaRecompras();

            for (DHMovimientosRecompras item: listaRecompras){
                if(item.getPorcentaje() >= 50 && item.getBandera_porcentaje() == 1){

                    card_recompra.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SessionApp.getInstance().setRecompra(item);
                            getContextMenu().getTokenFC((String... text) -> {
                                getContextMenu().consultaOfertaBimbo(text[0], false, new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {
                                        getContextMenu().getFolioRecompra(text[0], new IFunction() {
                                            @Override
                                            public void execute(Object[] data) {
                                                Fragment_fincomun_oferta.type = Fragment_fincomun_oferta.Type.RECOMPRA;
                                                getContext().setFragment(Fragment_fincomun_oferta.newInstance());
                                            }
                                        });

                                    }
                                });
                            });
                        }
                    });
                    card_recompra.setVisibility(View.VISIBLE);
                }else{
                    card_recompra.setVisibility(View.GONE);
                }

            }
        }else {
            card_recompra.setVisibility(View.GONE);
        }

        FCConsultaCreditosResponse creditosResponse = SessionApp.getInstance().getFcConsultaCreditosResponse();
      /*  creditosResponse = new FCConsultaCreditosResponse();
        creditosResponse.setListaCreditos(new ArrayList<>());
        creditosResponse.getListaCreditos().add(new DHListaCreditos(
                "12344",
                "1234567789123456789",
                "01/01/2020 00:00:00",
                "01/12/2021 00:00:00",
                "900.0",
                "1234556",
                "678905",
                "121324222",
                "12313123"
        ));
        SessionApp.getInstance().setFcConsultaCreditosResponse(creditosResponse);*/

        if (creditosResponse != null && creditosResponse.getListaCreditos() != null) {
            for (DHListaCreditos item : creditosResponse.getListaCreditos()) {
                SessionApp.getInstance().setDhListaCreditos(item);
                card_credit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (SessionApp.getInstance().getFcBanderas().getBanderaUsuario().equalsIgnoreCase("0") ||
                                TextUtils.isEmpty(SessionApp.getInstance().getFcBanderas().getBanderaUsuario())){
                            Fragment_fincomun_login.type = Fragment_fincomun_login.TypeLogin.APERTURA;
                        }else {
                            Fragment_fincomun_login.type = Fragment_fincomun_login.TypeLogin.ORIGINACION;
                        }
                        getContext().setFragment(Fragment_fincomun_login.newInstance());
                    }
                });
                card_credit.setVisibility(View.VISIBLE);
                card_codi.setVisibility(View.VISIBLE);

            }
        }

        FCBuscarSolicitudesResponse fcBuscarSolicitudesResponse = SessionApp.getInstance().getFcBuscarSolicitudesResponse();
      //  for (DHSolicitud item:fcBuscarSolicitudesResponse.getSolicitudes()){
        if (fcBuscarSolicitudesResponse!= null && fcBuscarSolicitudesResponse.getSolicitudes() != null && fcBuscarSolicitudesResponse.getSolicitudes().size() > 0) {
            DHSolicitud item = fcBuscarSolicitudesResponse.getSolicitudes().get(0);
            if ((item.getIdEtapaSolicitud() == 1
                    && item.getSolicitud().toUpperCase().equalsIgnoreCase("PENDIENTE"))
                    || (item.getIdEtapaSolicitud() == 3
                    && item.getSolicitud().toUpperCase().equalsIgnoreCase("DESEMBOLSADA"))
                    || (item.getIdEtapaSolicitud() == 2
                    && item.getSolicitud().toUpperCase().equalsIgnoreCase("PENDIENTE"))) {
                String monto = "$" + formatter.format(item.getMontoPrestamo());

                txt_title_offer.setText(monto);
                text_description_offer.setText("Solicitado " + item.getFechaCreacion());
                switch (item.getIdEtapaSolicitud()) {
                    case 1:
                        text_description2_offer.setText("PENDIENTE");
                        tv_pending.setVisibility(View.VISIBLE);
                        card_request.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                card_request.setEnabled(false);
                                if (!Fragment_fincomun_contratacion_2.newInstance().isAdded()) {
                                    getContextMenu().getTokenFC((String... text) -> {
                                        getContextMenu().postAnalisis(text[0], item, new IFunction() {
                                            @Override
                                            public void execute(Object[] data) {
                                                card_request.setEnabled(true);
                                                Fragment_fincomun_contratacion_2.type = Fragment_fincomun_oferta.Type.ANALISIS;
                                                getContext().setFragment(Fragment_fincomun_contratacion_2.newInstance());
                                            }
                                        }, Fragment_fincomun_oferta.Type.ANALISIS);
                                    });
                                }else{
                                    card_request.setEnabled(true);
                                }
                            }
                        });
                        break;
                    case 2:
                        text_description2_offer.setText("EN ANÁLISIS");
                        tv_pending.setText("Más informacion");
                        tv_pending.setVisibility(View.VISIBLE);
                        card_request.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getContext().alert(getString(R.string.txt_request_analisis));
                            }
                        });
                        break;
                    case 3:
                        text_description2_offer.setText("DESEMBOLSADA");
                        break;
                }


                tv_folio.setText("Folio: "+item.getFolio());
                card_request.setVisibility(View.VISIBLE);
            }
        }
       // }

        if (!TextUtils.isEmpty(SessionApp.getInstance().getFcBanderas().getNumCliente())){
            card_codi.setVisibility(View.VISIBLE);
        }else {
            card_codi.setVisibility(View.GONE);
        }
    }

    public void savePhoto(Uri uri, Bitmap image) {

        try {

            String path = FileUtils.getPath(getContext(), uri).split(":")[0];
            Logger.d(path);

            Fragment_cuenta_basica_fincomun_2.foto_firma = path;

            File file = new File(path);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public MenuActivity getContextMenu() {
        return  (MenuActivity) getContext();
    }
}
