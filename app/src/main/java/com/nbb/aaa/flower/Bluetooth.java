package com.nbb.aaa.flower;

public class Bluetooth {
    String address;

    public Bluetooth(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String bluetooth) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "{address: '" + address + "'}";
    }
}
