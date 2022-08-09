package com.apps.prahadir;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;

public class GrupAdapter extends RecyclerView.ViewHolder {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.NamaGrup)
    TextView namaGrup;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.LabelOwner)
    TextView labelOwner;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.Option)
    Button option;
    public GrupAdapter(@NonNull View itemView) {
        super(itemView);
    }
}
