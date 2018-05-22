package com.example.hatuan.cc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hatuan.adapter.LichLamViecAdapter;
import com.example.hatuan.download.DownloadMission;
import com.example.hatuan.model.NgayGio;
import com.example.hatuan.model.NhiemVu;
import com.example.hatuan.model.NhiemVuNgay;
import com.example.hatuan.util.CheckConnection;
import com.example.hatuan.util.Server;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import at.markushi.ui.CircleButton;
import de.hdodenhof.circleimageview.CircleImageView;

public class LichLamViecActivity extends AppCompatActivity{
    private static final long RIPPLE_DURATION = 250;

    FrameLayout lichlamviec;
    LinearLayout Avatar;

    Toolbar tbLichLamViec, tbMenu;
    Spinner spTuan;

    CircleImageView civAvatar;
    ImageView ivMenu, ivMenu90, ivTaoLichLamViec;

    TextView tvThongBao, tvChat, tvDuAn, tvLichLamViec, tvPhanHoi, tvHoTro, tvThongTinSanPham, tvDoiMatKhau;

    CircleButton cbQRScan;

    NgayGio ngayGio;

    public static ArrayList<NhiemVu> danhSachNhiemVu;
    public ArrayList<NhiemVuNgay> danhSachNhiemVuNgay;
    public ArrayList<NgayGio> danhSachNgayGio;
    public ArrayList<Integer> danhSachTuan;

    public ArrayList<NhiemVuNgay> Monday;
    public ArrayList<NhiemVuNgay> Tuesday;
    public ArrayList<NhiemVuNgay> Wednesday;
    public ArrayList<NhiemVuNgay> Thursday;
    public ArrayList<NhiemVuNgay> Friday;

    public HashMap<String, ArrayList<NhiemVuNgay>> hmNhiemVu;

    public static ExpandableListView lvExpNgayTrongTuan;
    LichLamViecAdapter lichLamViecAdapter;

    public String manhanvien = DangNhapActivity.manhanvien;
    NhiemVu nhiemVu;

    public int tuanLamViec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_lam_viec);
        addControls();
        addEvents();
    }

    private void addEvents() {
        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
            //Xét actionBar
            ActionBar();
            //Đổ dữ liệu Nhiệm Vụ từ Server về app
            MissionData();
            //Xét sự kiện của Spinner
            CatchEventSpinner();
            //Bắt sự kiện nhấn vào Item ngày
            CatchEventClickItem();
            //Bắt sự kiện nhấn vào nút quét QR
            CatchEventClickcbQRScan();
            //Bắt sự kiện nhấn vào nút tạo mới lịch làm việc
            CatchEventClickivTaoLichLamViec();
        }else {
            CheckConnection.ShowToast_Short(getApplicationContext(), "Không có kết nối internet");
        }
    }

    private void CatchEventClickivTaoLichLamViec() {
        ivTaoLichLamViec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TaoLichLamViecActivity.class);
                startActivity(intent);
            }
        });
    }

    private void CatchEventClickcbQRScan() {
        cbQRScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QRActivity.class);
                startActivity(intent);
            }
        });
    }

    private void CatchEventClickItem() {
        lvExpNgayTrongTuan.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return false;
            }
        });

        lvExpNgayTrongTuan.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
            }
        });

        lvExpNgayTrongTuan.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        // Listview on child click listener
        lvExpNgayTrongTuan.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                return false;
            }
        });
    }



    public void MissionData() {
        String url = Server.Duongdannhiemvu;
        DownloadMission downloadMission = new DownloadMission();
        //Lấy chuỗi JSON từ server về app
        downloadMission.execute(url,manhanvien);

        try {
            //Nhận về chuỗi JSON
            String data = downloadMission.get();

            String id = "";
            String nhiemvu = "";
            String checknhiemvu = "";
            String tuantrongnam = "";
            String ngaytrongtuan = "";
            String ngaytrongthang = "";
            //Đọc chuỗi JSON
            danhSachNhiemVu = new ArrayList<>();
            JSONObject mainObject = new JSONObject(data);
            JSONArray listNhanVien = mainObject.getJSONArray("nhiemvu");
            for (int i=0;i<listNhanVien.length(); i++){
                JSONObject nhanVienObject = listNhanVien.getJSONObject(i);
                id = nhanVienObject.getString("id");
                nhiemvu = nhanVienObject.getString("nhiemvu");
                checknhiemvu = nhanVienObject.getString("checknhiemvu");
                tuantrongnam = nhanVienObject.getString("tuantrongnam");
                ngaytrongtuan = nhanVienObject.getString("ngaytrongtuan");
                ngaytrongthang = nhanVienObject.getString("ngaytrongthang");
                danhSachNhiemVu.add(new NhiemVu(id, nhiemvu, checknhiemvu, tuantrongnam, ngaytrongtuan,ngaytrongthang, manhanvien));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Hàm bắt sự kiện của Spinner
    private void CatchEventSpinner() {
        danhSachTuan = new ArrayList<>();
        for (int i = danhSachNhiemVu.size()-1; i>=0; i--){
            nhiemVu = danhSachNhiemVu.get(i);
            //Tuần chỉ xuất hiện 1 lần trong spinner
            if (i==0){
                danhSachTuan.add(Integer.valueOf(nhiemVu.getTuantrongnam()));
            }
            else if(i>0 && !nhiemVu.getTuantrongnam().equals(danhSachNhiemVu.get(i-1).getTuantrongnam())){
                danhSachTuan.add(Integer.valueOf(nhiemVu.getTuantrongnam()));
            }

        }
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, danhSachTuan);
        spTuan.setAdapter(arrayAdapter);
        //Xét sự kiện chọn tuần
        spTuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tuanLamViec = danhSachTuan.get(position);
                LichLamViec(danhSachTuan.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(),"Chưa chọn tuần !",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void LichLamViec(int tuanLamViec) {
        //add Lịch làm việc vào ArrayList
        String id = "";
        String noiDungNhiemVu = "";
        String checkNhiemVu = "";
        String tuanTrongNam = "";
        String ngayTrongTuan = "";
        String ngayTrongThang = "";

        danhSachNgayGio = new ArrayList<NgayGio>();
        danhSachNhiemVuNgay = new ArrayList<NhiemVuNgay>();
        //Add danh sách ngày đã đăng kí
        for (int i=0; i<danhSachNhiemVu.size(); i++){
            nhiemVu = danhSachNhiemVu.get(i);
            tuanTrongNam = nhiemVu.getTuantrongnam();
            ngayTrongTuan = nhiemVu.getNgaytrongtuan();
            ngayTrongThang = nhiemVu.getNgaytrongthang();
            if(tuanTrongNam.equals(String.valueOf(tuanLamViec))){
                //1 ngày chỉ xuất hiện 1 lần trong ListView
                if(i==0){
                    danhSachNgayGio.add(new NgayGio(tuanTrongNam,ngayTrongTuan,ngayTrongThang));
                } else if (!nhiemVu.getNgaytrongthang().equals(danhSachNhiemVu.get(i-1).getNgaytrongthang())){
                    danhSachNgayGio.add(new NgayGio(tuanTrongNam,ngayTrongTuan,ngayTrongThang));
                }
            }
        }
        //Add danh sách nhiệm vụ của từng ngày
        hmNhiemVu = new HashMap<String, ArrayList<NhiemVuNgay>>();
        Monday = new ArrayList<NhiemVuNgay>();
        Tuesday = new ArrayList<NhiemVuNgay>();
        Wednesday = new ArrayList<NhiemVuNgay>();
        Thursday = new ArrayList<NhiemVuNgay>();
        Friday = new ArrayList<NhiemVuNgay>();
        for (int i=0; i<danhSachNgayGio.size(); i++){
            ngayGio = danhSachNgayGio.get(i);

            String thu = "";
            thu = ngayGio.getDayofweek();
            String ngay = ngayGio.getDayofyear();

            switch (thu) {
                case "Thứ 2": {
                    for (int j = 0; j < danhSachNhiemVu.size(); j++) {
                        if (danhSachNhiemVu.get(j).getNgaytrongthang().equals(ngay)) {
                            String maNhiemVu = danhSachNhiemVu.get(j).getId();
                            String nhiemVuNgay = danhSachNhiemVu.get(j).getNhiemvu();
                            String check = danhSachNhiemVu.get(j).getChecknhiemvu();
                            Monday.add(new NhiemVuNgay(maNhiemVu, nhiemVuNgay, check));
                        }
                    }
                    hmNhiemVu.put(ngayGio.getDayofyear(), Monday);
                }
                break;
                case "Thứ 3": {
                    for (int j = 0; j < danhSachNhiemVu.size(); j++) {
                        if (danhSachNhiemVu.get(j).getNgaytrongthang().equals(ngay)) {
                            String maNhiemVu = danhSachNhiemVu.get(j).getId();
                            String nhiemVuNgay = danhSachNhiemVu.get(j).getNhiemvu();
                            String check = danhSachNhiemVu.get(j).getChecknhiemvu();
                            Tuesday.add(new NhiemVuNgay(maNhiemVu, nhiemVuNgay, check));
                        }
                    }
                    hmNhiemVu.put(ngayGio.getDayofyear(), Tuesday);
                }
                break;
                case "Thứ 4": {
                    for (int j = 0; j < danhSachNhiemVu.size(); j++) {
                        if (danhSachNhiemVu.get(j).getNgaytrongthang().equals(ngay)) {
                            String maNhiemVu = danhSachNhiemVu.get(j).getId();
                            String nhiemVuNgay = danhSachNhiemVu.get(j).getNhiemvu();
                            String check = danhSachNhiemVu.get(j).getChecknhiemvu();
                            Thursday.add(new NhiemVuNgay(maNhiemVu, nhiemVuNgay, check));
                        }
                    }
                    hmNhiemVu.put(ngayGio.getDayofyear(), Wednesday);
                }
                break;
                case "Thứ 5": {
                    for (int j = 0; j < danhSachNhiemVu.size(); j++) {
                        if (danhSachNhiemVu.get(j).getNgaytrongthang().equals(ngay)) {
                            String maNhiemVu = danhSachNhiemVu.get(j).getId();
                            String nhiemVuNgay = danhSachNhiemVu.get(j).getNhiemvu();
                            String check = danhSachNhiemVu.get(j).getChecknhiemvu();
                            Wednesday.add(new NhiemVuNgay(maNhiemVu, nhiemVuNgay, check));
                        }
                    }
                    hmNhiemVu.put(ngayGio.getDayofyear(), Thursday);
                }
                break;
                case "Thứ 6": {
                    for (int j = 0; j < danhSachNhiemVu.size(); j++) {
                        if (danhSachNhiemVu.get(j).getNgaytrongthang().equals(ngay)) {
                            String maNhiemVu = danhSachNhiemVu.get(j).getId();
                            String nhiemVuNgay = danhSachNhiemVu.get(j).getNhiemvu();
                            String check = danhSachNhiemVu.get(j).getChecknhiemvu();
                            Friday.add(new NhiemVuNgay(maNhiemVu, nhiemVuNgay, check));
                        }
                    }
                    hmNhiemVu.put(ngayGio.getDayofyear(), Friday);
                }
                break;
            }
        }

        //Hiển thị lên màn hình
        lichLamViecAdapter = new LichLamViecAdapter(this, danhSachNgayGio, hmNhiemVu);
        lvExpNgayTrongTuan.setAdapter(lichLamViecAdapter);
    }

    public void addActivity(View view){
        switch (view.getId()){
            case R.id.tvChat:
                break;

            case R.id.tvDuAn:
                break;

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

            case R.id.Avatar:
                if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
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
            case R.id.tvMatKhau:
                if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                    Intent intent = new Intent(getApplicationContext(), DoiMatKhauActivity.class);
                    startActivityForResult(intent,1);
                }else {
                    CheckConnection.ShowToast_Short(getApplicationContext(), "Không có kết nối internet");
                }
                break;
        }
    }

    private void ActionBar() {
        if (tbLichLamViec != null) {
            setSupportActionBar(tbLichLamViec);
            getSupportActionBar().setTitle(null);
        }
        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.menu, null);
        lichlamviec.addView(guillotineMenu);

        new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.ivMenu), ivMenu90)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(tbLichLamViec)
                .setClosedOnStart(true)/*True là để hiện cái màn hình Activity - False là để hiện cái Menu*/
                .build();
    }

    private void addControls() {
        lichlamviec = findViewById(R.id.lichlamviec);
        Avatar = findViewById(R.id.Avatar);

        tbLichLamViec = findViewById(R.id.tbLichLamViec);
        tbMenu = findViewById(R.id.tbMenu);

        spTuan = findViewById(R.id.spTuan);

        civAvatar = findViewById(R.id.civAvatar);

        ivMenu = findViewById(R.id.ivMenu);
        ivMenu90 = findViewById(R.id.ivMenu90);
        ivTaoLichLamViec = findViewById(R.id.ivTaoLichLamViec);

        tvThongBao = findViewById(R.id.tvThongBao);
        tvChat = findViewById(R.id.tvChat);
        tvDuAn = findViewById(R.id.tvDuAn);
        tvLichLamViec = findViewById(R.id.tvLichLamViec);
        tvHoTro = findViewById(R.id.tvHoTro);
        tvPhanHoi = findViewById(R.id.tvPhanHoi);
        tvThongTinSanPham = findViewById(R.id.tvThongTinSanPham);
        tvDoiMatKhau = findViewById(R.id.tvMatKhau);

        cbQRScan = findViewById(R.id.cbQRScan);

        lvExpNgayTrongTuan = findViewById(R.id.lvExpNgayTrongTuan);
        //Xét sự ẩn hiện của nút Scan
        CatchEventcbQRScan();

    }



    private void CatchEventcbQRScan() {
        /*      Xét sự ẩn/hiện của QR Scan bằng cách Kiểm tra IMEI
                Nếu trả về 1 -> đúng IMEI
                Nếu trả về 0 -> Sai IMEI                    */
        int checkNumber = DangNhapActivity.check;
        if(checkNumber==1){
            cbQRScan.setVisibility(View.VISIBLE);
        }else {
            cbQRScan.setVisibility(View.GONE);
        }
    }


}
