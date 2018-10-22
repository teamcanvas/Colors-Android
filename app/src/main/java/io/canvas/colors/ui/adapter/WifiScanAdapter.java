package io.canvas.colors.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import io.canvas.colors.data.WifiScanData;
import io.canvas.colors.databinding.ItemWifiScanBinding;
import io.canvas.colors.ui.WifiPinDialog;
import io.canvas.colors.ui.activities.ConnectWifiActivity;

public class WifiScanAdapter extends RecyclerView.Adapter<WifiScanViewHolder> {
    private Context context;
    private List<WifiScanData> list;

    public WifiScanAdapter(Context context, List<WifiScanData> list) {
        this.context = context;
        this.list = list;
    }

    public void clear() {
        list.clear();
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemChanged(position);
        notifyItemRangeChanged(position, list.size());
    }

    @NonNull
    @Override
    public WifiScanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemWifiScanBinding binding = ItemWifiScanBinding.inflate(layoutInflater, parent, false);
        return new WifiScanViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull WifiScanViewHolder holder, int position) {
        WifiScanData model = list.get(position);
        holder.binding.setWifiData(model);

        holder.itemView.setOnClickListener(view -> {
            ConnectWifiActivity.SSID = list.get(position).getSSID();
            final WifiPinDialog wifiPinDialog = new WifiPinDialog(view.getContext());
            wifiPinDialog.show();
        });
    }
}

class WifiScanViewHolder extends RecyclerView.ViewHolder {
    public ItemWifiScanBinding binding;

    public WifiScanViewHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }
}
