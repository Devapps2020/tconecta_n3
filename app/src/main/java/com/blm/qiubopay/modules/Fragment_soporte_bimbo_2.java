package com.blm.qiubopay.modules;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IFCReport;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.bimbo.ModelItem;
import com.blm.qiubopay.models.bimbo.SoporteDTO;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;
import java.util.List;

import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;

public class Fragment_soporte_bimbo_2 extends HFragment implements IMenuContext {

    private ArrayList<CViewEditText> campos;
    private Button btn_soporte_enviar;
    SoporteDTO request;

    public static Fragment_soporte_bimbo_2 newInstance(Object... data) {
        Fragment_soporte_bimbo_2 fragment = new Fragment_soporte_bimbo_2();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_soporte_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_soporte_bimbo_2, container, false),R.drawable.background_splash_header_3);
    }

    @Override
    public void initFragment() {


        request = new SoporteDTO();

        campos = new ArrayList();

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        CViewEditText.ITextChanged validate = new CViewEditText.ITextChanged() {
            @Override
            public void onChange(String text) {
                validate();
            }
        };

        btn_soporte_enviar = getView().findViewById(R.id.btn_soporte_enviar);

        btn_soporte_enviar.setEnabled(false);

        btn_soporte_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarReporte();
            }
        });

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_numero_cliente_bimbo))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(14)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_soporte_4)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate)
                .setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id()));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_nombre_del_negocio))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(100)
                .setType(CViewEditText.TYPE.TEXT)
                .setHint(R.string.text_soporte_5)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate)
                .setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_name()));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_codigo_postal))
                .setRequired(true)
                .setMinimum(5)
                .setMaximum(5)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_soporte_6)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate)
                .setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code()));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_numero_celular))
                .setRequired(true)
                .setMinimum(10)
                .setMaximum(10)
                .setType(CViewEditText.TYPE.NUMBER)
                .setHint(R.string.text_soporte_7)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate)
                .setText(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_cellphone()));

        List<ModelItem> categorias = new ArrayList();
        categorias.add(new ModelItem("Sobre entrega de productos Bimbo", "1"));
        categorias.add(new ModelItem("Sobre servicio de pago en punto de venta", "2"));
        categorias.add(new ModelItem("Sobre créditos, sobregiros, asistencias o seguros", "3"));
        categorias.add(new ModelItem("Sobre problemas con la aplicación u otros", "4"));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_motivo_de_contacto))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(100)
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.text_soporte_8)
                .setSpinner(categorias)
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {
                        setTipologia(Integer.parseInt(campos.get(4).getTag()));
                        validate();
                    }
                }));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_tipologia_contacto))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(100)
                .setType(CViewEditText.TYPE.NONE)
                .setHint(R.string.text_soporte_9)
                .setAlert(R.string.text_input_required)
                .setEnabled(false)
                .setTextChanged(validate));

        campos.add(CViewEditText.create(getView().findViewById(R.id.edit_mensaje))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(100)
                .setType(CViewEditText.TYPE.MULTI_TEXT)
                .setHint(R.string.text_soporte_10)
                .setAlert(R.string.text_input_required)
                .setTextChanged(validate));

    }

    private void setTipologia(int option) {

        ArrayList<ModelItem> tipologia = new ArrayList();

        switch (option) {
            case 1:
                tipologia.add(new ModelItem("Visitas de vendedor o\nfalta de producto"));
                tipologia.add(new ModelItem("Evalúa a tu experiencia"));
                tipologia.add(new ModelItem("Promociones"));
                tipologia.add(new ModelItem("Crédito Pesito"));
                break;
            case 2:
                tipologia.add(new ModelItem("Recargas"));
                tipologia.add(new ModelItem("Pago de servicios"));
                tipologia.add(new ModelItem("Depósitos bancarios"));
                tipologia.add(new ModelItem("Recolecciones de efectivo"));
                tipologia.add(new ModelItem("Pagos con tarjeta"));
                break;
            case 3:
                tipologia.add(new ModelItem("Seguimiento o pago de crédito"));
                tipologia.add(new ModelItem("Activación, desembolso o incremento\nde línea de sobregiro"));
                tipologia.add(new ModelItem("Asistencia médica o funeraria"));
                break;
            case 4:
                tipologia.add(new ModelItem("Problemas o consultas con\nla aplicación"));
                tipologia.add(new ModelItem("Otros"));
                break;
        }

        campos.get(5).setText("");
        campos.get(5).setSpinner(tipologia);

    }

    private void validate() {

        btn_soporte_enviar.setEnabled(false);

        for(int i=0; i<campos.size(); i++) {
            if (!campos.get(i).isValid()) {
                return;
            }
        }

        btn_soporte_enviar.setEnabled(true);
    }

    public void enviarReporte(){

        getContext().loading(true);

        try {
            request.setCase_issue_type(campos.get(6).getText());
            request.setCase_issue_category(campos.get(4).getText());
            request.setCase_issue_detail(campos.get(5).getText());
            request.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            request.setCommerce_cp(campos.get(1).getText());
            request.setCommerce_name(campos.get(2).getText());

            request.setUser_type(("0".equals(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_activation_3()) &&
                    !"1".equals(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_activation_7()) ? 0 : 1) + "");

            if(Fragment_soporte_bimbo_1.soporte) {
                request.setSource("Soporte Técnico");
                CApplication.setAnalytics(CApplication.ACTION.CB_ASISTENCIA_TECNICA_envian);
            } else {
                request.setSource("Queremos Escucharte");
                CApplication.setAnalytics(CApplication.ACTION.CB_QUEREMOS_ESCUCHARTE_envian);
            }


            IFCReport petition = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContext().alert(R.string.general_error);

                    } else {

                        String json = new Gson().toJson(result);
                        SoporteDTO response = new Gson().fromJson(json, SoporteDTO.class);

                        if (response.getQpay_response().equals("true")) {

                            for(int i=0; i<campos.size(); i++) {
                                //campos.get(i).getEditText().setText("");
                            }

                            getContext().alert("Datos enviados con èxito", new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "Aceptar";
                                }

                                @Override
                                public void onClick() {
                                    getContextMenu().initHome();
                                }
                            });

                        } else {

                            getContextMenu().validaSesion(response.getQpay_code(), "No existen transacciones registradas.");

                        }

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    getContext().loading(false);

                    getContext().alert(R.string.general_error);
                }
            }, getContext());

            petition.sendReport(request);

        } catch (Exception e) {
            e.printStackTrace();

            getContext().loading(false);

            getContext().alert(R.string.general_error_catch);
        }

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}

