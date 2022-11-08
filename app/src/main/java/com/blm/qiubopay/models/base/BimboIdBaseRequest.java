package com.blm.qiubopay.models.base;

import com.google.gson.annotations.SerializedName;

public class BimboIdBaseRequest {

    @SerializedName("seed")
    private String seed;

    @SerializedName("bimboId")
    private String bimboId;

    public BimboIdBaseRequest(String seed, String bimboId) {
        this.seed = seed;
        this.bimboId = bimboId;
    }

    public String getSeed() {
        return seed;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String getBimboId() {
        return bimboId;
    }

    public void setBimboId(String bimboId) {
        this.bimboId = bimboId;
    }
}
