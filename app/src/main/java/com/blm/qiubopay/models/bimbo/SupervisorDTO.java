package com.blm.qiubopay.models.bimbo;

public class SupervisorDTO {

    private String supervisor_id;
    private String supervisor_name;
    private String supervisor_qualify;
    private String supervisor_qualify_count;
    private String supervisor_lastUpdate_qualify;

    public SupervisorDTO() {

    }

    public String getSupervisor_id() {
        return supervisor_id;
    }

    public void setSupervisor_id(String supervisor_id) {
        this.supervisor_id = supervisor_id;
    }

    public String getSupervisor_name() {
        return supervisor_name;
    }

    public void setSupervisor_name(String supervisor_name) {
        this.supervisor_name = supervisor_name;
    }

    public String getSupervisor_qualify() {
        return supervisor_qualify;
    }

    public void setSupervisor_qualify(String supervisor_qualify) {
        this.supervisor_qualify = supervisor_qualify;
    }

    public String getSupervisor_qualify_count() {
        return supervisor_qualify_count;
    }

    public void setSupervisor_qualify_count(String supervisor_qualify_count) {
        this.supervisor_qualify_count = supervisor_qualify_count;
    }

    public String getSupervisor_lastUpdate_qualify() {
        return supervisor_lastUpdate_qualify;
    }

    public void setSupervisor_lastUpdate_qualify(String supervisor_lastUpdate_qualify) {
        this.supervisor_lastUpdate_qualify = supervisor_lastUpdate_qualify;
    }
}
