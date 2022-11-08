package com.blm.qiubopay.modules.fincomun.operacionesqr;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HDatabase;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.bimbo.NotificationDTO;
import com.blm.qiubopay.models.bimbo.QrCodi;
import com.blm.qiubopay.models.bimbo.QrDTO;
import com.blm.qiubopay.modules.fincomun.components.FCEditText;
import com.blm.qiubopay.utils.SessionApp;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

import mx.com.fincomun.origilib.Objects.Retiro.DHCuenta;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_fincomun_cobro_solicitud extends HFragment implements IMenuContext {


    public Fragment_fincomun_cobro_solicitud() {
    }

    public static Fragment_fincomun_cobro_solicitud newInstance(){
        return new Fragment_fincomun_cobro_solicitud();
    }

    private AlertDialog alertFC = null;
    private ArrayList<FCEditText> campos = new ArrayList<>();
    private Button btn_generar;
    private DHCuenta cuentaPrincipal = new DHCuenta();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_fincomun_codi_cobro, container, false),R.drawable.background_splash_header_3);
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }


    @Override
    public void initFragment() {
        CViewMenuTop.create(getView())
                .setColorBack(R.color.FC_white)
                .onClickBack(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {
                        getContextMenu().backFragment();
                    }
                })
                .onClickQuestion(new CViewMenuTop.IClick() {
                    @Override
                    public void onClick() {

                    }
                });

        FCEditText.ITextChanged validate = new FCEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate();
            }
        };


        //  0 => Monto
        campos.add(FCEditText.create(getView().findViewById(R.id.et_monto))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(30)
                .setType(FCEditText.TYPE.CURRENCY)
                .setHint("Monto")
                .setAlert(R.string.text_input_required)
                .setIcon(R.drawable.icons_cr_dito)
                .setTextChanged(validate));

        //  1 => Referencia
        campos.add(FCEditText.create(getView().findViewById(R.id.et_referencia))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(7)
                .setType(FCEditText.TYPE.NUMBER)
                .setHint("Referencia")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

        //  2 => Concepto
        campos.add(FCEditText.create(getView().findViewById(R.id.et_concepto))
                .setRequired(false)
                .setMinimum(1)
                .setMaximum(200)
                .setType(FCEditText.TYPE.TEXT)
                .setHint("Concepto")
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));


        btn_generar = getView().findViewById(R.id.btn_generar);

        btn_generar.setEnabled(false);

        btn_generar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                campos.get(0).setRequired(true);
                campos.get(1).setRequired(true);
                campos.get(2).setRequired(true);

                for (FCEditText campo: campos){
                    if (!campo.isValid()){
                        getContextMenu().alert("Completa los campos obligatorios");
                        return;
                    }
                }

                Double monto = Double.parseDouble(campos.get(0).getTextDecimal());


                getContextMenu().gethCoDi().crearQR(monto, cuentaPrincipal.getClabeCuenta(), campos.get(1).getText(), campos.get(2).getText(), new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        try {
                            final String qrEncoded = (String) data[0];
                            QrCodi json = new Gson().fromJson((String) data[0], QrCodi.class);

                            json.setMonto(campos.get(0).getTextDecimal());
                            json.setReferencia(campos.get(1).getText());
                            json.setConcepto(campos.get(2).getText());
                            json.setCuenta(cuentaPrincipal.getClabeCuenta());
                            json.setEstatus("Pendiente de pago");

                            QrDTO qr = new QrDTO();
                            qr.setQrJson(new Gson().toJson(json));
                            qr.setStatus(NotificationDTO.STATUS.ACTIVE.ordinal());
                            qr.setDate(new Date());
                            qr.setFolio(json.getIc().getIDC());

                            HDatabase db = new HDatabase(getContext());
                            db.createOrUpdate(qr);

                            //TODO : Pasar al siguiente fragment
                            getContext().setFragment(Fragment_fincomun_cobro_detalle.newInstance(new Gson().toJson(json), qrEncoded));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                });

            }
        });


        if (SessionApp.getInstance().getCuentasCoDi() != null && !SessionApp.getInstance().getCuentasCoDi().isEmpty()){
            cuentaPrincipal = SessionApp.getInstance().getCuentasCoDi().get(0);
        }else{
            // TODO : POP NO TIENE CUENTAS
            showPopup(1, new IClickView() {
                @Override
                public void onClick(Object... data) {

                }
            });
        }

    }




    private void validate() {
        for (FCEditText item: campos){
            if(!item.isValid()){
                btn_generar.setEnabled(false);
                return;
            }
        }
        btn_generar.setEnabled(true);
    }

    private void showPopup(int estatus, final IClickView... listener) {

        getContext().closeLoading();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_alert_no_offer, null);

        ImageView imv_close = view.findViewById(R.id.imv_close);
        imv_close.setVisibility(View.VISIBLE);
        imv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertFC.dismiss();
            }
        });

        Button btn_aceptar = view.findViewById(R.id.btn_offer);
        btn_aceptar.setText("Aceptar");
        Button btn_contactar = view.findViewById(R.id.btn_acept);
        btn_contactar.setText("Contactar");

        ImageView imv_icon = view.findViewById(R.id.imv_icon);
        TextView text_title = view.findViewById(R.id.text_title);
        TextView text_mensaje = view.findViewById(R.id.text_mensaje);

        switch (estatus){
            case 0 :  //Exito
                btn_contactar.setVisibility(View.GONE);
                imv_icon.setBackground(getContext().getDrawable(R.drawable.icons_check));
                text_title.setText(getContext().getResources().getString(R.string.FC_txt_exito_header));
                text_mensaje.setText(getContext().getResources().getString(R.string.FC_txt_exito_desc));
                break;

            case 1:  //Error
                btn_contactar.setVisibility(View.VISIBLE);
                imv_icon.setBackground(getContext().getDrawable(R.drawable.warning));
                text_title.setText(getContext().getResources().getString(R.string.FC_txt_error_header));
                text_mensaje.setText(getContext().getResources().getString(R.string.FC_txt_error_desc));
                break;
        }



        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertFC.dismiss();

                if (listener.length > 0)
                    listener[0].onClick();

            }
        });

        btn_contactar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertFC.dismiss();

                if (listener.length > 1)
                    listener[1].onClick();

            }
        });



        builder.setView(view);
        builder.setCancelable(false);

        alertFC = builder.create();
        alertFC.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertFC.show();

        builder = null;

    }
}
