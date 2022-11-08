package com.blm.qiubopay.models.ocr;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class OcrMatchReceive implements Parcelable {

    private HashMap<String,String> mapOcrReceived;

    protected OcrMatchReceive(Parcel in) {
    }

    public static final Creator<OcrMatchReceive> CREATOR = new Creator<OcrMatchReceive>() {
        @Override
        public OcrMatchReceive createFromParcel(Parcel in) {
            return new OcrMatchReceive(in);
        }

        @Override
        public OcrMatchReceive[] newArray(int size) {
            return new OcrMatchReceive[size];
        }
    };

    public HashMap<String, String> getMapOcrReceived() {
        return mapOcrReceived;
    }

    public void setMapOcrReceived(HashMap<String, String> mapOcrReceived) {
        this.mapOcrReceived = mapOcrReceived;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeMap(mapOcrReceived);
    }

}
