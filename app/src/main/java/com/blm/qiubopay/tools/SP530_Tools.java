package com.blm.qiubopay.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.Arrays;

import static com.blm.qiubopay.conf.sp530.TransactionConstant.NUMBER_TRANSACTION_AMOUNT_DIGITS;

public class SP530_Tools {
    private static final String TAG = Tools.class.getSimpleName();

    /**
     * Characters used for hex array
     */
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * convert byte array to hex string
     * @param bytes byte array
     * @return hex string
     */
    public static String bytesArrayToHexString(byte[] bytes) {
        if (bytes==null) {
            Log.w(TAG, "bytesArrayToHexString, bytes is NULL");
            return "";
        }
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Convert hex string to byte array
     * @param s hex string
     * @return byte array
     */
    public static byte[] hexStringToByteArray(String s) {
        if (s==null) {
            Log.w(TAG, "hexStringToByteArray, s is NULL");
            return null;
        }
        int len = s.length();
        // do padding
        if (len%2!=0) {
            s="0"+s;
            len=s.length();
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * Returns version code of application
     * @param context Context of application
     * @return Version code of application
     */
    public static int getVersion(Context context) {
        int version = -1;
        PackageInfo pInfo=getPackageInfo(context);
        if (pInfo!=null) {
            version = pInfo.versionCode;
        }
        return version;
    }

    /**
     * Returns version name of application
     * @param context Context of application
     * @return Version name of application
     */
    public static String getVersionName(Context context) {
        String versionName="";
        PackageInfo pInfo=getPackageInfo(context);
        if (pInfo!=null) {
            versionName = pInfo.versionName;
        }
        return versionName;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pInfo=null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (Exception ex) {
            Log.e(TAG, "Name not found: "+ex.toString());
        }
        return pInfo;
    }

    public static byte[] getAmountByteArray(String strAmount) {
        byte[] buf=null;

        String s=strAmount;
        if (s!=null) {
            s=s.replace(".", "");
        }
        if ( (s==null)||(s.equals("")) ) {
            Log.w(TAG, "getAmountByteArray input amount is EMPTY");
            return buf;
        }

        int len=s.length();
        int MAX_LENGTH=NUMBER_TRANSACTION_AMOUNT_DIGITS;
        if (len>MAX_LENGTH) {
            Log.e(TAG, "getAmountByteArray the number of digits of input amount is larger than MAX_LENGTH ("+MAX_LENGTH+")");
            return buf;
        }

        int lengthBuf=(int)Math.ceil((float)MAX_LENGTH/2);
        buf=new byte[lengthBuf];
        for (int i=len-1, j=0; i>=0; i-=2,j++) {
            char c0='0';
            if (i-1>-1) {
                c0=s.charAt(i - 1);
            }
            char c1=s.charAt(i);
            int val0=Character.getNumericValue(c0);
            int val1=Character.getNumericValue(c1);
            byte b=(byte)( ((val0&0x0F)<<4) | (val1&0x0F) );
            buf[lengthBuf-1-j]=b;
        }

        return buf;
    }

    /**
     * integer to array bytes, eg 123=>"\x7B"
     * @param aInt
     * @param aOutLen length of bytes to return, if <=0 means use min number of bytes
     * @return array bytes
     */
    public static byte[] Int_toAb(int aInt, int aOutLen) {
        //get 4 bytes integer data
        byte[] abInt = new byte[4];
        for (int i = 0; i < 4; i++) {
            abInt[i] = (byte) ((aInt >>> (8 * (3 - i))) & 0xFF);
        }

        //return min used bytes
        if (aOutLen <= 0) {
            for (int i = 0; i < 4; i++) {
                if (abInt[i] != 0x00) {
                    return Ab_sub(abInt, i, 4 - i);
                }
            }
            return new byte[1];
        }

        //handle return 1-4 bytes
        if (aOutLen <= 4) {
            return Ab_sub(abInt, 4 - aOutLen, aOutLen);
        }

        //packing leading zero if > 4 bytes
        byte[] abRet = new byte[aOutLen];
        //Arrays.fill(abRet, (byte)0);
        System.arraycopy(abInt, 0, abRet, abRet.length - 4, 4);
        return abRet;
    }

    /**
     * integer to array bytes, eg 123=>"\x7B"
     * @param aInt
     * @return array bytes
     */
    public static byte[] Int_toAb(int aInt) {
        return Int_toAb(aInt, -1);
    }
    /**
     * get smaller integer
     * @param aA integer A
     * @param aB integer B
     * @return smaller integer
     */
    public static int Int_min(int aA, int aB) {
        if (aA > aB) return aB;
        return aA;
    }
    /**
     * get larger integer
     * @param aA integer A
     * @param aB integer B
     * @return larger integer
     */
    public static int Int_max(int aA, int aB) {
        if (aA > aB) return aA;
        return aB;
    }

    /**
     * array bytes subtraction
     *
     * @param aSrc
     * @param aOSet
     * @param aLen  if -ve, copy to end
     * @return subtracted array bytes
     */
    public static byte[] Ab_sub(byte[] aSrc, int aOSet, int aLen) {
        if (aOSet < 0) return null;
        if (aSrc == null) return null;
        if (aLen < 0) { //len(-ve) = copy to end
            if (aSrc.length <= aOSet) return null;
            aLen = aSrc.length - aOSet;
        } else {
            if (aSrc.length < aOSet + aLen) return null;
        }
        byte[] out = new byte[aLen];
        System.arraycopy(aSrc, aOSet, out, 0, aLen);
        return out;
    }
    /**
     * array bytes concat
     *
     * @param aFirst
     * @param aRest
     * @return concated array bytes
     */
    public static byte[] Ab_cat(byte[] aFirst, byte[]... aRest) {
        int totalLength = (aFirst == null) ? 0 : aFirst.length;
        for (byte[] array : aRest) {
            totalLength += (array == null) ? 0 : array.length;
        }
        if (totalLength == 0) return null;
        byte[] result = null;
        int offset = 0;
        if (aFirst != null) {
            result = Arrays.copyOf(aFirst, totalLength);
            offset = aFirst.length;
        } else {
            result = new byte[totalLength];
        }
        for (byte[] array : aRest) {
            if (array != null) {
                System.arraycopy(array, 0, result, offset, array.length);
                offset += array.length;
            }
        }
        return result;
    }
    /**
     * get non-null length, if null return 0
     *
     * @param aAb
     * @return
     */
    public static int Ab_nnLen(byte[] aAb) {
        if (aAb == null) return 0;
        return aAb.length;
    }
    /**
     * array byte copy
     *
     * @param aSrc
     * @param aSrcOSet
     * @param aDest
     * @param aDestOSet
     * @param aLen
     * @return 0 ok, -ve err
     */
    public static int Ab_copy(byte[] aSrc, int aSrcOSet, byte[] aDest, int aDestOSet, int aLen) {
        if ((aSrc == null) || (aDest == null)) {
            //Log.e(TAG, "abCopy: Src=%s, Dest=%s", (aSrc==null)?"null":"nonNull", (aDest==null)?"null":"nonNull");
            return -1;
        }
        if (aSrc.length < aSrcOSet + aLen) {
            //Log.e(TAG, "abCopy: SrcErr LenTot(%d) < OSet(%d) + Len(%s)", aSrc.length, aSrcOSet, aLen);
            return -1;

        }
        if (aDest.length < aDestOSet + aLen) {
            //Log.e(TAG, "abCopy: DestErr LenTot(%d) < OSet(%d) + Len(%s)", aDest.length, aDestOSet, aLen);
            return -1;

        }
        System.arraycopy(aSrc, aSrcOSet, aDest, aDestOSet, aLen);
        return 0;
    }
    /**
     * pack tlv list
     * @param aTlvList tlv list if null, create new, otherwise append to it
     * @param aTag
     * @param aLen len of var, if -ve, use aVar length
     * @param aVar null for all packing 0x00
     * @param aVOSet offset of required data in aVar
     * @return tlv list
     */
    public static byte[] tlv_pack(byte[] aTlvList, int aTag, int aLen, byte[] aVar, int aVOSet) {
        byte[] abTlv;

        //Tag
        abTlv = Int_toAb(aTag);
        //Len
        if (aLen < 0) { //use aVar len
            aLen = Int_max(Ab_nnLen(aVar) - aVOSet, 0);
        }
        if (aLen < 0x0080) {
            abTlv = Ab_cat(abTlv, Int_toAb(aLen));
        }
        else if (aLen < 0x0100) {
            abTlv = Ab_cat(abTlv, Int_toAb(0x81), Int_toAb(aLen));
        }
        else {
            abTlv = Ab_cat(abTlv, Int_toAb(0x82), Int_toAb(aLen));
        }
        //Var
        byte[] abV = new byte[aLen];
        if ((Ab_nnLen(aVar) > aVOSet) && (aVOSet >= 0)) {
            Ab_copy(aVar, aVOSet, abV, 0, Int_min(aLen, Ab_nnLen(aVar)-aVOSet));
        }
        abTlv = Ab_cat(abTlv, abV);
        return Ab_cat(aTlvList, abTlv);
    }

    public static byte[] getInputByteArray(String amount){//EditText et) {
        byte[] buf=null;

        String s=amount.trim();//et.getText().toString();
        if (s!=null) {
            s=s.replace(".", "");
            s=s.replace("$", "");
            s=s.replace(",", "");
        }
        if ( (s==null)||(s.equals("")) ) {
            Log.w("getInputByteArray", "getInputByteArray input amount is EMPTY");
            return buf;
        }

        int len=s.length();
        int MAX_LENGTH = 12;
        if (len>MAX_LENGTH) {
            Log.e("getInputByteArray", "getInputByteArray the number of digits of input amount is larger than MAX_LENGTH ("+MAX_LENGTH+")");
            return buf;
        }

        int lengthBuf=(int)Math.ceil((float)MAX_LENGTH/2);
        buf=new byte[lengthBuf];
        for (int i=len-1, j=0; i>=0; i-=2,j++) {
            char c0='0';
            if (i-1>-1) {
                c0=s.charAt(i - 1);
            }
            char c1=s.charAt(i);
            int val0=Character.getNumericValue(c0);
            int val1=Character.getNumericValue(c1);
            byte b=(byte)( ((val0&0x0F)<<4) | (val1&0x0F) );
            buf[lengthBuf-1-j]=b;
        }

        return buf;
    }
}
