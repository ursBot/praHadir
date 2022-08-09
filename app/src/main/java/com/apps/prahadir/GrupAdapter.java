package com.apps.prahadir;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;

public class GrupAdapter extends BaseAdapter {

    private List<Grup> mutableGrupList;
    private final List<Grup> staticGrupList;

    private final LayoutInflater inflater;

    public GrupAdapter(Context context, List<Grup> grupList) {
        staticGrupList = grupList;
        this.mutableGrupList = grupList;
        inflater = LayoutInflater.from(context);
    }

    public void RefreshList() {
        mutableGrupList = staticGrupList;
    }

    @Override
    public int getCount() {
        return mutableGrupList.size();
    }

    @Override
    public Grup getItem(int position) {
        return mutableGrupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_grup, null);
            holder.name = view.findViewById(R.id.NamaGrup);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(mutableGrupList.get(position).GetName());
        return view;
    }

    private static class ViewHolder {
        TextView name;
    }
}
