package com.mrapps.pinghomenetwork.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrapps.pinghomenetwork.R;
import com.mrapps.pinghomenetwork.DeviceInfo;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private List<DeviceInfo> deviceList;

    public DeviceAdapter(List<DeviceInfo> deviceList) {
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeviceInfo device = deviceList.get(position);

        holder.txtIp.setText("IP: " + device.getIp());
        holder.txtHost.setText("Host: " + device.getHostname());

        List<Integer> ports = device.getOpenPorts();
        if (ports == null || ports.isEmpty()) {
            holder.txtPorts.setText("Open Ports: None");
        } else {
            holder.txtPorts.setText("Open Ports: " + ports.toString());
        }
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtIp, txtHost, txtPorts;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIp = itemView.findViewById(R.id.txtIp);
            txtHost = itemView.findViewById(R.id.txtHost);
            txtPorts = itemView.findViewById(R.id.txtPorts);
        }
    }

}
