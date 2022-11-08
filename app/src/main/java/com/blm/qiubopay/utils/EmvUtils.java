package com.blm.qiubopay.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nexgo.oaf.apiv3.emv.AidEntity;
import com.nexgo.oaf.apiv3.emv.AidEntryModeEnum;
import com.nexgo.oaf.apiv3.emv.CapkEntity;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

//N3_FLAG_COMMENT
/*
import com.nexgo.oaf.apiv3.emv.AidEntity;
import com.nexgo.oaf.apiv3.emv.CapkEntity;
*/
//N3_FLAG_COMMENT

/**
 * cn.nexgo.inbas.components.emv
 * Author: zhaojiangon 2018/1/31 17:31.
 * Modified by Brad2018/4/23 : add test file
 */

public class EmvUtils {
    static Context context;
    public EmvUtils(Context context){
        this.context = context;
    }

    private static String inbas_aid_file_name ;
    private static String inbas_capk_file_name;



    //N3_FLAG_COMMENT
    private static void initValues(){
        //inbas_aid_file_name = "inbas_aid_backup.json";
        //inbas_capk_file_name = "inbas_capk_backup.json";

        //inbas_aid_file_name = "inbas_aid.json";
        //inbas_capk_file_name = "inbas_capk.json";

        inbas_aid_file_name = "inbas_aid_v3.json";
        inbas_capk_file_name = "inbas_capk_v3.json";

    }

    public static List<AidEntity> getAidListBackup(){
        initValues();
        List<AidEntity> aidEntityList = new ArrayList<>();
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray jsonArray;

        jsonArray = parser.parse(readAssetsTxt(inbas_aid_file_name)).getAsJsonArray();

        if (jsonArray == null){
            return null;
        }

        for (JsonElement user : jsonArray) {
            AidEntity userBean = gson.fromJson(user, AidEntity.class);
            aidEntityList.add(userBean);
        }
        return aidEntityList;
    }


    public static List<CapkEntity> getCapkListBackup(){
        initValues();
        List<CapkEntity> capkEntityList = new ArrayList<>();
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        JsonArray jsonArray;

        jsonArray = parser.parse(readAssetsTxt(inbas_capk_file_name)).getAsJsonArray();

        if (jsonArray == null){
            return null;
        }

        for (JsonElement user : jsonArray) {
            CapkEntity userBean = gson.fromJson(user, CapkEntity.class);
            capkEntityList.add(userBean);
        }
        return capkEntityList;
    }

    public static List<CapkEntity> getCapkList(){
        initValues();
        List<CapkEntity> capkEntityList = new ArrayList<>();
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        JsonArray jsonArray;

        jsonArray = parser.parse(readAssetsTxt(inbas_capk_file_name)).getAsJsonArray();

        if (jsonArray == null){
            return null;
        }

        for (JsonElement user : jsonArray) {
            CapkEntity userBean = gson.fromJson(user, CapkEntity.class);
            capkEntityList.add(userBean);
        }
        return capkEntityList;
    }

    public static List<AidEntity> getAidList(){
        initValues();
        List<AidEntity> aidEntityList = new ArrayList<>();
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonArray jsonArray;

        jsonArray = parser.parse(readAssetsTxt(inbas_aid_file_name)).getAsJsonArray();

        if (jsonArray == null){
            return null;
        }

        /*for (JsonElement user : jsonArray) {
            AidEntity userBean = gson.fromJson(user, AidEntity.class);
            JsonObject jsonObject = user.getAsJsonObject();
            if(jsonObject != null){
                if(jsonObject.get("emvEntryMode") != null){
                    int emvEntryMode = jsonObject.get("emvEntryMode").getAsInt();
                    userBean.setAidEntryModeEnum(AidEntryModeEnum.values()[emvEntryMode]);
                    Log.d("nexgo", "emvEntryMode" + userBean.getAidEntryModeEnum());
                }
            }

            aidEntityList.add(userBean);
        }*/

        for (JsonElement user : jsonArray) {
            AidEntity userBean = gson.fromJson(user, AidEntity.class);
            //userBean.setAidEntryModeEnum(AidEntryModeEnum.AID_ENTRY_CONTACT);
            aidEntityList.add(userBean);
        }

        return aidEntityList;
    }

    //N3_FLAG_COMMENT

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static String readAssetsTxt(String fileName) {
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            //noinspection ResultOfMethodCallIgnored
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            return new String(buffer, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static String readFile(String filePath) {
        try {
            InputStream is = new FileInputStream(filePath);
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            //noinspection ResultOfMethodCallIgnored
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            return new String(buffer, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String getElementAttr(Element element, String attr) {
        if (element == null) return "";
        if (element.hasAttribute(attr)) {
            return element.getAttribute(attr);
        }
        return "";
    }

    private static Element[] getElementsByName(Element parent, String name) {
        ArrayList resList = new ArrayList();
        NodeList nl = parent.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node nd = nl.item(i);
            if (nd.getNodeName().equals(name)) {
                resList.add(nd);
            }
        }
        Element[] res = new Element[resList.size()];
        for (int i = 0; i < resList.size(); i++) {
            res[i] = (Element) resList.get(i);
        }
        return res;
    }

}
