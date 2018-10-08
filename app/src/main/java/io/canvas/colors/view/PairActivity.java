package io.canvas.colors.view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.canvas.colors.R;
import io.canvas.colors.databinding.ActivityPairBinding;
import io.canvas.colors.service.BluetoothLeService;
import io.canvas.colors.view.adapter.ScanResultAdapter;

public class PairActivity extends AppCompatActivity {

    ActivityPairBinding binding;

    private ScanResultAdapter mAdapter;

    private static final String TAG = "BLE";

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    private ArrayList<BluetoothDevice> mLeDevices;

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pair);
        mHandler = new Handler();

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new ScanResultAdapter();
        binding.recyclerView.setAdapter(mAdapter);

        binding.fab.setEnabled(true);
        binding.fab.setBackgroundColor(getResources().getColor(R.color.online));
        binding.fab.setOnClickListener(view -> {
            Intent goMainIntent = new Intent(this, MainActivity.class);
            startActivity(goMainIntent);
        });

        scanLeDevice(true);
        //binding.animationView.playAnimation();
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            (device, rssi, scanRecord) -> runOnUiThread(() -> {
                //list.add(new ScanResultData(device))
                mAdapter.addDevice(device);
                mAdapter.notifyDataSetChanged();
                Log.d("SCAN RESULT", String.valueOf(device));
            });

    @Override
    protected void onResume() {
        super.onResume();
        // Initializes list view adapter.
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        scanLeDevice(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        mAdapter.clear();
    }

    public void startConnect(String name, String address) {
        Log.d("ADDRESS", address);
    }
}
