package com.mrapps.pinghomenetwork;

import java.util.ArrayList;
import java.util.List;

public class DeviceInfo {
    private String ip;
    private String hostname;
    private List<Integer> openPorts;

    public DeviceInfo(String ip, String hostname, List<Integer> openPorts) {
        this.ip = ip;
        this.hostname = (hostname != null && !hostname.isEmpty()) ? hostname : "Unknown";
        this.openPorts = (openPorts != null) ? openPorts : new ArrayList<>();
    }

    public String getIp() {
        return ip;
    }

    public String getHostname() {
        return hostname;
    }

    public List<Integer> getOpenPorts() {
        return openPorts;
    }
}
