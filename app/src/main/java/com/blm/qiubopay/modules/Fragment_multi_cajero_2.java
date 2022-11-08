package com.blm.qiubopay.modules;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blm.qiubopay.components.CViewMenuTop;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.LoginActivity;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.helpers.interfaces.IClickView;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.IMultiUserListener;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.LinkedUser;
import com.blm.qiubopay.models.QPAY_LinkedUser;
import com.blm.qiubopay.models.QPAY_Privileges;
import com.blm.qiubopay.models.QPAY_PrivilegesResponse;
import com.blm.qiubopay.models.QPAY_UserProfile;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;

import java.util.Arrays;
import java.util.List;

import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_multi_cajero_2 extends HFragment implements IMenuContext {

    private static final String TAG = "cajero_2";

    private View view;
    private MenuActivity context;

    private LinkedUser data;
    private Boolean dataLastCashier;

    private TextView textDias;
    private TextView textHorario;
    private HEditText editAlias;
    private HEditText editMonto;
    private RadioButton radCashCollect;
    private Button btnGuardar;

    private String horaEntrada;
    private String horaSalida;
    boolean[] checkedDays = new boolean[] { false,false,false,false,false,false,false};
    boolean acceptCashCollection = false;

    boolean daysIsValid = false;
    boolean scheduleIsValid = false;

    boolean acceptFiar = false;
    private RadioButton radFiar;


    public Fragment_multi_cajero_2() {
        // Required empty public constructor
    }

    public static Fragment_multi_cajero_2 newInstance(Object... data) {
        Fragment_multi_cajero_2 fragment = new Fragment_multi_cajero_2();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_multi_cajero_2_1", new Gson().toJson(data[0]));

        if(data.length > 1)
            args.putBoolean("Fragment_multi_cajero_2_2", (Boolean) data[1]);

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

        if (getArguments() != null) {
            data = new Gson().fromJson(getArguments().getString("Fragment_multi_cajero_2_1"), LinkedUser.class);
            dataLastCashier = getArguments().getBoolean("Fragment_multi_cajero_2_2", false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_multi_cajero_2, container, false);

        setView(view);

        initFragment();

        return view;
    }


    public void initFragment(){

        ITextChanged iTextChanged = new ITextChanged() {
            @Override
            public void onChange() { validate(); }

            @Override
            public void onMaxLength() {   }
        };

        // DECLARACION DE COMPONENTES DE PANTALLA

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        LinearLayout layout_horas = view.findViewById(R.id.layout_horas);

        LinearLayout layout_dias = view.findViewById(R.id.layout_dias);

        textHorario = view.findViewById(R.id.txt_horario);

        textDias = view.findViewById(R.id.txt_dias);

        editAlias = new HEditText((EditText) view.findViewById(R.id.edit_alias), false, 20, 1, HEditText.Tipo.TEXTO, iTextChanged);

        //RSB se inicializa posterior a obtener los privilegios de QTC
        editMonto = new HEditText((EditText) view.findViewById(R.id.edit_importe),false, 10, 1, HEditText.Tipo.MONEDA, iTextChanged);

        radCashCollect = view.findViewById(R.id.rad_cash_collect);
        radFiar = view.findViewById(R.id.rad_fiar);

        btnGuardar = view.findViewById(R.id.btn_guardar);

        Button btnEliminar = view.findViewById(R.id.btn_eliminar);

        LinearLayout layout_cash = view.findViewById(R.id.layout_cash_collect);

        if(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id() == null ||
                AppPreferences.getUserProfile().getQpay_object()[0].getQpay_bimbo_id().isEmpty()) {
            layout_cash.setVisibility(View.GONE);
            acceptCashCollection = false;
        }

        //20200728 RSB. Improvements0820. Colocar nombre de cajero
        TextView textNombreCajero = view.findViewById(R.id.text_nombre_cajero);
        String cashierName = data.getQpay_name() + (data.getQpay_father_surname()!=null &&
                !data.getQpay_father_surname().isEmpty() ? " " + data.getQpay_father_surname() : "");
        textNombreCajero.setText(Html.fromHtml("Cajero<br><b>" + cashierName + "</b>"));

        // LISTENERS DE COMPONENTES

        layout_horas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               getContextMenu().showTimer(R.string.cajero_hora_entrada, horaEntrada, new IClickView() {
                    @Override
                    public void onClick(Object... data) {
                        horaEntrada = data[0].toString();

                        context.showTimer(R.string.cajero_hora_salida, horaSalida, new IClickView() {
                            @Override
                            public void onClick(Object... data) {
                                horaSalida = data[0].toString();
                                checkTime(horaEntrada,horaSalida);
                            }
                        });
                    }
                });
            }
        });

        layout_dias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDays();
            }
        });

        radCashCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (acceptCashCollection)
                    acceptCashCollection = false;
                else
                    acceptCashCollection = true;

                radCashCollect.setChecked(acceptCashCollection);
                validate();
            }
        });

        radFiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (acceptFiar)
                    acceptFiar = false;
                else
                    acceptFiar = true;

                radFiar.setChecked(acceptFiar);
                validate();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserPrivileges(buildPrivileges(), new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        context.alert("Privilegíos actualizados", new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Aceptar";
                            }

                            @Override
                            public void onClick() {
                                context.initHome();
                            }
                        });
                    }
                });
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                QPAY_LinkedUser linkedUser = new QPAY_LinkedUser();
                linkedUser.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
                linkedUser.setQpay_id(data.getQpay_id());

                unlinkUserByAdmin(linkedUser, new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        context.alert("Has desvinculado al cajero", new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Aceptar";
                            }

                            @Override
                            public void onClick() {

                                //20200610 RSB. Quick Fix sin cajeros
                                if(dataLastCashier) {
                                    context.alert(R.string.cajero_1_sin_cajeros, new IAlertButton() {
                                        @Override
                                        public String onText() {
                                            return "Aceptar";
                                        }

                                        @Override
                                        public void onClick() {

                                            QPAY_UserProfile profile = AppPreferences.getUserProfile();
                                            profile.getQpay_object()[0].setQpay_user_type(Globals.ROL_NORMAL);
                                            AppPreferences.setUserProfile(profile);
                                            context.initHome();

                                        }
                                    });

                                } else {
                                    context.initHome();
                                }

                            }
                        });

                    }
                });

            }
        });

        //Carga de privilegíos

        getUserPrivileges();


    }


    /**
     * Checa los días a partir del arreglo checked Days
     */
    private void showAlertDays(){

        AlertDialog.Builder builderDialog = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT);

        //Array a mostrar en el multichoice como opciones
        String[] days = getResources().getStringArray(R.array.days_array);

        builderDialog.setTitle(R.string.cajero_2_alertdays_title);
        builderDialog.setCancelable(false);
        builderDialog.setMultiChoiceItems(days, checkedDays, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item, boolean checked) {
                checkedDays[item] = checked;
            }
        });
        builderDialog.setPositiveButton(R.string.accept_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkDays();
            }
        });
        builderDialog.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        final AlertDialog dialog = builderDialog.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });

        dialog.show();

    }


    /**
     * Valida y setea horas
     * @param hEntrada
     * @param hSalida
     * @return
     */
    private void checkTime(String hEntrada, String hSalida) {

        if (Integer.valueOf(hSalida.replace(":",""))<=Integer.valueOf(hEntrada.replace(":",""))) {
            context.alert(R.string.cajero_2_alertschedule_error);
            textHorario.setTextColor(getResources().getColor(R.color.colorRed));
            scheduleIsValid = false;
        } else {
            textHorario.setTextColor(getResources().getColor(R.color.dusk_blue));
            scheduleIsValid = true;
        }

        textHorario.setText(hEntrada + " - " + hSalida);
        validate();

    }


    /**
     * Valida y setea días
     */
    private void checkDays() {

        //Array de valores a mostrar en el text
        String[] daysValue = getResources().getStringArray(R.array.short_days_array);
        final List<String> daysList = Arrays.asList(daysValue);

        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (int item = 0; item<checkedDays.length; item++) {

            boolean checked = checkedDays[item];

            if (checked) {
                if (first) {
                    sb.append(daysList.get(item));
                } else {
                    sb.append(",");
                    sb.append(daysList.get(item));
                }

                first = false;
            }
        }

        if(!sb.toString().isEmpty()) {
            textDias.setTextColor(getResources().getColor(R.color.dusk_blue));
            textDias.setText(sb.toString());
            daysIsValid = true;
        } else {
            daysIsValid = false;
        }

        validate();

    }


    /**
     * Metodo para poblar la pantalla tras obtener la informacion del servicio
     * @param actualPrivileges
     */
    private void fillScreenData(QPAY_Privileges actualPrivileges) {

        if (actualPrivileges == null) { actualPrivileges = new QPAY_Privileges(); }

        editAlias.setText(actualPrivileges.getLinkAlias()!=null ? actualPrivileges.getLinkAlias() : "");

        String day;
        String[] daysArray = actualPrivileges.getDaysArray();

        for( int i=0; i<=6; i++){
            day = daysArray[i];
            if(day!=null && !day.isEmpty()) { checkedDays[i] = (day.compareTo("1") == 0 ? true : false); }
        }


        checkDays();

        horaEntrada = actualPrivileges.getStartTime();
        horaSalida = actualPrivileges.getEndTime();
        if(horaEntrada!=null && !horaEntrada.isEmpty() && horaSalida!=null && !horaSalida.isEmpty()) {
            checkTime(horaEntrada,horaSalida);
        }

        if (actualPrivileges.getAmountLimit()!=null) {
            String monto = Utils.paserCurrency(actualPrivileges.getAmountLimit().toString());
            editMonto.setTipo(HEditText.Tipo.NONE);
            editMonto.setText(monto);
            editMonto.setTipo(HEditText.Tipo.MONEDA);
        }

        String sCash = actualPrivileges.getCashCollection();
        if(sCash!=null && !sCash.isEmpty()) {
            acceptCashCollection = (sCash.compareTo("1") == 0 ? true : false);
            radCashCollect.setChecked(acceptCashCollection);
            validate();
        }

        String sFiar = actualPrivileges.getFiadoApp();
        if(sFiar!=null && !sFiar.isEmpty()) {
            acceptFiar = (sFiar.compareTo("1") == 0 ? true : false);
            radFiar.setChecked(acceptFiar);
            validate();
        }

    }

    private QPAY_Privileges buildPrivileges(){

        QPAY_Privileges privileges = new QPAY_Privileges();

        //Set Admin seed
        privileges.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        //Set cashierId
        privileges.setUserId(data.getQpay_id());
        //Set alias
        privileges.setLinkAlias(editAlias.getText());
        //Set days
        privileges.setDay1(checkedDays[0]==true ? "1" : "0");
        privileges.setDay2(checkedDays[1]==true ? "1" : "0");
        privileges.setDay3(checkedDays[2]==true ? "1" : "0");
        privileges.setDay4(checkedDays[3]==true ? "1" : "0");
        privileges.setDay5(checkedDays[4]==true ? "1" : "0");
        privileges.setDay6(checkedDays[5]==true ? "1" : "0");
        privileges.setDay7(checkedDays[6]==true ? "1" : "0");
        //Set schedule
        privileges.setStartTime(horaEntrada);
        privileges.setEndTime(horaSalida);
        //Set amount
        if(editMonto.getText()!=null && !editMonto.getText().isEmpty()){
            String dAmount = editMonto.getText().replace("$","").replace(",","");
            privileges.setAmountLimit(Double.valueOf(dAmount));
        }
        //Set cash collection
        privileges.setCashCollection(acceptCashCollection==true ? "1" : "0");
        privileges.setFiadoApp(acceptFiar ? "1" : "0");

        return privileges;
    }


    /**
     * Validación para habilitar el guardado
     */
    private void validate() {

        btnGuardar.setEnabled(false);

        if(!editAlias.isValid()) { return; }

        if(!daysIsValid) { return; }

        if(!scheduleIsValid) { return; }

        //if(!editMonto.isValid()) { return; }

        btnGuardar.setEnabled(true);

    }


    /**
     * Método para llamar servicio para obtener los privilegios del usuario
     *
     */
    private void getUserPrivileges() {

        context.loading(true);

        QPAY_LinkedUser linkedUser = new QPAY_LinkedUser();
        linkedUser.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        linkedUser.setQpay_id(data.getQpay_id());

        IMultiUserListener userPrivilegesListener = null;
        try {
            userPrivilegesListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        Log.d(TAG,"getUserPrivileges response: " + json);
                        QPAY_PrivilegesResponse response = gson.fromJson(json, QPAY_PrivilegesResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            QPAY_Privileges dataPrivileges = response.getQpay_object()[0];
                            fillScreenData(dataPrivileges);

                        } else {

                            if(response.getQpay_code().equals("017")
                                    || response.getQpay_code().equals("018")
                                    || response.getQpay_code().equals("019")
                                    || response.getQpay_code().equals("020")){
                                //Semilla erronea, se cierra la sesión.
                                context.alert(context.getResources().getString(R.string.message_logout), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {

                                        AppPreferences.Logout(context);
                                        context.startActivity(LoginActivity.class, true);

                                    }
                                });

                            } else if(response.getQpay_description().contains("UNAUTHORIZED:HTTP")){
                                //context.showAlertAEONBlockedUser();
                            } else {
                                context.alert(response.getQpay_description());
                            }

                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }
            }, context);
        } catch (Exception e) {
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

        userPrivilegesListener.getUserPrivileges(linkedUser);

    }


    /**
     * Método para llamar servicio para crear o actualizar los permisos del usuario
     * @param function
     */
    private void updateUserPrivileges(QPAY_Privileges privileges, final IFunction function) {

        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(privileges);
        Log.d(TAG,"updateUserPrivileges sending " + json );

        context.loading(true);

        IMultiUserListener userPrivilegesListener = null;
        try {
            userPrivilegesListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_BaseResponse response = gson.fromJson(json, QPAY_BaseResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            if(function != null)
                                function.execute();

                        } else {
                            if(response.getQpay_code().equals("017")
                                    || response.getQpay_code().equals("018")
                                    || response.getQpay_code().equals("019")
                                    || response.getQpay_code().equals("020")){
                                //Semilla erronea, se cierra la sesión.
                                context.alert(context.getResources().getString(R.string.message_logout), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        AppPreferences.Logout(context);
                                        context.startActivity(LoginActivity.class, true);

                                    }
                                });
                            } else if(response.getQpay_description().contains("UNAUTHORIZED:HTTP")){
                                //context.showAlertAEONBlockedUser();
                            } else {
                                context.alert(response.getQpay_description());
                            }


                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }
            }, context);
        } catch (Exception e) {
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

        userPrivilegesListener.createUserPrivileges(privileges);

    }


    /**
     * Método para desligar un cajero de la cuenta de administrador
     * @param function
     */
    private void unlinkUserByAdmin(QPAY_LinkedUser linkedUser, final IFunction function) {

        context.loading(true);

        IMultiUserListener unlinkListener = null;
        try {
            unlinkListener = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_BaseResponse response = gson.fromJson(json, QPAY_BaseResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            if(function != null)
                                function.execute();

                        } else {

                            if(response.getQpay_code().equals("017")
                                    || response.getQpay_code().equals("018")
                                    || response.getQpay_code().equals("019")
                                    || response.getQpay_code().equals("020")){
                                //Semilla erronea, se cierra la sesión.
                                context.alert(context.getResources().getString(R.string.message_logout), new IAlertButton() {
                                    @Override
                                    public String onText() {
                                        return "Aceptar";
                                    }

                                    @Override
                                    public void onClick() {
                                        AppPreferences.Logout(context);
                                        context.startActivity(LoginActivity.class, true);
                                    }
                                });

                            } else if(response.getQpay_description().contains("UNAUTHORIZED:HTTP")){
                               // context.showAlertAEONBlockedUser();
                            } else {
                                context.alert(response.getQpay_description());
                            }

                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    context.loading(false);
                    context.alert(R.string.general_error);
                }
            }, context);
        } catch (Exception e) {
            context.loading(false);
            context.alert(R.string.general_error_catch);
        }

        unlinkListener.unlinkUserByAdmin(linkedUser);

    }


    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}
