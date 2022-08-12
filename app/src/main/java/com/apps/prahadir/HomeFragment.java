package com.apps.prahadir;

import androidx.fragment.app.Fragment;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    private final String userID = Objects.requireNonNull(mUser).getUid();
    private final String userNAME = mUser.getEmail();
    private final String userEMAIL = mUser.getEmail();

    DocumentReference docUID = db.collection("User").document(userID);
    CollectionReference collGrup = docUID.collection("Grup");
    DocumentReference docGID;
    DocumentReference dbGrup;

    public static final String NAMA_USER = "NamaUSER";
    public static final String NAMA_GRUP = "NamaGrup";
    public static final String ID_GRUP = "IDGrup";
    public static final String OWNER_GRUP = "OwnerGrup";

    private GrupAdapter grupAdapter;
    private final ArrayList<Grup> grupList = new ArrayList<>();

    public HomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        AddFieldDoc();
        GetUser();
        ReadGrup();
        GrupView();
        GrupBaru();
        ClickGrup();

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.TeksEmail)
    TextView teksEmail;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.TeksUsername)
    TextView teksUsername;
    private void AddFieldDoc(){
        docUID.get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists())
            {
                Map<String, Object> HashMap = new HashMap<>();
                HashMap.put("Email", userEMAIL);
                HashMap.put("Username", "");

                docUID.set(HashMap);
            }
            GetUser();
        });
    }

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

    private void ReadGrup(){
        collGrup.orderBy("Nama").get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                grupList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    grupList.add(new Grup(document.getString("Nama"),
                            Objects.requireNonNull(document.getBoolean("Owner")).toString(),
                            document.getString("ID")));
                }
                grupView.setAdapter(grupAdapter);
            }
        });
        GrupKosong();
    }
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.TeksBelumPunyaGrup)
    TextView grupKosong;
    private void GrupKosong() {
        collGrup.get().addOnSuccessListener(queryDocumentSnapshots ->
        {
            if (queryDocumentSnapshots.isEmpty())
            {
                grupKosong.setVisibility(View.VISIBLE);
            }
            else
            {
                grupKosong.setVisibility(View.GONE);
            }
        });
    }
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.GrupView)
    ListView grupView;
    private void GrupView() {
        grupAdapter = new GrupAdapter(this, grupList);
        grupView.setAdapter(grupAdapter);
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.GrupBaru)
    Button buttonGrupBaru;
    @SuppressLint("SetTextI18n")
    private void GrupBaru() {
        buttonGrupBaru.setOnClickListener(view1 -> {
            judul = "Masukkan Nama Grup";
            input = "Nama Grup";
            error = "Nama Grup Salah!";
            Dialog();

            grupKosong.setVisibility(View.GONE);
            ReadGrup();
        });
    }

    private void ClickGrup() {
        grupView.setOnItemClickListener((adapterView, view, i, l) -> {
            Grup grup = grupAdapter.getItem(i);
            Intent intent = new Intent(getActivity(), GrupActivity.class);

            docUID.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists())
                {
                    String s = documentSnapshot.getString("Username");
                    if (!Objects.requireNonNull(s).isEmpty())
                    {
                        intent.putExtra(NAMA_USER, s);
                    }
                }
            });
            intent.putExtra(NAMA_GRUP, grup.GetNama());
            intent.putExtra(OWNER_GRUP, grup.GetOwner());
            intent.putExtra(ID_GRUP, grup.GetID());

            startActivity(intent);
            Toast.makeText(getActivity(), "Grup "+grup.GetNama(), Toast.LENGTH_SHORT).show();
        });
    }

    private String judul, input, error;
    private void Dialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        TextView judulDialog = dialog.findViewById(R.id.JudulDialog);
        EditText inputDialog = dialog.findViewById(R.id.InputDialog);
        TextView errorDialog = dialog.findViewById(R.id.ErrorDialog);
        Button btnBatal = dialog.findViewById(R.id.BtnBatal);
        Button btnKonfirmasi = dialog.findViewById(R.id.BtnKonfirmasi);

        judulDialog.setText(judul);
        inputDialog.setHint(input);
        errorDialog.setText(error);
        errorDialog.setVisibility(View.GONE);

        btnBatal.setOnClickListener(v -> dialog.dismiss());
        btnKonfirmasi.setOnClickListener(v -> {
            String cek = inputDialog.getText().toString();
            if (cek.isEmpty()){
                errorDialog.setVisibility(View.VISIBLE);
            }
            else
            {
                docGID = collGrup.document();
                dbGrup = db.collection("Grup").document(docGID.getId());

                docGID.get().addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        Map<String, Object> HashMap = new HashMap<>();
                        HashMap.put("Nama", cek);
                        HashMap.put("Owner", true);
                        HashMap.put("ID", docGID.getId());
                        docGID.set(HashMap);
                        dbGrup.set(HashMap);

                        grupKosong.setVisibility(View.GONE);
                        ReadGrup();
                    }
                });
                dialog.dismiss();
            }
            Toast.makeText(getActivity(), "Grup "+cek+" Berhasil Dibuat!", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }
}
