package com.blm.qiubopay.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.blm.qiubopay.database.operative.RestaurantDataHelper;
import com.blm.qiubopay.models.order.OrderNotification;
import com.blm.qiubopay.models.swap.QPAY_LinkT1000Request;
import com.blm.qiubopay.models.swap.SwapData;
import com.blm.qiubopay.models.carousel.PublicityResponse;
import com.blm.qiubopay.models.carousel.QPayBaseResponse;
import com.blm.qiubopay.models.transactions.TransactionsModel;
import com.blm.qiubopay.utils.LocalStorage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.blm.qiubopay.database.transactions.DataHelper;
import com.blm.qiubopay.models.BL_Device;
import com.blm.qiubopay.models.LastLocation;
import com.blm.qiubopay.models.LastLogin;
import com.blm.qiubopay.models.QPAY_Category1;
import com.blm.qiubopay.models.QPAY_Category2;
import com.blm.qiubopay.models.QPAY_Notification;
import com.blm.qiubopay.models.QPAY_Notifications;
import com.blm.qiubopay.models.QPAY_Pin;
import com.blm.qiubopay.models.QPAY_Privileges;
import com.blm.qiubopay.models.QPAY_UserCredentials;
import com.blm.qiubopay.models.QPAY_UserProfile;
import com.blm.qiubopay.models.bimbo.CatProducts;
import com.blm.qiubopay.models.bimbo.PedidoRequest;
import com.blm.qiubopay.models.bimbo.ProductoResponse;
import com.blm.qiubopay.models.financial_vas.QPAY_FinancialVasGetAmountsResponse;
import com.blm.qiubopay.models.financial_vas.QPAY_VasInfo;
import com.blm.qiubopay.models.fincomun.FCRequestDTO;
import com.blm.qiubopay.models.pidelo.NewOrder;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Globals;
import com.blm.qiubopay.utils.PideloUtilty;
import com.blm.qiubopay.utils.SessionApp;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import mx.com.fincomun.origilib.Objects.Retiro.DHCuenta;

//import com.example.android.multidex.myCApplication.R;

public class AppPreferences {

    private static final String USER_PROFILE     = "USER_PROFILE";
    private static final String REMEMBER_USER    = "REMEMBER_USER";
    private static final String DEVICE           = "DEVICE";
    private static final String CATEGORY1        = "CATEGORY1";
    private static final String CATEGORY2        = "CATEGORY2";
    private static final String CATEGORY3        = "CATEGORY3";
    private static final String ACCESS_PIN       = "ACCESS_PIN";
    private static final String FLAG_PIN         = "FLAG_PIN";
    private static final String SIGN_CONTRACT    = "SIGN_CONTRACT";
    private static final String USER_CREDENTIALS = "USER_CREDENTIALS";
    private static final String FLAG_MERCHANT    = "FLAG_MERCHANT";
    private static final String KINETO_BALANCE   = "KINETO_BALANCE";
    private static final String FINANCIAL_BALANCE   = "FINANCIAL_BALANCE";
    private static final String BENEFITS_BALANCE   = "BENEFITS_BALANCE";
    private static final String CLOSE_SESSION_FLAG   = "CLOSE_SESSION_FLAG";
    private static final String IMAGE_PERFIL     = "IMAGE_PERFIL";
    private static final String NOTIFICATIONS    = "NOTIFICATIONS";
    private static final String FLAG_SESSION     = "FLAG_SESSION";
    private static final String AMEX_UPDATE      = "AMEX_UPDATE";
    private static final String TODAY_LOCATION   = "TODAY_LOCATION";
    private static final String TODAY_LOGIN      = "TODAY_LOGIN";
    private static final String BOX_CUT_TIME     = "BOX_CUT_TIME";
    private static final String BOX_CUT_DATE     = "BOX_CUT_DATE";
    private static final String CLOSE_SESSION_MESSAGE = "CLOSE_SESSION_MESSAGE";
    private static final String USER_PRIVILEGES  = "USER_PRIVILEGES";
    private static final String FCM_TOKEN        = "FCM_TOKEN";
    private static final String CART_LIST        = "CART_LIST";
    private static final String COUNTER          = "COUNTER";
    private static final String FINANCIAL_VAS_AMOUNTS        = "FINANCIAL_VAS_AMOUNTS";
    private static final String FINANCIAL_VAS_INFO           = "FINANCIAL_VAS_INFO";
    private static final String LOCAL_TXN_TAE    = "LOCAL_TXN_TAE";
    private static final String LOCAL_TXN_SERVICE= "LOCAL_TXN_SERVICE";
    private static final String LOCAL_TXN_FINANCIAL = "LOCAL_TXN_FINANCIAL";
    private static final String LOCAL_TXN_QIUBO  = "LOCAL_TXN_QIUBO";
    private static final String LOCAL_TXN_UPDATE = "LOCAL_TXN_UPDATE";
    private static final String FLAG_RATE_APP    = "FLAG_RATE_APP";
    private static final String COUNT_TRANS_TO_RATE = "COUNT_TRANS_TO_RATE";
    private static final String FINCOMUN         = "FINCOMUN";
    private static final String FCSTEP            = "FCSTEP";
    private static final String PRODUCTS         = "PRODUCTS";
    private static final String ACCESS           = "ACCESS";
    private static final String ORDER            = "ORDER";
    private static final String SAVA_DATA_PRODUCTS            = "SAVA_DATA_PRODUCTS";
    private static final String REGISTER_BIMBO_ID_FC           = "REGISTER_BIMBO_ID_FC";

    private static final String UPDATE_PRODUCTOS = "UPDATE_PRODUCTOS";
    private static final String VERSION_PRODUCTOS = "HASH_PRODUCTOS";

    private static final String CODI_RASTREO = "CODI_RASTREO";
    private static final String CODI_CLABE = "CODI_CLABE";
    private static final String CODI_REGISTER = "CODI_REGISTER";
    private static final String CUENTA_CODI = "CUENTA_CODI";
    private static final String CODI_OMISION = "CODI_OMISION";

    private static final String ENKO = "ENKO";

    private static final String SWAP_T1000 = "SWAP_T1000";
    private static final String LINK_T1000 = "LINK_T1000";
    private static final String IS_LOGIN   = "IS_LOGIN";
    private static final String IS_BACKGROUND   = "IS_BACKGROUND";

    //REMESAS
    private static final String REMESAS_ERROR_COUNTER = "REMESAS_ERROR_COUNTER";
    private static final String REMESAS_BLOCKED_DATE = "REMESAS_BLOCKED_DATE";

    //Chambitas Contador
    private static final String CHAMBITAS_COUNTER = "CHAMBITAS_COUNTER";                     // CHAMBITAS NUEVAS
    private static final String CHAMBITAS_COMPLETED_COUNTER = "CHAMBITAS_COMPLETED_COUNTER"; // CHAMBITAS COMPLETADAS
    private static final String CHAMBITAS_CURRENT_COUNTER = "CHAMBITAS_CURRENT_COUNTER";     // CHAMBITAS ACTIVAS
    //Tips y Publicidad Contador
    private static final String PUBLICIDAD_COUNTER = "PUBLICIDAD_COUNTER";

    private static final String OBJECT_SIZE = "tconecta.objectSize";
    private static final String PUBLICITY_SIZE = "tconecta.publicitySize0";
    private static final String TCONECTA_YEAR = "tconecta.year";
    private static final String TCONECTA_MONTH = "tconecta.month";
    private static final String TCONECTA_DAY = "tconecta.day";
    private static final String CAMPAING_ID = "tconecta.campaignId";
    private static final String ACTIVATE_ORDER_STATUS = "tconecta.activateOrderStatus";
    private static final String NEW_ORDER_ID = "tconecta.newOrderId";
    private static final String ORDER_TYPE = "tconecta.orderType";
    private static final String NEW_ORDER_COUNTER = "tconecta.newOrderCounter";
    private static final String STORE_LOCATION = "tconecta.storeLocation";
    private static final String TODAY_TRANSACTIONS = "todayTransactions";
    private static final String YESTERDAY_TRANSACTIONS = "yesterdayTransactions";

    //Applink Notificaciones
    private static final String APPLINK = "APPLINK";


    private static SharedPreferences sharedPreferences;

    private AppPreferences(){}

    public static void Init(Context context){
        sharedPreferences = context.getSharedPreferences(Globals.TAG_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static void Logout(Context context){

        isLogin(false);

        //sharedPreferences.edit().putString(USER_PROFILE     , "").apply();
        sharedPreferences.edit().putString(DEVICE           , "").apply();
        sharedPreferences.edit().putString(CATEGORY1        , "").apply();
        sharedPreferences.edit().putString(IMAGE_PERFIL     , null).apply();
        sharedPreferences.edit().putString(CATEGORY2        , "").apply();
        sharedPreferences.edit().putString(CATEGORY3        , "").apply();
        sharedPreferences.edit().putString(ACCESS_PIN       , "").apply();
        sharedPreferences.edit().putString(FLAG_PIN         , "0").apply();
        sharedPreferences.edit().putString(SIGN_CONTRACT    , "0").apply();
        sharedPreferences.edit().putString(USER_CREDENTIALS , "").apply();
        sharedPreferences.edit().putString(FLAG_MERCHANT    , "").apply();
        sharedPreferences.edit().putString(KINETO_BALANCE   , "").apply();
        sharedPreferences.edit().putString(FINANCIAL_BALANCE   , "").apply();
        sharedPreferences.edit().putString(BENEFITS_BALANCE   , "").apply();
        sharedPreferences.edit().putString(SIGN_CONTRACT    , "").apply();
        sharedPreferences.edit().putString(NOTIFICATIONS    , "").apply();
        sharedPreferences.edit().putString(AMEX_UPDATE      , "0").apply();
        sharedPreferences.edit().putString(BOX_CUT_TIME     , "").apply();
        sharedPreferences.edit().putString(BOX_CUT_DATE     , "").apply();
        //sharedPreferences.edit().putString(CLOSE_SESSION_FLAG, "0").apply();
        sharedPreferences.edit().putString(TODAY_LOCATION   , "").apply();
        sharedPreferences.edit().putString(TODAY_LOGIN      , "").apply();
        sharedPreferences.edit().putString(USER_PRIVILEGES  , "").apply();
        sharedPreferences.edit().putString(FINANCIAL_VAS_AMOUNTS  , "").apply();
        sharedPreferences.edit().putBoolean(LOCAL_TXN_TAE   ,   false).apply();
        sharedPreferences.edit().putBoolean(LOCAL_TXN_SERVICE,  false).apply();
        sharedPreferences.edit().putBoolean(LOCAL_TXN_FINANCIAL,false).apply();
        sharedPreferences.edit().putBoolean(LOCAL_TXN_QIUBO ,   false).apply();
        PideloUtilty.deleteAll();
        sharedPreferences.edit().putString(FINANCIAL_VAS_INFO  , "").apply();
        sharedPreferences.edit().putString(FINCOMUN         , "").apply();
        sharedPreferences.edit().putString(FCSTEP           , "").apply();
        sharedPreferences.edit().putString(PRODUCTS         , "").apply();
        sharedPreferences.edit().putString(ORDER           , "").apply();
        sharedPreferences.edit().putBoolean(SAVA_DATA_PRODUCTS           , false).apply();
        sharedPreferences.edit().putBoolean(REGISTER_BIMBO_ID_FC           , false).apply();
        sharedPreferences.edit().putString(VERSION_PRODUCTOS, "").apply();
        sharedPreferences.edit().putBoolean(UPDATE_PRODUCTOS, false).apply();
        sharedPreferences.edit().putString(CODI_CLABE, "").apply();
        sharedPreferences.edit().putString(CODI_RASTREO, "").apply();
        sharedPreferences.edit().putBoolean(CODI_REGISTER, false).apply();
        sharedPreferences.edit().putString(CUENTA_CODI, "").apply();
        sharedPreferences.edit().putBoolean(CODI_OMISION, false).apply();
        sharedPreferences.edit().putBoolean(ENKO ,   false).apply();

        sharedPreferences.edit().putString(Globals.CATEGORIAS.TODAS.ordinal() + "", "").apply();

        //REMESAS
        //sharedPreferences.edit().putString(REMESAS_ERROR_COUNTER      , "0").apply();
        //sharedPreferences.edit().putString(REMESAS_BLOCKED_DATE       , "").apply();

        sharedPreferences.edit().putString(CHAMBITAS_COUNTER, "").apply();
        sharedPreferences.edit().putString(CHAMBITAS_COMPLETED_COUNTER, "").apply();
        sharedPreferences.edit().putString(CHAMBITAS_CURRENT_COUNTER, "").apply();
        sharedPreferences.edit().putString(PUBLICIDAD_COUNTER, "").apply();
        sharedPreferences.edit().putInt(OBJECT_SIZE, -1).apply();
        sharedPreferences.edit().putInt(PUBLICITY_SIZE, -1).apply();
        sharedPreferences.edit().putInt(TCONECTA_YEAR, -1).apply();
        sharedPreferences.edit().putInt(TCONECTA_MONTH, -1).apply();
        sharedPreferences.edit().putInt(TCONECTA_DAY, -1).apply();
        sharedPreferences.edit().putInt(CAMPAING_ID, -1).apply();
        sharedPreferences.edit().putBoolean(ACTIVATE_ORDER_STATUS, false).apply();
        sharedPreferences.edit().putString(NEW_ORDER_ID + 0, "").apply();
        sharedPreferences.edit().putString(ORDER_TYPE, "").apply();
        sharedPreferences.edit().putInt(NEW_ORDER_COUNTER, -1).apply();
        sharedPreferences.edit().putString(STORE_LOCATION, "").apply();

        SessionApp.clearInstance();

        DataHelper dataHelper = new DataHelper(context);
        dataHelper.delAllTransactions();

        RestaurantDataHelper restaurantDataHelper = new RestaurantDataHelper(context);
        restaurantDataHelper.delAllTransactions();
    }

    public static void clearOrder(Integer marca){
        sharedPreferences.edit().putString(marca + "", "").apply();
    }

    public static String getCodiClabe() {
        return sharedPreferences.getString(CODI_CLABE, "");
    }

    public static void setCodiClabe(String value) {
        sharedPreferences.edit().putString(CODI_CLABE, value).apply();
    }

    public static DHCuenta getCuentaCodi() {
        String json = sharedPreferences.getString(CUENTA_CODI, "");

        if(json != null && !json.isEmpty())
            return new Gson().fromJson(json, DHCuenta.class);
        else
            return null;
    }

    /**
     * Validar activación de tooltip Chambitas
     * @return
     */
    public static boolean isTooltipChambitasActive() {
        String json = sharedPreferences.getString(USER_PROFILE, "");
        if(!json.isEmpty()){
            QPAY_UserProfile userProfile = new Gson().fromJson(json, QPAY_UserProfile.class);
            String tooltip_chambitas = userProfile.getQpay_object()[0].getTooltip_chambitas();
            tooltip_chambitas = (tooltip_chambitas != null ? tooltip_chambitas.trim() : "");

            if(tooltip_chambitas.compareTo("1")==0) {
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }

    public static void setCuentaCodi(DHCuenta cuenta) {
        sharedPreferences.edit().putString(CUENTA_CODI, new Gson().toJson(cuenta)).apply();
    }

    public static String getCodiRastreo() {
        return sharedPreferences.getString(CODI_RASTREO, "");
    }

    public static void setCodiRastreo(String value) {
        sharedPreferences.edit().putString(CODI_RASTREO, value).apply();
    }

    public static void setCodiRegister(boolean flag){
        sharedPreferences.edit().putBoolean(CODI_REGISTER, flag).apply();
    }

    public static boolean getCodiRegister(){
        return sharedPreferences.getBoolean(CODI_REGISTER, false);
    }

    public static void setCodiOmision(boolean flag){
        sharedPreferences.edit().putBoolean(CODI_OMISION, flag).apply();
    }

    public static boolean getCodiOmision(){
        return sharedPreferences.getBoolean(CODI_OMISION, false);
    }

    public static void setStepFC(Integer step){
        sharedPreferences.edit().putInt(FCSTEP, step).apply();
    }

    public static Integer getStepFC(){
        return sharedPreferences.getInt(FCSTEP, 1);
    }

    public static void setVersionProductos(String hash){
        sharedPreferences.edit().putString(VERSION_PRODUCTOS, hash).apply();
    }

    public static String getVersionProductos(){
        return sharedPreferences.getString(VERSION_PRODUCTOS, "");
    }

    public static void setUpdateProductos(Boolean activar){
        sharedPreferences.edit().putBoolean(UPDATE_PRODUCTOS, activar).apply();
    }

    public static Boolean getUpdateProductos(){
        return sharedPreferences.getBoolean(UPDATE_PRODUCTOS, false);
    }

    public static void setFCRequest(FCRequestDTO fincomun){
        String json = new Gson().toJson(fincomun);
        sharedPreferences.edit().putString(FINCOMUN, json).apply();
    }

    public static FCRequestDTO getFCRequest(){
        FCRequestDTO fincomun = null;
        String json = sharedPreferences.getString(FINCOMUN, "");
        if(!json.isEmpty()){
            sharedPreferences.edit().putString(FINCOMUN         , "").apply();
        }
        fincomun = new FCRequestDTO();
        return fincomun;
    }

    public static PedidoRequest getOrder(Integer marca){
        PedidoRequest pedido = null;
        String json = sharedPreferences.getString(marca + "", "");
        if(!json.isEmpty()){
            pedido = new Gson().fromJson(json, PedidoRequest.class);
        }else{
            pedido = new PedidoRequest();//new Gson().fromJson(Globals.AFS_JSON, QPAY_UserProfile.class);
        }
        return pedido;
    }

    public static void setOrder(Integer marca, PedidoRequest pedido){
        String json = new Gson().toJson(pedido);
        sharedPreferences.edit().putString(marca + "", json).apply();
    }

    public static CatProducts getProducts(){
        CatProducts catProducts = null;
        String json = sharedPreferences.getString(PRODUCTS, "");
        if(!json.isEmpty()){
            catProducts = new Gson().fromJson(json, CatProducts.class);
        }else{
            catProducts = new CatProducts(new Gson().fromJson("{}", ProductoResponse.class).getProducts());
        }

        return catProducts;
    }

    public static void setSaveDataProducts(boolean flag){
        sharedPreferences.edit().putBoolean(SAVA_DATA_PRODUCTS, flag).apply();
    }

    public static boolean getSaveDataProducts(){
        return sharedPreferences.getBoolean(SAVA_DATA_PRODUCTS, false);
    }

    public static void setProducts(CatProducts products){
        String json = new Gson().toJson(products);
        sharedPreferences.edit().putString(PRODUCTS, json).apply();
    }

    /*USER_PROFILE*/
    public static boolean IsUserLogged(){
        String json = sharedPreferences.getString(USER_PROFILE, "");
        return !json.isEmpty();
    }

    public static void setUserProfile(QPAY_UserProfile userProfile){
        String json = new Gson().toJson(userProfile);
        sharedPreferences.edit().putString(USER_PROFILE, json).apply();

        TransactionsModel today = AppPreferences.getTodayTransactions();
        if (today.getSeed() == null) {
            today.setSeed(userProfile.getQpay_object()[0].getQpay_seed());
            today.setFecha(Tools.getTodayDate("dd/MM/yyyy"));
            AppPreferences.setTodayTransactions(today);
        }
    }

    public static QPAY_UserProfile getUserProfile() {
        QPAY_UserProfile userProfile = null;
        String json = sharedPreferences.getString(USER_PROFILE, "");
        if(!json.isEmpty()){
            userProfile = new Gson().fromJson(json, QPAY_UserProfile.class);
        }else{
            userProfile = null;//new Gson().fromJson(Globals.AFS_JSON, QPAY_UserProfile.class);
        }
        return userProfile;
    }

    /*DEVICE*/
    public static boolean isDeviceRegistered(){
        String json = sharedPreferences.getString(DEVICE, "");
        return !json.isEmpty();
    }

    public static void setDevice(BL_Device device){
        String json = new Gson().toJson(device);
        sharedPreferences.edit().putString(DEVICE, json).apply();
    }

    public static BL_Device getDevice(){
        BL_Device device = null;
        String json = sharedPreferences.getString(DEVICE, "");
        if(!json.isEmpty()){
            device = new Gson().fromJson(json, BL_Device.class);
        }
        return device;
    }

    public static void deleteDevice()
    {
        sharedPreferences.edit().putString(DEVICE, "").apply();
    }

    /*CATEGORY 1*/
    public static boolean isCategory1Registered(){
        String json = sharedPreferences.getString(CATEGORY1, "");
        return !json.isEmpty();
    }

    public static void setCategory1(QPAY_Category1 data){
        String json = new Gson().toJson(data);
        sharedPreferences.edit().putString(CATEGORY1, json).apply();
    }

    public static QPAY_Category1 getCategory1(){
        QPAY_Category1 object = null;
        String json = sharedPreferences.getString(CATEGORY1, "");
        if(!json.isEmpty()){
            object = new Gson().fromJson(json, QPAY_Category1.class);
        }
        return object;
    }

    /*CATEGORY 2*/
    public static boolean isCategory2Registered(){
        String json = sharedPreferences.getString(CATEGORY2, "");
        return !json.isEmpty();
    }

    public static void setCategory2(QPAY_Category2 data){
        String json = new Gson().toJson(data);
        sharedPreferences.edit().putString(CATEGORY2, json).apply();
    }

    public static QPAY_Category2 getCategory2(){
        QPAY_Category2 object = null;
        String json = sharedPreferences.getString(CATEGORY2, "");
        if(!json.isEmpty()){
            object = new Gson().fromJson(json, QPAY_Category2.class);
        }
        return object;
    }

    /* PIN DE ACCESO */
    public static boolean isPinRegistered(){
        String json = sharedPreferences.getString(ACCESS_PIN, "");
        return !json.isEmpty();
    }

    public static void setPin(QPAY_Pin data){
        String json = new Gson().toJson(data);
        sharedPreferences.edit().putString(ACCESS_PIN, json).apply();
    }

    public static QPAY_Pin getPin(){
        QPAY_Pin data = null;
        String json = sharedPreferences.getString(ACCESS_PIN, "");
        if(!json.isEmpty()){
            data = new Gson().fromJson(json, QPAY_Pin.class);
        }
        return data;
    }

    public static void isLogin(Boolean status){
        sharedPreferences.edit().putBoolean(IS_LOGIN, status).apply();
    }

    public static Boolean isLogin(){
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public static void isBackground(Boolean status){
        sharedPreferences.edit().putBoolean(IS_BACKGROUND, status).apply();
    }

    public static Boolean isBackground(){
        return sharedPreferences.getBoolean(IS_BACKGROUND, false);
    }

    /* BANDERA DEL PIN DE ACCESO */
    public static boolean isPinFlagRegistered(){
        String json = sharedPreferences.getString(FLAG_PIN, "");
        return !json.isEmpty();
    }

    public static void setPinFlag(String data){
        //String json = new Gson().toJson(data);
        sharedPreferences.edit().putString(FLAG_PIN, data).apply();
    }

    public static String getPinFlag(){
        String data = sharedPreferences.getString(FLAG_PIN, "");
        return data;
    }

    /* FIRMA DEL CONTRATO */
    public static boolean isContractSignRegistered(){
        String json = sharedPreferences.getString(SIGN_CONTRACT, "");
        return !json.isEmpty();
    }

    public static void setContractSign(String data){
        sharedPreferences.edit().putString(SIGN_CONTRACT, data).apply();
    }

    public static String getContractSign(){
        String data = sharedPreferences.getString(SIGN_CONTRACT, "");
        return data;
    }

    public static void setImagePerfil(String data){
        sharedPreferences.edit().putString(IMAGE_PERFIL, data).apply();
    }

    public static String getImagePerfil(){
        String data = sharedPreferences.getString(IMAGE_PERFIL, "");
        return data;
    }

    /*USER_CREDENTIALS*/
    public static boolean ExistUserCredentials(){
        String json = sharedPreferences.getString(USER_CREDENTIALS, "");
        return !json.isEmpty();
    }

    public static void setUserCredentials(QPAY_UserCredentials userCredentials){
        String json = new Gson().toJson(userCredentials);
        sharedPreferences.edit().putString(USER_CREDENTIALS, json).apply();
    }

    public static QPAY_UserCredentials getUserCredentials(){
        QPAY_UserCredentials userCredentials = null;
        String json = sharedPreferences.getString(USER_CREDENTIALS, "");
        if(!json.isEmpty()){
            userCredentials = new Gson().fromJson(json, QPAY_UserCredentials.class);
        }else{
            userCredentials = null;//new Gson().fromJson(Globals.AFS_JSON, QPAY_UserProfile.class);
        }
        return userCredentials;
    }

    /* BANDERA DE LA ACTIVACIÓN DEL MERCHANT */
    public static boolean isMerchantActivationFlagPresent(){
        String json = sharedPreferences.getString(FLAG_MERCHANT, "");
        return !json.isEmpty();
    }

    public static void setMerchantActivationFlag(String data){
        //String json = new Gson().toJson(data);
        sharedPreferences.edit().putString(FLAG_MERCHANT, data).apply();
    }

    public static String getMerchantActivationFlag(){
        String data = sharedPreferences.getString(FLAG_MERCHANT, "");
        return data;
    }

    /* SALDO DE KINETO */
    public static boolean isKinetoBalancePresent(){
        String json = sharedPreferences.getString(KINETO_BALANCE, "");
        return !json.isEmpty();
    }

    public static void setKinetoBalance(String data){
        sharedPreferences.edit().putString(KINETO_BALANCE, data).apply();
    }

    public static String getKinetoBalance(){
        String data = sharedPreferences.getString(KINETO_BALANCE, "");
        return data;
    }

    public static void setFinancialBalance(String data){
        sharedPreferences.edit().putString(FINANCIAL_BALANCE, data).apply();
    }

    public static String getFinancialBalance(){
        String data = sharedPreferences.getString(FINANCIAL_BALANCE, "");
        return data;
    }

    public static void setBenefitsBalance(String data){
        sharedPreferences.edit().putString(BENEFITS_BALANCE, data).apply();
    }

    public static String getBenefitsBalance(){
        String data = sharedPreferences.getString(BENEFITS_BALANCE, "");
        return data;
    }



    /* BANDERA PARA CERRAR SESIÓN */
    public static boolean isCloseSessionFlagEnabled(){
        boolean back = true;
        String json = sharedPreferences.getString(CLOSE_SESSION_FLAG, "");
        if(json.isEmpty() || json.equals("0"))
            back = false;
        return back;//!json.isEmpty();
    }

    /**
     * Validar URL de Enko
     * @return
     */
    public static String getEnkoUrl() {

        String json = sharedPreferences.getString(USER_PROFILE, "");
        if(!json.isEmpty()){
            QPAY_UserProfile userProfile = new Gson().fromJson(json, QPAY_UserProfile.class);
            String urlEnko = userProfile.getQpay_object()[0].getQpay_enko_url();
            urlEnko = (urlEnko != null ? urlEnko.trim() : "");

            if(urlEnko.isEmpty()) {
                return null;
            } else {
                return urlEnko;
            }
        }else{
            return null;
        }

    }

    //20200407 RSB. Multiuser - Se modifica para persistir el mensaje que mostrará al cerrar sesión
    public static void setCloseSessionFlag(String data,String message){
        sharedPreferences.edit().putString(CLOSE_SESSION_FLAG, data).apply();
        if(message!=null) {
            sharedPreferences.edit().putString(CLOSE_SESSION_MESSAGE, message).apply();
        }
    }

    //20200407 RSB. Multiuser - Se modifica para obtener el mensaje que mostrará al cerrar sesión
    public static String getCloseSessionMessage(){
        String data = sharedPreferences.getString(CLOSE_SESSION_MESSAGE, "");
        return data;
    }

    public static void addNotification(QPAY_Notification notification){

        QPAY_Notifications notifications = getNotifications();
        notifications.getNotifications().add(notification);
        notifications.setFcmId(getFCM());

        String json = new Gson().toJson(notifications);
        sharedPreferences.edit().putString(NOTIFICATIONS, json).apply();
    }

    public static QPAY_Notifications getNotifications(){

        String json = sharedPreferences.getString(NOTIFICATIONS, "");

        if(!json.isEmpty())
             return new Gson().fromJson(json, QPAY_Notifications.class);
        else
            return new QPAY_Notifications();

    }
    /* UPDATE AMEX */
    public static void setAmexUpdate(String data){
        sharedPreferences.edit().putString(AMEX_UPDATE, data).apply();
    }

    public static String getAmexUpdate(){
        String data = sharedPreferences.getString(AMEX_UPDATE, "");
        return data;
    }

    public static void setAccess(boolean activate){
        sharedPreferences.edit().putBoolean(ACCESS, activate).apply();
    }

    public static boolean getAccess(){
        return sharedPreferences.getBoolean(ACCESS, false);
    }

    public static void setRegisterBimboIdFc(boolean activate){
        sharedPreferences.edit().putBoolean(REGISTER_BIMBO_ID_FC, activate).apply();
    }

    public static boolean getRegisterBimboIdFc(){
        return sharedPreferences.getBoolean(REGISTER_BIMBO_ID_FC, false);
    }


    /* BANDERA DEL PIN DE ACCESO */
    /*public static boolean isCloseSessionFlagEnabled(){
        String json = sharedPreferences.getString(FLAG_SESSION, "");
        return !json.isEmpty();
    }

    public static void setCloseSessionFlag(String data){
        //String json = new Gson().toJson(data);
        sharedPreferences.edit().putString(FLAG_SESSION, data).apply();
    }*/

    //RSB 20200226. Improvements. Geolocalizacion (Metodos)

    /**
     * Persiste la primer localizacion del dia obtenida
     * @param lastLocation
     */
    public static void setTodayLastLocation(LastLocation lastLocation) {
        String json = new Gson().toJson(lastLocation);
        sharedPreferences.edit().putString(TODAY_LOCATION, json).apply();
    }

    /**
     * Obtiene la primer localizacion del dia obtenida por el GPS
     */
    public static LastLocation getTodayLastLocation() {
        LastLocation lastLocation;
        String json = sharedPreferences.getString(TODAY_LOCATION, "");
        Log.d("LastLocation","Today Location: "+json);
        if(!json.isEmpty() && !json.equals("0")){
            lastLocation = new Gson().fromJson(json, LastLocation.class);
        }else{
            lastLocation = null;
        }
        return lastLocation;
    }

    /**
     * Verifica si ya existe una localizacion para el dia de hoy
     */
    public static boolean hasTodayLocation() {
        LastLocation lastLocation = getTodayLastLocation();
        if(lastLocation!=null){
            if(lastLocation.getDate().equals(Tools.getTodayDate())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si la localizacion ha sido enviada
     */
    public static boolean todayLocationIsSent() {
        LastLocation lastLocation = getTodayLastLocation();
        if(lastLocation!=null){
            Boolean alreadySent = (lastLocation.getAlreadySent()!=null ? lastLocation.getAlreadySent() : false);
            if(alreadySent) {
                return true;
            }
        }
        return false;
    }

    /**
     * Persiste la fecha de login. Se persiste tras hacer login exitoso
     * @param lastLogin
     */
    public static void setDateLastLogin(LastLogin lastLogin) {
        String json = new Gson().toJson(lastLogin);
        sharedPreferences.edit().putString(TODAY_LOGIN, json).apply();
    }

    public static LastLogin getDateLastLogin() {
        LastLogin lastLogin;
        String json = sharedPreferences.getString(TODAY_LOGIN, "");
        if(!json.isEmpty() && !json.equals("0")){
            lastLogin = new Gson().fromJson(json, LastLogin.class);
        }else{
            lastLogin = null;
        }
        return lastLogin;
    }

    /**
     * Obtiene el ultimo login exitoso, si es hoy regresa true
     */
    public static boolean isTodayLastLogin() {
        LastLogin lastLogin = getDateLastLogin();
        if(lastLogin!=null){
            if(lastLogin.getLastLoginDate().equals(Tools.getTodayDate())) {
                return true;
            }
        }
        return false;
    }

    /* BOX CUT */
    public static void setBoxCutTime(String data){
        sharedPreferences.edit().putString(BOX_CUT_TIME, data).apply();
    }

    public static String getBoxCutTime(){
        String data = sharedPreferences.getString(BOX_CUT_TIME, "");
        return data;
    }

    public static void setBoxCutDate(String data){
        sharedPreferences.edit().putString(BOX_CUT_DATE, data).apply();
    }

    public static String getBoxCutDate(){
        String data = sharedPreferences.getString(BOX_CUT_DATE, "");
        return data;
    }

    /**
     * Declara los privilegios del usuario
     * @param userPrivileges
     */
    public static void setUserPrivileges(QPAY_Privileges userPrivileges){
        String json = new Gson().toJson(userPrivileges);
        sharedPreferences.edit().putString(USER_PRIVILEGES, json).apply();
    }

    /**
     * Obtiene los privilegios del usuario
     * @return
     */
    public static QPAY_Privileges getUserPrivileges(){
        QPAY_Privileges userPrivileges = null;
        String json = sharedPreferences.getString(USER_PRIVILEGES, "");
        if(!json.isEmpty()){
            userPrivileges = new Gson().fromJson(json, QPAY_Privileges.class);
        }else{
            userPrivileges = null;
        }
        return userPrivileges;
    }

    /**
     * Revisa si el rol es Operador/Cajero
     * @return
     */
    public static boolean isCashier(){
        String json = sharedPreferences.getString(USER_PROFILE, "");
        if(!json.isEmpty()){
            QPAY_UserProfile userProfile = new Gson().fromJson(json, QPAY_UserProfile.class);
            String rol = userProfile.getQpay_object()[0].getQpay_user_type();
            rol = (rol != null ? rol.trim() : "");

            if(rol.compareTo(Globals.ROL_CAJERO)==0) {
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * Revisa si el rol es Administrador/Patron
     * @return
     */
    public static boolean isAdmin(){
        String json = sharedPreferences.getString(USER_PROFILE, "");
        if(!json.isEmpty()){
            QPAY_UserProfile userProfile = new Gson().fromJson(json, QPAY_UserProfile.class);
            String rol = userProfile.getQpay_object()[0].getQpay_user_type();
            rol = (rol != null ? rol.trim() : "");

            if(rol.compareTo(Globals.ROL_PATRON)==0) {
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }

    //20200407 RSB. Multiuser - Se modifica para persistir el mensaje que mostrará al cerrar sesión
    public static void setFCM(String data){
        sharedPreferences.edit().putString(FCM_TOKEN, data).apply();
    }

    //20200407 RSB. Multiuser - Se modifica para obtener el mensaje que mostrará al cerrar sesión
    public static String getFCM(){
        String data = sharedPreferences.getString(FCM_TOKEN, "");
        return data;
    }

    /*FINANCIAL VAS*/
    public static void setFinancialVasAmounts(QPAY_FinancialVasGetAmountsResponse response){
        String json = new Gson().toJson(response);
        sharedPreferences.edit().putString(FINANCIAL_VAS_AMOUNTS, json).apply();
    }

    public static QPAY_FinancialVasGetAmountsResponse getFinancialVasAmounts(){
        QPAY_FinancialVasGetAmountsResponse response = null;
        String defalutValue = "{\"qpay_response\": \"true\",\"qpay_code\": \"000\",\"qpay_description\":\"Aprobada\",\"qpay_object\":[[{\"category\": \"VAS Limites\",\"parameter\": \"doscientos\",\"value\": \"200\"},{\"category\": \"VAS Limites\",\"parameter\": \"cien\",\"value\": \"100\"},{\"category\": \"VAS Limites\",\"parameter\": \"cincuenta\",\"value\": \"50\"},{\"category\": \"VAS Limites\",\"parameter\": \"veinte\",\"value\": \"20\"},{\"category\": \"VAS Limites\",\"parameter\": \"cero\",\"value\": \"0\"}]]}";
        String json = sharedPreferences.getString(FINANCIAL_VAS_AMOUNTS, "");
        if(!json.isEmpty()){
            response = new Gson().fromJson(json
                    , QPAY_FinancialVasGetAmountsResponse.class);
        }else{
            response = new Gson().fromJson(defalutValue
                    , QPAY_FinancialVasGetAmountsResponse.class);
        }
        return response;
    }

    public static void setFinancialVasInfo(QPAY_VasInfo data){
        String json = new Gson().toJson(data);
        sharedPreferences.edit().putString(FINANCIAL_VAS_INFO, json).apply();
    }

    public static QPAY_VasInfo getFinancialVasInfo(){
        QPAY_VasInfo response = null;
        String json = sharedPreferences.getString(FINANCIAL_VAS_INFO, "");
        if(!json.isEmpty()){
            response = new Gson().fromJson(json, QPAY_VasInfo.class);
        }else{
            response = new QPAY_VasInfo(true);
            setFinancialVasInfo(response);
        }
        return response;
    }
    /*FINANCIAL VAS*/

    //COUNTER
    public static String getCounter(){

        String back = "";
        int counter = 0;
        String json = sharedPreferences.getString(COUNTER, "");
        if(!json.isEmpty()){
            back = String.format("%06d", Integer.parseInt(json));
            counter = Integer.parseInt(json) + 1;
        }else{
            back = String.format("%06d", 1);
            counter = 2;
        }

        sharedPreferences.edit().putString(COUNTER, "" + counter).apply();

        return back;
    }


    //20200713 RSB. Banderas para activar transacciones locales TAE.
    public static void setLocalTxnTae(boolean data){
        sharedPreferences.edit().putBoolean(LOCAL_TXN_TAE, data).apply();
    }

    public static boolean getLocalTxnTae(){
        return sharedPreferences.getBoolean(LOCAL_TXN_TAE,false);
    }

    //20200713 RSB. Banderas para activar transacciones locales Servicios.
    public static void setLocalTxnService(boolean data){
        sharedPreferences.edit().putBoolean(LOCAL_TXN_SERVICE, data).apply();
    }

    public static boolean getLocalTxnService(){
        return sharedPreferences.getBoolean(LOCAL_TXN_SERVICE,false);
    }

    //20200713 RSB. Banderas para activar transacciones locales Financieras.
    public static void setLocalTxnFinancial(boolean data){
        sharedPreferences.edit().putBoolean(LOCAL_TXN_FINANCIAL, data).apply();
    }

    public static boolean getLocalTxnFinancial(){
        return sharedPreferences.getBoolean(LOCAL_TXN_FINANCIAL,false);
    }

    //20200713 RSB. Banderas para activar transacciones locales PagosConecta.
    public static void setLocalTxnQiubo(boolean data){
        sharedPreferences.edit().putBoolean(LOCAL_TXN_QIUBO, data).apply();
    }

    public static boolean getLocalTxnQiubo(){
        return sharedPreferences.getBoolean(LOCAL_TXN_QIUBO,false);
    }

    //20200713 RSB. Bandera para activar transacciones locales tras actualizar la app.
    public static void setLocalTxnUpdate(boolean data){
        sharedPreferences.edit().putBoolean(LOCAL_TXN_UPDATE, data).apply();
    }

    public static boolean getLocalTxnUpdate(){
        return sharedPreferences.getBoolean(LOCAL_TXN_UPDATE,false);
    }

    //20200728 RSB. RateApp. Banderas para calificar el App
    /**
     * Bandera que en true muestra el dialogo para calificar el app
     * @param data
     */
    public static void setFlagRateApp(boolean data){
        sharedPreferences.edit().putBoolean(FLAG_RATE_APP, data).apply();
    }

    public static boolean getFlagRateApp(){
        return sharedPreferences.getBoolean(FLAG_RATE_APP,false);
    }

    /**
     * Contador de transacciones correctas para mostrar dialogo de calificacion
     * @param data
     */
    public static void setCountTransToRate(int data){
        sharedPreferences.edit().putInt(COUNT_TRANS_TO_RATE, data).apply();
    }

    public static int getCountTransToRate(){
        /*
            Por default Se coloca un numero mayor a Globals.TRANSACTIONS_TO_RATE_APP porque se desea esta funcionalidad
            inicialmente solo sirva para nuevos registros, por lo cual en el registro básico CountTransToRate se dejara en 0
            de tal forma que solo aplique para usuarios de nuevo registro
         */
        return sharedPreferences.getInt(COUNT_TRANS_TO_RATE,Globals.TRANSACTIONS_TO_RATE_APP+1);
    }

    public static void setCartList(List<NewOrder> cartList){
        Gson gson = new Gson();
        String jsonString = gson.toJson(cartList);

        sharedPreferences.edit().putString(CART_LIST, jsonString).apply();
    }

    public static List<NewOrder> readCartList(){
        String jsonString = sharedPreferences.getString(CART_LIST, "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<NewOrder>>() {}.getType();

        return gson.fromJson(jsonString, type);
    }

    //20200811 RSB. Validar Pidelo
    /**
     * Validar activación de provee
     * @return
     */
    public static boolean isPideloActive() {

        String json = sharedPreferences.getString(USER_PROFILE, "");
        if(!json.isEmpty()){
            QPAY_UserProfile userProfile = new Gson().fromJson(json, QPAY_UserProfile.class);
            String pidelo = userProfile.getQpay_object()[0].getQpay_activation_pidelo();
            pidelo = (pidelo != null ? pidelo.trim() : "");

            if(pidelo.compareTo("1")==0) {
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }

    }

    //20200813 RSB. Validar VAS con Financiero
    /**
     * Validar activación de VAS
     * @return
     */
    public static boolean isVASFinancieroActive() {

        String json = sharedPreferences.getString(USER_PROFILE, "");
        if(!json.isEmpty()){
            QPAY_UserProfile userProfile = new Gson().fromJson(json, QPAY_UserProfile.class);
            String vas = userProfile.getQpay_object()[0].getQpay_activation_vas_saldo();
            vas = (vas != null ? vas.trim() : "");

            if(vas.compareTo("1")==0) {
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }

    }


    public static void setIsRegisteredEnko(boolean isRegisteredEnko){
        sharedPreferences.edit().putBoolean(ENKO, isRegisteredEnko).apply();
    }

    public static boolean getIsRegisteredEnko(){
        return sharedPreferences.getBoolean(ENKO,false);
    }

    //SWAP T1000
    public static void setSwapT1000(SwapData data){
        String json = null;
        if(data!=null) {
            json = new Gson().toJson(data);
        } else {
            json = "";
        }
        sharedPreferences.edit().putString(SWAP_T1000, json).apply();
    }

    public static boolean isSwapT1000(){
        String json = sharedPreferences.getString(SWAP_T1000, "");
        if(!json.isEmpty()){
            return true;
        } else {
            return false;
        }
    }

    public static SwapData getSwapT1000(){
        SwapData swapData = null;
        String json = sharedPreferences.getString(SWAP_T1000,"");
        if(!json.isEmpty()){
            swapData = new Gson().fromJson(json, SwapData.class);
        }
        return swapData;
    }

    //Multidevice T1000
    public static void setLinkT1000(QPAY_LinkT1000Request data){
        String json = null;
        if(data!=null) {
            json = new Gson().toJson(data);
        } else {
            json = "";
        }
        sharedPreferences.edit().putString(LINK_T1000, json).apply();
    }

    public static boolean isLinkT1000(){
        String json = sharedPreferences.getString(LINK_T1000, "");
        if(!json.isEmpty()){
            return true;
        } else {
            return false;
        }
    }

    public static QPAY_LinkT1000Request getLinkT1000(){
        QPAY_LinkT1000Request linkT1000Request = null;
        String json = sharedPreferences.getString(LINK_T1000,"");
        if(!json.isEmpty()){
            linkT1000Request = new Gson().fromJson(json, QPAY_LinkT1000Request.class);
        }
        return linkT1000Request;
    }

    public static boolean getAnalytics_activation() {

        if((AppPreferences.getUserProfile() != null && AppPreferences.getUserProfile().getQpay_object()[0].getAnalytics_activation() != null &&
                !AppPreferences.getUserProfile().getQpay_object()[0].getAnalytics_activation().isEmpty() &&
                "1".equals(AppPreferences.getUserProfile().getQpay_object()[0].getAnalytics_activation()))) {
            return true;
        } else {
            return false;
        }
    }

    //REMESAS
    public static void addRemesasErrorCounter() {
        sharedPreferences.edit().putString(REMESAS_ERROR_COUNTER, ""+(Integer.parseInt(getRemesasErrorCounter())+1)).apply();
    }

    public static void resetRemesasErrorCounter() {
        sharedPreferences.edit().putString(REMESAS_ERROR_COUNTER, "0").apply();
    }

    private static String getRemesasErrorCounter(){
        String json = sharedPreferences.getString(REMESAS_ERROR_COUNTER, "");
        if(json.isEmpty()){
            return "0";
        } else {
            return json;
        }
    }

    public static boolean isRemesasModuleBlocked(){
        String json = sharedPreferences.getString(REMESAS_ERROR_COUNTER, "");
        if(json.isEmpty()){
            return false;
        } else {
            if(Integer.parseInt(json) >= 5)
                return true;
            else
                return false;
        }
    }

    public static void setDateRemesasModuleBlocket(String date) {
        sharedPreferences.edit().putString(REMESAS_BLOCKED_DATE, date).apply();
    }

    public static String getDateRemesasModuleBlocket(){
        String json = sharedPreferences.getString(REMESAS_BLOCKED_DATE, "");
        if(json.isEmpty()){
            return "";
        } else {
            return json;
        }
    }


    public static void setChambitasCounter(String data){
        sharedPreferences.edit().putString(CHAMBITAS_COUNTER, data).apply();
    }

    public static String getChambitasCounter(){
        String data = sharedPreferences.getString(CHAMBITAS_COUNTER, "");
        return data;
    }

    public static void setChambitasCompletedCounter(String data){
        sharedPreferences.edit().putString(CHAMBITAS_COMPLETED_COUNTER, data).apply();
    }

    public static String getChambitasCompletedCounter(){
        String data = sharedPreferences.getString(CHAMBITAS_COMPLETED_COUNTER, "");
        return data;
    }

    public static void setChambitasCurrentCounter(String data){
        sharedPreferences.edit().putString(CHAMBITAS_CURRENT_COUNTER, data).apply();
    }

    public static String getChambitasCurrentCounter(){
        String data = sharedPreferences.getString(CHAMBITAS_CURRENT_COUNTER, "");
        return data;
    }

    public static void setPublicidadCounter(String data) {
        sharedPreferences.edit().putString(PUBLICIDAD_COUNTER, data).apply();
    }

    public static String getPublicidadCounter() {
        String data = sharedPreferences.getString(PUBLICIDAD_COUNTER, "");
        return data;
    }

    public static void savePublicity(QPayBaseResponse responseObjetct) {
        if (responseObjetct == null || responseObjetct.getQpayObject() == null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("tconecta.objectSize", 0);
            editor.commit();
        } else {
            int objectSize = responseObjetct.getQpayObject().size();
            for (int i = 0; i < objectSize; i++) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("tconecta.objectSize", objectSize);
                editor.putInt("tconecta.Id"+i, responseObjetct.getQpayObject().get(i).getId());
                editor.putString("tconecta.status"+i, responseObjetct.getQpayObject().get(i).getStatus());
                editor.putString("tconecta.name"+i, responseObjetct.getQpayObject().get(i).getName());
                editor.putString("tconecta.activationDate"+i, responseObjetct.getQpayObject().get(i).getActivationDate());
                editor.putString("tconecta.expirationDate"+i, responseObjetct.getQpayObject().get(i).getExpirationDate());
                int publicitySize = responseObjetct.getQpayObject().get(i).getPublicities().size();
                for (int j = 0; j < publicitySize; j++) {
                    editor.putInt("tconecta.publicitySize"+i, publicitySize);
                    editor.putInt("tconecta.publicityId"+j+":"+i, responseObjetct.getQpayObject().get(i).getPublicities().get(j).getId());
                    editor.putString("tconecta.title"+j+":"+i, responseObjetct.getQpayObject().get(i).getPublicities().get(j).getTitle());
                    editor.putString("tconecta.image"+j+":"+i, responseObjetct.getQpayObject().get(i).getPublicities().get(j).getImage());
                    editor.putString("tconecta.carrouselImage"+j+":"+i, responseObjetct.getQpayObject().get(i).getPublicities().get(j).getCarrouselImage());
                    editor.putString("tconecta.buttonText"+j+":"+i, responseObjetct.getQpayObject().get(i).getPublicities().get(j).getButtonText());
                    editor.putString("tconecta.description"+j+":"+i, responseObjetct.getQpayObject().get(i).getPublicities().get(j).getDescription());
                    editor.putString("tconecta.link"+j+":"+i, responseObjetct.getQpayObject().get(i).getPublicities().get(j).getLink());
                    editor.putString("tconecta.creationDate"+j+":"+i, responseObjetct.getQpayObject().get(i).getPublicities().get(j).getCreationDate());
                    editor.putInt("tconecta.companyId"+j+":"+i, responseObjetct.getQpayObject().get(i).getPublicities().get(j).getCompanyId());
                }
                editor.commit();
            }
        }
    }

    public static ArrayList<PublicityResponse> readPublicity(Context context) {
        ArrayList<PublicityResponse> publicityResponses = new ArrayList<>();
        LocalStorage localStorage = new LocalStorage(context);
        Integer objectSize = localStorage.getLocalInt(Globals.TAG_PREFERENCES, "tconecta.objectSize");
        for (int i = 0; i < objectSize; i++) {
            Integer id = localStorage.getLocalInt(Globals.TAG_PREFERENCES, "tconecta.Id"+i);
            String status = localStorage.getLocalString(Globals.TAG_PREFERENCES, "tconecta.status"+i);
            String name = localStorage.getLocalString(Globals.TAG_PREFERENCES, "tconecta.name"+i);
            String activationDate = localStorage.getLocalString(Globals.TAG_PREFERENCES, "tconecta.activationDate"+i);
            String expirationDate = localStorage.getLocalString(Globals.TAG_PREFERENCES, "tconecta.expirationDate"+i);
            Integer publicitySize = localStorage.getLocalInt(Globals.TAG_PREFERENCES, "tconecta.publicitySize"+i);
            for (int j = 0; j < publicitySize; j++) {
                Integer publicityId = localStorage.getLocalInt(Globals.TAG_PREFERENCES, "tconecta.publicityId"+j+":"+i);
                String title = localStorage.getLocalString(Globals.TAG_PREFERENCES, "tconecta.title"+j+":"+i);
                String image = localStorage.getLocalString(Globals.TAG_PREFERENCES, "tconecta.image"+j+":"+i);
                String carrouselImage = localStorage.getLocalString(Globals.TAG_PREFERENCES, "tconecta.carrouselImage"+j+":"+i);
                String buttonText = localStorage.getLocalString(Globals.TAG_PREFERENCES, "tconecta.buttonText"+j+":"+i);
                String description = localStorage.getLocalString(Globals.TAG_PREFERENCES, "tconecta.description"+j+":"+i);
                String link = localStorage.getLocalString(Globals.TAG_PREFERENCES, "tconecta.link"+j+":"+i);
                String creationDate = localStorage.getLocalString(Globals.TAG_PREFERENCES, "tconecta.creationDate"+j+":"+i);
                Integer companyId = localStorage.getLocalInt(Globals.TAG_PREFERENCES, "tconecta.companyId"+j+":"+i);
                PublicityResponse publicityResponse = new PublicityResponse(publicityId, title, image, carrouselImage, buttonText,
                        description, link, creationDate, companyId, id);
                publicityResponses.add(publicityResponse);
            }
        }
        return publicityResponses;
    }

    public static void saveTodayDate(ArrayList<Integer> todayDate) {
        int counter = 0;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("tconecta.year", todayDate.get(counter++));
        editor.putInt("tconecta.month", todayDate.get(counter++));
        editor.putInt("tconecta.day", todayDate.get(counter));
        editor.commit();
    }

    public static ArrayList<Integer> readDate(Context context) {
        ArrayList<Integer> todayDate = new ArrayList<>();
        LocalStorage localStorage = new LocalStorage(context);
        int year = localStorage.getLocalInt(Globals.TAG_PREFERENCES, "tconecta.year");
        int month = localStorage.getLocalInt(Globals.TAG_PREFERENCES, "tconecta.month");
        int day = localStorage.getLocalInt(Globals.TAG_PREFERENCES, "tconecta.day");
        todayDate.add(year);
        todayDate.add(month);
        todayDate.add(day);
        return todayDate;
    }

    public static void saveCampaignId(int campaignId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("tconecta.campaignId", campaignId);
        editor.commit();
    }

    public static int readCampaignId(Context context) {
        LocalStorage localStorage = new LocalStorage(context);
        return localStorage.getLocalInt(Globals.TAG_PREFERENCES, "tconecta.campaignId");
    }

    public static void setRememberUser(Boolean status){
        sharedPreferences.edit().putBoolean(REMEMBER_USER, status).apply();
    }

    public static boolean isRememberUser(){
        return sharedPreferences.getBoolean(REMEMBER_USER, false);
    }

    public static void setAppLink(@Globals.AppLink String data){
        sharedPreferences.edit().putString(APPLINK, data).apply();
    }

    public static String getAppLink(){
        String data = sharedPreferences.getString(APPLINK, "");
        return data;
    }

    public static void saveNewOrder(String newOrderId, String orderType) {
        ArrayList<OrderNotification> orders = readNewOrders();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (orders.isEmpty()) {
            editor.putInt(NEW_ORDER_COUNTER, 1);
            editor.putString(NEW_ORDER_ID + 0, newOrderId);
            editor.putString(ORDER_TYPE + 0, orderType);
        } else {
            editor.putInt(NEW_ORDER_COUNTER, orders.size() + 1);
            editor.putString(NEW_ORDER_ID + orders.size(), newOrderId);
            editor.putString(ORDER_TYPE + orders.size(), orderType);
        }
        editor.commit();
    }

    public static ArrayList<OrderNotification> readNewOrders() {
        ArrayList<OrderNotification> newOrders = new ArrayList<>();
        int newOrderCounter = sharedPreferences.getInt(NEW_ORDER_COUNTER, -1);
        if (newOrderCounter >= 0) {
            for (int i = 0; i < newOrderCounter; i++) {
                int item = newOrderCounter - 1;
                String newOrderId = sharedPreferences.getString(NEW_ORDER_ID + item, "");
                String orderType = sharedPreferences.getString(ORDER_TYPE + item, "");
                newOrders.add(new OrderNotification(newOrderId, orderType) );
            }
        }
        return newOrders;
    }

    public static void removeOrders() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(NEW_ORDER_COUNTER, 0);
        editor.putString(NEW_ORDER_ID + 0, "");
        editor.putString(ORDER_TYPE + 0, "");
        editor.commit();
    }

    public static void setStoreLocation(LastLocation lastLocation) {
        String json = new Gson().toJson(lastLocation);
        sharedPreferences.edit().putString(STORE_LOCATION, json).apply();
    }

    public static LastLocation getStoreLocation() {
        LastLocation lastLocation;
        String json = sharedPreferences.getString(STORE_LOCATION, "");
        if(!json.isEmpty() && !json.equals("0")){
            lastLocation = new Gson().fromJson(json, LastLocation.class);
        }else{
            lastLocation = null;
        }
        return lastLocation;
    }

    /**
     *
     *
     */
    public static void setTodayTransactions(TransactionsModel transactions) {
        sharedPreferences.edit().putString(TODAY_TRANSACTIONS, new Gson().toJson(transactions)).apply();
    }

    public static TransactionsModel getTodayTransactions() {
        TransactionsModel transactionsModel = new TransactionsModel();
        String json = sharedPreferences.getString(TODAY_TRANSACTIONS, "");
        if(!json.isEmpty())
            transactionsModel = new Gson().fromJson(json, TransactionsModel.class);
        return transactionsModel;
    }

    public static void setYesterdayTransactions(TransactionsModel transactions) {
        sharedPreferences.edit().putString(YESTERDAY_TRANSACTIONS, new Gson().toJson(transactions)).apply();
    }

    public static TransactionsModel getYesterdayTransactions() {
        TransactionsModel transactionsModel = new TransactionsModel();
        String json = sharedPreferences.getString(YESTERDAY_TRANSACTIONS, "");
        if(!json.isEmpty())
            transactionsModel = new Gson().fromJson(json, TransactionsModel.class);
        return transactionsModel;
    }
}
