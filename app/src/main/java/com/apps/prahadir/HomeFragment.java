package com.apps.prahadir;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String USERID, USERNAME, USEREMAIL;
    EditText inputGrupBaru;

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

        BindExtra();
        CekUsername();
        FetchGrup();
        GrupView();
        MakeColecctionUser();
        CekUsername();
        buttonGrupBaru.setOnClickListener(view1 -> GrupBaru());
        ClickGrup();

        return view;
    }

    private void MakeColecctionUser(){
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

    private void FetchGrup(){
        Query grupSort = db.collection("User").document(USERID).collection("Grup");

        grupSort.orderBy("nama").get().addOnCompleteListener(task -> {
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
        TeksBelumPunyaGrup();
    }
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.GrupView)
    ListView grupView;
    private void GrupView() {
        grupAdapter = new GrupAdapter(this, grupList);
        grupView.setAdapter(grupAdapter);
    }

    private void ClickGrup() {
        grupView.setOnItemClickListener((adapterView, view, i, l) -> {
            Grup grup = grupAdapter.getItem(i);
            Intent intent = new Intent(getActivity(), GrupActivity.class);

            intent.putExtra(USER_ID, USERID);
            intent.putExtra(USER_USERNAME, USERNAME);
            intent.putExtra(USER_EMAIL, USEREMAIL);
            intent.putExtra(NAMA_GRUP, grup.GetNama());
            intent.putExtra(OWNER_GRUP, grup.GetOwner());
            intent.putExtra(ID_GRUP, grup.GetID());

            startActivity(intent);
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


        if(USER_ID==null){
            USERID = intent.getStringExtra(GrupOwnerFragment.USER_ID);
            USERNAME = intent.getStringExtra(GrupOwnerFragment.USER_USERNAME);
            USEREMAIL = intent.getStringExtra(GrupOwnerFragment.USER_EMAIL);
            teksEmail.setText(USEREMAIL);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.TeksBelumPunyaGrup)
    TextView teksBelumPunyaGrup;
    private void TeksBelumPunyaGrup() {
        CollectionReference grupOwnerRoute = db.collection("User").document(USERID).collection("Grup");

        grupOwnerRoute.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty())
            {
                teksBelumPunyaGrup.setVisibility(View.VISIBLE);
            }
            else
            {
                teksBelumPunyaGrup.setVisibility(View.INVISIBLE);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.GrupBaru)
    Button buttonGrupBaru;
    @SuppressLint("SetTextI18n")
    private void GrupBaru() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fragment_home_grupbaru);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        inputGrupBaru = dialog.findViewById(R.id.MasukkanBuatGrupBaru);

        Button buttonKonfirmasi = dialog.findViewById(R.id.BtnKonfirmasi);
        buttonKonfirmasi.setOnClickListener(view -> {
            if (!inputGrupBaru.getText().toString().isEmpty())
            {
                String namaGrup = inputGrupBaru.getText().toString();
                DocumentReference grupOwnerRoute = db.collection("User").document(USERID).collection("Grup").document();
                Map<String, Object> grupOwnerMap = new HashMap<>();

                grupOwnerRoute.get().addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        grupOwnerMap.put("nama", namaGrup);
                        grupOwnerMap.put("owner", true);
                        grupOwnerMap.put("id", grupOwnerRoute.getId());

                        grupOwnerRoute.set(grupOwnerMap);

                        FetchGrup();

                        teksBelumPunyaGrup.setVisibility(View.INVISIBLE);
                    }
                });

                DocumentReference grupRoute = db.collection("Grup").document(grupOwnerRoute.getId());
                Map<String, Object> grupMap = new HashMap<>();
                grupOwnerRoute.get().addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        grupMap.put("nama", namaGrup);
                        grupMap.put("owner", true);
                        grupMap.put("id", grupRoute.getId());

                        grupRoute.set(grupMap);
                    }
                });

                dialog.dismiss();
            }
            else
            {
                TextView cekGrupBaru = dialog.findViewById(R.id.CekGrupBaru);
                cekGrupBaru.setText("Nama Grup Tidak Boleh Kosong!");
                cekGrupBaru.setVisibility(View.VISIBLE);
            }
        });

        Button buttonTutup = dialog.findViewById(R.id.BtnBatal);
        buttonTutup.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
