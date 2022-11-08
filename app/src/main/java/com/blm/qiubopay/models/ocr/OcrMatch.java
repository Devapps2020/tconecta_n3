package com.blm.qiubopay.models.ocr;

/*
 * RSB 20200315. Esta clase es para enviar al COR y funcione de manera genérica.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 *  Clase para el consumo del servicio del OCR
 */
public class OcrMatch implements Parcelable {

    //Variable que define el nombre que sera el key en el map de retorno
    private String idValue;

    //Variable que contiene parametro a buscar
    private String wordOrRegex;

    //Variable que indica si el parametro a buscar ser{a requerido o puede retonar sin este valor.
    private boolean isRequired;

    //Variable que se incluye en caso de que se tenga que buscar dentro del objeto ya encontrado
    //Ej. Primero buscar un bloque que contenga la palabra "NOMBRE", despues buscar dentro de ese
    //bloque "JORGE".
    private List<OcrMatch> ocrMatch;

    public OcrMatch(String idValue, String wordOrRegex, boolean isRequired, List<OcrMatch> ocrMatch) {
        this.idValue = idValue;
        this.wordOrRegex = wordOrRegex;
        this.isRequired = isRequired;
        this.ocrMatch = ocrMatch;
    }

    protected OcrMatch(Parcel in) {
        idValue = in.readString();
        wordOrRegex = in.readString();
        isRequired = in.readByte() != 0;
        ocrMatch = in.createTypedArrayList(OcrMatch.CREATOR);
    }

    public static final Creator<OcrMatch> CREATOR = new Creator<OcrMatch>() {
        @Override
        public OcrMatch createFromParcel(Parcel in) {
            return new OcrMatch(in);
        }

        @Override
        public OcrMatch[] newArray(int size) {
            return new OcrMatch[size];
        }
    };

    public String getIdValue() {
        return idValue;
    }

    public void setIdValue(String idValue) {
        this.idValue = idValue;
    }

    public String getWordOrRegex() {
        return wordOrRegex;
    }

    public void setWordOrRegex(String wordOrRegex) {
        this.wordOrRegex = wordOrRegex;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public List<OcrMatch> getOcrMatch() {
        return ocrMatch;
    }

    public void setOcrMatch(List<OcrMatch> ocrMatch) {
        this.ocrMatch = ocrMatch;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idValue);
        parcel.writeString(wordOrRegex);
        parcel.writeByte((byte) (isRequired ? 1 : 0));
        parcel.writeTypedList(ocrMatch);
    }
}
