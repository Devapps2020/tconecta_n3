package com.blm.qiubopay.modules.fincomun.basica;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.RegisterActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.Base64;
import com.blm.qiubopay.helpers.FileUtils;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.proceedings.QPAY_UserFcIdentification;
import com.blm.qiubopay.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import mx.com.fincomun.origilib.Http.Request.Apertura.FCGuardarDocumentoRequest;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCGuardarDocumentoResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCValidacionCURPResponse;
import mx.com.fincomun.origilib.Model.Apertura.CargaDocumento;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IActivityResult;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.IRequestPermissions;

public class Fragment_cuenta_basica_fincomun_3 extends HFragment implements IMenuContext {

    private ArrayList<HEditText> campos;
    private Button btn_next;
    private Uri uri;
    private boolean selfie = false;

    public static Fragment_cuenta_basica_fincomun_3 newInstance(Object... data) {
        Fragment_cuenta_basica_fincomun_3 fragment = new Fragment_cuenta_basica_fincomun_3();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_cuenta_basica_fincomun_3, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        Logger.d(Fragment_cuenta_basica_fincomun_2.identificacion_frente);
        Logger.d(Fragment_cuenta_basica_fincomun_2.identificacion_reverso);
        Logger.d(Fragment_cuenta_basica_fincomun_2.comprobante_domicilio);

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

        Button btn_take_1 = getView().findViewById(R.id.btn_take_1);
        btn_take_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().initHome();
            }
        });

        Button btn_take_2 = getView().findViewById(R.id.btn_take_2);
        btn_take_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContextMenu().initHome();

            }
        });

        CheckBox check_enable_edit = getView().findViewById(R.id.check_enable_edit);

        check_enable_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (HEditText edit: campos)
                    if(edit.isValid())
                        edit.getEditText().setEnabled(check_enable_edit.isChecked());
                    else
                        edit.getEditText().setEnabled(true);

            }
        });

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_name),
                true, 50, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_name)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_surname_f),
                true, 50, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_surname_f)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_surname_m),
                true, 50, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_surname_m)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_birthday),
                true, 10, 10, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_birthday)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_street),
                true, 50, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_street)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_number_iside),
                false, 5, 1, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_number_iside)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_number_exterior),
                true, 5, 1, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_number_exterior)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_suburd),
                true, 50, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_suburd)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_cp),
                true, 5, 1, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_cp)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_state),
                true, 50, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_state)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_municipio),
                true, 50, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_municipio)));

        LinearLayout layout_fecha = getView().findViewById(R.id.layout_fecha);
        layout_fecha.setOnClickListener(view -> {
            getContextMenu().showPicker(R.string.placeholderParameterBirthDate, true, new IClickView() {
                @Override
                public void onClick(Object... data) {
                    campos.get(3).getEditText().setText(data[0].toString());
                }
            });
        });

        btn_next = getView().findViewById(R.id.btn_next3);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getContext().loading(true);

                getContextMenu().gethCoDi().validacionCURP(Fragment_cuenta_basica_fincomun_2.datosCredencial.getFolioDeSolicitud(), Fragment_cuenta_basica_fincomun_2.datosCredencial.getCurp(), new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        FCValidacionCURPResponse response = (FCValidacionCURPResponse)data[0];

                        getContext().loading(false);

                        getContextMenu().getTokenFC(new IFunction<String>() {
                            @Override
                            public void execute(String... token) {

                                Fragment_cuenta_basica_fincomun_2.datosCredencial.setNombre(campos.get(0).getText());
                                Fragment_cuenta_basica_fincomun_2.datosCredencial.setApellidoPaterno(campos.get(1).getText());
                                Fragment_cuenta_basica_fincomun_2.datosCredencial.setApellidoMaterno(campos.get(2).getText());
                                Fragment_cuenta_basica_fincomun_2.fechaNacimiento = campos.get(3).getText();

                                Fragment_cuenta_basica_fincomun_2.datosApertura.setCalle(campos.get(4).getText());
                                Fragment_cuenta_basica_fincomun_2.datosApertura.setNumero(campos.get(6).getText());
                                Fragment_cuenta_basica_fincomun_2.datosApertura.setColonia(campos.get(7).getText());
                                Fragment_cuenta_basica_fincomun_2.datosApertura.setCodigoPostal(campos.get(8).getText());

                                Fragment_cuenta_basica_fincomun_2.datosApertura.setEstado(campos.get(9).getText());
                                Fragment_cuenta_basica_fincomun_2.datosApertura.setMunicipio(campos.get(10).getText());

                               Fragment_cuenta_basica_fincomun_2.datosCredencial.setTokenJwt(token[0]);

                                getContextMenu().gethCoDi().validaCredencial(Fragment_cuenta_basica_fincomun_2.datosCredencial, new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {

                                        FCGuardarDocumentoRequest request = new FCGuardarDocumentoRequest();
                                        request.setFolioDeSolicitud(Fragment_cuenta_basica_fincomun_2.datosCredencial.getFolioDeSolicitud());
                                        request.setNombreDelDocumento("documento.jpg");
                                        request.setNombreDelUsuario(Fragment_cuenta_basica_fincomun_2.datosCredencial.getNombre() + " " + Fragment_cuenta_basica_fincomun_2.datosCredencial.getApellidoPaterno() + " " + Fragment_cuenta_basica_fincomun_2.datosCredencial.getApellidoMaterno());

                                        getContextMenu().gethCoDi().cargaDocumento(Fragment_cuenta_basica_fincomun_2.identificacion_frente, CargaDocumento.TipoCargaDocumento.IDENTIFICACION_ANVERSO, request, new IFunction() {
                                            @Override
                                            public void execute(Object[] data) {

                                                FCGuardarDocumentoResponse response = (FCGuardarDocumentoResponse)data[0];

                                                if(response.getCodigoEdo() != 0) {
                                                    getContext().loading(false);
                                                    getContext().alert("Fincomún", response.getDescripcion());
                                                    return;
                                                }

                                                request.setFolioDeSolicitud(Fragment_cuenta_basica_fincomun_2.datosCredencial.getFolioDeSolicitud());
                                                request.setNombreDelDocumento("documento.jpg");
                                                request.setNombreDelUsuario(Fragment_cuenta_basica_fincomun_2.datosCredencial.getNombre() + " " + Fragment_cuenta_basica_fincomun_2.datosCredencial.getApellidoPaterno() + " " + Fragment_cuenta_basica_fincomun_2.datosCredencial.getApellidoMaterno());

                                                getContextMenu().gethCoDi().cargaDocumento(Fragment_cuenta_basica_fincomun_2.identificacion_reverso, CargaDocumento.TipoCargaDocumento.IDENTIFICACION_REVERSO, request, new IFunction() {
                                                    @Override
                                                    public void execute(Object[] data) {

                                                        FCGuardarDocumentoResponse response = (FCGuardarDocumentoResponse)data[0];

                                                        if(response.getCodigoEdo() != 0) {
                                                            getContext().loading(false);
                                                            getContext().alert("Fincomún", response.getDescripcion());
                                                            return;
                                                        }

                                                        if(Fragment_cuenta_basica_fincomun_2.comprobante_domicilio != null && !Fragment_cuenta_basica_fincomun_2.comprobante_domicilio.isEmpty()) {

                                                            request.setFolioDeSolicitud(Fragment_cuenta_basica_fincomun_2.datosCredencial.getFolioDeSolicitud());
                                                            request.setNombreDelDocumento("documento.jpg");
                                                            request.setNombreDelUsuario(Fragment_cuenta_basica_fincomun_2.datosCredencial.getNombre() + " " + Fragment_cuenta_basica_fincomun_2.datosCredencial.getApellidoPaterno() + " " + Fragment_cuenta_basica_fincomun_2.datosCredencial.getApellidoMaterno());

                                                            getContextMenu().gethCoDi().cargaDocumento(Fragment_cuenta_basica_fincomun_2.identificacion_reverso, CargaDocumento.TipoCargaDocumento.COMPROBANTE, request, new IFunction() {
                                                                @Override
                                                                public void execute(Object[] data) {

                                                                    FCGuardarDocumentoResponse response = (FCGuardarDocumentoResponse)data[0];
                                                                    getContext().loading(false);

                                                                    if(response.getCodigoEdo() != 0) {
                                                                        getContext().alert("Fincomún", response.getDescripcion());
                                                                        return;
                                                                    }

                                                                    takeSelfie();
                                                                }
                                                            });
                                                        } else {
                                                            getContext().loading(false);
                                                            takeSelfie();
                                                        }

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

        TextView text_tipo_1 = getView().findViewById(R.id.text_tipo_1);
        TextView text_tipo_2 = getView().findViewById(R.id.text_tipo_2);

        ImageView img_front = getView().findViewById(R.id.img_front);
        ImageView img_revert = getView().findViewById(R.id.img_revert);

        if(Fragment_cuenta_basica_fincomun_2.documento == 1) {
            text_tipo_1.setText("INE Frente");
            text_tipo_2.setText("INE Reverso");
        } else {
            text_tipo_1.setText("IFE Frente");
            text_tipo_2.setText("IFE Reverso");
        }

        TextView text_domicilio = getView().findViewById(R.id.text_domicilio);
        text_domicilio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        setValuesOCR();

        for (HEditText edit: campos)
            edit.getEditText().setEnabled(!edit.isValid());

        campos.get(5).getEditText().setEnabled(true);

        if (Fragment_cuenta_basica_fincomun_2.imagenes.size() > 1) {
            img_front.setImageBitmap(getImage(Fragment_cuenta_basica_fincomun_2.imagenes.get(0)));
            img_revert.setImageBitmap(getImage(Fragment_cuenta_basica_fincomun_2.imagenes.get(1)));
        }

    }

    public void setValuesOCR() {

        try {

            campos.get(0).setText(Fragment_cuenta_basica_fincomun_2.datosCredencial.getNombre());
            campos.get(1).setText(Fragment_cuenta_basica_fincomun_2.datosCredencial.getApellidoPaterno());
            campos.get(2).setText(Fragment_cuenta_basica_fincomun_2.datosCredencial.getApellidoMaterno());
            campos.get(3).setText(Fragment_cuenta_basica_fincomun_2.fechaNacimiento);

            campos.get(4).setText(Fragment_cuenta_basica_fincomun_2.datosApertura.getCalle());
            campos.get(6).setText(Fragment_cuenta_basica_fincomun_2.datosApertura.getNumero());

            campos.get(7).setText(Fragment_cuenta_basica_fincomun_2.datosApertura.getColonia());
            campos.get(8).setText(Fragment_cuenta_basica_fincomun_2.datosApertura.getCodigoPostal());

            campos.get(9).setText(Fragment_cuenta_basica_fincomun_2.datosApertura.getEstado());
            campos.get(10).setText(Fragment_cuenta_basica_fincomun_2.datosApertura.getMunicipio());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void validate(){

        btn_next.setEnabled(false);

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                return;
            }

        btn_next.setEnabled(true);

    }

    public void takeSelfie() {

        getContext().loading(false);

        getContext().alert("Tomate una foto!\", \"Toma la foto lo más claro posible, en un lugar iluminado, sin accesorios en la cara", new IAlertButton() {
            @Override
            public String onText() {
                return "Aceptar";
            }

            @Override
            public void onClick() {

                selfie = true;
                takePhoto();

            }
        });

    }

    public void takePhoto() {

        getContext().requestPermissions(new IRequestPermissions() {
            @Override
            public void onPostExecute() {

                setResultCamera();

                uri = Utils.getFilePhoto(getContext());

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if(selfie)
                    i.putExtra("android.intent.extras.CAMERA_FACING", 1);

                i.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                getContext().startActivityForResult(i, 1);

            }
        }, new String[]{ Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE });

    }

    public Bitmap getImage(String img) {
        byte[] decodedString = Base64.decode(img);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public void setResultCamera(){

        getContext().setOnActivityResult(new IActivityResult() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent intent) {

                try {

                    final Bitmap image = Utils.decodeSampledBitmapFromFile();
                    savePhoto(uri, image);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void savePhoto(Uri uri, Bitmap image) {

        try {

            String path = FileUtils.getPath(getContext(), uri).split(":")[0];
            Logger.d(Utils.file_absolutePath);
            Logger.d(path);

            File file = new File(path);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            if(selfie) {
                Fragment_cuenta_basica_fincomun_2.selfie = path;
                continuarFlujo(path);
            } else {
                Fragment_cuenta_basica_fincomun_2.comprobante_domicilio = path;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void continuarFlujo(String archivo) {

        getContext().loading(true);

        FCGuardarDocumentoRequest request = new FCGuardarDocumentoRequest();
        request.setFolioDeSolicitud(Fragment_cuenta_basica_fincomun_2.datosCredencial.getFolioDeSolicitud());
        request.setNombreDelDocumento("documento.jpg");
        request.setNombreDelUsuario(Fragment_cuenta_basica_fincomun_2.datosCredencial.getNombre() + " " + Fragment_cuenta_basica_fincomun_2.datosCredencial.getApellidoPaterno() + " " + Fragment_cuenta_basica_fincomun_2.datosCredencial.getApellidoMaterno());

        getContextMenu().gethCoDi().cargaDocumento(archivo, CargaDocumento.TipoCargaDocumento.SELFIE, request, new IFunction() {
            @Override
            public void execute(Object[] data) {

                selfie = false;
                getContext().loading(false);

                FCGuardarDocumentoResponse response = (FCGuardarDocumentoResponse) data[0];

                if (response.getCodigoEdo() != 0) {
                    getContext().loading(false);
                    getContext().alert("Fincomún", response.getDescripcion());
                    return;
                }

                saveRecordData();

            }
        });

    }

    public void saveRecordData() {

        Gson gson = new Gson();

        QPAY_UserFcIdentification identification = new QPAY_UserFcIdentification();
        identification = gson.fromJson(gson.toJson(Fragment_cuenta_basica_fincomun_2.datosCredencial), QPAY_UserFcIdentification.class);

        RegisterActivity.createUserFcIdentification(getContext(), identification, new IFunction<QPAY_BaseResponse>() {
            @Override
            public void execute(QPAY_BaseResponse... data) {
                getContext().setFragment(Fragment_cuenta_basica_fincomun_4.newInstance());
            }
        });

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}
