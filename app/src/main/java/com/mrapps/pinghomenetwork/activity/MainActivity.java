package com.mrapps.pinghomenetwork.activity;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mrapps.pinghomenetwork.R;
import com.mrapps.pinghomenetwork.adapter.DeviceAdapter;
import com.mrapps.pinghomenetwork.DeviceInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private final List<DeviceInfo> deviceList = new ArrayList<>();
    private final Set<String> scannedIps = new HashSet<>();
    private DeviceAdapter adapter;

    private static final int THREAD_COUNT = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeviceAdapter(deviceList);
        recyclerView.setAdapter(adapter);

        Button btnScan = findViewById(R.id.btnScan);
        btnScan.setOnClickListener(v -> scanNetwork());
    }

    private void scanNetwork() {
        deviceList.clear();
        scannedIps.clear();
        adapter.notifyDataSetChanged();

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        int ipInt = wm.getConnectionInfo().getIpAddress();
        String myIp = Formatter.formatIpAddress(ipInt);

        String prefix = myIp.substring(0, myIp.lastIndexOf(".") + 1);

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        // Step 1: Get ARP devices (fast scan)
        for (String ip : getArpDevices()) {
            executor.execute(() -> checkAndAddDevice(ip));
        }

        // Step 2: Subnet scan (fallback, in case ARP missed something)
        for (int i = 1; i < 255; i++) {
            String host = prefix + i;
            executor.execute(() -> checkAndAddDevice(host));
        }

        executor.shutdown();
    }

    private void checkAndAddDevice(String ip) {
        if (scannedIps.contains(ip)) return;

        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            if (inetAddress.isReachable(800)) {
                String hostname = inetAddress.getCanonicalHostName();

                DeviceInfo device = new DeviceInfo(ip, hostname, new ArrayList<>()); // no port filter

                scannedIps.add(ip);
                runOnUiThread(() -> {
                    deviceList.add(device);
                    adapter.notifyItemInserted(deviceList.size() - 1);
                });
            }
        } catch (Exception ignored) {}
    }

    private List<String> getArpDevices() {
        List<String> devices = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("/proc/net/arp"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 4 && !parts[0].equals("IP")) {
                    devices.add(parts[0]);
                }
            }
        } catch (Exception ignored) {}
        return devices;
    }
}
