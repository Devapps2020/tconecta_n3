package com.blm.qiubopay.modules.fincomun.prestamo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.database.DataBaseAccessHelper;
import com.blm.qiubopay.models.Estados;
import com.blm.qiubopay.models.LastLocation;
import com.blm.qiubopay.models.sepomex.SepomexInformation;
import com.blm.qiubopay.models.sepomex.StateInfo;
import com.blm.qiubopay.modules.home.Fragment_browser;
import com.blm.qiubopay.tools.Tools;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.reginald.editspinner.EditSpinner;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.RegisterActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.Base64;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HOcrActivity;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.helpers.interfaces.IOcrListener;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.HOcrMatch;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.bimbo.ModelItem;
import com.blm.qiubopay.models.bimbo.ModelSpinner;
import com.blm.qiubopay.models.proceedings.QPAY_UserFcIdentification;
import com.blm.qiubopay.models.proceedings.QPAY_UserFcPersonalData;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import mx.com.fincomun.origilib.Http.Request.Originacion.Credito.FCOfertaSeleccionadaCreditoRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCAvisoPrivacidadRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCBeneficiariosRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCComprobantesNegocioRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCDatosNegocioRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCDatosPersonalesRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCGastosNegocioRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCIdentificacionRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCReferenciasRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCSMSRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCValidaSMSRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCVentaCompraRequest;
import mx.com.fincomun.origilib.Http.Response.Originacion.Credito.FCOfertaSeleccionadaCreditoResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCAvisoPrivacidadResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCBeneficiariosResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCComprobantesNegocioResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCDatosNegocioResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCDatosPersonalesResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCGastosNegocioResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCIdentificacionResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCReferenciasResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCSMSResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCValidaSMSResponse;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCVentaCompraResponse;
import mx.com.fincomun.origilib.Model.Originacion.AvisoPrivacidad;
import mx.com.fincomun.origilib.Model.Originacion.Beneficiarios;
import mx.com.fincomun.origilib.Model.Originacion.ComprobantesNegocio;
import mx.com.fincomun.origilib.Model.Originacion.Credito.OfertaSeleccionadaCredito;
import mx.com.fincomun.origilib.Model.Originacion.DatosNegocio;
import mx.com.fincomun.origilib.Model.Originacion.DatosPersonales;
import mx.com.fincomun.origilib.Model.Originacion.GastosNegocio;
import mx.com.fincomun.origilib.Model.Originacion.Identificacion;
import mx.com.fincomun.origilib.Model.Originacion.Referencias;
import mx.com.fincomun.origilib.Model.Originacion.SMS;
import mx.com.fincomun.origilib.Model.Originacion.VentaCompraNegocio;
import mx.com.fincomun.origilib.Objects.Catalogos.ActividadEconomica;
import mx.com.fincomun.origilib.Objects.Catalogos.ActividadGeneral;
import mx.com.fincomun.origilib.Objects.Catalogos.Genero;
import mx.com.fincomun.origilib.Objects.Catalogos.Localidad;
import mx.com.fincomun.origilib.Objects.Catalogos.LugarNacimiento;
import mx.com.fincomun.origilib.Objects.Catalogos.Municipio;
import mx.com.fincomun.origilib.Objects.Credito.DHOferta;
import mx.com.fincomun.origilib.Objects.DHBeneficiario;
import mx.com.fincomun.origilib.Objects.GastosNegocio.DHGastosFamiliares;
import mx.com.fincomun.origilib.Objects.GastosNegocio.DHGastosNegocio;
import mx.com.fincomun.origilib.Objects.Referencia.DHReferencia;
import mx.com.fincomun.origilib.Objects.VentaCompra.DHCompra;
import mx.com.fincomun.origilib.Objects.VentaCompra.DHVenta;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IActivityResult;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.IRequestPermissions;

public class Fragment_prestamos_solicitud_fincomun_4 extends HFragment implements IMenuContext {

    private boolean negocio = false;
    private boolean domicilio = false;

    private ArrayList<HEditText> campos;
    private ArrayList<HEditText> camposAviso;
    private Object data;
    private Button btn_save;
    private Button btn_next;

    private Button btn_ife,btn_ine;


    private LinearLayout btn_take_3,btn_take_4;
    private ImageView img_front, img_revert, img_camera_1, img_camera_2;
    private Button btn_take_1, btn_take_2;
    private Boolean front = true;
    private Uri uri;

    private int identificacion = 1;


    private CheckBox check_acept,check_direccion;
    private TextView btn_aviso;
    private EditSpinner spgenero;

    //OCR id's de retorno
    private final String IFE = "FEDERAL";
    private final String CURP = "CURP";
    private final String NOMBRE = "NOMBRE";
    private final String DOMICILIO = "DOMICILIO";
    private final String CP = "CP";

    private final String SEXO = "SEXO";
    private final String CLAVE = "CLAVE DE ELECTOR";

    private final String ESTADO = "ESTADO";
    private final String MUNICIPIO = "MUNICIPIO";
    private final String LOCALIDAD = "LOCALIDAD";

    private final String EMISION = "EMISIÓN";
    private final String REGISTRO = "REGISTRO";

    private final String OCR = "OCR";
    private final String CIC = "CIC";

    private EditSpinner estados;
    private EditSpinner poblacion;

    private Collection<SepomexInformation> sepomexSearchResult;
    private DataBaseAccessHelper dataBaseAccessHelper;
    private ArrayList<StateInfo> stateList;

    private boolean setAddress1st = false;

    final private int CALLE_MAX_SIZE = 30;
    final private int NO_EXT_MAZ_SIZE = 6;
    final private int CP_MAX_SIZE = 5;

    EditSpinner spestado;
    EditSpinner spestado_origen;
    EditSpinner splocalidad;


    private FCIdentificacionRequest fcIdentificacionRequest = new FCIdentificacionRequest();
    private FCComprobantesNegocioRequest fcComprobantesNegocioRequest = new FCComprobantesNegocioRequest();
    private FCDatosPersonalesRequest fcDatosPersonalesRequest = new FCDatosPersonalesRequest();

    public static Fragment_prestamos_solicitud_fincomun_4 newInstance(Object... data) {
        Fragment_prestamos_solicitud_fincomun_4 fragment = new Fragment_prestamos_solicitud_fincomun_4();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_prestamos_solicitud_fincomun_5, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        //Inicializar los spinner y la BD
        dataBaseAccessHelper = DataBaseAccessHelper.getInstance(getContext());
        stateList = (ArrayList<StateInfo>) new Estados().getListEstados();

        CApplication.setAnalytics(CApplication.ACTION.CB_FINCOMUN_Solicitud_credito);

        btn_next = getView().findViewById(R.id.btn_next);
        btn_save = getView().findViewById(R.id.btn_save);
        img_front = getView().findViewById(R.id.img_front);
        img_revert = getView().findViewById(R.id.img_revert);
        btn_take_1 = getView().findViewById(R.id.btn_take_1);
        btn_take_2 = getView().findViewById(R.id.btn_take_2);
        btn_take_3 = getView().findViewById(R.id.btn_take_3);
        btn_take_4 = getView().findViewById(R.id.btn_take_4);
        img_camera_1 = getView().findViewById(R.id.img_camera_1);
        img_camera_2 = getView().findViewById(R.id.img_camera_2);
        btn_ife = getView().findViewById(R.id.btn_ife);
        btn_ine = getView().findViewById(R.id.btn_ine);

        fcIdentificacionRequest = new FCIdentificacionRequest();
        fcIdentificacionRequest.setImagen(new ArrayList(Arrays.asList("", "")));

        fcComprobantesNegocioRequest = new FCComprobantesNegocioRequest();
        fcComprobantesNegocioRequest.setImagenNegocio(new ArrayList(Arrays.asList("", "")));

        btn_ine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_ine.setBackground(getContext().getDrawable(R.drawable.background_selector_blue));
                btn_ife.setBackground(getContext().getDrawable(R.drawable.background_selector_green));
                identificacion = 1;
            }
        });

        btn_ife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_ife.setBackground(getContext().getDrawable(R.drawable.background_selector_blue));
                btn_ine.setBackground(getContext().getDrawable(R.drawable.background_selector_green));
                identificacion = 2;
            }
        });

        btn_take_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                front = true;
                getContextMenu().showAlertINE(identificacion, true, new IClickView() {
                    @Override
                    public void onClick(Object... data) {
                        setOCRAction();
                    }
                });
            }
        });

        btn_take_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                front = false;
                setOCRAction();
            }
        });

        btn_take_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                negocio = true;
                HOcrActivity.take_btn = true;
                setOCRAction();
            }
        });

        btn_take_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                domicilio = true;
                HOcrActivity.take_btn = true;
                setOCRAction();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continuarFlujo();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionApp.getInstance().getFcRequestDTO().setStep(Globals.NUMBER.TRES);
                AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());
                getContext().alert("Datos Guardados");
            }
        });

        configCampos();

        initFragmentAviso();

        validate();

    }

    public void initFragmentAviso(){

        camposAviso = new ArrayList<>();

        ITextChanged iTextChanged = new ITextChanged() {
            @Override
            public void onChange() {
                validate();
            }
            @Override
            public void onMaxLength() {

            }
        };
        //0
        camposAviso.add(new HEditText((EditText) getView().findViewById(R.id.edt_phone),
                true, 10, 10, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_phone)));
        //1
        camposAviso.add(new HEditText((EditText) getView().findViewById(R.id.edt_email),
                true, 75, 1, HEditText.Tipo.EMAIL,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_email)));

        btn_aviso = getView().findViewById(R.id.btn_aviso);

        btn_aviso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().setFragment(Fragment_prestamos_solicitud_fincomun_3.newInstance());
            }
        });

        check_acept = getView().findViewById(R.id.check_acept);
        check_direccion = getView().findViewById(R.id.check_direccion);

        check_acept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        check_direccion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (check_direccion.isChecked())
                    btn_take_4.setVisibility(View.GONE);
                else
                    btn_take_4.setVisibility(View.VISIBLE);


                validate();
            }
        });

        camposAviso.get(0).getEditText().setText("" + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone());
        camposAviso.get(1).getEditText().setText("" + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mail());

        validate();
    }

    public void validate(){

        btn_save.setEnabled(false);
        btn_next.setEnabled(false);

        for(int i=0; i<camposAviso.size(); i++)
            if(!camposAviso.get(i).isValid()){
                return;
            }

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                getContext().logger(campos.get(i).getText() + "");
                return;
            }

        if(fcComprobantesNegocioRequest.getImagenNegocio().get(0).isEmpty())
            return;

        if(fcIdentificacionRequest.getImagen().get(0).isEmpty() || fcIdentificacionRequest.getImagen().get(1).isEmpty())
            return;

        if(!check_direccion.isChecked()){
            if(fcComprobantesNegocioRequest.getComprobanteDomicilio() == null || fcComprobantesNegocioRequest.getComprobanteDomicilio().isEmpty()) {
                return;
            }
        }

        if(!check_acept.isChecked()){
            return;
        }



        btn_save.setEnabled(true);
        btn_next.setEnabled(true);

    }

    public Bitmap getImage(String img) {
        byte[] decodedString = Base64.decode(img);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    private void setOCRAction() {

        getContextMenu().requestPermissions(new IRequestPermissions() {
            @Override
            public void onPostExecute() {



                HOcrActivity.matches = new ArrayList();

                if(!negocio && !domicilio) {

                    if(front) {

                        HOcrActivity.matches.add(new HOcrMatch(HOcrActivity.CURP_REGEX));
                        HOcrActivity.matches.add(new HOcrMatch(HOcrActivity.NOMBRE_REGEX));
                        HOcrActivity.matches.add(new HOcrMatch(HOcrActivity.DOMICILIO_REGEX));
                        HOcrActivity.matches.add(new HOcrMatch(HOcrActivity.CP_REGEX));
                        HOcrActivity.matches.add(new HOcrMatch(HOcrActivity.SEXO_REGEX));
                        HOcrActivity.matches.add(new HOcrMatch(HOcrActivity.CLAVE_REGEX));
                        HOcrActivity.matches.add(new HOcrMatch(HOcrActivity.ESTADO_REGEX));
                        HOcrActivity.matches.add(new HOcrMatch(HOcrActivity.MUNICIPIO_REGEX));
                        HOcrActivity.matches.add(new HOcrMatch(HOcrActivity.LOCALIDAD_REGEX));
                        HOcrActivity.matches.add(new HOcrMatch(HOcrActivity.EMISION_REGEX));
                        HOcrActivity.matches.add(new HOcrMatch(HOcrActivity.REGISTRO_REGEX));
                        HOcrActivity.matches.add(new HOcrMatch(HOcrActivity.IFE));

                    } else {

                        HOcrActivity.matches.add(new HOcrMatch(HOcrActivity.OCR_REGEX));
                        HOcrActivity.matches.add(new HOcrMatch(HOcrActivity.CIC_REGEX));

                    }

                }



                HOcrActivity.setListener(new IOcrListener() {
                    @Override
                    public void execute(List<HOcrMatch> data, Bitmap bitmap) {

                        try {

                            String strImg = Utils.convert(bitmap);

                            if(negocio) {
                                negocio = false;

                                fcComprobantesNegocioRequest.getImagenNegocio().set(0, strImg);
                                img_camera_1.setImageDrawable(getContextMenu().getResources().getDrawable(R.drawable.c_exitoso));
                                img_camera_1.setImageTintList(null);

                                validate();

                                return;
                            }

                            if(domicilio) {
                                domicilio = false;

                                fcComprobantesNegocioRequest.setComprobanteDomicilio(strImg);
                                img_camera_2.setImageDrawable(getContextMenu().getResources().getDrawable(R.drawable.c_exitoso));
                                img_camera_2.setImageTintList(null);

                                validate();

                                return;
                            }


                            if(front) {

                                img_front.setImageBitmap(getImage(strImg));

                                fcIdentificacionRequest.getImagen().add(0, strImg);

                                String ife = HOcrActivity.matches.get(11).getValue();

                                if(!ife.isEmpty()) {
                                    identificacion = 2;
                                    campos.get(15).setRequired(false);
                                } else {
                                    identificacion = 1;
                                    campos.get(15).setRequired(true);
                                }

                                campos.get(16).setRequired(true);

                                String curp = HOcrActivity.matches.get(0).getValue().replace(CURP, "");
                                String nombre = HOcrActivity.matches.get(1).getValue();
                                String domicilio = HOcrActivity.matches.get(2).getValue();
                                String cp = HOcrActivity.matches.get(3).getValue().replace(CP, "");
                                String sexo = HOcrActivity.matches.get(4).getValue().replace(SEXO, "");
                                String clave = HOcrActivity.matches.get(5).getValue().replace(CLAVE, "").replace("ELECTOR", "").trim();
                                String estado = HOcrActivity.matches.get(6).getValue().replace(ESTADO, "");
                                String municipio = HOcrActivity.matches.get(7).getValue().replace(MUNICIPIO, "");
                                String localidad = HOcrActivity.matches.get(8).getValue().replace(LOCALIDAD, "");
                                String emision = HOcrActivity.matches.get(9).getValue().replace(EMISION, "");
                                String registro = HOcrActivity.matches.get(10).getValue().replace(REGISTRO, "");

                                String[] nombres = nombre.split("\\n");
                                String[] domicilios = domicilio.split("\\n");

                                //fcIdentificacionRequest.setEstado(estado);
                                //fcIdentificacionRequest.setEntidad(estado);
                                //fcIdentificacionRequest.setMunicipio(municipio);
                                //fcIdentificacionRequest.setLocalidad(localidad);

                                fcIdentificacionRequest.setCurp(curp);

                                if(nombres.length != 0) {

                                    try {
                                        fcIdentificacionRequest.setApellidoPaterno(nombres[1]);
                                        fcIdentificacionRequest.setApellidoMaterno(nombres[2]);
                                        fcIdentificacionRequest.setNombre(nombres[3]);
                                    } catch (Exception ex) { }

                                }

                                if(domicilios.length != 0) {

                                    try {

                                        fcIdentificacionRequest.setCalle(domicilios[1]);
                                        fcIdentificacionRequest.setColonia(domicilios[2].replace(cp,"").trim());
                                        fcIdentificacionRequest.setMunicipio(domicilios[3].split(",")[0].trim());
                                        fcIdentificacionRequest.setEstado(domicilios[3].split(",")[1].trim());

                                        if(cp.isEmpty())
                                            cp = HOcrActivity.matcher(HOcrActivity.CP_REGEX, domicilios[2]).trim();

                                        fcIdentificacionRequest.setColonia(domicilios[2].replace(cp,"").trim());

                                    } catch (Exception ex) {

                                        try {

                                            fcIdentificacionRequest.setCalle("");
                                            fcIdentificacionRequest.setColonia(domicilios[1].replace(cp,"").trim());
                                            fcIdentificacionRequest.setMunicipio(domicilios[2].split(",")[0].trim());
                                            fcIdentificacionRequest.setEstado(domicilios[2].split(",")[1].trim());

                                            if(cp.isEmpty())
                                                cp = HOcrActivity.matcher(HOcrActivity.CP_REGEX, domicilios[1]).trim();

                                            fcIdentificacionRequest.setColonia(domicilios[1].replace(cp,"").trim());

                                        } catch (Exception e) {

                                        }

                                    }
                                }

                                fcIdentificacionRequest.setCp(cp);
                                fcIdentificacionRequest.setClaveElector(clave);

                                try {

                                    fcIdentificacionRequest.setAnioEmision(Integer.parseInt(emision.trim()));
                                    fcIdentificacionRequest.setAnioRegistro(Integer.parseInt(registro.trim()));

                                } catch (Exception ex) {}

                                if(curp.length() > 0) {
                                    String date = "19" + curp.substring(4, 12);
                                    date =  date.substring(6, 8) + "/" + date.substring(4, 6) + "/" + date.substring(0, 4);
                                    fcIdentificacionRequest.setFechaNacimiento(date);
                                    fcIdentificacionRequest.setRfc(curp.substring(0, 10) + "333");

                                    SessionApp.getInstance().getFcRequestDTO().setGenero(curp.substring(10, 11));

                                }

                                front = !front;
                                if(!front) {
                                    getContextMenu().showAlertINE(identificacion, false, new IClickView() {
                                        @Override
                                        public void onClick(Object... data) {
                                            setOCRAction();
                                        }
                                    });
                                }

                            } else {

                                img_revert.setImageBitmap(getImage(strImg));

                                fcIdentificacionRequest.getImagen().add(1, strImg);

                                String ocr = HOcrActivity.matches.get(0).getValue().replaceAll(">>", "").replaceAll("<<", "").trim();
                                fcIdentificacionRequest.setOcr(ocr);

                                String cic = HOcrActivity.matches.get(1).getValue().replaceAll(">>", "").replaceAll("<<", "").trim();
                                fcIdentificacionRequest.setCic(cic);

                                getContextMenu().closeAlertFC();

                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();

                            if(front) {
                                front = !front;
                                getContextMenu().showAlertINE(identificacion, false, new IClickView() {
                                    @Override
                                    public void onClick(Object... data) {
                                        setOCRAction();
                                    }
                                });
                            } else {
                                front = true;
                            }

                        }

                        setValuesOCR();

                    }
                });

                getContext().startActivity(HOcrActivity.class);
            }
        }, new String[]{ Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE });

    }

    public void takePhoto() {

        getContextMenu().requestPermissions(new IRequestPermissions() {
            @Override
            public void onPostExecute() {

                setResultCamera();

                uri = Utils.getFilePhoto(getContext());

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                getContext().startActivityForResult(i, 1);

            }
        }, new String[]{ Manifest.permission.CAMERA });

    }

    public void setResultCamera(){

        getContext().setOnActivityResult(new IActivityResult() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent intent) {

                try {

                    final Bitmap image = Utils.decodeSampledBitmapFromFile();
                    String strImg = Utils.convert(image);
                    Utils.deleteFilePhoto();
                    uri = null;

                    fcComprobantesNegocioRequest.getImagenNegocio().set(0, strImg);
                    img_camera_1.setImageDrawable(getContextMenu().getResources().getDrawable(R.drawable.c_exitoso));
                    img_camera_1.setImageTintList(null);

                    validate();

                } catch (Exception e) {

                }

            }
        });
    }

    public void configCampos() {

        campos = new ArrayList();

        ITextChanged iTextChanged = new ITextChanged() {
            @Override
            public void onChange() {
                validate();
            }
            @Override
            public void onMaxLength() {
            }
        };

        //0
        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_name),
                true, 30, 1, HEditText.Tipo.TEXTO_Ñ,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_name)));
        //1
        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_surname_f),
                true, 30, 1, HEditText.Tipo.TEXTO_Ñ,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_surname_f)));
        //2
        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_surname_m),
                true, 30, 1, HEditText.Tipo.TEXTO_Ñ,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_surname_m)));
        //3
        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_birthday),
                true, 10, 10, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_birthday)));

        //////////////////////////////////////

        //4
        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_street),
                true, 50, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_street)));
        //5
        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_number_iside),
                false, 5, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_number_iside)));
        //6
        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_number_exterior),
                true, 5, 1, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_number_exterior)));
        //7
        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_suburd),
                true, 50, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_suburd)));
        //8
        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_cp),
                true, 5, 5, HEditText.Tipo.NUMERO, new ITextChanged() {
            @Override
            public void onChange() {
                if(campos.get(8).getText().length() == 5)
                    sepomex(campos.get(8).getText());

                validate();
            }

            @Override
            public void onMaxLength() {

            }
        },
                (TextView) getView().findViewById(R.id.text_error_cp)));
        //9
        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_antiquity),
                true, 10, 10, HEditText.Tipo.NONE, iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_antiquity)));
        //10
        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_state),
                true, 50, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_state)));

        //11
        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_municipality),
                true, 50, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_municipality)));

        //////////////////////////////////////

        //12
        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_elector_key),
                true, 18, 18 , HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_elector_key)));

        //13
        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_curp),
                true, 18, 18, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_curp)));
        //14
        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_rfc),
                true, 13, 13, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_rfc)));
        //15
        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_cic), identificacion ==1
                , 10, 10, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_cic)));
        //16
        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_ocr),
                true, 13, 13, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_ocr)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_register_year),
                true, 4, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_register_year)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_register_issue),
                true, 4, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_register_issue)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_general_activity),
                true, 50, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_general_activity)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_economic_activity),
                true, 50, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_economic_activity)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_gender),
                true, 15, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_gender)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_state_birth),
                true, 50, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_state_birth)));

        LinearLayout layout_registro = getView().findViewById(R.id.layout_registro);
        layout_registro.setOnClickListener(view -> {
            getContextMenu().showPickerYear(R.string.anio_registro,  new IClickView() {
                @Override
                public void onClick(Object... data) {
                    campos.get(17).getEditText().setText(data[0].toString());
                }
            });
        });

        LinearLayout layout_emision = getView().findViewById(R.id.layout_emision);
        layout_emision.setOnClickListener(view -> {
            getContextMenu().showPickerYear(R.string.anio_emision,  new IClickView() {
                @Override
                public void onClick(Object... data) {
                    campos.get(18).getEditText().setText(data[0].toString());
                }
            });
        });

        LinearLayout layout_fecha_antiguedad = getView().findViewById(R.id.layout_fecha_antiguedad);
        layout_fecha_antiguedad.setOnClickListener(view -> {
            getContextMenu().showPicker(R.string.placeholderParameterAntiguedad, false, new IClickView() {
                @Override
                public void onClick(Object... data) {
                    campos.get(9).getEditText().setText(data[0].toString());
                }
            });
        });

        LinearLayout layout_fecha_nacimiento = getView().findViewById(R.id.layout_fecha_nacimiento);
        layout_fecha_nacimiento.setOnClickListener(view -> {
            getContextMenu().showPicker(R.string.placeholderParameterBirthDate, true, new IClickView() {
                @Override
                public void onClick(Object... data) {
                    campos.get(3).getEditText().setText(data[0].toString());
                }
            });
        });

        setList();

        getStates();

        setAddress();

        setEstados();

        // Verificar si ya tiene algún domicilio y pintarlo
        if(AppPreferences.getUserProfile().getQpay_object()[0].userHaveTheCompleteRegistration()){
            campos.get(8).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code());
        } else {
            // Geolocalizar y obtener CP
            getGeoInfo();
        }

    }

    public void setValuesOCR() {

        campos.get(0).setText(fcIdentificacionRequest.getNombre() + "");
        campos.get(1).setText(fcIdentificacionRequest.getApellidoPaterno() + "");
        campos.get(2).setText(fcIdentificacionRequest.getApellidoMaterno() + "");
        campos.get(3).setText(fcIdentificacionRequest.getFechaNacimiento() + "");

        //////////////////////////////////////

        campos.get(4).setText(fcIdentificacionRequest.getCalle() + "");
        campos.get(5).setText("");
        campos.get(6).setText(fcIdentificacionRequest.getNumExterior() + "");
        campos.get(7).setText(fcIdentificacionRequest.getColonia() + "");
        campos.get(8).setText(fcIdentificacionRequest.getCp() + "");

        if(!(fcIdentificacionRequest.getCp() + "").isEmpty()) {
            sepomex(campos.get(8).getText());
        }

        campos.get(9).setText(fcIdentificacionRequest.getAntiguedad() + "");

        //campos.get(10).setIdentifier(fcIdentificacionRequest.getEstado() + "");
        //campos.get(11).setIdentifier(fcIdentificacionRequest.getMunicipio() + "");

        //////////////////////////////////////

        campos.get(12).setText(fcIdentificacionRequest.getClaveElector() + "");
        campos.get(13).setText(fcIdentificacionRequest.getCurp() + "");
        campos.get(14).setText(fcIdentificacionRequest.getRfc() + "");
        campos.get(15).setText(fcIdentificacionRequest.getCic() + "");
        campos.get(16).setText(fcIdentificacionRequest.getOcr() + "");
        campos.get(17).setText(fcIdentificacionRequest.getAnioRegistro() + "");
        campos.get(18).setText(fcIdentificacionRequest.getAnioEmision() + "");

        campos.get(19).setIdentifier(fcIdentificacionRequest.getId_actividad_general() + "");
        campos.get(20).setIdentifier(fcIdentificacionRequest.getId_actividad_economica() + "");

        if("H".equals(SessionApp.getInstance().getFcRequestDTO().getGenero())) {
            spgenero.selectItem(1);
            campos.get(21).setIdentifier("M");
        } else if("M".equals(SessionApp.getInstance().getFcRequestDTO().getGenero())) {
            spgenero.selectItem(0);
            campos.get(21).setIdentifier("F");
        }

    }

    public void setList() {

        splocalidad = getView().findViewById(R.id.edt_suburd);
        spestado = getView().findViewById(R.id.edt_state);
        spestado_origen = getView().findViewById(R.id.edt_state_birth);

        EditSpinner spgenerl = getView().findViewById(R.id.edt_general_activity);
        EditSpinner speconomica = getView().findViewById(R.id.edt_economic_activity);

        spgenero = getView().findViewById(R.id.edt_gender);

        spgenero.setLines(3);
        spgenero.setSingleLine(false);
        splocalidad.setLines(3);
        splocalidad.setSingleLine(false);
        spestado.setLines(3);
        spestado.setSingleLine(false);
        spestado_origen.setLines(3);
        spestado_origen.setSingleLine(false);
        spgenerl.setLines(3);
        spgenerl.setSingleLine(false);
        speconomica.setLines(3);
        speconomica.setSingleLine(false);

        ArrayList<ModelSpinner> genero = new ArrayList();

        ArrayList<ModelSpinner> localidad = new ArrayList();
        ArrayList<ModelSpinner> estado = new ArrayList();

        ArrayList<ModelSpinner> general = new ArrayList();
        ArrayList<ModelSpinner> economica = new ArrayList();

        int index = 0;
        int position = -1;

        getContextMenu().logger(SessionApp.getInstance().getCatalog().getLugarNacimiento());

        for(LugarNacimiento est : SessionApp.getInstance().getCatalog().getLugarNacimiento()) {

            estado.add(new ModelSpinner(est.getName(), est.getValue()));

            Integer num = Integer.parseInt(est.getValue().trim());
            Integer val = Integer.parseInt(fcIdentificacionRequest.getEstado() != null ? fcIdentificacionRequest.getEstado() : "0");

            if(num == val)
                position = index;

            index++;
        }

        for(Localidad loc : SessionApp.getInstance().getCatalog().getLocalidad())
            localidad.add(new ModelSpinner(loc.getName(), loc.getValue()));

        int cgen = 0, ceco = 0;

        int cont = 0;
        for(ActividadGeneral act : SessionApp.getInstance().getCatalog().getActividadGeneral()) {

            if("12".equals(act.getValue()))
                cgen = cont;

            general.add(new ModelSpinner(act.getName(), act.getValue()));

            cont ++;
        }

        cont = 0;
        for(ActividadEconomica act : SessionApp.getInstance().getCatalog().getActividadEconomica()) {

            if("6131023".equals(act.getValue()))
                ceco = cont;

            economica.add(new ModelSpinner(act.getName(), act.getValue()));

            cont ++;
        }

        for(Genero gen : SessionApp.getInstance().getCatalog().getGenero())
            genero.add(new ModelSpinner(gen.getName(), gen.getValue()));

        getContextMenu().setDataSpinner(splocalidad, localidad, data -> campos.get(11).setIdentifier(data[0]));

        splocalidad.selectItem(0);

        campos.get(11).setIdentifier(localidad.get(0).getValue());

        getContextMenu().setDataSpinner(spgenerl, general, data -> campos.get(19).setIdentifier(data[0]));

        getContextMenu().setDataSpinner(speconomica, economica, data -> campos.get(20).setIdentifier(data[0]));

        getContextMenu().setDataSpinner(spgenero, genero, data -> campos.get(21).setIdentifier(data[0]));

        spgenerl.selectItem(cgen);
        speconomica.selectItem(ceco);

        campos.get(19).setIdentifier("12");
        campos.get(20).setIdentifier("6131023");

        if(position > -1) {
            setMunicipios(estado.get(position).getValue());
            campos.get(11).setIdentifier(estado.get(position).getValue());
        }

    }

    public void setMunicipios(String data) {

        EditSpinner spmunicipio = getView().findViewById(R.id.edt_municipality);
        spmunicipio.setLines(3);
        spmunicipio.setSingleLine(false);

        ArrayList<ModelSpinner> municipio = new ArrayList();

        campos.get(11).setIdentifier(data);

        for(Municipio mun : SessionApp.getInstance().getCatalog().getMunicipio())
            if(mun.getValue().substring(0,data.length()).equals(data))
                municipio.add(new ModelSpinner(mun.getName(), mun.getValue()));

        campos.get(11).setText("");
        //VERIFICAR
        getContextMenu().setDataSpinner(spmunicipio, municipio, data1 -> campos.get(11).setIdentifier(data1[0]));

    }

    public void continuarFlujo() {

        fcIdentificacionRequest.setNumExterior("NA");
        fcIdentificacionRequest.setNumInterior("NA");
        fcIdentificacionRequest.setManzana("NA");
        fcIdentificacionRequest.setLote("NA");

        fcIdentificacionRequest.setNombre(campos.get(0).getText());
        fcIdentificacionRequest.setApellidoPaterno(campos.get(1).getText());
        fcIdentificacionRequest.setApellidoMaterno(campos.get(2).getText());
        fcIdentificacionRequest.setFechaNacimiento(campos.get(3).getText());

        fcIdentificacionRequest.setCalle_1("NA");
        fcIdentificacionRequest.setCalle_2("NA");

        //////////////////////////////////////

        fcIdentificacionRequest.setCalle(campos.get(4).getText());
        fcIdentificacionRequest.setNumInterior(campos.get(5).getText());
        fcIdentificacionRequest.setNumExterior(campos.get(6).getText());
        fcIdentificacionRequest.setColonia(campos.get(7).getText());
        fcIdentificacionRequest.setCp(campos.get(8).getText());
        fcIdentificacionRequest.setAntiguedad(campos.get(9).getText());
        fcIdentificacionRequest.setEstado(campos.get(10).getText());
        fcIdentificacionRequest.setEntidad(campos.get(10).getIdentifier());
        fcIdentificacionRequest.setMunicipio(campos.get(11).getIdentifier());
        fcIdentificacionRequest.setLocalidad(SessionApp.getInstance().getCatalog().getLocalidad().get(0).getValue());

        for(Municipio mun : SessionApp.getInstance().getCatalog().getMunicipio()) {
            if(stripAccents(mun.getName()).toUpperCase().equals(stripAccents(campos.get(11).getText()).toUpperCase())) {
                fcIdentificacionRequest.setMunicipio(mun.getValue());
                break;
            }
        }

        //fcIdentificacionRequest.setMunicipio("9010");



        //////////////////////////////////////

        fcIdentificacionRequest.setClaveElector(campos.get(12).getText());
        fcIdentificacionRequest.setCurp(campos.get(13).getText());
        fcIdentificacionRequest.setRfc(campos.get(14).getText());
        fcIdentificacionRequest.setCic(campos.get(15).getText());
        fcIdentificacionRequest.setOcr(campos.get(16).getText());
        fcIdentificacionRequest.setAnioRegistro(Integer.parseInt(campos.get(17).getText()));
        fcIdentificacionRequest.setAnioEmision(Integer.parseInt(campos.get(18).getText()));
        fcIdentificacionRequest.setId_actividad_general("12");
        fcIdentificacionRequest.setId_actividad_economica("6131023");
        fcIdentificacionRequest.setIdIdentificacion("1");

        SessionApp.getInstance().getFcRequestDTO().setIdentificacion(fcIdentificacionRequest);
        SessionApp.getInstance().getFcRequestDTO().setStep(Globals.NUMBER.UNO);
        AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());

        getContextMenu().getTokenFC(new IFunction<String>() {
            @Override
            public void execute(String... data) {
                postAvisoPrivacidad(data[0]);
            }
        });

    }

    public static String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

    public void postAvisoPrivacidad(String token){

        getContext().loading(true);

        SessionApp.getInstance().getFcRequestDTO().setToken(token);
        FCAvisoPrivacidadRequest fcAvisoPrivacidadRequest = new FCAvisoPrivacidadRequest();

        fcAvisoPrivacidadRequest.setTelefono(camposAviso.get(0).getText());
        fcAvisoPrivacidadRequest.setEmail(camposAviso.get(1).getText());
        fcAvisoPrivacidadRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcAvisoPrivacidadRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());

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
                } else {
                    SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                    SessionApp.getInstance().getFcRequestDTO().setToken(response.getTokenJwt());
                    AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());

                    postIdentificacion(token);
                }


            }
            @Override
            public void onFailure(String mensaje) {
                getContext().loading(false);
                getContext().alert(mensaje);
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
        fcIdentificacionRequest.setNumCliente("");
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

                if(response.getRespuesta().getCodigo() < 0) {
                    getContext().loading(false);
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                } else {
                    SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                    SessionApp.getInstance().getFcRequestDTO().setToken(response.getTokenJwt());
                    SessionApp.getInstance().getFcRequestDTO().setNumCliente(response.getNumCliente());
                    AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());

                    postDatosPersonales(token);
                }

            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
            }
        });

    }

    public void postDatosPersonales(String token){

        fcDatosPersonalesRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcDatosPersonalesRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcDatosPersonalesRequest.setNumCliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());

        SessionApp.getInstance().getFcRequestDTO().setToken(token);

        fcDatosPersonalesRequest.setNacionalidad("484");
        fcDatosPersonalesRequest.setEstadoCivil("1");
        fcDatosPersonalesRequest.setPais("484");
        fcDatosPersonalesRequest.setOcupacion("3");
        fcDatosPersonalesRequest.setGradoEstudios("1");
        fcDatosPersonalesRequest.setLugarNacimiento(campos.get(22).getIdentifier());
        fcDatosPersonalesRequest.setTipo_vivienda(0);
        fcDatosPersonalesRequest.setHorario_contacto(2);
        fcDatosPersonalesRequest.setDependientes_economicos(0);

        fcDatosPersonalesRequest.setGenero(campos.get(21).getIdentifier());

        fcDatosPersonalesRequest.setUsuario(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id());

        SessionApp.getInstance().getFcRequestDTO().setDatos_personales(fcDatosPersonalesRequest);

        Logger.d("REQUEST : "  + new Gson().toJson(fcDatosPersonalesRequest));

        DatosPersonales datosPersonales = new DatosPersonales(getContext());
        datosPersonales.postDatosPersonales(fcDatosPersonalesRequest, new DatosPersonales.onRequest() {
            @Override
            public <E> void onSuccess(E e) {

                FCDatosPersonalesResponse response = (FCDatosPersonalesResponse)e;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getRespuesta().getCodigo() < 0) {
                    getContext().loading(false);
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                } else {
                    SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                    AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());

                    postDatosNegocio(token);
                }

            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
            }
        });
    }

    public void postDatosNegocio(String token){

        SessionApp.getInstance().getFcRequestDTO().setToken(token);
        FCDatosNegocioRequest fcDatosNegocioRequest = new FCDatosNegocioRequest();

        fcDatosNegocioRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcDatosNegocioRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcDatosNegocioRequest.setNumCliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());

        fcDatosNegocioRequest.setNombreNegocio(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name());
        fcDatosNegocioRequest.setSector("32");
        fcDatosNegocioRequest.setGiro("001");
        fcDatosNegocioRequest.setAntiguedad(fcIdentificacionRequest.getAntiguedad());
        fcDatosNegocioRequest.setTelefono(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone());
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

                if(response.getRespuesta().getCodigo() < 0) {
                    getContext().loading(false);
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                } else {
                    postVentaCompra(token);
                }

            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
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

                if(response.getRespuesta().getCodigo() < 0) {
                    getContext().loading(false);
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                } else {
                    postGastosNegocio(token);
                }

            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert(s);
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

                if(response.getRespuesta().getCodigo() < 0) {
                    getContext().loading(false);
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                } else {
                    SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                    SessionApp.getInstance().getFcRequestDTO().setToken(response.getTokenJwt());
                    AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());

                    postComprobantesNegocio(token);

                }

            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert("SDK : " + s);
            }

        });
    }

    public void postComprobantesNegocio(String token){

        SessionApp.getInstance().getFcRequestDTO().setToken(token);

        fcComprobantesNegocioRequest.setFolio(SessionApp.getInstance().getFcRequestDTO().getFolio());
        fcComprobantesNegocioRequest.setTokenJwt(SessionApp.getInstance().getFcRequestDTO().getToken());
        fcComprobantesNegocioRequest.setNumCliente(SessionApp.getInstance().getFcRequestDTO().getNumCliente());

        fcComprobantesNegocioRequest.setImagenNegocio(new ArrayList(Arrays.asList(fcComprobantesNegocioRequest.getImagenNegocio().get(0))));

        if(fcComprobantesNegocioRequest.getComprobanteDomicilio() != null && fcComprobantesNegocioRequest.getComprobanteDomicilio().isEmpty())
            fcComprobantesNegocioRequest.setComprobanteDomicilio(fcComprobantesNegocioRequest.getImagenNegocio().get(0));

        fcComprobantesNegocioRequest.setEstadoCuenta(fcComprobantesNegocioRequest.getImagenNegocio().get(0));

        Logger.d("REQUEST : "  + new Gson().toJson(fcComprobantesNegocioRequest));

        SessionApp.getInstance().getFcRequestDTO().setComprobante_negocio(fcComprobantesNegocioRequest);

        ComprobantesNegocio comprobantesNegocio = new ComprobantesNegocio(getContext());
        comprobantesNegocio.postComprobantesNegocio(fcComprobantesNegocioRequest , new ComprobantesNegocio.onRequest() {
            @Override
            public <E> void onSuccess(E e) {

                FCComprobantesNegocioResponse response = (FCComprobantesNegocioResponse)e;

                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if(response.getRespuesta().getCodigo() < 0) {
                    getContext().loading(false);
                    getContext().alert(response.getRespuesta().getDescripcion().get(0));
                } else {

                    SessionApp.getInstance().getFcRequestDTO().setFolio(response.getFolio());
                    SessionApp.getInstance().getFcRequestDTO().setToken(response.getTokenJwt());
                    AppPreferences.setFCRequest(SessionApp.getInstance().getFcRequestDTO());

                    Fragment_prestamos_solicitud_fincomun_7.fcDatosPersonalesRequest = fcDatosPersonalesRequest;
                    Fragment_prestamos_solicitud_fincomun_7.fcIdentificacionRequest = fcIdentificacionRequest;

                    getContextMenu().setFragment(Fragment_prestamos_solicitud_fincomun_7.newInstance(), true);
                }


            }
            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert("SDK : " + s);
            }
        });

    }

    private boolean isSameAdress(){
        if(campos.get(4).getText().trim().compareTo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_street().trim())!=0){
            return false;
        }
        if(campos.get(6).getText().trim().compareTo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_external_number().trim())!=0){
            return false;
        }
        if(campos.get(8).getText().trim().compareTo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code().trim())!=0){
            return false;
        }
        StateInfo userState = getStateInfo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_state());
        if(campos.get(10).getText().trim().compareTo(userState.getState_name())!=0){
            return false;
        }
        if(campos.get(11).getText().trim().compareTo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_municipality().trim())!=0){
            return false;
        }
        if(campos.get(7).getText().trim().compareTo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_suburb().trim())!=0){
            return false;
        }
        return true;
    }

    public void setEstados(){

        ArrayList<ModelSpinner> estados = new ArrayList();

        for(int i=0; i<stateList.size(); i++)
            estados.add(new ModelSpinner(stateList.get(i).getState_name(), stateList.get(i).getState_code()));

        getContextMenu().setDataSpinner(spestado, estados, data1 -> campos.get(10).setIdentifier(Integer.parseInt(data1[0]) + ""));

        getContextMenu().setDataSpinner(spestado_origen, estados, data1 -> campos.get(22).setIdentifier(Integer.parseInt(data1[0]) + ""));

    }

    private ArrayList<StateInfo> getStates() {

        stateList = (ArrayList<StateInfo>) new Estados().getListEstados();

        setEstados();

        return stateList;
    }

    public void sepomex(String codigo){

        dataBaseAccessHelper.open();
        sepomexSearchResult = dataBaseAccessHelper.findPostalCode(codigo);
        dataBaseAccessHelper.close();

        if(sepomexSearchResult != null && sepomexSearchResult.size() > 0){

            ArrayList<ModelSpinner> poblados = new ArrayList();

            for(int i=0; i<sepomexSearchResult.size(); i++) {
                poblados.add(new ModelSpinner(Tools.getOnlyNumbersAndLetters(((SepomexInformation)sepomexSearchResult.toArray()[i]).getD_asenta().replace(")","").replace("(",""))));
            }

            getContextMenu().setDataSpinner(splocalidad, poblados, data1 -> campos.get(7).setIdentifier(data1[0]));

            SepomexInformation info = (SepomexInformation) sepomexSearchResult.toArray()[0];

            for(int i=0; i<stateList.size(); i++){

                if(info.getD_estado().contains(stateList.get(i).getState_name())){

                    campos.get(10).setText(stateList.get(i).getState_name());
                    campos.get(10).setIdentifier(Integer.parseInt(stateList.get(i).getState_code()) + "");

                    campos.get(22).setText(stateList.get(i).getState_name());
                    campos.get(22).setIdentifier(Integer.parseInt(stateList.get(i).getState_code()) + "");

                    campos.get(11).setText(Tools.getOnlyNumbersAndLetters(info.getD_mnpio()));
                    //campos.get(11).setTag(info.getC_tipo_asenta());

                    campos.get(7).setText(Tools.getOnlyNumbersAndLetters(info.getD_asenta()));
                    //campos.get(7).setTag(info.getC_mnpio());

                    return;
                }

            }
        } else {
            //campos.get(7).removeSpinner();
        }

    }

    private void setAddress(){
        setAddress1st = false;
        campos.get(4).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_street());
        campos.get(6).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_external_number());
        campos.get(5).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_internal_number() != null ? AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_internal_number() : "");
        campos.get(8).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code());
        //Para estado se hace el set por el StateInfo
        StateInfo userState = getStateInfo(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_state());
        campos.get(10).setText(userState.getState_name());

        if(!userState.getState_code().isEmpty())
            campos.get(10).setIdentifier( Integer.parseInt(userState.getState_code()) + "");

        campos.get(7).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_suburb());
        campos.get(11).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_municipality());

    }

    private void getGeoInfo() {

        LastLocation coordenadas = CApplication.getLastLocation();
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {

            List<Address> addresses = geocoder.getFromLocation(coordenadas.getLatitude(), coordenadas.getLongitude(), 1);

            if(addresses.size()>0){

                String editCP = campos.get(8).getText();
                if(editCP!=null && editCP.isEmpty()) {

                    String calle = addresses.get(0).getThoroughfare();
                    calle = (calle != null ? calle.toUpperCase() : "");
                    calle = calle.replace("Ñ","N").replace("Á","A")
                            .replace("É","E").replace("Í","I")
                            .replace("Ó","O").replace("Ú","U");
                    calle = calle.replaceAll("[^A-Za-z1-9 ]+", "");
                    calle = (calle.length() > CALLE_MAX_SIZE ? calle.substring(0, CALLE_MAX_SIZE) : calle);
                    campos.get(4).setText(calle);

                    String noExt = addresses.get(0).getFeatureName();
                    noExt = (noExt != null ? noExt : "");
                    noExt = noExt.replaceAll("[^1-9]+", "");
                    noExt = (noExt.length() > NO_EXT_MAZ_SIZE ? noExt.substring(0, NO_EXT_MAZ_SIZE) : noExt);
                    campos.get(6).setText(noExt);

                    String cp = addresses.get(0).getPostalCode();
                    cp = (cp != null ? cp : "");
                    cp = (cp.length() > CP_MAX_SIZE ? cp.substring(0, CP_MAX_SIZE) : cp);
                    campos.get(8).setText(cp);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private StateInfo getStateInfo(String codeOrName) {

        try {
            Integer code = Integer.parseInt(codeOrName.trim());
            codeOrName = String.format("%02d",code);
        } catch (Exception e) {
            Log.e("info_negocio_1",e.getMessage());
        }

        codeOrName = (codeOrName==null?"":codeOrName.trim().toUpperCase());

        ArrayList<StateInfo> stateList = (ArrayList<StateInfo>) new Estados().getListEstados();

        for (StateInfo state:stateList){
            if(state.getState_code().compareTo(codeOrName)==0){
                return state;
            }
            if(state.getState_name().trim().toUpperCase().compareTo(codeOrName)==0){
                return state;
            }
        }

        return new StateInfo("","");
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }


}
