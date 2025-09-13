package com.mrapps.pinghomenetwork;

import java.util.ArrayList;
import java.util.List;

public class DeviceInfo {
    private final String ip;
    private final String hostname;
    private final List<Integer> openPorts;

    public DeviceInfo(String ip, String hostname, List<Integer> openPorts) {
        this.ip = ip;
        this.hostname = hostname;
        this.openPorts = openPorts;
    }

    public String getIp() { return ip; }
    public String getHostname() { return hostname; }
    public List<Integer> getOpenPorts() { return openPorts; }
}