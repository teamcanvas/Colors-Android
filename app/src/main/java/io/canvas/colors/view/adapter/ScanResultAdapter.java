package io.canvas.colors.view.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import io.canvas.colors.R;
import io.canvas.colors.databinding.ItemScanResultBinding;
import io.canvas.colors.view.ConnectWifiActivity;
import io.canvas.colors.view.DeviceControlActivity;

public class ScanResultAdapter extends RecyclerView.Adapter<ScanResultViewHolder> {
    private ArrayList<BluetoothDevice> mLeDevices;

    public ScanResultAdapter() {
        super();
        mLeDevices = new ArrayList<BluetoothDevice>();
    }

    public void addDevice(BluetoothDevice device) {
        if (!mLeDevices.contains(device)) {
            mLeDevices.add(device);
        }
    }

    public BluetoothDevice getDevice(int position) {
        return mLeDevices.get(position);
    }

    public void clear() {
        mLeDevices.clear();
    }

    @Override
    public int getItemCount() {
        return this.mLeDevices.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void removeItem(int position) {
        mLeDevices.remove(position);
        notifyItemChanged(position);
        notifyItemRangeChanged(position, mLeDevices.size());
    }

    @NonNull
    @Override
    public ScanResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemScanResultBinding binding = ItemScanResultBinding.inflate(layoutInflater, parent, false);
        return new ScanResultViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ScanResultViewHolder holder, int position) {
        BluetoothDevice device = mLeDevices.get(position);
        final String deviceName = device.getName();
        if (deviceName != null && deviceName.length() > 0) {
            holder.binding.deviceName.setText(deviceName);
        } else {
            holder.binding.deviceName.setText(R.string.unknown_device);
            //holder.binding.deviceAddress.setText(device.getAddress());
        }

        holder.itemView.setOnClickListener(view -> {
            new AlertDialog.Builder(view.getContext())
                    .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                        //연결 작업 시~작!
                        Intent intent = new Intent(view.getContext(), ConnectWifiActivity.class);
                        intent.putExtra(ConnectWifiActivity.EXTRAS_DEVICE_NAME, mLeDevices.get(position).getName());
                        intent.putExtra(ConnectWifiActivity.EXTRAS_DEVICE_ADDRESS, mLeDevices.get(position).getAddress());
                        view.getContext().startActivity(intent);
//                        final PairActivity pairActivity = new PairActivity();
//                        pairActivity.startConnect(mLeDevices.get(position).getName(), mLeDevices.get(position).getAddress()); //PairActivity에 선택한 맥 주소 전송
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .setCancelable(false)
                    .setMessage(mLeDevices.get(position).getAddress() + "에 연결하시겠습니까?")
                    .show();
        });
    }
}

class ScanResultViewHolder extends RecyclerView.ViewHolder {
    public ItemScanResultBinding binding;

    public ScanResultViewHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }
}
