package com.blm.qiubopay.tools;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.blm.qiubopay.BuildConfig;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.models.financial_vas.QPAY_FinancialVasAmounts;
import com.blm.qiubopay.models.financial_vas.QPAY_FinancialVasGetAmountsResponse;
import com.blm.qiubopay.models.financial_vas.QPAY_VasInfo;
import com.blm.qiubopay.CApplication;
import com.blm.qiubopay.utils.Globals;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Tools {

    public static boolean isConectionMitError(String s){
        boolean back = false;
        if (s != null) {
            if (s.contains("java.net.UnknownHostException")
                    || s.contains("Empty apiKey")
                    || s.contains("javax.net.ssl"))
                back = true;
        }
        return back;
    }

    public static String encodeSHA256(String data) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static String getHashSha256(byte[] b) {
        try {
            String hexHash = DigestUtils.sha256Hex(b);
            return hexHash;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getVersion(){
        String versionName = "";// = BuildConfig.VERSION_NAME;
        try{
            versionName = BuildConfig.VERSION_NAME;
        }catch (Exception exp){
            Log.e("Error", exp.getMessage());
        }

        return Globals.VERSION;
    }

    /*public static String getUUID(Context context){
        String back = "";
        try{
            if(Build.VERSION.SDK_INT >= 26) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    back = Build.getSerial();
                }
            }
            else
                back = Build.SERIAL;
        }catch (Exception exp){
            Log.e("Error", exp.getMessage());
        }
        return back;
    }*/

    public static String getUUID(){
        String id = "";
        try{
            if(Build.VERSION.SDK_INT >= 26) {
                if (ContextCompat.checkSelfPermission(CApplication.getAppContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    id = Build.getSerial();
                }
            }
            else {
                id = Build.SERIAL;
            }
        }catch (Exception exp){
            Log.e("Error", exp.getMessage());
            id = Settings.Secure.getString(CApplication.getAppContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        if(id.equalsIgnoreCase("unknown") || id.isEmpty() || id == null) {
            id = Settings.Secure.getString(CApplication.getAppContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        //Si aun asi el android id llegará a ser nulo de plano
        if (id == null || id.isEmpty()) { id="unknown"; }

        return id;
    }

    public static String getModel(){
        String back = "";
        try{
            back = Build.MODEL;
        }catch (Exception exp){
            Log.e("Error", exp.getMessage());
        }
        return back.trim();
    }

    public static String getManufacter(){
        String back = "";
        try{
            back = Build.MANUFACTURER;
        }catch (Exception exp){
            Log.e("Error", exp.getMessage());
        }
        return back;
    }

    public static String getAPI(){
        String back = "";
        try{
            back = "" + Build.VERSION.SDK_INT;
        }catch (Exception exp){
            Log.e("Error", exp.getMessage());
        }
        return back;
    }

    public static String getOsVersion(){
        String back = "";
        try{
            back = "" + Build.VERSION.RELEASE;
        }catch (Exception exp){
            Log.e("Error", exp.getMessage());
        }
        return back;
    }

    public static String getIccId(Context context){
        String iccid;
        try {
            TelephonyManager telMngr = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
            iccid = telMngr.getSimSerialNumber();

            if (iccid==null){
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    SubscriptionManager sm = SubscriptionManager.from(context);
                    List<SubscriptionInfo> sis = sm.getActiveSubscriptionInfoList();
                    SubscriptionInfo si = sis.get(0);
                    iccid = si.getIccId();
                } else {
                    iccid = "NA";
                }
            }
            //data.getQpay_device_info()[0].setQpay_icc_id(iccid);

        } catch (SecurityException e) {
            iccid = "ND";
        } catch (Exception e ) {
            iccid = "NA";
        }

        return iccid;
    }

    public static String getFormatDate(int day, int month, int year){
        String d,m;
        String date;
        if((""+day).length() == 1)
            d="0"+day;
        else
            d=""+day;

        if((""+month).length() == 1)
            m="0"+month;
        else
            m=""+month;

        //date = d+"/"+m+"/"+year;
        date = year+"-"+m+"-"+d;

        Log.i("SchedulesActivity", date);

        return date;
    }

    public static String hexStringToString(String s){
        String s1 = "";
        for(int i = 0; i < (s.length()/2);i++){
            int j = i * 2;
            int k = Integer.parseInt(s.substring(j, j + 2), 16);
            s1 = s1 + (char)k;
        }
        return s1;
    }

    public static String StringToHexString(String s){
        StringBuffer stringbuffer = new StringBuffer(s.length() * 2);
        for(int i = 0; i < s.length(); i++){
            int j = s.charAt(i) & 0xff;
            if(j < 16)
                stringbuffer.append('0');
            stringbuffer.append(Integer.toHexString(j));
        }
        return stringbuffer.toString().toUpperCase();
    }

    public static String getOnlyNumbers(String data)
    {
        String back = "";
        try {
            String numberRefined = data.replaceAll("[^0-9\\.]", "");
            back = numberRefined;
        }catch (Exception e){
            back = "";
        }
        return back;
    }

    public static String getOnlyNumbersAndLetters(String data)
    {
        String back = "";
        try {
            String numberRefined = data.replaceAll("[^a-zA-Z0-9\\s]", "");
            back = numberRefined;
        }catch (Exception e){
            back = "";
        }
        return back;
    }

    public static String getFormattedLine(String title, String value){
        String back = "";
        String whiteSpaces = "";
        int spaces;
        if((title.trim().length() + value.trim().length()) >= 27)
        {
            back = title.trim() + " " + value.trim();
        }
        else
        {
            spaces = 27 - (title.trim().length() + value.trim().length());
            back = title.trim() + getWhiteSpaces(spaces) + value.trim();
        }
        return back;
    }

    private static String getWhiteSpaces(int number){
        String back = "";
        for(int i=0; i<=number; i++)
            back += " ";
        return back;
    }

    public static String getPaddingCharacter(int number, String character)
    {
        String back = "";
        for(int i=0; i<number; i++)
            back += character;
        return back;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String stringToHexString(String s){
        StringBuffer stringbuffer = new StringBuffer(s.length() * 2);
        for(int i = 0; i < s.length(); i++){
            int j = s.charAt(i) & 0xff;
            if(j < 16)
                stringbuffer.append('0');
            stringbuffer.append(Integer.toHexString(j));
        }
        return stringbuffer.toString().toUpperCase();
    }

    public static String leftPad(String str, int size, char padChar) {
        StringBuilder padded = new StringBuilder(str == null ? "" : str);
        while (padded.length() < size) {
            padded.insert(0, padChar);
        }
        return padded.toString();
    }

    public static String getReference(){
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String format = s.format(new Date());
        return format;
    }

    public static String getTagWithFormat(String tag, String value){
        String back = "";
        back += tag + getSize(value) + value;
        return back;
    }

    private static String getSize(String value){
        String s = String.format("%X", value.length()/2);
        if(s.length()==1)
            s="0"+s;
        return s;
    }

    public static boolean isN3Terminal(){
        boolean back = false;

        if(getModel().trim().equals("N5")
                || getModel().trim().equals("N3")
                || getModel().trim().equals("N86"))
            back = true;

        return back;
    }

    public static boolean isOnlyN3Terminal(){
        boolean back = false;

        if(getModel().trim().equals("N5")
                || getModel().trim().equals("N3"))
            back = true;

        return back;
    }

    public static String getCardHolderName(String tk1){
        String back = "";
        String[] track1;
        if(tk1 != null){
            if(tk1.contains("^")){
                track1 = tk1.split("\\^");
                if(track1.length == 3){
                    if(!track1[1].trim().equals(""))
                        back = track1[1].trim();
                    else
                        return "CLIENTE";
                }
                else
                    return "CLIENTE";
            }
            else
                return "CLIENTE";
        }
        else
            return "CLIENTE";

        return back;
    }

    public static String getMonto(String monto) {

        monto = monto.replace(",",".");
        String[] montoArray = monto.split("\\.");
        if(montoArray.length>1){
            //Significa que es decimal, se deja pasar tal cual
        } else {
            //Significa que es base 100, pero en lugar de quitar los ultimos dos dígitos ahora
            //le agregaremos un punto a la cadena y así respetamos CFE
            if(monto.length()<3){
                monto = String.format("%03d", Integer.valueOf(monto));
            }
            StringBuffer str = new StringBuffer(monto);
            str.insert(monto.length()-2,".");
            monto = str.toString();

        }

        return monto;

    }

    public static String getTodayDate() {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        return currentDate;
    }

    public static String getTodayDate(String pattern) {
        String currentDate = new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date());
        return currentDate;
    }

    public static String getTodayDateForTipDetail() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return currentDate;
    }

    public static String getTodayHour() {
        String currentDate = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        return currentDate;
    }

    public static String getTodaySqliteFormatDate() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return currentDate;
    }

    public static String getTodayDateWithMoreOrMinusDays(int days) {
        Date date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        //Number of Days to add
        c.add(Calendar.DAY_OF_MONTH, days);
        //Date after adding the days to the given date
        //String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(c.getTime());
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(c.getTime());
        //Displaying the new Date after addition of Days
        System.out.println("Date after Addition: " + currentDate);

        return currentDate;
    }

    public static String getTodayDateWithMoreOrMinusDays(int days, String pattern) {
        Date date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        //Number of Days to add
        c.add(Calendar.DAY_OF_MONTH, days);
        //Date after adding the days to the given date
        //String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(c.getTime());
        String currentDate = new SimpleDateFormat(pattern, Locale.getDefault()).format(c.getTime());
        //Displaying the new Date after addition of Days
        System.out.println("Date after Addition: " + currentDate);

        return currentDate;
    }

    public static String getDbDateFormat(String dateString){
        //04-05-2020 22:15:13 CreatedAt Format
        String newDateString = "";

        SimpleDateFormat format_1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        if(dateString.contains("T") || dateString.contains("+"))
        {
            dateString = dateString.replace("T", " ");
            dateString = dateString.replace(".000+0000", "");

            format_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

        SimpleDateFormat format_2 = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = format_1.parse(dateString);
            System.out.println(date);
            newDateString = format_2.format(date);
        } catch (ParseException e) {
            newDateString = null;
        }

        if(newDateString == null)
            newDateString = getTodayDate();

        return newDateString;
    }

    public static String getSqliteDateFormat(String dateString){
        //04-05-2020 22:15:13 CreatedAt Format
        String newDateString = "";

        SimpleDateFormat format_1 = new SimpleDateFormat("dd-MM-yyyy");

        SimpleDateFormat format_2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format_1.parse(dateString);
            System.out.println(date);
            newDateString = format_2.format(date);
        } catch (ParseException e) {
            newDateString = null;
        }

        if(newDateString == null)
            newDateString = getTodaySqliteFormatDate();

        return newDateString;
    }

    public static String getTagWithOutRPadding(String tag, String value, boolean tagIncluded){

        String pad = value;//"000000000000000002011E041E031F020000000000000000";
        char[] array = pad.toCharArray();
        String padding= "";

        for(int i = array.length-1; i>=0; i--){
            if(array[i] != '0'){
                padding = pad.substring(0, i+1);
                break;
            }
        }

        if(padding.length() % 2 != 0)
            padding += "0";

        System.out.println(padding);

        if(tagIncluded)
            padding = tag + getTagSize(padding) + padding;

        return padding;
    }

    private static String getTagSize(String value){
        String s = String.format("%X", value.length()/2);
        if(s.length()==1)
            s="0"+s;
        return s;
    }

    public static void setVasInfo(Boolean defaultValues
            ,Boolean weNeedToAsk
            ,Double balance
            ,Boolean updateAmountsToday){
        if(AppPreferences.isCashier())
            return;

        QPAY_VasInfo vasInfo;
        if(defaultValues)
            vasInfo = new QPAY_VasInfo(true);
        else
            vasInfo = AppPreferences.getFinancialVasInfo();

        if(weNeedToAsk)
            vasInfo.setUpdateBalanceFlag("1");
        else
            vasInfo.setUpdateBalanceFlag("0");

        if(balance > vasInfo.getBalance()){
            //Se recargó el saldo de la bolsa y se reinician los contadores de los montos mínimos.
            vasInfo.setAmounts(AppPreferences.getFinancialVasAmounts().getQpay_object());
        }

        if(balance > 0)
            vasInfo.setBalance(balance);

        if(updateAmountsToday)
            vasInfo.setUpdateVasAmountsToday("1");

        AppPreferences.setFinancialVasInfo(vasInfo);
    }

    public static void setAlreadyUsedFinancialVasToday()
    {
        QPAY_VasInfo vasInfo;
        vasInfo = AppPreferences.getFinancialVasInfo();

        vasInfo.setAlreadyUsedVasToday("1");
        //vasInfo.setCurrentDay(getTodayDate());

        AppPreferences.setFinancialVasInfo(vasInfo);
    }

    public static Boolean isFinancialVasUsedToday(){
        Boolean back = false;
        QPAY_VasInfo vasInfo = AppPreferences.getFinancialVasInfo();

        if(vasInfo.getCurrentDay().equals(getTodayDate()))
            if(vasInfo.getAlreadyUsedVasToday().equals("1"))
                back = true;

        return back;
    }

    public static void initialVasInfo(){

        if(!AppPreferences.getFinancialVasInfo().getCurrentDay().equals(getTodayDate())){
            setVasInfo(true, false, -1.0, false);
        }

    }

    public static Boolean isNeedToOfferFinancialVasWithTheNewBalance(){
        Boolean back = false;

        try {
            QPAY_VasInfo vasInfo = AppPreferences.getFinancialVasInfo();
            QPAY_FinancialVasAmounts[][] temporalAmounts = vasInfo.getAmounts();

            if (null != vasInfo.getAmounts() && vasInfo.getAmounts()[0].length > 0) {


                for (int i = 0; i < temporalAmounts[0].length; i++) {
                    if (null == temporalAmounts[0][i].getFlag() || !temporalAmounts[0][i].getFlag().equals("1")) {
                        //No se ha preguntado por este monto
                        if (vasInfo.getBalance() <= Double.parseDouble(temporalAmounts[0][i].getValue())) {
                            //Se pregunta por este monto y se guarda el valor
                            back = true;
                            temporalAmounts[0][i].setFlag("1");

                            vasInfo.setAmounts(temporalAmounts);
                            AppPreferences.setFinancialVasInfo(vasInfo);
                        }
                    }
                }
            }
        }catch (Exception e){
            Log.e("FINANCIAL VAS", "Error en la validación de montos");
        }


        return back;
    }

    public static void setFinancialVasAmounts(QPAY_FinancialVasGetAmountsResponse amounts){
        QPAY_VasInfo vasInfo = AppPreferences.getFinancialVasInfo();

        vasInfo.setAmounts(amounts.getQpay_object());

        AppPreferences.setFinancialVasInfo(vasInfo);
        AppPreferences.setFinancialVasAmounts(amounts);

    }

    public static Boolean userIsOnlyVAS(){
        return !"1".equals(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_activation_3()) && !"1".equals(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_activation_7());
    }

    public static Boolean userIsFinancial(){
        return !("0".equals(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_activation_3()) &&
                !"1".equals(AppPreferences.getUserProfile().getQpay_object()[0].getQpay_activation_7()));
    }

    public static Boolean chekIsTheSameApp(final List<String> list){
        Boolean back = true;
        String app = list.get(0);

        for(int i=1; i<list.size(); i++)
        {
            if(!app.equals(list.get(i)))
                back = false;
        }

        return back;
    }

    public static List<String> delAidsInApp(final List<String> list) {

        List<String> newList = new ArrayList<>();

        for(int i=0; i<list.size(); i++)
        {
            if(!list.get(i).contains("000"))
                newList.add(list.get(i));
        }

        return newList;
    }

    public static int getAppIndex(final List<String> list, String name) {
        int index = 0;

        //printList("LISTA DE BUSQUEDA", list);
        //Log.d("N3 ISSUE", "Cadena de búsqueda: " + name);

        //for(int i=0; i<list.size(); i++)
        for(int i=(list.size()-1); i>=0; i--)
        {
            if(list.get(i).equals(name)) {
                //Log.d("N3 ISSUE", "Index que se manda: " + i);
                return index;//index = i;
            }
        }

        return index;
    }

    public static String getStateName(String code) {

        String name = "";
        if(code.equals("1"))
            name = "Aguascalientes";
        if(code.equals("2"))
            name = "Baja California";
        if(code.equals("3"))
            name = "Baja California Sur";
        if(code.equals("4"))
            name = "Campeche";
        if(code.equals("5"))
            name = "Chiapas" ;
        if(code.equals("6"))
            name = "Chihuahua";
        if(code.equals("7"))
            name = "Coahuila";
        if(code.equals("8"))
            name = "Colima"  ;
        if(code.equals("9"))
            name = "Ciudad de Mexico";
        if(code.equals("10"))
            name = "Durango" ;
        if(code.equals("11"))
            name = "Guanajuato"  ;
        if(code.equals("12"))
            name = "Guerrero";
        if(code.equals("13"))
            name = "Hidalgo" ;
        if(code.equals("14"))
            name = "Jalisco" ;
        if(code.equals("15"))
            name = "Mexico"  ;
        if(code.equals("16"))
            name = "Michoacan";
        if(code.equals("17"))
            name = "Morelos" ;
        if(code.equals("18"))
            name = "Nayarit" ;
        if(code.equals("19"))
            name = "Nuevo Leon";
        if(code.equals("20"))
            name = "Oaxaca"  ;
        if(code.equals("21"))
            name = "Puebla"  ;
        if(code.equals("22"))
            name = "Queretaro";
        if(code.equals("23"))
            name = "Quintana Roo";
        if(code.equals("24"))
            name = "San Luis Potosi";
        if(code.equals("25"))
            name = "Sinaloa";
        if(code.equals("26"))
            name = "Sonora" ;
        if(code.equals("27"))
            name = "Tabasco";
        if(code.equals("28"))
            name = "Tamaulipas";
        if(code.equals("29"))
            name = "Tlaxcala";
        if(code.equals("30"))
            name = "Veracruz";
        if(code.equals("31"))
            name = "Yucatan";
        if(code.equals("32"))
            name = "Zacatecas";

        return name.toUpperCase();
    }
}
