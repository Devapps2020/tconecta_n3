package com.blm.qiubopay.modules;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blm.qiubopay.components.CViewMenuTop;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.reginald.editspinner.EditSpinner;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.listeners.IDongleCost;
import com.blm.qiubopay.listeners.IDonglePurchase;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_CashCollectionResponse;
import com.blm.qiubopay.models.QPAY_Seed;
import com.blm.qiubopay.models.dongle.QPAY_DongleBuyResponse;
import com.blm.qiubopay.models.dongle.QPAY_DongleCostResponse;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;

import mx.devapps.utils.interfaces.IAlertButton;

public class Fragment_compra_rollos_1_v2 extends HFragment {
    private View view;
    private MenuActivity context;
    private QPAY_CashCollectionResponse data;

    private ArrayList<HEditText> campos;
    private Button btn_confirmar;

    private EditSpinner bancos;
    private EditSpinner formas_pago;

    private TextView precio_lector;
    private TextView direccion_cliente;

    private String customerAddress;
    private String dongleCost;

    private ArrayList<String> formas_pago_array;

    public static Fragment_compra_rollos_1_v2 newInstance(Object... data) {
        Fragment_compra_rollos_1_v2 fragment = new Fragment_compra_rollos_1_v2();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_compra_rollos_1_v2", new Gson().toJson(data[0]));

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
            data = new Gson().fromJson(getArguments().getString("Fragment_compra_rollos_1_v2"), QPAY_CashCollectionResponse.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_compra_rollos_1_v2, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(view).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        customerAddress = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_street() + " " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_external_number();
        customerAddress += "\n" + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_suburb();
        customerAddress += "\n" + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_municipality() + " C.P. " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_postal_code();
        customerAddress += "\n" + getStateName(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_merchant_state().trim()).toUpperCase();

        precio_lector       = view.findViewById(R.id.precio_lector);
        direccion_cliente   = view.findViewById(R.id.direccion_cliente);

        direccion_cliente.setText(customerAddress);

        btn_confirmar = view.findViewById(R.id.btn_confirmar);
        btn_confirmar.setEnabled(false);
        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_confirmar.setEnabled(false);

                if(theUserCanBuyDongle(AppPreferences.getKinetoBalance(), dongleCost)) {
                    context.alert(context.getResources().getString(R.string.alert_message_buy_a_dongle).replace("**monto**", dongleCost), new IAlertButton() {
                        @Override
                        public String onText() {
                            return "SI";
                        }

                        @Override
                        public void onClick() {
                            //context.setFragment(Fragment_autorizacion_1.newInstance());
                            compraDeDongle();

                        }
                    }, new IAlertButton() {
                        @Override
                        public String onText() {
                            return "NO";
                        }

                        @Override
                        public void onClick() {
                            btn_confirmar.setEnabled(true);
                        }
                    });

                }
                else {
                    context.alert("No cuenta con saldo suficiente para realizar esta operación.");
                    btn_confirmar.setEnabled(true);
                }
            }
        });

        //setFormasDePago();

        //recuperaPrecioDongle();
    }

    public void setDataSpinner(EditSpinner spiner, ArrayList<String> data){

        final LinearLayout layout_formulario = view.findViewById(R.id.layout_formulario);

        ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item,data);
        spiner.setAdapter(adapter);
        spiner.setEditable(false);
        spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                layout_formulario.setVisibility(View.VISIBLE);

            }
        });
    }

    //Recuperar precio del dongle
    public void recuperaPrecioDongle() {

        context.loading(true);

        try {

            IDongleCost sale = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Logger.i(new Gson().toJson(result));

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);
                    } else {
                        //Respuesta correcta
                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_DongleCostResponse.QPAY_DongleCostResponseExcluder()).create();
                        String json = gson.toJson(result);
                        QPAY_DongleCostResponse dongleCostResponse = gson.fromJson(json, QPAY_DongleCostResponse.class);

                        if (dongleCostResponse.getQpay_response().equals("true")) {
                            dongleCost = "$" + String.format("%.02f", dongleCostResponse.getQpay_object()[0].getCost()) + " MXN";
                            precio_lector.setText(dongleCost);
                            btn_confirmar.setEnabled(true);
                        } else if (dongleCostResponse.getQpay_response().equals("false")) {
                            context.alert("Error " + dongleCostResponse.getQpay_code() + "\n" + dongleCostResponse.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    context.loading(false);

                    context.alert(R.string.general_error);

                }
            }, context);

            sale.doGetDongleCost();

        } catch (Exception e) {

            context.loading(false);

            e.printStackTrace();
            context.alert(R.string.general_error_catch);
        }

    }

    private Boolean theUserCanBuyDongle(String balance, String dongleCost)
    {
        Boolean back = true;
        try
        {
            Double userBalance = new Double(Tools.getOnlyNumbers(balance));
            Double donglePrice = new Double(Tools.getOnlyNumbers(dongleCost));
            if(donglePrice > userBalance)
                back = false;
        }catch (Exception e)
        {
            back = false;
        }
        return back;
    }

    //Compra de dongle
    public void compraDeDongle() {

        context.loading(true);

        QPAY_Seed seed = new QPAY_Seed();
        seed.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

        try {

            IDonglePurchase sale = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Logger.i(new Gson().toJson(result));

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);
                        btn_confirmar.setEnabled(true);
                    } else {
                        //Respuesta correcta
                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_DongleBuyResponse.QPAY_DongleBuyResponseExcluder()).create();
                        String json = gson.toJson(result);
                        Log.i("","");
                        QPAY_DongleBuyResponse dongleBuyResponse = gson.fromJson(json, QPAY_DongleBuyResponse.class);

                        if (dongleBuyResponse.getQpay_response().equals("true")) {
                            String balance = "Saldo " + Utils.paserCurrency(dongleBuyResponse.getQpay_object()[0].getBalance().replace("MXN", ""));
                            AppPreferences.setKinetoBalance(balance);
                            //context.showAlert("Compra exitosa" + "\n" + "En unos momentos recibirá un correo confirmando su compra.");
                            context.alert("Compra exitosa" + "\n" + "En unos momentos recibirá un correo confirmando su compra.", new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "ACEPTAR";
                                }

                                @Override
                                public void onClick() {
                                    //context.setFragment(Fragment_autorizacion_1.newInstance());
                                    //compraDeDongle();
                                    context.initHome();
                                }
                            });

                        } else if (dongleBuyResponse.getQpay_response().equals("false")) {
                            context.alert("Error " + dongleBuyResponse.getQpay_code() + "\n" + dongleBuyResponse.getQpay_description());
                            btn_confirmar.setEnabled(true);
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    context.loading(false);

                    context.alert(R.string.general_error);

                    btn_confirmar.setEnabled(true);

                }
            }, context);

            sale.doPurchase(seed);

        } catch (Exception e) {

            context.loading(false);

            e.printStackTrace();
            context.alert(R.string.general_error_catch);

            btn_confirmar.setEnabled(true);
        }

    }

    public void setFormasDePago(){

        formas_pago = view.findViewById(R.id.edit_forma_pago);
        formas_pago_array = new ArrayList();
        //for(int i=0; i<stateList.size(); i++)
        formas_pago_array.add("Redención de bolsa de saldo");
        formas_pago_array.add("Pago con tarjeta (próximamente)");
        setDataSpinner2(formas_pago, formas_pago_array);
        formas_pago.setText(formas_pago_array.get(0));

    }

    public void setDataSpinner2(final EditSpinner spiner, final ArrayList<String> data){

        ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item,data);
        spiner.setAdapter(adapter);
        spiner.setEditable(false);
        spiner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                spiner.setText(formas_pago_array.get(0));
                //spiner.setTag(stateList.get(position).getState_code());
                //validate();
            }
        });
    }

    private String getStateName(String code){
        String back = "";
        switch (code)
        {
            case "01":
                back = "Aguascalientes";
                break;
            case "02":
                back = "Baja California";
                break;
            case "03":
                back = "Baja California Sur";
                break;
            case "04":
                back = "Campeche";
                break;
            case "05":
                back = "Chiapas";
                break;
            case "06":
                back = "Chihuahua";
                break;
            case "07":
                back = "Coahuila";
                break;
            case "08":
                back = "Colima";
                break;
            case "09":
                back = "Ciudad de Mexico";
                break;
            case "10":
                back = "Durango";
                break;
            case "11":
                back = "Guanajuato";
                break;
            case "12":
                back = "Guerrero";
                break;
            case "13":
                back = "Hidalgo";
                break;
            case "14":
                back = "Jalisco";
                break;
            case "15":
                back = "Mexico";
                break;
            case "16":
                back = "Michoacan";
                break;
            case "17":
                back = "Morelos";
                break;
            case "18":
                back = "Nayarit";
                break;
            case "19":
                back = "Nuevo Leon";
                break;
            case "20":
                back = "Oaxaca";
                break;
            case "21":
                back = "Puebla";
                break;
            case "22":
                back = "Queretaro";
                break;
            case "23":
                back = "Quintana Roo";
                break;
            case "24":
                back = "San Luis Potosi";
                break;
            case "25":
                back = "Sinaloa";
                break;
            case "26":
                back = "Sonora";
                break;
            case "27":
                back = "Tabasco";
                break;
            case "28":
                back = "Tamaulipas";
                break;
            case "29":
                back = "Tlaxcala";
                break;
            case "30":
                back = "Veracruz";
                break;
            case "31":
                back = "Yucatan";
                break;
            case "32":
                back = "Zacatecas";
                break;
        }
        return back;
    }
}
