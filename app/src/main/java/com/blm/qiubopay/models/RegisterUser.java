package com.blm.qiubopay.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RegisterUser implements Serializable {

    private String firstName;
    private String secondName;
    private String lastName;
    private String mobileNumber;

    private Parent parent;
    private List<DeviceList> deviceList;

    public RegisterUser(String firstName, String secondName, String lastName, String mobileNumber){
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;

        parent = new Parent();
        parent.setLookupType("DEVICE");
        parent.setLookupValue("161150");
        //this.parent = new ArrayList<Parent>();
        //this.parent.add(parent1);

        DeviceList deviceList1 = new DeviceList();
        deviceList1.setDeviceId(mobileNumber);
        deviceList1.setDeviceType("Mobile");
        deviceList1.setLanguage("Spanish");
        this.deviceList = new ArrayList<DeviceList>();
        this.deviceList.add(deviceList1);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public List<DeviceList> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceList> deviceList) {
        this.deviceList = deviceList;
    }
}
