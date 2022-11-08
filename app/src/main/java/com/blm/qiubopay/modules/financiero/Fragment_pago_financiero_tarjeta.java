package com.blm.qiubopay.modules.financiero;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nexgo.common.ByteUtils;
import com.nexgo.common.LogUtils;
import com.nexgo.oaf.SdkProxy;
import com.nexgo.oaf.api.cardReader.CardInfoEntity;
import com.nexgo.oaf.api.cardReader.CardReader;
import com.nexgo.oaf.api.cardReader.CardReaderEntity;
import com.nexgo.oaf.api.cardReader.CardReaderFailEnum;
import com.nexgo.oaf.api.cardReader.CardReaderTypeEnum;
import com.nexgo.oaf.api.cardReader.OnCardReaderListener;
import com.nexgo.oaf.api.cardReader.TrackAlgorithmModeEnum;
import com.nexgo.oaf.api.communication.Communication;
import com.nexgo.oaf.api.communication.OnDeviceConnectListener;
import com.nexgo.oaf.api.display.DisPlayContentList;
import com.nexgo.oaf.api.display.Display;
import com.nexgo.oaf.api.display.DisplayContentEntity;
import com.nexgo.oaf.api.display.DisplayDirectEnum;
import com.nexgo.oaf.api.display.DisplayModeEnum;
import com.nexgo.oaf.api.emv.EmvAttributeEntity;
import com.nexgo.oaf.api.emv.EmvHandler;
import com.nexgo.oaf.api.emv.EmvTransFlowEnum;
import com.nexgo.oaf.api.emv.ICCardEntity;
import com.nexgo.oaf.api.emv.LoadEmvAttributeEnum;
import com.nexgo.oaf.api.emv.OnEmvProcessListener;
import com.nexgo.oaf.api.emv.OnLoadEmvAttributeListener;
import com.nexgo.oaf.api.emv.OperateCodeEnum;
import com.nexgo.oaf.api.emv.TradeTypeEnum;
import com.nexgo.oaf.api.pinpad.PinPad;
import com.nexgo.oaf.api.terminal.DateTimeEntity;
import com.nexgo.oaf.api.terminal.OnGetTerminalInfoListener;
import com.nexgo.oaf.api.terminal.Terminal;
import com.nexgo.oaf.api.terminal.TerminalInfoEntity;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.R;
import com.blm.qiubopay.activities.MenuActivity;
import com.blm.qiubopay.components.CViewEditText;
import com.blm.qiubopay.components.CViewMenuTop;
import com.blm.qiubopay.connection.IGenericConnectionDelegate;
import com.blm.qiubopay.database.transactions.DataHelper;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.listeners.IMenuContext;
import com.blm.qiubopay.listeners.ISignatureUpload;
import com.blm.qiubopay.listeners.IUpdateDeviceData;
import com.blm.qiubopay.listeners.IVisa;
import com.blm.qiubopay.models.BL_Device;
import com.blm.qiubopay.models.ErrorResponse;
import com.blm.qiubopay.models.QPAY_CashBackBin;
import com.blm.qiubopay.models.QPAY_DeviceUpdate;
import com.blm.qiubopay.models.QPAY_Sigature;
import com.blm.qiubopay.models.QPAY_VisaResponse;
import com.blm.qiubopay.models.base.QPAY_BaseResponse;
import com.blm.qiubopay.models.mitec.CustomMitecTransaction;
import com.blm.qiubopay.models.n3.EmvTlvResponse;
import com.blm.qiubopay.models.visa.request.QPAY_VisaEmvRequest;
import com.blm.qiubopay.modules.Fragment_pago_financiero_2;
import com.blm.qiubopay.modules.Fragment_registro_financiero_5;
import com.blm.qiubopay.tools.CheckInternetConnectionHelper;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.tools.tlv.TLV;
import com.blm.qiubopay.tools.tlv.TLVUtils;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.RC4Helper;
import com.blm.qiubopay.utils.Utils;
import com.blm.qiubopay.utils.WSHelper;

import org.scf4a.Event;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import integration.praga.mit.com.apiintegration.ApiIntegrationService;
import integration.praga.mit.com.apiintegration.model.Checkmsi;
import integration.praga.mit.com.apiintegration.model.CheckmsiResponse;
import integration.praga.mit.com.apiintegration.model.Location;
import integration.praga.mit.com.apiintegration.model.TransactionEntity;
import integration.praga.mit.com.apiintegration.model.TransactionResponse;
import mx.devapps.utils.components.HFragment;
import mx.devapps.utils.interfaces.IActivityResult;
import mx.devapps.utils.interfaces.IAlertButton;
import mx.devapps.utils.interfaces.IFunction;

public class Fragment_pago_financiero_tarjeta extends HFragment implements IMenuContext {

    private boolean isCashbackTransaction;

    private CViewEditText monto;
    private Communication communication;
    private Event.ConnectType mConnectType = null;
    private BluetoothAdapter mBluetoothAdapter;
    private EmvAttributeEntity emvAttributeEntity = null;
    private EmvHandler mEmvHandler = null;
    private Terminal mTerminal;
    private TerminalInfoEntity mInfo;
    private ICCardEntity emvCardInfo;
    private boolean isFallbackTxn = false;
    private int fallbackCounter = 0;
    private boolean isMagneticStripeCard    = false;
    private Map<String, TLV> tlvList;
    private Map<String, TLV> emvTlvList;
    private CardInfoEntity magneticStripeInfo;
    private float cashbackAmount;
    private float fee;
    private float cashbackTotalAmount;
    private String pinFlag;
    private QPAY_CashBackBin cashBackBin;
    private ArrayList<QPAY_CashBackBin> arrayCashbackBin;
    private CardReader mCardReader;
    private boolean isPinAuthentication;
    private String TAG_9F03;

    //Objetos lector N3
    //N3_FLAG_COMMENT
    /*private DeviceEngine deviceEngine;
    private com.nexgo.oaf.apiv3.device.reader.CardInfoEntity cardInfoN3;
    private com.nexgo.oaf.apiv3.emv.EmvHandler emvHandler;
    private EmvUtils emvUtils;
    private com.nexgo.oaf.apiv3.device.reader.CardReader cardReader;
    private CardSlotTypeEnum mExistSlot;*/
    //N3_FLAG_COMMENT

    private String cardNo;
    private String pwdText;
    private TextView pwdTv;
    private AlertDialog pwdAlertDialog;
    private View dv;
    private EmvTlvResponse emvTlvResponse;
    private String[] tagsNameList = {"9F02","5F24","9A","4F","9F34","9F03","9F06","9F21","5A","9F10","82","8E","5F25","9F07","9F0D","9F0E","9F0F","9F26","9F27","9F36","9C","9F33","9F37","9F39","9F40","95","9B","84","5F2A","5F34","9F09","9F1A","9F1E","9F35","5F20","5F30","5F28","50","9F08","9F01","9F15"};
    //,"9F41"

    //CAPK Y AID K206
    private LinkedList<String> caps = new LinkedList<String>();
    private LinkedList<String> aids = new LinkedList<String>();
    private boolean isLoadCAPK = false;
    private boolean isLoadAID  = false;

    Gson gson_txr;

    private Button btn_confirmar;

    //Mitec
    private CustomMitecTransaction customMitecTransaction;

    private OnDeviceConnectListener onDeviceConnectListener = new OnDeviceConnectListener() {
        @Override
        public void onDeviceConnected() {

            getContextMenu().communication = communication;

            boolean isConnected = communication.getConnectionStatus();//= true;

            if(isConnected ){
                getContextMenu().loadingConectando(false, null, new IFunction() {
                    @Override
                    public void execute(Object[] data) {
                        getInfoTerminal();
                    }
                });
            }else{

                getContextMenu().loadingConectando(false, null, new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        getContextMenu().alert("No se ha conectado con el dispositivo", new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Reintentar";
                            }

                            @Override
                            public void onClick() {
                                conectar();
                            }
                        }, new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Cancelar";
                            }

                            @Override
                            public void onClick() {
                                getContextMenu().disConnect();
                                getContextMenu().initHome();
                            }
                        });

                    }
                });
            }

        }

        @Override
        public void onDeviceDisConnected() {

            if(getContextMenu().isConnected)
                getContextMenu().loadingConectando(false, null, new IFunction() {
                    @Override
                    public void execute(Object[] data) {

                        getContextMenu().alert("No se ha conectado con el dispositivo", new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Reintentar";
                            }

                            @Override
                            public void onClick() {
                                conectar();
                            }
                        }, new IAlertButton() {
                            @Override
                            public String onText() {
                                return "Cancelar";
                            }

                            @Override
                            public void onClick() {
                                getContextMenu().disConnect();
                                getContextMenu().initHome();
                            }
                        });
                    }
                });

        }
    };

    private OnEmvProcessListener onEmvProcessListener = new OnEmvProcessListener() {
        @Override
        public void onEmvProcessResult(ICCardEntity icCardInfo) {

            if (icCardInfo != null) {
                String rfCardInfo = "";
                if (icCardInfo.getTerminalDealResult() == 1) {
                    getContextMenu().alert(R.string.transaction_failed);
                } else {
                    //tlvList = TLVUtils.builderTLVMap(icCardInfo.getIcDataFull());
                    rfCardInfo = "ICData : " + icCardInfo.getIcData() +
                            " \n card num：" + icCardInfo.getCardNumber() +
                            " \n Track 2 ：" + icCardInfo.getTrack2() +
                            " \n password：" + icCardInfo.getPinString() +
                            " \n serial num：" + icCardInfo.getSerials() +
                            " \n ICData Full：" + icCardInfo.getIcDataFull() +
                            " \n Data1：" + icCardInfo.getSerials() +
                            " \n Data1：" + icCardInfo.getTerminalDealResult() +
                            " \n Term of validity：" + icCardInfo.getExpiryDate();

                    //Log.i("*LA INFORMACIÓN DE LA TDC*", rfCardInfo);
                    //spinnerDialog.showAlert(rfCardInfo);

                    String fallbackFlag = "" + icCardInfo.getTerminalDealResult();

                    if(fallbackFlag.trim().equals("-1"))
                    {
                        isFallbackTxn = true;
                        startCardReader();
                    }
                    else {
                        emvCardInfo = icCardInfo;
                        isFallbackTxn = false;
                        tlvList = TLVUtils.builderTLVMap(emvCardInfo.getIcDataFull().toLowerCase());
                        emvTlvList = TLVUtils.builderTLVMap(tlvList.get("c2").getValue());
                        cardNo = emvTlvList.get("5a").getValue().trim();

                        getContextMenu().loadingConectando(false, null, new IFunction() {
                            @Override
                            public void execute(Object[] data) {

                                getContextMenu().loading(true);

                                if (isCashbackTransaction)
                                    startCashBackProcess();
                                else {
                                    if(Globals.GATEWAY.equals(Globals.GATEWAY_MIT))
                                        startSendMitecTransaction();
                                    else
                                        startSendTransaction();
                                }
                            }
                        });

                    }
                }

                if(!isFallbackTxn)
                    mTerminal.resetTerminal();
            }
        }

        @Override
        public void onSelApp(List<String> appNameList, boolean isFirstSelect) {
            LogUtils.debug("onReceive onSelApp isFirstSelect:{}", isFirstSelect);
            //EmvL2.getInstance().onSetSelAppResponse(1);
        }

        @Override
        public void onConfirmCardNo(String cardNo) {
            LogUtils.debug("onReceive cardNo:{}", cardNo);
            //EmvL2.getInstance().onSetConfirmCardNoResponse(true);
        }

        @Override
        public void onCardHolderInputPin(boolean isOnlinePin, int pinTryCount) {
            LogUtils.debug("onReceive onCardHolderInputPin(), isOnlinePin:{}", isOnlinePin);
            //EmvL2.getInstance().onSetPinInputResponse(true, false);
        }

        @Override
        public void onCertVerify(String certName, String certInfo) {
            LogUtils.debug("onReceive onCertVerify(), certName:{}", certName);
            //EmvL2.getInstance().onSetCertVerifyResponse(true);
        }
    };

    private OnGetTerminalInfoListener onGetTerminalInfoListener = new OnGetTerminalInfoListener() {
        @Override
        public void onGetTerminalInfo(TerminalInfoEntity info) {
            mInfo = info;
            //Log.d(TAG, String.format("Call onGetTerminalInfo. Serial - %s - %s - %s, app version number - %s, firmware version number - %s", info.getSn(), info.getTerminalId(), info.getTerminalNum(), info.getAppVersion(), info.getFirmwareVersion()));
            //Toast.makeText(MainActivity.this, String.format("Call onGetTerminalInfo. Serial - %s - %s - %s, app version number - %s, firmware version number - %s", info.getSn(), info.getTerminalId(), info.getTerminalNum(), info.getAppVersion(), info.getFirmwareVersion()), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onReceiveBatteryState(boolean isBatteryEnough) {
            //Log.d(TAG, String.format("Call onReceiveBatteryState. State: %s", isBatteryEnough? "Batt good":"Batt low"));
            //Toast.makeText(MainActivity.this, String.format("Call onReceiveBatteryState. State: %s", isBatteryEnough? "Batt good":"Batt low"), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onGetTerminalTime(DateTimeEntity dateTime) {
            //Log.d(TAG, "Call onGetTerminalTime");
        }
    };

    private OnCardReaderListener onCardReaderListener = new OnCardReaderListener() {
        @Override
        public void onSearchResult(CardInfoEntity bean) {

            if (null != bean) {
                String cardInfo = "";

                switch (bean.getCardType()) {
                    case 0x01:
                    case 0x21:
                        /*cardInfo = "Card type：magnetic card" +
                                "\ncard num：" + bean.getCardNumber() +
                                "\nTrack 1：" + bean.getTrack1() +
                                "\nTrack 2：" + bean.getTrack2() +
                                "\nTrack 3：" + bean.getTrack3() +
                                "\nTerm of validity：" + bean.getExpiryDate();*/
                        final String cinfo = "Card type：magnetic card" +
                                "\ncard num：" + bean.getCardNumber() +
                                "\nTrack 1 len：" + bean.getTrack1Len() +
                                "\nTrack 1：" + bean.getTrack1() +
                                "\nTrack 2 len：" + bean.getTrack2Len() +
                                "\nTrack 2：" + bean.getTrack2() +
                                "\nTrack 3 len：" + bean.getTrack3Len() +
                                "\nTrack 3：" + bean.getTrack3() +
                                "\nService code：" + bean.getServiceCode() +
                                "\nCardholder name：" + bean.getCardholderName() +
                                "\nTerm of validity：" + bean.getExpiryDate();

                        isMagneticStripeCard = true;
                        magneticStripeInfo = bean;
                        cardInfo = cinfo;
                        cardNo = magneticStripeInfo.getCardNumber().trim();

                        //mTerminal = communication.getTerminal();
                        //mTerminal.getTerminalInfo(onGetTerminalInfoListener);

                        getContextMenu().loadingConectando(false, null, new IFunction() {
                            @Override
                            public void execute(Object[] data) {

                                getContextMenu().loading(true);

                                if (isCashbackTransaction)
                                    startCashBackProcess();
                                else {
                                    if(Globals.GATEWAY.equals(Globals.GATEWAY_MIT))
                                        startSendMitecTransaction();
                                    else
                                        startSendTransaction();
                                }
                            }
                        });

                        break;
                    case 0x02: {

                        //spinnerDialog.showProgress(R.string.reading_card);
                        cardInfo = "Card typ：IC card";
                        //bean.getCardNumber()
                        isMagneticStripeCard = false;

                        mEmvHandler = communication.getEmvHandler();
                        //mTerminal = communication.getTerminal();
                        //mTerminal.getTerminalInfo(onGetTerminalInfoListener);

                        //load_capk_206();

                        //Log.d("CARGA CAPK Y AID K260", "Clear AID complite.");
                        //Toast.makeText(getContextMenu(), "Clear AID complite.", Toast.LENGTH_LONG).show();

                        /*emvAttributeEntity = new EmvAttributeEntity();

                        emvAttributeEntity.setAuthAccount("000000000050");

                        emvAttributeEntity.setEmvProcess(EmvTransFlowEnum.FULL);
                        emvAttributeEntity.setTradeType(TradeTypeEnum.CONSUME); // 0x00 consumption  0x31  Balance inquiry
                        emvAttributeEntity.setForceOnLine(true);
                        emvAttributeEntity.setIsRf(false);

                        mEmvHandler.startEmvProcess(emvAttributeEntity, onEmvProcessListener);*/

                        /*final String cinfo3 = "Card type：magnetic card" +
                                "\ncard num：" + bean.getCardNumber() +
                                "\nTrack 1 len：" + bean.getTrack1Len() +
                                "\nTrack 1：" + bean.getTrack1() +
                                "\nTrack 2 len：" + bean.getTrack2Len() +
                                "\nTrack 2：" + bean.getTrack2() +
                                "\nTrack 3 len：" + bean.getTrack3Len() +
                                "\nTrack 3：" + bean.getTrack3() +
                                "\nService code：" + bean.getServiceCode() +
                                "\nCardholder name：" + bean.getCardholderName() +
                                "\nTerm of validity：" + bean.getExpiryDate();

                        Log.d("onCardReaderListener", cinfo3);*/

                        emvAttributeEntity = new EmvAttributeEntity();
                        if (Globals.mitec) {
                            if (isCashbackTransaction) {

                                emvAttributeEntity.setTradeType(TradeTypeEnum.EC_CONSUME); // 0x00 consumption  0x31  Balance inquiry
                                //Log.d("*** N3 READER ***","K206 CARD NUMBER: " + bean.getCardNumber());

                        /*final String cinfo2 = "Card type：magnetic card" +
                                "\ncard num：" + bean.getCardNumber() +
                                "\nTrack 1 len：" + bean.getTrack1Len() +
                                "\nTrack 1：" + bean.getTrack1() +
                                "\nTrack 2 len：" + bean.getTrack2Len() +
                                "\nTrack 2：" + bean.getTrack2() +
                                "\nTrack 3 len：" + bean.getTrack3Len() +
                                "\nTrack 3：" + bean.getTrack3() +
                                "\nService code：" + bean.getServiceCode() +
                                "\nCardholder name：" + bean.getCardholderName() +
                                "\nTerm of validity：" + bean.getExpiryDate();*/

                                //Log.d("onCardReaderListener", cinfo2);

                                //cashBackBin = findCashbackBin(bean.getCardNumber().trim().substring(0, 6));

                                //if(cashBackBin != null) {
                                cashbackAmount = Float.parseFloat(getImporte());
                                fee = (float) 12.00;//Float.parseFloat(cashBackBin.getFee());
                                cashbackTotalAmount = cashbackAmount + fee;
                                DecimalFormat decimalFormat = new DecimalFormat("#.00");

                                String nuevo_monto = String.format("%012d", getAmount(Utils.paserCurrency(decimalFormat.format(cashbackTotalAmount))));

                                Log.d("*** N3 READER ***", "NUEVO MONTO CASHBACK: " + nuevo_monto);

                                emvAttributeEntity.setAuthAccount(nuevo_monto);  //The amount must be formatted as a string of length 12

                                Log.d("*** N3 READER ***", "MONTO CASHBACK: " + String.format("%012d", getAmount()));

                                TAG_9F03 = "9F0306" + String.format("%012d", getAmount());

                                //emvAttributeEntity.setAuthAccountOther(String.format("%012d", getAmount()));
                                //}
                                //else
                                //emvAttributeEntity.setAuthAccount(String.format("%012d", getAmount()));  //The amount must be formatted as a string of length 12
                            } else {
                                emvAttributeEntity.setTradeType(TradeTypeEnum.CONSUME); // 0x00 consumption  0x31  Balance inquiry
                                emvAttributeEntity.setAuthAccount(String.format("%012d", getAmount()));  //The amount must be formatted as a string of length 12
                            }
                        } else {
                            emvAttributeEntity.setTradeType(TradeTypeEnum.CONSUME); // 0x00 consumption  0x31  Balance inquiry
                            emvAttributeEntity.setAuthAccount(String.format("%012d", getAmount()));  //The amount must be formatted as a string of length 12
                        }

                        emvAttributeEntity.setEmvProcess(EmvTransFlowEnum.FULL);
                        emvAttributeEntity.setForceOnLine(true);
                        emvAttributeEntity.setIsRf(false);

                        byte[] tek = new byte[16];
                        Arrays.fill(tek, (byte) 0x31);
                        emvAttributeEntity.setTEK2_TEXT(tek);

                        mEmvHandler.startEmvProcess(emvAttributeEntity, onEmvProcessListener);
                    }
                    break;
                    case 0x04:
                        cardInfo = "Card typ：Contactless card";

                        final String cinfo2 = "Card type：magnetic card" +
                                "\ncard num：" + bean.getCardNumber() +
                                "\nTrack 1 len：" + bean.getTrack1Len() +
                                "\nTrack 1：" + bean.getTrack1() +
                                "\nTrack 2 len：" + bean.getTrack2Len() +
                                "\nTrack 2：" + bean.getTrack2() +
                                "\nTrack 3 len：" + bean.getTrack3Len() +
                                "\nTrack 3：" + bean.getTrack3() +
                                "\nService code：" + bean.getServiceCode() +
                                "\nCardholder name：" + bean.getCardholderName() +
                                "\nTerm of validity：" + bean.getExpiryDate();


                        break;
                    case 0x41:
                        cardInfo = "Card typ：composite card" +
                                "\nCard typ：composite card：" + bean.getCardNumber() +
                                "\nTrack 1：" + bean.getTrack1() +
                                "\nTrack 2：" + bean.getTrack2() +
                                "\nTrack 3：" + bean.getTrack3() +
                                "\nTerm of validity：" + bean.getExpiryDate();
                        break;
                    default:
                        break;
                }
                //spinnerDialog.showAlert("Transacción aprobada.");
                //communication.disConnect(onDeviceConnectListener);
                //Toast.makeText(getActivity(), getString(R.string.success_title) + , Toast.LENGTH_LONG).show();
                //DialogUtils.showMsg(getActivity(), R.string.success_title, cardInfo);
            }
        }

        @Override
        public void onSearchFail(CardReaderFailEnum cardReaderFailEnum) {

            String errMsg = "";
            switch (cardReaderFailEnum) {
                case CANCEL_READ_CARD:

                    if(isCashbackTransaction)
                        errMsg = "Retiro cancelado";// getString(R.string.cancel_read_card);
                    else
                        errMsg = "Venta cancelada";

                    break;
                case READ_CARD_TIMEOUT:

                    errMsg = "Tiempo agotado";

                    break;
                case READ_CARD_ERROR:
                case OTHER_ERROR:
                default:
                    errMsg = "Intentar nuevamente";
                    break;
            }

            final String finalErrMsg = errMsg;

            getContextMenu().loadingConectando(false, null, new IFunction() {
                @Override
                public void execute(Object[] data) {
                    getContextMenu().alert(finalErrMsg);
                }
            });

        }
    };

    public static Fragment_pago_financiero_tarjeta newInstance(boolean pago) {
        Fragment_pago_financiero_tarjeta fragment = new Fragment_pago_financiero_tarjeta();
        Bundle args = new Bundle();

        args.putBoolean("Fragment_pago_tarjeta_1", pago);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            isCashbackTransaction = getArguments().getBoolean("Fragment_pago_tarjeta_1");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(!isCashbackTransaction)
            return super.onCreated(inflater.inflate(R.layout.fragment_pago_financiero_1, container, false),R.drawable.background_splash_header_1);
        else
            return super.onCreated(inflater.inflate(R.layout.fragment_cashback_1, container, false),R.drawable.background_splash_header_1);
    }

    @Override
    public void initFragment(){

        CViewMenuTop.create(getView())
                .setColorBack(R.color.clear_blue)
                .onClickBack(new CViewMenuTop.IClick() {
            @Override
            public void onClick() {
                getContextMenu().disConnect();
                getContextMenu().backFragment();
            }
        });

        if(isCashbackTransaction)
            CApplication.setAnalytics(CApplication.ACTION.CB_RETIRO_EFECTIVO_inician);
        else
            CApplication.setAnalytics(CApplication.ACTION.CB_PAGOS_TARJETA_inician);

        cashbackAmount = 0;
        fee = 0;
        cashbackTotalAmount = 0;

        gson_txr = new Gson();

        btn_confirmar = getView().findViewById(R.id.btn_confirmar);

        monto = CViewEditText.create(getView().findViewById(R.id.edit_importe))
                .setRequired(true)
                .setMinimum(1)
                .setMaximum(11)
                .setType(CViewEditText.TYPE.CURRENCY)
                .setHint("Monto")
                .setAlert(R.string.text_input_required)
                .setTextChanged(new CViewEditText.ITextChanged() {
                    @Override
                    public void onChange(String text) {

                        if(monto != null && monto.isValid())
                            btn_confirmar.setEnabled(true);
                        else
                            btn_confirmar.setEnabled(false);

                    }
                });

        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                if (!mBluetoothAdapter.isEnabled()) {

                    getContextMenu().alert("El Bluetooth de su dispositivo se encuentra apagado.", new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Activar";
                        }

                        @Override
                        public void onClick() {

                            getContextMenu().setOnActivityResult(new IActivityResult() {
                                @Override
                                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                                    dispositivo();
                                }
                            });

                            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent, 1);



                        }
                    }, new IAlertButton() {
                        @Override
                        public String onText() {
                            return "Cancelar";
                        }

                        @Override
                        public void onClick() {
                            getContextMenu().backFragment();
                        }
                    });

                    return;
                }

                if(isCashbackTransaction){
                    cashbackAmount = Float.parseFloat(monto.getText().replace("$", "").replace(",",""));
                    if (cashbackAmount < 100) {
                        getContextMenu().alert(R.string.cashback_min_amount_error);
                        return;
                    }
                }

                if(AppPreferences.getAmexUpdate().equals("1"))
                    initCardReader();
                else
                    load_capk_206();

            }
        });

        chargeCashBackBines();

        if(!Tools.getModel().equals(Globals.NEXGO_N3))
            dispositivo();
        else {
            //N3_FLAG_COMMENT
            /*deviceEngine = ((Application) getActivity().getApplication()).deviceEngine;
            cardReader = deviceEngine.getCardReader();
            emvHandler = deviceEngine.getEmvHandler("app1");
            emvHandler.clearLog();
            emvUtils = new EmvUtils(getContextMenu());*/
            //N3_FLAG_COMMENT

            dv = getLayoutInflater().inflate(R.layout.dialog_inputpin_layout, null);
            pwdTv = (TextView) dv.findViewById(R.id.input_pin);
            pwdAlertDialog = new AlertDialog.Builder(getContextMenu()).setView(dv).create();
            pwdAlertDialog.setCanceledOnTouchOutside(false);

        }

        getContextMenu().isConnected = true;
    }

    private void initCardReader(){
        isFallbackTxn = false;
        fallbackCounter = 0;
        //initValues();

        getContextMenu().loadingConectando(true, "Enviando datos...", new IFunction() {
            @Override
            public void execute(Object[] data) {
                startCardReader();
            }
        });

    }

    public void dispositivo(){

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {

            getContextMenu().alert("El Bluetooth de su dispositivo se encuentra apagado.", new IAlertButton() {
                @Override
                public String onText() {
                    return "Activar";
                }

                @Override
                public void onClick() {

                    getContextMenu().setOnActivityResult(new IActivityResult() {
                        @Override
                        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                            dispositivo();
                        }
                    });

                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 1);



                }
            }, new IAlertButton() {
                @Override
                public String onText() {
                    return "Cancelar";
                }

                @Override
                public void onClick() {
                    getContextMenu().backFragment();
                }
            });

            return;
        }

        try {
            communication = SdkProxy.getCommunication();
            mConnectType = Event.ConnectType.SPP;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        communication.open(mConnectType, getActivity());

        conectar();

    }

    public void conectar(){

        if(communication.getConnectionStatus()) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getInfoTerminal();
                }
            });

            return;
        }

        getContextMenu().loadingConectando(true, "Conectando con lector...", new IFunction() {
            @Override
            public void execute(Object[] data) {

                communication.startConnect(AppPreferences.getDevice().getMacAddress(), onDeviceConnectListener);

                Log.d("ConectarDispositivo","QTC Registered " + AppPreferences.getDevice().isQtcRegistered());
                if(!AppPreferences.getDevice().isQtcRegistered()) {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            updateData(AppPreferences.getDevice());
                        }
                    };
                    runnable.run();
                }
            }
        });

    }

    private void startCardReader(){
        startK206CardReader();
    }


    private void startK206CardReader(){

        Display mDisplay = communication.getDisplay();
        mCardReader = communication.getCardReader();
        PinPad mPinPad = communication.getPinPad();

        mDisplay.clearSceen();

        List<DisplayContentEntity> displayContentEntityList = new ArrayList<DisplayContentEntity>();

        DisplayContentEntity displayContentLine_one = new DisplayContentEntity(1,
                DisplayModeEnum.POSITIVE_DISPLAY, DisplayDirectEnum.ALIGN_CENTER_DISPLAY, getString(R.string.welcome_use));

        DisplayContentEntity displayContentLine_two;
        DisplayContentEntity displayContentLine_three;

        if(isFallbackTxn == true && fallbackCounter <= 1)
        {
            fallbackCounter++;
            displayContentLine_two = new DisplayContentEntity(2,
                    DisplayModeEnum.POSITIVE_DISPLAY, DisplayDirectEnum.ALIGN_CENTER_DISPLAY,  "Error de lectura");
            displayContentLine_three = new DisplayContentEntity(3,
                    DisplayModeEnum.POSITIVE_DISPLAY, DisplayDirectEnum.ALIGN_CENTER_DISPLAY,  "Intente nuevamente.");

            displayContentEntityList.add(displayContentLine_one);
            displayContentEntityList.add(displayContentLine_two);
            displayContentEntityList.add(displayContentLine_three);
        }
        else
        {
            if(isFallbackTxn == true) {
                fallbackCounter++;
                displayContentLine_two = new DisplayContentEntity(2,
                        DisplayModeEnum.POSITIVE_DISPLAY, DisplayDirectEnum.ALIGN_CENTER_DISPLAY,  "Deslice tarjeta");
            }
            else
            {
                displayContentLine_two = new DisplayContentEntity(2,
                        DisplayModeEnum.POSITIVE_DISPLAY, DisplayDirectEnum.ALIGN_CENTER_DISPLAY, getString(R.string.read_type));
            }
            displayContentEntityList.add(displayContentLine_one);
            displayContentEntityList.add(displayContentLine_two);

        }

        DisPlayContentList disPlayContentList = new DisPlayContentList(30, displayContentEntityList);
        mDisplay.displayOnMultiLine(disPlayContentList);
        if(isFallbackTxn == true)
            SystemClock.sleep(2000);
        else
            SystemClock.sleep(50);

        int timeOut = 50;

        if(isFallbackTxn == true && fallbackCounter > 2) {
            CardReaderEntity cardReaderEntity = new CardReaderEntity(CardReaderTypeEnum.WAIT_MAG_CARD, timeOut, TrackAlgorithmModeEnum.DUKPT);
            mCardReader.startSearch(cardReaderEntity, onCardReaderListener);
        }
        else
        {
            CardReaderEntity cardReaderEntity = new CardReaderEntity(CardReaderTypeEnum.WAIT_MAG_IC_RF_CARD, timeOut, TrackAlgorithmModeEnum.DUKPT);
            mCardReader.startSearch(cardReaderEntity, onCardReaderListener);
        }
    }

    private void startCashBackProcess() {
        if(cardNo != null)
            cashBackBin = findCashbackBin(cardNo.trim().substring(0, 6));
        else {
            if (isMagneticStripeCard) {
                cashBackBin = findCashbackBin(magneticStripeInfo.getCardNumber().trim().substring(0, 6));
            } else {
                tlvList = TLVUtils.builderTLVMap(emvCardInfo.getIcDataFull().toLowerCase());
                //Log.d("*** N3 READER ***","TLV: " + tlvList.get("c2").getValue().toUpperCase());
                emvTlvList = TLVUtils.builderTLVMap(tlvList.get("c2").getValue());
                cashBackBin = findCashbackBin(emvTlvList.get("5a").getValue().trim().substring(0, 6));
            }
        }

        if(cashBackBin != null)
        {
            cashbackAmount = Float.parseFloat(getImporte());
            fee = Float.parseFloat(cashBackBin.getFee());
            cashbackTotalAmount = cashbackAmount + fee;

            if(cashbackAmount > Float.parseFloat(cashBackBin.getMaxAmount())) {
                String msg = "El monto máximo de retiro para esta tarjeta es de $" + cashBackBin.getMaxAmount() + " pesos.";
                getContextMenu().alert(msg);
                return;
            }

            String msg = "Se realizará un cargo total por " + Utils.paserCurrency("" + cashbackTotalAmount) + getContextMenu().getResources().getString(R.string.desea_continuar);

            getContextMenu().alert(msg, new IAlertButton() {
                @Override
                public String onText() {
                    return "SI";
                }

                @Override
                public void onClick() {

                    getContextMenu().loading(true);

                    if(Globals.GATEWAY.equals(Globals.GATEWAY_MIT))
                        startSendMitecTransaction();
                    else
                        startSendTransaction();
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
            getContextMenu().alert(R.string.cashback_not_found_bin);
        }
    }

    private String getExtraTags(){
        return "9F4103000000";
    }

    public void startSendMitecTransaction() {



        checkMsi(cardNo);

    }

    //public void startSendMitecTransactionFinish() {
    public void finishTransaction(final String paymentType){

        if(!CheckInternetConnectionHelper.isAvailable(getContextMenu()))
        {
            getContextMenu().alert("No cuenta con una conexión a internet.");
            return;
        }

        //getContextMenu().loading(true);

        try {
            final TransactionEntity tx = new TransactionEntity();

            //tx.setBusinessId("8");//"219"); // tu código de comercio, lo obtendrás al momento de iniciar sesión

            tx.setCurrency("MXN"); // moneda de la transacción, MXN

            tx.setKeyId("1"); // identificador de la llave, en este caso 1
            tx.setEncriptionMode("2"); // tipo de encripción, forzosamente 2 = 3DES
            tx.setLocation(new Location()); // ubicación del dispositivo, lat-long
            tx.setSerialNumber(Tools.getUUID()); // serial del lector

            if(isMagneticStripeCard || isFallbackTxn) {

                tx.setPosEntryMode("022"); // 051 = EMV, 071 = CTLS, 022=SWIPE MS
                tx.setTracks(magneticStripeInfo.getTrack2()); // tracks cifrados separados por el caracter ";", track1;track2;track3
                tx.setCardHolder("CLIENTE"); // el nombre del tarjetahabiente, cifrado
                tx.setPinOfflineValidation(false);

                //Se checa si es una txr AMEX
                if(magneticStripeInfo.getCardNumber().trim().startsWith("3"))
                    tx.setType("AMEX");
                else
                    tx.setType("V/MC");

            }else {

                tlvList = TLVUtils.builderTLVMap(emvCardInfo.getIcDataFull().toLowerCase());
                emvTlvList = TLVUtils.builderTLVMap(tlvList.get("c2").getValue());

                //Bancdera del PIN
                //pinFlag = emvTlvList.get("9f34").getValue().substring(4,6);
                //20200330 RSBB. Cambio solicitado por Alex
                String TAG_9F34 = emvTlvList.get("9f34").getValue().trim();
                pinFlag = TAG_9F34.substring(TAG_9F34.length()-2);

                if(pinFlag.equals("00")) {
                    tx.setPinOfflineValidation(false);
                    isPinAuthentication = false;
                    pinFlag = "0";
                }
                else if(pinFlag.equals("02")) {
                    tx.setPinOfflineValidation(true);
                    isPinAuthentication = true;
                    pinFlag = "1";
                }

                tx.setCardHolder(Tools.hexStringToString(emvTlvList.get("5f20").getValue()).trim());// el nombre del tarjetahabiente
                tx.setPosEntryMode("051"); // 051 = EMV, 071 = CTLS, 022=SWIPE MS
                //Log.d("*** N3 READER ***","TLV: " + tlvList.get("c2").getValue().toUpperCase());

                if(isCashbackTransaction)
                    tx.setTagSequence(tlvList.get("c2").getValue().toUpperCase().replace("9F0306000000000000", TAG_9F03)); // cadena de tags cifrada en formato TLV
                else
                    tx.setTagSequence(tlvList.get("c2").getValue().toUpperCase()); // cadena de tags cifrada en formato TLV

                tx.setTracks(tlvList.get("c3").getValue()); // tracks cifrados separados por el caracter ";", track1;track2;track3

                //Se checa si es una txr AMEX
                if(emvTlvList.get("5a").getValue().trim().startsWith("3"))
                    tx.setType("AMEX");
                else
                    tx.setType("V/MC");
            }

            tx.setTerminalModel("NEXGO K206"); // modelo del dispositivo lector
            if(mInfo != null
                    && mInfo.getSn() != null
                    && !mInfo.getSn().trim().equals("")
                    && mInfo.getSn().trim().length() > 4 ) {
                tx.setVersionTerminal(mInfo.getAppVersion()); // versión del firmware del dispositivo lector
                tx.setSerialNumber(mInfo.getSn());// serial del lector
            }else{
                tx.setVersionTerminal(Tools.getVersion()); // versión del firmware del dispositivo lector
                tx.setSerialNumber(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id());// serial del lector
            }
            //Log.d("*** N3 READER ***","Serie: " + mInfo.getSn());
            tx.setPaymentType(paymentType);

            tx.setVersion(Tools.getVersion()); // versión de la aplicación de cobro

            //tx.setType("SALE"); // tipo de operación, en este caso VENTA
            tx.setUser("T-CONECTA"); // usuario que ejecuta la trasacción, p. ej. CAJERO 1

            //20200416 Validar si es cajero, es usuario debe ser el blmID
            if(AppPreferences.isCashier()) {
                String blmId = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_blm_id();
                tx.setUser(blmId);
            }

            tx.setReference(Tools.getReference()); // referencia de pago

            if(isCashbackTransaction)//cashbackTotalAmount
                tx.setAmount((double)cashbackTotalAmount); // importe de la transacción
            else
                tx.setAmount(new Double(getAmountWithoutFormat())); // importe de la transacción

            if (isCashbackTransaction) {

                CApplication.setAnalytics(CApplication.ACTION.CB_RETIRO_EFECTIVO_retiran);

                /****************************************************************************/
                /*                              CASHBACK                                    */
                /****************************************************************************/
                ApiIntegrationService.init(getContextMenu(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data1(), Globals.BASE_URL, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data2(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data3());
                //ApiIntegrationService.init(getContextMenu(), Globals.BUSINESS_ID, Globals.BASE_URL, Globals.API_KEY, Globals.ENCRIPTION_KEY);
                ApiIntegrationService.getInstance().cashback(tx, new ApiIntegrationService.IPragaIntegrationListener<TransactionResponse>() {
                    @Override
                    public void onSuccess(TransactionResponse transactionResponse) {
                        getContextMenu().loading(false);

                        customMitecTransaction = new CustomMitecTransaction();
                        customMitecTransaction.setPaymentType(paymentType);
                        customMitecTransaction.setTransactionResponse(transactionResponse);
                        customMitecTransaction.setQpay_response(monto.getText().toString());
                        customMitecTransaction.setInitial_amount(monto.getText());
                        customMitecTransaction.setQpay_description("Retiro de efectivo");
                        //customMitecTransaction.setComision(Utils.paserCurrency("12.00"));//cashBackBin.getFee()));
                        //RSB 20200224. Improvements. Cambiar Bansefi fee
                        customMitecTransaction.setComision(Utils.paserCurrency(cashBackBin.getFee()));
                        customMitecTransaction.setTotal(Utils.paserCurrency("" + cashbackTotalAmount ));

                        if (transactionResponse.isApproved()) {
                            //getContextMenu().alert("Cobro exitoso" + "\nFolio: " + transactionResponse.getFolio());

                            //20200713 RSB. Valida si debe persistir tras ingresar a reporte de transacciones
                            if(AppPreferences.getLocalTxnFinancial()) {
                                try {
                                    DataHelper dataHelper = new DataHelper(getContextMenu());
                                    dataHelper.insertLocalTransaction(DataHelper.DB_TXR_TYPE.FINANCIAL_TXR, Tools.getTodayDate(), "CASHBACK", gson_txr.toJson(customMitecTransaction), null);
                                } catch (Exception e) {
                                    Log.e("DATA HELPER", "CASHBACK");
                                }
                            }

                            if(isPinAuthentication){
                                getContextMenu().loading(false);
                                getContextMenu().disConnect();
                                getContextMenu().cargarSaldo(false,false,true,new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {
                                        getContextMenu().setFragment(Fragment_pago_financiero_2.newInstance(isCashbackTransaction, customMitecTransaction), true);
                                    }
                                });

                            } else {

                                final TransactionResponse txrResponse = transactionResponse;

                                getContextMenu().setImage = new IFunction<Bitmap>() {
                                    @Override
                                    public void execute(Bitmap... data) {
                                        getContextMenu().backFragment();
                                        enviarFirma(data[0], null, txrResponse);
                                    }
                                };

                                getContextMenu().setFragment(Fragment_registro_financiero_5.newInstance());

                            }
                        }
                        else
                        {
                            getContextMenu().loading(false);
                            getContextMenu().alert("Cobro Denegado" + "\nCódigo de error: " + transactionResponse.getErrorCode() + "\nDescripción: " + transactionResponse.getErrorDescription());
                            //getContextMenu().alert(transactionResponse.getErrorDescription());
                        }
                    }


                    @Override
                    public void onError(String s) {
                        getContextMenu().loading(false);

                        if(Tools.isConectionMitError(s))
                            getContextMenu().alert(R.string.general_error_internet);
                        else
                            getContextMenu().alert(R.string.general_error);
                    }

                });

            }  else {

                CApplication.setAnalytics(CApplication.ACTION.CB_PAGOS_TARJETA_cobran);

                /****************************************************************************/
                /*                              VENTA TDC                                   */
                /****************************************************************************/
                ApiIntegrationService.init(getContextMenu(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data1(), Globals.BASE_URL, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data2(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data3());
                /*Log.d("*** N3 READER ***","PETICION DATA 1: " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data1());
                Log.d("*** N3 READER ***","PETICION DATA 2: " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data2());
                Log.d("*** N3 READER ***","PETICION DATA 3: " + AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data3());*/

                //ApiIntegrationService.init(getContextMenu(), Globals.BUSINESS_ID, Globals.BASE_URL, Globals.API_KEY, Globals.ENCRIPTION_KEY);
                Gson gson = new Gson();
                String strJson = gson.toJson(tx, TransactionEntity.class);
                //Log.d("*** N3 READER ***","PETICION: " + strJson);
                ApiIntegrationService.getInstance().saleWithDongleUsing3DS(tx, new ApiIntegrationService.IPragaIntegrationListener<TransactionResponse>() {
                    @Override
                    public void onSuccess(TransactionResponse transactionResponse){
                        getContextMenu().loading(false);

                        customMitecTransaction = new CustomMitecTransaction();
                        customMitecTransaction.setPaymentType(paymentType);
                        customMitecTransaction.setTransactionResponse(transactionResponse);
                        customMitecTransaction.setQpay_response(monto.getText());
                        customMitecTransaction.setInitial_amount(monto.getText());
                        customMitecTransaction.setQpay_description("Pago con Tarjeta");

                        //EmvOnlineResultEntity emvOnlineResult = new EmvOnlineResultEntity();
                        //emvOnlineResult.setRecvField55(null); //fill with the server response 55 field EMV data to do second auth

                        if (transactionResponse.isApproved()) {

                            getContextMenu().loading(false);

                            //emvOnlineResult.setAuthCode(transactionResponse.getAuth());
                            //emvHandler.onSetOnlineProcResponse(SdkResult.Success, emvOnlineResult);
                            //getContextMenu().alert("Cobro exitoso" + "\nFolio: " + transactionResponse.getFolio());

                            //20200713 RSB. Valida si debe persistir tras ingresar a reporte de transacciones
                            if(AppPreferences.getLocalTxnFinancial()) {
                                try {
                                    DataHelper dataHelper = new DataHelper(getContextMenu());
                                    dataHelper.insertLocalTransaction(DataHelper.DB_TXR_TYPE.FINANCIAL_TXR, Tools.getTodayDate(), "SELL", gson_txr.toJson(customMitecTransaction), null);
                                } catch (Exception e) {
                                    Log.e("DATA HELPER", "SELL");
                                }
                            }

                            if(isPinAuthentication){
                                getContextMenu().disConnect();
                                getContextMenu().cargarSaldo(false,false,true,new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {
                                        getContextMenu().setFragment(Fragment_pago_financiero_2.newInstance(isCashbackTransaction, customMitecTransaction), true);
                                    }
                                });

                            } else {

                                /*context.clearFragment();
                                context.home = false;
                                context.setFragment(Fragment_pago_financiero_2.newInstance(isCashbackTransaction, customMitecTransaction));*/
                                final TransactionResponse txrResponse = transactionResponse;

                                getContextMenu().setImage = new IFunction<Bitmap>() {
                                    @Override
                                    public void execute(Bitmap... data) {
                                        getContextMenu().backFragment();
                                        enviarFirma(data[0], null, txrResponse);
                                    }
                                };

                                getContextMenu().setFragment(Fragment_registro_financiero_5.newInstance());

                            }
                        }
                        else {

                            getContextMenu().loading(false);

                            //emvOnlineResult.setRejCode("00");
                            //emvHandler.onSetOnlineProcResponse(SdkResult.Emv_Declined, emvOnlineResult);
                            getContextMenu().alert("Cobro Denegado" + "\nCódigo de error: " + transactionResponse.getErrorCode() + "\nDescripción: " + transactionResponse.getErrorDescription());
                            getContextMenu().alert(transactionResponse.getErrorDescription());
                        }
                    }

                    @Override
                    public void onError(String s) {
                        getContextMenu().loading(false);

                        if(Tools.isConectionMitError(s))
                            getContextMenu().alert(R.string.general_error_internet);
                        else
                            getContextMenu().alert(R.string.general_error);
                    }
                });
            }
            //}
        } catch (Exception e) {
            e.printStackTrace();

            getContextMenu().loading(false);
            getContextMenu().alert(R.string.general_error_catch);

        }
    }

    private void checkMsi(String bin){
        Checkmsi checkmsi = new Checkmsi();

        if (isCashbackTransaction){
            finishTransaction("C");
        }
        else {

            try {

                checkmsi.setAmount(new Float(getAmountWithoutFormat()));
                checkmsi.setBinTC(bin.substring(0, 6));
                checkmsi.setBusinessId(Integer.parseInt(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data1()));

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            ApiIntegrationService.init(getContextMenu(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data1(), Globals.BASE_URL, AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data2(), AppPreferences.getUserProfile().getQpay_object()[0].getQpay_praga_data3());
            ApiIntegrationService.getInstance().checkmsi(checkmsi, new ApiIntegrationService.IPragaIntegrationListener<CheckmsiResponse>() {
                @Override
                public void onSuccess(CheckmsiResponse checkmsiResponse) {
                    Log.d("MSI", "onSuccess");

                    if (checkmsiResponse.getResultCode().trim().equals("000")) {
                        //Cuenta con promociones

                        final CheckmsiResponse response = checkmsiResponse;
                        //Se pregunta si se desea aplicar meses sin intereses
                        getContextMenu().alert("¿Desea diferir esta compra a MESES SIN INTERESES?", new IAlertButton() {
                            @Override
                            public String onText() {
                                return "SI";
                            }

                            @Override
                            public void onClick() {
                                showPromotions(response.getPaymentType());
                            }
                        }, new IAlertButton() {
                            @Override
                            public String onText() {
                                return "NO";
                            }

                            @Override
                            public void onClick() {
                                getContextMenu().loading(true);
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
                final AlertDialog alertDialog = new AlertDialog.Builder(getContextMenu()).setView(dv).create();
                ListView lv = (ListView) dv.findViewById(R.id.aidlistView);
                List<Map<String, String>> listItem = new ArrayList<>();
                for (int i = 0; i < array.length; i++) {
                    Map<String, String> map = new HashMap<>();
                    map.put("appIdx", (i + 1) + "");
                    map.put("appName", array[i].replace("M", " meses sin intereses."));
                    listItem.add(map);
                }
                SimpleAdapter adapter = new SimpleAdapter(getContextMenu(),
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

    public void startSendTransaction(){

        //getContextMenu().loading(true);

        try {

            /*QPAY_VisaEmvRequest visaEmvRequest = new QPAY_VisaEmvRequest();

            visaEmvRequest.getCspBody().setTxId(null);

            if(isMagneticStripeCard || isFallbackTxn)
            {
                //Es una transacción Banda
                visaEmvRequest.getCspBody().setEmv(null);

                //cspHeader
                visaEmvRequest.getCspHeader().setQpay_entryMode("2");
                visaEmvRequest.getCspHeader().setQpay_tdc(magneticStripeInfo.getCardNumber().replace("f","*"));
                //if (emvTlvList.containsKey("5f20"))
                    //visaEmvRequest.getCspHeader().setQpay_cardHolder(Tools.hexStringToString(emvTlvList.get("5f20").getValue()));
                //else
                visaEmvRequest.getCspHeader().setQpay_cardHolder("CLIENTE");
                visaEmvRequest.getCspHeader().setQpay_latitude("0.0");
                visaEmvRequest.getCspHeader().setQpay_longitude("0.0");
                visaEmvRequest.getCspHeader().setQpay_pin("0");
                pinFlag = "0";

                //cspBody
                //Se checa si es una transacción de cashback
                if (isCashbackTransaction) {
                    visaEmvRequest.getCspHeader().setProductId("13005");
                    visaEmvRequest.getCspBody().setAmt(Utils.paserCurrency(cashbackTotalAmount + "").replace("$","").replace(",",""));
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
                visaEmvRequest.getCspBody().setTrack2(magneticStripeInfo.getTrack2());
                visaEmvRequest.getCspBody().setPosCondCode("0");
                visaEmvRequest.getCspBody().setCardholderIdMet("2");
                visaEmvRequest.getCspBody().setPinEntryMode("0");
                visaEmvRequest.getCspBody().setExpYear(magneticStripeInfo.getExpiryYearEnc());
                visaEmvRequest.getCspBody().setExpMonth(magneticStripeInfo.getExpiryMonthEnc());
                //visaEmvRequest.getCspBody().setIssuer("13");
                visaEmvRequest.getCspBody().setIssuer("7");
                if(isFallbackTxn)
                    visaEmvRequest.getCspBody().setCapture("17");
                else
                    visaEmvRequest.getCspBody().setCapture("1");
            }
            else
            {
                //Es una transacción EMV
                tlvList = TLVUtils.builderTLVMap(emvCardInfo.getIcDataFull().toLowerCase());

                String track2 = "";
                String expMonth = "";
                String expYear = "";
                String cipherMode = "";

                emvTlvList = TLVUtils.builderTLVMap(tlvList.get("c2").getValue());


                if (tlvList.containsKey("c3") && tlvList.containsKey("c4") && tlvList.containsKey("c5")) {
                    //Información cifrada
                    track2 = tlvList.get("c3").getValue();
                    expYear = tlvList.get("c4").getValue();
                    expMonth = tlvList.get("c5").getValue();
                    cipherMode = "2";
                    visaEmvRequest.getCspBody().setKeyId("51");
                } else {
                    //Información en claro
                    //cspBody
                    track2 = getFormatedTrack2(emvTlvList.get("57").getValue());
                    expMonth = "20";
                    expYear = "08";
                    cipherMode = "0";
                    visaEmvRequest.getCspBody().setKeyId(null);
                }

                //cspHeader
                visaEmvRequest.getCspHeader().setQpay_entryMode("1");
                visaEmvRequest.getCspHeader().setQpay_tdc(emvTlvList.get("5a").getValue().replace("f", "*"));
                if (emvTlvList.containsKey("5f20"))
                    visaEmvRequest.getCspHeader().setQpay_cardHolder(Tools.hexStringToString(emvTlvList.get("5f20").getValue()));
                else
                    visaEmvRequest.getCspHeader().setQpay_cardHolder("CLIENTE");
                visaEmvRequest.getCspHeader().setQpay_latitude("0.0");
                visaEmvRequest.getCspHeader().setQpay_longitude("0.0");
                visaEmvRequest.getCspHeader().setQpay_application_label(Tools.hexStringToString(emvTlvList.get("50").getValue()));

                //cspBody
                //Se checa si es una transacción de cashback
                if (isCashbackTransaction) {
                    visaEmvRequest.getCspHeader().setProductId("13005");
                    visaEmvRequest.getCspBody().setAmt(Utils.paserCurrency(cashbackTotalAmount + "").replace("$","").replace(",",""));
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

                pinFlag = emvTlvList.get("9f34").getValue().substring(4,6);

                if(pinFlag.equals("00")) {
                    visaEmvRequest.getCspBody().setPinEntryMode("0");
                    isPinAuthentication = false;
                    visaEmvRequest.getCspHeader().setQpay_pin("0");
                    pinFlag = "0";
                }
                else if(pinFlag.equals("02")) {
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
                visaEmvRequest.getCspBody().getEmv().setAid(emvTlvList.get("4f").getValue());//A0000000031010
                visaEmvRequest.getCspBody().getEmv().setApplicationInterchangeProfile(emvTlvList.get("82").getValue());//1c00
                visaEmvRequest.getCspBody().getEmv().setDedicatedFileName(emvTlvList.get("84").getValue());//a0000000031010
                visaEmvRequest.getCspBody().getEmv().setTerminalVerificationResults(emvTlvList.get("95").getValue());//8000008000
                visaEmvRequest.getCspBody().getEmv().setTransactionDate(emvTlvList.get("9a").getValue());//151007
                visaEmvRequest.getCspBody().getEmv().setTsi(emvTlvList.get("9b").getValue());//6800
                visaEmvRequest.getCspBody().getEmv().setTransactionType(emvTlvList.get("9c").getValue());//0
                visaEmvRequest.getCspBody().getEmv().setIssuerCountryCode(emvTlvList.get("5f28").getValue());//840

                visaEmvRequest.getCspBody().getEmv().setTransactionCurrencyCode(emvTlvList.get("5f2a").getValue());//0484
                visaEmvRequest.getCspBody().getEmv().setCardSequenceNumber(emvTlvList.get("5f34").getValue());// Se cambia de 9f41 a 5f34
                visaEmvRequest.getCspBody().getEmv().setTransactionAmount(emvTlvList.get("9f02").getValue());//txtAmount.getText().toString().replace("$", "").replace(".", ""));//Se cambia el monto por el regresado en el tag 9f02
                visaEmvRequest.getCspBody().getEmv().setAmountOther(emvTlvList.get("9f03").getValue());//"0");//100
                visaEmvRequest.getCspBody().getEmv().setApplicationVersionNumber(emvTlvList.get("9f09").getValue());//008c

                visaEmvRequest.getCspBody().getEmv().setIssuerApplicationData(emvTlvList.get("9f10").getValue());//06010a03a40000
                visaEmvRequest.getCspBody().getEmv().setTerminalCountryCode(emvTlvList.get("9f1a").getValue());//484
                visaEmvRequest.getCspBody().getEmv().setInterfaceDeviceSerialNumber(emvTlvList.get("9f1e").getValue());//324b353534323833
                visaEmvRequest.getCspBody().getEmv().setApplicationCryptogram(emvTlvList.get("9f26").getValue());//543a534f738c3993
                visaEmvRequest.getCspBody().getEmv().setCryptogramInformationData(emvTlvList.get("9f27").getValue());//80
                visaEmvRequest.getCspBody().getEmv().setTerminalCapabilities(emvTlvList.get("9f33").getValue());//e0b0c8
                visaEmvRequest.getCspBody().getEmv().setCardholderVerificationMethodResults(emvTlvList.get("9f34").getValue());//410302
                visaEmvRequest.getCspBody().getEmv().setTerminalType(emvTlvList.get("9f35").getValue());//22
                visaEmvRequest.getCspBody().getEmv().setApplicationTransactionCounter(emvTlvList.get("9f36").getValue());//0100
                visaEmvRequest.getCspBody().getEmv().setUnpredictableNumber(emvTlvList.get("9f37").getValue());//1eb0b54e
                visaEmvRequest.getCspBody().getEmv().setTransactionSequenceCounterId(emvTlvList.get("9f41").getValue());//17794
                visaEmvRequest.getCspBody().getEmv().setApplicationCurrencyCode(emvTlvList.get("9f1a").getValue());//484//"0484");//840

            }*/

            IVisa visaTransaction = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    getContextMenu().loading(false);

                    if (result instanceof ErrorResponse) {
                        //Se muestra el error.
                        getContextMenu().alert(R.string.general_error);
                        if(Tools.getModel().equals(Globals.NEXGO_N3)) {
                            //N3_FLAG_COMMENT
                            /*
                            EmvOnlineResultEntity emvOnlineResult = new EmvOnlineResultEntity();
                            emvOnlineResult.setRecvField55(null); //fill with the server response 55 field EMV data to do second auth
                            emvOnlineResult.setRejCode("00");
                            emvHandler.onSetOnlineProcResponse(SdkResult.Success, null);
                            */
                            //N3_FLAG_COMMENT
                        }
                    } else {

                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_VisaResponse.QPAY_VisaResponseExcluder()).create();
                        String json = gson.toJson(result);

                        final QPAY_VisaResponse response = new Gson().fromJson(json, QPAY_VisaResponse.class);


                        if(response.getQpay_response().equals("true")){

                            //getContextMenu().disConnect();
                            response.setQpay_response(monto.getText());

                            if(Tools.getModel().equals(Globals.NEXGO_N3)) {
                                //N3_FLAG_COMMENT
                                /*
                                EmvOnlineResultEntity emvOnlineResult = new EmvOnlineResultEntity();
                                emvOnlineResult.setRecvField55(null); //fill with the server response 55 field EMV data to do second auth
                                emvOnlineResult.setAuthCode(response.getQpay_object()[0].getCspHeader().getRspId());
                                emvHandler.onSetOnlineProcResponse(SdkResult.Success, null);
                                */
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

                                getContextMenu().loading(false);
                                getContextMenu().disConnect();
                                /*getContextMenu().cargarSaldo(true,true,true,new IFunction() {
                                    @Override
                                    public void execute(Object[] data) {
                                        getContextMenu().setFragment(Fragment_pago_financiero_2.newInstance(isCashbackTransaction, customMitecTransaction), true);
                                    }
                                });*/

                            } else {

                                getContextMenu().setImage = new IFunction<Bitmap>() {
                                    @Override
                                    public void execute(Bitmap... data) {
                                        getContextMenu().backFragment();
                                        enviarFirma(data[0], response, null);

                                    }
                                };

                                getContextMenu().setFragment(Fragment_registro_financiero_5.newInstance());
                            }
                        }
                        else {
                            getContextMenu().validaSesion(response.getQpay_code(), response.getQpay_description());
                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    getContextMenu().loading(false);

                    if(Tools.getModel().equals(Globals.NEXGO_N3)) {
                        //N3_FLAG_COMMENT
                        /*
                        EmvOnlineResultEntity emvOnlineResult = new EmvOnlineResultEntity();
                        emvOnlineResult.setRecvField55(null); //fill with the server response 55 field EMV data to do second auth
                        emvOnlineResult.setRejCode("00");
                        emvHandler.onSetOnlineProcResponse(SdkResult.Success, null);
                        */
                        //N3_FLAG_COMMENT
                    }

                    getContextMenu().alert(R.string.general_error);
                }
            }, getContextMenu());

            visaTransaction.getTransact(getVisaPaymentObject());//visaEmvRequest);

        } catch (Exception e) {
            e.printStackTrace();

            getContextMenu().loading(false);

            if(Tools.getModel().equals(Globals.NEXGO_N3)) {
                //N3_FLAG_COMMENT
                /*
                EmvOnlineResultEntity emvOnlineResult = new EmvOnlineResultEntity();
                emvOnlineResult.setRecvField55(null); //fill with the server response 55 field EMV data to do second auth
                emvOnlineResult.setRejCode("00");
                emvHandler.onSetOnlineProcResponse(SdkResult.Success, null);
                */
                //N3_FLAG_COMMENT
            }

            getContextMenu().alert(R.string.general_error_catch);

        }

    }

    public void enviarFirma(Bitmap image, final QPAY_VisaResponse response, final TransactionResponse transactionResponse ){

        getContextMenu().loading(true);

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

                    getContextMenu().loading(false);

                    //Transacción visa
                    if (result instanceof ErrorResponse) {
                        getContextMenu().alert(R.string.general_error);
                    } else {

                        //Respuesta correcta
                        Gson gson = new GsonBuilder().setExclusionStrategies(new QPAY_BaseResponse.QPAY_BaseResponseExcluder()).create();
                        String json = gson.toJson(result);

                        QPAY_BaseResponse taeResponse = new Gson().fromJson(json, QPAY_BaseResponse.class);

                        getContextMenu().loading(false);

                        if (taeResponse.getQpay_response().equals("true")) {
                            getContextMenu().disConnect();
                            getContextMenu().cargarSaldo(false,false,true,new IFunction() {
                                @Override
                                public void execute(Object[] data) {
                                    if(Globals.GATEWAY.equals(Globals.GATEWAY_MIT))
                                        getContextMenu().setFragment(Fragment_pago_financiero_2.newInstance(isCashbackTransaction, customMitecTransaction), true);
                                    else
                                        getContextMenu().setFragment(Fragment_pago_financiero_2.newInstance(isCashbackTransaction, response), true);
                                }
                            });

                        } else
                            getContextMenu().alert(taeResponse.getQpay_description());

                    }
                }

                @Override
                public void onConnectionFailed(Object result) {

                    getContextMenu().loading(false);

                    getContextMenu().alert(R.string.general_error);
                }
            }, getContextMenu());

            signatureUpload.uploadSignature(signature);

        } catch (Exception e) {
            e.printStackTrace();

            getContextMenu().loading(false);

            getContextMenu().alert(R.string.general_error_catch);
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

        arrayCashbackBin.add(new QPAY_CashBackBin("402766","Banco Azteca"    ,"A0000000041010","12.00","100.00","500.00"));
        arrayCashbackBin.add(new QPAY_CashBackBin("476684","Mundial"         ,"A0000000031010","12.00","100.00","500.00"));
        arrayCashbackBin.add(new QPAY_CashBackBin("476687","Mundial"         ,"A0000000031010","12.00","100.00","500.00"));
        arrayCashbackBin.add(new QPAY_CashBackBin("483112","Banco Azteca"    ,"A0000000041010","12.00","100.00","500.00"));
        arrayCashbackBin.add(new QPAY_CashBackBin("516576","Banco Azteca"    ,"A0000000041010","12.00","100.00","500.00"));
        arrayCashbackBin.add(new QPAY_CashBackBin("520416","Perfiles"        ,"A0000000041010","12.00","100.00","500.00"));
        arrayCashbackBin.add(new QPAY_CashBackBin("525678","Perfil Ejecutivo","A0000000041010","12.00","100.00","500.00"));
        arrayCashbackBin.add(new QPAY_CashBackBin("526354","Banco Azteca"    ,"A0000000041010","12.00","100.00","500.00"));
        arrayCashbackBin.add(new QPAY_CashBackBin("534381","Banco Azteca"    ,"A0000000041010","12.00","100.00","500.00"));
        arrayCashbackBin.add(new QPAY_CashBackBin("551238","Banco Azteca"    ,"A0000000041010","12.00","100.00","500.00"));
        arrayCashbackBin.add(new QPAY_CashBackBin("551507","Bansefi"         ,"A0000000041010","10.80","100.00","500.00"));
        arrayCashbackBin.add(new QPAY_CashBackBin("557907","Santander"       ,"A0000000041010","12.00","100.00","500.00"));
        arrayCashbackBin.add(new QPAY_CashBackBin("557909","Santander"       ,"A0000000041010","12.00","100.00","500.00"));
        arrayCashbackBin.add(new QPAY_CashBackBin("557910","Santander"       ,"A0000000041010","12.00","100.00","500.00"));

    }

    private int getAmount(){

        String amount = "";

        amount = monto.getText().replace("$","").replace(".","").replace(",","");

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

    private QPAY_VisaEmvRequest getVisaPaymentObject(){
        QPAY_VisaEmvRequest visaEmvRequest = new QPAY_VisaEmvRequest();

        visaEmvRequest.getCspBody().setTxId(null);

        if(!Tools.getModel().equals(Globals.NEXGO_N3)) {
            if (isMagneticStripeCard || isFallbackTxn) {
                //Es una transacción Banda
                visaEmvRequest.getCspBody().setEmv(null);

                //cspHeader
                visaEmvRequest.getCspHeader().setQpay_entryMode("2");
                visaEmvRequest.getCspHeader().setQpay_tdc(magneticStripeInfo.getCardNumber().replace("f", "*"));
                /*if (emvTlvList.containsKey("5f20"))
                    visaEmvRequest.getCspHeader().setQpay_cardHolder(Tools.hexStringToString(emvTlvList.get("5f20").getValue()));
                else*/
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
                visaEmvRequest.getCspBody().setTrack2(magneticStripeInfo.getTrack2());
                visaEmvRequest.getCspBody().setPosCondCode("0");
                visaEmvRequest.getCspBody().setCardholderIdMet("2");
                visaEmvRequest.getCspBody().setPinEntryMode("0");
                visaEmvRequest.getCspBody().setExpYear(magneticStripeInfo.getExpiryYearEnc());
                visaEmvRequest.getCspBody().setExpMonth(magneticStripeInfo.getExpiryMonthEnc());
                //visaEmvRequest.getCspBody().setIssuer("13");
                visaEmvRequest.getCspBody().setIssuer("7");
                if (isFallbackTxn)
                    visaEmvRequest.getCspBody().setCapture("17");
                else
                    visaEmvRequest.getCspBody().setCapture("1");
            } else {
                //Es una transacción EMV
                tlvList = TLVUtils.builderTLVMap(emvCardInfo.getIcDataFull().toLowerCase());

                String track2 = "";
                String expMonth = "";
                String expYear = "";
                String cipherMode = "";

                emvTlvList = TLVUtils.builderTLVMap(tlvList.get("c2").getValue());

                if (tlvList.containsKey("c3") && tlvList.containsKey("c4") && tlvList.containsKey("c5")) {
                    //Información cifrada
                    track2 = tlvList.get("c3").getValue();
                    expYear = tlvList.get("c4").getValue();
                    expMonth = tlvList.get("c5").getValue();
                    cipherMode = "2";
                    visaEmvRequest.getCspBody().setKeyId("51");
                } else {
                    //Información en claro
                    //cspBody
                    track2 = getFormatedTrack2(emvTlvList.get("57").getValue());
                    expMonth = "20";
                    expYear = "08";
                    cipherMode = "0";
                    visaEmvRequest.getCspBody().setKeyId(null);
                }

                //cspHeader
                visaEmvRequest.getCspHeader().setQpay_entryMode("1");
                visaEmvRequest.getCspHeader().setQpay_tdc(emvTlvList.get("5a").getValue().replace("f", "*"));
                if (emvTlvList.containsKey("5f20"))
                    visaEmvRequest.getCspHeader().setQpay_cardHolder(Tools.hexStringToString(emvTlvList.get("5f20").getValue()));
                else
                    visaEmvRequest.getCspHeader().setQpay_cardHolder("CLIENTE");
                visaEmvRequest.getCspHeader().setQpay_latitude("0.0");
                visaEmvRequest.getCspHeader().setQpay_longitude("0.0");
                visaEmvRequest.getCspHeader().setQpay_application_label(Tools.hexStringToString(emvTlvList.get("50").getValue()));

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

                pinFlag = emvTlvList.get("9f34").getValue().substring(4, 6);

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
                visaEmvRequest.getCspBody().getEmv().setAid(emvTlvList.get("4f").getValue());//A0000000031010
                visaEmvRequest.getCspBody().getEmv().setApplicationInterchangeProfile(emvTlvList.get("82").getValue());//1c00
                visaEmvRequest.getCspBody().getEmv().setDedicatedFileName(emvTlvList.get("84").getValue());//a0000000031010
                visaEmvRequest.getCspBody().getEmv().setTerminalVerificationResults(emvTlvList.get("95").getValue());//8000008000
                visaEmvRequest.getCspBody().getEmv().setTransactionDate(emvTlvList.get("9a").getValue());//151007
                visaEmvRequest.getCspBody().getEmv().setTsi(emvTlvList.get("9b").getValue());//6800
                visaEmvRequest.getCspBody().getEmv().setTransactionType(emvTlvList.get("9c").getValue());//0
                visaEmvRequest.getCspBody().getEmv().setIssuerCountryCode(emvTlvList.get("5f28").getValue());//840

                visaEmvRequest.getCspBody().getEmv().setTransactionCurrencyCode(emvTlvList.get("5f2a").getValue());//0484
                visaEmvRequest.getCspBody().getEmv().setCardSequenceNumber(emvTlvList.get("5f34").getValue());// Se cambia de 9f41 a 5f34
                visaEmvRequest.getCspBody().getEmv().setTransactionAmount(emvTlvList.get("9f02").getValue());//txtAmount.getText().toString().replace("$", "").replace(".", ""));//Se cambia el monto por el regresado en el tag 9f02
                visaEmvRequest.getCspBody().getEmv().setAmountOther(emvTlvList.get("9f03").getValue());//"0");//100
                visaEmvRequest.getCspBody().getEmv().setApplicationVersionNumber(emvTlvList.get("9f09").getValue());//008c

                visaEmvRequest.getCspBody().getEmv().setIssuerApplicationData(emvTlvList.get("9f10").getValue());//06010a03a40000
                visaEmvRequest.getCspBody().getEmv().setTerminalCountryCode(emvTlvList.get("9f1a").getValue());//484
                visaEmvRequest.getCspBody().getEmv().setInterfaceDeviceSerialNumber(emvTlvList.get("9f1e").getValue());//324b353534323833
                visaEmvRequest.getCspBody().getEmv().setApplicationCryptogram(emvTlvList.get("9f26").getValue());//543a534f738c3993
                visaEmvRequest.getCspBody().getEmv().setCryptogramInformationData(emvTlvList.get("9f27").getValue());//80
                visaEmvRequest.getCspBody().getEmv().setTerminalCapabilities(emvTlvList.get("9f33").getValue());//e0b0c8
                visaEmvRequest.getCspBody().getEmv().setCardholderVerificationMethodResults(emvTlvList.get("9f34").getValue());//410302
                visaEmvRequest.getCspBody().getEmv().setTerminalType(emvTlvList.get("9f35").getValue());//22
                visaEmvRequest.getCspBody().getEmv().setApplicationTransactionCounter(emvTlvList.get("9f36").getValue());//0100
                visaEmvRequest.getCspBody().getEmv().setUnpredictableNumber(emvTlvList.get("9f37").getValue());//1eb0b54e
                visaEmvRequest.getCspBody().getEmv().setTransactionSequenceCounterId(emvTlvList.get("9f41").getValue());//17794
                visaEmvRequest.getCspBody().getEmv().setApplicationCurrencyCode(emvTlvList.get("9f1a").getValue());//484//"0484");//840

            }
        }
        return visaEmvRequest;
    }

    private void updateData(final BL_Device data) {

        //getContextMenu().loading(true);

        try {

            QPAY_DeviceUpdate deviceUpdate = new QPAY_DeviceUpdate();
            String seed = AppPreferences.getUserProfile().getQpay_object()[0].getQpay_seed();
            deviceUpdate.setQpay_seed(seed);
            deviceUpdate.getQpay_device_info()[0].setQpay_dongle_sn(data.getName());
            deviceUpdate.getQpay_device_info()[0].setQpay_dongle_mac(data.getMacAddress());

            IUpdateDeviceData iUpdateDeviceData = new WSHelper(new IGenericConnectionDelegate() {
                @Override
                public void onConnectionEnded(Object result) {

                    //getContextMenu().loading(false);

                    Gson gson = new GsonBuilder().create();
                    String json = gson.toJson(result);
                    QPAY_BaseResponse baseResponse = gson.fromJson(json, QPAY_BaseResponse.class);

                    if (result instanceof ErrorResponse) {
                        Log.d("ConectarDispositivo",((ErrorResponse) result).getMessage());
                        //getContextMenu().alert(R.string.general_error);
                    }else{

                        if(baseResponse.getQpay_response().equals("true")) {

                            //CApplication.setAnalytics(CApplication.ACTION.QPAY_login_exitoso);
                            Log.d("ConectarDispositivo","Success. " + baseResponse.getQpay_description());
                            BL_Device myDevice = AppPreferences.getDevice();
                            myDevice.setQtcRegistered(true);
                            AppPreferences.setDevice(myDevice);

                        }else{

                            //CApplication.setAnalytics(CApplication.ACTION.QPAY_login_no_exitoso);
                            Log.d("ConectarDispositivo","Failed." + baseResponse.getQpay_description());

                        }
                    }
                }

                @Override
                public void onConnectionFailed(Object result) {
                    //getContextMenu().loading(false);
                    Log.d("ConectarDispositivo",getString(R.string.general_error));
                    //getContextMenu().alert(R.string.general_error);
                }
            }, getContextMenu());

            iUpdateDeviceData.doUpdateDeviceData(deviceUpdate);



        } catch (Exception e) {
            //getContextMenu().loading(false);
            //getContextMenu().alert(R.string.general_error_catch);
            Log.d("ConectarDispositivo",getString(R.string.general_error_catch));
        }

    }

    public void load_capk_206(){

        getContextMenu().loadingConectando(true, "Enviando datos...", new IFunction() {
            @Override
            public void execute(Object[] data) {

            }
        });

        Log.d("CARGA CAPK Y AID K260", "Start load capk.");
        isLoadCAPK = true;
        caps.clear();

        caps.add("9F0605A0000000259F2201C8DF0503351231DF060101DF070101DF028190BF0CFCED708FB6B048E3014336EA24AA007D7967B8AA4E613D26D015C4FE7805D9DB131CED0D2A8ED504C3B5CCD48C33199E5A5BF644DA043B54DBF60276F05B1750FAB39098C7511D04BABC649482DDCF7CC42C8C435BAB8DD0EB1A620C31111D1AAAF9AF6571EEBD4CF5A08496D57E7ABDBB5180E0A42DA869AB95FB620EFF2641C3702AF3BE0B0C138EAEF202E21DDF040103DF031433BD7A059FAB094939B90A8F35845C9DC779BD50");
        caps.add("9F0605A0000000259F2201C9DF0503351231DF060101DF070101DF0281B0B362DB5733C15B8797B8ECEE55CB1A371F760E0BEDD3715BB270424FD4EA26062C38C3F4AAA3732A83D36EA8E9602F6683EECC6BAFF63DD2D49014BDE4D6D603CD744206B05B4BAD0C64C63AB3976B5C8CAAF8539549F5921C0B700D5B0F83C4E7E946068BAAAB5463544DB18C63801118F2182EFCC8A1E85E53C2A7AE839A5C6A3CABE73762B70D170AB64AFC6CA482944902611FB0061E09A67ACB77E493D998A0CCF93D81A4F6C0DC6B7DF22E62DBDF040103DF03148E8DFF443D78CD91DE88821D70C98F0638E51E49");
        caps.add("9F0605A0000000259F2201CADF0503351231DF060101DF070101DF0281F8C23ECBD7119F479C2EE546C123A585D697A7D10B55C2D28BEF0D299C01DC65420A03FE5227ECDECB8025FBC86EEBC1935298C1753AB849936749719591758C315FA150400789BB14FADD6EAE2AD617DA38163199D1BAD5D3F8F6A7A20AEF420ADFE2404D30B219359C6A4952565CCCA6F11EC5BE564B49B0EA5BF5B3DC8C5C6401208D0029C3957A8C5922CBDE39D3A564C6DEBB6BD2AEF91FC27BB3D3892BEB9646DCE2E1EF8581EFFA712158AAEC541C0BBB4B3E279D7DA54E45A0ACC3570E712C9F7CDF985CFAFD382AE13A3B214A9E8E1E71AB1EA707895112ABC3A97D0FCB0AE2EE5C85492B6CFD54885CDD6337E895CC70FB3255E3DF040103DF03146BDA32B1AA171444C7E8F88075A74FBFE845765F");

        byte[] data = ByteUtils.hexString2ByteArray(caps.pop());

        mEmvHandler = communication.getEmvHandler();
        mEmvHandler.loadPublicKey(OperateCodeEnum.ADD_ONE, data, onLoadEmvAttributeListener);

        //Toast.makeText(getContextMenu(), "Actualizando llaves AMEX", Toast.LENGTH_LONG).show();
    }

    public void load_aid_206(){

        Log.d("CARGA CAPK Y AID K260", "Start load AID.");
        isLoadAID = true;
        aids.clear();

        //AMEX
        aids.add("9F0606A00000002501DF0101009F09020001DF1105C800000000DF1205C800000000DF130500000000009F1B0400000000DF15000DF160163DF170100DF1414009F6A0400000000000000000000000000000000DF180101DF1906000000020000DF2006000000030000DF2106000000010000");

        byte[] data = ByteUtils.hexString2ByteArray(aids.pop());
        mEmvHandler.loadAID(OperateCodeEnum.ADD_ONE, data, onLoadEmvAttributeListener);

        //Toast.makeText(getContextMenu(), "Actualizando llaves AMEX", Toast.LENGTH_LONG).show();
    }

    private OnLoadEmvAttributeListener onLoadEmvAttributeListener = new OnLoadEmvAttributeListener() {

        @Override
        public void onUpdatePublicKey(LoadEmvAttributeEnum loadEmvAttributeEnum,
                                      byte[] publicKeyData) {
            Log.d("CARGA CAPK Y AID K260", "Call onUpdatePublicKey");
            Log.d("CARGA CAPK Y AID K260", String.format("onUpdatePublicKey rezult - %s", loadEmvAttributeEnum.toString()));
            if(isLoadCAPK) {
                if(!caps.isEmpty()) {
                    Log.d("CARGA CAPK Y AID K260", "Load next CAPK.");

                    byte[] data = ByteUtils.hexString2ByteArray(caps.pop());
                    mEmvHandler.loadPublicKey(OperateCodeEnum.ADD_ONE, data, onLoadEmvAttributeListener);
                }else {
                    Log.d("CARGA CAPK Y AID K260", "Load CAPK complite.");
                    //Toast.makeText(getContextMenu(), "Load CAPK complite.", Toast.LENGTH_LONG).show();
                    load_aid_206();
                }

            }/*else {
                Log.d("CARGA CAPK Y AID K260", "Clear CAPK complite.");
                Toast.makeText(getContextMenu(), "Clear CAPK complite.", Toast.LENGTH_LONG).show();
            }*/
        }

        @Override
        public void onUpdateAID(LoadEmvAttributeEnum loadEmvAttributeEnum, byte[] aidData) {
            Log.d("CARGA CAPK Y AID K260", "Call onUpdateAID");
            Log.d("CARGA CAPK Y AID K260", String.format("onUpdateAID rezult - %s", loadEmvAttributeEnum.toString()));
            if(isLoadAID) {
                if(!aids.isEmpty()) {
                    Log.d("CARGA CAPK Y AID K260", "Load next AID.");

                    byte[] data = ByteUtils.hexString2ByteArray(aids.pop());
                    mEmvHandler.loadAID(OperateCodeEnum.ADD_ONE, data, onLoadEmvAttributeListener);
                }else {
                    Log.d("CARGA CAPK Y AID K260", "Load AID complite.");
                    //Toast.makeText(getContextMenu(), "Load AID complite.", Toast.LENGTH_LONG).show();
                    //startCardReader();
                    getContextMenu().loading(false);
                    AppPreferences.setAmexUpdate("1");
                    initCardReader();
                }

            }/*else{
                startCardReader();
            }*/
        }

        @Override
        public void onSetTlv(LoadEmvAttributeEnum loadEmvAttributeEnum) {
            Log.d("CARGA CAPK Y AID K260", String.format("onSetTlv rezult - %s", loadEmvAttributeEnum.toString()));
            /*if(!tags.isEmpty()) {
                Log.d("CARGA CAPK Y AID K260", "Load next tag.");
                String key = tags.pop();
                byte[] tag = ByteUtils.hexString2ByteArray(key);
                byte[] tagValue = ByteUtils.hexString2ByteArray(conf.get(key));
                mEmvHandler.setTlv(tag, tagValue, onLoadEmvAttributeListener);
            }else {
                Log.d("CARGA CAPK Y AID K260", "Load EMV config complite.");
                Toast.makeText(getContextMenu(), "Load EMV config complite.", Toast.LENGTH_LONG).show();
            }*/
        }

        @Override
        public void onGetTlv(LoadEmvAttributeEnum loadEmvAttributeEnum, byte[] data) {
            Log.d("CARGA CAPK Y AID K260", "Call onGetTlv");
        }
    };

    @Override
    public MenuActivity getContextMenu() {
        return (MenuActivity) getContext();
    }

    //ContactLess
    private void getInfoTerminal(){
        mTerminal = communication.getTerminal();
        mTerminal.getTerminalInfo(onGetTerminalInfoListener);
    }

}