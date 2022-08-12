package com.apps.prahadir;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.util.Objects;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.apps.prahadir.databinding.ActivityGrupBinding;

public class GrupActivity extends AppCompatActivity {

    private String GRUPOWNER;

    ActivityGrupBinding binding;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HideActionBar();
        binding = ActivityGrupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BindExtra();
    }

    private void HideActionBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

    private void BindExtra(){
        Intent intent = getIntent();
        GRUPOWNER = intent.getStringExtra(HomeFragment.OWNER_GRUP);
        CheckOwner();
    }

    private void CheckOwner() {
        if (GRUPOWNER.equals("true"))
        {
            replaceFragment(new GrupOwnerFragment());
        }
        else
        {
            replaceFragment(new GrupAnggotaFragment());
        }
    }
}