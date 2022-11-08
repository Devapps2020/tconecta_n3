package com.blm.qiubopay.conf;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Data_setting_devicesp530 - data class for storing SP530 parameters
 */
public class Data_setting_devicesp530 {
    private static final String m_className="Data_setting_devicesp530";

    public static final boolean DEFAULT_ALWAYS_USE_CRC_CHECKSUM_VALUE   = false;
    public static final boolean DEFAULT_SSL_CHANNEL_ONE_VALUE           = true;

    private Context m_context;
    public boolean m_bAlwaysUseCRC;
    public boolean m_bSSLChOne;

    private final String KEY_PACKET_ENCAPSULATE_LEVEL   = "PACKET_ENCAPSULATE_LEVEL";
    private static final String KEY_ALWAYS_USE_CRC      = "ALWAYS_USE_CRC";
    private static final String KEY_SSL_CHANNEL_ONE     = "SSL_CHANNEL_ONE";

    /**
     * Constructor for Data_setting_devicesp530
     * @param context context of application
     */
    public Data_setting_devicesp530(Context context) {
        m_context=context;
        refresh();
    }

    /**
     * Refresh parameters
     */
    public void refresh() {
        //SharedPreferences preference=m_context.getSharedPreferences(AppSectionsConstant.Storage.PREFERENCE_DEVICE_CONF_SETTING, Context.MODE_PRIVATE);

        m_bAlwaysUseCRC = DEFAULT_ALWAYS_USE_CRC_CHECKSUM_VALUE;//preference.getBoolean(KEY_ALWAYS_USE_CRC, DEFAULT_ALWAYS_USE_CRC_CHECKSUM_VALUE);
        m_bSSLChOne     = DEFAULT_SSL_CHANNEL_ONE_VALUE;//preference.getBoolean(KEY_SSL_CHANNEL_ONE, DEFAULT_SSL_CHANNEL_ONE_VALUE);
    }

    /**
     * Check for always use CRC as checksum method
     * @param context context of application
     * @return true if always use crc checksum; false otherwise
     */
    public static boolean isAlwaysUseCRC(Context context) {
        SharedPreferences preference=context.getSharedPreferences(AppSectionsConstant.Storage.PREFERENCE_DEVICE_CONF_SETTING, Context.MODE_PRIVATE);
        boolean bAlwaysUseCRC=preference.getBoolean(KEY_ALWAYS_USE_CRC, DEFAULT_ALWAYS_USE_CRC_CHECKSUM_VALUE);
        return bAlwaysUseCRC;
    }

    /**
     * Set for always use CRC as checksum method
     * @param val true for always use crc checsum; false otherwise
     */
    public void setAlwaysUseCRC(boolean val) {
        SharedPreferences preference=m_context.getSharedPreferences(AppSectionsConstant.Storage.PREFERENCE_DEVICE_CONF_SETTING, Context.MODE_PRIVATE);
        preference.edit().putBoolean(KEY_ALWAYS_USE_CRC, val).commit();
        refresh();
    }

    /**
     * Check for the use of SSL for logical channel one
     * @param context context of application
     * @return true if use of SSL for logical channel one; false otherwise
     */
    public static boolean isSSLChannelOne(Context context) {
        SharedPreferences preference=context.getSharedPreferences(AppSectionsConstant.Storage.PREFERENCE_DEVICE_CONF_SETTING, Context.MODE_PRIVATE);
        boolean bSSLChOne=preference.getBoolean(KEY_SSL_CHANNEL_ONE, DEFAULT_SSL_CHANNEL_ONE_VALUE);
        return bSSLChOne;
    }

    /**
     * Set the use of SSL for logical channel one
     * @param val true for enable; false for disable
     */
    public void setSSLChannelOne(boolean val) {
        SharedPreferences preference=m_context.getSharedPreferences(AppSectionsConstant.Storage.PREFERENCE_DEVICE_CONF_SETTING, Context.MODE_PRIVATE);
        preference.edit().putBoolean(KEY_SSL_CHANNEL_ONE, val).commit();
        refresh();
    }

}
