package com.blm.qiubopay.models.n3;

import android.util.Log;
//import com.nexgo.oaf.apiv3.emv.EmvDataSourceEnum;

public class EmvTlvResponse {
    private String TAG_9F02;
    private String TAG_5F24;
    private String TAG_9A;
    private String TAG_4F;
    private String TAG_9F34;
    private String TAG_9F03;
    private String TAG_9F06;
    private String TAG_9F21;
    private String TAG_5A;
    private String TAG_9F10;
    private String TAG_82;
    private String TAG_8E;
    private String TAG_5F25;
    private String TAG_9F07;
    private String TAG_9F0D;
    private String TAG_9F0E;
    private String TAG_9F0F;
    private String TAG_9F26;
    private String TAG_9F27;
    private String TAG_9F36;
    private String TAG_9C;
    private String TAG_9F33;
    private String TAG_9F37 ;
    private String TAG_9F39;
    private String TAG_9F40;
    private String TAG_95;
    private String TAG_9B;
    private String TAG_84;
    private String TAG_5F2A;
    private String TAG_5F34;
    private String TAG_9F09;
    private String TAG_9F1A;
    private String TAG_9F1E;
    private String TAG_9F35;
    private String TAG_9F41;
    private String TAG_5F20;
    private String TAG_5F30;
    private String TAG_5F28;
    private String TAG_50;
    private String TAG_9F08;
    private String TAG_9F01;
    private String TAG_9F15;
    /*private String TAG_;
    private String TAG_;
    private String TAG_;
    private String TAG_;
    private String TAG_;
    private String TAG_;*/

    /*public EmvTlvResponse(com.nexgo.oaf.apiv3.emv.EmvHandler emvHandler){
        setTAG_9F02(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x02}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_5F24(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x5f, (byte) 0x24}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9A(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9a}, EmvDataSourceEnum.FROM_KERNEL)));

        setTAG_4F(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x4f}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F34(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x34}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F03(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x03}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F06(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x06}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F21(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x21}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_5A(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x5a}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F10(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x10}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_82(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x82}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_8E(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x8e}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_5F25(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x5f, (byte) 0x25}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F07(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x07}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F0D(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x0d}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F0E(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x0e}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F0F(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x0f}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F26(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x26}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F27(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x27}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F36(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x36}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9C(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9c}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F33(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x33}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F37(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x37}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F39(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x39}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F40(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x40}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_95(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x95}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9B(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9b}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_84(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x84}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_5F2A(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x5f, (byte) 0x2a}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_5F34(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x5f, (byte) 0x34}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F09(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x09}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F1A(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x1a}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F1E(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x1e}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F35(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x35}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F41(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x41}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_5F20(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x5f, (byte) 0x20}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_5F30(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x5f, (byte) 0x30}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_5F28(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x5f, (byte) 0x28}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_50(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x50}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F08(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x08}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F01(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x01}, EmvDataSourceEnum.FROM_KERNEL)));
        setTAG_9F15(ByteUtils.byteArray2HexString(emvHandler.getTlv(new byte[]{(byte) 0x9f, (byte) 0x15}, EmvDataSourceEnum.FROM_KERNEL)));
        Log.d("*** N3 READER ***","");
    }*/

    public String getCompleteTlv(){
        String back="";

        back += "9F02"  +   getSize(this.getTAG_9F02()) + this.getTAG_9F02();
        back += "5F24"  +   getSize(this.getTAG_5F24()) + this.getTAG_5F24();
        back += "9A"    +   getSize(this.getTAG_9A()) + this.getTAG_9A();
        back += "4F"    +   getSize(this.getTAG_4F()) + this.getTAG_4F();
        back += "9F34"  +   getSize(this.getTAG_9F34()) + this.getTAG_9F34();
        back += "9F03"  +   getSize(this.getTAG_9F03()) + this.getTAG_9F03();
        back += "9F06"  +   getSize(this.getTAG_9F06()) + this.getTAG_9F06();
        back += "9F21"  +   getSize(this.getTAG_9F21()) + this.getTAG_9F21();
        back += "5A"    +   getSize(this.getTAG_5A()) + this.getTAG_5A();
        back += "9F10"  +   getSize(this.getTAG_9F10()) + this.getTAG_9F10();
        back += "82"    +   getSize(this.getTAG_82()) + this.getTAG_82();
        back += "8E"    +   getSize(this.getTAG_8E()) + this.getTAG_8E();
        back += "5F25"  +   getSize(this.getTAG_5F25()) + this.getTAG_5F25();
        back += "9F07"  +   getSize(this.getTAG_9F07()) + this.getTAG_9F07();
        back += "9F0D"  +   getSize(this.getTAG_9F0D()) + this.getTAG_9F0D();
        back += "9F0E"  +   getSize(this.getTAG_9F0E()) + this.getTAG_9F0E();
        back += "9F0F"  +   getSize(this.getTAG_9F0F()) + this.getTAG_9F0F();
        back += "9F26"  +   getSize(this.getTAG_9F26()) + this.getTAG_9F26();
        back += "9F27"  +   getSize(this.getTAG_9F27()) + this.getTAG_9F27();
        back += "9F36"  +   getSize(this.getTAG_9F36()) + this.getTAG_9F36();
        back += "9C"    +   getSize(this.getTAG_9C()) + this.getTAG_9C();
        back += "9F33"  +   getSize(this.getTAG_9F33()) + this.getTAG_9F33();
        back += "9F37" +   getSize(this.getTAG_9F37()) + this.getTAG_9F37();
        back += "9F39"  +   getSize(this.getTAG_9F39()) + this.getTAG_9F39();
        back += "9F40"  +   getSize(this.getTAG_9F40()) + this.getTAG_9F40();
        back += "95"    +   getSize(this.getTAG_95()) + this.getTAG_95();
        back += "9B"    +   getSize(this.getTAG_9B()) + this.getTAG_9B();
        back += "84"    +   getSize(this.getTAG_84()) + this.getTAG_84();
        back += "5F2A"  +   getSize(this.getTAG_5F2A()) + this.getTAG_5F2A();
        back += "5F34"  +   getSize(this.getTAG_5F34()) + this.getTAG_5F34();
        back += "9F09"  +   getSize(this.getTAG_9F09()) + this.getTAG_9F09();
        back += "9F1A"  +   getSize(this.getTAG_9F1A()) + this.getTAG_9F1A();
        //back += "9F1E"  +   getSize(this.getTAG_9F1E()) + this.getTAG_9F1E();
        back += "9F35"  +   getSize(this.getTAG_9F35()) + this.getTAG_9F35();
        back += "9F41"  +   getSize(this.getTAG_9F41()) + this.getTAG_9F41();
        back += "5F20"  +   getSize(this.getTAG_5F20()) + this.getTAG_5F20();
        back += "5F30"  +   getSize(this.getTAG_5F30()) + this.getTAG_5F30();
        back += "5F28"  +   getSize(this.getTAG_5F28()) + this.getTAG_5F28();
        back += "50"    +   getSize(this.getTAG_50()) + this.getTAG_50();
        back += "9F08"  +   getSize(this.getTAG_9F08()) + this.getTAG_9F08();
        back += "9F01"  +   getSize(this.getTAG_9F01()) + this.getTAG_9F01();
        back += "9F15"  +   getSize(this.getTAG_9F15()) + this.getTAG_9F15();

        Log.d("*** N3 READER ***","TAGS: " + back);

        return back.replace(" ","");
    }

    private String getSize(String value){
        String s = String.format("%X", value.length()/2);
        if(s.length()==1)
            s="0"+s;
        return s;
    }

    public String getTAG_9F02() {
        return TAG_9F02;
    }

    public void setTAG_9F02(String TAG_9F02) {
        this.TAG_9F02 = TAG_9F02;
    }

    public String getTAG_5F24() {
        return TAG_5F24;
    }

    public void setTAG_5F24(String TAG_5F24) {
        this.TAG_5F24 = TAG_5F24;
    }

    public String getTAG_9A() {
        return TAG_9A;
    }

    public void setTAG_9A(String TAG_9A) {
        this.TAG_9A = TAG_9A;
    }

    public String getTAG_4F() {
        return TAG_4F;
    }

    public void setTAG_4F(String TAG_4F) {
        this.TAG_4F = TAG_4F;
    }

    public String getTAG_9F34() {
        return TAG_9F34;
    }

    public void setTAG_9F34(String TAG_9F34) {
        this.TAG_9F34 = TAG_9F34;
    }

    public String getTAG_9F03() {
        return TAG_9F03;
    }

    public void setTAG_9F03(String TAG_9F03) {
        this.TAG_9F03 = TAG_9F03;
    }

    public String getTAG_9F06() {
        return TAG_9F06;
    }

    public void setTAG_9F06(String TAG_9F06) {
        this.TAG_9F06 = TAG_9F06;
    }

    public String getTAG_9F21() {
        return TAG_9F21;
    }

    public void setTAG_9F21(String TAG_9F21) {
        this.TAG_9F21 = TAG_9F21;
    }

    public String getTAG_5A() {
        return TAG_5A;
    }

    public void setTAG_5A(String TAG_5A) {
        this.TAG_5A = TAG_5A;
    }

    public String getTAG_9F10() {
        return TAG_9F10;
    }

    public void setTAG_9F10(String TAG_9F10) {
        this.TAG_9F10 = TAG_9F10;
    }

    public String getTAG_82() {
        return TAG_82;
    }

    public void setTAG_82(String TAG_82) {
        this.TAG_82 = TAG_82;
    }

    public String getTAG_8E() {
        return TAG_8E;
    }

    public void setTAG_8E(String TAG_8E) {
        this.TAG_8E = TAG_8E;
    }

    public String getTAG_5F25() {
        return TAG_5F25;
    }

    public void setTAG_5F25(String TAG_5F25) {
        this.TAG_5F25 = TAG_5F25;
    }

    public String getTAG_9F07() {
        return TAG_9F07;
    }

    public void setTAG_9F07(String TAG_9F07) {
        this.TAG_9F07 = TAG_9F07;
    }

    public String getTAG_9F0D() {
        return TAG_9F0D;
    }

    public void setTAG_9F0D(String TAG_9F0D) {
        this.TAG_9F0D = TAG_9F0D;
    }

    public String getTAG_9F0E() {
        return TAG_9F0E;
    }

    public void setTAG_9F0E(String TAG_9F0E) {
        this.TAG_9F0E = TAG_9F0E;
    }

    public String getTAG_9F0F() {
        return TAG_9F0F;
    }

    public void setTAG_9F0F(String TAG_9F0F) {
        this.TAG_9F0F = TAG_9F0F;
    }

    public String getTAG_9F26() {
        return TAG_9F26;
    }

    public void setTAG_9F26(String TAG_9F26) {
        this.TAG_9F26 = TAG_9F26;
    }

    public String getTAG_9F27() {
        return TAG_9F27;
    }

    public void setTAG_9F27(String TAG_9F27) {
        this.TAG_9F27 = TAG_9F27;
    }

    public String getTAG_9F36() {
        return TAG_9F36;
    }

    public void setTAG_9F36(String TAG_9F36) {
        this.TAG_9F36 = TAG_9F36;
    }

    public String getTAG_9C() {
        return TAG_9C;
    }

    public void setTAG_9C(String TAG_9C) {
        this.TAG_9C = TAG_9C;
    }

    public String getTAG_9F33() {
        return TAG_9F33;
    }

    public void setTAG_9F33(String TAG_9F33) {
        this.TAG_9F33 = TAG_9F33;
    }

    public String getTAG_9F37() {
        return TAG_9F37;
    }

    public void setTAG_9F37(String TAG_9F37) {
        this.TAG_9F37 = TAG_9F37;
    }

    public String getTAG_9F39() {
        return TAG_9F39;
    }

    public void setTAG_9F39(String TAG_9F39) {
        this.TAG_9F39 = TAG_9F39;
    }

    public String getTAG_9F40() {
        return TAG_9F40;
    }

    public void setTAG_9F40(String TAG_9F40) {
        this.TAG_9F40 = TAG_9F40;
    }

    public String getTAG_95() {
        return TAG_95;
    }

    public void setTAG_95(String TAG_95) {
        this.TAG_95 = TAG_95;
    }

    public String getTAG_9B() {
        return TAG_9B;
    }

    public void setTAG_9B(String TAG_9B) {
        this.TAG_9B = TAG_9B;
    }

    public String getTAG_84() {
        return TAG_84;
    }

    public void setTAG_84(String TAG_84) {
        this.TAG_84 = TAG_84;
    }

    public String getTAG_5F2A() {
        return TAG_5F2A;
    }

    public void setTAG_5F2A(String TAG_5F2A) {
        this.TAG_5F2A = TAG_5F2A;
    }

    public String getTAG_5F34() {
        return TAG_5F34;
    }

    public void setTAG_5F34(String TAG_5F34) {
        this.TAG_5F34 = TAG_5F34;
    }

    public String getTAG_9F09() {
        return TAG_9F09;
    }

    public void setTAG_9F09(String TAG_9F09) {
        this.TAG_9F09 = TAG_9F09;
    }

    public String getTAG_9F1A() {
        return TAG_9F1A;
    }

    public void setTAG_9F1A(String TAG_9F1A) {
        this.TAG_9F1A = TAG_9F1A;
    }

    public String getTAG_9F1E() {
        return TAG_9F1E;
    }

    public void setTAG_9F1E(String TAG_9F1E) {
        this.TAG_9F1E = TAG_9F1E;
    }

    public String getTAG_9F35() {
        return TAG_9F35;
    }

    public void setTAG_9F35(String TAG_9F35) {
        this.TAG_9F35 = TAG_9F35;
    }

    public String getTAG_9F41() {
        return TAG_9F41;
    }

    public void setTAG_9F41(String TAG_9F41) {
        this.TAG_9F41 = TAG_9F41;
    }

    public String getTAG_5F20() {
        return TAG_5F20;
    }

    public void setTAG_5F20(String TAG_5F20) {
        this.TAG_5F20 = TAG_5F20;
    }

    public String getTAG_5F30() {
        return TAG_5F30;
    }

    public void setTAG_5F30(String TAG_5F30) {
        this.TAG_5F30 = TAG_5F30;
    }

    public String getTAG_5F28() {
        return TAG_5F28;
    }

    public void setTAG_5F28(String TAG_5F28) {
        this.TAG_5F28 = TAG_5F28;
    }

    public String getTAG_50() {
        return TAG_50;
    }

    public void setTAG_50(String TAG_50) {
        this.TAG_50 = TAG_50;
    }

    public String getTAG_9F08() {
        return TAG_9F08;
    }

    public void setTAG_9F08(String TAG_9F08) {
        this.TAG_9F08 = TAG_9F08;
    }

    public String getTAG_9F01() {
        return TAG_9F01;
    }

    public void setTAG_9F01(String TAG_9F01) {
        this.TAG_9F01 = TAG_9F01;
    }

    public String getTAG_9F15() {
        return TAG_9F15;
    }

    public void setTAG_9F15(String TAG_9F15) {
        this.TAG_9F15 = TAG_9F15;
    }
}
