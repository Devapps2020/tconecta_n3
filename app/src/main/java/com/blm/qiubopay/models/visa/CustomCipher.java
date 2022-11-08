package com.blm.qiubopay.models.visa;

import android.util.Log;

import com.blm.qiubopay.utils.Globals;
import com.nexgo.common.ByteUtils;
import com.blm.qiubopay.tools.Tools;
import com.blm.qiubopay.utils.Des;


import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Random;

public class CustomCipher {

    //private static final String a = "E54CC6D061790E35D47288CC3073469B3F29620AB6800517";
    //private static final String b = "BBBC6588EC5E4217AA7F88A96C5344A83260AD0B8E777B51";//DESARROLLO
    private static final String b = Globals.debug ? "BBBC6588EC5E4217AA7F88A96C5344A83260AD0B8E777B51" : "BBAE77642D9A55614B9EE2450E6609E056870F4E15F16A51";//PRODUCCIÓN 2

    private static String getRandomNumber(){
        int min = 10000000;
        int max = 99999999;
        String back = "";
        Random r = new Random();
        int random = r.nextInt(max - min + 1) + min;

        back = "" + random;

        back = back + Tools.getPaddingCharacter(8-back.length(),"0");

        if(back.length() > 8)
            back = back.substring(0,8);

        back = Tools.stringToHexString(back);

        Log.d("*** N3 READER ***","RANDOM NUMBER: " + back + " LEN: " + back.length());

        return back;//Tools.stringToHexString(back);
    }

    public static String getValue(String data, boolean isExpirationDate){
        String back = "";

        if(data.contains("="))
        {
            data = data.replace("=","D") + "F";
        }else if((data.contains("D") || data.contains("d")) && !data.contains("F") && !data.contains("f"))
            data = data.toUpperCase() + "F";

        try {

            String lenHexPlain, lenHexCipher;
            int lenPlain, lenCipher;

            //String randomNumber = "73CA1244C3777614";
            String randomNumber = getRandomNumber();

            //String hashNumber =  "8843C6681B2D0650163B3378BB79660C370A23D26139725190DDDE1BBEF39882";//
            //String hashNumber = Tools.getHashSha256(Hex.decodeHex(randomNumber.toCharArray())).toUpperCase();
            String hashNumber = new String(Hex.encodeHex(DigestUtils.sha256(Hex.decodeHex(randomNumber.toCharArray())))).toUpperCase();
            String newData = hashNumber.substring(0, 48);
            String sessionKey = ByteUtils.byteArray2HexString(Des.trides_crypt2(ByteUtils.hexString2ByteArray(newData), ByteUtils.hexString2ByteArray(b)));
            lenPlain = ByteUtils.hexString2ByteArray(data).length;

            if(!isExpirationDate)
                data = data + Tools.getPaddingCharacter(48 - data.length(), "0");
            else
                data = data + Tools.getPaddingCharacter(16 - data.length(), "0");

            back = ByteUtils.byteArray2HexString(Des.trides_crypt2(ByteUtils.hexString2ByteArray(data), ByteUtils.hexString2ByteArray(sessionKey)));
            lenCipher = ByteUtils.hexString2ByteArray(back).length;

            lenHexPlain = Integer.toHexString(lenPlain);
            lenHexCipher = Integer.toHexString(lenCipher + 1);

            if(lenHexCipher.length() == 1) lenHexCipher = "0" + lenHexCipher;
            if(lenHexPlain.length() == 1) lenHexPlain = "0" + lenHexPlain;

            back = "08" + randomNumber + lenHexCipher + lenHexPlain + back;

        }catch (Exception e){
            back = null;
        }
        return back;
    }

    public static String getOriginalValue(String data){
        String back = "";
        try {
            String randomNumber = data.substring(2, 18);
            String cipher_data = data.substring(22);

            String hashNumber = new String(Hex.encodeHex(DigestUtils.sha256(Hex.decodeHex(randomNumber.toCharArray())))).toUpperCase();
            String newData = hashNumber.substring(0, 48);
            String sessionKey = ByteUtils.byteArray2HexString(Des.trides_crypt2(ByteUtils.hexString2ByteArray(newData), ByteUtils.hexString2ByteArray(b)));
            //lenPlain = ByteUtils.hexString2ByteArray(data).length;

            /*if(padding)
                back = ByteUtils.byteArray2HexString(Des.trides_decrypt(ByteUtils.hexString2ByteArray(cipher_data), ByteUtils.hexString2ByteArray(sessionKey)));
            else*/
            back = ByteUtils.byteArray2HexString(Des.trides_decrypt2(ByteUtils.hexString2ByteArray(cipher_data), ByteUtils.hexString2ByteArray(sessionKey)));

        }catch (Exception e){
            back = null;
        }
        return back;
    }

    public static String getOriginalValue(String data, String key){
        String back = "";
        try {
            String randomNumber = data.substring(2, 18);
            String cipher_data = data.substring(22);

            String hashNumber = new String(Hex.encodeHex(DigestUtils.sha256(Hex.decodeHex(randomNumber.toCharArray())))).toUpperCase();
            String newData = hashNumber.substring(0, 48);
            String sessionKey = ByteUtils.byteArray2HexString(Des.trides_crypt2(ByteUtils.hexString2ByteArray(newData), ByteUtils.hexString2ByteArray(key)));
            //lenPlain = ByteUtils.hexString2ByteArray(data).length;

            /*if(padding)
                back = ByteUtils.byteArray2HexString(Des.trides_decrypt(ByteUtils.hexString2ByteArray(cipher_data), ByteUtils.hexString2ByteArray(sessionKey)));
            else*/
            back = ByteUtils.byteArray2HexString(Des.trides_decrypt2(ByteUtils.hexString2ByteArray(cipher_data), ByteUtils.hexString2ByteArray(sessionKey)));

        }catch (Exception e){
            back = null;
        }
        //return getValue(back, false);
        return back;
    }
}
