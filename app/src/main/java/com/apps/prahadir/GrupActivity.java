package com.apps.prahadir;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.Objects;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.apps.prahadir.databinding.ActivityGrupBinding;

public class GrupActivity extends AppCompatActivity {

    private String USERID, GRUPNAMA, GRUPOWNER, GRUPID;

    ActivityGrupBinding binding;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HideActionBar();
        binding = ActivityGrupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BindExtra();
        CheckOwner();
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
        USERID = intent.getStringExtra(HomeFragment.USER_ID);
        GRUPNAMA = intent.getStringExtra(HomeFragment.NAMA_GRUP);
        GRUPOWNER = intent.getStringExtra(HomeFragment.OWNER_GRUP);
        GRUPID = intent.getStringExtra(HomeFragment.ID_GRUP);
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