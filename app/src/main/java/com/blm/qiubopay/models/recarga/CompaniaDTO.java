package com.blm.qiubopay.models.recarga;

import com.blm.qiubopay.models.services.ServicePackageModel;
import com.blm.qiubopay.utils.Globals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CompaniaDTO implements Serializable {

    private String id;
    private Integer name;
    private String description;
    private Integer image;
    private List<String> amounts;
    private List<PaqueteDTO> packages;
    private List<ServicePackageModel> services;
    private String prefix;

    public CompaniaDTO(@Globals.CarrierID String id, Integer name, String description, Integer image, List<String> amounts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.amounts = amounts;
        this.image = image;
    }

    public CompaniaDTO(@Globals.ServicePayId String id, Integer name, String description, Integer image, List<String> amounts, String prefix) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.amounts = amounts;
        this.image = image;
        this.prefix = prefix;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAmounts() {
        return amounts;
    }

    public void setAmounts(List<String> amounts) {
        this.amounts = amounts;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public List<PaqueteDTO> getPackages() {
        return packages;
    }

    public void setPackages(List<PaqueteDTO> packages) {
        this.packages = packages;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public List<ServicePackageModel> getServices() {
        return services;
    }

    public void setServices(List<ServicePackageModel> services) {
        this.services = services;
    }
}
