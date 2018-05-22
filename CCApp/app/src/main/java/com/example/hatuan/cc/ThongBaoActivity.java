package com.example.hatuan.cc;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.FrameLayout;

import com.example.hatuan.adapter.ThongBaoAdapter;
import com.example.hatuan.download.DownloadThongBao;
import com.example.hatuan.model.ThongBao;
import com.example.hatuan.util.RecyclerItemTouchHelper;
import com.example.hatuan.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ThongBaoActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView rycThongBao;
    private List<ThongBao> danhSachThongBao;
    private ThongBaoAdapter thongBaoAdapter;
    private FrameLayout thongBaoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_bao);
        rycThongBao = findViewById(R.id.rycThongBao);
        thongBaoLayout = findViewById(R.id.thongBao);

        danhSachThongBao = new ArrayList<>();
        thongBaoAdapter = new ThongBaoAdapter(this, danhSachThongBao);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rycThongBao.setLayoutManager(mLayoutManager);
        rycThongBao.setItemAnimator(new DefaultItemAnimator());
        rycThongBao.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rycThongBao.setAdapter(thongBaoAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rycThongBao);


        // making http call and fetching menu json
        prepareCart();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(rycThongBao);
    }

    private void addCOntrols() {

    }

    private void prepareCart() {
        String url = Server.Duongdanthongbao;
        DownloadThongBao downloadThongBao = new DownloadThongBao();
        //Lấy chuỗi JSON từ server về app
        downloadThongBao.execute(url);

        try {
            //Nhận về chuỗi JSON
            String data = downloadThongBao.get();

            String id = "";
            String ngaygio = "";
            String tieude = "";
            String noidung = "";
            //Đọc chuỗi JSON
            danhSachThongBao = new ArrayList<>();
            JSONObject mainObject = new JSONObject(data);
            JSONArray listThongBao = mainObject.getJSONArray("thongbao");
            for (int i=0;i<listThongBao.length(); i++){
                JSONObject thongBaoObject = listThongBao.getJSONObject(i);
                id = thongBaoObject.getString("id");
                ngaygio = thongBaoObject.getString("thoigian");
                tieude = thongBaoObject.getString("tieude");
                noidung = thongBaoObject.getString("noidung");
                danhSachThongBao.add(new ThongBao(id, ngaygio, tieude, noidung));
            }
            thongBaoAdapter.notifyDataSetChanged();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ThongBaoAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String tieude = danhSachThongBao.get(viewHolder.getAdapterPosition()).getTieude();

            // backup of removed item for undo purpose
            final ThongBao deletedThongBao = danhSachThongBao.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            thongBaoAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(thongBaoLayout, tieude + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    thongBaoAdapter.restoreItem(deletedThongBao, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
