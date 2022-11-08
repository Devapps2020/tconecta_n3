package com.blm.qiubopay.modules.fincomun.retiro;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.reginald.editspinner.EditSpinner;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.models.bimbo.ModelSpinner;
import com.blm.qiubopay.utils.Utils;

import java.util.ArrayList;

import mx.com.fincomun.origilib.Http.Response.Retiro.FCConsultaCuentasResponse;
import mx.com.fincomun.origilib.Objects.Retiro.DHCuenta;

public class Fragment_retiro_tarjeta_fincomun_1 extends HFragment {

    private View view;
    private MenuActivity context;
    private ArrayList<HEditText> campos;
    private Object data;

    public static FCConsultaCuentasResponse response;
    int monto = 0;
    int monto_custom = 1500;
    double monto_cuenta = 0;

    Button montocien, montodocientos, montotrecientos, montoquinientos, montomil, montomilquinientos;
    Button btn_siguiente;

    public static Fragment_retiro_tarjeta_fincomun_1 newInstance(Object... data) {
        Fragment_retiro_tarjeta_fincomun_1 fragment = new Fragment_retiro_tarjeta_fincomun_1();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = (MenuActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            data = new Gson().fromJson(getArguments().getString("Fragment_prestamos_fincomun_2"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_retiro_tarjeta_fincomun_1, container, false);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
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

        campos.add(new HEditText((EditText) view.findViewById(R.id.edit_select_cuenta),
                true, 100, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_select_cuenta)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edit_monto),
                false, 11, 1, HEditText.Tipo.MONEDA_SC, new ITextChanged() {
            @Override
            public void onChange() {

                setColor();

                if(!campos.get(1).getText().isEmpty())
                    monto = campos.get(1).getTextInt();

                validate();

            }
            @Override
            public void onMaxLength() {

            }
        },(TextView) view.findViewById(R.id.text_error_monto)));

        campos.add(new HEditText((EditText) view.findViewById(R.id.edit_concepto),
                false, 50, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) view.findViewById(R.id.text_error_concepto)));

        EditSpinner spcuenta = view.findViewById(R.id.edit_select_cuenta);
        montocien = view.findViewById(R.id.button_monto_cien);
        montodocientos =  view.findViewById(R.id.button_monto_docientos);
        montotrecientos =  view.findViewById(R.id.button_monto_trecientos);
        montoquinientos =  view.findViewById(R.id.button_monto_quinientos);
        montomil =  view.findViewById(R.id.button_monto_mil);
        montomilquinientos =  view.findViewById(R.id.button_monto_milquinientos);

        montocien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                campos.get(1).setText("");

                setColor();

                if(monto != 100) {
                    monto = 100;
                    montocien.setBackground(context.getResources().getDrawable(R.drawable.background_card_blue_ligth));
                    montocien.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    monto = 0;
                }

                validate();

            }
        });

        montodocientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                campos.get(1).setText("");

                setColor();

                if(monto != 200) {
                    monto = 200;
                    montodocientos.setBackground(context.getResources().getDrawable(R.drawable.background_card_blue_ligth));
                    montodocientos.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    monto = 0;
                }

                validate();

            }
        });

        montotrecientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                campos.get(1).setText("");

                setColor();

                if(monto != 300) {
                    monto = 300;
                    montotrecientos.setBackground(context.getResources().getDrawable(R.drawable.background_card_blue_ligth));
                    montotrecientos.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    monto = 0;
                }

                validate();

            }
        });

        montoquinientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                campos.get(1).setText("");

                setColor();

                if(monto != 500) {
                    monto = 500;
                    montoquinientos.setBackground(context.getResources().getDrawable(R.drawable.background_card_blue_ligth));
                    montoquinientos.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    monto = 0;
                }

                validate();

            }
        });

        montomil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                campos.get(1).setText("");

                setColor();

                if(monto != 1000) {
                    monto = 1000;
                    montomil.setBackground(context.getResources().getDrawable(R.drawable.background_card_blue_ligth));
                    montomil.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    monto = 0;
                }

                validate();

            }
        });

        montomilquinientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                campos.get(1).setText("");

                setColor();

                if(monto != monto_custom) {
                    monto = monto_custom;
                    montomilquinientos.setBackground(context.getResources().getDrawable(R.drawable.background_card_blue_ligth));
                    montomilquinientos.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    monto = 0;
                }

                validate();

            }
        });

        monto_cuenta = Double.parseDouble(response.getCuentas().get(0).getSaldo());

        if(monto_cuenta >= 8000) {
            monto_custom = 8000;
            montomilquinientos.setText("$" + monto_custom);
        } else {

            if(monto_cuenta > 100) {
                String mont = Utils.paserCurrency("" + monto_cuenta).replaceAll(",", "").replace("$", "");
                mont = mont.substring(0, mont.length() - 5);
                monto_custom = Integer.parseInt(mont + "00");

                montomilquinientos.setText("$" + monto_custom);
            }


        }

        spcuenta.setLines(3);
        spcuenta.setSingleLine(false);

        ArrayList<ModelSpinner> cuentas = new ArrayList();

        for(DHCuenta cue : response.getCuentas())
            cuentas.add(new ModelSpinner(cue.getNombreCuenta() + " - " + cue.getNumeroCuenta() , cue.getNumeroCuenta()));

        context.setDataSpinner(spcuenta, cuentas, data -> campos.get(0).setIdentifier(data[0]));

        btn_siguiente = view.findViewById(R.id.btn_siguiente);
        btn_siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!esMultiplo(monto , 100 ) || monto > 8000) {
                    context.alert("El monto ingresado no es valido");
                    return;
                }

                double maximo = Double.parseDouble(response.getCuentas().get(0).getSaldo());

                if(monto > maximo) {
                    context.alert("Saldo insuficiente");
                    return;
                }

                Fragment_retiro_tarjeta_fincomun_2.nombre = campos.get(0).getText();

                if(campos.get(2).getText().isEmpty())
                    Fragment_retiro_tarjeta_fincomun_2.fcRetiroSinTarjetaRequest.setConcepto("Retiro de efectivo");
                else
                    Fragment_retiro_tarjeta_fincomun_2.fcRetiroSinTarjetaRequest.setConcepto(campos.get(2).getText() + "");

                Fragment_retiro_tarjeta_fincomun_2.fcRetiroSinTarjetaRequest.setMonto(monto + "");
                Fragment_retiro_tarjeta_fincomun_2.fcRetiroSinTarjetaRequest.setNumeroCuenta(campos.get(0).getIdentifier());

                Logger.d(new Gson().toJson(Fragment_retiro_tarjeta_fincomun_2.fcRetiroSinTarjetaRequest));

                context.setFragment(Fragment_retiro_tarjeta_fincomun_2.newInstance());

            }
        });

    }

    public static boolean esMultiplo(int n1,int n2){
        if (n1%n2==0)
            return true;
        else
            return false;
    }

    public void setColor() {

        Button [] butons = new Button[] { montocien, montodocientos, montotrecientos, montoquinientos, montomil, montomilquinientos };

        for (Button but : butons) {
            but.setBackground(context.getDrawable(R.drawable.background_button_blue_ligth));
            but.setTextColor(context.getResources().getColor(R.color.colorGray));
        }

    }

    public void validate(){

        btn_siguiente.setEnabled(false);

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                return;
            }

        if(monto == 0)
            return;

        btn_siguiente.setEnabled(true);

    }



}
