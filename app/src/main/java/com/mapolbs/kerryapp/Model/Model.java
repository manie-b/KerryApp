package com.mapolbs.kerryapp.Model;

public class Model {

    private String customername;

    public Model(String cust_name) {
        this.customername = cust_name;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }
}
