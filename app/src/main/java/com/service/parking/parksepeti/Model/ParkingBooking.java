package com.service.parking.parksepeti.Model;

public class ParkingBooking {
    private String parkingId;
    private String transactionId;
    private String timestamp;
    private String by;
    private String spotHost;
    private String slotNo;
    private String parkingArea;

    public ParkingBooking() {
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public void setSpotHost(String spotHost) {
        this.spotHost = spotHost;
    }

    public void setSlotNo(String slotNo) {
        this.slotNo = slotNo;
    }

    public void setParkingArea(String parkingArea) {
        this.parkingArea = parkingArea;
    }
}
