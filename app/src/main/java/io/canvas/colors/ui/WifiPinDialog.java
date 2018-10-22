package io.canvas.colors.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import io.canvas.colors.R;
import io.canvas.colors.ui.activities.ConnectWifiActivity;

public class WifiPinDialog extends Dialog {
    private String SSID = "";
    private String SSID_PW = "";

    public WifiPinDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        //getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_connect_wifi);

        TextView textView = findViewById(R.id.tvSSID);
        textView.setText(ConnectWifiActivity.SSID);
        Log.d("WIFI", ConnectWifiActivity.SSID);
        TextInputEditText textInputEditText = findViewById(R.id.etPW);
        Button button = findViewById(R.id.connectWifi);
        button.setOnClickListener(view -> {
            Log.d("WIFI", textInputEditText.getText().toString());
            ConnectWifiActivity.SSID_PW = textInputEditText.getText().toString();
            dismiss();
        });
    }
}

