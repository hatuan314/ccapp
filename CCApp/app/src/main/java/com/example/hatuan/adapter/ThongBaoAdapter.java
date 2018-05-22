package com.example.hatuan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hatuan.cc.R;
import com.example.hatuan.model.ThongBao;

import java.util.List;

/**
 * Created by Ha Tuan on 4/10/2018.
 */

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.MyViewHolder> {
    private Context context;
    private List<ThongBao> danhSachThongBao;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTieuDe, tvThoiGian;
        public ImageView ivImage, delete_icon;
        public RelativeLayout view_HienThi, view_Xoa;

        public MyViewHolder(View view) {
            super(view);
            tvTieuDe = view.findViewById(R.id.tvTieuDe);
            tvThoiGian = view.findViewById(R.id.tvThoiGian);
            ivImage = view.findViewById(R.id.ivImage);
            delete_icon = view.findViewById(R.id.delete_icon);
            view_HienThi = view.findViewById(R.id.view_HienThi);
            view_Xoa = view.findViewById(R.id.view_Xoa);
        }
    }

    public ThongBaoAdapter(Context context, List<ThongBao> danhSachThongBao) {
        this.context = context;
        this.danhSachThongBao = danhSachThongBao;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thong_bao, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ThongBao thongBao = danhSachThongBao.get(position);
        holder.tvTieuDe.setText(thongBao.getTieude());
        holder.tvThoiGian.setText(thongBao.getThoiGian());
    }

    @Override
    public int getItemCount() {
        return danhSachThongBao.size();
    }

    public void removeItem(int position) {
        danhSachThongBao.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(ThongBao thongBao, int position) {
        danhSachThongBao.add(position, thongBao);
        // notify item added by position
        notifyItemInserted(position);
    }
}
