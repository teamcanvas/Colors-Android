package io.canvas.colors.data;

import java.io.Serializable;

public class ScanResultData implements Serializable {

    private String deviceName;
    private String macAddress;

    public ScanResultData(String deviceName, String macAddress) {
        this.deviceName = deviceName;
        this.macAddress = macAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
