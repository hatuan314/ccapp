package com.example.hatuan.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import com.example.hatuan.cc.R;
import com.example.hatuan.model.NhiemVu;

/**
 * Created by Ha Tuan on 4/6/2018.
 */

public class TaoLichLamViecAdapter extends ArrayAdapter<NhiemVu> {
    Activity context;
    int resource;
    List<NhiemVu> objects;
    public TaoLichLamViecAdapter(@NonNull Activity context, int resource, @NonNull List<NhiemVu> objects) {
        super(context, resource, objects);
        this.context= context;
        this.resource= resource;
        this.objects=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater= this.context.getLayoutInflater();
        View row= inflater.inflate(this.resource,null);

        TextView txtMission= row.findViewById(R.id.tvNhiemVuNgay);
        CheckBox chkMission= row.findViewById(R.id.cbNhiemVuNgay);

        NhiemVu nhiemVu= this.objects.get(position);
        txtMission.setText(nhiemVu.getNhiemvu());
        if (nhiemVu.getChecknhiemvu().equals("0"))
            chkMission.setChecked(false);
        else
            chkMission.setChecked(true);
        return row;
    }
}
