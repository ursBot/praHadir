package com.apps.prahadir;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class DataAdapter extends BaseAdapter {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String USERID, GRUPNAMA, GRUPOWNER, GRUPID;
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    private final List<Data> dataSet;

    private final LayoutInflater inflater;

    public static final ArrayList<String> dataArray = new ArrayList<String>();

    private final String id;
    private final String nama;

    public DataAdapter(GrupOwnerFragment context, List<Data> dataList, String id, String nama) {
        this.dataSet = dataList;
        inflater = LayoutInflater.from(context.getActivity());
        this.id = id;
        this.nama = nama;
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Data getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final DataAdapter.ViewHolder holder;
        if (view == null) {
            holder = new DataAdapter.ViewHolder();
            view = inflater.inflate(R.layout.list_data, null);

            holder.nama = (TextView) view.findViewById(R.id.NamaData);
            holder.nama.setText(dataSet.get(position).GetNama());

            USERID = mUser.getUid();
            holder.check = (CheckBox) view.findViewById(R.id.CheckboxData);
            holder.check.setOnClickListener(view1 -> Toast.makeText(view1.getContext(), "Check Clicked "+position, Toast.LENGTH_SHORT).show());

            holder.option = (ConstraintLayout) view.findViewById(R.id.ButtonOption);
            holder.option.setOnClickListener(view1 -> Toast.makeText(view1.getContext(), "Button Clicked "+position, Toast.LENGTH_SHORT).show());

            view.setTag(holder);
        }

        return view;
    }

    private static class ViewHolder {
        TextView nama;
        CheckBox check;
        ConstraintLayout option;
    }

}
