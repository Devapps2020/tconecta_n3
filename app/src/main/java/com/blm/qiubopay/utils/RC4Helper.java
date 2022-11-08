package com.blm.qiubopay.utils;


public class RC4Helper {
    private static RC4Helper instance;
    private int sbox[];
    private int KEY[];

    private RC4Helper(){
        sbox = new int[256];
        KEY = new int[256];
    }

    public static RC4Helper getInstance(){
        if(instance == null){
            instance = new RC4Helper();
        }
        return instance;
    }

    public String encriptaDato(String cadena, String semilla){
        if(cadena.isEmpty()){
            return  cadena;
        }
        return StringToHexString(Salaa(cadena,semilla));
    }

    public String desencriptaDato(String cadena, String semilla){
        return Pura(hexStringToString(cadena),semilla);
    }

    private void RC4Initialize(String s){
        int l = 0;
        int i1 = 0;
        i1 = s.length();
        for(int j = 0; j <= 255; j++){
            KEY[j] = s.charAt(j % i1);
            sbox[j] = j;
        }
        l = 0;
        for(int k = 0; k <= 255; k++){
            l = (l + sbox[k] + KEY[k]) % 256;
            int i = sbox[k];
            sbox[k] = sbox[l];
            sbox[l] = i;
        }
    }

    private String Salaa(String s, String s1){
        RC4Initialize(s1);
        int k = 0;
        int l = 0;
        String s2 = "";
        for(int j = 0; j < s.length(); j++){
            k = (k + 1) % 256;
            l = (l + sbox[k]) % 256;
            int i = sbox[k];
            sbox[k] = sbox[l];
            sbox[l] = i;
            int i1 = sbox[(sbox[k] + sbox[l]) % 256];
            int j1 = s.charAt(j) ^ i1;
            s2 = s2 + (char)j1;
        }
        return s2;
    }

    private String Pura(String s, String s1){
        RC4Initialize(s1);
        int k = 0;
        int l = 0;
        String s2 = "";
        for(int j = 0; j < s.length(); j++){
            k = (k + 1) % 256;
            l = (l + sbox[k]) % 256;
            int i = sbox[k];
            sbox[k] = sbox[l];
            sbox[l] = i;
            int i1 = sbox[(sbox[k] + sbox[l]) % 256];
            int j1 = s.charAt(j) ^ i1;
            s2 = s2 + (char)j1;
        }
        return s2;
    }

    public String hexStringToString(String s){
        String s1 = "";
        for(int i = 0; i < (s.length()/2);i++){
            int j = i * 2;
            int k = Integer.parseInt(s.substring(j, j + 2), 16);
            s1 = s1 + (char)k;
        }
        return s1;
    }

    public synchronized String StringToHexString(String s){
        StringBuffer stringbuffer = new StringBuffer(s.length() * 2);
        for(int i = 0; i < s.length(); i++){
            int j = s.charAt(i) & 0xff;
            if(j < 16)
                stringbuffer.append('0');
            stringbuffer.append(Integer.toHexString(j));
        }
        return stringbuffer.toString().toUpperCase();
    }
}

