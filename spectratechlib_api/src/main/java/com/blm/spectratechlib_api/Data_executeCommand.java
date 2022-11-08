package com.blm.spectratechlib_api;

import com.spectratech.lib.Callback;

public class Data_executeCommand {
    /**
     * Variable to store command code
     */
    public byte[] m_commandCode;
    /**
     * Variable to store data
     */
    public byte[] m_dataBuf;
    /**
     * Varialbe to store callback function for finish executing command
     */
    public Callback m_cb_finish;

    public boolean m_bIncludeUIExtraTlv;

    /**
     * Constructor for Data_executeCommand
     */
    public Data_executeCommand() {
        init();
    }

    private void init() {
        m_commandCode=null;
        m_dataBuf=null;
        m_bIncludeUIExtraTlv=true;
        m_cb_finish=null;
    }

}
