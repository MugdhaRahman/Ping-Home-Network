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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private List<DeviceInfo> deviceList = new ArrayList<>();
    private DeviceAdapter adapter;

    private final int[] PORTS = {22, 80, 139, 443, 445, 8009, 5353};
    private static final int THREAD_COUNT = 20; // instead of 50

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
        adapter.notifyDataSetChanged();

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        int ipInt = wm.getConnectionInfo().getIpAddress();
        String myIp = Formatter.formatIpAddress(ipInt);

        String prefix = myIp.substring(0, myIp.lastIndexOf(".") + 1);

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 1; i < 255; i++) {
            String host = prefix + i;
            executor.execute(() -> {
                List<Integer> openPorts = getOpenPorts(host);
                if (!openPorts.isEmpty()) {
                    String hostname = resolveHostname(host);
                    DeviceInfo device = new DeviceInfo(host, hostname, openPorts);
                    runOnUiThread(() -> {
                        deviceList.add(device);
                        adapter.notifyDataSetChanged();
                    });
                }
            });
        }

        executor.shutdown();
    }

    private List<Integer> getOpenPorts(String ip) {
        List<Integer> openPorts = new ArrayList<>();
        for (int port : PORTS) {
            if (isReachable(ip, port, 200)) {
                openPorts.add(port);
            }
        }
        return openPorts;
    }

    private boolean isReachable(String ip, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, port), 500); // 500ms
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private String resolveHostname(String ip) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            return inetAddress.getCanonicalHostName();
        } catch (Exception e) {
            return "Unknown";
        }
    }
}
