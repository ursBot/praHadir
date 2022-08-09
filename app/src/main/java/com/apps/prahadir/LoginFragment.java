package com.apps.prahadir;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginFragment extends Fragment {

    public static final String USER_ID = "UserID";
    public static final String USER_USERNAME = "UserUsername";
    public static final String USER_EMAIL = "UserEmail";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressDialog mLoadingBar;

    public LoginFragment() {
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, view);

        mAuth=FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mLoadingBar=new ProgressDialog(getActivity());

        Login();
        Daftar();
        buttonLupasPassword.setOnClickListener(view1 -> LupaPassword());


        return view;

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.BtnLogin)
    Button buttonLogin;
    private void Login() {
        buttonLogin.setOnClickListener(view -> checkCrededentials());
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.BtnDaftar)
    Button buttonDaftar;
    private void Daftar() {
        buttonDaftar.setOnClickListener(view -> replaceFragment(new DaftarFragment()));
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.BtnLupaPassword)
    Button buttonLupasPassword;
    private void LupaPassword() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fragment_login_email);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button buttonKonfirmasi = dialog.findViewById(R.id.BtnKonfirmasi);
        buttonKonfirmasi.setOnClickListener(v -> {dialog.dismiss(); PasswordBaru();});

        Button buttonTutup = dialog.findViewById(R.id.BtnBatal);
        buttonTutup.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void PasswordBaru() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fragment_login_password);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button buttonKonfirmasi = dialog.findViewById(R.id.BtnKonfirmasi);
        buttonKonfirmasi.setOnClickListener(v -> dialog.cancel());

        Button buttonTutup = dialog.findViewById(R.id.BtnBatal);
        buttonTutup.setOnClickListener(v -> dialog.cancel());

        dialog.show();
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.MasukkanEmail)
    EditText inputEmail;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.MasukkanPassword)
    EditText inputPassword;
    private void checkCrededentials() {
        String email=inputEmail.getText().toString();
        String password=inputPassword.getText().toString();

        if (email.isEmpty()){
            showErorr(inputEmail, "Email Salah, silahkan diisi ulang!");
        }
        else if (password.isEmpty() || password.length()<=6){
            showErorr(inputPassword, "Password Minimal 6 Karakter!");
        }
        else
        {
            mLoadingBar.setTitle("Login");
            mLoadingBar.setMessage("Please Wait");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), "Successfully Login", Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    String currentID = mUser.getUid();
                    String currentUsername = mUser.getEmail();
                    String currentEmail = mUser.getEmail();
                    intent.putExtra(USER_ID, currentID);
                    intent.putExtra(USER_USERNAME, currentUsername);
                    intent.putExtra(USER_EMAIL, currentEmail);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                    mLoadingBar.dismiss();
                }
            });
        }
    }

    private void showErorr(EditText input, String s){
        input.setError(s);
        input.requestFocus();
    }
}