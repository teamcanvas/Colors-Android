package io.canvas.colors.view;

import android.Manifest;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.blakequ.bluetooth_manager_lib.BleManager;
import com.blakequ.bluetooth_manager_lib.connect.BluetoothConnectManager;
import com.blakequ.bluetooth_manager_lib.device.resolvers.GattAttributeResolver;
import com.blakequ.bluetooth_manager_lib.scan.BluetoothScanManager;
import com.blakequ.bluetooth_manager_lib.scan.bluetoothcompat.ScanCallbackCompat;
import com.blakequ.bluetooth_manager_lib.scan.bluetoothcompat.ScanResultCompat;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

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

    BluetoothGatt mBluetoothGatt;
    BluetoothGattCharacteristic characteristic;
    boolean enabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pair);
        scanManager = BleManager.getScanManager(this);
        connectManager = BleManager.getConnectManager(this);

        initPermission();

        binding.fab.setOnClickListener(view -> {

        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new ScanResultAdapter(this, list);
        binding.recyclerView.setAdapter(mAdapter);

        binding.fab.setEnabled(true);
        binding.fab.setBackgroundColor(getResources().getColor(R.color.online));
        binding.fab.setOnClickListener(view -> {
            Intent goMainIntent = new Intent(this, MainActivity.class);
            startActivity(goMainIntent);
        });

        connectManager.addConnectStateListener((address, state) -> {
            switch (state) {
                case CONNECTING:
                    Log.d("BLE", "Connecting to " + address);
                    break;
                case CONNECTED:
                    Log.d("BLE", "Connected!");
//                    binding.fab.setEnabled(true);
//                    binding.fab.setBackgroundColor(getResources().getColor(R.color.online));
                    break;
                case NORMAL:
                    break;
            }
        });
    }

    private void initPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(PairActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                scanDevice(getApplicationContext());
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(PairActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                finish();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("기기 검색을 위해 위치정보 권한을 허용해 주세요.")
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
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

                list.add(new ScanResultData(result.getLeDevice().getName(), result.getLeDevice().getAddress()));
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

    public void startConnect(String address) {

        Log.d("TAGTAG", address);
        connectManager.connect(address);

        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                UUID.fromString(GattAttributeResolver.CLIENT_CHARACTERISTIC_CONFIG));
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor);

        connectManager.getBluetoothGatt(address).connect();

        connectManager.setBluetoothGattCallback(new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
                Log.d("GATT", characteristic.toString());
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
                Log.d("GATT", characteristic.toString());
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorRead(gatt, descriptor, status);
                Log.d("DESCRIPTOR", descriptor.toString());
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorWrite(gatt, descriptor, status);
            }
        });
    }
}
