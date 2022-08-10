package com.apps.prahadir;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.Objects;
import com.apps.prahadir.databinding.ActivityAbsensiBinding;

public class AbsensiActivity extends AppCompatActivity {

    ActivityAbsensiBinding binding;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HideActionBar();
        binding = ActivityAbsensiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void HideActionBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}