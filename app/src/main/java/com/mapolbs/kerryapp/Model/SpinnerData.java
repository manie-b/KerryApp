package com.mapolbs.kerryapp.Model;

import androidx.annotation.NonNull;

public class SpinnerData {

    String pickupData;

    public SpinnerData(String pickupData) {
        this.pickupData = pickupData;
    }

    public String getPickupData() {
        return pickupData;
    }

    public void setPickupData(String pickupData) {
        this.pickupData = pickupData;
    }

    @NonNull
    @Override
    public String toString() {
        return pickupData;
    }
}
