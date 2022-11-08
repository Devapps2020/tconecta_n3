package com.blm.qiubopay.modules.restaurante;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.database.operative.RestaurantDataHelper;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.helpers.interfaces.ITextChanged;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.IRestaurantOperative;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.operative.PayType;
import com.blm.qiubopay.models.operative.restaurant.DbDetail;
import com.blm.qiubopay.models.operative.restaurant.DbOrder;
import com.blm.qiubopay.models.operative.restaurant.QPAY_SaveTipResponse;
import com.blm.qiubopay.models.operative.restaurant.QPAY_TipDetail;
import com.blm.qiubopay.models.operative.restaurant.QPAY_TipOrder;
import com.blm.qiubopay.modules.Fragment_pago_financiero_N3;
import com.blm.qiubopay.modules.financiero.Fragment_pago_financiero_tarjeta;
import com.blm.qiubopay.utils.OperativeUtils;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_restaurante_2 extends HFragment implements IMenuContext {
    private View view;
    private MenuActivity context;
    private Object data;
    private Button btn_continuar;
    private Button btn_cancelar;

    private TextView txt_commensal;

    private int commensals_to_pay;
    private int maxCommensals = 10;

    private Double amountPerCommensal;
    private Double tipAmount;
    private Double totalAmount;
    private Double tipPercent;

    private EditText edit_total;
    private HEditText monto;
    private HEditText edit_tip_percent;


    private RestaurantDataHelper dataHelper;

    private ArrayList<RadioButton> payTypeArray;
    private ArrayList<RadioButton> tipTypeArray;

    private DbOrder dbOrder;

    private PayType payType;

    private DecimalFormat decimalFormat;

    private ArrayList<DbDetail> dbDetailArrayList;

    private int coveredCommensals = 0;
    private Double coveredAmount = 0.00;

    private TextView commensal_name;

    private QPAY_TipOrder order;

    public static Fragment_restaurante_2 newInstance(Object... data) {
        Fragment_restaurante_2 fragment = new Fragment_restaurante_2();
        Bundle args = new Bundle();

        if(data.length > 0)
            args.putString("Fragment_restaurante_2", new Gson().toJson(data[0]));

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
            data = new Gson().fromJson(getArguments().getString("Fragment_restaurante_2"), Object.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if( view != null)
            return view;

        view = inflater.inflate(R.layout.fragment_restaurante_2, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(getView()).setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        decimalFormat = new DecimalFormat("#.00",otherSymbols);

        dataHelper = new RestaurantDataHelper(context);
        dbOrder = dataHelper.getLastOpenOrder();

        initAmounts();

        ScrollView scrollView = view.findViewById(R.id.scrollView);

        commensal_name = view.findViewById(R.id.commensal_name);
        commensal_name.setText(String.format("Persona %d", coveredCommensals + 1));

        //edit_total      = view.findViewById(R.id.edit_importe);
        monto = new HEditText((EditText) view.findViewById(R.id.edit_importe),
                true, 11, 1, HEditText.Tipo.MONEDA, new ITextChanged() {
            @Override
            public void onChange() {

                if(monto.isValid())
                    btn_continuar.setEnabled(true);
                else
                    btn_continuar.setEnabled(false);

            }

            @Override
            public void onMaxLength() {
                getContext().hideKeyboard();
            }
        });
        monto.disableLongClickable();

        monto.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    scrollView.smoothScrollTo(0,monto.getEditText().getBottom() + 15);
                }
            }
        });

        edit_tip_percent = new HEditText((EditText) view.findViewById(R.id.edit_tip_percent),
                true, 2, 1, HEditText.Tipo.TIP, new ITextChanged() {
            @Override
            public void onChange() {

                updateAmounts();

            }

            @Override
            public void onMaxLength() {
                getContext().hideKeyboard();
            }
        },null);
        edit_tip_percent.disableLongClickable();
        edit_tip_percent.getEditText().setEnabled(false);

        payTypeArray = new ArrayList();
        payTypeArray.add((RadioButton) view.findViewById(R.id.rad_credit_card));
        payTypeArray.add((RadioButton) view.findViewById(R.id.rad_cash));

        check(true,false,0);

        payTypeArray.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check(false,false,0);
                payType = PayType.CREDIT;
                validate();
            }
        });

        payTypeArray.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check(false,false,1);
                payType = PayType.CASH;
                validate();
            }
        });

        tipTypeArray = new ArrayList();
        tipTypeArray.add((RadioButton) view.findViewById(R.id.rad_10));
        tipTypeArray.add((RadioButton) view.findViewById(R.id.rad_13));
        tipTypeArray.add((RadioButton) view.findViewById(R.id.rad_15));
        tipTypeArray.add((RadioButton) view.findViewById(R.id.rad_other));

        check(true,true,0);

        tipTypeArray.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gender = GENERO_MASCULINO;
                tipPercent = 10.00;
                edit_tip_percent.getEditText().setEnabled(false);
                check(false,true,0);
                updateAmounts();
                validate();
            }
        });

        tipTypeArray.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gender = GENERO_MASCULINO;
                tipPercent = 13.00;
                edit_tip_percent.getEditText().setEnabled(false);
                check(false,true,1);
                updateAmounts();
                validate();
            }
        });

        tipTypeArray.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gender = GENERO_MASCULINO;
                tipPercent = 15.00;
                edit_tip_percent.getEditText().setEnabled(false);
                check(false,true,2);
                updateAmounts();
                validate();
            }
        });

        tipTypeArray.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gender = GENERO_MASCULINO;
                edit_tip_percent.setNewText("");
                tipPercent = 0.00;
                edit_tip_percent.getEditText().setEnabled(true);
                check(false, true,3);
                updateAmounts();
                validate();
            }
        });

        txt_commensal   = view.findViewById(R.id.txt_commensal);
        //edit_total      = view.findViewById(R.id.edit_total);

        LinearLayout layout_commensals = view.findViewById(R.id.layout_commensals);
        layout_commensals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dbDetailArrayList != null)
                    showCommensalToPay(remainingCommensal());
                else
                    showCommensalToPay(dbOrder.getCommensal_number());
            }
        });

        btn_continuar = view.findViewById(R.id.btn_continuar);
        btn_continuar.setOnClickListener(new View.OnClickListener() {
            @Override//amountPerCommensal
            public void onClick(View v) {
                if(!getAmountWithoutFormat().trim().isEmpty()) {
                    updateAmounts();
                    if(payType == PayType.CASH) {
                        if (dataHelper.insertOrderDetail(dbOrder.getOrder_id(), commensals_to_pay, tipPercent, (amountPerCommensal*commensals_to_pay), tipAmount, totalAmount, payType, "0", "0")) {

                            context.alert(R.string.restaurante_10, new IAlertButton() {
                                @Override
                                public String onText() {
                                    return "ACEPTAR";
                                }

                                @Override
                                public void onClick() {
                                    initFragment();
                                }
                            });
                        }
                    }else{
                        //El pago es con tarjeta de crédito.
                        DbDetail dbDetail = new DbDetail();

                        dbDetail.setFk_order(dbOrder.getOrder_id());
                        dbDetail.setDate(dbOrder.getDate());
                        dbDetail.setCommensals_no(commensals_to_pay);//dbOrder.getCommensal_number());
                        //dbDetail.setAmount(Double.parseDouble(getAmountWithoutFormat()));
                        dbDetail.setAmount(amountPerCommensal*commensals_to_pay);
                        dbDetail.setTip_percent(tipPercent);
                        dbDetail.setTip_amount(tipAmount);
                        dbDetail.setTotal(totalAmount);
                        dbDetail.setFolio("0");
                        dbDetail.setAuth("0");
                        dbDetail.setPayment_type(PayType.CREDIT);

                        context.setFragment(Fragment_pago_financiero_N3.newInstance(false, dbDetail));

                    }
                }
            }
        });

        btn_cancelar = view.findViewById(R.id.btn_cancelar);
        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override//amountPerCommensal
            public void onClick(View v) {
                getContext().alert(R.string.restaurante_12, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "SI";
                    }

                    @Override
                    public void onClick() {
                        dataHelper.delTransactions(dbOrder.getOrder_id());
                        context.initHome();
                    }
                }, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "NO";
                    }

                    @Override
                    public void onClick() {
                        //getContext().backFragment();
                    }
                });

            }
        });

        setCommensalNumber();

        monto.setNewText(Utils.paserCurrency(decimalFormat.format(amountPerCommensal)));

        updateAmounts();

        checkOrder();

        btn_continuar.setEnabled(false);
    }

    private void initAmounts()
    {
        dbDetailArrayList = dataHelper.getOrderDetail(dbOrder.getOrder_id());

        commensals_to_pay = 1;
        tipPercent = 0.00;

        if(dbDetailArrayList != null) {
            remainingCommensal();
        }

        //No se han registrado pagos
        amountPerCommensal = dbOrder.getAmount() / dbOrder.getCommensal_number();

        tipAmount = OperativeUtils.getTipAmount(amountPerCommensal, tipPercent);
    }

    private void setCommensalNumber(){
        txt_commensal.setText(""+commensals_to_pay);
    }

    private void check(Boolean restore, Boolean tip, int position)
    {
        if(tip) {
            for (int i = 0; i < tipTypeArray.size(); i++) {
                if (i != position)
                    tipTypeArray.get(i).setChecked(false);
                else if(restore)
                    tipTypeArray.get(i).setChecked(false);
            }
        }else{
            for (int i = 0; i < payTypeArray.size(); i++) {
                if (i != position && !restore)
                    payTypeArray.get(i).setChecked(false);
                else if(restore)
                    payTypeArray.get(i).setChecked(false);
            }
        }

    }

    private void validate() {

        int select = 0;
        for(int i=0; i<payTypeArray.size(); i++)
            if(payTypeArray.get(i).isChecked())
                select++;

        if(select < 1){
            //text_error_genero.setVisibility(View.VISIBLE);
            return;
        }

        btn_continuar.setEnabled(true);
    }

    private void showCommensalToPay(int number){
        View dv = getLayoutInflater().inflate(R.layout.dialog_commensal, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(context).setView(dv).create();
        ListView lv = dv.findViewById(R.id.aidlistView);
        List<Map<String, String>> listItem = new ArrayList<>();

        for(int i=1; i<=number; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("index", ""+i);
            listItem.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(context,
                listItem,
                R.layout.commensal_item,
                new String[]{"index"},
                new int[]{R.id.commensal});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                alertDialog.dismiss();
                alertDialog.cancel();

                commensals_to_pay = position + 1;
                setCommensalNumber();
                updateAmounts();

            }
        });

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void updateAmounts(){
        if(edit_tip_percent.getEditText().isEnabled())
            tipPercent = Double.parseDouble(getTipPercentWithoutFormat().trim().isEmpty() ? "0.00" : getTipPercentWithoutFormat());

        tipAmount = OperativeUtils.getTipAmount(amountPerCommensal * commensals_to_pay, tipPercent);

        if(dbDetailArrayList != null){
            //Ya se han registrado pagos
            amountPerCommensal = remainingAmount() / remainingCommensal();
        }else {
            //No se han registrado pagos
            amountPerCommensal = dbOrder.getAmount() / dbOrder.getCommensal_number();
        }
        totalAmount = (amountPerCommensal * commensals_to_pay) + tipAmount;

        if(!edit_tip_percent.getEditText().isEnabled()){
            edit_tip_percent.setNewText(OperativeUtils.paserTip(decimalFormat.format(tipPercent)));
        }

        monto.setNewText(Utils.paserCurrency(decimalFormat.format(totalAmount)));
    }

    private String getAmountWithoutFormat(){
        return monto.getText().replace("$","").replace(",","");
    }

    private String getTipPercentWithoutFormat(){
        return edit_tip_percent.getText().replace("$","").replace(",","");
    }

    private int remainingCommensal(){
        coveredCommensals = 0;

        for (int i=0;i<dbDetailArrayList.size();i++)
            coveredCommensals += dbDetailArrayList.get(i).getCommensals_no();

        return dbOrder.getCommensal_number()-coveredCommensals;
    }

    private Double remainingAmount(){
        coveredAmount = 0.00;

        for (int i=0;i<dbDetailArrayList.size();i++)
            coveredAmount += dbDetailArrayList.get(i).getAmount();

        return dbOrder.getAmount() - coveredAmount;
    }

    private QPAY_TipDetail[] getOrderDetail(){
        QPAY_TipDetail[] detail = new QPAY_TipDetail[dbDetailArrayList.size()];

        for (int i=0;i<dbDetailArrayList.size();i++)
            detail[i] = dbDetailArrayList.get(i).toTipDetail();

        return detail;
    }

    private void restoreView(){
        check(true,false,0);
        monto.setNewText("0.00");
        edit_tip_percent.setNewText("");
        commensal_name.setText("Persona");
        txt_commensal.setText("1");
        btn_continuar.setEnabled(false);
    }

    private void checkOrder()
    {
        if(coveredAmount >= dbOrder.getAmount()){
            //El monto de la comanda fue cubierto
            restoreView();
            saveTipDetail(new IFunction() {
                @Override
                public void execute(Object[] xdata) {
                    dataHelper.updateDetails(dbOrder.getOrder_id());

                    context.alert(R.string.restaurante_11, new IAlertButton() {
                        @Override
                        public String onText() {
                            return "ACEPTAR";
                        }

                        @Override
                        public void onClick() {

                            context.setFragment(Fragment_restaurante_3.newInstance(order, xdata[0]));

                        }
                    });

                }
            });
        }
    }

    /*PETICIÓN AL SERVER*/
    private void saveTipDetail(final IFunction function)
    {
        order = new QPAY_TipOrder();
        order.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        order.setSell_number(""+dbOrder.getOrder_id());
        order.setCreation_date(dbOrder.getDate());
        order.setTotal(dbOrder.getAmount());
        order.setTip_detail(getOrderDetail());
        //20210115 RSB. Improvements 0121. Ticket restaurante
        order.setPeople_amount(""+dbOrder.getCommensal_number());

        context.loading(true);

        try {

            IRestaurantOperative sale = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    Logger.i(new Gson().toJson(result));

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);
                    } else {
                        //Respuesta correcta
                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_SaveTipResponse response = gson.fromJson(json, QPAY_SaveTipResponse.class);

                        if (response.getQpay_response().equals("true")) {

                            if(function != null)
                                function.execute(response);

                        } else  {
                            getContext().loading(false);
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    context.loading(false);
                    context.alert(R.string.general_error);

                }
            }, context);

            sale.saveTipDetail(order);

        } catch (Exception e) {

            context.loading(false);
            e.printStackTrace();
            context.alert(R.string.general_error_catch);

        }
    }

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }
}

