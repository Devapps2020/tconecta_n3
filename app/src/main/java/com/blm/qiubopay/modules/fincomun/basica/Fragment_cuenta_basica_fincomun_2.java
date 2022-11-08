package com.blm.qiubopay.modules.fincomun.basica;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.FileUtils;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HOcrActivity;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.helpers.interfaces.IOcrListener;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.HOcrMatch;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import mx.com.fincomun.origilib.Http.Request.Apertura.FCAperturaCuentaRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCValidaCredencialRequest;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCNuevaSolicitudResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCValidaClienteResponse;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IActivityResult;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.IRequestPermissions;

public class Fragment_cuenta_basica_fincomun_2 extends HFragment implements IMenuContext {

    AlertDialog alertDialog;

    public static FCValidaCredencialRequest datosCredencial;
    public static FCAperturaCuentaRequest datosApertura;

    private ArrayList<HEditText> campos;
    Button btn_next;

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

    public static Fragment_cuenta_basica_fincomun_2 newInstance(Object... data) {
       Fragment_cuenta_basica_fincomun_2 fragment = new Fragment_cuenta_basica_fincomun_2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_cuenta_basica_fincomun_2, container, false), R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        datosCredencial = new FCValidaCredencialRequest();
        datosApertura = new FCAperturaCuentaRequest();

        imagenes = new ArrayList<>();
        campos = new ArrayList<>();

        ITextChanged iTextChanged = new ITextChanged() {
            @Override
            public void onChange() {
                validate();
            }
            @Override
            public void onMaxLength() {

            }
        };

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_email),
                true, 100, 5, HEditText.Tipo.EMAIL,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_email)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_phone),
                true, 10, 10, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_phone)));

        btn_next = getView().findViewById(R.id.btn_next);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getContextMenu().getTokenFC(new IFunction<String>() {
                    @Override
                    public void execute(String... token) {

                        Fragment_cuenta_basica_fincomun_2.datosApertura.setCelular(campos.get(1).getText());
                        Fragment_cuenta_basica_fincomun_2.datosApertura.setEmail(campos.get(0).getText());

                        getContextMenu().gethCoDi().validaCliente(campos.get(1).getText(), campos.get(0).getText(), new IFunction() {
                            @Override
                            public void execute(Object[] data) {

                                FCValidaClienteResponse response = (FCValidaClienteResponse)data[0];

                                if(response.getCodigo() != 5 && response.getCodigo() != 0) {
                                    getContext().loading(false);
                                    getContext().alert("Fincomún", response.getDescripcion());
                                    return;
                                }

                                getContextMenu().gethCoDi().nuevaSolicitud(new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {
                                        FCNuevaSolicitudResponse response = (FCNuevaSolicitudResponse)data[0];

                                        datosApertura.setFolioDeSolicitud(response.getFolioSolicitud());
                                        datosCredencial.setFolioDeSolicitud(response.getFolioSolicitud());
                                        getContext().loading(false);
                                        next();

                                    }
                                });
                            }
                        });
                    }
                });

            }
        });

        ImageView img_ine = getView().findViewById(R.id.img_ine);
        ImageView img_ife = getView().findViewById(R.id.img_ife);

        img_ine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_ine.setAlpha(1f);
                img_ife.setAlpha(0.5f);
                documento = 1;
                validate();
                identification();
            }
        });

        img_ife.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_ife.setAlpha(1f);
                img_ine.setAlpha(0.5f);
                documento = 4;
                validate();
                identification();
            }
        });

        campos.get(0).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_mail());
        campos.get(1).setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone());

    }

    public void validate(){

        btn_next.setEnabled(false);

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                return;
            }

        if(documento == 0)
            return;

        if(imagenes.size() < 2 || imagenes.get(0).isEmpty() || imagenes.get(1).isEmpty())
            return;

        btn_next.setEnabled(true);

    }

    public void identification() {

        front = true;
        getContextMenu().showAlertINE(documento, true, new IClickView() {
            @Override
            public void onClick(Object... data) {
                setOCRAction();
            }
        });

    }

    public void next() {

        getContextMenu().closeAlertFC();
        getContext().setFragment(Fragment_cuenta_basica_fincomun_3.newInstance());

    }

    public void setOCRAction() {

        getContext().requestPermissions(new IRequestPermissions() {
            @Override
            public void onPostExecute() {

                HOcrActivity.matches = new ArrayList();

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

                HOcrActivity.setListener(new IOcrListener() {
                    @Override
                    public void execute(List<HOcrMatch> data, Bitmap bitmap) {

                        try {

                            Logger.d(new Gson().toJson(data));
                            String strImg = Utils.convert(bitmap);
                            uri = Utils.getFilePhoto(getContextMenu());

                            savePhoto(uri, bitmap);

                            if(front) {

                                imagenes.add(0, strImg);

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

                                datosCredencial.setCurp(curp);

                                if(nombres.length != 0) {

                                    try {
                                        datosCredencial.setApellidoPaterno(nombres[1]);
                                        datosCredencial.setApellidoMaterno(nombres[2]);
                                        datosCredencial.setNombre(nombres[3]);
                                    } catch (Exception ex) { }

                                }

                                if(domicilios.length != 0) {
                                    try {

                                        datosApertura.setCalle(domicilios[1]);
                                        datosApertura.setColonia(domicilios[2].replace(cp,"").trim());
                                        datosApertura.setMunicipio(domicilios[3].split(",")[0].trim());
                                        datosApertura.setEstado(domicilios[3].split(",")[1].trim());

                                        cp = HOcrActivity.matcher(HOcrActivity.CP_REGEX, domicilios[2]).trim();
                                        datosApertura.setColonia(domicilios[2].replace(cp,"").trim());

                                    } catch (Exception ex) {

                                        try {

                                            datosApertura.setCalle("");
                                            datosApertura.setColonia(domicilios[1].replace(cp,"").trim());
                                            datosApertura.setMunicipio(domicilios[2].split(",")[0].trim());
                                            datosApertura.setEstado(domicilios[2].split(",")[1].trim());

                                            cp = HOcrActivity.matcher(HOcrActivity.CP_REGEX, domicilios[1]).trim();
                                            datosApertura.setColonia(domicilios[1].replace(cp,"").trim());

                                        } catch (Exception e) {

                                        }

                                    }
                                }

                                SessionApp.getInstance().getFcRequestDTO().setGenero(sexo);

                                datosCredencial.setClaveElector(clave);
                                datosCredencial.setAnioEmision(emision);
                                datosCredencial.setAnioRegistro(registro);

                                datosApertura.setCodigoPostal(cp);
                                datosApertura.setNumero("");
                                //datosApertura.setNumInterior("");
                                //datosApertura.setManzana("");
                                //datosApertura.setLote("");
                                //datosApertura.setAntiguedad("");

                                if(curp.length() > 0) {
                                    String date = "19" + curp.substring(4, 12);
                                    date =  date.substring(6, 8) + "/" + date.substring(4, 6) + "/" + date.substring(0, 4);
                                    fechaNacimiento = date;
                                    rfc = curp.substring(0, 10);
                                }

                                front = !front;
                                if(!front) {
                                    getContextMenu().showAlertINE(documento, false, new IClickView() {
                                        @Override
                                        public void onClick(Object... data) {
                                            setOCRAction();
                                        }
                                    });
                                }

                            } else {

                                imagenes.add(1, strImg);

                                String ocr = HOcrActivity.matches.get(0).getValue().replaceAll(">>", "").replaceAll("<<", "").trim();
                                datosCredencial.setOcr(ocr);

                                if(documento == 1) {
                                    String cic = HOcrActivity.matches.get(1).getValue().replaceAll(">>", "").replaceAll("<<", "").trim();
                                    datosCredencial.setCic(cic);
                                }

                                front = true;
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();

                            front = !front;
                            if(!front) {
                                getContextMenu().showAlertINE(documento, false, new IClickView() {
                                    @Override
                                    public void onClick(Object... data) {
                                        setOCRAction();
                                    }
                                });
                            }

                        }

                        validate();

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

            if(front)
                identificacion_frente = path;
            else
                identificacion_reverso = path;

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

                        front = !front;
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

                    //next();

                } catch (Exception e) {

                }

            }
        });
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

 }
