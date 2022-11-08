package com.blm.qiubopay.modules.fincomun.originacion;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.database.DataBaseAccessHelper;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HLocActivity;
import com.blm.qiubopay.helpers.HOcrActivity;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.Estados;
import com.blm.qiubopay.models.bimbo.ModelItem;
import com.blm.qiubopay.models.bimbo.ModelSpinner;
import com.blm.qiubopay.models.sepomex.SepomexInformation;
import com.blm.qiubopay.models.sepomex.StateInfo;
import com.blm.qiubopay.modules.fincomun.components.FCEditText;
import com.blm.qiubopay.modules.fincomun.prestamo.Fragment_prestamos_solicitud_fincomun_7;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.google.gson.Gson;
import com.j256.ormlite.stmt.query.In;
import com.orhanobut.logger.Logger;
import com.reginald.editspinner.EditSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mx.com.fincomun.origilib.Http.Request.Apertura.FCValidacionCURPRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCAvisoPrivacidadRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCComprobantesNegocioRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCDatosNegocioRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCDatosPersonalesRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCGastosNegocioRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCIdentificacionRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCVentaCompraRequest;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCValidacionCURPResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCAvisoPrivacidadResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCComprobantesNegocioResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCDatosNegocioResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCDatosPersonalesResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCGastosNegocioResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCIdentificacionResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCVentaCompraResponse;
import mx.com.fincomun.origilib.Model.Apertura.ValidacionCURP;
import mx.com.fincomun.origilib.Model.Originacion.AvisoPrivacidad;
import mx.com.fincomun.origilib.Model.Originacion.ComprobantesNegocio;
import mx.com.fincomun.origilib.Model.Originacion.DatosNegocio;
import mx.com.fincomun.origilib.Model.Originacion.DatosPersonales;
import mx.com.fincomun.origilib.Model.Originacion.GastosNegocio;
import mx.com.fincomun.origilib.Model.Originacion.Identificacion;
import mx.com.fincomun.origilib.Model.Originacion.VentaCompraNegocio;
import mx.com.fincomun.origilib.Objects.Analisis.DHDatosTelefonicos;
import mx.com.fincomun.origilib.Objects.Catalogos.ActividadEconomica;
import mx.com.fincomun.origilib.Objects.Catalogos.ActividadGeneral;
import mx.com.fincomun.origilib.Objects.Catalogos.Genero;
import mx.com.fincomun.origilib.Objects.Catalogos.Localidad;
import mx.com.fincomun.origilib.Objects.Catalogos.LugarNacimiento;
import mx.com.fincomun.origilib.Objects.Catalogos.Municipio;
import mx.com.fincomun.origilib.Objects.Catalogos.Poblacion;
import mx.com.fincomun.origilib.Objects.GastosNegocio.DHGastosFamiliares;
import mx.com.fincomun.origilib.Objects.GastosNegocio.DHGastosNegocio;
import mx.com.fincomun.origilib.Objects.VentaCompra.DHCompra;
import mx.com.fincomun.origilib.Objects.VentaCompra.DHVenta;
import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.IRequestPermissions;

import static com.blm.qiubopay.utils.Utils.getImage;

public class Fragment_fincomun_contratacion_2 extends HFragment implements IMenuContext {
    private static Fragment_fincomun_contratacion_2 fragment = null;
    public static Fragment_fincomun_oferta.Type type = Fragment_fincomun_oferta.Type.OFFER;
    private View view;
    private HActivity context;
    private ArrayList<FCEditText> campos;
    private Object data;
    private Button btn_next;
    private ImageView iv_front, iv_back, iv_address,iv_business;
    private TextView tv_edit,tv_cellphone, tv_localphone, tv_email, tv_fullname, tv_gender,tv_birthplace, tv_birthday;
    private TextView tv_street,tv_avenue, tv_interior_num,tv_outdoor_num, tv_municipality, tv_population, tv_state, tv_postal_code,tv_suburb,tv_country;
    private TextView tv_step;
    private LinearLayout ll_edit,ll_address;

    private FCIdentificacionRequest fcIdentificacionRequest = new FCIdentificacionRequest();
    private FCComprobantesNegocioRequest fcComprobantesNegocioRequest = new FCComprobantesNegocioRequest();
    private FCDatosPersonalesRequest fcDatosPersonalesRequest = new FCDatosPersonalesRequest();

    private String numCliente="";
    Integer valEdo = -1;
    Integer valMun = -1;
    Integer valNac = -1;

    private Collection<SepomexInformation> sepomexSearchResult;
    private DataBaseAccessHelper dataBaseAccessHelper;
    private ArrayList<StateInfo> stateList;

    EditSpinner spestado;
    EditSpinner spestado_origen;
    EditSpinner splocalidad;

    private Boolean isLoading  =false;
    private Double longitud = 0.0;
    private Double latitud = 0.0;

    public static Fragment_fincomun_contratacion_2 newInstance() {
        if (fragment == null){
             fragment = new Fragment_fincomun_contratacion_2();
        }

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = (HActivity) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragment = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_fincomun_contratacion_2"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_contratacion_2, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {
        CViewMenuTop.create(getView())
                .showTitle("Contratación de préstamo")
                .setColorTitle(R.color.FC_blue_6)
                .setColorBack(R.color.FC_blue_6)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContextMenu().backFragment();
                    }
                });

        //Inicializar los spinner y la BD
        dataBaseAccessHelper = DataBaseAccessHelper.getInstance(getContext());
        stateList = (ArrayList<StateInfo>) new Estados().getListEstados();

        campos = new ArrayList();

        tv_step = getView().findViewById(R.id.tv_step);
        ll_edit = getView().findViewById(R.id.ll_edit);
        tv_edit = getView().findViewById(R.id.tv_edit);
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type){
                    case RECOMPRA:
                        Fragment_fincomun_contratacion_1.type = Fragment_fincomun_oferta.Type.RECOMPRA;
                        getContextMenu().setFragment(Fragment_fincomun_contratacion_1.newInstance());
                        break;
                    case OFFER:
                    case ADELANTO_VENTAS:
                         getContextMenu().backFragment();
                        break;
                    case ANALISIS:
                        Fragment_fincomun_contratacion_1.type = Fragment_fincomun_oferta.Type.ANALISIS;
                        getContextMenu().setFragment(Fragment_fincomun_contratacion_1.newInstance());
                        break;
                }
            }
        });

        btn_next = getView().findViewById(R.id.btn_next);

        //0/cellphone
        campos.add(FCEditText.create(getView().findViewById(R.id.et_cellphone))
                .setRequired(false)
                .setMinimum(10)
                .setMaximum(10)
                .setType(FCEditText.TYPE.NUMBER)
                .setHint("N° de celular")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate));
        //1/localphone
        campos.add(FCEditText.create(getView().findViewById(R.id.et_localphone))
                .setRequired(false)
                .setMinimum(10)
                .setMaximum(10)
                .setType(FCEditText.TYPE.NUMBER)
                .setHint("Télefono adicional")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate));
        //2/email
        campos.add(FCEditText.create(getView().findViewById(R.id.et_email))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(50)
                .setType(FCEditText.TYPE.EMAIL)
                .setHint("Correo electrónico")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate));
        //3/name
        campos.add(FCEditText.create(getView().findViewById(R.id.et_name))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(50)
                .setType(FCEditText.TYPE.TEXT_SN)
                .setHint("Nombre")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate));
        //4/gender
        campos.add(FCEditText.create(getView().findViewById(R.id.et_gender))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(20)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("Género")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate));
        //5/birthplace
        campos.add(FCEditText.create(getView().findViewById(R.id.et_birthplace))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(30)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("Lugar de nacimiento")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate));
        //6/birthday
        campos.add(FCEditText.create(getView().findViewById(R.id.et_birthday))
                .setRequired(false)
                .setMinimum(10)
                .setMaximum(10)
                .setType(FCEditText.TYPE.NONE)
                .setDatePicker(true)
                .setIcon(R.drawable.date_range)
                .setHint("Fecha de nacimiento")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate));
        //7/street
        campos.add(FCEditText.create(getView().findViewById(R.id.et_street))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(30)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("Calle")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate));
        //8/interior
        campos.add(FCEditText.create(getView().findViewById(R.id.et_interior_num))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(15)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("Número interior")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate));
        //9/outdoor
        campos.add(FCEditText.create(getView().findViewById(R.id.et_outdoor_num))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(15)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("Número exterior")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate));
        //10/state
        campos.add(FCEditText.create(getView().findViewById(R.id.et_state))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(30)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("Entidad federativa/Estado")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate)
                .setTextChanged(new FCEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        setMunicipios(campos.get(10).getTag());
                    }
                }));
        //11/municipaliy
        campos.add(FCEditText.create(getView().findViewById(R.id.et_municipality))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(30)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("Alcaldía/Municipio")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate));
        //12/population
        campos.add(FCEditText.create(getView().findViewById(R.id.et_population))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(30)
                .setType(FCEditText.TYPE.TEXT_SN)
                .setHint("Ciudad/Población")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate));

        //13/postal code
        campos.add(FCEditText.create(getView().findViewById(R.id.et_postal_code))
                .setRequired(false)
                .setMinimum(5)
                .setMaximum(5)
                .setType(FCEditText.TYPE.NUMBER)
                .setHint("Código Postal")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate)
                .setTextChanged(new FCEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        if(!TextUtils.isEmpty(text) && text.length() == 5)
                            sepomex(text);
                    }
                })
        );
        //14/suburb
        campos.add(FCEditText.create(getView().findViewById(R.id.et_suburb))
                .setRequired(false)
                .setMinimum(5)
                .setMaximum(30)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("Colonia")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate));
        //15/country
        campos.add(FCEditText.create(getView().findViewById(R.id.et_country))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(20)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("País")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate));
        //16/name business
        campos.add(FCEditText.create(getView().findViewById(R.id.et_name_business))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(20)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("Nombre del negocio")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate));
        //17/paterno
        campos.add(FCEditText.create(getView().findViewById(R.id.et_last_name))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(50)
                .setType(FCEditText.TYPE.TEXT_SN)
                .setHint("Apellido Paterno")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate));
        //18/paterno
        campos.add(FCEditText.create(getView().findViewById(R.id.et_last_name_2))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(50)
                .setType(FCEditText.TYPE.TEXT_SN)
                .setHint("Apellido Materno")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate));

        //19/curp
        campos.add(FCEditText.create(getView().findViewById(R.id.et_curp))
                .setRequired(false)
                .setMinimum(18)
                .setMaximum(18)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("CURP")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate));
        //20/rfc
        campos.add(FCEditText.create(getView().findViewById(R.id.et_rfc))
                .setRequired(false)
                .setMinimum(13)
                .setMaximum(13)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("RFC")
                .setAlert(R.string.text_input_required)
                .setEnabled(true)
                .setTextChanged(validate));


        ll_address = getView().findViewById(R.id.ll_address);

        iv_front = getView().findViewById(R.id.iv_front);
        iv_back = getView().findViewById(R.id.iv_back);
        iv_address = getView().findViewById(R.id.iv_address);
        iv_business = getView().findViewById(R.id.iv_business);

        tv_cellphone = getView().findViewById(R.id.tv_cellphone);
        tv_localphone = getView().findViewById(R.id.tv_localphone);
        tv_email = getView().findViewById(R.id.tv_email);
        tv_fullname = getView().findViewById(R.id.tv_fullname);
        tv_gender = getView().findViewById(R.id.tv_gender);
        tv_birthplace = getView().findViewById(R.id.tv_birthplace);
        tv_birthday = getView().findViewById(R.id.tv_birthday);

        tv_street = getView().findViewById(R.id.tv_street);
        tv_avenue = getView().findViewById(R.id.tv_avenue);
        tv_interior_num = getView().findViewById(R.id.tv_interior_num);
        tv_outdoor_num = getView().findViewById(R.id.tv_outdoor_num);
        tv_municipality = getView().findViewById(R.id.tv_municipality);
        tv_population = getView().findViewById(R.id.tv_population);
        tv_state = getView().findViewById(R.id.tv_state);
        tv_postal_code = getView().findViewById(R.id.tv_postal_code);
        tv_suburb = getView().findViewById(R.id.tv_suburb);
        tv_country = getView().findViewById(R.id.tv_country);
        

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    isLoading = true;
                    allRequired();
                }
            }
        });

        configView();
        fillData();
        requestUbicacion();

            getContextMenu().catalogs(new IFunction() {
                @Override
                public void execute(Object[] data) {
                    setList();
                }
            });


    }

    private void requestUbicacion() {
        ((HLocActivity) getContext()).obtainLocation(new IFunction() {
            @Override
            public void execute(Object[] data) {
                longitud = CApplication.getLastLocation().getLongitude();
                latitud = CApplication.getLastLocation().getLatitude();
            }
        });
    }

    FCEditText.ITextChanged validate = new FCEditText.ITextChanged() {
        @Override
        public void onChange(String text) {
            validate();
        }
    };

    private void configView() {
        switch (type){
            case OFFER:
            case ADELANTO_VENTAS:
                break;
            case RECOMPRA:
                tv_step.setVisibility(View.GONE);
                break;
        }
    }

    private void allRequired(){
        getContext().loading(true);

        String mVal = campos.get(11).getText();
        String mTag = campos.get(11).getTag();

        String mValColonia = campos.get(14).getText();
        String mTagColonia = campos.get(14).getTag();

        campos.get(11).setTextChanged(null);
        campos.get(10).setTextChanged(null);
        campos.get(13).setTextChanged(null);
        for (FCEditText item: campos){
            item.setRequired(true);
        }
        campos.get(8).setRequired(false);
        campos.get(12).setRequired(false);
        campos.get(10).setTextChanged(validate);
        campos.get(10).setTextChanged(new FCEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                setMunicipios(campos.get(10).getTag());
            }
        });
        campos.get(11).setTextChanged(validate);

        campos.get(11).setTag(mTag);
        campos.get(11).setText(mVal);

        campos.get(13).setTextChanged(new FCEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                if(!TextUtils.isEmpty(text) && text.length() == 5)
                    sepomex(text);
            }
        });


        campos.get(14).setTag(mTagColonia);
        campos.get(14).setText(mValColonia);



        for (FCEditText item: campos){
            if(!item.isValid()){
                //btn_next.setEnabled(false);
                getContext().loading(false);
                isLoading = false;
                getContext().alert("Completa los campos obligatorios");
                return;
            }
        }

        Pattern pattern = Pattern.compile(HOcrActivity.CURP_REGEX);
        Matcher matcher = pattern.matcher(campos.get(19).getText());

        Pattern patternRFC = Pattern.compile(HOcrActivity.RFC_REGEX);
        Matcher matcherRFC = patternRFC.matcher(campos.get(20).getText());

        if (!matcher.find()){
            getContext().loading(false);
            isLoading = false;
            getContext().alert("Verifica que tu CURP sea válido");
            return;
        }else if (!matcherRFC.find()){
            getContext().loading(false);
            isLoading = false;
            getContext().alert("Verifica que tu RFC tenga homoclave");
            return;
        }

        if (fcIdentificacionRequest.getImagen() != null && fcIdentificacionRequest.getImagen().size() < 2){
            getContext().loading(false);
            isLoading = false;
            getContext().alert("Toma la fotografía de tu identificación");
            return;
        }
        continuarFlujo();
    }

    private void validate() {
        for (FCEditText item: campos){
            if(!item.isValid()){
                return;
            }
        }
        btn_next.setEnabled(true);
    }

    public void fillData() {
        fcIdentificacionRequest = Fragment_fincomun_contratacion_1.fcIdentificacionRequest;
        fcComprobantesNegocioRequest = Fragment_fincomun_contratacion_1.fcComprobantesNegocioRequest;

        if(fcIdentificacionRequest == null || fcIdentificacionRequest.getImagen() == null || TextUtils.isEmpty(fcIdentificacionRequest.getImagen().get(0))){
            fcIdentificacionRequest = SessionApp.getInstance().getFcRequestDTO().getIdentificacion();
            fcComprobantesNegocioRequest = SessionApp.getInstance().getFcRequestDTO().getComprobante_negocio();

            numCliente = fcIdentificacionRequest.getNumCliente();
        }

        tv_fullname.setText(fcIdentificacionRequest.getNombre()+" "+fcIdentificacionRequest.getApellidoPaterno()+" "+fcIdentificacionRequest.getApellidoMaterno());
        campos.get(3).setText(fcIdentificacionRequest.getNombre());
        campos.get(17).setText(fcIdentificacionRequest.getApellidoPaterno());
        campos.get(18).setText(fcIdentificacionRequest.getApellidoMaterno());

        campos.get(12).setText("");
        campos.get(16).setText((TextUtils.isEmpty(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name()))?"":AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name());
        if (SessionApp.getInstance().getDatosNegocio() != null){
            campos.get(16).setText(SessionApp.getInstance().getDatosNegocio().getNombreNegocio());
            campos.get(1).setText(SessionApp.getInstance().getDatosNegocio().getTelefono());
        }
        if("H".equals(SessionApp.getInstance().getFcRequestDTO().getGenero())) {
            tv_gender.setText("Masculino");
            campos.get(4).setText("Masculino");
            campos.get(4).setTag("M");
        } else if("M".equals(SessionApp.getInstance().getFcRequestDTO().getGenero())) {
            tv_gender.setText("Femenino");
            campos.get(4).setText("Femenino");
            campos.get(4).setTag("F");
        }else {
            if (SessionApp.getInstance().getDatosPersonales() != null){
                if("M".equals(SessionApp.getInstance().getDatosPersonales().getGenero())) {
                    tv_gender.setText("Masculino");
                    campos.get(4).setText("Masculino");
                    campos.get(4).setTag("M");
                } else if("F".equals(SessionApp.getInstance().getDatosPersonales().getGenero())) {
                    tv_gender.setText("Femenino");
                    campos.get(4).setText("Femenino");
                    campos.get(4).setTag("F");
                }
            }

        }
        tv_birthplace.setText("");
        campos.get(5).setText("");
        tv_birthday.setText(fcIdentificacionRequest.getFechaNacimiento());
        campos.get(6).setType(FCEditText.TYPE.NONE);
        campos.get(6).setText(fcIdentificacionRequest.getFechaNacimiento());
        campos.get(6).setType(FCEditText.TYPE.TEXT);


        tv_street.append(": "+fcIdentificacionRequest.getCalle());
        campos.get(7).setText(fcIdentificacionRequest.getCalle());
        tv_avenue.setVisibility(View.GONE);
        tv_interior_num.append(": "+fcIdentificacionRequest.getNumInterior());
        campos.get(8).setText(fcIdentificacionRequest.getNumInterior());
        tv_outdoor_num.append(": "+fcIdentificacionRequest.getNumExterior());
        campos.get(9).setText(fcIdentificacionRequest.getNumExterior());
        tv_state.append(": "+fcIdentificacionRequest.getEstado());
        tv_municipality.append(": "+fcIdentificacionRequest.getMunicipio());
        tv_population.append(": ");
        tv_postal_code.append(": "+fcIdentificacionRequest.getCp());
        campos.get(13).setText(fcIdentificacionRequest.getCp());
        tv_suburb.append(": "+fcIdentificacionRequest.getColonia());
        campos.get(14).setText(fcIdentificacionRequest.getColonia());
        tv_country.append(": México");
        campos.get(15).setText("Mexico");

        String curp = fcIdentificacionRequest.getCurp();
        if (!TextUtils.isEmpty(curp) && curp.length() > 4) {
            String lastDigits = curp.substring(curp.length() - 2, curp.length());
            String digit1 = "0";
            String digit2 = "0";
            if (!Character.isDigit(lastDigits.charAt(0))) {
                digit1 = "0";
            } else {
                digit1 = String.valueOf(lastDigits.charAt(0));
            }
            if (!Character.isDigit(lastDigits.charAt(1))) {
                digit2 = "0";
            } else {
                digit2 = String.valueOf(lastDigits.charAt(1));
            }
            curp = curp.substring(0, curp.length() - 2) + digit1 + digit2;
            fcIdentificacionRequest.setCurp(curp);
            campos.get(19).setText(curp);
            campos.get(20).setText(fcIdentificacionRequest.getRfc());
        }else{
            fcIdentificacionRequest.setCurp((TextUtils.isEmpty(curp))?"":curp);
            fcIdentificacionRequest.setRfc("");
        }

        try {
            iv_front.setImageBitmap(getImage(fcIdentificacionRequest.getImagen().get(0)));
            iv_back.setImageBitmap(getImage(fcIdentificacionRequest.getImagen().get(1)));
            iv_business.setImageBitmap(getImage(fcComprobantesNegocioRequest.getImagenNegocio().get(0)));

            if (!TextUtils.isEmpty(fcComprobantesNegocioRequest.getComprobanteDomicilio())){
                iv_address.setImageBitmap(getImage(fcComprobantesNegocioRequest.getComprobanteDomicilio()));
                ll_address.setVisibility(View.VISIBLE);
            }else {
                ll_address.setVisibility(View.GONE);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }


        switch (type){
            case ANALISIS:
            case RECOMPRA:
            case RENOVACION:
                if (SessionApp.getInstance().getFcRequestDTO().getEmailAnalisis() != null){
                    campos.get(2).setText(SessionApp.getInstance().getFcRequestDTO().getEmailAnalisis());
                }else {
                    campos.get(2).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mail()+"");
                }

                if(SessionApp.getInstance().getFcRequestDTO().getDatosTelefonicos() != null && SessionApp.getInstance().getFcRequestDTO().getDatosTelefonicos().size() > 0) {
                    for (DHDatosTelefonicos item: SessionApp.getInstance().getFcRequestDTO().getDatosTelefonicos()) {
                        switch (item.getDescripcionTelefono()){
                            case "PRINCIPAL":
                                campos.get(0).setText(item.getTelefono());
                                break;
                            case "NEGOCIO":
                                campos.get(1).setText(item.getTelefono());
                                break;
                        }
                    }
                }else{
                    campos.get(1).setText("");
                    campos.get(0).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone());
                    tv_cellphone.setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone());
                    tv_localphone.setText("");
                }


                break;
            case OFFER:
            case ADELANTO_VENTAS:
                campos.get(1).setText("");
                tv_email.setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mail());
                campos.get(2).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mail()+"");

                campos.get(0).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone());
                tv_cellphone.setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone());
                tv_localphone.setText("");
                break;
        }

    }

    public void setList() {

        ArrayList<ModelItem> genero = new ArrayList();

        ArrayList<ModelItem> localidad = new ArrayList();
        ArrayList<ModelItem> estado = new ArrayList();

        ArrayList<ModelItem> general = new ArrayList();
        ArrayList<ModelItem> economica = new ArrayList();
        ArrayList<ModelItem> pais = new ArrayList();


        int index = 0;
        int position = -1;
        int positionNac = -1;



        if (fcComprobantesNegocioRequest != null || fcComprobantesNegocioRequest.getImagenNegocio() != null){
            try {
                valEdo = Integer.parseInt(fcIdentificacionRequest.getEntidad());
                valMun = Integer.parseInt(fcIdentificacionRequest.getMunicipio());

                if (SessionApp.getInstance().getDatosPersonales() != null){//5
                    valNac = Integer.parseInt(SessionApp.getInstance().getDatosPersonales().getLugarNacimiento());
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        for(LugarNacimiento est : SessionApp.getInstance().getCatalog().getLugarNacimiento()) {

            estado.add(new ModelItem(est.getName(), est.getValue()));

            Integer num = Integer.parseInt(est.getValue().trim());

            if(num == valEdo)
                position = index;

            if(num == valNac)
                positionNac = index;

            index++;
        }

        for(Localidad loc : SessionApp.getInstance().getCatalog().getLocalidad())
            localidad.add(new ModelItem(loc.getName(), loc.getValue()));

        int cgen = 0, ceco = 0;

        int cont = 0;
        for(ActividadGeneral act : SessionApp.getInstance().getCatalog().getActividadGeneral()) {

            if("12".equals(act.getValue()))
                cgen = cont;

            general.add(new ModelItem(act.getName(), act.getValue()));

            cont ++;
        }

        cont = 0;
        for(ActividadEconomica act : SessionApp.getInstance().getCatalog().getActividadEconomica()) {

            if("6131023".equals(act.getValue()))
                ceco = cont;

            economica.add(new ModelItem(act.getName(), act.getValue()));

            cont ++;
        }

        for(Genero gen : SessionApp.getInstance().getCatalog().getGenero())
            genero.add(new ModelItem(gen.getName(), gen.getValue()));

        pais.add(new ModelItem("Mexico", "484"));

        campos.get(4).setSpinner(genero);
        campos.get(5).setSpinner(estado);
        campos.get(10).setSpinner(estado);
        campos.get(15).setSpinner(pais);



        if(position > -1) {
            campos.get(10).setText(estado.get(position).getName());
            campos.get(10).setTag(estado.get(position).getValue());
            setMunicipios(estado.get(position).getValue());
            valEdo = -1;
        }else{
            setMunicipios("");
        }

        if (positionNac > -1){
            campos.get(5).setText(estado.get(positionNac).getName());
            campos.get(5).setTag(estado.get(positionNac).getValue());
        }


    }

    public void setMunicipios(String data) {

        int index = 0;
        int position = -1;
        ArrayList<ModelItem> municipio = new ArrayList();

        if(!TextUtils.isEmpty(data)) {
            for (Municipio mun : SessionApp.getInstance().getCatalog().getMunicipio())
                if (mun.getValue().substring(0, data.length()).equals(data)) {
                    municipio.add(new ModelItem(mun.getName(), mun.getValue()));

                    Integer num = Integer.parseInt(mun.getValue().trim());

                    if(num.equals(this.valMun)) {
                        position = index;
                    }

                    index++;
                }

            if (position > -1){
                campos.get(11).setText(municipio.get(position).getName());
                campos.get(11).setTag(municipio.get(position).getValue());
                valMun = -1;
            }else {

                campos.get(11).setText("");
            }
        }


        campos.get(11).setSpinner(municipio);

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    public void continuarFlujo() {

        fcIdentificacionRequest.setNumExterior("NA");
        fcIdentificacionRequest.setNumInterior("NA");
        fcIdentificacionRequest.setManzana("NA");
        fcIdentificacionRequest.setLote("NA");

        fcIdentificacionRequest.setNombre(campos.get(3).getText());
        fcIdentificacionRequest.setApellidoPaterno(campos.get(17).getText());
        fcIdentificacionRequest.setApellidoMaterno(campos.get(18).getText());
        fcIdentificacionRequest.setFechaNacimiento(campos.get(6).getText());

        fcIdentificacionRequest.setCalle_1("NA");
        fcIdentificacionRequest.setCalle_2("NA");
        if (fcIdentificacionRequest.getAnioEmision() == null){
            fcIdentificacionRequest.setAnioEmision(2020);
        }
        if (fcIdentificacionRequest.getAnioRegistro() == null){
            fcIdentificacionRequest.setAnioRegistro(2020);
        }

        //////////////////////////////////////

        fcIdentificacionRequest.setCalle(campos.get(7).getText());
        fcIdentificacionRequest.setNumInterior((TextUtils.isEmpty(campos.get(8).getText())?"NA":campos.get(8).getText()));
        fcIdentificacionRequest.setNumExterior(campos.get(9).getText());
        fcIdentificacionRequest.setEstado(campos.get(10).getText());
        fcIdentificacionRequest.setEntidad(campos.get(10).getTag());
        fcIdentificacionRequest.setMunicipio(campos.get(11).getTag());
        fcIdentificacionRequest.setCp(campos.get(13).getText());
        fcIdentificacionRequest.setColonia(campos.get(14).getText());
        //DEFAULT
        fcIdentificacionRequest.setAntiguedad("01/01/2020");
        fcIdentificacionRequest.setLocalidad(SessionApp.getInstance().getCatalog().getLocalidad().get(0).getValue());

        //////////////////////////////////////

        fcIdentificacionRequest.setCurp(campos.get(19).getText());
        fcIdentificacionRequest.setRfc(campos.get(20).getText());

        if(TextUtils.isEmpty(fcIdentificacionRequest.getCic())){
            fcIdentificacionRequest.setCic("000000");

        } if(TextUtils.isEmpty(fcIdentificacionRequest.getOcr())){
            fcIdentificacionRequest.setOcr("000000");

        }

        fcIdentificacionRequest.setId_actividad_general("12");
        fcIdentificacionRequest.setId_actividad_economica("6131023");
        fcIdentificacionRequest.setIdIdentificacion("1");

        SessionApp.getInstance().getFcRequestDTO().setIdentificacion(fcIdentificacionRequest);


        getContextMenu().getTokenFC(new IFunction<String>() {
            @Override
            public void execute(String... data) {
                if (data != null) {
                    postAvisoPrivacidad(data[0]);
                }else {
                    isLoading = false;
                }
            }
        });
    }

    public void postAvisoPrivacidad(String token){

        getContext().loading(true);

        SessionApp.getInstance().getFcRequestDTO().setToken(token);
        FCAvisoPrivacidadRequest fcAvisoPrivacidadRequest = new FCAvisoPrivacidadRequest();

        fcAvisoPrivacidadRequest.setTelefono(campos.get(0).getText());
        fcAvisoPrivacidadRequest.setEmail(campos.get(2).getText());
        fcAvisoPrivacidadRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcAvisoPrivacidadRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());

        fcAvisoPrivacidadRequest.setLongitud(longitud);
        fcAvisoPrivacidadRequest.setLatitud(latitud);

        SessionApp.getInstance().getFcRequestDTO().setPrivacidad(fcAvisoPrivacidadRequest);

        AvisoPrivacidad avisoPrivacidad = new AvisoPrivacidad(getContext());

        avisoPrivacidad.postAvisoPrivacidad(fcAvisoPrivacidadRequest, new AvisoPrivacidad.onRequest() {
            @Override
            public <E> void onSuccess(E e) {

                FCAvisoPrivacidadResponse response = (FCAvisoPrivacidadResponse) e;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getRespuesta().getCodigo() < 0) {
                    getContext().loading(false);
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                    isLoading = false;
                } else {
                    SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                    SessionApp.getInstance().getFcRequestDTO().setToken(response.getTokenJwt());

                    //postIdentificacion(token);
                    validaCurp(token);
                }


            }
            @Override
            public void onFailure(String mensaje) {
                getContext().loading(false);
                getContext().alert(mensaje);
                isLoading = false;
            }
        });

    }

    public void validaCurp(String token){
        getContext().loading(true);

        ValidacionCURP validacionCURP = new ValidacionCURP(getContext());
        FCValidacionCURPRequest request = new FCValidacionCURPRequest(
                fcIdentificacionRequest.getCurp(),
                SessionApp.getInstance().getFcRequestDTO().getFolio()
        );

        Logger.d("REQUEST : "  + new Gson().toJson(fcIdentificacionRequest));

        validacionCURP.postValidacionCURP(request, new ValidacionCURP.onRequest() {
            @Override
            public <E> void onSuccess(E e) {
                FCValidacionCURPResponse response = (FCValidacionCURPResponse)e;
                Logger.d("RESPONSE : "  + new Gson().toJson(response));
                if (response.getEstatusDeRespuesta() != null){
                    if(response.getEstatusDeRespuesta().getEdo() == 0){
                        if (response.isVerificacion()){
                            postIdentificacion(token);

                        }else{
                            getContext().loading(false);
                            getContext().alert(response.getEstatusDeRespuesta().getDescripcion());
                            isLoading = false;
                        }

                    }else{
                        getContext().loading(false);
                        getContext().alert(response.getEstatusDeRespuesta().getDescripcion());
                        isLoading = false;                    }
                }else{
                    getContext().loading(false);
                    getContext().alert(response.getEstatusDeRespuesta().getDescripcion());
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
                isLoading = false;
            }
        });
    }
    public void postIdentificacion(String token){

        getContext().loading(true);

        SessionApp.getInstance().getFcRequestDTO().setToken(token);

        ArrayList<String> imagesnes = new ArrayList();

        for(String img : fcIdentificacionRequest.getImagen())
            if(img != null && !img.isEmpty())
                imagesnes.add(img);

        fcIdentificacionRequest.setImagen(imagesnes);

        fcIdentificacionRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcIdentificacionRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcIdentificacionRequest.setNumCliente(numCliente);
        fcIdentificacionRequest.setSucursal(Globals.FC_SUCURSAL);
        fcIdentificacionRequest.setUsuario(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());

        SessionApp.getInstance().getFcRequestDTO().setIdentificacion(fcIdentificacionRequest);

        Logger.d("REQUEST : "  + new Gson().toJson(fcIdentificacionRequest));

        Identificacion identificacion = new Identificacion(getContext());
        identificacion.postIdentificacion(fcIdentificacionRequest, new Identificacion.onRequest() {
            @Override
            public <E> void onSuccess(E e) {

                FCIdentificacionResponse response = (FCIdentificacionResponse) e;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getRespuesta().getCodigo() == 0) {
                    SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                    SessionApp.getInstance().getFcRequestDTO().setToken(response.getTokenJwt());
                    SessionApp.getInstance().getFcRequestDTO().setNumCliente(response.getNumCliente());

                    postDatosPersonales(token);
                } else {

                    getContext().loading(false);
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                    isLoading = false;
                }

            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
                isLoading = false;
            }
        });

    }

    public void postDatosPersonales(String token){

        fcDatosPersonalesRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcDatosPersonalesRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcDatosPersonalesRequest.setNumCliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());

        SessionApp.getInstance().getFcRequestDTO().setToken(token);
        fcDatosPersonalesRequest.setId_profesion("738");
        fcDatosPersonalesRequest.setNacionalidad("484");
        fcDatosPersonalesRequest.setEstadoCivil("1");
        fcDatosPersonalesRequest.setPais("484");
        fcDatosPersonalesRequest.setOcupacion("3");
        fcDatosPersonalesRequest.setGradoEstudios("1");
        fcDatosPersonalesRequest.setLugarNacimiento(campos.get(5).getTag());
        fcDatosPersonalesRequest.setTipo_vivienda(0);
        fcDatosPersonalesRequest.setHorario_contacto(2);
        fcDatosPersonalesRequest.setDependientes_economicos(0);

        fcDatosPersonalesRequest.setGenero(campos.get(4).getTag());

        fcDatosPersonalesRequest.setUsuario(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());

        SessionApp.getInstance().getFcRequestDTO().setDatos_personales(fcDatosPersonalesRequest);

        Logger.d("REQUEST : "  + new Gson().toJson(fcDatosPersonalesRequest));

        DatosPersonales datosPersonales = new DatosPersonales(getContext());
        datosPersonales.postDatosPersonales(fcDatosPersonalesRequest, new DatosPersonales.onRequest() {
            @Override
            public <E> void onSuccess(E e) {

                FCDatosPersonalesResponse response = (FCDatosPersonalesResponse)e;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getRespuesta().getCodigo() == 0) {
                    SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());

                    postDatosNegocio(token);
                } else {

                    getContext().loading(false);
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                    isLoading = false;
                }

            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
                isLoading = false;
            }
        });
    }

    public void postDatosNegocio(String token){

        SessionApp.getInstance().getFcRequestDTO().setToken(token);
        FCDatosNegocioRequest fcDatosNegocioRequest = new FCDatosNegocioRequest();

        fcDatosNegocioRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcDatosNegocioRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcDatosNegocioRequest.setNumCliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());

        fcDatosNegocioRequest.setNombreNegocio(campos.get(16).getText());
        fcDatosNegocioRequest.setSector("32");
        fcDatosNegocioRequest.setGiro("001");
        fcDatosNegocioRequest.setAntiguedad(fcIdentificacionRequest.getAntiguedad());
        fcDatosNegocioRequest.setTelefono(campos.get(1).getText());
        fcDatosNegocioRequest.setCalle(fcIdentificacionRequest.getCalle());
        fcDatosNegocioRequest.setNumInterior(fcIdentificacionRequest.getNumInterior());
        fcDatosNegocioRequest.setNumExterior("NA");
        fcDatosNegocioRequest.setManzana("NA");
        fcDatosNegocioRequest.setLote("NA");
        fcDatosNegocioRequest.setCalle_1("NA");
        fcDatosNegocioRequest.setCalle_2("NA");
        fcDatosNegocioRequest.setColonia(fcIdentificacionRequest.getColonia());
        fcDatosNegocioRequest.setCp(fcIdentificacionRequest.getCp());
        fcDatosNegocioRequest.setMunicipio(fcIdentificacionRequest.getMunicipio());
        fcDatosNegocioRequest.setLocalidad(fcIdentificacionRequest.getLocalidad());
        fcDatosNegocioRequest.setEstado(fcIdentificacionRequest.getEstado());
        fcDatosNegocioRequest.setEntidad(fcIdentificacionRequest.getEntidad());

        SessionApp.getInstance().getFcRequestDTO().setDatos_negocio(fcDatosNegocioRequest);

        Logger.d("REQUEST : "  + new Gson().toJson(fcDatosNegocioRequest));

        DatosNegocio datosNegocio = new DatosNegocio(getContext());
        datosNegocio.postDatosNegocio(fcDatosNegocioRequest, new DatosNegocio.onRequest() {
            @Override
            public <E> void onSuccess(E e) {

                FCDatosNegocioResponse response = (FCDatosNegocioResponse)e;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getRespuesta().getCodigo() == 0) {
                    postVentaCompra(token);

                } else {
                    isLoading = false;
                    getContext().loading(false);
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                }

            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
                isLoading = false;
            }
        });
    }

    public void postVentaCompra(String token){

        SessionApp.getInstance().getFcRequestDTO().setToken(token);

        FCVentaCompraRequest fcVentaCompraRequest = new FCVentaCompraRequest();
        fcVentaCompraRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcVentaCompraRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcVentaCompraRequest.setNumCliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());

        fcVentaCompraRequest.setVenta(new DHVenta());
        fcVentaCompraRequest.setCompra(new DHCompra());
        fcVentaCompraRequest.getVenta().setLunes(100.00);
        fcVentaCompraRequest.getVenta().setMartes(100.00);
        fcVentaCompraRequest.getVenta().setMiercoles(100.00);
        fcVentaCompraRequest.getVenta().setJueves(100.00);
        fcVentaCompraRequest.getVenta().setViernes(100.00);
        fcVentaCompraRequest.getVenta().setSabado(100.00);
        fcVentaCompraRequest.getVenta().setDomingo(100.00);
        fcVentaCompraRequest.setOtrosIngresos(5000.00);
        fcVentaCompraRequest.getCompra().setFuente_otros_ingresos("100.00");
        fcVentaCompraRequest.setInventario(5000.00);
        fcVentaCompraRequest.getCompra().setLunes(100.00);
        fcVentaCompraRequest.getCompra().setMartes(100.00);
        fcVentaCompraRequest.getCompra().setMiercoles(100.00);
        fcVentaCompraRequest.getCompra().setJueves(100.00);
        fcVentaCompraRequest.getCompra().setViernes(100.00);
        fcVentaCompraRequest.getCompra().setSabado(100.00);
        fcVentaCompraRequest.getCompra().setDomingo(100.00);
        SessionApp.getInstance().getFcRequestDTO().setVenta_compra(fcVentaCompraRequest);

        Logger.d("REQUEST : "  + new Gson().toJson(fcVentaCompraRequest));

        VentaCompraNegocio ventaCompraNegocio = new VentaCompraNegocio(getContext());
        ventaCompraNegocio.postVentaCompraNegocio(fcVentaCompraRequest, new VentaCompraNegocio.onRequest() {
            @Override
            public <E> void onSuccess(E e) {

                FCVentaCompraResponse response = (FCVentaCompraResponse)e;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getRespuesta().getCodigo() == 0) {
                    postGastosNegocio(token);

                } else {
                    isLoading = false;
                    getContext().loading(false);
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                }

            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
                isLoading = false;
            }
        });

    }

    public void postGastosNegocio(String token){

        SessionApp.getInstance().getFcRequestDTO().setToken(token);

        FCGastosNegocioRequest fcGastosNegocioRequest = new FCGastosNegocioRequest();
        fcGastosNegocioRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcGastosNegocioRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());

        fcGastosNegocioRequest.setGastosNegocio(new DHGastosNegocio());
        fcGastosNegocioRequest.setGastosFamiliares(new DHGastosFamiliares());
        fcGastosNegocioRequest.getGastosNegocio().setValorCompraMercancia(500);
        fcGastosNegocioRequest.getGastosNegocio().setTrasnporte(100);
        fcGastosNegocioRequest.getGastosNegocio().setServiciosNegocio(100);
        fcGastosNegocioRequest.getGastosNegocio().setEmpleados(100);
        fcGastosNegocioRequest.getGastosNegocio().setTotalGastosNegocio(1500);
        fcGastosNegocioRequest.getGastosFamiliares().setAlimentacion(100);
        fcGastosNegocioRequest.getGastosFamiliares().setGastoMensualDespensa(100);
        fcGastosNegocioRequest.getGastosFamiliares().setServiciosFamiliares(100);
        fcGastosNegocioRequest.getGastosFamiliares().setRopa(100);
        fcGastosNegocioRequest.getGastosFamiliares().setColegio(100);
        fcGastosNegocioRequest.getGastosFamiliares().setDiversion(100);
        fcGastosNegocioRequest.getGastosFamiliares().setRenta(100);
        fcGastosNegocioRequest.getGastosFamiliares().setOtrosGastosFamiliares(100);
        fcGastosNegocioRequest.getGastosFamiliares().setTotalGastosFamiliares(800);

        SessionApp.getInstance().getFcRequestDTO().setGastos_negocio(fcGastosNegocioRequest);
        Logger.d("REQUEST : "  + new Gson().toJson(fcGastosNegocioRequest));

        GastosNegocio gastosNegocio = new GastosNegocio(getContext());
        gastosNegocio.postGastosNegocio(fcGastosNegocioRequest, new GastosNegocio.onRequest() {
            @Override
            public <E> void onSuccess(E e) {

                FCGastosNegocioResponse response = (FCGastosNegocioResponse) e;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getRespuesta().getCodigo() == 0) {
                    SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                    SessionApp.getInstance().getFcRequestDTO().setToken(response.getTokenJwt());

                    postComprobantesNegocio(token);

                } else {
                    isLoading = false;
                    getContext().loading(false);
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));

                }

            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
                isLoading = false;
            }

        });
    }

    public void postComprobantesNegocio(String token){

        SessionApp.getInstance().getFcRequestDTO().setToken(token);

        fcComprobantesNegocioRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcComprobantesNegocioRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcComprobantesNegocioRequest.setNumCliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());

        fcComprobantesNegocioRequest.setImagenNegocio(new ArrayList(Arrays.asList(fcComprobantesNegocioRequest.getImagenNegocio().get(0))));
        if (TextUtils.isEmpty(fcComprobantesNegocioRequest.getComprobanteDomicilio())) {
            fcComprobantesNegocioRequest.setComprobanteDomicilio(fcComprobantesNegocioRequest.getImagenNegocio().get(0));
        }
        fcComprobantesNegocioRequest.setEstadoCuenta(fcComprobantesNegocioRequest.getImagenNegocio().get(0));

        Logger.d("REQUEST : "  + new Gson().toJson(fcComprobantesNegocioRequest));

        SessionApp.getInstance().getFcRequestDTO().setComprobante_negocio(fcComprobantesNegocioRequest);

        ComprobantesNegocio comprobantesNegocio = new ComprobantesNegocio(getContext());
        comprobantesNegocio.postComprobantesNegocio(fcComprobantesNegocioRequest , new ComprobantesNegocio.onRequest() {
            @Override
            public <E> void onSuccess(E e) {

                FCComprobantesNegocioResponse response = (FCComprobantesNegocioResponse)e;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));
                getContext().loading(false);

                if(response.getRespuesta().getCodigo() == 0) {
                    SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                    SessionApp.getInstance().getFcRequestDTO().setToken(response.getTokenJwt());

                    switch (type){
                        case RECOMPRA:
                            getContextMenu().backFragment();
                            break;
                        case OFFER:
                        case ANALISIS:
                        case ADELANTO_VENTAS:
                            getContext().setFragment(Fragment_fincomun_contratacion_3.newInstance());
                            break;
                    }
                    isLoading = false;
                } else {
                    isLoading = false;
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                }


            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
                isLoading = false;
            }
        });

    }

    public void sepomex(String codigo){

        dataBaseAccessHelper.open();
        sepomexSearchResult = dataBaseAccessHelper.findPostalCode(codigo);
        dataBaseAccessHelper.close();

        if(sepomexSearchResult != null && sepomexSearchResult.size() > 0){

            ArrayList<ModelItem> poblados = new ArrayList();

            for(int i=0; i<sepomexSearchResult.size(); i++) {
                poblados.add(new ModelItem(Tools.getOnlyNumbersAndLetters(((SepomexInformation)sepomexSearchResult.toArray()[i]).getD_asenta().replace(")","").replace("(","")),i+""));
            }

            campos.get(14).setSpinner(poblados);
            SepomexInformation info = (SepomexInformation) sepomexSearchResult.toArray()[0];

            for(int i=0; i<stateList.size(); i++){

                if(info.getD_estado().contains(stateList.get(i).getState_name())){
                    campos.get(14).setText(Tools.getOnlyNumbersAndLetters(info.getD_asenta()));
                    return;
                }

            }
        } else {
            campos.get(14).removeSpinner();
            campos.get(14).setRequired(false)
                    .setMinimum(5)
                    .setMaximum(30)
                    .setType(FCEditText.TYPE.TEXT)
                    .setHint("Colonia")
                    .setAlert(R.string.text_input_required)
                    .setEnabled(true)
                    .setTextChanged(validate);
        }

    }
}