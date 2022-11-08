package com.blm.qiubopay.modules.fincomun.basica;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.IMenuContext;

import java.util.ArrayList;

import mx.com.fincomun.origilib.Objects.Apertura.DHBeneficiarioApertura;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;

public class Fragment_cuenta_basica_fincomun_4 extends HFragment implements IMenuContext {


    private ArrayList<HEditText> campos;
    Button btn_next;
    Button btn_add_new;

    ArrayList<DHBeneficiarioApertura> beneficiarios;

    private LinearLayout layout_beneficiario_2, layout_beneficiario_3, layout_beneficiario_4;

    public static Fragment_cuenta_basica_fincomun_4 newInstance(Object... data) {
        Fragment_cuenta_basica_fincomun_4 fragment = new Fragment_cuenta_basica_fincomun_4();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_cuenta_basica_fincomun_4, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().backFragment();
            }
        });

        beneficiarios = new ArrayList<>();

        layout_beneficiario_2 = getView().findViewById(R.id.layout_beneficiario_2);
        layout_beneficiario_3 = getView().findViewById(R.id.layout_beneficiario_3);
        layout_beneficiario_4 = getView().findViewById(R.id.layout_beneficiario_4);

        btn_add_new = getView().findViewById(R.id.btn_add_new);
        btn_next = getView().findViewById(R.id.btn_next5);

        config();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                beneficiarios = getBeneficiarios();

                int porcentaje = 0;

                for (DHBeneficiarioApertura ben : beneficiarios)
                    porcentaje += Integer.parseInt(ben.getPorcentajeBen());

                if(porcentaje != 100) {
                    getContext().alert("El porcentaje especificado debe ser 100%", new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Aceptar";
                        }

                        @Override
                        public void onClick() {

                        }
                    });

                    getContext().loading(false);
                    return;
                }

                Fragment_cuenta_basica_fincomun_2.datosApertura.setBeneficiarios(beneficiarios);
                getContext().setFragment(Fragment_cuenta_basica_fincomun_5.newInstance());
            }
        });

        btn_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(layout_beneficiario_2.getVisibility() == View.GONE){
                    layout_beneficiario_2.setVisibility(View.VISIBLE);
                    config();
                    validate();
                    return;
                }

                if(layout_beneficiario_3.getVisibility() == View.GONE){
                    layout_beneficiario_3.setVisibility(View.VISIBLE);
                    config();
                    validate();
                    return;
                }

                if(layout_beneficiario_4.getVisibility() == View.GONE){
                    layout_beneficiario_4.setVisibility(View.VISIBLE);
                    config();
                    validate();
                    return;
                }

            }
        });

    }


    public void config(){

        ITextChanged iTextChanged = new ITextChanged() {
            @Override
            public void onChange() {
                validate();
            }
            @Override
            public void onMaxLength() {
            }
        };

        campos = new ArrayList<>();

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_name_1),
                true, 50, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_name_1)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_surnames_1),
                true, 50, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_surnames_1)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_birthday),
                true, 15, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_birthday)));


        LinearLayout layout_fecha = getView().findViewById(R.id.layout_fecha);
        layout_fecha.setOnClickListener(view -> {
            getContextMenu().showPicker(R.string.placeholderParameterBirthDate, true, new IClickView() {
                @Override
                public void onClick(Object... data) {
                    campos.get(2).getEditText().setText(data[0].toString());
                }
            });
        });


        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_porcentaje_1),
                true, 3, 1, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_porcentaje_1)));


        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_name_2),
                layout_beneficiario_2.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_name_2)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_surnames_2),
                layout_beneficiario_2.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_birthday_2)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_birthday_2),
                layout_beneficiario_2.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_birthday_2)));

        LinearLayout layout_fecha_2 = getView().findViewById(R.id.layout_fecha_2);
        layout_fecha_2.setOnClickListener(view -> {
            getContextMenu().showPicker(R.string.placeholderParameterBirthDate, true, new IClickView() {
                @Override
                public void onClick(Object... data) {
                    campos.get(6).getEditText().setText(data[0].toString());
                }
            });
        });


        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_porcentaje_2),
                layout_beneficiario_2.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_porcentaje_2)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_name_3),
                layout_beneficiario_3.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_name_3)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_surnames_3),
                layout_beneficiario_3.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_birthday_3)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_birthday_3),
                layout_beneficiario_3.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_birthday_3)));

        LinearLayout layout_fecha_3 = getView().findViewById(R.id.layout_fecha_3);
        layout_fecha_3.setOnClickListener(view -> {
            getContextMenu().showPicker(R.string.placeholderParameterBirthDate, true, new IClickView() {
                @Override
                public void onClick(Object... data) {
                    campos.get(10).getEditText().setText(data[0].toString());
                }
            });
        });

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_porcentaje_3),
                layout_beneficiario_3.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_porcentaje_3)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_name_4),
                layout_beneficiario_4.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_name_4)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_surnames_4),
                layout_beneficiario_4.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.TEXTO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_birthday_4)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_birthday_4),
                layout_beneficiario_4.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.NONE,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_birthday_4)));

        LinearLayout layout_fecha_4 = getView().findViewById(R.id.layout_fecha_4);
        layout_fecha_4.setOnClickListener(view -> {
            getContextMenu().showPicker(R.string.placeholderParameterBirthDate, true, new IClickView() {
                @Override
                public void onClick(Object... data) {
                    campos.get(14).getEditText().setText(data[0].toString());
                }
            });
        });

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_porcentaje_4),
                layout_beneficiario_4.getVisibility() == View.VISIBLE, 25, 1, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_porcentaje_4)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_telefono_1),
                true, 10, 10, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_telefono_1)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_telefono_2),
                layout_beneficiario_2.getVisibility() == View.VISIBLE, 10, 10, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_telefono_2)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_telefono_3),
                layout_beneficiario_3.getVisibility() == View.VISIBLE, 10, 10, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_telefono_3)));

        campos.add(new HEditText((EditText) getView().findViewById(R.id.edt_telefono_4),
                layout_beneficiario_4.getVisibility() == View.VISIBLE, 10, 10, HEditText.Tipo.NUMERO,iTextChanged,
                (TextView) getView().findViewById(R.id.text_error_telefono_4)));

        validate();

    }

    public void validate(){

        btn_add_new.setEnabled(false);
        btn_next.setEnabled(false);

        for(int i=0; i<campos.size(); i++)
            if(!campos.get(i).isValid()){
                return;
            }

        btn_add_new.setEnabled(true);
        btn_next.setEnabled(true);

    }

    public ArrayList<DHBeneficiarioApertura> getBeneficiarios() {

        ArrayList<DHBeneficiarioApertura> beneficiarios = new ArrayList();

        DHBeneficiarioApertura beneficiario = new DHBeneficiarioApertura();

        beneficiario.setNombresBeneficiario(campos.get(0).getText());
        beneficiario.setApellidoPatBen(campos.get(1).getText());
        beneficiario.setApellidoMatBen(campos.get(1).getText());
        beneficiario.setFechaNacimientoBen(campos.get(2).getText().replace("/",""));
        beneficiario.setPorcentajeBen(campos.get(3).getText());
        beneficiario.setTelefonoBen(campos.get(16).getText());

        beneficiarios.add(beneficiario);

        if(layout_beneficiario_2.getVisibility() == View.VISIBLE){

            DHBeneficiarioApertura beneficiario2 = new DHBeneficiarioApertura();

            beneficiario2.setNombresBeneficiario(campos.get(4).getText());
            beneficiario2.setApellidoPatBen(campos.get(5).getText());
            beneficiario2.setApellidoMatBen(campos.get(5).getText());
            beneficiario2.setFechaNacimientoBen(campos.get(6).getText().replace("/",""));
            beneficiario2.setPorcentajeBen(campos.get(7).getText());
            beneficiario2.setTelefonoBen(campos.get(17).getText());

            beneficiarios.add(beneficiario2);

        }

        if(layout_beneficiario_3.getVisibility() == View.VISIBLE){

            DHBeneficiarioApertura beneficiario3 = new DHBeneficiarioApertura();

            beneficiario3.setNombresBeneficiario(campos.get(8).getText());
            beneficiario3.setApellidoPatBen(campos.get(9).getText());
            beneficiario3.setApellidoMatBen(campos.get(9).getText());
            beneficiario3.setFechaNacimientoBen(campos.get(10).getText().replace("/",""));
            beneficiario3.setPorcentajeBen(campos.get(11).getText());
            beneficiario3.setTelefonoBen(campos.get(18).getText());

            beneficiarios.add(beneficiario3);

        }

        if(layout_beneficiario_4.getVisibility() == View.VISIBLE){

            DHBeneficiarioApertura beneficiario4 = new DHBeneficiarioApertura();

            beneficiario4.setNombresBeneficiario(campos.get(12).getText());
            beneficiario4.setApellidoPatBen(campos.get(13).getText());
            beneficiario4.setApellidoMatBen(campos.get(13).getText());
            beneficiario4.setFechaNacimientoBen(campos.get(14).getText().replace("/",""));
            beneficiario4.setPorcentajeBen(campos.get(15).getText());
            beneficiario4.setTelefonoBen(campos.get(19).getText());

            beneficiarios.add(beneficiario4);

        }

        return beneficiarios;
    }


    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}
