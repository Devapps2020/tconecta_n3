package com.blm.qiubopay.modules.fincomun.originacion;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HOcrActivity;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.helpers.interfaces.IOcrListener;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.HOcrMatch;
import com.blm.qiubopay.modules.fincomun.adelantoVenta.Fragment_adelanto_venta_simulador;
import com.blm.qiubopay.modules.fincomun.components.FCEditText;
import com.blm.qiubopay.modules.fincomun.prestamo.Fragment_prestamos_solicitud_fincomun_3;
import com.blm.qiubopay.utils.SessionApp;
import com.blm.qiubopay.utils.Utils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import mx.com.fincomun.origilib.Http.Request.Originacion.Credito.FCSinOfertaRequest;
import mx.com.fincomun.origilib.Http.Response.Originacion.Credito.FCSinOfertaResponse;
import mx.com.fincomun.origilib.Model.Originacion.Credito.AltaBitacoraSinOferta;
import mx.com.fincomun.origilib.Objects.Bitacora.DHImagenesBitacora;
import mx.devapps.utils.components.HActivity;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IRequestPermissions;

import static com.blm.qiubopay.utils.Utils.getImage;

public class Fragment_fincomun_oferta_excepcion extends HFragment implements IMenuContext {

    private HActivity context;
    private ArrayList<FCEditText> campos;
    private Object data;
    private Button btn_send;
    private TextView tv_aviso;
    private CheckBox check_consent;
    private CardView cv_photo_1,cv_photo_2,cv_photo_3;
    private ImageView iv_photo_1,iv_photo_2,iv_photo_3;
    private boolean card1,card2,card3;
    private ArrayList<DHImagenesBitacora> imagenes = new ArrayList<>();
    private boolean isShowAviso = false;
    private String b64_img1,b64_img2,b64_img3;


    private Boolean isLoading = false;
    public static Fragment_fincomun_oferta_excepcion newInstance() {
        Fragment_fincomun_oferta_excepcion fragment = new Fragment_fincomun_oferta_excepcion();

        return fragment;
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
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
            data = new Gson().fromJson(getArguments().getString("Fragment_fincomun_oferta_excepcion"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_oferta_excepcion, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {

        CViewMenuTop.create(getView())
                .showTitle("")
                .setColorTitle(R.color.FC_blue_6)
                .setColorBack(R.color.FC_blue_6)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContextMenu().initHome();
                    }
                });

        cv_photo_1 = getView().findViewById(R.id.cv_photo_1);
        cv_photo_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    isLoading = true;
                    showInstructions1();
                }
            }
        });

        cv_photo_2 = getView().findViewById(R.id.cv_photo_2);
        cv_photo_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    isLoading = true;
                    showInstructions2();
                }
            }
        });

        cv_photo_3 = getView().findViewById(R.id.cv_photo_3);
        cv_photo_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    isLoading = true;
                    showInstructions3();
                }
            }
        });

        iv_photo_1 = getView().findViewById(R.id.iv_photo_1);
        iv_photo_2 = getView().findViewById(R.id.iv_photo_2);
        iv_photo_3 = getView().findViewById(R.id.iv_photo_3);

        tv_aviso = getView().findViewById(R.id.tv_aviso);
        tv_aviso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowAviso = true;
                getContext().setFragment(Fragment_prestamos_solicitud_fincomun_3.newInstance());
            }
        });
        check_consent = getView().findViewById(R.id.check_consent);

        btn_send = getView().findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    isLoading = true;
                    validate();
                }
            }
        });
        campos = new ArrayList<>();

        // 0 nombre
        campos.add(FCEditText.create(getView().findViewById(R.id.et_nombre))
                .setRequired(false)
                .setMinimum(5)
                .setMaximum(50)
                .setType(FCEditText.TYPE.TEXT_SN)
                .setHint("Nombre completo")
                .setAlert(R.string.text_input_required)
                .setEnabled(true));
        // 1 celular
        campos.add(FCEditText.create(getView().findViewById(R.id.et_telefono))
                .setRequired(false)
                .setMinimum(10)
                .setMaximum(10)
                .setType(FCEditText.TYPE.NUMBER)
                .setHint("N° de celular")
                .setAlert(R.string.text_input_required)
                .setEnabled(true));

        getContextMenu().saveRegister(CApplication.ACTION.CB_FINCOMUN_ORIGINACION_OFERTA);
    }

    @Override
    public boolean onBackPressed() {
        if (!isShowAviso) {
            getContextMenu().initHome();
        }else{
            isShowAviso = false;
        }
        return false;
    }

    private void showInstructions1(){
        String title= "Captura una foto panorámica de tu comercio";
        String description = "En donde estés atrás del mostrador";
        getContextMenu().showAlertLayoutBitacora(R.layout.item_alert_edo_cuenta, R.drawable.ic_mostrador,title,description, new IClickView() {
            @Override
            public void onClick(java.lang.Object... data) {
                card1 = true;
                HOcrActivity.take_btn = true;
                isLoading = false;
                setOCRAction();

            }
        });
    }

    private void showInstructions2(){
        String title= "Captura una foto panorámica de tu comercio";
        String description = "En donde estés con tu mercancía y se vea el tamaño del negocio";
        getContextMenu().showAlertLayoutBitacora(R.layout.item_alert_edo_cuenta, R.drawable.ic_mercancia,title,description, new IClickView() {
            @Override
            public void onClick(java.lang.Object... data) {
                card2 = true;
                HOcrActivity.take_btn = true;
                isLoading = false;
                setOCRAction();
            }
        });
    }

    private void showInstructions3(){
        String title= "Captura una foto panorámica";
        String description = "En donde se vea tu inventario en bodega o en la tienda";
        getContextMenu().showAlertLayoutBitacora(R.layout.item_alert_edo_cuenta, R.drawable.ic_inventario,title,description, new IClickView() {
            @Override
            public void onClick(java.lang.Object... data) {
                card3 = true;
                HOcrActivity.take_btn = true;
                isLoading = false;
                setOCRAction();
            }
        });
    }

    private void setOCRAction() {

        getContextMenu().requestPermissions(new IRequestPermissions() {
            @Override
            public void onPostExecute() {

                HOcrActivity.matches = new ArrayList();

                HOcrActivity.setListener(new IOcrListener() {
                    @Override
                    public void execute(List<HOcrMatch> data, Bitmap bitmap) {

                        try {

                            String strImg = Utils.convert(bitmap);

                            if(card1) {
                                card1 = false;
                                b64_img1 = strImg;
                                iv_photo_1.setImageBitmap(getImage(strImg));

                                return;
                            }

                            if(card2) {
                                card2 = false;
                                b64_img2 = strImg;
                                iv_photo_2.setImageBitmap(getImage(strImg));

                                return;
                            }

                            if(card3) {
                                card3 = false;
                                b64_img3 = strImg;
                                iv_photo_3.setImageBitmap(getImage(strImg));

                                return;
                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                getContext().startActivity(HOcrActivity.class);
            }
        }, new String[]{ Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE });

    }

    private void validate(){
        if (TextUtils.isEmpty(campos.get(0).getText())){
            campos.get(0).setRequired(true);
            isLoading = false;
            getContextMenu().alert("Captura tu nombre");
            return;
        }else if (TextUtils.isEmpty(campos.get(1).getText())){
            campos.get(1).setRequired(true);
            isLoading = false;
            getContextMenu().alert("Captura tu número de celular");
            return;
        }



        for (FCEditText item: campos){
            if(!item.isValid()){
                isLoading = false;
                getContext().alert("Completa los campos obligatorios");
                return;
            }
        }


        if (!check_consent.isChecked()){
            isLoading = false;
            getContextMenu().alert("Otorga tu consentimiento para el tratamiento de datos personales");
            return;
        }

        getContextMenu().getTokenFC((String... text) -> {
            if (text != null) {
                requestOffer(text[0]);
            }else{
                isLoading = false;
            }
        });
    }

    private void requestOffer(String token) {
        if (b64_img1 != null){
            imagenes.add(new DHImagenesBitacora(b64_img1,1));
        }

        if (b64_img2 != null){
            imagenes.add(new DHImagenesBitacora(b64_img2,2));
        }

        if (b64_img3 != null){
            imagenes.add(new DHImagenesBitacora(b64_img3,3));
        }

        AltaBitacoraSinOferta altaBitacoraSinOferta = new AltaBitacoraSinOferta(getContext());
        FCSinOfertaRequest request = new FCSinOfertaRequest(
                AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id(),
                campos.get(1).getText(),
                campos.get(0).getText(),
                "",
                "",
                "",
                imagenes,
                Settings.Secure.getString(CApplication.getAppContext().getContentResolver(), Settings.Secure.ANDROID_ID),
                token
        );


        Logger.d("REQUEST : "  + new Gson().toJson(request));

        altaBitacoraSinOferta.altaBitacoraSinOferta(request, new AltaBitacoraSinOferta.onRequest() {
            @Override
            public <E> void onSuccess(E e) {
                getContext().loading(false);
                FCSinOfertaResponse response = (FCSinOfertaResponse)e;
                Logger.d("REQUEST : "  + new Gson().toJson(response));

                if (response.codigo == 0){
                    getContextMenu().alert(getContext().getResources().getString(R.string.txt_no_offfer_send), new IAlertButton() {
                        @Override
                        public String onText() {
                            return "CERRAR";
                        }

                        @Override
                        public void onClick() {
                            getContextMenu().backFragment();
                        }
                    });
                }else {
                    getContextMenu().alert(response.getDescripcion(), new IAlertButton() {
                        @Override
                        public String onText() {
                            return "CERRAR";
                        }

                        @Override
                        public void onClick() {
                            getContextMenu().backFragment();
                        }
                    });
                }
                isLoading = false;
            }

            @Override
            public void onFailure(String s) {
                isLoading = false;
                getContext().loading(false);
                getContextMenu().alert(s, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "CERRAR";
                    }

                    @Override
                    public void onClick() {
                        getContextMenu().backFragment();
                    }
                });
            }
        });
    }
}