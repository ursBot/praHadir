package com.apps.prahadir;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String USERID, USERNAME, USEREMAIL;
    private FirebaseAuth mAuth;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Rename and change types of parameters
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        BindExtra();
        MakeColecction();
        CekUsername();

        mAuth = FirebaseAuth.getInstance();

        buttonUbahUsername.setOnClickListener(view1 -> UbahUsername());
        buttonUbahPassword.setOnClickListener(view1 -> UbahPassword());
        buttonLogout.setOnClickListener(view1 -> ButtonLogout());
        buttonTentangAplikasi.setOnClickListener(view1 -> AboutApps());

        return view;
    }

    private void MakeColecction(){
        DocumentReference userRoute = db.collection("User").document(USERID);
        Map<String, Object> userMap = new HashMap<>();

        userRoute.get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                userMap.put("Email", USEREMAIL);
                userMap.put("Username", "");

                userRoute.set(userMap);
            }
        });
    }

    private void CekUsername(){
        DocumentReference userRoute = db.collection("User").document(USERID);

        userRoute.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String currentUsername = documentSnapshot.getString("Username");
                if (Objects.requireNonNull(currentUsername).isEmpty())
                {
                    teksUsername.setText(USERNAME);
                }
                else
                {
                    teksUsername.setText(currentUsername);
                }
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.TeksUsername)
    TextView teksUsername;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.TeksEmail)
    TextView teksEmail;
    private void BindExtra(){
        Intent intent = requireActivity().getIntent();
        USERID = intent.getStringExtra(LoginFragment.USER_ID);
        USERNAME = intent.getStringExtra(LoginFragment.USER_USERNAME);
        USEREMAIL = intent.getStringExtra(LoginFragment.USER_EMAIL);
        teksEmail.setText(USEREMAIL);
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.UbahUsername)
    Button buttonUbahUsername;
    private void UbahUsername() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fragment_profile_username);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button buttonKonfirmasi = dialog.findViewById(R.id.BtnKonfirmasi);
        buttonKonfirmasi.setOnClickListener(v -> dialog.dismiss());

        Button buttonTutup = dialog.findViewById(R.id.BtnBatal);
        buttonTutup.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.UbahPassword)
    Button buttonUbahPassword;
    private void UbahPassword() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fragment_profile_password);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button buttonKonfirmasi = dialog.findViewById(R.id.BtnKonfirmasi);
        buttonKonfirmasi.setOnClickListener(v -> dialog.dismiss());

        Button buttonTutup = dialog.findViewById(R.id.BtnBatal);
        buttonTutup.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.Logout)
    Button buttonLogout;
    private void ButtonLogout() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fragment_profile_logout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button buttonKonfirmasi = dialog.findViewById(R.id.BtnKonfirmasi);
        buttonKonfirmasi.setOnClickListener(view -> Logout());

        Button buttonTutup = dialog.findViewById(R.id.BtnBatal);
        buttonTutup.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
    private void Logout() {
        mAuth.signOut();
        Intent intent=new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.TentangAplikasi)
    Button buttonTentangAplikasi;
    private void AboutApps() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fragment_profile_about_apps);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button buttonKonfirmasi = dialog.findViewById(R.id.BtnKonfirmasi);

        buttonKonfirmasi.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

}
