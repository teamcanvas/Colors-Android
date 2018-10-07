package io.canvas.colors.view.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import io.canvas.colors.data.ScanResultData;
import io.canvas.colors.databinding.ItemScanResultBinding;
import io.canvas.colors.view.PairActivity;

public class ScanResultAdapter extends RecyclerView.Adapter<ScanResultViewHolder> {
    private Context context;
    private ArrayList<BluetoothDevice> mLeDevices;

    public ScanResultAdapter() {
        super();
        mLeDevices = new ArrayList<BluetoothDevice>();
    }

    public void addDevice(BluetoothDevice device) {
        if(!mLeDevices.contains(device)) {
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
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
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
        //TODO 이 에러 해결하. 크롬 북마크바에 추가해둠
        ScanResultData model = list.get(position);
        holder.binding.setScanResultData(model);
        if (model.getDeviceName() == null) {
            holder.binding.deviceName.setText("Unknown Device");
        }

        holder.itemView.setOnClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                        //연결 작업 시~작!
                        final PairActivity pairActivity = new PairActivity();
                        //pairActivity.startConnect(model.getMacAddress()); //PairActivity에 선택한 맥 주소 전송
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .setCancelable(false)
                    .setMessage(list.get(position).getMacAddress() + "에 연결하시겠습니까?")
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
