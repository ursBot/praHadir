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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GrupOwnerFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String USERID, GRUPNAMA, GRUPOWNER, GRUPID;
    EditText inputDataBaru;

    public static final String USER_ID = "UserID";
    public static final String NAMA_GRUP = "NamaGrup";
    public static final String ID_GRUP = "IDGrup";
    public static final String OWNER_GRUP = "OwnerGrup";

    private DataAdapter dataAdapter;
    private final ArrayList<Data> dataList = new ArrayList<>();

    public GrupOwnerFragment() {
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
        View view = inflater.inflate(R.layout.fragment_grup_owner, container, false);
        ButterKnife.bind(this, view);

        BindExtra();
        FetchData();
        DataView();
        buttonDataBaru.setOnClickListener(view1 -> DataBaru());

        return view;
    }
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.NamaGrup)
    TextView namaGrup;
    private void BindExtra(){
        Intent intent = requireActivity().getIntent();
        USERID = intent.getStringExtra(HomeFragment.USER_ID);
        GRUPNAMA = intent.getStringExtra(HomeFragment.NAMA_GRUP);
        GRUPOWNER = intent.getStringExtra(HomeFragment.OWNER_GRUP);
        GRUPID = intent.getStringExtra(HomeFragment.ID_GRUP);
        namaGrup.setText(GRUPNAMA);
    }

    private void FetchData(){
        Query dataSort = db.collection("User").document(USERID).collection("Grup").document(GRUPID).collection("Data");

        dataSort.orderBy("nama").get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                dataList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    dataList.add(new Data(document.getString("nama")));
                }
                dataView.setAdapter(dataAdapter);
            }
        });
        TeksBelumPunyaData();
    }
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.DataView)
    ListView dataView;
    private void DataView() {
        dataAdapter = new DataAdapter(this, dataList);
        dataView.setAdapter(dataAdapter);
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.DataKosong)
    TextView teksBelumPunyaData;
    private void TeksBelumPunyaData() {
        CollectionReference dataRoute = db.collection("User").document(USERID).collection("Grup").document(GRUPID).collection("Data");
        Map<String, Object> grupOwnerMap = new HashMap<>();

        dataRoute.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty())
            {
                teksBelumPunyaData.setVisibility(View.VISIBLE);
            }
            else
            {
                teksBelumPunyaData.setVisibility(View.INVISIBLE);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.TambahData)
    Button buttonDataBaru;
    @SuppressLint("SetTextI18n")
    private void DataBaru() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fragment_grup_databaru);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        inputDataBaru = dialog.findViewById(R.id.MasukkanBuatDataBaru);

        Button buttonKonfirmasi = dialog.findViewById(R.id.BtnKonfirmasi);
        buttonKonfirmasi.setOnClickListener(view -> {
            if (!inputDataBaru.getText().toString().isEmpty())
            {
                String namaData = inputDataBaru.getText().toString();
                DocumentReference dataRoute = db.collection("User").document(USERID).collection("Grup").document(GRUPID).collection("Data").document();
                Map<String, Object> dataMap = new HashMap<>();

                dataRoute.get().addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        dataMap.put("nama", namaData);

                        dataRoute.set(dataMap);

                        FetchData();

                        teksBelumPunyaData.setVisibility(View.INVISIBLE);
                    }
                });

                DocumentReference dataIDRoute = db.collection("Grup").document(GRUPID).collection("Data").document(dataRoute.getId());
                Map<String, Object> dataIDMap = new HashMap<>();
                dataIDRoute.get().addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        dataIDMap.put("nama", namaData);

                        dataIDRoute.set(dataIDMap);
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
