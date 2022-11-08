package com.blm.qiubopay.tools;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.nexgo.common.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

public class ClsUtils {

    static public boolean createBond(Class btClass, BluetoothDevice btDevice)
            throws Exception {
        Method createBondMethod = btClass.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        LogUtils.error("createBond--returnValue:{}", returnValue);
        return returnValue.booleanValue();
    }


    static public boolean removeBond(Class btClass, BluetoothDevice btDevice)
            throws Exception {
        Method removeBondMethod = btClass.getMethod("removeBond");
        Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    static public boolean setPin(Class btClass, BluetoothDevice btDevice,
                                 String str) throws Exception {
        try {
            Method removeBondMethod = btClass.getDeclaredMethod("setPin",
                    new Class[] {byte[].class});
            Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice,
                    new Object[]{str.getBytes()});
            LogUtils.error("returnValue:{}", returnValue);
        } catch (SecurityException e) {
            LogUtils.error("setPin:{}", e.toString());
        } catch (IllegalArgumentException e) {
            LogUtils.error("setPin:{}", e.toString());
        } catch (Exception e) {
            LogUtils.error("setPin:{}", e.toString());
        }
        return true;
    }

    // 取消用户输入
    static public boolean cancelPairingUserInput(Class btClass,
                                                 BluetoothDevice device)
            throws Exception {
        Method createBondMethod = null;
        Boolean returnValue = false;
        try {
            createBondMethod = btClass.getMethod("cancelPairingUserInput");
            returnValue = (Boolean) createBondMethod.invoke(device);
            //cancelBondProcess(btClass,device);
            LogUtils.error("returnValue:{}", returnValue);
        } catch (SecurityException e) {
            LogUtils.error("setPin:{}", e.toString());
        } catch (IllegalArgumentException e) {
            LogUtils.error("setPin:{}", e.toString());
        } catch (Exception e) {
            LogUtils.error("setPin:{}", e.toString());
        }
        return returnValue.booleanValue();
    }

    // 取消配对
    static public boolean cancelBondProcess(Class btClass,
                                            BluetoothDevice device)
            throws Exception {
        Method createBondMethod = btClass.getMethod("cancelBondProcess");
        Boolean returnValue = (Boolean) createBondMethod.invoke(device);
        return returnValue.booleanValue();
    }

    /**
     * @param clsShow
     */
    static public void printAllInform(Class clsShow) {
        try {
            // 取得所有方法
            Method[] hideMethod = clsShow.getMethods();
            int i = 0;
            for (; i < hideMethod.length; i++) {
                LogUtils.error("method name", hideMethod[i].getName() + ";and the i is:"
                        + i);
            }
            // 取得所有常量
            Field[] allFields = clsShow.getFields();
            for (i = 0; i < allFields.length; i++) {
                LogUtils.error("Field name", allFields[i].getName());
            }
        } catch (SecurityException e) {
            LogUtils.error("printAllInform:{}", e.toString());
        } catch (IllegalArgumentException e) {
            LogUtils.error("printAllInform:{}", e.toString());
        } catch (Exception e) {
            LogUtils.error("printAllInform:{}", e.toString());
        }
    }



    static public int getConnectionState(Class btClass, BluetoothAdapter btAdapter)
            throws Exception {
        Method createBondMethod = btClass.getMethod("getConnectionState");
        Integer returnValue = (Integer) createBondMethod.invoke(btAdapter);
        return returnValue.intValue();
    }


    static public boolean pair(String strAddr, String strPsw)
    {
        boolean result = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.cancelDiscovery();
        if (!bluetoothAdapter.isEnabled())
        {
            bluetoothAdapter.enable();
        }

        if (!BluetoothAdapter.checkBluetoothAddress(strAddr))
        {
            LogUtils.error("mylog,devAdd un effient!");
        }
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(strAddr);

        if (device.getBondState() != BluetoothDevice.BOND_BONDED){
            try {
                LogUtils.error("mylog,NOT BOND_BONDED");
                ClsUtils.setPin(device.getClass(), device, strPsw);
                ClsUtils.createBond(device.getClass(), device);

                result = true;
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                LogUtils.error("mylog,setPiN failed!");
                e.printStackTrace();
            } //
        } else {
            LogUtils.error("mylog,HAS BOND_BONDED");
            try {
                ClsUtils.createBond(device.getClass(), device);
                ClsUtils.setPin(device.getClass(), device, strPsw);
                ClsUtils.createBond(device.getClass(), device);

                result = true;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                LogUtils.error("mylog,setPiN failed!");
                e.printStackTrace();
            }
        }
        return result;
    }

    public static boolean matchBtDevice(BluetoothDevice mUserPairDevice) {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (null == defaultAdapter) {
            return false;
        }
        Set<BluetoothDevice> mPairedDevices = defaultAdapter.getBondedDevices();
        Iterator<BluetoothDevice> mPairedDevicesIt = mPairedDevices.iterator();
        try {
            while (mPairedDevicesIt.hasNext()) {
                BluetoothDevice bd = mPairedDevicesIt.next();
                if (bd.getAddress().equals(mUserPairDevice.getAddress())) {
                    //mCurrentDevice = mUserPairDevice;
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
