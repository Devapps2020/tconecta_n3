package com.blm.qiubopay.modules.fincomun.prestamo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HCoDi;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.modules.fincomun.codi.Fragment_codi_menu;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import mx.com.fincomun.origilib.Http.Request.Apertura.FCConsultaBanderaFCILAperturaRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCBuscarSolicitudesRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCSimuladorRequest;
import mx.com.fincomun.origilib.Http.Request.Recompras.FCListaRecomprasRequest;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCConsultaBanderaFCILAperturaResponse;
import mx.com.fincomun.origilib.Http.Response.Banxico.ValidaCuenta.FCValidacionCuentaResponse;
import mx.com.fincomun.origilib.Http.Response.FCRespuesta;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCBuscarSolicitudesResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCSimuladorResponse;
import mx.com.fincomun.origilib.Http.Response.Recompras.FCListaRecomprasResponse;
import mx.com.fincomun.origilib.Model.Apertura.ConsultaBanderaFCIL;
import mx.com.fincomun.origilib.Model.Originacion.BuscarSolicitud;
import mx.com.fincomun.origilib.Model.Originacion.Simulador;
import mx.com.fincomun.origilib.Model.Recompras.ListaRecompras;
import mx.com.fincomun.origilib.Objects.ConsultaCredito.DHListaCreditos;
import mx.com.fincomun.origilib.Objects.DHOfertaBimbo;
import mx.com.fincomun.origilib.Objects.Retiro.DHCuenta;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_prestamos_fincomun_1 extends HFragment implements IMenuContext {

    private ArrayList<HEditText> campos;
    private Object data;
    private Button btn_codi;
    public static String promotor;

    public static FCConsultaBanderaFCILAperturaResponse banderas;

    public static Fragment_prestamos_fincomun_1 newInstance(Object... data) {
        Fragment_prestamos_fincomun_1 fragment = new Fragment_prestamos_fincomun_1();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_prestamos_fincomun_1, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        CApplication.setAnalytics(CApplication.ACTION.CB_FINCOMUN_Inicio_V2);

        btn_codi = getView().findViewById(R.id.btn_codi);

        TextView monto_autorizado = getView().findViewById(R.id.monto_autorizado);
        TextView text_tipo_pago =  getView().findViewById(R.id.text_tipo_pago);

        ImageView img_fincomun = getView().findViewById(R.id.img_fincomun);
        img_fincomun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setStep(3);
            }
        });

        ITextChanged iTextChanged = new ITextChanged() {
            @Override
            public void onChange() {
                validate();
            }
            @Override
            public void onMaxLength() {

            }
        };

        Button btn_apply_credit = getView().findViewById(R.id.btn_apply_credit_fc);
        Button btn_view_more = getView().findViewById(R.id.btn_view_more);
        Button btn_me_interesa = getView().findViewById(R.id.btn_me_interesa);

        btn_apply_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getContextMenu().showAlertPromotor(new IClickView() {
                    @Override
                    public void onClick(Object... data) {

                        promotor = (String) data[0];

                        getContextMenu().catalogs(new IFunction() {
                            @Override
                            public void execute(Object[] data) {
                                setStep(null);
                            }
                        });

                    }
                });

            }
        });

        btn_me_interesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getContextMenu().showAlertPromotor(new IClickView() {
                    @Override
                    public void onClick(Object... data) {

                        promotor = (String) data[0];

                        getContextMenu().catalogs(new IFunction() {
                            @Override
                            public void execute(Object[] data) {
                                configPrestamo(SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo().get(0).getMonto());
                            }
                        });

                    }
                });


            }
        });

        btn_view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().getTokenFC((String... text) -> {
                    buscarSolicitud(text[0]);
                });
            }
        });

        LinearLayout layout_lista = getView().findViewById(R.id.layout_lista);
        LinearLayout layout_title_prestamos = getView().findViewById(R.id.layout_title_prestamos);
        LinearLayout layout_oferta = getView().findViewById(R.id.layout_oferta);

        if(SessionApp.getInstance().getFcConsultaCreditosResponse().getListaCreditos() == null || SessionApp.getInstance().getFcConsultaCreditosResponse().getListaCreditos().isEmpty()) {

            layout_lista.setVisibility(View.GONE);
            layout_title_prestamos.setVisibility(View.INVISIBLE);
            monto_autorizado.setText(Utils.paserCurrency(SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo().get(0).getMonto()).replace(".00", ""));
            layout_oferta.setVisibility(View.VISIBLE);

            DHOfertaBimbo oferta = SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo().get(0);

            String frecuencia = "";
            switch (Integer.parseInt(oferta.getFrecuencia())) {
                case 7:
                    frecuencia = "semanal";
                    break;
                case 14:
                    frecuencia = "catorcenal";
                    break;
                case 15:
                    frecuencia = "quincenal";
                    break;
                case 30:
                    frecuencia = "mensual";
                    break;
            }

            frecuencia = "quincenal";

            text_tipo_pago.setText(18 + " pagos " + frecuencia + " de " + Utils.paserCurrency("" + Math.ceil(getCuota(false, Integer.parseInt(oferta.getMonto()), 18, 15))));

        } else {

            ListView list_prestamos = getView().findViewById(R.id.list_prestamos);
            ArrayAdapter adapter = new PrestamosAdapter(getContext(), SessionApp.getInstance().getFcConsultaCreditosResponse().getListaCreditos());
            list_prestamos.setAdapter(adapter);
            layout_oferta.setVisibility(View.GONE);
            btn_apply_credit.setVisibility(View.GONE);
            btn_me_interesa.setVisibility(View.GONE);
            list_prestamos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SessionApp.getInstance().setDhListaCreditos(SessionApp.getInstance().getFcConsultaCreditosResponse().getListaCreditos().get(position));
                    getContext().setFragment(Fragment_prestamos_detalle_fincomun_1.newInstance());
                }
            });

        }

        if(banderas.tieneCreditos == 1) {
            viewCodi(true);
        } else if(banderas.tieneCuenta == 1)  {
            viewCodi(true);
        } else {
            viewCodi(false);
        }

    }

    public void viewCodi(boolean view) {

        if (view) {
            btn_codi.setVisibility(View.GONE);
            btn_codi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iniciarCoDi();
                }
            });
        } else
            btn_codi.setVisibility(View.GONE);

    }

    public void configPrestamo(String monto) {

        FCSimuladorRequest simuladorRequest = new FCSimuladorRequest();

        simuladorRequest.setIdTipoCredito(2);
        simuladorRequest.setIdModCredito(6);
        simuladorRequest.setIdTipoProducto(2);
        simuladorRequest.setMontoPrestamo(new BigInteger(monto.replace(".00", "")));
        simuladorRequest.setIngresos(BigInteger.ONE);
        simuladorRequest.setNumPagos("18");
        simuladorRequest.setDestino("311");
        simuladorRequest.setSucursal(Globals.FC_SUCURSAL);
        simuladorRequest.setUsuario(Globals.FC_USUARIO);//
        simuladorRequest.setFrecuenciaPago("15");
        simuladorRequest.setIdPromotor(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());

        simuladorRequest.setCuota(getCuota(false,
                Integer.parseInt(monto.replace(".00", "")),
                Integer.parseInt(simuladorRequest.getNumPagos()),
                Integer.parseInt(simuladorRequest.getFrecuenciaPago())));

        SessionApp.getInstance().getFcRequestDTO().setSimulador(simuladorRequest);

        getContextMenu().getTokenFC((String... data) -> {
            simuladorRequest.setTokenJwt(data[0]);
            simulador(simuladorRequest);
        });

    }

    private void simulador(FCSimuladorRequest request) {

        Logger.d("REQUEST : "  + new Gson().toJson(request));

        Simulador simulador = new Simulador(getContext());
        simulador.postSimulador(request, new Simulador.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {

                FCSimuladorResponse response = (FCSimuladorResponse)Object;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getRespuesta().getCodigo() < 0) {
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                } else {
                    SessionApp.getInstance().getFcRequestDTO().setStep(Globals.NUMBER.UNO);
                    SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                    SessionApp.getInstance().getFcRequestDTO().setToken(response.getTokenJwt());
                    AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());
                    getContext().setFragment(Fragment_prestamos_solicitud_fincomun_4.newInstance());
                }

                getContext().loading(false);
            }

            @Override
            public void onFailure(String mensaje) {
                getContext().loading(false);
                getContext().alert(mensaje);
            }
        });

    }

    public Double getCuota(boolean seguro, Integer monto, Integer plazo, Integer frecuencia) {

        try {

            Double tasa_periodo = getPlazoPeriodo(seguro, frecuencia)/100;
            Double potencia1 = (tasa_periodo * (Math.pow((1.0 + tasa_periodo), plazo)));
            Double potencia2 = (Math.pow((1.0 + tasa_periodo), plazo) - 1.0);

            AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());

            return round(monto * (potencia1 / potencia2));

        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }

    public Double getPlazoPeriodo(boolean seguro, Integer frecuencia) {

        DHOfertaBimbo ofertaBimbo = SessionApp.getInstance().getFcConsultaOfertaBimboResponse().getOfertaBimbo().get(0);

        Double tasa_seguro = Double.parseDouble(ofertaBimbo.getTasa_con_medico_en_casa()) * 100;
        Double tasa_sin_seguro = Double.parseDouble(ofertaBimbo.getTasa_sin_medico_en_casa()) * 100;
        Double tasa = (seguro ? tasa_seguro : tasa_sin_seguro);

        SessionApp.getInstance().getFcRequestDTO().setTasa(tasa);

        ArrayList<Integer> frecuencias = new ArrayList();
        frecuencias.add(7);
        frecuencias.add(14);
        frecuencias.add(28);

        int periodo = 360;

        if(frecuencias.contains(frecuencia))
            periodo = 364;

        return round((tasa / periodo) * frecuencia * 1.16);
    }

    public Double round(double number) {
        number = Math.round(number * 100);
        return  number/100;
    }

    public void validate(){

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                return;
            }

    }

    public void buscarSolicitud(String token) {

        FCBuscarSolicitudesRequest fcBuscarSolicitudesRequest = new FCBuscarSolicitudesRequest();
        fcBuscarSolicitudesRequest.setCliente(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());
        fcBuscarSolicitudesRequest.setTokenJwt(token);

       /* fcBuscarSolicitudesRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcBuscarSolicitudesRequest.setTokenJwt(token);
        fcBuscarSolicitudesRequest.setNombre("");
        fcBuscarSolicitudesRequest.setId_promotor(Globals.FC_PROMOTOR);
        fcBuscarSolicitudesRequest.setFechaInicial("");
        fcBuscarSolicitudesRequest.setFechaFinal("");
        fcBuscarSolicitudesRequest.setEtapa_solicitud(1);
        fcBuscarSolicitudesRequest.setEstatus(1);

        */

        Logger.d("REQUEST : "  + new Gson().toJson(fcBuscarSolicitudesRequest));


        BuscarSolicitud buscarSolicitud = new BuscarSolicitud(getContext());
        buscarSolicitud.postBuscarSolicitud(fcBuscarSolicitudesRequest, new BuscarSolicitud.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCBuscarSolicitudesResponse response = (FCBuscarSolicitudesResponse) Object;
                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getSolicitudes() == null || response.getSolicitudes().isEmpty())
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                else {
                    Fragment_prestamos_fincomun_2.solicitudes = response.getSolicitudes();
                    getRecompras(token);
                }


                getContext().loading(false);
            }
            @Override
            public void onFailure(String mensaje) {
                getContext().loading(false);
                getContext().alert(mensaje);
            }
        });

    }

    private void getRecompras(String token) {
        getContext().loading(true);

        ListaRecompras listaRecompras = new ListaRecompras(getContext());
        FCListaRecomprasRequest request = new FCListaRecomprasRequest(
                AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id(),
                token
        );

        listaRecompras.postListaRecompras(request, new ListaRecompras.onRequest() {
            @Override
            public <E> void onSuccess(E Object) {
                FCListaRecomprasResponse response = (FCListaRecomprasResponse)Object;
                if (response == null || response.getMovimientos().isEmpty()){
                   // getContext().alert(response.getRespuesta().getDescripcion().get(0));
                    getContext().setFragment(Fragment_prestamos_fincomun_2.newInstance());

                }else{
                    Fragment_prestamos_fincomun_2.movimientos = response.getMovimientos();
                    Fragment_prestamos_fincomun_2.porcentajes = response.getPorcentajes();
                    getContext().setFragment(Fragment_prestamos_fincomun_2.newInstance());
                }
            }

            @Override
            public void onFailure(String s) {
                getContext().loading(false);

            }
        });
    }

    public void validatePerfil() {

        Button btn_codi = getView().findViewById(R.id.btn_codi);

        getContextMenu().getTokenFC(new IFunction<String>() {
            @Override
            public void execute(String... data) {

                FCConsultaBanderaFCILAperturaRequest request = new FCConsultaBanderaFCILAperturaRequest(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id(), data[0]);

                ConsultaBanderaFCIL consultaBanderaFCIL = new ConsultaBanderaFCIL(getContext());
                consultaBanderaFCIL.postConsultaBanderaFCIL(new FCConsultaBanderaFCILAperturaRequest(),new ConsultaBanderaFCIL.onRequest(){
                    @Override
                    public <E> void onSuccess(E Object) {
                        FCConsultaBanderaFCILAperturaResponse response = (FCConsultaBanderaFCILAperturaResponse)Object;
                        Logger.d(new Gson().toJson(response) );

                        if(response.getTieneCreditos() == 0) {

                            btn_codi.setVisibility(View.VISIBLE);

                            btn_codi.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    iniciarCoDi();
                                }
                            });


                        } else  {
                            btn_codi.setVisibility(View.GONE);
                        }

                    }
                    @Override
                    public void onFailure(String s) {
                        Logger.d(s);
                        getContext().alert(s);
                        getContext().loading(false);
                    }
                });


            }
        });

    }

    public void iniciarCoDi() {

        getContextMenu().sethCoDi(new HCoDi(getContextMenu()));

        if(!AppPreferences.getCodiClabe().isEmpty()) {

            if(!AppPreferences.getCodiRegister()){
                getContext().loading(true);

                getContextMenu().gethCoDi().cargarKeysource(new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        getContextMenu().gethCoDi().statusValidacionCuenta(new IFunction() {
                            @Override
                            public void execute(Object[] data) {

                                FCRespuesta respuesta = (FCRespuesta) data[0];
                                getContext().loading(false);

                                if(respuesta.getCodigo() == 1) {
                                    AppPreferences.setCodiRegister(true);
                                    getContext().setFragment(Fragment_codi_menu.newInstance());
                                } else
                                    getContext().alert("Fincomún", respuesta.getDescripcion().get(0));

                            }
                        });
                    }
                });
            } else {
                getContextMenu().gethCoDi().cargarKeysource(new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        getContext().setFragment(Fragment_codi_menu.newInstance());
                    }
                });
            }

            return;
        }

        getContext().alert("CoDi", "Usar códigos QR para pagar y cobrar\nen establecimientos o para solicitar\ndinero a otras personas.", new IAlertButton() {
            @Override
            public String onText() {
                return "Registrar";
            }

            @Override
            public void onClick() {
                getContext().loading(true);
                getContextMenu().gethCoDi().registroInicial(new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        getContext().loading(false);
                        getContextMenu().gethCoDi().confirmSMS(new IFunction() {
                            @Override
                            public void execute(Object[] data) {
                                getContext().loading(true);
                                getContextMenu().gethCoDi().crearKeysource((String) data[0], new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {
                                        getContextMenu().gethCoDi().guardarKeysource(new IFunction() {
                                            @Override
                                            public void execute(Object[] data) {
                                                getContextMenu().gethCoDi().cargarKeysource(new IFunction() {
                                                    @Override
                                                    public void execute(Object[] data) {

                                                        getContextMenu().gethCoDi().registroSubsecuente(new IFunction() {
                                                            @Override
                                                            public void execute(Object[] data) {
                                                                getContextMenu().gethCoDi().registroAppPorOmision(new IFunction() {
                                                                    @Override
                                                                    public void execute(Object[] data) {
                                                                        getContext().loading(false);
                                                                        getContextMenu().gethCoDi().selectCuenta(new IFunction<DHCuenta>() {
                                                                            @Override
                                                                            public void execute(DHCuenta[] cuentas) {
                                                                                getContext().loading(true);
                                                                                getContextMenu().gethCoDi().validacionCuenta(cuentas[0], new IFunction() {
                                                                                    @Override
                                                                                    public void execute(Object[] data) {
                                                                                        FCValidacionCuentaResponse respuesta = (FCValidacionCuentaResponse) data[0];
                                                                                        AppPreferences.setCodiRastreo(respuesta.getClaveRastreo());
                                                                                        AppPreferences.setCodiClabe(cuentas[0].getClabeCuenta());
                                                                                        getContext().loading(false);
                                                                                        getContext().alert("CoDi", "La validación de la cuenta tardará unos minutos. Recibirás una notificación por parte de Banxico");
                                                                                    }
                                                                                });
                                                                            }
                                                                        });

                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
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
    }

    public void setStep(Integer number) {

        if(number == null)
            number = 0;

        switch (Globals.NUMBER.from(number)){
            case UNO:
                getContext().setFragment(Fragment_prestamos_solicitud_fincomun_4.newInstance());
                break;
            case DOS:
                break;
            case TRES:
                getContext().setFragment(Fragment_prestamos_solicitud_fincomun_6.newInstance());
                break;
            default:
                getContext().setFragment(Fragment_prestamos_solicitud_fincomun_1.newInstance());
                break;
        }
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }


    public class PrestamosAdapter extends ArrayAdapter<DHListaCreditos> {

        List<DHListaCreditos> pagos;

        public PrestamosAdapter(Context context, List<DHListaCreditos> data) {
            super(context, 0, data);
            pagos = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_credito, parent, false);

            DHListaCreditos credito = pagos.get(position);

            TextView text_nombre = convertView.findViewById(R.id.text_nombre);
            TextView text_numero = convertView.findViewById(R.id.text_numero);

            text_nombre.setText("Nombre del crédito : " + credito.getNumeroCredito());
            text_numero.setText("Saldo : " + Utils.paserCurrency("" + credito.getMontoPago()));

            return convertView;
        }

    }

}
