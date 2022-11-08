package com.blm.qiubopay.models.apuestas;

import java.io.Serializable;

public class WagerResult implements Serializable {
    private int Id;
    private String Name;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
