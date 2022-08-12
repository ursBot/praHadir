package com.apps.prahadir;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private final String userID = Objects.requireNonNull(mUser).getUid();
    private final String userNAME = mUser.getEmail();
    private final String userEMAIL = mUser.getEmail();

    DocumentReference docUID = db.collection("User").document(userID);

    private String judul, input, error;

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
        View view = inflater.inflate(R.layout.fragment_main_profile, container, false);
        ButterKnife.bind(this, view);

        GetUser();

        buttonUbahUsername.setOnClickListener(view1 -> UbahUsername());
        buttonUbahPassword.setOnClickListener(view1 -> UbahPassword());
        buttonLogout.setOnClickListener(view1 -> ButtonLogout());
        buttonTentangAplikasi.setOnClickListener(view1 -> AboutApps());

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.TeksUsername)
    TextView teksUsername;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.TeksEmail)
    TextView teksEmail;
    private void GetUser(){
        docUID.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists())
            {
                String s = documentSnapshot.getString("Username");
                if (!Objects.requireNonNull(s).isEmpty())
                {
                    teksUsername.setText(s);
                }
                else
                {
                    teksUsername.setText(userNAME);
                }
                teksEmail.setText(userEMAIL);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.UbahUsername)
    Button buttonUbahUsername;
    private void UbahUsername() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        judul = "Masukkan Username Baru";
        input = "Username Baru";
        error = "Username Salah!";

        TextView judulDialog = dialog.findViewById(R.id.JudulDialog);
        EditText inputDialog = dialog.findViewById(R.id.InputDialog);
        TextView errorDialog = dialog.findViewById(R.id.ErrorDialog);
        Button btnBatal = dialog.findViewById(R.id.BtnBatal);
        Button btnKonfirmasi = dialog.findViewById(R.id.BtnKonfirmasi);

        judulDialog.setText(judul);
        inputDialog.setHint(input);
        errorDialog.setText(error);
        errorDialog.setVisibility(View.GONE);

        btnKonfirmasi.setOnClickListener(v -> {
            String cek = inputDialog.getText().toString();
            if (cek.isEmpty()){
                errorDialog.setVisibility(View.VISIBLE);
            }
            else
            {
                docUID.update("Username", cek);
                Toast.makeText(getActivity(), "Username Berhasil Dirubah", Toast.LENGTH_SHORT).show();
                GetUser();

                dialog.dismiss();
            }
        });
        btnBatal.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.UbahPassword)
    Button buttonUbahPassword;
    private void UbahPassword() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        judul = "Masukkan Password Baru";
        input = "Password Baru";
        error = "Password Salah!";

        TextView judulDialog = dialog.findViewById(R.id.JudulDialog);
        EditText inputDialog = dialog.findViewById(R.id.InputDialog);
        TextView errorDialog = dialog.findViewById(R.id.ErrorDialog);
        Button btnBatal = dialog.findViewById(R.id.BtnBatal);
        Button btnKonfirmasi = dialog.findViewById(R.id.BtnKonfirmasi);

        judulDialog.setText(judul);
        inputDialog.setHint(input);
        inputDialog.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        errorDialog.setText(error);
        errorDialog.setVisibility(View.GONE);

        btnKonfirmasi.setOnClickListener(v -> {
            String cek = inputDialog.getText().toString();
            if (cek.isEmpty() || cek.length()<6){
                errorDialog.setVisibility(View.VISIBLE);
            }
            else
            {
                mUser.updatePassword(cek)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Password Berhasil Dirubah", Toast.LENGTH_SHORT).show();
                            }
                        });

                dialog.dismiss();
            }
        });
        btnBatal.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.Logout)
    Button buttonLogout;
    private void ButtonLogout() {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView judulDialog = dialog.findViewById(R.id.JudulDialog);
        judul = "Apakah Anda Akan Keluar?";
        judulDialog.setText(judul);

        Button btnBatal = dialog.findViewById(R.id.BtnBatal);
        Button btnKonfirmasi = dialog.findViewById(R.id.BtnKonfirmasi);

        btnKonfirmasi.setOnClickListener(view -> Logout());
        btnBatal.setOnClickListener(v -> dialog.dismiss());

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
        dialog.setContentView(R.layout.fragment_main_profile_about);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        Button buttonKonfirmasi = dialog.findViewById(R.id.BtnKonfirmasi);
        buttonKonfirmasi.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
