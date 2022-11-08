package com.blm.qiubopay.models.sepomex;

public class StateInfo {
    private String state_name;
    private String state_code;

    public StateInfo(String state_name, String state_code)
    {
        this.state_name = state_name;
        this.state_code = state_code;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }
}
