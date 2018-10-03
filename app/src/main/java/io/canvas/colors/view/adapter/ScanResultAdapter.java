package io.canvas.colors.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private List<ScanResultData> list;

    public ScanResultAdapter(Context context, List<ScanResultData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemChanged(position);
        notifyItemRangeChanged(position, list.size());
    }

    @NonNull
    @Override
    public ScanResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemScanResultBinding binding = ItemScanResultBinding.inflate(layoutInflater, parent,false);
        return new ScanResultViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ScanResultViewHolder holder, int position) {
        ScanResultData model = list.get(position);
        holder.binding.setScanResultData(model);

        holder.itemView.setOnClickListener(view -> {
            new AlertDialog.Builder(context)
                    .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                        //연결 작업 시~작!
                        final PairActivity pairActivity = new PairActivity();
                        pairActivity.startConnect(model.getMacAddress()); //PairActivity에 선택한 맥 주소 전송
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
