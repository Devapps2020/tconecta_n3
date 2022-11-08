package com.blm.qiubopay.modules.fincomun.prestamo;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HCoDi;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.modules.fincomun.originacion.Fragment_fincomun_contratacion_1;
import com.blm.qiubopay.modules.fincomun.originacion.Fragment_fincomun_contratacion_2;
import com.blm.qiubopay.modules.fincomun.originacion.Fragment_fincomun_contratacion_3;
import com.blm.qiubopay.modules.home.Fragment_menu_inicio;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.blm.qiubopay.R;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.orhanobut.logger.Logger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.com.fincomun.origilib.Http.Request.Originacion.FCAnalisisRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCComprobantesNegocioRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCIdentificacionRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCReferenciasRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCSimuladorRequest;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCAnalisisResponse;
import mx.com.fincomun.origilib.Model.Originacion.Analisis;
import mx.com.fincomun.origilib.Objects.Analisis.DHDatosArchivo;
import mx.com.fincomun.origilib.Objects.Analisis.DHDatosCliente;
import mx.com.fincomun.origilib.Objects.Analisis.DHDatosDirecciones;
import mx.com.fincomun.origilib.Objects.Analisis.DHDatosIne;
import mx.com.fincomun.origilib.Objects.Analisis.DHDatosReferencia;
import mx.com.fincomun.origilib.Objects.Analisis.DHDatosSimulador;
import mx.com.fincomun.origilib.Objects.Recompras.DHMovimientosRecompras;
import mx.com.fincomun.origilib.Objects.Recompras.DHPorcentajesRecompras;
import mx.com.fincomun.origilib.Objects.Referencia.DHReferencia;
import mx.com.fincomun.origilib.Objects.Solicitud.DHSolicitud;
import mx.devapps.utils.components.HActivity;

public class Fragment_prestamos_fincomun_2 extends HFragment implements IMenuContext {

    public static ArrayList<DHSolicitud> solicitudes;
    public static ArrayList<DHMovimientosRecompras> movimientos;
    public static ArrayList<DHPorcentajesRecompras> porcentajes;
    private View view;
    private HActivity context;
    private ArrayList<HEditText> campos;
    private Object data;

    RecyclerView rv_requests,rv_recompras;
    RequestAdapter adapter;
    RecompraAdapter recompraAdapter;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static Fragment_prestamos_fincomun_2 newInstance(Object... data) {
        Fragment_prestamos_fincomun_2 fragment = new Fragment_prestamos_fincomun_2();
        Bundle args = new Bundle();



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
            data = new Gson().fromJson(getArguments().getString("Fragment_prestamos_fincomun_2"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_prestamos_fincomun_2, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){


        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
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

        LinearLayout layout_solicitudes = getView().findViewById(R.id.layout_solicitudes);
        LinearLayout layout_recompras = getView().findViewById(R.id.layout_recompras);

        TextView tex_my_request = getView().findViewById(R.id.tex_my_request);
        TextView tex_repurchases = getView().findViewById(R.id.tex_repurchases);


        tex_my_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layout_solicitudes.setVisibility(View.VISIBLE);
                layout_recompras.setVisibility(View.GONE);

            }
        });

        tex_repurchases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layout_solicitudes.setVisibility(View.GONE);
                layout_recompras.setVisibility(View.VISIBLE);

                setRecompras();
            }
        });




        rv_requests = getView().findViewById(R.id.rv_requests);
        rv_requests.setHasFixedSize(true);
        rv_requests.setLayoutManager(new LinearLayoutManager(getActivity()));

        rv_recompras = getView().findViewById(R.id.rv_recompras);
        rv_recompras.setHasFixedSize(true);
        rv_recompras.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new RequestAdapter(solicitudes);
        rv_requests.setAdapter(adapter);

    }

    private void setRecompras() {
        if (movimientos !=null && porcentajes != null){
            recompraAdapter = new RecompraAdapter(fillPorcentaje(movimientos,porcentajes));
            rv_recompras.setAdapter(recompraAdapter);
        }
    }

    public void validate(){

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                return;
            }


    }


    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ItemViewHolder> {


        private ArrayList<DHSolicitud> items;

        public RequestAdapter(ArrayList<DHSolicitud> items) {
            this.items = items;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_request_fincomun, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ItemViewHolder holder, final int position) {
            DHSolicitud item = items.get(position);

            if(holder.txt_title != null)
                holder.txt_title.setText(Utils.paserCurrency(String.valueOf(item.getMontoPrestamo())));

            if(holder.text_description != null)
                holder.text_description.setText("Solicitado "+item.getFechaCreacion());

            if(holder.text_description2 != null)
                holder.text_description2.setText(item.getSolicitud()+"/"+item.getEtapaSolicitud());

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionStatus(item);
                }
            });
        }

        private void actionStatus(DHSolicitud item) {
            switch (item.getIdEtapaSolicitud()){
                case 1:
                    if (item.getSolicitud().toUpperCase().equalsIgnoreCase("PENDIENTE")){
                        getContextMenu().getTokenFC((String... text) -> {
                            postAnalisis(text[0],item);
                        });
                    }
                    break;
            }

        }
/*
        @Override
        public int getItemViewType(int position) {
            return items.get(position).getLayout();
        }
 */

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            public View view;
            public TextView txt_title;
            public TextView text_description;
            public TextView text_description2;

            public ItemViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                txt_title = itemView.findViewById(R.id.txt_title);
                text_description = itemView.findViewById(R.id.text_description);
                text_description2 = itemView.findViewById(R.id.text_description2);
            }
        }
    }

    private void postAnalisis(String token, DHSolicitud item) {
        getContext().loading(true);
        Analisis analisis = new Analisis(getContext());

        FCAnalisisRequest request = new FCAnalisisRequest(
                /*item.getFolio()*/"45651668",
                item.getNumCliente(),
                token
        );
        Logger.d("REQUEST : "  + new Gson().toJson(request));

        analisis.postAnalisis(request, new Analisis.onRequest() {
            @Override
            public <E> void onSuccess(E e) {
                FCAnalisisResponse response = (FCAnalisisResponse)e;
                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response == null){
                    getContext().alert("Error al consultar información de la solicitud");

                }else{
                    if (response.getRespuesta().getCodigo() == 0){
                        prepareData(response);

                    }else{
                        getContext().alert(response.getRespuesta().getDescripcion().get(0));
                        getContext().loading(false);

                    }
                }

            }

            @Override
            public void onFailure(String mensaje) {
                getContext().loading(false);
                getContext().alert(mensaje);
            }
        });
    }

    private void prepareData(FCAnalisisResponse response) {
        SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());

        FCSimuladorRequest simuladorRequest = new FCSimuladorRequest();

        DHDatosSimulador simulador = response.getDatosSimulador();
        simuladorRequest.setIdTipoCredito(simulador.getId_tipo_prod_cred());
        simuladorRequest.setIdModCredito(simulador.getId_mod_credito());
        simuladorRequest.setIdTipoProducto(Integer.parseInt(simulador.getId_product_credito()));
        simuladorRequest.setMontoPrestamo(BigDecimal.valueOf(simulador.getMontoPrestamo()).toBigInteger());
        simuladorRequest.setIngresos(BigInteger.ONE);
        simuladorRequest.setNumPagos(response.getDatosSimulador().getNumPagos());
        simuladorRequest.setDestino("311");
        simuladorRequest.setSucursal(Globals.FC_SUCURSAL);
        simuladorRequest.setUsuario(Globals.FC_USUARIO);//
        simuladorRequest.setFrecuenciaPago(response.getDatosSimulador().getFrecuenciaPago());
        simuladorRequest.setIdPromotor(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());

        simuladorRequest.setCuota(response.getDatosSimulador().getCuota());
        SessionApp.getInstance().getFcRequestDTO().setSimulador(simuladorRequest);

        FCIdentificacionRequest fcIdentificacionRequest = new FCIdentificacionRequest();

        DHDatosCliente datosCliente = response.getDatosCliente();
        DHDatosDirecciones direccion = datosCliente.getDireccion();
        DHDatosIne datosIne = response.getDatosIne();

        if (direccion != null) {

            fcIdentificacionRequest.setNombre(datosCliente.getNombre());
            fcIdentificacionRequest.setApellidoPaterno(datosCliente.getApellidoPaterno());
            fcIdentificacionRequest.setApellidoMaterno(datosCliente.getApellidoMaterno());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            SimpleDateFormat d2 = new SimpleDateFormat("yyyy-MM-dd");

            try {
                fcIdentificacionRequest.setFechaNacimiento(d2.format(df.parse(datosCliente.getFechaNacimiento())));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            fcIdentificacionRequest.setCalle(direccion.getCalle());
            fcIdentificacionRequest.setCalle_1(direccion.getCalle1());
            fcIdentificacionRequest.setCalle_2(direccion.getCalle2());
            fcIdentificacionRequest.setNumExterior(direccion.getNumExterior());
            fcIdentificacionRequest.setNumInterior(direccion.getNumInterior());
            fcIdentificacionRequest.setManzana(direccion.getManzana());
            fcIdentificacionRequest.setLote(direccion.getLote());

            fcIdentificacionRequest.setEstado(direccion.getEstado());
            fcIdentificacionRequest.setEntidad(String.valueOf(direccion.getIdEntidad()));
            fcIdentificacionRequest.setMunicipio(String.valueOf(direccion.getIdMunicipio()));
            fcIdentificacionRequest.setCp(direccion.getCp());
            fcIdentificacionRequest.setColonia(direccion.getColonia());

            try {
                fcIdentificacionRequest.setAntiguedad(d2.format(df.parse(direccion.getAntiguedad())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            fcIdentificacionRequest.setLocalidad(String.valueOf(direccion.getIdLocalidad()));

            fcIdentificacionRequest.setCurp(datosIne.getCurp());
            fcIdentificacionRequest.setClaveElector(datosIne.getClaveElector());
             fcIdentificacionRequest.setRfc(datosCliente.getRfc());
             fcIdentificacionRequest.setCic(datosIne.getCic());
             fcIdentificacionRequest.setOcr(datosIne.getOcr());
            fcIdentificacionRequest.setAnioEmision(datosIne.getAnioEmision());
            fcIdentificacionRequest.setAnioRegistro(datosIne.getAnioRegistro());
            fcIdentificacionRequest.setId_actividad_general(datosIne.getIdActividadGeneral());
            fcIdentificacionRequest.setId_actividad_economica(datosIne.getIdActividadEconomica());
            fcIdentificacionRequest.setIdIdentificacion("1");

            SessionApp.getInstance().getFcRequestDTO().setGenero("N");

            FCComprobantesNegocioRequest fcComprobantesNegocioRequest = new FCComprobantesNegocioRequest();
            fcComprobantesNegocioRequest.setFolio(response.getFolio());
            fcComprobantesNegocioRequest.setNomina(response.getNomina());


            ArrayList<String> imgNegocio = new ArrayList<>();
            ArrayList<String> imgIdentificacion = new ArrayList<>();
            for (DHDatosArchivo archivo: response.getDatosArchivos()){
                if (archivo.getNombreArchivo().contains("FOTO_NEGOCIO_1")){
                    imgNegocio.add(0,archivo.getBase64());
                }else if (archivo.getNombreArchivo().contains("INE_F")
                        || archivo.getNombreArchivo().contains("INE_QR_F")
                        || archivo.getNombreArchivo().contains("IFE_F")){
                    imgIdentificacion.add(0,archivo.getBase64());
                }else if(archivo.getNombreArchivo().contains("INE_T")
                        || archivo.getNombreArchivo().contains("INE_QR_T")
                        || archivo.getNombreArchivo().contains("IFE_T")){
                    imgIdentificacion.add(1,archivo.getBase64());
                }else if(archivo.getNombreArchivo().contains("COMPROBANTE_DOMICILIO")){
                    fcComprobantesNegocioRequest.setComprobanteDomicilio(archivo.getBase64());
                }else if(archivo.getNombreArchivo().contains("ESTADO_CUENTA")){
                    fcComprobantesNegocioRequest.setEstadoCuenta(archivo.getBase64());
                }
            }
            imgNegocio.add(1,"");
            fcComprobantesNegocioRequest.setImagenNegocio(imgNegocio);

            if (!TextUtils.isEmpty(fcComprobantesNegocioRequest.getComprobanteDomicilio())
                    && fcComprobantesNegocioRequest.getComprobanteDomicilio().equalsIgnoreCase(fcComprobantesNegocioRequest.getImagenNegocio().get(0))){
                fcComprobantesNegocioRequest.setComprobanteDomicilio("");
            }
            fcIdentificacionRequest.setImagen(imgIdentificacion);
            SessionApp.getInstance().getFcRequestDTO().setIdentificacion(fcIdentificacionRequest);
            SessionApp.getInstance().getFcRequestDTO().setComprobante_negocio(fcComprobantesNegocioRequest);

            FCReferenciasRequest fcReferenciasRequest = new FCReferenciasRequest();
            fcReferenciasRequest.setFolio(response.getFolio());
            ArrayList<DHReferencia> referencias = new ArrayList<>();
            for (DHDatosReferencia referencia: response.getDatosReferencias()){
                referencias.add(new DHReferencia(
                        referencia.getNombre(),
                        referencia.getApellidoPaterno(),
                        referencia.getApellidoMaterno(),
                        referencia.getIdParentesco(),
                        referencia.getTelefono(),
                        referencia.getIdReferencia()));
            }
            fcReferenciasRequest.setReferencias(referencias);
            SessionApp.getInstance().getFcRequestDTO().setReferencias(fcReferenciasRequest);

        }




        getContext().loading(false);
        getContext().setFragment(Fragment_fincomun_contratacion_2.newInstance());


    }

    private ArrayList<DHMovimientosRecompras> fillPorcentaje(ArrayList<DHMovimientosRecompras> items, ArrayList<DHPorcentajesRecompras> porcentajes) {
        for (DHMovimientosRecompras item: items) {
            for (DHPorcentajesRecompras pc: porcentajes) {
                if (pc.getNum_credito() == item.getIdCuenta()){
                    item.setBandera_porcentaje(pc.getBandera_porcentaje());
                    item.setPorcentaje(pc.getPorcentaje().intValue());
                }
            }
        }
        return items;
    }

    public class RecompraAdapter extends RecyclerView.Adapter<RecompraAdapter.ItemViewHolder> {


        private ArrayList<DHMovimientosRecompras> items;

        public RecompraAdapter(ArrayList<DHMovimientosRecompras> items) {
            this.items = items;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_recompra_fincomun, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ItemViewHolder holder, final int position) {
            DHMovimientosRecompras item = items.get(position);

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionRecompra(item);
                }
            });

            Date date = new Date();
            try {
                date = new SimpleDateFormat("yyyyMMdd").parse(String.valueOf(item.getFecProximoPago()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.tv_nombre.setText(item.getProducto());
            holder.tv_fecha.setText("Fecha próximo pago: "+sdf.format(date));

            String formatted = Utils.paserCurrency(item.getImpTotalPagar());
            holder.tv_pago.setText("Cuota: "+formatted);

        }

        private void actionStatus(DHMovimientosRecompras item) {

        }
/*
        @Override
        public int getItemViewType(int position) {
            return items.get(position).getLayout();
        }
 */

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_nombre,tv_fecha,tv_pago;;
            public View view;

            public ItemViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                tv_nombre = (TextView) itemView.findViewById(R.id.tv_nombre);
                tv_fecha = (TextView) itemView.findViewById(R.id.tv_fecha);
                tv_pago = (TextView) itemView.findViewById(R.id.tv_pago);
            }
        }
    }

    private void actionRecompra(DHMovimientosRecompras item) {
    }
}
