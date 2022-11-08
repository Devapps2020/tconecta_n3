package com.blm.qiubopay.modules;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.listeners.IMitDebugTxrListener;
import com.blm.qiubopay.models.QPAY_UserProfile;
import com.blm.qiubopay.models.mitec.QPAY_MitErrorFinancialTxrRequest;
import com.blm.qiubopay.models.transactions.TransactionsModel;
import com.blm.qiubopay.modules.fiado.Fragment_fiado_7;
import com.blm.qiubopay.modules.restaurante.Fragment_restaurante_1;
import com.blm.qiubopay.modules.restaurante.Fragment_restaurante_2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nexgo.common.ByteUtils;
import com.nexgo.oaf.apiv3.DeviceEngine;
import com.nexgo.oaf.apiv3.DeviceInfo;
import com.nexgo.oaf.apiv3.SdkResult;
import com.nexgo.oaf.apiv3.device.pinpad.OnPinPadInputListener;
import com.nexgo.oaf.apiv3.device.pinpad.PinPadKeyCode;
import com.nexgo.oaf.apiv3.device.reader.CardInfoEntity;
import com.nexgo.oaf.apiv3.device.reader.CardSlotTypeEnum;
import com.nexgo.oaf.apiv3.device.reader.OnCardInfoListener;
import com.nexgo.oaf.apiv3.emv.AidEntity;
import com.nexgo.oaf.apiv3.emv.CandidateAppInfoEntity;
import com.nexgo.oaf.apiv3.emv.CapkEntity;
import com.nexgo.oaf.apiv3.emv.EmvChannelTypeEnum;
import com.nexgo.oaf.apiv3.emv.EmvDataSourceEnum;
import com.nexgo.oaf.apiv3.emv.EmvEntryModeEnum;
import com.nexgo.oaf.apiv3.emv.EmvHandler2;
import com.nexgo.oaf.apiv3.emv.EmvOnlineResultEntity;
import com.nexgo.oaf.apiv3.emv.EmvProcessFlowEnum;
import com.nexgo.oaf.apiv3.emv.EmvProcessResultEntity;
import com.nexgo.oaf.apiv3.emv.EmvTransConfigurationEntity;
import com.nexgo.oaf.apiv3.emv.EmvTransDataEntity;
import com.nexgo.oaf.apiv3.emv.OnEmvProcessListener;
import com.nexgo.oaf.apiv3.emv.OnEmvProcessListener2;
import com.nexgo.oaf.apiv3.emv.PromptEnum;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.database.operative.RestaurantDataHelper;
import com.blm.qiubopay.database.transactions.DataHelper;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.helpers.HEditText;
import com.blm.qiubopay.helpers.HFragment;
import com.blm.qiubopay.listeners.ISignatureUpload;
import com.blm.qiubopay.listeners.IVisa;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_CashBackBin;
import com.blm.qiubopay.models.QPAY_Sigature;
import com.blm.qiubopay.models.QPAY_VisaResponse;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.mitec.CustomMitecTransaction;
import com.blm.qiubopay.models.n3.EmvTlvResponse;
import com.blm.qiubopay.models.operative.OperativeType;
import com.blm.qiubopay.models.operative.PayType;
import com.blm.qiubopay.models.operative.restaurant.DbDetail;
import com.blm.qiubopay.models.visa.CustomCipher;
import com.blm.qiubopay.models.visa.request.QPAY_VisaEmvRequest;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.tools.tlv.TLV;
import com.blm.qiubopay.utils.EmvUtils;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.RC4Helper;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import integration.praga.mit.com.apiintegration.ApiIntegrationService;
import integration.praga.mit.com.apiintegration.model.Checkmsi;
import integration.praga.mit.com.apiintegration.model.CheckmsiResponse;
import integration.praga.mit.com.apiintegration.model.Location;
import integration.praga.mit.com.apiintegration.model.TransactionEntity;
import integration.praga.mit.com.apiintegration.model.TransactionResponse;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_pago_financiero_N3
        extends HFragment
        implements OnCardInfoListener, OnEmvProcessListener2, OnPinPadInputListener {//N3_FLAG_COMMENT

    private View view;
    private MenuActivity context;
    private boolean isCashbackTransaction;

    private CViewEditText monto;
    //private Event.ConnectType mConnectType = null;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean isFallbackTxn = false;
    private int fallbackCounter = 0;
    private boolean isMagneticStripeCard = false;
    private Map<String, TLV> tlvList;
    private Map<String, TLV> emvTlvList;
    private float cashbackAmount;
    private float fee;
    private float cashbackTotalAmount;
    private String pinFlag;
    private QPAY_CashBackBin cashBackBin;
    private ArrayList<QPAY_CashBackBin> arrayCashbackBin;
    private boolean isPinAuthentication;
    private String TAG_9F03;

    //Objetos lector N3
    //N3_FLAG_COMMENT

    private DeviceEngine deviceEngine;
    private CardInfoEntity cardInfoN3;
    //private com.nexgo.oaf.apiv3.emv.EmvHandler emvHandler;
    private EmvHandler2 emvHandler2;
    private EmvUtils emvUtils;
    private com.nexgo.oaf.apiv3.device.reader.CardReader cardReader;// = deviceEngine.getCardReader();
    private CardSlotTypeEnum mExistSlot;
    private boolean firstTimePIN;
    private boolean iccReaderError;

    //N3_FLAG_COMMENT

    private String cardNo;
    private String pwdText;
    private TextView pwdTv;
    private AlertDialog pwdAlertDialog;
    private View dv;
    private EmvTlvResponse emvTlvResponse;
    private String[] tagsNameList = {"9F02", "5F24", "9A", "4F", "9F34", "9F03", "9F06", "9F21", "5A", "9F10", "82", "8E", "5F25", "9F07", "9F0D", "9F0E", "9F0F", "9F26", "9F27", "9F36", "9C", "9F33", "9F37", "9F39", "9F40", "95", "9B", "84", "5F2A", "5F34", "9F09", "9F1A", "9F1E", "9F35", "5F20", "5F30", "5F28", "50", "9F08", "9F01", "9F15"};
    //,"9F41"

    Gson gson_txr;

    private boolean lockedtrx = false;

    //Operativa restaurante
    private OperativeType operativeType;
    private DbDetail dbDetail;
    private RestaurantDataHelper restaurantDataHelper;

    //Mitec
    private CustomMitecTransaction customMitecTransaction;

    //Cashback
    private boolean isAbortProcessByCashback = false;
    private float defaultFee = (float) 12.0;

    Button btn_confirmar;

    public Fragment_pago_financiero_N3() {
    }

    //public static Fragment_pago_financiero_N3 newInstance(boolean pago) {
    public static Fragment_pago_financiero_N3 newInstance(boolean pago, DbDetail detail) {
        Fragment_pago_financiero_N3 fragment = new Fragment_pago_financiero_N3();
        Bundle args = new Bundle();
        Gson gson = new Gson();

        args.putBoolean("Fragment_pago_tarjeta_1", pago);
        args.putString("Fragment_pago_tarjeta_1_2", detail!=null ? gson.toJson(detail) : "");

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
        Gson gson = new Gson();

        if (getArguments() != null) {
            isCashbackTransaction = getArguments().getBoolean("Fragment_pago_tarjeta_1");
            //operativeType = (OperativeType) getArguments().getSerializable("Fragment_pago_tarjeta_1_2");
            dbDetail = (DbDetail) gson.fromJson(getArguments().getString("Fragment_pago_tarjeta_1_2"), DbDetail.class);
        }
        Log.d("","");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        if(view != null)
            return view;

        if(!isCashbackTransaction)
            view = inflater.inflate(R.layout.fragment_pago_financiero_1, container, false);
        else
            view = inflater.inflate(R.layout.fragment_cashback_1, container, false);

        setView(view);

        initFragment();

        return view;
    }

    public void initFragment(){

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContext().backFragment();
            }
        });

        cashbackAmount = 0;
        fee = 0;
        cashbackTotalAmount = 0;
        firstTimePIN = true;

        gson_txr = new Gson();

        btn_confirmar = view.findViewById(R.id.btn_confirmar);
        final Button btn_venta_propina = view.findViewById(R.id.btn_venta_propina);

        monto = CViewEditText.create(view.findViewById(R.id.edit_importe))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(11)
                .setType(CViewEditText.TYPE.CURRENCY)
                .setHint(R.string.text_depositos_21)
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {

                        if(monto.isValid()) {
                            btn_confirmar.setEnabled(true);
                            btn_venta_propina.setEnabled(false);
                        }
                        else {
                            btn_confirmar.setEnabled(false);
                            btn_venta_propina.setEnabled(true);
                        }

                    }});

        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isCashbackTransaction){
                    cashbackAmount = Float.parseFloat(monto.getText().replace("$", "").replace(",",""));
                    if (cashbackAmount < 100) {
                        context.alert(R.string.cashback_min_amount_error);
                        return;
                    }
                }

                isFallbackTxn = false;
                fallbackCounter = 0;
                //startCardReader();
                //initValues();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        context.loadingConectando("Inserte o deslice la tarjeta...", new IAlertButton() {
                            @Override
                            public String onText() {
                                return "CANCELAR";
                            }

                            @Override
                            public void onClick() {
                                //emvHandler.emvProcessCancel();
                                emvHandler2.emvProcessCancel();
                            }
                        });

                        startCardReader();

                    }
                });
            }
        });

        btn_venta_propina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                restaurantDataHelper = new RestaurantDataHelper(getContext());

                //context.clearFragment();

                if(restaurantDataHelper.isAnOpenOrder())
                    context.setFragment(Fragment_restaurante_2.newInstance());
                else
                    context.setFragment(Fragment_restaurante_1.newInstance());

            }
        });

        chargeCashBackBines();

        //N3_FLAG_COMMENT

        deviceEngine = ((CApplication) getActivity().getApplication()).deviceEngine;
        cardReader = deviceEngine.getCardReader();
        //Funciones deprecadas N3
        /*emvHandler = deviceEngine.getEmvHandler("app1");
        emvHandler.clearLog();*/
        emvHandler2 = deviceEngine.getEmvHandler2("app2");
        emvHandler2.clearLog();
        emvUtils = new EmvUtils(getContext());

        //N3_FLAG_COMMENT

        dv = getLayoutInflater().inflate(R.layout.dialog_inputpin_layout, null);
        pwdTv = (TextView) dv.findViewById(R.id.input_pin);
        pwdAlertDialog = new AlertDialog.Builder(getContext()).setView(dv).create();
        pwdAlertDialog.setCanceledOnTouchOutside(false);

        context.isConnected = true;

        //Se checa si es un pago con propina
        if(dbDetail != null){
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
            otherSymbols.setDecimalSeparator('.');
            DecimalFormat decimalFormat = new DecimalFormat("#.00",otherSymbols);
            monto.setText(Utils.paserCurrency(decimalFormat.format(dbDetail.getTotal())));
            monto.setEnabled(false);
            //if(monto.isValid())
            btn_confirmar.setEnabled(true);
        }else{
            if(!isCashbackTransaction) {
                //Checa si el usuario es de tipo restaurante.
                String operativeType = AppPreferences.getUserProfile().getQpay_object()[0].getOperative_type();
                operativeType = (operativeType!=null && !operativeType.isEmpty() ? operativeType : Globals.OPERATIVE_TYPE_RESTAURANT);


                if (operativeType.equals("1")
                        || operativeType.equals(Globals.OPERATIVE_TYPE_RESTAURANT)) {
                    //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.button_width), (int) getResources().getDimension(R.dimen.button_height));
                    //layoutParams.setMargins(0, 0, 0, 0);

                    //btn_confirmar.setLayoutParams(layoutParams);
                    btn_venta_propina.setVisibility(View.VISIBLE);
                    btn_venta_propina.setEnabled(true);
                }
            }
        }

        if(context.pagoTarjeta != null) {
            monto.setTextMonto(Fragment_fiado_7.monto);
            monto.setEnabled(false);
        }

    }

    private void startCardReader(){
        //Log.d("EXTRA_TAG", getExtraTags());
        //CustomCipher.getOriginalValue("084f4b5767937bff3f110e195be4deb58cad35897af69210656da3");
        //CustomCipher.getOriginalValue("08f7cb2f4f8b831767110efca9da193c03d823bda1ee0a740b8812");
        //CustomCipher.getOriginalValue("085767937bff9bb367110e5d2e2c57b77f648690f2be734528eae8");
        startN3CardReader();
    }

    private void startCashBackProcess() {
        if(isAbortProcessByCashback)
            startSendMitecTransaction();
        else {
            if (cardNo != null)
                cashBackBin = findCashbackBin(cardNo.trim().substring(0, 6));

            if (cashBackBin != null) {
                cashbackAmount = Float.parseFloat(getImporte());
                fee = Float.parseFloat(cashBackBin.getFee());
                cashbackTotalAmount = cashbackAmount + fee;

                if (cashbackAmount > Float.parseFloat(cashBackBin.getMaxAmount())) {
                    String msg = "El monto máximo de retiro para esta tarjeta es de $" + cashBackBin.getMaxAmount() + " pesos.";
                    context.alert(msg);
                    return;
                }

                String msg = "Se realizará un cargo total por " + Utils.paserCurrency("" + cashbackTotalAmount) + context.getResources().getString(R.string.desea_continuar);
                context.alert(msg, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "SI";
                    }

                    @Override
                    public void onClick() {
                        /*if(defaultFee != Float.parseFloat(cashBackBin.getFee()))
                            abortCashbackProcess();
                        else*/
                            startSendMitecTransaction();
                    }
                }, new IAlertButton() {
                    @Override
                    public String onText() {
                        return "NO";
                    }

                    @Override
                    public void onClick() {

                    }
                });

                //startEmvParser(tlv);
            } else {
                context.alert(R.string.cashback_not_found_bin);
            }
        }
    }

    private String getCashbackAmount() {
        String amount = "";

        if(cardNo != null)
            cashBackBin = findCashbackBin(cardNo.trim().substring(0, 6));

        if(cashBackBin != null)
        {
            cashbackAmount = Float.parseFloat(getImporte());
            fee = Float.parseFloat(cashBackBin.getFee());
            cashbackTotalAmount = cashbackAmount + fee;

            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
            otherSymbols.setDecimalSeparator('.');
            DecimalFormat decimalFormat = new DecimalFormat("#.00",otherSymbols);

            amount = String.format("%012d", getAmount(Utils.paserCurrency(decimalFormat.format(cashbackTotalAmount))));


        } else {
            context.alert(R.string.cashback_not_found_bin);
            amount = String.format("%012d", getAmount());//0;
        }

        return amount;
    }

    private void abortCashbackProcess(){
        isAbortProcessByCashback = true;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                emvHandler2.emvProcessCancel();
                startCardReader();
            }
        });

        //
    }

    private String getExtraTags(){
        //return "9F4103" + AppPreferences.getCounter();//"9F4103000000";
        return "9F4103000000";
    }

    public void startSendMitecTransaction() {

        context.loading(true);

        checkMsi(cardNo);

    }

    //public void startSendMitecTransactionFinish() {
    public void finishTransaction(final String paymentType){

        context.loading(true);

        try {
            DeviceInfo deviceInfo = deviceEngine.getDeviceInfo();
            final TransactionEntity tx = new TransactionEntity();

            final EmvOnlineResultEntity emvOnlineResult = new EmvOnlineResultEntity();

            //tx.setBusinessId("8");//"219"); // tu código de comercio, lo obtendrás al momento de iniciar sesión

            tx.setCurrency("MXN"); // moneda de la transacción, MXN

            tx.setKeyId("1"); // identificador de la llave, en este caso 1
            tx.setEncriptionMode("2"); // tipo de encripción, forzosamente 2 = 3DES
            tx.setLocation(new Location()); // ubicación del dispositivo, lat-long
            tx.setSerialNumber(deviceInfo.getSn()); // serial del lector

            if(Tools.isN3Terminal()){
                /*************************************************/
                /*N3*/
                /*************************************************/

                //N3_FLAG_COMMENT
                if(isMagneticStripeCard || isFallbackTxn) {
                    ////N3_FLAG_COMMENT
                    tx.setPosEntryMode("022"); // 051 = EMV, 071 = CTLS, 022=SWIPE MS
                    tx.setTracks(CustomCipher.getValue(cardInfoN3.getTk2().trim().toUpperCase(),false)); // tracks cifrados separados por el caracter ";", track1;track2;track3
                    tx.setCardHolder(Tools.getCardHolderName(cardInfoN3.getTk1())); // el nombre del tarjetahabiente, cifrado

                    if(cardInfoN3.getCardNo().trim().startsWith("3"))
                        tx.setType("AMEX");
                    else
                        tx.setType("V/MC");

                }else{
                    //N3_FLAG_COMMENT
                    //tx.setCardHolder(Tools.hexStringToString(emvTlvResponse.getTAG_5F20()).trim());// el nombre del tarjetahabiente
                    tx.setCardHolder(Tools.hexStringToString(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x5f, (byte) 0x20}, EmvDataSourceEnum.FROM_KERNEL))));// ll nombre del tarjetahabiente
                    if(tx.getCardHolder().trim().equals(""))
                        tx.setCardHolder("CLIENTE"); // el nombre del tarjetahabiente, cifrado

                    tx.setPosEntryMode("051"); // 051 = EMV, 071 = CTLS, 022=SWIPE MS

                    //String aaa = emvHandler.getTlvByTags(tagsNameList);
                    //tx.setTagSequence(emvHandler.getTlvByTags(tagsNameList));//emvTlvResponse.getCompleteTlv()); // cadena de tags cifrada en formato TLV
                    Log.d("*** N3 READER ***","TLV: " + emvHandler2.getTlvByTags(tagsNameList));
                    tx.setTagSequence(emvHandler2.getTlvByTags(tagsNameList) + getExtraTags()); // cadena de tags cifrada en formato TLV

                    //Se checa el valor del tag 8E
                    /*String TAG_8E = ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x8e}, EmvDataSourceEnum.FROM_KERNEL)).trim().toUpperCase();
                    Log.d("*** N3 READER ***", "8E: " + TAG_8E);
                    if(TAG_8E.endsWith("00")){
                        Log.d("*** N3 READER ***", "Entra al reemplazo");
                        String TAG_8E_2 = Tools.getTagWithOutRPadding("8E", TAG_8E, false);
                        Log.d("*** N3 READER ***", "NUEVO 8E: " + TAG_8E_2);

                        String TAG_8E_NEW_FORMAT = Tools.getTagWithFormat("8E", TAG_8E);
                        String TAG_8E_2_NEW_FORMAT = Tools.getTagWithFormat("8E", TAG_8E_2);//Tools.getTagWithOutRPadding("8E", TAG_8E_2, true);

                        Log.d("*** N3 READER ***", "NUEVO 8E CON FORMATO: " + TAG_8E_2_NEW_FORMAT);

                        tx.setTagSequence(emvHandler.getTlvByTags(tagsNameList2) + TAG_8E_2_NEW_FORMAT + getExtraTags());
                        //if(TAG_8E.equals(TAG_8E_2))
                            //tx.setTagSequence(tx.getTagSequence().replace(TAG_8E_NEW_FORMAT, TAG_8E_2_NEW_FORMAT));

                        Log.d("*** N3 READER ***", "NUEVO TAG LIST" + tx.getTagSequence());
                    }*/

                    tx.setTracks(CustomCipher.getValue(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x57}, EmvDataSourceEnum.FROM_KERNEL)).trim().toUpperCase(),false)); // tracks cifrados separados por el caracter ";", track1;track2;track3

                    if(cardNo.trim().startsWith("3"))
                        tx.setType("AMEX");
                    else
                        tx.setType("V/MC");

                    //Bancdera del PIN
                    String TAG_9F34 = ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9f, (byte) 0x34}, EmvDataSourceEnum.FROM_KERNEL)).trim();//emvTlvList.get("9f34").getValue().trim();
                    pinFlag = TAG_9F34.substring(TAG_9F34.length()-2);

                    if (pinFlag.equals("00")) {
                        tx.setPinOfflineValidation(false);
                        isPinAuthentication = false;
                        pinFlag = "0";
                    } else if (pinFlag.equals("02")) {
                        tx.setPinOfflineValidation(true);
                        isPinAuthentication = true;
                        pinFlag = "1";
                    }
                }
                if(Tools.getModel().equals("N86"))
                    tx.setTerminalModel("NEXGO N86"); // modelo del dispositivo lector
                else
                    tx.setTerminalModel("NEXGO N3"); // modelo del dispositivo lector

                tx.setVersionTerminal("1.0.0"); // versión del firmware del dispositivo lector

                //N3_FLAG_COMMENT
            }
            //Pago de contado o a meses sin intereses.
            tx.setPaymentType(paymentType);

            tx.setVersion(Tools.getVersion()); // versión de la aplicación de cobro

            //tx.setType("SALE"); // tipo de operación, en este caso VENTA
            tx.setUser("T-CONECTA"); // usuario que ejecuta la trasacción, p. ej. CAJERO 1

            tx.setReference(Tools.getReference()); // referencia de pago

            if(isCashbackTransaction)//cashbackTotalAmount
                tx.setAmount((double)cashbackTotalAmount); // importe de la transacción
            else
                tx.setAmount(new Double(getAmountWithoutFormat())); // importe de la transacción



            //Gson gson = new Gson();


            if (isCashbackTransaction) {
                /****************************************************************************/
                /*                              CASHBACK                                    */
                /****************************************************************************/
                ApiIntegrationService.init(getContext(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data1(), Globals.BASE_URL, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data2(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data3());
                //ApiIntegrationService.init(getContext(), Globals.BUSINESS_ID, Globals.BASE_URL, Globals.API_KEY, Globals.ENCRIPTION_KEY);

                //TODO TRANSACTION COUNTER. FINANCIAL TRANSACTION SUM TOTAL
                TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
                transactionsModel.getFinancialTransactions().setTotales(transactionsModel.getFinancialTransactions().getTotales() + 1);
                AppPreferences.setTodayTransactions(transactionsModel);
                ApiIntegrationService.getInstance().cashback(tx, new ApiIntegrationService.IPragaIntegrationListener<TransactionResponse>() {
                    @Override
                    public void onSuccess(TransactionResponse transactionResponse) {

                        //TODO TRANSACTION COUNTER. FINANCIAL TRANSACTION SUM SUCCESSFUL
                        TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
                        transactionsModel.getFinancialTransactions().setExitosos(transactionsModel.getFinancialTransactions().getExitosos() + 1);
                        AppPreferences.setTodayTransactions(transactionsModel);

                        context.loading(false);

                        isAbortProcessByCashback = false;

                        customMitecTransaction = new CustomMitecTransaction();
                        customMitecTransaction.setPaymentType(paymentType);
                        customMitecTransaction.setTransactionResponse(transactionResponse);
                        customMitecTransaction.setQpay_response(monto.getText().toString());
                        customMitecTransaction.setInitial_amount(monto.getText());
                        customMitecTransaction.setQpay_description("Retiro de efectivo");
                        customMitecTransaction.setComision(Utils.paserCurrency(cashBackBin.getFee()));
                        customMitecTransaction.setTotal(Utils.paserCurrency("" + cashbackTotalAmount ));
                        customMitecTransaction.setUsePIN(isPinAuthentication);
                        customMitecTransaction.setComesFromATippedSale(false);
                        customMitecTransaction.setPosEntryMode(tx.getPosEntryMode());

                        //220211 RSB. Transacciones autorizadas sin informacion
                        boolean isApprovedTx = false;
                        if(transactionResponse.isApproved()
                                && transactionResponse.getCcType()!=null && !transactionResponse.getCcType().isEmpty()
                                && transactionResponse.getOperationType()!=null && !transactionResponse.getOperationType().isEmpty()) {
                            if(tx.getPosEntryMode().equals("022")) {
                                isApprovedTx = true;
                            } else  {
                                if (transactionResponse.getArqc()!=null && !transactionResponse.getArqc().isEmpty())
                                    isApprovedTx = true;
                            }
                        }

                        if (transactionResponse.isApproved() && isApprovedTx) {
                            //getContextMenu().alert("Cobro exitoso" + "\nFolio: " + transactionResponse.getFolio());

                            //20200713 RSB. Valida si debe persistir tras ingresar a reporte de transacciones
                            if(AppPreferences.getLocalTxnFinancial()) {
                                try {
                                    DataHelper dataHelper = new DataHelper(getContext());
                                    dataHelper.insertLocalTransaction(DataHelper.DB_TXR_TYPE.FINANCIAL_TXR, Tools.getTodayDate(), "CASHBACK", gson_txr.toJson(customMitecTransaction), null);
                                } catch (Exception e) {
                                    Log.e("DATA HELPER", "CASHBACK");
                                }
                            }

                            if(!isMagneticStripeCard && !isFallbackTxn) {
                                emvOnlineResult.setAuthCode(transactionResponse.getAuth());
                                emvHandler2.onSetOnlineProcResponse(SdkResult.Success, emvOnlineResult);
                            }
                            //context.showAlert("Cobro exitoso" + "\nFolio: " + transactionResponse.getFolio());
                            if(isPinAuthentication){

                                customMitecTransaction.setUsePIN(true);
                                context.cargarSaldo(true,false,true,new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {
                                        context.setFragment(Fragment_pago_financiero_2.newInstance(isCashbackTransaction, customMitecTransaction), true);
                                    }
                                });
                            } else {

                                customMitecTransaction.setUsePIN(false);
                                /*context.cargarSaldo(true,true,true,new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {
                                        context.setFragment(Fragment_pago_financiero_2.newInstance(isCashbackTransaction, customMitecTransaction), true);
                                    }
                                });*/

                                final TransactionResponse txrResponse = transactionResponse;

                                context.setImage = new IFunction<Bitmap>() {
                                    @Override
                                    public void execute(Bitmap... data) {
                                        context.backFragment();
                                        enviarFirma(data[0], null, txrResponse);

                                    }
                                };

                                context.setFragment(Fragment_registro_financiero_5.newInstance());

                            }
                        }
                        else
                        {
                            if(!isMagneticStripeCard && !isFallbackTxn) {
                                emvOnlineResult.setRejCode("00");
                                emvHandler2.onSetOnlineProcResponse(SdkResult.Emv_Declined, emvOnlineResult);
                            }

                            String errorCode = transactionResponse.getErrorCode()!=null
                                    && !transactionResponse.getErrorCode().isEmpty() ? transactionResponse.getErrorCode() : "";
                            String errorDesc = transactionResponse.getErrorDescription()!=null
                                    && !transactionResponse.getErrorDescription().isEmpty() ? transactionResponse.getErrorDescription() : "";

                            context.alert("Cobro Denegado" + "\nCódigo de error: " + errorCode
                                    + "\nDescripción: " + errorDesc);

                            //Call runnable for approved transactions without info
                            if (transactionResponse.isApproved() && !isApprovedTx){
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        sendErrorTxrInfo(new Gson().toJson(customMitecTransaction));
                                    }
                                };
                                runnable.run();
                            }

                            //context.showAlert(transactionResponse.getErrorDescription());
                        }
                    }


                    @Override
                    public void onError(String s) {

                        //TODO TRANSACTION COUNTER. FINANCIAL TRANSACTION SUM UNSUCCESSFUL
                        TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
                        transactionsModel.getFinancialTransactions().setNoExitosos(transactionsModel.getFinancialTransactions().getNoExitosos() + 1);
                        AppPreferences.setTodayTransactions(transactionsModel);

                        isAbortProcessByCashback = false;

                        if(!isMagneticStripeCard && !isFallbackTxn) {
                            emvOnlineResult.setRejCode("00");
                            emvHandler2.onSetOnlineProcResponse(SdkResult.Emv_Declined, emvOnlineResult);
                        }
                        context.loading(false);
                        context.alert(R.string.general_error_transaction);
                        btn_confirmar.setEnabled(false);
                    }

                });

            }
            else
            {
                /****************************************************************************/
                /*                              VENTA TDC                                   */
                /****************************************************************************/
                Log.d("*** N3 READER ***","PETICION MIT: " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data3());
                ApiIntegrationService.init(getContext(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data1(), Globals.BASE_URL, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data2(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data3());
                //ApiIntegrationService.init(getContext(), Globals.BUSINESS_ID, Globals.BASE_URL, Globals.API_KEY, Globals.ENCRIPTION_KEY);

                Gson gson = new Gson();
                TransactionEntity tx2 = tx;
                //tx2.setTagSequence("");
                final String strJson = gson.toJson(tx2, TransactionEntity.class);
                Log.d("*** N3 READER ***","PETICION MIT: " + strJson);

                //TODO TRANSACTION COUNTER. FINANCIAL TRANSACTION SUM TOTAL
                TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
                transactionsModel.getFinancialTransactions().setTotales(transactionsModel.getFinancialTransactions().getTotales() + 1);
                AppPreferences.setTodayTransactions(transactionsModel);
                ApiIntegrationService.getInstance().saleWithDongleUsing3DS(tx, new ApiIntegrationService.IPragaIntegrationListener<TransactionResponse>() {
                    @Override
                    public void onSuccess(TransactionResponse transactionResponse){

                        //TODO TRANSACTION COUNTER. FINANCIAL TRANSACTION SUM SUCCESSFUL
                        TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
                        transactionsModel.getFinancialTransactions().setExitosos(transactionsModel.getFinancialTransactions().getExitosos() + 1);
                        AppPreferences.setTodayTransactions(transactionsModel);

                        context.loading(false);

                        customMitecTransaction = new CustomMitecTransaction();
                        customMitecTransaction.setPaymentType(paymentType);
                        customMitecTransaction.setTransactionResponse(transactionResponse);
                        customMitecTransaction.setQpay_response(monto.getText());
                        customMitecTransaction.setInitial_amount(monto.getText());
                        customMitecTransaction.setQpay_description("Pago con Tarjeta");
                        customMitecTransaction.setUsePIN(isPinAuthentication);
                        customMitecTransaction.setComesFromATippedSale(false);
                        customMitecTransaction.setPosEntryMode(tx.getPosEntryMode());

                        //EmvOnlineResultEntity emvOnlineResult = new EmvOnlineResultEntity();
                        //emvOnlineResult.setRecvField55(null); //fill with the server response 55 field EMV data to do second auth

                        //220211 RSB. Transacciones autorizadas sin informacion
                        boolean isApprovedTx = false;
                        if(transactionResponse.isApproved()
                                && transactionResponse.getCcType()!=null && !transactionResponse.getCcType().isEmpty()
                                && transactionResponse.getOperationType()!=null && !transactionResponse.getOperationType().isEmpty()) {
                            if(tx.getPosEntryMode().equals("022")) {
                                isApprovedTx = true;
                            } else  {
                                if (transactionResponse.getArqc()!=null && !transactionResponse.getArqc().isEmpty())
                                    isApprovedTx = true;
                            }
                        }

                        Gson gson = new Gson();
                        String strJson = gson.toJson(transactionResponse, TransactionResponse.class);
                        Log.d("*** N3 READER ***","RESPUESTA MIT: " + strJson);

                        if (transactionResponse.isApproved() && isApprovedTx) {

                            //Se checa si es una operativa restaurante.
                            if(dbDetail != null){
                                customMitecTransaction.setComesFromATippedSale(true);
                                restaurantDataHelper = new RestaurantDataHelper(context);
                                restaurantDataHelper.insertOrderDetail(dbDetail.getFk_order()
                                        ,dbDetail.getCommensals_no()
                                        ,dbDetail.getTip_percent()
                                        ,dbDetail.getAmount()
                                        ,dbDetail.getTip_amount()
                                        ,dbDetail.getTotal()
                                        , PayType.CREDIT
                                        ,transactionResponse.getFolio()
                                        ,transactionResponse.getAuth());
                            }

                            //20200713 RSB. Valida si debe persistir tras ingresar a reporte de transacciones
                            if(AppPreferences.getLocalTxnFinancial()) {
                                try {
                                    DataHelper dataHelper = new DataHelper(getContext());
                                    dataHelper.insertLocalTransaction(DataHelper.DB_TXR_TYPE.FINANCIAL_TXR, Tools.getTodayDate(), "SELL", gson_txr.toJson(customMitecTransaction), null);
                                } catch (Exception e) {
                                    Log.e("DATA HELPER", "SELL");
                                }
                            }

                            if(!isMagneticStripeCard && !isFallbackTxn) {
                                if(Globals.EMV_FULL) {
                                    String message = "IccCsn: "         + transactionResponse.getIccCsn();
                                    message += "\niccAtc: "             + transactionResponse.getIccAtc();
                                    message += "\niccArpc: "            + transactionResponse.getIccArpc();
                                    message += "\niccIssuerScript: "    + transactionResponse.getIccIssuerScript();

                                    Log.d("EMV_RESPONSE",message);

                                    emvOnlineResult.setRecvField55(transactionResponse.getIccIssuerScript().getBytes());
                                }

                                emvOnlineResult.setAuthCode(transactionResponse.getAuth());
                                emvHandler2.onSetOnlineProcResponse(SdkResult.Success, emvOnlineResult);
                            }
                            //context.showAlert("Cobro exitoso" + "\nFolio: " + transactionResponse.getFolio());
                            if(isPinAuthentication){

                                customMitecTransaction.setUsePIN(true);
                                context.cargarSaldo(true,false,true,new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {
                                        context.setFragment(Fragment_pago_financiero_2.newInstance(isCashbackTransaction, customMitecTransaction), true);
                                    }
                                });

                            } else {

                                customMitecTransaction.setUsePIN(false);
                                /*context.cargarSaldo(true,true,true,new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {
                                        context.setFragment(Fragment_pago_financiero_2.newInstance(isCashbackTransaction, customMitecTransaction), true);
                                    }
                                });*/

                                final TransactionResponse txrResponse = transactionResponse;

                                context.setImage = new IFunction<Bitmap>() {
                                    @Override
                                    public void execute(Bitmap... data) {
                                        context.backFragment();
                                        enviarFirma(data[0], null, txrResponse);

                                    }
                                };

                                context.setFragment(Fragment_registro_financiero_5.newInstance());

                            }
                        }
                        else {
                            if(!isMagneticStripeCard && !isFallbackTxn) {
                                emvOnlineResult.setRejCode("00");
                                emvHandler2.onSetOnlineProcResponse(SdkResult.Emv_Declined, emvOnlineResult);
                            }

                            String errorCode = transactionResponse.getErrorCode()!=null
                                    && !transactionResponse.getErrorCode().isEmpty() ? transactionResponse.getErrorCode() : "";
                            String errorDesc = transactionResponse.getErrorDescription()!=null
                                    && !transactionResponse.getErrorDescription().isEmpty() ? transactionResponse.getErrorDescription() : "";

                            context.alert("Cobro Denegado" + "\nCódigo de error: " + errorCode
                                    + "\nDescripción: " + errorDesc);

                            //Call runnable for approved transactions without info
                            if (transactionResponse.isApproved() && !isApprovedTx){
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        sendErrorTxrInfo(new Gson().toJson(customMitecTransaction));
                                    }
                                };
                                runnable.run();
                            }

                            //context.alert(transactionResponse.getErrorDescription());
                        }
                    }

                    @Override
                    public void onError(String s) {

                        //TODO TRANSACTION COUNTER. FINANCIAL TRANSACTION SUM UNSUCCESSFUL
                        TransactionsModel transactionsModel = AppPreferences.getTodayTransactions();
                        transactionsModel.getFinancialTransactions().setNoExitosos(transactionsModel.getFinancialTransactions().getNoExitosos() + 1);
                        AppPreferences.setTodayTransactions(transactionsModel);

                        //context.showAlert("Petición de cobro: \n"  + strJson);
                        if(!isMagneticStripeCard && !isFallbackTxn) {
                            emvOnlineResult.setRejCode("00");
                            emvHandler2.onSetOnlineProcResponse(SdkResult.Emv_Declined, emvOnlineResult);
                        }
                        context.loading(false);
                        context.alert(R.string.general_error_transaction);
                        btn_confirmar.setEnabled(false);
                    }
                });
            }
            //}
        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);
            context.alert(R.string.general_error_catch);

        }
    }

    private void checkMsi(String bin){
        Checkmsi checkmsi = new Checkmsi();

        if (isCashbackTransaction){
            finishTransaction("C");
        }
        else if(null == bin || bin.isEmpty() || bin.length()<6){
            finishTransaction("C");
        }
        else {

            checkmsi.setAmount(new Float(getAmountWithoutFormat()));
            checkmsi.setBinTC(bin.substring(0, 6));
            checkmsi.setBusinessId(Integer.parseInt(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data1()));

            ApiIntegrationService.init(getContext(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data1(), Globals.BASE_URL, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data2(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data3());
            ApiIntegrationService.getInstance().checkmsi(checkmsi, new ApiIntegrationService.IPragaIntegrationListener<CheckmsiResponse>() {
                @Override
                public void onSuccess(CheckmsiResponse checkmsiResponse) {
                    Log.d("MSI", "onSuccess");

                    if (checkmsiResponse.getResultCode().trim().equals("000")) {
                        //Cuenta con promociones
                        context.loading(false);
                        final CheckmsiResponse response = checkmsiResponse;
                        //Se pregunta si se desea aplicar meses sin intereses
                        context.alert("¿Desea diferir esta compra a MESES SIN INTERESES?", new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Si";
                            }

                            @Override
                            public void onClick() {
                                showPromotions(response.getPaymentType());
                            }
                        }, new IAlertButton() {
                            @Override
                            public String onText() {
                                return "No";
                            }

                            @Override
                            public void onClick() {
                                finishTransaction("C");
                            }
                        });

                    } else//No cuenta con promociones
                        finishTransaction("C");
                }

                @Override
                public void onError(String s) {
                    Log.d("MSI", "onError");
                    finishTransaction("C");
                }
            });
        }
    }

    public void showPromotions(String p){
        int k = 0;
        /*for (k = 0; k < appInfoList.size(); k++) {
            Log.d("huacong", "appInfoList " + k + ByteUtils.byteArray2HexString(appInfoList.get(k).getAid()));
            Log.d("huacong", "appInfoList " + k + ByteUtils.byteArray2HexString(appInfoList.get(k).getAppLabel()));
            Log.d("huacong", "appInfoList " + k + appInfoList.get(k).getPriority());
        }*/

        final String[] array = p.replace("C,","").split(",");

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View dv = getLayoutInflater().inflate(R.layout.dialog_app_list, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(dv).create();
                ListView lv = (ListView) dv.findViewById(R.id.aidlistView);
                List<Map<String, String>> listItem = new ArrayList<>();
                for (int i = 0; i < array.length; i++) {
                    Map<String, String> map = new HashMap<>();
                    map.put("appIdx", (i + 1) + "");
                    map.put("appName", array[i].replace("M", " meses sin intereses."));
                    listItem.add(map);
                }
                SimpleAdapter adapter = new SimpleAdapter(getContext(),
                        listItem,
                        R.layout.promotion_list,
                        new String[]{"appIdx", "appName"},
                        new int[]{R.id.tv_appIndex, R.id.tv_appName});
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //emvHandler.onSetSelAppResponse(position + 1);
                        alertDialog.dismiss();
                        alertDialog.cancel();
                        finishTransaction(array[position]);
                    }
                });
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        });
    }

    public void enviarFirma(Bitmap image, final QPAY_VisaResponse response, final TransactionResponse transactionResponse ){

        context.loading(true);

        try {

            QPAY_Sigature signature = new QPAY_Sigature();
            signature.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());

            if(response != null) {
                signature.setQpay_rspId(response.getQpay_object()[0].getCspHeader().getRspId());
                signature.setQpay_image_name_1(response.getQpay_object()[0].getCspHeader().getRspId().trim() + ".png");
            }

            if(transactionResponse != null){
                signature.setQpay_rspId(transactionResponse.getFolio());
                signature.setQpay_image_name_1(transactionResponse.getFolio() + ".png");
            }

            signature.setQpay_image_1(Utils.convert(image));


            ISignatureUpload signatureUpload = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    //Transacción visa
                    if (result instanceof ErrorResponse) {
                        context.alert(R.string.general_error);
                    } else {

                        //Respuesta correcta
                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_BaseResponse.QPAY_BaseResponseExcluder()).create();
                        String json = gson.toJson(result);

                        QPAY_BaseResponse baseResponse = new Gson().fromJson(json, QPAY_BaseResponse.class);

                        if (baseResponse.getQpay_response().equals("true")) {
                            //if(Globals.GATEWAY.equals(Globals.GATEWAY_MIT))
                            context.cargarSaldo(true,false,true,new IFunction() {
                                @Override
                                public void execute(Object[] data) {
                                    context.setFragment(Fragment_pago_financiero_2.newInstance(isCashbackTransaction, customMitecTransaction), true);
                                }
                            });
                            //else
                            //context.setFragment(Fragment_pago_financiero_2.newInstance(isCashbackTransaction, response));
                        } else
                            context.alert(baseResponse.getQpay_description());

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    context.loading(false);

                    context.alert(R.string.general_error);
                }
            }, context);

            signatureUpload.uploadSignature(signature);

        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);

            context.alert(R.string.general_error_catch);
        }

    }

    private String getFormatedTrack2(String data){
        String back = "";
        back = ";" + data.replace("d","=");

        if(data.contains("f"))
            back = back.replace("f","?");
        else
            back += "?";

        RC4Helper rc4 = RC4Helper.getInstance();
        back = rc4.StringToHexString(back);
        return back;
    }

    private QPAY_CashBackBin findCashbackBin(String bin) {
        QPAY_CashBackBin result = null;
        QPAY_CashBackBin temporal = null;
        for(int i = 0; i < arrayCashbackBin.size(); i++)
        {
            temporal = arrayCashbackBin.get(i);
            if(temporal.getBin().trim().equals(bin.trim()))
                result = temporal;
        }
        return result;
    }

    private void chargeCashBackBines(){

        arrayCashbackBin = new ArrayList<QPAY_CashBackBin>();

        arrayCashbackBin.add(new QPAY_CashBackBin("402766","Banco Azteca"    ,"A0000000041010","12.00","100.00","500.00"));//
        arrayCashbackBin.add(new QPAY_CashBackBin("476684","Mundial"         ,"A0000000031010","12.00","100.00","500.00"));//
        arrayCashbackBin.add(new QPAY_CashBackBin("476687","Mundial"         ,"A0000000031010","12.00","100.00","500.00"));//
        arrayCashbackBin.add(new QPAY_CashBackBin("483112","Banco Azteca"    ,"A0000000041010","12.00","100.00","500.00"));//
        arrayCashbackBin.add(new QPAY_CashBackBin("516576","Banco Azteca"    ,"A0000000041010","12.00","100.00","500.00"));//
        arrayCashbackBin.add(new QPAY_CashBackBin("520416","Perfiles"        ,"A0000000041010","12.00","100.00","500.00"));//
        arrayCashbackBin.add(new QPAY_CashBackBin("525678","Perfil Ejecutivo","A0000000041010","12.00","100.00","500.00"));//
        arrayCashbackBin.add(new QPAY_CashBackBin("526354","Banco Azteca"    ,"A0000000041010","12.00","100.00","500.00"));//
        arrayCashbackBin.add(new QPAY_CashBackBin("534381","Banco Azteca"    ,"A0000000041010","12.00","100.00","500.00"));//
        arrayCashbackBin.add(new QPAY_CashBackBin("551238","Banco Azteca"    ,"A0000000041010","12.00","100.00","500.00"));//
        arrayCashbackBin.add(new QPAY_CashBackBin("551507","Bansefi"         ,"A0000000041010","10.80","100.00","500.00"));//
        arrayCashbackBin.add(new QPAY_CashBackBin("557907","Santander"       ,"A0000000041010","12.00","100.00","500.00"));//
        arrayCashbackBin.add(new QPAY_CashBackBin("557909","Santander"       ,"A0000000041010","12.00","100.00","500.00"));//
        arrayCashbackBin.add(new QPAY_CashBackBin("557910","Santander"       ,"A0000000041010","12.00","100.00","500.00"));//
        //Bines de prueba y de QA
        if(Globals.environment != Globals.ENVIRONMENT.PRD){
            arrayCashbackBin.add(new QPAY_CashBackBin("557905","Santander"       ,"A0000000041010","12.00","100.00","500.00"));
            arrayCashbackBin.add(new QPAY_CashBackBin("491089","HSBC"       ,"A0000000041010","12.00","100.00","500.00"));
            arrayCashbackBin.add(new QPAY_CashBackBin("421316","HSBC"       ,"A0000000041010","12.00","100.00","500.00"));
            arrayCashbackBin.add(new QPAY_CashBackBin("413406","HSBC"       ,"A0000000041010","12.00","100.00","500.00"));
            arrayCashbackBin.add(new QPAY_CashBackBin("415231","BBVA"       ,"A0000000041010","12.00","100.00","500.00"));
            arrayCashbackBin.add(new QPAY_CashBackBin("465828","INBURSA"       ,"A0000000041010","12.00","100.00","500.00"));
            arrayCashbackBin.add(new QPAY_CashBackBin("557907","Santander"       ,"A0000000041010","12.00","100.00","500.00"));
        }
    }

    private int getAmount(){

        String amount = "";
        int back = 0;

        amount = monto.getText().replace("$", "").replace(".", "").replace(",", "");

        try {
            back = Integer.parseInt(amount);
        } catch (Exception e) {
            back = 0;
        }

        return back;

    }

    private int getAmount(String theAmount){

        String amount = "";

        amount = theAmount.replace("$","").replace(".","").replace(",","");

        int back = 0;
        try
        {
            back = Integer.parseInt(amount);
        }
        catch (Exception e)
        {
            back = 0;
        }

        return back;
    }

    private String getAmountWithoutFormat(){
        return monto.getText().replace("$","").replace(",","");
    }

    private String getImporte(){

        String amount = monto.getText()
                .replace("$","")
                .replace(",","");

        return amount;
    }


    /*****************************************************************************/
    /*                           FUNCIONES N3                                    */
    /*****************************************************************************/

    private void startN3CardReader() {
        //N3_FLAG_COMMENT

        Log.d("*** N3 READER ***","startN3CardReader");

        firstTimePIN = true;
        iccReaderError = true;

        initEmvParam();

        HashSet<CardSlotTypeEnum> slotTypes = new HashSet<>();

        slotTypes.add(CardSlotTypeEnum.ICC1);
        slotTypes.add(CardSlotTypeEnum.SWIPE);
        //slotTypes.add(CardSlotTypeEnum.RF);

        cardReader.searchCard(slotTypes, 60, this);
    }

    private void initEmvParam() {
        Log.d("*** N3 READER ***","initEmvParam");
        initEmvAid();
        initEmvCapk();
    }

    private void initEmvAid() {
        emvHandler2.delAllAid();
        if(emvHandler2.getAidListNum() <= 0){
            List<AidEntity> aidEntityList = emvUtils.getAidList();
            if (aidEntityList == null) {
                Log.d("nexgo", "initAID failed");
                return;
            }

            int i = emvHandler2.setAidParaList(aidEntityList);
            Log.d("nexgo", "setAidParaList " + i);
        }else{
            Log.d("nexgo", "setAidParaList " + "already load aid");
        }
        Log.d("nexgo", "Finish aid load");
    }

    private void initEmvCapk() {
        emvHandler2.delAllCapk();
        int capk_num = emvHandler2.getCapkListNum();
        Log.d("nexgo", "capk_num " + capk_num);
        if(capk_num <= 0){
            List<CapkEntity> capkEntityList = emvUtils.getCapkList();
            if (capkEntityList == null) {
                Log.d("nexgo", "initCAPK failed");
                return;
            }
            int j = emvHandler2.setCAPKList(capkEntityList);
            Log.d("nexgo", "setCAPKList " + j);
        }else{
            Log.d("nexgo", "setCAPKList " + "already load capk");
        }
        Log.d("nexgo", "Finish capk load");
    }

    private void showInputPin(boolean isOnlinPin) {
        Log.d("*** N3 READER ***","showInputPin");

        context.loading(false);

        pwdText = "";
        pwdAlertDialog.show();
        pwdTv.setText(pwdText);
        com.nexgo.oaf.apiv3.device.pinpad.PinPad pinPad = deviceEngine.getPinPad();
        pinPad.inputOfflinePin(new int[]{0x00, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b}, 60, this);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCardInfo(int retCode, final CardInfoEntity cardInfo) {
        Log.d("*** N3 READER ***","onCardInfo");
        cardInfoN3 = cardInfo;

        context.loading(false);

        final StringBuilder sb = new StringBuilder();
        sb.append("Los valores de regreso son: " + retCode + "\n");

        if (retCode == SdkResult.Success && cardInfo != null) {
            sb.append("Tipo de entrada: " + cardInfo.getCardExistslot() + "\n");
            sb.append("Track 1: " + cardInfo.getTk1() + "\n");
            sb.append("Track 2: " + cardInfo.getTk2() + "\n");
            sb.append("Track 3: " + cardInfo.getTk3() + "\n");
            sb.append("Número de tarjeta: " + cardInfo.getCardNo() + "\n");
            sb.append("Is ICC: " + cardInfo.isICC() + "\n");

            if (cardInfo.isICC() && cardInfo.getCardExistslot() != CardSlotTypeEnum.SWIPE) {
                isMagneticStripeCard = false;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        context.loadingConectando("Enviando datos...", new IAlertButton() {
                            @Override
                            public String onText() {
                                return "CANCELAR";
                            }

                            @Override
                            public void onClick() {
                                emvHandler2.emvProcessCancel();
                            }
                        });

                    }
                });

                emvHandler2 = null;
                emvHandler2 = deviceEngine.getEmvHandler2("app2");
                mExistSlot = cardInfo.getCardExistslot();
                //EmvTransDataEntity
                EmvTransConfigurationEntity transData = new EmvTransConfigurationEntity();
                //transData.setB9C((byte) 0x00);

                if(isCashbackTransaction)
                    transData.setEmvTransType((byte) 0x09); //0x00-sale, 0x20-refund,0x09-sale with cashback
                else
                    transData.setEmvTransType((byte) 0x00); //0x00-sale, 0x20-refund,0x09-sale with cashback

                transData.setCountryCode("0484");    //CountryCode
                transData.setCurrencyCode("0484");    //CurrencyCode, 840 indicate USD dollar

                Log.d("*** N3 READER ***", "Amount: " + Tools.leftPad("" + getAmount(), 12, '0'));
                //transData.setTransAmt(Tools.leftPad("" + getAmount(), 12, '0'));//"000000000001");

                if(isCashbackTransaction) {
                    if(isAbortProcessByCashback)
                        transData.setCashbackAmount(getCashbackAmount());
                    else {
                        //transData.setTransAmount(Tools.leftPad("" + getAmount(), 12, '0'));//"000000000001");
                        cashbackAmount = Float.parseFloat(getImporte());
                        cashbackTotalAmount = cashbackAmount + defaultFee;

                        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
                        otherSymbols.setDecimalSeparator('.');
                        DecimalFormat decimalFormat = new DecimalFormat("#.00",otherSymbols);

                        String nuevo_monto = String.format("%012d", getAmount(Utils.paserCurrency(decimalFormat.format(cashbackTotalAmount))));

                        Log.d("Monto cashback sin fee",Tools.leftPad("" + getAmount(), 12, '0'));
                        Log.d("Monto total cashback",nuevo_monto);

                        transData.setCashbackAmount(Tools.leftPad("" + getAmount(), 12, '0'));//"000000000001");
                        transData.setTransAmount("000000001200");//nuevo_monto);
                    }
                }
                else
                    transData.setTransAmount(Tools.leftPad("" + getAmount(), 12, '0'));//"000000000001");

                //transData.setam

                transData.setTermId("00000001");
                transData.setMerId("000000000000001");

                String date1 = new SimpleDateFormat("yyMMdd", Locale.getDefault()).format(new Date());
                String date2 = new SimpleDateFormat("hhmmss", Locale.getDefault()).format(new Date());

                Log.d("Terminal date",date1);
                Log.d("Terminal time",date2);

                transData.setTransDate(date1);
                transData.setTransTime(date2);

                transData.setTraceNo("00000000");

                /*if (cardInfo.getCardExistslot() == CardSlotTypeEnum.RF) {
                    transData.setProcType(com.nexgo.oaf.apiv3.emv.EmvTransFlowEnum.QPASS);
                    transData.setChannelType(EmvChannelTypeEnum.FROM_PICC);
                } else {
                    transData.setProcType(com.nexgo.oaf.apiv3.emv.EmvTransFlowEnum.FULL);
                    transData.setChannelType(EmvChannelTypeEnum.FROM_ICC);
                }*/

                transData.setEmvProcessFlowEnum(EmvProcessFlowEnum.EMV_PROCESS_FLOW_STANDARD);

                if (cardInfo.getCardExistslot() == CardSlotTypeEnum.RF) {
                    transData.setEmvEntryModeEnum(EmvEntryModeEnum.EMV_ENTRY_MODE_CONTACTLESS);
                } else {
                    transData.setEmvEntryModeEnum(EmvEntryModeEnum.EMV_ENTRY_MODE_CONTACT);
                }

                //emvHandler.initTermConfig(ByteUtils.hexString2ByteArray("9f1a0209785f2a0209789f3c020978"));
                emvHandler2.initTermConfig(ByteUtils.hexString2ByteArray("9F3501225F2A0204845F3601029F1A0204849F150253999F6604370040009F3901059F3C020484BF7301019F3303E0B0C89F4005F000F0F0019F3D0204849F1C084444444444444444DF44039F3704DF45039F3704"));
                emvHandler2.emvDebugLog(true);
                Log.d("huacong", "emvProcess start");
                emvHandler2.emvProcess(transData, this);
            }
            else{
                //Es banda
                if(cardInfo.getCardExistslot() == CardSlotTypeEnum.SWIPE && !cardInfo.isICC()) {
                    isMagneticStripeCard = true;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cardNo = cardInfo.getCardNo();
                            if (isCashbackTransaction)
                                startCashBackProcess();
                            else
                                startSendMitecTransaction();
                        }
                    });
                }
                else{
                    //Se deslizó, pero la tarjeta es CHIP
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            context.alert("Tarjeta con chip, por favor inserte tarjeta.");
                        }
                    });
                }
            }
        }
        else{
            //Se devolvió un objeto nulo

        }
    }

    @Override
    public void onSwipeIncorrect() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), getString(R.string.swipe_again), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMultipleCards() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), getString(R.string.search_toomany_cards), Toast.LENGTH_SHORT).show();
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onSelApp(final List<String> list, List<CandidateAppInfoEntity> list1, boolean b) {
        Log.d("*** N3 READER ***","onSelApp");
        int k = 0;

        for (k = 0; k < list1.size(); k++) {
            Log.d("huacong", "appInfoList " + k + ByteUtils.byteArray2HexString(list1.get(k).getAid()));
            Log.d("huacong", "appInfoList " + k + ByteUtils.byteArray2HexString(list1.get(k).getAppLabel()));
            Log.d("huacong", "appInfoList " + k + list1.get(k).getPriority());
        }

        //Tools.printList("INICIO", list);
        if(list.size() == 1)
            emvHandler2.onSetSelAppResponse(0);
        else if(list.size() > 1)
        {
            List<String> newList = Tools.delAidsInApp(list);
            //Tools.printList("NUEVA LISTA", newList);
            if(newList.size() == 1){
                int theIndex = Tools.getAppIndex(list, newList.get(0));
                //emvHandler.onSetSelAppResponse(Tools.getAppIndex(list,newList.get(0)));

                switch (theIndex) {
                    case 0:
                        emvHandler2.onSetSelAppResponse(0);
                        break;
                    case 1:
                        emvHandler2.onSetSelAppResponse(1);
                        break;
                    case 2:
                        emvHandler2.onSetSelAppResponse(2);
                        break;
                    case 3:
                        emvHandler2.onSetSelAppResponse(3);
                        break;
                    case 4:
                        emvHandler2.onSetSelAppResponse(4);
                        break;
                    case 5:
                        emvHandler2.onSetSelAppResponse(5);
                        break;
                }
            }
            else if(list.size() == newList.size() ? Tools.chekIsTheSameApp(list) : Tools.chekIsTheSameApp(newList)) {
                //Log.d("N3 ISSUE", "APLICACIONES REPETIDAS");
                if (list.size() == newList.size()) {
                    emvHandler2.onSetSelAppResponse(1);

                } else {
                    emvHandler2.onSetSelAppResponse(2);//Tools.getAppIndex(list, newList.get(1)));
                }
            }else
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        View dv = getLayoutInflater().inflate(R.layout.dialog_app_list, null);
                        final AlertDialog alertDialog = new AlertDialog.Builder(context).setView(dv).create();
                        ListView lv = (ListView) dv.findViewById(R.id.aidlistView);
                        List<Map<String, String>> listItem = new ArrayList<>();
                        for (int i = 0; i < newList.size(); i++) {
                            Map<String, String> map = new HashMap<>();
                            map.put("appIdx", (i + 1) + "");
                            map.put("appName", newList.get(i));
                            listItem.add(map);
                        }
                        SimpleAdapter adapter = new SimpleAdapter(context,
                                listItem,
                                R.layout.app_list_item,
                                new String[]{"appIdx", "appName"},
                                new int[]{R.id.tv_appIndex, R.id.tv_appName});
                        lv.setAdapter(adapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //emvHandler.onSetSelAppResponse(position + 1);
                                //Log.d("N3 ISSUE", "INDEX: " + position);
                                if(list.size() == newList.size()){
                                    emvHandler2.onSetSelAppResponse(position + 1);
                                }else {

                                    int theIndex = Tools.getAppIndex(list, newList.get(position));

                                    switch (theIndex) {
                                        case 0:
                                            emvHandler2.onSetSelAppResponse(0);
                                            break;
                                        case 1:
                                            emvHandler2.onSetSelAppResponse(1);
                                            break;
                                        case 2:
                                            emvHandler2.onSetSelAppResponse(2);
                                            break;
                                        case 3:
                                            emvHandler2.onSetSelAppResponse(3);
                                            break;
                                        case 4:
                                            emvHandler2.onSetSelAppResponse(4);
                                            break;
                                        case 5:
                                            emvHandler2.onSetSelAppResponse(5);
                                            break;
                                    }
                                }

                                alertDialog.dismiss();
                                alertDialog.cancel();
                            }
                        });
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }
                });

        }
    }

    @Override
    public void onTransInitBeforeGPO() {
        emvHandler2.onSetTransInitBeforeGPOResponse(true);
    }

    @Override
    public void onConfirmCardNo(CardInfoEntity cardInfoEntity) {
        Log.d("*** N3 READER ***","onConfirmCardNo");
        Log.d("","");

        if(mExistSlot == CardSlotTypeEnum.RF ){
            emvHandler2.onSetConfirmCardNoResponse(true);
            return ;
        }

        cardNo = cardInfoEntity.getCardNo();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                emvHandler2.onSetConfirmCardNoResponse(true);
            }
        });
    }

    @Override
    public void onCardHolderInputPin(boolean b, int i) {
        Log.d("*** N3 READER ***","onCardHolderInputPin");
        final boolean isOnlinePin = b;//false;//b;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String msg = "";
                        if(firstTimePIN) {
                            //msg = "Es necesario que se ingrese el NIP de la tarjeta.";
                            //msg = "Ingresar NIP.";
                            msg = "Da click en \"Continuar\" para ingresar NIP.";
                            firstTimePIN = false;
                        }
                        else{
                            msg = "NIP erróneo, Es necesario que se ingrese nuevamente el NIP de la tarjeta.";
                        }
                        context.alert(msg, new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Continuar";
                            }

                            @Override
                            public void onClick() {
                                showInputPin(isOnlinePin);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onContactlessTapCardAgain() {
        Log.d("nexgo", "onReadCardAgain");
    }

    @Override
    public void onPrompt(PromptEnum promptEnum) {
        Log.d("*** N3 READER ***","onPrompt");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                emvHandler2.onSetPromptResponse(true);
            }
        });

    }

    @Override
    public void onRemoveCard() {
        Log.d("*** N3 READER ***","onRemoveCard");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                emvHandler2.onSetRemoveCardResponse();
            }
        });
    }

    @Override
    public void onOnlineProc() {
        Log.d("*** N3 READER ***","onOnlineProc");
        Log.d("huacong", "onOnlineProc");

        iccReaderError = false;
        context.loading(false);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isCashbackTransaction)
                    startCashBackProcess();
                else
                {
                    startSendMitecTransaction();
                }
            }
        });

    }

    @Override
    public void onFinish(int i, EmvProcessResultEntity emvProcessResultEntity) {
        Log.d("*** N3 READER ***","onFinish");
        Log.d("*** N3 READER ***","onFinish-9F34: " + ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9f, (byte) 0x34}, EmvDataSourceEnum.FROM_KERNEL)));
        context.loading(false);
        if(isAbortProcessByCashback) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startCardReader();
                }
            });
        }
        else {
            if (iccReaderError && isDeclinedTransaction()) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        context.alert("Falla de lectura chip, intente nuevamente.");
                    }
                });
            }
        }
    }

    @Override
    public void onInputResult(int i, byte[] bytes) {
        Log.d("","");
        final int retCode = i;
        final byte[] data = bytes;
        System.out.println("onInputResult->" + i + "," + ByteUtils.byteArray2HexStringWithSpace(data));
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pwdAlertDialog.dismiss();

                if(retCode == SdkResult.PinPad_Input_Cancel)
                    iccReaderError = false;

                if (retCode == SdkResult.Success
                        || retCode == SdkResult.PinPad_No_Pin_Input
                        || retCode == SdkResult.PinPad_Input_Cancel) {
                    if (data != null) {
                        byte[] temp = new byte[8];
                        System.arraycopy(data, 0, temp, 0, 8);
                    }
                    isPinAuthentication = true;
                    //boolean b1 = retCode != SdkResult.PinPad_Input_Cancel;
                    //boolean b2 = retCode == SdkResult.PinPad_No_Pin_Input;
                    emvHandler2.onSetPinInputResponse(retCode != SdkResult.PinPad_Input_Cancel, retCode == SdkResult.PinPad_No_Pin_Input);

                } else {
                    iccReaderError = false;
                    Toast.makeText(getContext(), "Error en la captura del pin.", Toast.LENGTH_SHORT).show();
                    emvHandler2.onSetPinInputResponse(false, false);
                }
            }
        });
    }

    @Override
    public void onSendKey(byte b) {
        Log.d("","");
        final byte keyCode = b;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (keyCode == PinPadKeyCode.KEYCODE_CLEAR) {
                    pwdText = "";
                } else {
                    pwdText += "* ";
                }
                pwdTv.setText(pwdText);
            }
        });
    }

    private boolean isDeclinedTransaction(){
        boolean back = false;

        if(null == customMitecTransaction || null == customMitecTransaction.getTransactionResponse() || !customMitecTransaction.getTransactionResponse().isApproved())
            back = true;

        return back;
    }

    //N3_FLAG_COMMENT
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*Métodos deprecados*/
    //Se comenta por la integración del nuevo SDK

    /*private void initEmvAid() {
        emvHandler2.delAllAid();
        List<AidEntity> aidEntityList = emvUtils.getAidList();
        if (aidEntityList == null) {
            Log.d("huacong", "initAID failed");
            return;
        }
        emvHandler2.setAidParaList(aidEntityList);
    }

    private void initEmvCapk() {
        emvHandler2.delAllCapk();
        List<CapkEntity> capkEntityList = emvUtils.getCapkList();
        if (capkEntityList == null) {
            Log.d("huacong", "initCAPK failed");
            return;
        }
        emvHandler2.setCAPKList(capkEntityList);
    }*/

    /*@Override
    public void onConfirmEcSwitch() {
        Log.d("*** N3 READER ***","onConfirmEcSwitch");
    }*/

    /*@Override
    public void onReadCardAgain() {
        Log.d("*** N3 READER ***","onReadCardAgain");
    }*/

    /*@Override
    public void onAfterFinalSelectedApp() {
        Log.d("*** N3 READER ***","onAfterFinalSelectedApp");
        if (mExistSlot == CardSlotTypeEnum.RF) {
            byte[] aid = emvHandler2.getTlv(new byte[]{0x4F}, EmvDataSourceEnum.FROM_KERNEL);
            if (aid != null) {
                if (ByteUtils.byteArray2HexString(aid).toUpperCase().contains("A0000000043060")) {
                    emvHandler2.setTlv(new byte[]{(byte) 0x9F, (byte) 0x33}, new byte[]{(byte) 0xE0, (byte) 0x40, (byte) 0xC8});
                    emvHandler2.setTlv(new byte[]{(byte) 0xDF, (byte) 0x81, (byte) 0x18}, new byte[]{(byte) 0x40});
                    emvHandler2.setTlv(new byte[]{(byte) 0xDF, (byte) 0x81, (byte) 0x19}, new byte[]{(byte) 0x40});
                }

                emvHandler2.setTlv(new byte[]{(byte) 0xDF, (byte) 0x81, (byte) 0x21, (byte) 0x00}, ByteUtils.hexString2ByteArray("fc50b8a000"));

            }
        }
        emvHandler2.onSetTransInitBeforeGPOResponse(true);
    }*/

    /*@Override
    public void onRequestAmount()
    {
        Log.d("*** N3 READER ***","onRequestAmount");
        Log.d("*** N3 READER ***", Tools.leftPad(""+getAmount(),12,'0'));
        emvHandler2.onSetRequestAmountResponse(Tools.leftPad(""+getAmount(),12,'0'));//String.format("%012d", getAmount()));
    }*/

    /*VISA*/

    //Lógica de visa
    /*
    private QPAY_VisaEmvRequest getVisaPaymentObject(){
        QPAY_VisaEmvRequest visaEmvRequest = new QPAY_VisaEmvRequest();

        visaEmvRequest.getCspBody().setTxId(null);

        if (isMagneticStripeCard || isFallbackTxn) {
            //Es una transacción Banda
            visaEmvRequest.getCspBody().setEmv(null);

            //cspHeader
            visaEmvRequest.getCspHeader().setQpay_entryMode("2");
            visaEmvRequest.getCspHeader().setQpay_tdc(cardInfoN3.getCardNo().replace("f", "*"));

            visaEmvRequest.getCspHeader().setQpay_cardHolder("CLIENTE");
            visaEmvRequest.getCspHeader().setQpay_latitude("0.0");
            visaEmvRequest.getCspHeader().setQpay_longitude("0.0");
            visaEmvRequest.getCspHeader().setQpay_pin("0");
            pinFlag = "0";

            //cspBody
            //Se checa si es una transacción de cashback
            if (isCashbackTransaction) {
                visaEmvRequest.getCspHeader().setProductId("13005");
                visaEmvRequest.getCspBody().setAmt(Utils.paserCurrency(cashbackTotalAmount + "").replace("$", "").replace(",", ""));
                visaEmvRequest.getCspBody().setCashbackAmt(getImporte());
                visaEmvRequest.getCspBody().setCashbackFee(cashBackBin.getFee());
            } else {
                visaEmvRequest.getCspHeader().setProductId("13001");
                visaEmvRequest.getCspBody().setAmt(getImporte());
                visaEmvRequest.getCspBody().setCashbackAmt("0");
                visaEmvRequest.getCspBody().setCashbackFee("0");
            }

            visaEmvRequest.getCspBody().setKeyId("51");
            visaEmvRequest.getCspBody().setEncryptionMode("2");
            visaEmvRequest.getCspBody().setTrack2(CustomCipher.getValue(cardInfoN3.getTk2().trim().toUpperCase(), false));
            visaEmvRequest.getCspBody().setPosCondCode("0");
            visaEmvRequest.getCspBody().setCardholderIdMet("2");
            visaEmvRequest.getCspBody().setPinEntryMode("0");
            visaEmvRequest.getCspBody().setExpYear(CustomCipher.getValue(cardInfoN3.getExpiredDate().substring(0,2),true));//magneticStripeInfo.getExpiryYearEnc());
            visaEmvRequest.getCspBody().setExpMonth(CustomCipher.getValue(cardInfoN3.getExpiredDate().substring(2,4),true));//magneticStripeInfo.getExpiryMonthEnc());
            //visaEmvRequest.getCspBody().setIssuer("13");
            visaEmvRequest.getCspBody().setIssuer("7");
            if (isFallbackTxn)
                visaEmvRequest.getCspBody().setCapture("17");
            else
                visaEmvRequest.getCspBody().setCapture("1");
        } else {
            //Es una transacción EMV
            //tlvList = TLVUtils.builderTLVMap(emvCardInfo.getIcDataFull().toLowerCase());

            String[] arrayTrack2 = ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x57}, EmvDataSourceEnum.FROM_KERNEL)).trim().toUpperCase().split("D");

            String track2 = CustomCipher.getValue(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x57}, EmvDataSourceEnum.FROM_KERNEL)).trim().toUpperCase(),false);
            String expYear = CustomCipher.getValue(arrayTrack2[1].substring(0,2),true);
            String expMonth = CustomCipher.getValue(arrayTrack2[1].substring(2,4),true);
            String cipherMode = "2";
            visaEmvRequest.getCspBody().setKeyId("51");

            //cspHeader
            visaEmvRequest.getCspHeader().setQpay_entryMode("1");
            visaEmvRequest.getCspHeader().setQpay_tdc(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x5a}, EmvDataSourceEnum.FROM_KERNEL)));
            visaEmvRequest.getCspHeader().setQpay_cardHolder(Tools.hexStringToString(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x5f, (byte) 0x20}, EmvDataSourceEnum.FROM_KERNEL))));
            visaEmvRequest.getCspHeader().setQpay_latitude("0.0");
            visaEmvRequest.getCspHeader().setQpay_longitude("0.0");
            visaEmvRequest.getCspHeader().setQpay_application_label(Tools.hexStringToString(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x50}, EmvDataSourceEnum.FROM_KERNEL))));

            //cspBody
            //Se checa si es una transacción de cashback
            if (isCashbackTransaction) {
                visaEmvRequest.getCspHeader().setProductId("13005");
                visaEmvRequest.getCspBody().setAmt(Utils.paserCurrency(cashbackTotalAmount + "").replace("$", "").replace(",", ""));
                visaEmvRequest.getCspBody().setCashbackAmt(getImporte());
                visaEmvRequest.getCspBody().setCashbackFee(cashBackBin.getFee());
            } else {
                visaEmvRequest.getCspHeader().setProductId("13001");
                visaEmvRequest.getCspBody().setAmt(getImporte());
                visaEmvRequest.getCspBody().setCashbackAmt("0");
                visaEmvRequest.getCspBody().setCashbackFee("0");
            }

            visaEmvRequest.getCspBody().setEncryptionMode(cipherMode);//"0");//0
            visaEmvRequest.getCspBody().setTrack2(track2);//getFormatedTrack2(emvTlvList.get("57").getValue()));
            visaEmvRequest.getCspBody().setPosCondCode("0");
            visaEmvRequest.getCspBody().setCardholderIdMet("2");

            //Bancdera del PIN
            String TAG_9F34 = ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9f, (byte) 0x34}, EmvDataSourceEnum.FROM_KERNEL)).trim();
            pinFlag = TAG_9F34.substring(TAG_9F34.length()-2);

            if (pinFlag.equals("00")) {
                visaEmvRequest.getCspBody().setPinEntryMode("0");
                isPinAuthentication = false;
                visaEmvRequest.getCspHeader().setQpay_pin("0");
                pinFlag = "0";
            } else if (pinFlag.equals("02")) {
                visaEmvRequest.getCspBody().setPinEntryMode("1");
                isPinAuthentication = true;
                visaEmvRequest.getCspHeader().setQpay_pin("1");
                pinFlag = "1";
            }
            visaEmvRequest.getCspBody().setExpYear(expYear);//"20");
            visaEmvRequest.getCspBody().setExpMonth(expMonth);//"08");
            //visaEmvRequest.getCspBody().setIssuer("13");
            visaEmvRequest.getCspBody().setIssuer("7");
            visaEmvRequest.getCspBody().setCapture("9");

            //emv
            visaEmvRequest.getCspBody().getEmv().setAid(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x4f}, EmvDataSourceEnum.FROM_KERNEL)));//A0000000031010//4f
            visaEmvRequest.getCspBody().getEmv().setApplicationInterchangeProfile(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x82}, EmvDataSourceEnum.FROM_KERNEL)));//1c00//82
            visaEmvRequest.getCspBody().getEmv().setDedicatedFileName(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x84}, EmvDataSourceEnum.FROM_KERNEL)));//a0000000031010//84
            visaEmvRequest.getCspBody().getEmv().setTerminalVerificationResults(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x95}, EmvDataSourceEnum.FROM_KERNEL)));//8000008000//95
            visaEmvRequest.getCspBody().getEmv().setTransactionDate(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9a}, EmvDataSourceEnum.FROM_KERNEL)));//151007//9a
            visaEmvRequest.getCspBody().getEmv().setTsi(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9b}, EmvDataSourceEnum.FROM_KERNEL)));//6800//9b
            visaEmvRequest.getCspBody().getEmv().setTransactionType(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9c}, EmvDataSourceEnum.FROM_KERNEL)));//0//9c
            visaEmvRequest.getCspBody().getEmv().setIssuerCountryCode(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x5f, (byte) 0x28}, EmvDataSourceEnum.FROM_KERNEL)));//840//5f28

            visaEmvRequest.getCspBody().getEmv().setTransactionCurrencyCode(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x5f, (byte) 0x2a}, EmvDataSourceEnum.FROM_KERNEL)));//0484//5f2a
            visaEmvRequest.getCspBody().getEmv().setCardSequenceNumber(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x5f, (byte) 0x34}, EmvDataSourceEnum.FROM_KERNEL)));// Se cambia de 9f41 a 5f34
            visaEmvRequest.getCspBody().getEmv().setTransactionAmount(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9f, (byte) 0x02}, EmvDataSourceEnum.FROM_KERNEL)));//9f02
            visaEmvRequest.getCspBody().getEmv().setAmountOther(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9f, (byte) 0x03}, EmvDataSourceEnum.FROM_KERNEL)));//"0");//100//9f03
            visaEmvRequest.getCspBody().getEmv().setApplicationVersionNumber(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9f, (byte) 0x09}, EmvDataSourceEnum.FROM_KERNEL)));//008c//9f09

            visaEmvRequest.getCspBody().getEmv().setIssuerApplicationData(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9f, (byte) 0x10}, EmvDataSourceEnum.FROM_KERNEL)));//06010a03a40000//9f10
            visaEmvRequest.getCspBody().getEmv().setTerminalCountryCode(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9f, (byte) 0x1a}, EmvDataSourceEnum.FROM_KERNEL)));//484//9f1a
            visaEmvRequest.getCspBody().getEmv().setInterfaceDeviceSerialNumber(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9f, (byte) 0x1e}, EmvDataSourceEnum.FROM_KERNEL)));//324b353534323833//9f1e
            visaEmvRequest.getCspBody().getEmv().setApplicationCryptogram(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9f, (byte) 0x26}, EmvDataSourceEnum.FROM_KERNEL)));//543a534f738c3993//9f26
            visaEmvRequest.getCspBody().getEmv().setCryptogramInformationData(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9f, (byte) 0x27}, EmvDataSourceEnum.FROM_KERNEL)));//80//9f27
            visaEmvRequest.getCspBody().getEmv().setTerminalCapabilities(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9f, (byte) 0x33}, EmvDataSourceEnum.FROM_KERNEL)));//e0b0c8//9f33
            visaEmvRequest.getCspBody().getEmv().setCardholderVerificationMethodResults(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9f, (byte) 0x34}, EmvDataSourceEnum.FROM_KERNEL)));//410302//9f34
            visaEmvRequest.getCspBody().getEmv().setTerminalType(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9f, (byte) 0x35}, EmvDataSourceEnum.FROM_KERNEL)));//22//9f35
            visaEmvRequest.getCspBody().getEmv().setApplicationTransactionCounter(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9f, (byte) 0x36}, EmvDataSourceEnum.FROM_KERNEL)));//0100//9f36
            visaEmvRequest.getCspBody().getEmv().setUnpredictableNumber(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9f, (byte) 0x37}, EmvDataSourceEnum.FROM_KERNEL)));//1eb0b54e//9f37
            visaEmvRequest.getCspBody().getEmv().setTransactionSequenceCounterId(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9f, (byte) 0x41}, EmvDataSourceEnum.FROM_KERNEL)));//17794//9f41
            visaEmvRequest.getCspBody().getEmv().setApplicationCurrencyCode(ByteUtils.byteArray2HexString(emvHandler2.getTlv(new byte[]{(byte) 0x9f, (byte) 0x1a}, EmvDataSourceEnum.FROM_KERNEL)));//484//"0484");//840//9f1a

        }

        return visaEmvRequest;
    }

     */

    //Lógica de visa
    /*
    public void startSendTransaction(){

        context.loading(true);

        try {

            IVisa visaTransaction = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    context.loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        context.alert(R.string.general_error);
                        if(Tools.isN3Terminal()){
                            //N3_FLAG_COMMENT

                            EmvOnlineResultEntity emvOnlineResult = new EmvOnlineResultEntity();
                            emvOnlineResult.setRecvField55(null); //fill with the server response 55 field EMV data to do second auth
                            emvOnlineResult.setRejCode("00");
                            emvHandler2.onSetOnlineProcResponse(SdkResult.Success, null);

                            //N3_FLAG_COMMENT
                        }
                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_VisaResponse.QPAY_VisaResponseExcluder()).create();
                        String json = gson.toJson(result);

                        final QPAY_VisaResponse response = new Gson().fromJson(json, QPAY_VisaResponse.class);


                        if(response.getQpay_response().equals("true")){

                            response.setQpay_response(monto.getText());

                            if(Tools.isN3Terminal()){
                                //N3_FLAG_COMMENT

                                EmvOnlineResultEntity emvOnlineResult = new EmvOnlineResultEntity();
                                emvOnlineResult.setRecvField55(null); //fill with the server response 55 field EMV data to do second auth
                                emvOnlineResult.setAuthCode(response.getQpay_object()[0].getCspHeader().getRspId());
                                emvHandler2.onSetOnlineProcResponse(SdkResult.Success, null);

                                //N3_FLAG_COMMENT
                            }

                            if(isCashbackTransaction){
                                response.setQpay_description("Retiro de efectivo");
                                response.setComision(Utils.paserCurrency(cashBackBin.getFee()));
                                response.setTotal(Utils.paserCurrency(cashbackTotalAmount + ""));
                            }
                            else
                                response.setQpay_description("Pago con Tarjeta");


                            if(isPinAuthentication){
                                context.cargarSaldo(true,true,true,new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {
                                        context.setFragment(Fragment_pago_financiero_2.newInstance(isCashbackTransaction, response), true);
                                    }
                                });
                            } else {

                                context.setImage = new IFunction<Bitmap>() {
                                    @Override
                                    public void execute(Bitmap... data) {
                                        context.backFragment();
                                        enviarFirma(data[0], response, null);

                                    }
                                };

                                context.setFragment(Fragment_registro_financiero_5.newInstance());
                            }
                        }
                        else
                        {
                            //context.showAlert(response.getQpay_description());
                            //context.onLoading(false);
                            if(Tools.isN3Terminal()){
                                //N3_FLAG_COMMENT

                                EmvOnlineResultEntity emvOnlineResult = new EmvOnlineResultEntity();
                                emvOnlineResult.setRecvField55(null); //fill with the server response 55 field EMV data to do second auth
                                emvOnlineResult.setRejCode(response.getQpay_object()[0].getCspHeader().getRspId());
                                emvHandler2.onSetOnlineProcResponse(SdkResult.Success, null);

                                //N3_FLAG_COMMENT
                            } else {
                                context.validaSesion(response.getQpay_code(), response.getQpay_description());
                            }
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    context.loading(false);

                    if(Tools.isN3Terminal()){
                        //N3_FLAG_COMMENT

                        EmvOnlineResultEntity emvOnlineResult = new EmvOnlineResultEntity();
                        emvOnlineResult.setRecvField55(null); //fill with the server response 55 field EMV data to do second auth
                        emvOnlineResult.setRejCode("00");
                        emvHandler2.onSetOnlineProcResponse(SdkResult.Success, null);

                        //N3_FLAG_COMMENT
                    }

                    context.alert(R.string.general_error);
                }
            }, context);
            //N3_FLAG_COMMENT
            visaTransaction.getTransact(getVisaPaymentObject());//visaEmvRequest);

        } catch (Exception e) {
            e.printStackTrace();

            context.loading(false);

            if(Tools.isN3Terminal()){
                //N3_FLAG_COMMENT

                EmvOnlineResultEntity emvOnlineResult = new EmvOnlineResultEntity();
                emvOnlineResult.setRecvField55(null); //fill with the server response 55 field EMV data to do second auth
                emvOnlineResult.setRejCode("00");
                emvHandler2.onSetOnlineProcResponse(SdkResult.Success, null);

                //N3_FLAG_COMMENT
            }

            context.alert(R.string.general_error_catch);

        }

    }
     */

    //Envío de información de transacción de dudosa respuesta
    private void sendErrorTxrInfo(String txrInfo){
        QPAY_MitErrorFinancialTxrRequest mitErrorFinancialTxrRequest = new QPAY_MitErrorFinancialTxrRequest();
        mitErrorFinancialTxrRequest.setQpay_seed(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed());
        mitErrorFinancialTxrRequest.setTransaction(txrInfo);

        IMitDebugTxrListener service = null;
        try {
            service = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    if (result instanceof ErrorResponse) {
                        Log.e("TConecta","Error al enviar la información de una transacción no aprobada");
                    } else {
                        Gson gson = new GsonBuilder().create();
                        String json = gson.toJson(result);
                        QPAY_BaseResponse response = gson.fromJson(json, QPAY_BaseResponse.class);

                        Log.d("TConecta", "Información Debug enviada: " + json);
                    }

                }

                @Override
                public void onConnectionFailed(Object result) {
                    Log.e("TConecta","Error al enviar la información de una transacción no aprobada");
                }
            }, getContext());

        }catch (Exception e) {
            Log.e("TConecta","Error al enviar la información de una transacción no aprobada");
        }

        service.sendDebugInfoMitTxr(mitErrorFinancialTxrRequest);
    }

}
