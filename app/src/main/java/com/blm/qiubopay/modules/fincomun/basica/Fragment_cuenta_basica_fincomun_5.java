package com.blm.qiubopay.modules.fincomun.basica;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.RegisterActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.FileUtils;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.QPAY_Register;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.modules.Fragment_registro_financiero_5;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import mx.com.fincomun.origilib.Http.Request.Apertura.FCAperturaCuentaRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCEnvioTokenRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCGuardarDocumentoRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCPLDRequest;
import mx.com.fincomun.origilib.Http.Request.Apertura.FCValidaTokenRequest;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCAperturaCuentaResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCGuardarDocumentoResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCPLDResponse;
import mx.com.fincomun.origilib.Http.Response.Apertura.FCValidaTokenResponse;
import mx.com.fincomun.origilib.Model.Apertura.CargaDocumento;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_cuenta_basica_fincomun_5 extends HFragment implements IMenuContext {



    public static FCAperturaCuentaResponse response;

    RadioButton rad_pregunta_si_1, rad_pregunta_no_1, rad_pregunta_si_2, rad_pregunta_no_2;
    CheckBox check_documentos, check_doc_1, check_doc_2, check_doc_3, check_doc_4, check_doc_5, check_consentimiento, check_autorizo;
    ImageView img_firm;
    Button btn_next;
    Uri uri;

    public static Fragment_cuenta_basica_fincomun_5 newInstance(Object... data) {
        Fragment_cuenta_basica_fincomun_5 fragment = new Fragment_cuenta_basica_fincomun_5();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_cuenta_basica_fincomun_5, container, false),R.drawable.background_splash_header_1);
    }

    @Override

    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        rad_pregunta_si_1 = getView().findViewById(R.id.rad_pregunta_si_1);
        rad_pregunta_no_1 = getView().findViewById(R.id.rad_pregunta_no_1);
        rad_pregunta_si_2 = getView().findViewById(R.id.rad_pregunta_si_2);
        rad_pregunta_no_2 = getView().findViewById(R.id.rad_pregunta_no_2);
        check_documentos = getView().findViewById(R.id.check_documentos);
        check_doc_1 = getView().findViewById(R.id.check_doc_1);
        check_doc_2 = getView().findViewById(R.id.check_doc_2);
        check_doc_3 = getView().findViewById(R.id.check_doc_3);
        check_doc_4 = getView().findViewById(R.id.check_doc_4);
        check_doc_5 = getView().findViewById(R.id.check_doc_5);
        check_consentimiento = getView().findViewById(R.id.check_consentimiento);
        check_autorizo = getView().findViewById(R.id.check_autorizo);
        img_firm = getView().findViewById(R.id.img_firm);
        btn_next = getView().findViewById(R.id.btn_next);

        check_doc_1.setEnabled(false);
        check_doc_2.setEnabled(false);
        check_doc_3.setEnabled(false);
        check_doc_4.setEnabled(false);
        check_doc_5.setEnabled(false);

        rad_pregunta_si_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        rad_pregunta_no_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        rad_pregunta_si_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        rad_pregunta_no_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        check_documentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                check_doc_1.setChecked(check_documentos.isChecked());
                check_doc_2.setChecked(check_documentos.isChecked());
                check_doc_3.setChecked(check_documentos.isChecked());
                check_doc_4.setChecked(check_documentos.isChecked());
                check_doc_5.setChecked(check_documentos.isChecked());

                validate();
            }
        });

        check_consentimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        check_autorizo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        img_firm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 uri = Utils.getFilePhoto(getContext());
                 getContext().setFragment(Fragment_registro_financiero_5.newInstance());
            }
        });

       getContextMenu().setImage = data -> {
            img_firm.setImageBitmap(data[0]);
            savePhoto(uri, data[0]);
            getContextMenu().backFragment();
            validate();
        };

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getContextMenu().getTokenFC(new IFunction<String>() {
                    @Override
                    public void execute(String... token) {

                        FCPLDRequest request = new FCPLDRequest();
                        request.setApellidoMaterno(Fragment_cuenta_basica_fincomun_2.datosCredencial.getApellidoMaterno());
                        request.setApellidoPaterno(Fragment_cuenta_basica_fincomun_2.datosCredencial.getApellidoPaterno());
                        request.setFechaNacimiento(Fragment_cuenta_basica_fincomun_2.fechaNacimiento);
                        request.setNombre(Fragment_cuenta_basica_fincomun_2.datosCredencial.getNombre());
                        request.setPregunta1("Pregutna 1");
                        request.setPregunta2("Pregutna 2");
                        request.setRespuesta1(rad_pregunta_si_1.isChecked() ? "Sí" : "No");
                        request.setRespuesta2(rad_pregunta_si_2.isChecked() ? "Sí" : "No");
                        request.setRfc(Fragment_cuenta_basica_fincomun_2.rfc);
                        request.setTokenJwt(token[0]);

                        getContextMenu().gethCoDi().pld(request, new IFunction() {
                            @Override
                            public void execute(Object[] data) {
                                FCPLDResponse response = (FCPLDResponse)data[0];

                                if(response.getSP_CODIGO() == 0 || response.getSP_CODIGO() == 0) {
                                    getContext().loading(false);
                                    getContext().alert("Fincomún", response.getSP_MENSAJE());
                                    return;
                                }

                                FCGuardarDocumentoRequest request = new FCGuardarDocumentoRequest();
                                request.setFolioDeSolicitud(Fragment_cuenta_basica_fincomun_2.datosCredencial.getFolioDeSolicitud());
                                request.setNombreDelDocumento("documento.jpg");
                                request.setNombreDelUsuario(Fragment_cuenta_basica_fincomun_2.datosCredencial.getNombre() + " " + Fragment_cuenta_basica_fincomun_2.datosCredencial.getApellidoPaterno() + " " + Fragment_cuenta_basica_fincomun_2.datosCredencial.getApellidoMaterno());

                                getContextMenu().gethCoDi().cargaDocumento(Fragment_cuenta_basica_fincomun_2.foto_firma, CargaDocumento.TipoCargaDocumento.FIRMA, request, new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {

                                        FCGuardarDocumentoResponse response = (FCGuardarDocumentoResponse) data[0];

                                        if (response.getCodigoEdo() != 0) {
                                            getContext().loading(false);
                                            getContext().alert("Fincomún", response.getDescripcion());
                                            return;
                                        }

                                        finalizarProceso(token[0]);

                                    }
                                });


                            }
                        });

                    }
                });

            }
        });

    }

    public void validate() {

        btn_next.setEnabled(false);

        if(!rad_pregunta_si_1.isChecked() && !rad_pregunta_no_1.isChecked())
            return;

        if(!rad_pregunta_si_2.isChecked() && !rad_pregunta_no_2.isChecked())
            return;

        if(!check_documentos.isChecked() || !check_consentimiento.isChecked() ||  !check_autorizo.isChecked() || (Fragment_cuenta_basica_fincomun_2.foto_firma == null || Fragment_cuenta_basica_fincomun_2.foto_firma .isEmpty()))
            return;

        btn_next.setEnabled(true);

    }

    public void finalizarProceso(String token) {

        FCEnvioTokenRequest request = new FCEnvioTokenRequest();
        request.setCelular(Fragment_cuenta_basica_fincomun_2.datosApertura.getCelular());
        request.setEmail(Fragment_cuenta_basica_fincomun_2.datosApertura.getEmail());
        request.setNumCliente(Fragment_cuenta_basica_fincomun_2.datosApertura.getCelular());
        request.setTipoSol(1);

        getContextMenu().gethCoDi().envioToken(request, new IFunction() {
            @Override
            public void execute(Object[] data) {

                getContext().loading(false);

                getContextMenu().gethCoDi().confirmToken(null, new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        FCValidaTokenRequest valida = new FCValidaTokenRequest();
                        valida.setTokenJwt(token);
                        valida.setToken((String) data[0]);
                        valida.setNumCliente(Fragment_cuenta_basica_fincomun_2.datosApertura.getCelular());
                        valida.setTipoSol(1);

                        getContext().loading(true);

                        getContextMenu().gethCoDi().validaToken(valida, new IFunction() {
                            @Override
                            public void execute(Object[] data) {

                                FCValidaTokenResponse responseValida = (FCValidaTokenResponse)data[0];

                                if(responseValida.getCodigo() != 0) {
                                    getContext().alert("Fincomún", responseValida.getDescripcion());
                                    return;
                                }

                                request.setTipoSol(2);

                                getContextMenu().gethCoDi().envioToken(request, new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {

                                        getContextMenu().gethCoDi().confirmToken("Ingresa el código que te enviamos a tu correo",new IFunction() {
                                            @Override
                                            public void execute(Object[] data) {

                                                valida.setToken((String) data[0]);
                                                valida.setTipoSol(2);

                                                getContext().loading(true);

                                                getContextMenu().gethCoDi().validaToken(valida, new IFunction() {
                                                    @Override
                                                    public void execute(Object[] data) {

                                                        FCValidaTokenResponse responseValida = (FCValidaTokenResponse)data[0];

                                                        if(responseValida.getCodigo() != 0) {
                                                            getContext().loading(false);
                                                            getContext().alert("Fincomún", responseValida.getDescripcion());
                                                            return;
                                                        }

                                                        FCAperturaCuentaRequest request = Fragment_cuenta_basica_fincomun_2.datosApertura;
                                                        request.setApMaterno(Fragment_cuenta_basica_fincomun_2.datosCredencial.getApellidoMaterno());
                                                        request.setApPaterno(Fragment_cuenta_basica_fincomun_2.datosCredencial.getApellidoPaterno());
                                                        request.setClaveElector(Fragment_cuenta_basica_fincomun_2.datosCredencial.getClaveElector());
                                                        request.setCurp(Fragment_cuenta_basica_fincomun_2.datosCredencial.getCurp());
                                                        request.setNombres(Fragment_cuenta_basica_fincomun_2.datosCredencial.getNombre());
                                                        request.setLatitud(11111.0);
                                                        request.setLongitud(55555.0);

                                                        Fragment_cuenta_basica_fincomun_2.datosApertura = request;

                                                        getContextMenu().gethCoDi().aperturaCuenta(Fragment_cuenta_basica_fincomun_2.datosApertura , new IFunction() {
                                                            @Override
                                                            public void execute(Object[] data) {

                                                                FCAperturaCuentaResponse result = (FCAperturaCuentaResponse)data[0];

                                                                response = result;

                                                                if(response.getCodigo() != 0) {
                                                                    getContext().loading(false);
                                                                    getContext().alert("Fincomún", response.getDescripcion());
                                                                    return;
                                                                }

                                                                Utils.deleteFilePhoto(Fragment_cuenta_basica_fincomun_2.identificacion_frente);
                                                                Utils.deleteFilePhoto(Fragment_cuenta_basica_fincomun_2.identificacion_reverso);
                                                                Utils.deleteFilePhoto(Fragment_cuenta_basica_fincomun_2.comprobante_domicilio);
                                                                Utils.deleteFilePhoto(Fragment_cuenta_basica_fincomun_2.foto_firma);
                                                                Utils.deleteFilePhoto(Fragment_cuenta_basica_fincomun_2.selfie);

                                                                getContext().loading(false);

                                                                QPAY_Register register = new QPAY_Register();
                                                                register.setFolio("-");
                                                                register.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
                                                                register.setRegisterType(Globals.REGISTER_TYPE_FC_CUENTA);

                                                                RegisterActivity.createRegister(getContext(), register, new IFunction<QPAY_BaseResponse>() {
                                                                    @Override
                                                                    public void execute(QPAY_BaseResponse... data) {
                                                                        getContext().setFragment(Fragment_cuenta_basica_fincomun_6.newInstance());
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
        return (MenuActivity) getContext();
    }
}
