package com.blm.qiubopay.models.K206;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blm.qiubopay.R;

import java.util.ArrayList;
import java.util.List;

//import com.example.android.multidex.myCApplication.R;

/**
 * list of bluetooth
 *
 * Created by zhanghuan on 15-8-6
 */
public class DeviceAdapter extends BaseAdapter {

    private List<BluetoothDevice> mDevicesList;
    private LayoutInflater mInflator;

    //private Logger log;

    public DeviceAdapter(Context context) {
        super();
        mDevicesList = new ArrayList<BluetoothDevice>();
        this.mInflator = LayoutInflater.from(context);
        //log = LoggerFactory.getLogger(DeviceAdapter.class);
    }

    public void addDevice(BluetoothDevice device) {
        for (int i = 0; i < mDevicesList.size(); i++) {
            final BluetoothDevice bleDevice = this.getDevice(i);
            if (bleDevice.getAddress().equalsIgnoreCase(device.getAddress()))
                return;
        }
        mDevicesList.add(device);
    }

    public BluetoothDevice getDevice(int position) {
        return mDevicesList.get(position);
    }

    public void clear() {
        mDevicesList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDevicesList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDevicesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = mInflator.inflate(R.layout.item_dispositivo, null, false);

        TextView name = (TextView) view.findViewById(R.id.text_nombre);
        name.setText(mDevicesList.get(position).getName() + "\n[" + mDevicesList.get(position).getAddress() + "]");

        return view;


    }

}
