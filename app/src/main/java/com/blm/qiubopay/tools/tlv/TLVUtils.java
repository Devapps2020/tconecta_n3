package com.blm.qiubopay.tools.tlv;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TLVUtils {

    private final static String TAG = "TLVUtils";

    public static List<TLV> builderTLVList(String hexString) {
        List<TLV> list = new ArrayList<>();

        int position = 0;

        while (position != hexString.length()) {
            String tag = getTag(hexString, position);
            position += tag.length();

            LPosition l_position = getLengthAndPosition(hexString, position);

            int _vl = l_position.get_vL();

            position = l_position.get_position();

            String _value = hexString.substring(position, position + _vl * 2);

            position = position + _value.length();

            list.add(new TLV(tag, _vl, _value));
        }
        return list;
    }


    public static Map<String, TLV> builderTLVMap(String hexString) {

        // TLV文档连接 http://wenku.baidu.com/view/b31b26a13186bceb18e8bb53.html?re=view&qq-pf-to=pcqq.c2c
        Map<String, TLV> map = new HashMap<>();

        int position = 0;
        while (position < hexString.length()) {
            String tag = getTag(hexString, position);
            if (tag.equals("00")) {
                break;
            }
            position += tag.length();

            LPosition l_position = getLengthAndPosition(hexString, position);

            int _vl = l_position.get_vL();
            position = l_position.get_position();
            String _value = hexString.substring(position, position + _vl * 2);
            position = position + _value.length();
            //LogUtil.e("TLV-builderTLVMap", tag + ": " + _value);
            Log.i("TLV-builderTLVMap", tag + ": " + _value);

            if (map.get(tag) == null) {
                map.put(tag, new TLV(tag, _vl, _value));
            }
        }

        return map;
    }


    private static LPosition getLengthAndPosition(String hexString, int pos) {
        int position = pos;
        String hexLength;

        String firstByteString = hexString.substring(position, position + 2);
        int i = Integer.parseInt(firstByteString, 16);

        if (((i >> 7) & 1) == 0) {
            hexLength = hexString.substring(position, position + 2);
            position = position + 2;
        } else {
            int _L_Len = i & 127;
            position = position + 2;
            hexLength = hexString.substring(position, position + _L_Len * 2);

            position = position + _L_Len * 2;
        }

        return new LPosition(Integer.parseInt(hexLength, 16), position);
    }


    private static String getTag(String hexString, int position) {
        try {
            String firstByte = hexString.substring(position, position + 2);
            int i = Integer.parseInt(firstByte, 16);

            if ((i & 0x1F) == 0x1F) {
                return hexString.substring(position, position + 4);
            } else {
                return hexString.substring(position, position + 2);
            }
        } catch (Exception e) {
            return "";
        }
    }


}
