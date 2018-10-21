package io.canvas.colors.data;

import java.io.Serializable;

public class WifiScanData implements Serializable {

    private String SSID;
    private String RSSI;

    public WifiScanData(String SSID, String RSSI) {
        this.SSID = SSID;
        this.RSSI = RSSI;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getRSSI() {
        return RSSI;
    }

    public void setRSSI(String RSSI) {
        this.RSSI = RSSI;
    }
}
