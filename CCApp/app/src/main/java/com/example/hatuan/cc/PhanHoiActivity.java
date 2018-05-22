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

public class PhanHoiActivity extends AppCompatActivity {
    private static final long RIPPLE_DURATION = 250;

    FrameLayout phanhoi;
    LinearLayout Avatar;

    Toolbar tbPhanHoi;

    CircleImageView civAvatar;
    ImageView ivMenu, ivMenu90;

    TextView tvThongBao, tvChat, tvDuAn, tvLichLamViec, tvPhanHoi, tvHoTro, tvThongTinSanPham;

    CircleButton cbQRScan;

    WebView wbFeedBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phan_hoi);
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
        if (tbPhanHoi != null) {
            setSupportActionBar(tbPhanHoi);
            getSupportActionBar().setTitle(null);
        }
        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.menu, null);
        phanhoi.addView(guillotineMenu);

        new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.ivMenu), ivMenu90)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(tbPhanHoi)
                .setClosedOnStart(true)/*True là để hiện cái màn hình Activity - False là để hiện cái Menu*/
                .build();
    }

    private void addControls() {
        phanhoi = findViewById(R.id.phanhoi);
        Avatar = findViewById(R.id.Avatar);

        tbPhanHoi = findViewById(R.id.tbPhanHoi);

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

        wbFeedBack = findViewById(R.id.wbFeedBack);

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

        wbFeedBack.setWebViewClient(new WebViewClient());
        wbFeedBack.loadUrl("https://docs.google.com/forms/d/14yh14TEUCoes8zqeEGpPzkOnC5LGjFcSYkrEGfCG8-8/edit?usp=sharing");
        wbFeedBack.getSettings().setJavaScriptEnabled(true);
    }
}
