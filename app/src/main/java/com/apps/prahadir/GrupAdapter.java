package com.apps.prahadir;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.List;

public class GrupAdapter extends BaseAdapter {

    private final List<Grup> grupSet;

    private final LayoutInflater inflater;

    public GrupAdapter(HomeFragment context, List<Grup> grupList) {
        this.grupSet = grupList;
        inflater = LayoutInflater.from(context.getActivity());
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


    @SuppressLint("InflateParams")
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
        }

        return view;
    }

    private static class ViewHolder {
        TextView nama;
        TextView owner;
        ConstraintLayout option;
    }
}
