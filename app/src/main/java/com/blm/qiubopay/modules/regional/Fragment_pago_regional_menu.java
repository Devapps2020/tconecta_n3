package com.blm.qiubopay.modules.regional;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.activities.ScanActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.database.transactions.DataHelper;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.IScreenTxn;
import com.blm.qiubopay.listeners.IStartTxn;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.PQPrefijo;
import com.blm.qiubopay.models.QPAY_ScreenTxn;
import com.blm.qiubopay.models.QPAY_StartTxn;
import com.blm.qiubopay.models.QPAY_StartTxn_item;
import com.blm.qiubopay.models.QPAY_StartTxn_object;
import com.blm.qiubopay.models.QPAY_StartTxn_response;
import com.blm.qiubopay.models.pagos_qiubo.QPAY_QiuboPaymentItem;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;
import mx.devapps.utils.interfaces.IRequestPermissions;

public class Fragment_pago_regional_menu extends HFragment implements IMenuContext {

    private ArrayList<CViewEditText> campos = null;
    private Spinner spin_companias = null;

    public static List<QPAY_QiuboPaymentItem> proveedores;
    public static ArrayList<PQPrefijo> prefijos;
    public static String clave_qiubo;

    private PQPrefijo prefijo;
    private QPAY_StartTxn_response response;

    private LinearLayout layout_content_screen;
    private LinearLayout layout_content_screen_btn;

    private CViewEditText input;
    private int size = 0;
    private IFunction continuar;
    private IFunction functionExecute;
    private IFunction validate;
    private IFunction validateAmount;
    private Integer amountByUser;
    private QPAY_StartTxn_item button;
    private boolean consumeAutoService;
    private boolean finish;

    public static Fragment_pago_regional_menu newInstance() {
        return new Fragment_pago_regional_menu();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreated(inflater.inflate(R.layout.fragment_pago_regional_menu, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment() {


        CViewMenuTop.create(getView()).onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        campos = new ArrayList();

        spin_companias = getView().findViewById(R.id.spin_companias);
        layout_content_screen = getView().findViewById(R.id.layout_content_screen);
        layout_content_screen_btn = getView().findViewById(R.id.layout_content_screen_btn);

        TextView text_saldo = getView().findViewById(R.id.text_saldo);
        text_saldo.setText(getContextMenu().getSaldo());

        proveedores.add(0, new QPAY_QiuboPaymentItem("0", "0", "Seleccionar proveedor", "0", "1", "1"));

        initProducto();

        ProveedoresAdapter mCustomAdapter = new ProveedoresAdapter(getContext(), prefijos);
        spin_companias.setAdapter(mCustomAdapter);

        spin_companias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                prefijo = mCustomAdapter.getItem(i);

                layout_content_screen.removeAllViews();
                layout_content_screen_btn.removeAllViews();

                if("0".equals(prefijo.getPrefijo()))
                    return;

                initStartTxn();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        LinearLayout layout_scaner = getView().findViewById(R.id.layout_scaner);

        layout_scaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getContext().requestPermissions(new IRequestPermissions() {
                    @Override
                    public void onPostExecute() {

                        ScanActivity.action = new ZBarScannerView.ResultHandler() {
                            @Override
                            public void handleResult(Result result) {

                                if(result == null)
                                    return;

                                try{

                                    String resultado = result.getContents().trim();

                                    resultado = resultado.replaceAll("[^0-9]","").trim();

                                    clave_qiubo = resultado;

                                    initStartTxn();

                                }catch (Exception ex){

                                }
                            }
                        };

                        getContext().startActivity(ScanActivity.class, false);
                    }
                }, new String[]{Manifest.permission.CAMERA});

            }
        });

    }

    public static void initProducto(){

        prefijos = new ArrayList();
        QPAY_QiuboPaymentItem item;

        for(int i=0; i< proveedores.size(); i++) {
            Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_QiuboPaymentItem.QPAY_QiuboPaymentItemExcluder()).create();
            String json = gson.toJson(proveedores.get(i));
            item = gson.fromJson(json, QPAY_QiuboPaymentItem.class);

            prefijos.add(new PQPrefijo(item.getCompany(), item.getPrefix(), null));
        }

    }

    public void startTxn(final IFunction function){

        getContext().loading(true);

        try {

            QPAY_StartTxn qpay_startTxn = new QPAY_StartTxn();

            qpay_startTxn.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            qpay_startTxn.setQpay_seedtimestamp(Utils.timeStamp());

            IStartTxn iStartTxn = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    String gson = new Gson().toJson(result).replaceAll("data-","").replaceAll("return", "returnn");

                    Log.e("response", "" + gson);

                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    } else {

                        QPAY_StartTxn_response response_txn = new Gson().fromJson(gson, QPAY_StartTxn_response.class);

                        if("000".equals(response_txn.getQpay_code())) {
                            function.execute(response_txn);
                        } else {
                            getContext().alert(R.string.general_error_catch);
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                }

            }, getContext());

            Log.e("request", "" + new Gson().toJson(qpay_startTxn));

            iStartTxn.startTxn(qpay_startTxn);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    public void initStartTxn(){

        startTxn(new IFunction() {
            @Override
            public void execute(Object[] data) {

                response = (QPAY_StartTxn_response)data[0];

                functionExecute = new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        getContextMenu().cargarSaldo(true,false,false, new IFunction() {
                            @Override
                            public void execute(Object[] data) {

                                Fragment_pago_regional_ticket.response = (QPAY_StartTxn_object) data[0];

                                CApplication.setAnalytics(CApplication.ACTION.CB_PAGOS_REGIONALES_pagan, new HashMap<String, String>() {{
                                    put(CApplication.ACTION.OPCION.name(), "" + prefijo.getEmpresa());
                                    put(CApplication.ACTION.MONTO.name(), "" + Fragment_pago_regional_ticket.response.getAmount());
                                }});

                                getContext().setFragment(Fragment_pago_regional_ticket.newInstance(), true);
                            }
                        });

                    }
                };

                continuar = new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        if(button != null)
                            size++;

                        QPAY_StartTxn_item[] array = new QPAY_StartTxn_item[size];

                        int sizee = 0;

                        for(int i=0; i<response.getQpay_object()[0].getItems().length; i++) {
                            if("true".equals(response.getQpay_object()[0].getItems()[i].getReturnn()) && !"button".equals(response.getQpay_object()[0].getItems()[i].getFieldType())) {
                                array[sizee] = new QPAY_StartTxn_item(response.getQpay_object()[0].getItems()[i].getName(), response.getQpay_object()[0].getItems()[i].getValue());
                                sizee++;
                            }
                        }

                        if(button != null) {
                            array[sizee] = new QPAY_StartTxn_item(button.getName(), button.getValue());
                            button = null;
                        }

                        response.getQpay_object()[0].setItems(array);

                        screenTxn(response);

                    }
                };

                renderScreen();

            }
        });

    }

    public void renderScreen() {

        int first = 0;

        layout_content_screen.removeAllViews();
        layout_content_screen_btn.removeAllViews();

        size = 0;

        for(final QPAY_StartTxn_item item: response.getQpay_object()[0].getItems()) {

            if("tenderToken".equals(item.getName()))
                finish = true;

            LayoutInflater inflater = LayoutInflater.from(getContext());

            if("true".equals(item.getReturnn()) && !"button".equals(item.getFieldType()))
                size++;

            switch (item.getFieldType()) {
                case "textbox":
                    LinearLayout view = (LinearLayout) inflater.inflate(R.layout.item_layout_screen, null, true);

                    if(item.getText().contains("monto")) {

                        input = CViewEditText.create(view.findViewById(R.id.edit_1))
                                .setRequired(true)
                                .setMinimum(1)
                                .setMaximum(11)
                                .setType(CViewEditText.TYPE.CURRENCY_SD)
                                .setHint(item.getText())
                                .setAlert(R.string.text_input_required)
                                .setTextChanged(new CViewEditText.ITextChanged() {
                                @Override
                                public void onChange(String text) {

                                    Integer iValue = 0;
                                    if (!input.getTextInteger().isEmpty()) {
                                        iValue = Integer.parseInt(input.getTextInteger());
                                    }

                                    item.setValue(iValue+"");

                                    amountByUser = iValue;
                                    if(iValue>0){
                                        validateAmount = null;
                                    } else {
                                        validateAmount = validateFunction();
                                    }

                                }});

                        validateAmount = validateFunction();

                    } else {

                        input = CViewEditText.create(view.findViewById(R.id.edit_1))
                                .setRequired(true)
                                .setMinimum(1)
                                .setMaximum(25)
                                .setType(CViewEditText.TYPE.NUMBER)
                                .setHint(item.getText())
                                .setAlert(R.string.text_input_required)
                                .setTextChanged(new CViewEditText.ITextChanged() {
                                    @Override
                                    public void onChange(String text) {
                                        item.setValue(text);
                                    }});

                    }

                    input.setHint("" + item.getText().replace("Qiubo", "regional"));

                    if("concode".equals(item.getName())) {
                        input.setText(clave_qiubo);
                        clave_qiubo = null;

                        validate = new IFunction() {
                            @Override
                            public void execute(Object[] data) {

                                if(input.getText().startsWith(prefijo.getPrefijo())) {

                                    //Valida el tipo de usuario y si puede realizar la operación
                                    if(!getContextMenu().validateUserOperation(true)){

                                        getContext().alert(R.string.cajero_sin_permiso, new IAlertButton() {
                                            @Override
                                            public String onText() {
                                                return "Aceptar";
                                            }

                                            @Override
                                            public void onClick() {
                                                getContextMenu().initHome();
                                            }
                                        });

                                        return;
                                    }

                                    validate = null;

                                    continuar.execute();

                                }
                                else
                                    getContext().alert("La referencia no es válida");

                            }
                        };

                        //20200731 RSB. Imp. Amazon y ViaSat, o culaquier servicio que necesite un consumo inicial
                        if(consumeAutoService){ item.setValue(input.getText()); }
                    }

                    layout_content_screen.addView(view);
                    break;

                case "button":

                    LinearLayout layput_espacio = (LinearLayout) inflater.inflate(R.layout.item_espacio_screen, null, true);
                    LinearLayout layput_button = (LinearLayout) inflater.inflate(R.layout.item_button_screen, null, true);

                    Button btn_button = layput_button.findViewById(R.id.btn_button);
                    btn_button.setText(item.getText());
                    btn_button.setTag(item.getValue());

                    btn_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            button = item;

                            if("Regresar".equals(item.getText().trim()) || "Cancelar".equals(item.getText().trim()))
                                getContext().backFragment();
                            else {

                                if(validate != null) {
                                    validate.execute();
                                } else if (validateAmount != null) {
                                    validateAmount.execute();
                                } else {
                                    continuar.execute();
                                }

                            }

                        }
                    });

                    if(consumeAutoService){ button = item; }

                    if(first == 0) {
                        layout_content_screen_btn.addView(layput_espacio);
                        first++;
                    }

                    layout_content_screen_btn.addView(layput_button);

                    break;
                case "label":

                    LinearLayout label_text = (LinearLayout) inflater.inflate(R.layout.item_label_screen, null, true);

                    TextView label_text_screen = label_text.findViewById(R.id.label_text_screen);
                    label_text_screen.setText(item.getText());

                    if(!"Veolia".equals(item.getText().trim()))
                        layout_content_screen.addView(label_text);

                    break;
            }
        }

        if(consumeAutoService){
            consumeAutoService = false;
            if (validate != null) {
                validate.execute();
            } else {
                continuar.execute();
            }
        }

    }

    public void screenTxn(QPAY_StartTxn_response request){

        getContext().loading(true);

        try {

            QPAY_ScreenTxn qpayScreenTxn = new QPAY_ScreenTxn();

            qpayScreenTxn.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
            qpayScreenTxn.setQpay_seedtimestamp(Utils.timeStamp());
            qpayScreenTxn.setQpay_requestId(request.getQpay_object()[0].getRequestId());
            qpayScreenTxn.setDynamicData(new QPAY_StartTxn_object());
            qpayScreenTxn.getDynamicData().setItems(response.getQpay_object()[0].getItems());

            IScreenTxn iScreenTxn = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContext().loading(false);

                    String gson = new Gson().toJson(result).replaceAll("data-","").replaceAll("return", "returnn");

                    Log.d("PagoQiubo", "Pago 2 Response: " + gson);
                    getContext().loading(false);
                    if (result instanceof ErrorResponse) {
                        getContext().alert(R.string.general_error);
                    } else {

                        QPAY_StartTxn_response response_txn = new Gson().fromJson(gson, QPAY_StartTxn_response.class);

                        if("000".equals(response_txn.getQpay_code())) {

                            if(response_txn.getQpay_object()[0].getTransRef() == null) {
                                response = response_txn;
                                renderScreen();
                            } else {

                                //20200713 RSB. Valida si debe persistir tras ingresar a reporte de transacciones
                                if(AppPreferences.getLocalTxnQiubo()) {
                                    try {
                                        Gson gsonTxr = new Gson();
                                        DataHelper dataHelper = new DataHelper(getContext());
                                        //dataHelper.insertQiuboPayTxr(Tools.getTodayDate(), gsonTxr.toJson(response_txn.getQpay_object()[0]),null);
                                        dataHelper.insertLocalTransaction(DataHelper.DB_TXR_TYPE.PQ_TXR, Tools.getTodayDate(), "", gsonTxr.toJson(response_txn.getQpay_object()[0]), null);
                                    } catch (Exception e) {
                                        Log.e("DATA HELPER", "ERROR AL DAR DE ALTA EL REGISTRO");
                                    }
                                }

                                functionExecute.execute(response_txn.getQpay_object()[0]);
                            }

                        } else {
                            getContextMenu().validaSesion(response_txn.getQpay_code(), response_txn.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                }

            }, getContext());

            String request_obj = new Gson().toJson(qpayScreenTxn)
                    .replaceAll("fieldType", "data-fieldType")
                    .replaceAll("name", "data-name")
                    .replaceAll("value", "data-value")
                    .replaceAll("returnn", "return")
                    .replaceAll("text", "data-text");

            Log.d("Conecta", "Pago 2 Request: " + request_obj);

            iScreenTxn.screenTxn(request_obj);

        } catch (Exception e) {
            e.printStackTrace();
            getContext().loading(false);
            getContext().alert(R.string.general_error_catch);
        }

    }

    private IFunction validateFunction () {
        return new IFunction() {
            @Override
            public void execute(Object[] data) {
                amountByUser = (amountByUser != null ? amountByUser : 0);
                if (amountByUser <= 0) {
                    getContext().alert("El monto debe ser mayor a $0.00");
                }
            }
        };
    }

    public static String getNameProduct(String clave){

        for(int i=0; i<proveedores.size(); i++) {
            Log.d("clave", clave + " - " + proveedores.get(i).getPrefix().trim());
            if(clave.trim().equals(proveedores.get(i).getPrefix().trim() + ""))
                return proveedores.get(i).getCompany();
        }

        return "Pago Regional";
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    public class ProveedoresAdapter extends ArrayAdapter<PQPrefijo> {

        private List<PQPrefijo> proveedores;

        public ProveedoresAdapter(Context context, List<PQPrefijo> proveedores) {
            super(context, R.layout.item_option_recargas);
           this.proveedores = proveedores;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return getView(position, convertView, parent);
        }

        @Override
        public int getCount() {
            return proveedores.size();
        }

        public PQPrefijo getItem(int position) {
            return proveedores.get(position);
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option_recargas, parent, false);

            TextView text_name = convertView.findViewById(R.id.text_name);
            TextView text_description = convertView.findViewById(R.id.text_description);
            ImageView img_marca = convertView.findViewById(R.id.img_marca);
            PQPrefijo data = proveedores.get(position);

            img_marca.setImageDrawable(img_marca.getContext().getResources().getDrawable(R.drawable.toda));

            if(data.getEmpresa() != null)
                text_name.setText(data.getEmpresa());

            if(data.getPrefijo() != null)
                if(!"0".equals(data.getPrefijo()))
                    text_description.setText("Código : " + data.getPrefijo());
                else {
                    text_name.setText("Servicio Regional");
                    text_description.setText(data.getEmpresa());
                    img_marca.setImageDrawable(img_marca.getContext().getResources().getDrawable(R.drawable.pagos_regionales));
                }



            return convertView;
        }

        public PQPrefijo getPQPrefijo(int position){
            return proveedores.get(position);
        }

    }

}