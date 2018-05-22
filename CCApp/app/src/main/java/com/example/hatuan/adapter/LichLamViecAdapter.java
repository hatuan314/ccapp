package com.example.hatuan.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hatuan.cc.LichLamViecActivity;
import com.example.hatuan.cc.R;
import com.example.hatuan.model.NgayGio;
import com.example.hatuan.model.NhiemVu;
import com.example.hatuan.model.NhiemVuNgay;
import com.example.hatuan.util.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ha Tuan on 4/4/2018.
 */

public class LichLamViecAdapter extends BaseExpandableListAdapter {
    Context context;
    private ArrayList<NgayGio> danhSachNgayGio;
    private HashMap<String, ArrayList<NhiemVuNgay>> hmNhiemVu;

    public NhiemVuNgay nhiemVuNgay;
    public NgayGio ngayGio;

    public LichLamViecAdapter(Context context, ArrayList<NgayGio> danhSachNgayGio, HashMap<String, ArrayList<NhiemVuNgay>> hmNhiemVu) {
        this.context = context;
        this.danhSachNgayGio = danhSachNgayGio;
        this.hmNhiemVu = hmNhiemVu;
    }

    @Override
    public int getGroupCount() {
        return this.danhSachNgayGio.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.hmNhiemVu.get(this.danhSachNgayGio.get(groupPosition).getDayofyear()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.danhSachNgayGio.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.hmNhiemVu.get(this.danhSachNgayGio.get(groupPosition).getDayofyear())
                .get(childPosititon);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ngayGio = (NgayGio) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_ngay_trong_tuan, null);
        }
        TextView tvThu = convertView.findViewById(R.id.tvThu);
        TextView tvNgayTrongThang = convertView.findViewById(R.id.tvNgayTrongThang);
        tvThu.setText(ngayGio.getDayofweek());
        tvNgayTrongThang.setText(ngayGio.getDayofyear());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        nhiemVuNgay = (NhiemVuNgay) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_nhiem_vu_trong_ngay, null);
        }
        LinearLayout nhie_vu_ngay = convertView.findViewById(R.id.nhiem_vu_trong_ngay);
        TextView tvNhiemVuNgay = convertView.findViewById(R.id.tvNhiemVuNgay);
        final CheckBox cbNhiemVuNgay = convertView.findViewById(R.id.cbNhiemVuNgay);

        tvNhiemVuNgay.setText(nhiemVuNgay.getNhiemVu());

        //Xem người dùng đã nhấn check vào những nhiệm vụ nào
        if(nhiemVuNgay.getCheckNhiemVu().equals("1")){
            cbNhiemVuNgay.setChecked(true);
        }else if (nhiemVuNgay.getCheckNhiemVu().equals("0")){
            cbNhiemVuNgay.setChecked(false);
        }

        //Xét sự kiện nhấn vào CheckBox
        cbNhiemVuNgay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                String nhiemVu = (String) ((NhiemVuNgay) getChild(groupPosition,childPosition)).getId();
                if ((isChecked)){
                    //Cập nhật trạng thái của nhiệm vụ
                    UpdateStatusMission(nhiemVu, "1");
                    ((NhiemVuNgay) getChild(groupPosition,childPosition)).setCheckNhiemVu("1");
                }else if(!isChecked){
                    UpdateStatusMission(nhiemVu, "0");
                    ((NhiemVuNgay) getChild(groupPosition,childPosition)).setCheckNhiemVu("0");
                }
            }
        });
        return convertView;
    }

    //Hàm cập nhật trạng thái nhiệm vụ
    private void UpdateStatusMission(final String id, final String checknhiemvu) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.Duongdanchecknhiemvu, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("manhanvien",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap= new HashMap<String,String>();
                hashMap.put("id", id);
                hashMap.put("checknhiemvu", checknhiemvu);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
