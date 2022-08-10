package com.apps.prahadir;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import butterknife.BindView;

public class GrupAdapter extends BaseAdapter {

    private List<Grup> grupSet;

    private final LayoutInflater inflater;

    public GrupAdapter(HomeFragment context, List<Grup> grupList) {
        this.grupSet = grupList;
        inflater = LayoutInflater.from(context.getActivity());
    }

    public void RefreshList() {

    }

    @Override
    public int getCount() {
        return grupSet.size();
    }

    @Override
    public Grup getItem(int position) {
        return grupSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_grup, null);

            holder.nama = (TextView) view.findViewById(R.id.NamaGrup);
            holder.nama.setText(grupSet.get(position).GetNama());

            holder.owner = (TextView) view.findViewById(R.id.LabelOwner);

            holder.option = (ConstraintLayout) view.findViewById(R.id.ButtonOption);
            holder.option.setOnClickListener(view1 -> Toast.makeText(view1.getContext(), "Button Clicked "+position, Toast.LENGTH_SHORT).show());

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //holder.owner.setVisibility(View.INVISIBLE);
        return view;
    }

    private static class ViewHolder {
        TextView nama;
        TextView owner;
        ConstraintLayout option;
    }
}
