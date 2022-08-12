package com.apps.prahadir;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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

            holder.nama = view.findViewById(R.id.NamaGrup);

            holder.owner = view.findViewById(R.id.LabelOwner);

            holder.option = view.findViewById(R.id.ButtonOption);

            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }
        holder.nama.setText(grupSet.get(position).GetNama());

        return view;
    }

    private static class ViewHolder {
        TextView nama;
        TextView owner;
        ConstraintLayout option;
    }
}
