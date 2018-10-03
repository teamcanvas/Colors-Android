package io.canvas.colors.view;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blakequ.bluetooth_manager_lib.BleManager;
import com.blakequ.bluetooth_manager_lib.connect.BluetoothConnectManager;
import com.blakequ.bluetooth_manager_lib.scan.BluetoothScanManager;
import com.blakequ.bluetooth_manager_lib.scan.bluetoothcompat.ScanCallbackCompat;
import com.blakequ.bluetooth_manager_lib.scan.bluetoothcompat.ScanResultCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.canvas.colors.R;
import io.canvas.colors.data.ScanResultData;
import io.canvas.colors.databinding.ActivityPairBinding;
import io.canvas.colors.view.adapter.ScanResultAdapter;

public class PairActivity extends AppCompatActivity {

    ActivityPairBinding binding;
    private BluetoothScanManager scanManager;
    private BluetoothConnectManager connectManager;

    private ScanResultAdapter mAdapter;
    private List<ScanResultData> list = new ArrayList<>();

    private static final String TAG = "BLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pair);
        scanManager = BleManager.getScanManager(this);
        connectManager = BleManager.getConnectManager(this);

//        binding.fab.setOnClickListener(view -> {
//            Intent intent = new Intent(PairActivity.this, MainActivity.class);
//            startActivity(intent);
//        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new ScanResultAdapter(this, list);
        binding.recyclerView.setAdapter(mAdapter);

        connectManager.addConnectStateListener((address, state) -> {
            switch (state) {
                case CONNECTING:
                    Log.d("BLE", "Connecting to " + address);
                    break;
                case CONNECTED:
                    Log.d("BLE", "Connected!");
                    binding.fab.setEnabled(true);
                    binding.fab.setBackgroundColor(getColor(R.color.online));
                    break;
                case NORMAL:
                    break;
            }
        });
    }

    private void scanDevice(Context view) {
        //scanManager.addScanFilterCompats(new ScanFilterCompat.Builder().setDeviceName("TRASHGO").build());
        scanManager.setScanOverListener(() -> {
            //scan over of one times
        });

        scanManager.setScanCallbackCompat(new ScanCallbackCompat() {
            @Override
            public void onBatchScanResults(List<ScanResultCompat> results) {
                super.onBatchScanResults(results);
            }

            @Override
            public void onScanFailed(final int errorCode) {
                super.onScanFailed(errorCode);
                //code
                Log.d(TAG, String.valueOf(errorCode));
            }

            @Override
            public void onScanResult(int callbackType, ScanResultCompat result) {
                super.onScanResult(callbackType, result);

                //TODO iBeacon에서 일반 BLE디바이스로 변경하여 로그를 출력합니다.
                Log.d(TAG, "NAME: " + result.getLeDevice().getName());
                Log.d(TAG, "RSSI: " + String.valueOf(result.getLeDevice().getRssi()));
                Log.d(TAG, "MAC ADDRESS: " + result.getLeDevice().getDevice().getAddress());
                Log.d(TAG, "SERVICE UUID: " + Arrays.toString(result.getDevice().getUuids()));

                list.add(new ScanResultData(result.getLeDevice().getAddress(), result.getLeDevice().getAddress()));
                mAdapter.notifyDataSetChanged();

                if (result.getLeDevice().getRssi() > -35) { //존나 가까우면 연결합니다.
                    scanManager.stopCycleScan(); //일단 스캔 멈춤
                    //TODO 아래 주석 확인 ㄱ
                    /**
                     * 이미 검증을 마친 기능입니다. 잘 작동합니다.
                     * 또한 위에서 이미 TRASHGO만 필터링 했고 근접 거리에서만 연결을 요청하기에 중복 필터는.. 필요없다고 생각했읍니다.
                     * 따라서, 바로 MAC주소를 가져와서 ConnectionService에 연결해달라고 징징댑니다.
                     */
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        scanManager.startScanNow();
        //scanManager.startScanNow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scanManager.stopCycleScan(); //stop scan
    }
}
