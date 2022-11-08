package com.blm.qiubopay.models.publicity;



import com.blm.qiubopay.models.base.QPAY_BaseResponse;

import java.io.Serializable;

public class QPAY_TipsAdvertising_publicities extends QPAY_BaseResponse implements Serializable {

    private Integer id;
    private String title;
    private String image;
    private String carrouselImage;
    private String buttonText;
    private String description;
    private String link;
    private String creation_date;
    private Integer companyId;
    private Integer position;

    public QPAY_TipsAdvertising_publicities() {

    }

    public QPAY_TipsAdvertising_publicities(Integer id, String title, String image, String carrouselImage,
                                            String buttonText, String description, String link,
                                            String creation_date, Integer companyId, Integer position) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.carrouselImage = carrouselImage;
        this.buttonText = buttonText;
        this.description = description;
        this.link = link;
        this.creation_date = creation_date;
        this.companyId = companyId;
        this.position = position;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getCarrouselImage() {
        return carrouselImage;
    }

    public void setCarrouselImage(String carrouselImage) {
        this.carrouselImage = carrouselImage;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}