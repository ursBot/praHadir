package com.apps.prahadir;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String USERID, USERNAME, USEREMAIL;
    EditText inputGrupBaru;
    //FirestoreRecyclerOptions<Grup>options;
    //FirestoreRecyclerAdapter<Grup,GrupAdapter>adapter;

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
        MakeColecctionUser();
        CekUsername();
        TeksBelumPunyaGrup();
        FetchGrup();
        GrupView();
        buttonGrupBaru.setOnClickListener(view1 -> GrupBaru());

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
        Map<String, Object> userMap = new HashMap<>();

        userRoute.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String currentUsername = documentSnapshot.getString("Username");
                if (currentUsername.isEmpty())
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
        CollectionReference grupOwnerRoute = db.collection("User").document(USERID).collection("Grup");

        grupOwnerRoute.get().addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    grupList.add(new Grup(document.getString("name")));
                }
            }
        });
    }
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.GrupView)
    ListView grupView;
    private void GrupView() {
        grupAdapter = new GrupAdapter(getActivity(), grupList);
        grupView.setAdapter(grupAdapter);
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
    @BindView(R.id.TeksBelumPunyaGrup)
    TextView teksBelumPunyaGrup;
    private void TeksBelumPunyaGrup() {
        CollectionReference grupOwnerRoute = db.collection("User").document(USERID).collection("Grup");
        Map<String, Object> grupOwnerMap = new HashMap<>();

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
                        grupOwnerMap.put("name", namaGrup);
                        grupOwnerMap.put("owner", true);

                        grupOwnerRoute.set(grupOwnerMap);

                        teksBelumPunyaGrup.setVisibility(View.INVISIBLE);
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
