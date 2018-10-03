package io.canvas.colors.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import io.canvas.colors.R;
import io.canvas.colors.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity {

    ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);

        binding.floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomeActivity.this, PairActivity.class);
            startActivity(intent);
        });

    }
}