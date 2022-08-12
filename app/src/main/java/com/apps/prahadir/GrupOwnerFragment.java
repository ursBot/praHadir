package com.apps.prahadir;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
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

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GrupOwnerFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    private final String userID = Objects.requireNonNull(mUser).getUid();
    private final String userNAME = mUser.getEmail();
    private final String userEMAIL = mUser.getEmail();
    private String grupNAMA, grupOWNER, grupID, userNAMA;
    private String judul, input, error;

    DocumentReference docUID = db.collection("User").document(userID);
    CollectionReference collGrup = docUID.collection("Grup");
    DocumentReference docGID = collGrup.document();

    CollectionReference collData;
    CollectionReference collAbsen;
    DocumentReference docDID;
    DocumentReference docAID;
    DocumentReference dbData;
    DocumentReference dbAbsen;

    public static final String USER_ID = "UserID";
    public static final String USER_USERNAME = "UserID";
    public static final String USER_EMAIL = "UserID";
    public static final String NAMA_GRUP = "NamaGrup";
    public static final String ID_GRUP = "IDGrup";
    public static final String OWNER_GRUP = "OwnerGrup";

    private DataAdapter dataAdapter;
    private final ArrayList<Data> dataList = new ArrayList<>();

    private AbsenAdapter absenAdapter;
    private final ArrayList<Absen> absenList = new ArrayList<>();

    Map<String, Object> HashMap = new HashMap<>();

    public GrupOwnerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Rename and change types of parameters
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grup_owner, container, false);
        ButterKnife.bind(this, view);

        BindExtra();
        RUTE();

        Tanggal();
        Back();
        ReadData();
        DataViewOwner();
        DataBaru();
        ClickData();

        //FetchAbsen();
        //AbsenView();
        //buttonAbsenBaru.setOnClickListener(view1 -> AbsenBaru());

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.NamaGrup)
    TextView namaGrup;
    private void BindExtra(){
        Intent intent = requireActivity().getIntent();
        userNAMA = intent.getStringExtra(HomeFragment.NAMA_USER);
        grupNAMA = intent.getStringExtra(HomeFragment.NAMA_GRUP);
        grupOWNER = intent.getStringExtra(HomeFragment.OWNER_GRUP);
        grupID = intent.getStringExtra(HomeFragment.ID_GRUP);
        namaGrup.setText(grupNAMA);
    }

    private void RUTE(){
        collData = collGrup.document(grupID).collection("Data");
        collAbsen = collGrup.document(grupID).collection("Absen");
        docDID = collData.document();
        docAID = collAbsen.document();
        dbData = db.collection("Grup").document(grupID).collection("Data").document(docDID.getId());
        dbAbsen = db.collection("Grup").document(grupID).collection("Absen").document(docAID.getId());
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.BackFromGrup)
    View back;
    private void Back() {
        back.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
    }

    private void ReadData(){
        collData.orderBy("Nama").get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                dataList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    dataList.add(new Data(document.getString("Nama"), false));
                }
                dataViewOwner.setAdapter(dataAdapter);
            }
        });
        DataKosong();
    }
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.DataKosong)
    TextView teksDataKosong;
    private void DataKosong() {
        collData.get().addOnSuccessListener(queryDocumentSnapshots ->
        {
            if (queryDocumentSnapshots.isEmpty())
            {
                teksDataKosong.setVisibility(View.VISIBLE);
            }
            else
            {
                teksDataKosong.setVisibility(View.INVISIBLE);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.DataViewOwner)
    ListView dataViewOwner;
    private void DataViewOwner() {
        dataAdapter = new DataAdapter(this, dataList);
        dataViewOwner.setAdapter(dataAdapter);
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.TambahData)
    Button buttonDataBaru;
    @SuppressLint("SetTextI18n")
    private void DataBaru() {
        buttonDataBaru.setOnClickListener(view1 ->
        {
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            judul = "Masukkan Nama Data Baru";
            input = "Nama Data Baru";
            error = "Nama Data Tidak Boleh Kosong!";

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
            btnKonfirmasi.setOnClickListener(v ->
            {
                String cek = inputDialog.getText().toString();
                if (cek.isEmpty())
                {
                    errorDialog.setVisibility(View.VISIBLE);
                }
                else
                {
                    docDID = collData.document();
                    docDID.get().addOnSuccessListener(documentSnapshot -> {
                        if (!documentSnapshot.exists()) {
                            HashMap.put("Nama", cek);
                            docDID.set(HashMap);
                            dbData.set(HashMap);

                            teksDataKosong.setVisibility(View.GONE);
                            ReadData();
                            Log.d("debugggg", "asu");
                        }
                    });
                    Toast.makeText(getActivity(), "Berhasil Menambah Data", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            dialog.show();
        });
    }

    private void ClickData() {
        dataViewOwner.setOnItemClickListener((adapterView, view, i, l) -> {
            Data data = dataAdapter.getItem(i);

            if (data.GetSelected()){
                data.SetSelected(false);
            }
            else
            {
                data.SetSelected(true);
            }

            dataList.set(i, data);
            dataAdapter.updateRecords(dataList);

            Toast.makeText(getActivity(), data.GetNama()+" check", Toast.LENGTH_SHORT).show();
        });
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.Kurang)
    View kurang;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.Tambah)
    View tambah;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.Tanggal)
    TextView tanggal;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void Tanggal() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

        Calendar c = Calendar.getInstance();
        String dt = sdf.format(new Date());

        tanggal.setText(dt);
        tambah.setOnClickListener(view -> {
            c.add(java.util.Calendar.DATE, 1);
            tanggal.setText(sdf.format(c));
        });
        kurang.setOnClickListener(view -> {
            c.add(java.util.Calendar.DATE, -1);
            tanggal.setText(sdf.format(c));
        });
    }

    private void FetchAbsen(){
        collAbsen.orderBy("tanggal").get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                absenList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    absenList.add(new Absen(document.getString("tanggal")));
                }
                absenView.setAdapter(absenAdapter);
            }
        });
        TeksBelumPunyaAbsen();
    }
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.AbsensiView)
    ListView absenView;
    private void AbsenView() {
        absenAdapter = new AbsenAdapter(this, absenList);
        absenView.setAdapter(absenAdapter);
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.AbsensiKosong)
    TextView teksBelumPunyaAbsen;
    private void TeksBelumPunyaAbsen() {
        collAbsen.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty())
            {
                teksBelumPunyaAbsen.setVisibility(View.VISIBLE);
            }
            else
            {
                teksBelumPunyaAbsen.setVisibility(View.INVISIBLE);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.AbsensiBaru)
    Button buttonAbsenBaru;
    @SuppressLint("SetTextI18n")
    private void AbsenBaru() {

        buttonAbsenBaru.setOnClickListener(view -> {
            String tgl = tanggal.getText().toString();

            Map<String, Object> tglMap = new HashMap<>();

            docAID.get().addOnSuccessListener(documentSnapshot -> {
                if (!documentSnapshot.exists()) {
                    tglMap.put("Tanggal", tgl);

                    docAID.set(tglMap);

                    FetchAbsen();

                    teksBelumPunyaAbsen.setVisibility(View.INVISIBLE);
                }
            });

            Map<String, Object> tglIDMap = new HashMap<>();
            dbAbsen.get().addOnSuccessListener(documentSnapshot -> {
                if (!documentSnapshot.exists()) {
                    tglIDMap.put("Taggal", tgl);

                    dbAbsen.set(tglIDMap);
                }
            });
        });
    }
}
