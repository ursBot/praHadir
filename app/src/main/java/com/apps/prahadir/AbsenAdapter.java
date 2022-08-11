package com.apps.prahadir;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AbsenAdapter extends BaseAdapter {

    private final List<Absen> absenSet;

    private final LayoutInflater inflater;

    public AbsenAdapter(GrupOwnerFragment context, List<Absen> absenList) {
        this.absenSet = absenList;
        inflater = LayoutInflater.from(context.getActivity());
    }

    @Override
    public int getCount() {
        return absenSet.size();
    }

    @Override
    public Absen getItem(int position) {
        return absenSet.get(position);
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
            view = inflater.inflate(R.layout.list_tanggalabsen, null);

            holder.tanggal = (TextView) view.findViewById(R.id.TanggalAbsen);
            holder.tanggal.setText(absenSet.get(position).GetTanggal());

            holder.hapus = (View) view.findViewById(R.id.Hapus);
            holder.hapus.setOnClickListener(view1 -> Toast.makeText(view1.getContext(), "Button Clicked "+position, Toast.LENGTH_SHORT).show());

            view.setTag(holder);
        }

        return view;
    }

    private static class ViewHolder {
        TextView tanggal;
        View hapus;
    }
}
