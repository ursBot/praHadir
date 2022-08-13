package com.apps.prahadir;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;

public class AbsenActivity extends AppCompatActivity {

    private String grupNAMA, grupOWNER, grupID, userNAMA;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HideActionBar();

        BindExtra();
    }

    private void HideActionBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_absen);
    }

    private void BindExtra(){
        TextView namaGrup = findViewById(R.id.NamaGrup);
        Intent intent = getIntent();
        grupNAMA = intent.getStringExtra(GrupOwnerFragment.NAMA_GRUP);
        namaGrup.setText(grupNAMA);
    }
}