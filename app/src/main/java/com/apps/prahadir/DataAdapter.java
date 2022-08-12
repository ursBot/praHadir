package com.apps.prahadir;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

public class DataAdapter extends BaseAdapter {

    private List<Data> dataSet;

    private final LayoutInflater inflater;

    public DataAdapter(GrupOwnerFragment context, List<Data> dataList) {
        this.dataSet = dataList;
        inflater = LayoutInflater.from(context.getActivity());
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
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_grup_data, null);

            holder.nama = (TextView) view.findViewById(R.id.NamaData);
            holder.check = (CheckBox) view.findViewById(R.id.CheckboxData);
            holder.option = (ConstraintLayout) view.findViewById(R.id.ButtonOption);

            view.setTag(holder);
        }
        else
        {
            holder = (DataAdapter.ViewHolder) view.getTag();
        }
        Data data = dataSet.get(position);

        holder.nama.setText(dataSet.get(position).GetNama());
        holder.check.setChecked(data.GetSelected());
        holder.option.setOnClickListener(view1 -> Toast.makeText(view1.getContext(), dataSet.get(position).GetNama()+position, Toast.LENGTH_SHORT).show());

        return view;
    }
    public void updateRecords(List<Data> dataList){
        this.dataSet = dataList;

        notifyDataSetChanged();
    }
    private static class ViewHolder {
        TextView nama;
        CheckBox check;
        ConstraintLayout option;
    }

}
