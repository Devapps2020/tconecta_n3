package com.blm.qiubopay.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

public class Des {
    static String a = "DES/ECB/NoPadding";
    //static String b = "DESede/ECB/NoPadding";
    static String b = "DESede/ECB/PKCS7Padding";
    static String bb = "DESede/ECB/NoPadding";
    //NoPadding
    //PKCS7Padding
    //PKCS5Padding

    public Des() {
    }

    public static byte[] des_crypt(byte[] var0, byte[] var1) {
        if (var1 == null) {
            return null;
        } else {
            byte[] var2 = new byte[(var1.length + 7) / 8 * 8];
            System.arraycopy(var1, 0, var2, 0, var1.length);

            try {
                DESKeySpec var3 = new DESKeySpec(var0);
                SecretKeyFactory var4 = SecretKeyFactory.getInstance("DES");
                SecretKey var5 = var4.generateSecret(var3);
                Cipher var6 = Cipher.getInstance(a);
                var6.init(1, var5);
                return var6.doFinal(var2);
            } catch (Exception var7) {
                var7.printStackTrace();
                return null;
            }
        }
    }

    public static byte[] des_decrypt(byte[] var0, byte[] var1) {
        if (var1 == null) {
            return null;
        } else {
            byte[] var2 = new byte[(var1.length + 7) / 8 * 8];
            System.arraycopy(var1, 0, var2, 0, var1.length);

            try {
                DESKeySpec var3 = new DESKeySpec(var0);
                SecretKeyFactory var4 = SecretKeyFactory.getInstance("DES");
                SecretKey var5 = var4.generateSecret(var3);
                Cipher var6 = Cipher.getInstance(a);
                var6.init(2, var5);
                return var6.doFinal(var2);
            } catch (Exception var7) {
                var7.printStackTrace();
                return null;
            }
        }
    }

    public static byte[] trides_crypt(byte[] var0, byte[] var1) {
        try {
            byte[] var2 = new byte[24];
            if (var1 == null) {
                return null;
            } else {
                byte[] var3 = new byte[(var1.length + 7) / 8 * 8];
                System.arraycopy(var1, 0, var3, 0, var1.length);
                if (var0.length == 16) {
                    System.arraycopy(var0, 0, var2, 0, var0.length);
                    System.arraycopy(var0, 0, var2, 16, 8);
                } else {
                    System.arraycopy(var0, 0, var2, 0, 24);
                }

                DESedeKeySpec var4 = new DESedeKeySpec(var2);
                SecretKeyFactory var5 = SecretKeyFactory.getInstance("DESede");
                SecretKey var6 = var5.generateSecret(var4);
                Cipher var7 = Cipher.getInstance(b);
                var7.init(1, var6);
                return var7.doFinal(var3);
            }
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }

    public static byte[] trides_crypt2(byte[] var1, byte[] var0) {
        try {
            byte[] var2 = new byte[24];
            if (var1 == null) {
                return null;
            } else {
                if (var0.length == 16) {
                    System.arraycopy(var0, 0, var2, 0, var0.length);
                    System.arraycopy(var0, 0, var2, 16, 8);
                } else {
                    System.arraycopy(var0, 0, var2, 0, 24);
                }

                DESedeKeySpec var3 = new DESedeKeySpec(var2);
                SecretKeyFactory var4 = SecretKeyFactory.getInstance("DESede");
                SecretKey var5 = var4.generateSecret(var3);
                Cipher var6 = Cipher.getInstance(bb);
                var6.init(1, var5);
                return var6.doFinal(var1);
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public static byte[] trides_decrypt(byte[] var0, byte[] var1) {
        try {
            byte[] var2 = new byte[24];
            if (var1 == null) {
                return null;
            } else {
                byte[] var3 = new byte[(var1.length + 7) / 8 * 8];
                System.arraycopy(var1, 0, var3, 0, var1.length);
                if (var0.length == 16) {
                    System.arraycopy(var0, 0, var2, 0, var0.length);
                    System.arraycopy(var0, 0, var2, 16, 8);
                } else {
                    System.arraycopy(var0, 0, var2, 0, 24);
                }

                DESedeKeySpec var4 = new DESedeKeySpec(var2);
                SecretKeyFactory var5 = SecretKeyFactory.getInstance("DESede");
                SecretKey var6 = var5.generateSecret(var4);
                Cipher var7 = Cipher.getInstance(b);
                var7.init(2, var6);
                return var7.doFinal(var3);
            }
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }

    public static byte[] trides_decrypt2(byte[] var1, byte[] var0) {
        try {
            byte[] var2 = new byte[24];
            if (var1 == null) {
                return null;
            } else {
                if (var0.length == 16) {
                    System.arraycopy(var0, 0, var2, 0, var0.length);
                    System.arraycopy(var0, 0, var2, 16, 8);
                } else {
                    System.arraycopy(var0, 0, var2, 0, 24);
                }

                DESedeKeySpec var3 = new DESedeKeySpec(var2);
                SecretKeyFactory var4 = SecretKeyFactory.getInstance("DESede");
                SecretKey var5 = var4.generateSecret(var3);
                Cipher var6 = Cipher.getInstance(bb);
                var6.init(2, var5);
                return var6.doFinal(var1);
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }
}
