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
    DocumentReference docGID = collGrup.document();

    public static final String USER_ID = "UserID";
    public static final String USER_USERNAME = "UserID";
    public static final String USER_EMAIL = "UserID";
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
        Map<String, Object> HashMap = new HashMap<>();

        docUID.get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists())
            {
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
        collGrup.orderBy("nama").get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                grupList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    grupList.add(new Grup(document.getString("nama"),
                            Objects.requireNonNull(document.getBoolean("owner")).toString(),
                            document.getString("id")));
                }
                grupView.setAdapter(grupAdapter);
            }
        });
        DataGrup();
    }
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.TeksBelumPunyaGrup)
    TextView teksBelumPunyaGrup;
    private void DataGrup() {
        collGrup.get().addOnSuccessListener(queryDocumentSnapshots ->
        {
            if (queryDocumentSnapshots.isEmpty())
            {
                teksBelumPunyaGrup.setVisibility(View.VISIBLE);
            }
            else
            {
                teksBelumPunyaGrup.setVisibility(View.GONE);
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
        });
    }

    private void ClickGrup() {
        grupView.setOnItemClickListener((adapterView, view, i, l) -> {
            Grup grup = grupAdapter.getItem(i);
            Intent intent = new Intent(getActivity(), GrupActivity.class);

            intent.putExtra(NAMA_GRUP, grup.GetNama());
            intent.putExtra(OWNER_GRUP, grup.GetOwner());
            intent.putExtra(ID_GRUP, grup.GetID());

            startActivity(intent);
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

        btnKonfirmasi.setOnClickListener(v -> {
            String cek = inputDialog.getText().toString();
            if (cek.isEmpty()){
                errorDialog.setVisibility(View.VISIBLE);
            }
            else
            {
                Map<String, Object> HashMap = new HashMap<>();

                docGID.get().addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        HashMap.put("nama", cek);
                        HashMap.put("owner", true);
                        HashMap.put("id", docGID.getId());

                        docGID.set(HashMap);
                        teksBelumPunyaGrup.setVisibility(View.GONE);
                    }
                });

                DocumentReference grupGID = db.collection("Grup").document(docGID.getId());
                Map<String, Object> HashMap1 = new HashMap<>();
                docGID.get().addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        HashMap1.put("nama", cek);
                        HashMap1.put("owner", true);
                        HashMap1.put("id", grupGID.getId());

                        grupGID.set(HashMap1);
                    }
                });

                ReadGrup();
                dialog.dismiss();
            }
        });

        btnBatal.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
