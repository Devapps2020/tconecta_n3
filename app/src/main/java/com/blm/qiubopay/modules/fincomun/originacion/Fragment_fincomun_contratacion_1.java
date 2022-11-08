package com.blm.qiubopay.modules.fincomun.originacion;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.FileUtils;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HOcrActivity;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.helpers.interfaces.IOcrListener;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.HOcrMatch;
import com.blm.qiubopay.models.bimbo.ModelItem;
import com.blm.qiubopay.modules.fincomun.components.FCEditText;
import com.blm.qiubopay.modules.fincomun.prestamo.Fragment_prestamos_solicitud_fincomun_3;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.com.fincomun.origilib.Http.Request.Originacion.FCBancosRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCComprobantesNegocioRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCDatosPersonalesRequest;
import mx.com.fincomun.origilib.Http.Request.Originacion.FCIdentificacionRequest;
import mx.com.fincomun.origilib.Http.Response.Originacion.FCBancosResponse;
import mx.com.fincomun.origilib.Model.Originacion.Bancos;
import mx.com.fincomun.origilib.Objects.Bancos.DHBancos;
import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IActivityResult;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.IRequestPermissions;

import static com.blm.qiubopay.utils.Utils.getImage;

public class Fragment_fincomun_contratacion_1 extends HFragment  implements IMenuContext {


    public static FCIdentificacionRequest fcIdentificacionRequest = new FCIdentificacionRequest();
    public static FCComprobantesNegocioRequest fcComprobantesNegocioRequest = new FCComprobantesNegocioRequest();
    public static Fragment_fincomun_oferta.Type type = Fragment_fincomun_oferta.Type.OFFER;
    private FCDatosPersonalesRequest fcDatosPersonalesRequest = new FCDatosPersonalesRequest();

    private boolean negocio = false;
    private boolean edoCuenta = false;
    private boolean address = false;
    private boolean firstLoad = true;
    private Boolean isLoading  = false;

    private View view;
    private HActivity context;
    private ArrayList<FCEditText> campos;
    private Object data;
    private Button btn_next;
    private CardView cv_IFE,cv_INE,cv_address,cv_business,cv_edo_cuenta;
    private ImageView iv_IFE, iv_INE,iv_address, iv_business,iv_edo_cuenta;
    private CheckBox check_address,check_consent,check_edoCuenta;
    private LinearLayout ll_address,ll_edo_cuenta;
    private TextView tv_instructions,tv_img_1,tv_img_2,tv_img_3,tv_img_4;
    private TextView tv_step,btn_aviso,tv_banco;

    public static Integer documento = 0;
    public static ArrayList<String> imagenes;

    public static String identificacion_frente;
    public static String identificacion_reverso;
    public static String comprobante_domicilio;
    public static String foto_firma;
    public static String selfie;
    public static String rfc;
    public static String fechaNacimiento;


    private Uri uri;
    boolean front = true;

    //OCR id's de retorno
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

    private ArrayList<ModelItem> bancos;

    public static Fragment_fincomun_contratacion_1 newInstance() {
        Fragment_fincomun_contratacion_1 fragment = new Fragment_fincomun_contratacion_1();
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
            data = new Gson().fromJson(getArguments().getString("Fragment_fincomun_contratacion_1"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_contratacion_1, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {
        SessionApp.getInstance().setRequiredEdoCuenta(true);

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
        fcIdentificacionRequest = new FCIdentificacionRequest();
        fcIdentificacionRequest.setImagen(new ArrayList(Arrays.asList("", "")));

        fcComprobantesNegocioRequest = new FCComprobantesNegocioRequest();
        fcComprobantesNegocioRequest.setImagenNegocio(new ArrayList(Arrays.asList("", "")));

        tv_banco = getView().findViewById(R.id.tv_banco);

        imagenes = new ArrayList<>();
        campos = new ArrayList<>();

        //0/cuenta
        campos.add(FCEditText.create(getView().findViewById(R.id.et_clabe))
                .setRequired(false)
                .setHint("CLABE interbancaria")
                .setMinimum(18)
                .setMaximum(18)
                .setType(FCEditText.TYPE.NUMBER)
                .setIconPass(R.drawable.show, R.drawable.show_off)
                .setAlert(R.string.text_input_required)
                .setTextChanged(new FCEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        if (!TextUtils.isEmpty(text) && text.length() == 3){

                            String val  = text.substring(0,3);
                            for (DHBancos banco: SessionApp.getInstance().getBancos()) {
                                if (banco.getDigitosClabe().equalsIgnoreCase(val)){
                                    tv_banco.setText(banco.getDescriBanco());
                                    tv_banco.setTag(banco.getClaveBanco());
                                }
                            }
                        }else if (!TextUtils.isEmpty(text) && text.length() <= 2){
                            tv_banco.setText("");
                            tv_banco.setTag("");
                        }
                    }
                })
        );
        tv_step = getView().findViewById(R.id.tv_step);
        cv_IFE = getView().findViewById(R.id.cv_IFE);
        cv_IFE.setTag("1");
        cv_IFE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    isLoading= true;
                    switch (String.valueOf(v.getTag())) {
                        case "1":
                            documento = 4;
                            identification(true);
                            break;
                        case "2":
                            firstLoad = false;
                            identification(true);
                            break;
                    }
                }

            }
        });
        cv_INE = getView().findViewById(R.id.cv_INE);
        cv_INE.setTag("1");
        cv_INE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    isLoading = true;
                    switch (String.valueOf(v.getTag())) {
                        case "1":
                            documento = 1;
                            identification(true);
                            break;
                        case "3":
                            firstLoad = false;
                            identification(false);
                            break;
                    }
                }
            }
        });
        btn_aviso = getView().findViewById(R.id.btn_aviso);
        btn_aviso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().setFragment(Fragment_prestamos_solicitud_fincomun_3.newInstance());
            }
        });

        tv_instructions = getView().findViewById(R.id.tv_instructions);
        tv_instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().showAlertINE(documento, true, new IClickView() {
                    @Override
                    public void onClick(Object... data) {
                        getContextMenu().showAlertINE(documento, false, new IClickView() {
                            @Override
                            public void onClick(Object... data) {
                            }
                        });
                    }
                });
            }
        });
        cv_address = getView().findViewById(R.id.cv_address);
        cv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    isLoading = true;
                    address = true;
                    HOcrActivity.take_btn = true;
                    setOCRAction();
                    isLoading = false;
                }
            }
        });
        cv_edo_cuenta = getView().findViewById(R.id.cv_edo_cuenta);
        cv_edo_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    isLoading = true;
                    showInstructionsEdoCuenta();
                }
            }
        });

        cv_business = getView().findViewById(R.id.cv_business);
        cv_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    isLoading = true;
                    showInstructionsStore();
                }
            }
        });

        iv_IFE = getView().findViewById(R.id.iv_IFE);
        iv_INE = getView().findViewById(R.id.iv_INE);
        iv_address = getView().findViewById(R.id.iv_address);
        iv_business = getView().findViewById(R.id.iv_business);
        iv_edo_cuenta = getView().findViewById(R.id.iv_edo_cuenta);

        tv_img_1 = getView().findViewById(R.id.tv_img_1);
        tv_img_2 = getView().findViewById(R.id.tv_img_2);
        tv_img_3 = getView().findViewById(R.id.tv_img_3);
        tv_img_4 = getView().findViewById(R.id.tv_img_4);

        ll_edo_cuenta = getView().findViewById(R.id.ll_edo_cuenta);
        ll_address = getView().findViewById(R.id.ll_address);
        check_address = getView().findViewById(R.id.check_address);
        check_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ll_address.setVisibility(View.GONE);
                }else {
                    ll_address.setVisibility(View.VISIBLE);
                }
            }
        });

        check_edoCuenta = getView().findViewById(R.id.check_edoCuenta);
        check_edoCuenta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SessionApp.getInstance().setRequiredEdoCuenta(isChecked);
                if (isChecked){
                    ll_edo_cuenta.setVisibility(View.VISIBLE);
                }else {
                    ll_edo_cuenta.setVisibility(View.GONE);
                    fcComprobantesNegocioRequest.setEstadoCuenta(null);
                    iv_edo_cuenta.setImageResource(R.drawable.estado_cta);
                }
            }
        });
        check_consent = getView().findViewById(R.id.check_consent);

        btn_next = getView().findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });


        configView();
        showInstructions();
        getContextMenu().saveRegister(CApplication.ACTION.CB_FINCOMUN_ORIGINACION_FOTOS);

        getContextMenu().getTokenFC((String... text) -> {
            if (text != null) {
                getBancos(text[0], new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        fillData();
                    }
                });
            }else{
                isLoading = false;
            }
        });
    }

    private void configView() {
        switch (type){
            case RECOMPRA:
                tv_step.setVisibility(View.GONE);
                break;
            case OFFER:
            case ANALISIS:
                break;
            case ADELANTO_VENTAS:
                ll_edo_cuenta.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void validate() {
        if(check_edoCuenta.isChecked()){
            campos.get(0).setRequired(true);
        }
        if(TextUtils.isEmpty(fcIdentificacionRequest.getImagen().get(0))){
            getContextMenu().alert("Captura el frente de tu identificación");
        }else if (TextUtils.isEmpty(fcIdentificacionRequest.getImagen().get(1))){
            getContextMenu().alert("Captura el reverso de tu identificación");
        }else if (!check_address.isChecked() && TextUtils.isEmpty(fcComprobantesNegocioRequest.getComprobanteDomicilio())){
            getContextMenu().alert("Captura tu comprobante de domicilio");
        }else if(TextUtils.isEmpty(fcComprobantesNegocioRequest.getImagenNegocio().get(0))){
            getContextMenu().alert("Captura tu la foto de tu negocio");
        }else if (check_edoCuenta.isChecked() && TextUtils.isEmpty(fcComprobantesNegocioRequest.getEstadoCuenta())){
            getContextMenu().alert("Captura tu estado de cuenta");
        }else if (check_edoCuenta.isChecked() && !campos.get(0).isValid()){
            getContextMenu().alert("Ingresa la cuenta CLABE en datos bancario");
        }else if (check_edoCuenta.isChecked() && TextUtils.isEmpty(tv_banco.getText().toString()) && tv_banco.getTag() != null){
            getContextMenu().alert("Ingresa la cuenta CLABE válida");
        }else if (!check_consent.isChecked()){
            getContextMenu().alert("Otorga tu consentimiento para el tratamiento de datos personales");
        }else {

            SessionApp.getInstance().setClabeEdoCuenta(campos.get(0).getText());

            if (SessionApp.getInstance().isRequiredEdoCuenta()) {
                SessionApp.getInstance().setIdBancoEdoCuenta(Integer.parseInt(String.valueOf(tv_banco.getTag())));
            }

            switch (type){
                case OFFER:
                    btn_next.setEnabled(false);
                    if (!Fragment_fincomun_contratacion_2.newInstance().isAdded()) {
                        getContext().setFragment(Fragment_fincomun_contratacion_2.newInstance());
                        btn_next.setEnabled(true);
                    }else{
                        btn_next.setEnabled(true);
                    }
                    break;
                case RECOMPRA:
                case ANALISIS:
                    Fragment_fincomun_contratacion_2.newInstance().fillData();
                    getContextMenu().backFragment();
                    break;
                case ADELANTO_VENTAS:
                    btn_next.setEnabled(false);
                    if (!Fragment_fincomun_contratacion_2.newInstance().isAdded()) {
                        getContext().setFragment(Fragment_fincomun_contratacion_2.newInstance());
                        btn_next.setEnabled(true);
                    }else{
                        btn_next.setEnabled(true);
                    }
                    break;
            }
        }
    }

    private void showInstructions() {
        getContextMenu().showAlertLayout(R.layout.item_requeriments_personal_data, new IClickView() {
            @Override
            public void onClick(java.lang.Object... data) {
                isLoading = false;
            }
        });
    }

    private void showInstructionsStore(){
        getContextMenu().showAlertLayout(R.layout.item_alert_store, new IClickView() {
            @Override
            public void onClick(java.lang.Object... data) {
                negocio = true;
                HOcrActivity.take_btn = true;
                setOCRAction();
                isLoading =  false;
            }
        });
    }

    private void showInstructionsEdoCuenta(){
        getContextMenu().showAlertLayout(R.layout.item_alert_edo_cuenta, new IClickView() {
            @Override
            public void onClick(java.lang.Object... data) {
                edoCuenta = true;
                HOcrActivity.take_btn = true;
                setOCRAction();
                isLoading = false;
            }
        });
    }

    public void identification(Boolean isFront) {

        front = isFront;
        getContextMenu().showAlertINE(documento, front, new IClickView() {
            @Override
            public void onClick(Object... data) {
                isLoading =  false;
                setOCRAction();
            }
        });
    }

    private void setOCRAction() {

        getContextMenu().requestPermissions(new IRequestPermissions() {
            @Override
            public void onPostExecute() {

                HOcrActivity.matches = new ArrayList();

                if(!negocio || !address || !edoCuenta) {

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
                                iv_business.setImageBitmap(getImage(strImg));

                                tv_img_3.setText("Tomar foto");

                                return;
                            }

                            if(edoCuenta) {
                                edoCuenta = false;

                                fcComprobantesNegocioRequest.setEstadoCuenta(strImg);
                                iv_edo_cuenta.setImageBitmap(getImage(strImg));

                                return;
                            }

                            if(address) {
                                address = false;

                                fcComprobantesNegocioRequest.setComprobanteDomicilio(strImg);
                                iv_address.setImageBitmap(getImage(strImg));

                                tv_img_4.setText("Tomar foto");

                                return;
                            }

                            if(front) {
                                cv_IFE.setVisibility(View.VISIBLE);
                                iv_IFE.setImageBitmap(getImage(strImg));

                                if (!TextUtils.isEmpty(fcIdentificacionRequest.getImagen().get(0))){
                                }
                                fcIdentificacionRequest.getImagen().remove(0);
                                fcIdentificacionRequest.getImagen().add(0, strImg);

                                cv_IFE.setTag("2");
                                cv_INE.setTag("3");
                                tv_img_1.setText("Captura frente");
                                tv_img_2.setText("Captura reverso");

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

                                fcIdentificacionRequest.setCp(cp);
                                fcIdentificacionRequest.setCurp(curp);
                                fcIdentificacionRequest.setNumInterior("");
                                fcIdentificacionRequest.setNumExterior("");

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

                                        cp = HOcrActivity.matcher(HOcrActivity.CP_REGEX, domicilios[2]).trim();
                                        fcIdentificacionRequest.setColonia(domicilios[2].replace(cp,"").trim());

                                    } catch (Exception ex) {

                                        try {

                                            fcIdentificacionRequest.setCalle("");
                                            fcIdentificacionRequest.setColonia(domicilios[1].replace(cp,"").trim());
                                            fcIdentificacionRequest.setMunicipio(domicilios[2].split(",")[0].trim());
                                            fcIdentificacionRequest.setEstado(domicilios[2].split(",")[1].trim());

                                            cp = HOcrActivity.matcher(HOcrActivity.CP_REGEX, domicilios[1]).trim();
                                            fcIdentificacionRequest.setColonia(domicilios[1].replace(cp,"").trim());

                                        } catch (Exception e) {

                                        }

                                    }
                                }


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

                                if (firstLoad) {
                                    front = !front;
                                }
                                if(!front) {
                                    getContextMenu().showAlertINE(1, false, new IClickView() {
                                        @Override
                                        public void onClick(Object... data) {
                                            setOCRAction();
                                        }
                                    });
                                }

                            } else {

                                iv_INE.setImageBitmap(getImage(strImg));
                                if (!TextUtils.isEmpty(fcIdentificacionRequest.getImagen().get(1))){
                                }
                                fcIdentificacionRequest.getImagen().remove(1);
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
                                if (firstLoad) {
                                    front = !front;
                                }
                                getContextMenu().showAlertINE(1, false, new IClickView() {
                                    @Override
                                    public void onClick(Object... data) {
                                        setOCRAction();
                                    }
                                });
                            } else {
                                front = true;
                            }

                        }

                       // setValuesOCR();

                    }
                });

                getContext().startActivity(HOcrActivity.class);
            }
        }, new String[]{ Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE });

    }

    public void takePhoto() {

        getContext().requestPermissions(new IRequestPermissions() {
            @Override
            public void onPostExecute() {

                setResultCamera();
                uri = Utils.getFilePhoto(getContext());
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                getContext().startActivityForResult(i, 1);

            }
        }, new String[]{ Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE });

    }

    public void savePhoto(Uri uri, Bitmap image) {

        try {

            String path = FileUtils.getPath(getContext(), uri).split(":")[0];
            Logger.d(path);

            if(front) {
                identificacion_frente = path;
                cv_IFE.setTag("2");
                cv_INE.setTag("3");
                tv_img_1.setText("Captura frente");
                tv_img_2.setText("Captura reverso");
                Glide.with(getContext()).load(path).into(iv_IFE);
            }else {
                identificacion_reverso = path;
                Glide.with(getContext()).load(path).into(iv_INE);

            }

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

    public void setResultCamera(){

        getContext().setOnActivityResult(new IActivityResult() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent intent) {

                try {

                    final Bitmap image = Utils.decodeSampledBitmapFromFile();
                    String strImg = Utils.convert(image);

                    savePhoto(uri, image);

                    if(documento == 1 || documento == 4) {

                        if(front)
                            imagenes.add(0, strImg);
                        else
                            imagenes.add(1, strImg);

                        if(String.valueOf(cv_IFE.getTag()).equalsIgnoreCase(String.valueOf(cv_INE.getTag()))){
                            front = !front;
                        }
                        if(!front) {
                            getContextMenu().showAlertINE(documento,false, new IClickView() {
                                @Override
                                public void onClick(Object... data) {
                                    takePhoto();
                                }
                            });
                            return;
                        }
                    } else {
                        imagenes.add(0, strImg);
                    }


                } catch (Exception e) {

                }

            }
        });
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    private void getBancos(String token,IFunction function) {
        getContext().loading(true);

        Bancos bancos = new Bancos(getContext());
        FCBancosRequest request = new FCBancosRequest();
        request.setTokenJwt(token);

        Logger.d("REQUEST : "  + new Gson().toJson(request));

        bancos.postBancos(request, new Bancos.onRequest() {
            @Override
            public <E> void onSuccess(E e) {
                getContext().loading(false);
                FCBancosResponse response = (FCBancosResponse)e;
                Logger.d("RESPONSE : "  + new Gson().toJson(response));

                if (response != null && response.getCodigo()==0){
                    SessionApp.getInstance().setBancos(response.getBancos());
                    function.execute();
                }else{
                    getContext().alert(response.getDescripcion());
                }

            }

            @Override
            public void onFailure(String s) {
                getContext().loading(false);
                getContext().alert("Error al cargar los bancos");

            }
        });

    }

    private void fillData() {
        bancos = new ArrayList();

        for(DHBancos banco : SessionApp.getInstance().getBancos()) {
            bancos.add(new ModelItem(banco.getDescriBanco(), banco.getClaveBanco()));
        }

        getContext().loading(false);

    }
}