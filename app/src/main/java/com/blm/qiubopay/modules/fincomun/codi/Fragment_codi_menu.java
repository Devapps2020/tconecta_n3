package com.blm.qiubopay.modules.fincomun.codi;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.gson.Gson;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.RegisterActivity;
import com.blm.qiubopay.activities.ScanActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HCoDi;
import com.blm.qiubopay.helpers.HDatabase;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.QPAY_Register;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.bimbo.NotificationDTO;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.SessionApp;

import java.io.IOException;

import me.dm7.barcodescanner.zbar.ZBarScannerView;
import mx.com.fincomun.origilib.Http.Response.Banxico.ValidaCuenta.FCValidacionCuentaResponse;
import mx.com.fincomun.origilib.Http.Response.FCRespuesta;
import mx.com.fincomun.origilib.Http.Response.Retiro.FCConsultaCuentasResponse;
import mx.com.fincomun.origilib.Model.Banxico.Objects.ModelObjetoCobro;
import mx.com.fincomun.origilib.Objects.Retiro.DHCuenta;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IActivityResult;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.IRequestPermissions;

public class Fragment_codi_menu extends HFragment implements IMenuContext {

    private Uri uri;

    private Integer GALLERY_REQUEST = 11;

    public static Fragment_codi_menu newInstance(Object... data) {
     Fragment_codi_menu fragment = new Fragment_codi_menu();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_codi_menu", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_codi_menu, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        CardView card_codi = getView().findViewById(R.id.card_codi);
        CardView card_quiero_pagar = getView().findViewById(R.id.card_quiero_pagar);
        CardView card_quiero_cobrar = getView().findViewById(R.id.card_quiero_cobrar);
        CardView card_notificaciones = getView().findViewById(R.id.card_notificaciones);
        CardView card_ayuda = getView().findViewById(R.id.card_ayuda);
        CardView card_buzon = getView().findViewById(R.id.card_buzon);
        CardView card_creditos = getView().findViewById(R.id.card_creditos);
        CardView card_cuentas = getView().findViewById(R.id.card_cuentas);

        card_codi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                iniciarCoDi(true, new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        QPAY_Register register = new QPAY_Register();
                        register.setFolio("-");
                        register.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
                        register.setRegisterType(Globals.REGISTER_TYPE_FC_CODI);

                        RegisterActivity.createRegister(getContext(), register, new IFunction<QPAY_BaseResponse>() {
                            @Override
                            public void execute(QPAY_BaseResponse... data) {

                            }
                        });

                    }
                });
            }
        });

        card_quiero_pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                iniciarCoDi(false, new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        getContextMenu().requestPermissions(new IRequestPermissions() {
                            @Override
                            public void onPostExecute() {
                                showImageOptionDialog();
                            }
                        }, new String[]{ Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE });
                    }
                });
            }
        });

        card_quiero_cobrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarCoDi(false, new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        getContextMenu().requestPermissions(new IRequestPermissions() {
                            @Override
                            public void onPostExecute() {
                                quieroCobrar();
                            }
                        }, new String[]{ Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE });
                    }
                });
            }
        });

        card_notificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarCoDi(false, new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        if(AppPreferences.getCodiOmision())
                            getContext().setFragment(Fragment_codi_notificaciones_1.newInstance());
                        else
                            getContext().alert("CoDi", "¿Registrar aplicación por omisión?", new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "Si";
                                }

                                @Override
                                public void onClick() {

                                    getContext().loading(true);
                                    getContextMenu().gethCoDi().registroAppPorOmision(new IFunction() {
                                        @Override
                                        public void execute(Object[] data) {
                                            getContext().loading(false);
                                            AppPreferences.setCodiOmision(true);
                                            getContext().setFragment(Fragment_codi_notificaciones_1.newInstance());
                                        }
                                    });

                                }
                            }, new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "No";
                                }

                                @Override
                                public void onClick() {

                                }
                            });
                    }
                });
            }
        });

        card_ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().setFragment(Fragment_codi_ayuda_1.newInstance());
            }
        });

        card_buzon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarCoDi(false, new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        getContext().setFragment(Fragment_codi_buzon_1.newInstance());
                    }
                });
            }
        });

        card_creditos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // getContextMenu().initFC();
            }
        });

        card_cuentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getContextMenu().getTokenFC(new IFunction<String>() {
                    @Override
                    public void execute(String... data) {
                        if (data !=null) {

                            getContextMenu().gethCoDi().consultaCuentas(data[0], new IFunction() {
                                @Override
                                public void execute(Object[] data) {

                                    FCConsultaCuentasResponse response = (FCConsultaCuentasResponse) data[0];
                                    SessionApp.getInstance().setCuentasCoDi(response.getCuentas());
                                    getContext().setFragment(Fragment_codi_cuentas_1.newInstance());

                                }
                            });
                        }
                    }
                });

            }
        });

    }

    public void quieroCobrar() {

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
                                getContext().setFragment(Fragment_codi_quiero_cobrar_1.newInstance());
                            } else
                                getContext().alert("Fincomún", respuesta.getDescripcion().get(0));

                        }
                    });
                }
            });
        } else {
            getContext().setFragment(Fragment_codi_quiero_cobrar_1.newInstance());
        }

    }

    private void showImageOptionDialog(){

        final String[] options = getResources().getStringArray(R.array.image_options);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.dialog_image_options)
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                setResultCamera();
                                getImageFromGallery();
                                break;
                            case 1:
                                capturePictureFromCamera();
                                break;
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getImageFromGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        getContext().startActivityForResult(intent, GALLERY_REQUEST);
    }

    public void setResultCamera(){

        getContextMenu().setOnActivityResult(new IActivityResult() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent intent) {

                if(intent != null) {

                    try {

                        Uri imageUri = intent.getData();

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContextMenu().getContentResolver() , imageUri);
                        decifrarQR(scanQRImage(bitmap).trim());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }

    public void capturePictureFromCamera() {

        ScanActivity.action = new ZBarScannerView.ResultHandler() {
            @Override
            public void handleResult(me.dm7.barcodescanner.zbar.Result result) {

                if(result == null || result.getContents() == null || result.getContents().trim().isEmpty())
                    return;

                decifrarQR(result.getContents().trim());

            }
        };

        getContext().startActivity(ScanActivity.class, false);

    }

    public static String scanQRImage(Bitmap bMap) {

        try {

            String contents = null;

            int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
            //copy pixel data from the Bitmap into the 'intArray' array
            bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

            LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Reader reader = new MultiFormatReader();
            Result result = reader.decode(bitmap);
            contents = result.getText();

            return contents;

        } catch (Exception ex) {
            return "";
        }

    }

    public void decifrarQR(String qr) {

        getContext().loading(true);

        getContextMenu().gethCoDi().decifrarQR(qr, new IFunction() {
            @Override
            public void execute(Object[] data) {
                getContext().loading(false);
                Fragment_codi_quiero_pagar_1.modelCobro = (ModelObjetoCobro) data[0];
                getContext().setFragment(Fragment_codi_quiero_pagar_1.newInstance());
            }
        });
    }

    public void  iniciarCoDi(boolean reset, IFunction function) {
        //VERIFICAR
        getContextMenu().sethCoDi(new HCoDi((MenuActivity) getContext()));

        if(!AppPreferences.getCodiClabe().isEmpty() && !reset) {

            getContextMenu().gethCoDi().cargarKeysource(new IFunction() {
                @Override
                public void execute(Object[] data) {
                    function.execute();
                }
            });

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

                try {
                    HDatabase db = new HDatabase(getContext());
                    db.deleteAll(NotificationDTO.class);
                } catch (Exception ex) {

                }

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
                                                                                AppPreferences.setCuentaCodi(cuentas[0]);
                                                                                HCoDi.cuentaSeleccionada = cuentas[0];
                                                                                getContext().loading(false);

                                                                                getContext().alert("CoDi", "La validación de la cuenta tardará unos minutos. Recibirás una notificación por parte de Banxico", new IAlertButton() {
                                                                                    @Override
                                                                                    public String onText() {
                                                                                        return "Aceptar";
                                                                                    }

                                                                                    @Override
                                                                                    public void onClick() {
                                                                                        function.execute();
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

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}

