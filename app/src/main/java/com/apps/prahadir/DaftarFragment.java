package com.apps.prahadir;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DaftarFragment extends Fragment {

    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;

    public DaftarFragment() {
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
        View view = inflater.inflate(R.layout.fragment_login_daftar, container, false);
        ButterKnife.bind(this, view);

        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(getActivity());

        Daftar();
        PunyaAkun();

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = this.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, fragment);
        fragmentTransaction.commit();
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.BtnDaftarAkun)
    Button buttonDaftar;
    private void Daftar() {
        buttonDaftar.setOnClickListener(view -> Auth());
    }
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.MasukkanEmail)
    EditText inputEmail;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.MasukkanPassword)
    EditText inputPassword;
    private void Auth() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (email.isEmpty() || email.length()<6){
            showErorr(inputEmail, "Email Salah, silahkan diisi ulang!");
        }
        else if (password.isEmpty() || password.length()<6){
            showErorr(inputPassword, "Password Minimal 6 Karakter!");
        }
        else
        {
            mLoadingBar.setTitle("Registration");
            mLoadingBar.setMessage("Please Wait");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), "Successfully Registration", Toast.LENGTH_SHORT).show();
                    replaceFragment(new LoginFragment());
                }
                else
                {
                    Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                }
                mLoadingBar.dismiss();
            });
        }
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.BtnSudahPunyaAkun)
    Button buttonSudahPunya;
    private void PunyaAkun() {
        buttonSudahPunya.setOnClickListener(view -> replaceFragment(new LoginFragment()));
    }

    private void showErorr(EditText input, String s){
        input.setError(s);
        input.requestFocus();
    }
}