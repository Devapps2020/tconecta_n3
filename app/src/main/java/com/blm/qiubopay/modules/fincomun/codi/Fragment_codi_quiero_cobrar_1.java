package com.blm.qiubopay.modules.fincomun.codi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.reginald.editspinner.EditSpinner;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HDatabase;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.models.bimbo.ModelSpinner;
import com.blm.qiubopay.models.bimbo.NotificationDTO;
import com.blm.qiubopay.models.bimbo.QrCodi;
import com.blm.qiubopay.models.bimbo.QrDTO;
import com.blm.qiubopay.utils.SessionApp;

import java.util.ArrayList;
import java.util.Date;

import mx.com.fincomun.origilib.Objects.Retiro.DHCuenta;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_codi_quiero_cobrar_1 extends HFragment implements IMenuContext {

    private ArrayList<HEditText> campos;

    private DHCuenta seleccionada = new DHCuenta();
    private Button btn_generar;

    public static Fragment_codi_quiero_cobrar_1 newInstance(Object... data) {
       Fragment_codi_quiero_cobrar_1 fragment = new Fragment_codi_quiero_cobrar_1();
        Bundle args = new Bundle();

        if (data.length > 0)
            args.putString("Fragment_codi_quiero_cobrar_1", new Gson().toJson(data[0]));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_codi_quiero_cobrar_1, container, false),R.drawable.background_splash_header_1);
    }

    @Override

    public void initFragment() {

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

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

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_cuenta_select),
                true, 500, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_cuenta)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_write_amount),
                true, 30, 1, HEditText.Tipo.MONEDA, iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_write_amount)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_payment_concept),
                true, 30, 1, HEditText.Tipo.TEXTO, iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_payment_concept)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_reference),
                true, 7, 1, HEditText.Tipo.NUMERO, iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_reference)));

        validate();

        CheckBox check_mount = getView().findViewById(R.id.check_mount);
        check_mount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(check_mount.isChecked()) {
                    campos.get(1).setRequired(false);
                    campos.get(1).getEditText().setText("");
                    campos.get(1).getEditText().setEnabled(false);
                }  else {
                    campos.get(1).setRequired(true);
                    campos.get(1).getEditText().setEnabled(true);
                }

                validate();

            }
        });

        btn_generar = getView().findViewById(R.id.btn_generar);

        btn_generar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Double monto = 0.0;

                if(campos.get(1).getEditText().isEnabled()) {
                    monto = Double.parseDouble(campos.get(1).getTextDecimal());
                }

                getContextMenu().gethCoDi().crearQR(monto, seleccionada.getClabeCuenta(), campos.get(3).getText(), campos.get(2).getText(), new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        try {

                            QrCodi json = new Gson().fromJson((String) data[0], QrCodi.class);

                            json.setMonto(campos.get(1).getTextDecimal());
                            json.setConcepto(campos.get(2).getText());
                            json.setReferencia(campos.get(3).getText());
                            json.setCuenta(seleccionada.getClabeCuenta());
                            json.setEstatus("Pendiente de pago");

                            QrDTO qr = new QrDTO();
                            qr.setQrJson(new Gson().toJson(json));
                            qr.setStatus(NotificationDTO.STATUS.ACTIVE.ordinal());
                            qr.setDate(new Date());
                            qr.setFolio(json.getIc().getIDC());

                            HDatabase db = new HDatabase(getContext());
                            db.createOrUpdate(qr);

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        Fragment_codi_quiero_cobrar_2.qr = (String) data[0];
                        getContext().setFragment(Fragment_codi_quiero_cobrar_2.newInstance());
                    }
                });

            }
        });

        Button btn_devoluciones = getView().findViewById(R.id.btn_devoluciones);
        btn_devoluciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().setFragment(Fragment_codi_devoluciones.newInstance());
            }
        });

        setCuentas();

    }

    public void setCuentas() {

        ArrayList<ModelSpinner> infoCuenta = new ArrayList();

        for(DHCuenta cue : SessionApp.getInstance().getCuentasCoDi())
            infoCuenta.add(new ModelSpinner(cue.getNombreCuenta(), "" + cue.getClabeCuenta()));

        EditSpinner spCuenta = getView().findViewById(R.id.edt_cuenta_select);
        spCuenta.setLines(2);
        spCuenta.setSingleLine(false);
        //VALIDAR
        /*context.setDataSpinner(spCuenta, infoCuenta, new IFunction<String>() {
            @Override
            public void execute(String... data) {

                if(data[0] == null || data[0].isEmpty())
                    return;

                for(DHCuenta cue : SessionApp.getInstance().getCuentasCoDi()){
                    if(data[0].equals(cue.getClabeCuenta())) {
                        seleccionada.setNombreCuenta(cue.getNombreCuenta());
                        seleccionada.setNumeroCuenta(cue.getNumeroCuenta());
                        seleccionada.setClabeCuenta(cue.getClabeCuenta());
                        seleccionada.setSaldo(cue.getSaldo());
                        validate();
                        return;
                    }
                }
            }
        });*/

    }

    public void validate() {

        for (int i = 0; i < campos.size(); i++)
            if (!campos.get(i).isValid()) {
                return;
            }

    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}