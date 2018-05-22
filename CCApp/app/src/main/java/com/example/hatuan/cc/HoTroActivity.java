package com.example.hatuan.cc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hatuan.util.CheckConnection;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import at.markushi.ui.CircleButton;
import de.hdodenhof.circleimageview.CircleImageView;

public class HoTroActivity extends AppCompatActivity{
    private static final long RIPPLE_DURATION = 250;

    FrameLayout hotro;
    LinearLayout Avatar;

    Toolbar tbHoTro, tbMenu;

    CircleImageView civAvatar;
    ImageView ivMenu, ivMenu90;

    TextView tvThongBao, tvChat, tvDuAn, tvLichLamViec, tvPhanHoi, tvHoTro, tvThongTinSanPham;

    CircleButton cbQRScan;

    WebView wbSupport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_tro);
        addControls();
        addEvents();
    }
    private void addEvents() {
        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
            //Xét actionBar
            ActionBar();
        }else {
            CheckConnection.ShowToast_Short(getApplicationContext(), "Không có kết nối internet");
        }
    }

    public void addActivity(View view){
        switch (view.getId()){
            case R.id.tvLichLamViec:
                if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                    Intent intent = new Intent(getApplicationContext(), LichLamViecActivity.class);
                    startActivity(intent);
                }else {
                    CheckConnection.ShowToast_Short(getApplicationContext(), "Không có kết nối internet");
                }
                break;
            case R.id.tvHoTro:
                if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                    Intent intent = new Intent(getApplicationContext(), HoTroActivity.class);
                    startActivity(intent);
                }else {
                    CheckConnection.ShowToast_Short(getApplicationContext(), "Không có kết nối internet");
                }
                break;
            case R.id.tvPhanHoi:
                if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                    Intent intent = new Intent(getApplicationContext(), PhanHoiActivity.class);
                    startActivity(intent);
                }else {
                    CheckConnection.ShowToast_Short(getApplicationContext(), "Không có kết nối internet");
                }
                break;
            case R.id.tvThongBao:
                if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                    Intent intent = new Intent(getApplicationContext(), ThongBaoActivity.class);
                    startActivity(intent);
                }else {
                    CheckConnection.ShowToast_Short(getApplicationContext(), "Không có kết nối internet");
                }
                break;
            case R.id.Avatar:
                if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                }else {
                    CheckConnection.ShowToast_Short(getApplicationContext(), "Không có kết nối internet");
                }
                break;
        }
    }

    private void ActionBar() {
        if (tbHoTro != null) {
            setSupportActionBar(tbHoTro);
            getSupportActionBar().setTitle(null);
        }
        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.menu, null);
        hotro.addView(guillotineMenu);

        new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.ivMenu), ivMenu90)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(tbHoTro)
                .setClosedOnStart(true)/*True là để hiện cái màn hình Activity - False là để hiện cái Menu*/
                .build();
    }

    private void addControls() {
        hotro = findViewById(R.id.hotro);
        Avatar = findViewById(R.id.Avatar);

        tbHoTro = findViewById(R.id.tbHoTro);
        tbMenu = findViewById(R.id.tbMenu);

        civAvatar = findViewById(R.id.civAvatar);

        ivMenu = findViewById(R.id.ivMenu);
        ivMenu90 = findViewById(R.id.ivMenu90);

        tvThongBao = findViewById(R.id.tvThongBao);
        tvChat = findViewById(R.id.tvChat);
        tvDuAn = findViewById(R.id.tvDuAn);
        tvLichLamViec = findViewById(R.id.tvLichLamViec);
        tvHoTro = findViewById(R.id.tvHoTro);
        tvPhanHoi = findViewById(R.id.tvPhanHoi);
        tvThongTinSanPham = findViewById(R.id.tvThongTinSanPham);

        wbSupport = findViewById(R.id.wbSupport);

        cbQRScan = findViewById(R.id.cbQRScan);
/*        Xét sự ẩn/hiện của QR Scan bằng cách Kiểm tra IMEI
                Nếu trả về 1 -> đúng IMEI
                Nếu trả về 0 -> Sai IMEI                    */
        int checkNumber = DangNhapActivity.check;
        if (checkNumber == 1) {
            cbQRScan.setVisibility(View.VISIBLE);
        } else {
            cbQRScan.setVisibility(View.GONE);
        }

        wbSupport.setWebViewClient(new WebViewClient());
        wbSupport.loadUrl("https://drive.google.com/open?id=1wxqhssqZJwvDIU3h5j--CpplqCZnR9CStVgvqTasDRk");
        wbSupport.getSettings().setJavaScriptEnabled(true);
    }
}
