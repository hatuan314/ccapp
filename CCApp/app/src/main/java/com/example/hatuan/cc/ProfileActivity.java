package com.example.hatuan.cc;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hatuan.model.NhanVien;
import com.example.hatuan.util.CheckConnection;
import com.example.hatuan.util.Server;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity {
    private static final long RIPPLE_DURATION = 250;

    FrameLayout thongTinNhanVien;
    LinearLayout anhDaiDien;

    Toolbar tbThongTinNhanVien;
    CircleImageView civAvatar;
    ImageView ivMenu, ivMenu90;

    TextView tvThongBao, tvChat, tvDuAn, tvLichLamViec, tvPhanHoi, tvHoTro, tvThongTinSanPham;

    public String id_user=DangNhapActivity.manhanvien;
    NhanVien nhanVien;

    private int REQUEST_CODE_CAMERA=123;
    private int REQUEST_CODE_FOLDER=456;

    public String user_name = "";
    public String Name="";
    public String Birthday="";
    public String Home_Town="";
    public String Sex="";
    public String Phone="";
    public String Email="";
    public String Position="";
    public String Avatar="";
    public String Cover="";
    public String PhongBan="";


    private ImageButton btnAvt, btnCover;
    private TextView txtProHeaderName;
    private TextView txtProHeaderPosition;
    private ImageView imgCoverPro;
    private CircleImageView imgAvt;

    private TextView txtName,  txtBirthday, txtHomeTown, txtPhone, txtEmail, txtPosition, txtPhongBan;

    public ImageButton btnSelected=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        addControls();

        loadProfileData(id_user);   // Load dữ liệu người dùng

        addEvents();
    }

    private void addEvents() {
        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
            //Xét actionBar
            ActionBar();
            /**
             *  Xử lý khi người dùng thay đổi ảnh đại diện hoặc ảnh bìa
             */
            btnCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
//                btnSelected = btnCover;
//                //Lựa chọn ảnh từ Thư viện hay lấy từ máy ảnh
//                DialogLuaChonHinhAnh();
                }
            });

            btnAvt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
//                btnSelected = btnAvt;
//                DialogLuaChonHinhAnh();
                }
            });
        }else {
            CheckConnection.ShowToast_Short(getApplicationContext(), "Không có kết nối internet");
        }

    }
    public void addActivity(View view){
        switch (view.getId()){
            case R.id.tvThongBao:
                break;

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
        }
    }

    private void ActionBar() {
        if (tbThongTinNhanVien != null) {
            setSupportActionBar(tbThongTinNhanVien);
            getSupportActionBar().setTitle(null);
        }
        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.menu, null);
        thongTinNhanVien.addView(guillotineMenu);

        new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.ivMenu), ivMenu90)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(tbThongTinNhanVien)
                .setClosedOnStart(true)/*True là để hiện cái màn hình Activity - False là để hiện cái Menu*/
                .build();
    }

    private void DialogLuaChonHinhAnh() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.lua_chon_hinh_anh_dialog);

        TextView tvThuVienAnh = dialog.findViewById(R.id.tvThuVienAnh);
        TextView tvMayAnh = dialog.findViewById(R.id.tvMayAnh);

        tvThuVienAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER);
            }
        });

        tvMayAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });

        dialog.show();
    }

    private void loadProfileData(String id_user) {
        /**
         * Hàm tải dữ liệu hồ sơ người dùng
         */
        nhanVien = DangNhapActivity.danhSachNhanVien.get(0);
        user_name = nhanVien.getUsername();
        Name = nhanVien.getFullname();
        Birthday = nhanVien.getBirthday();
        Home_Town = nhanVien.getAddress();
        Phone = nhanVien.getPhone();
        Email = nhanVien.getEmail();
        Avatar = nhanVien.getAvatar();
        Cover = nhanVien.getCover();
        PhongBan = nhanVien.getRom();
        Position = nhanVien.getChưcvu();

            txtProHeaderName.setText(user_name);
            txtProHeaderPosition.setText(Position);

            txtName.setText(Name);
            txtBirthday.setText(Birthday);
            txtHomeTown.setText(Home_Town);
            txtPhone.setText(Phone);
            txtEmail.setText(Email);
            txtPosition.setText(Position);
            txtPhongBan.setText(PhongBan);

            if (Avatar.equals("")){
//                Toast.makeText(getApplicationContext(),"Bạn chưa cập nhật ảnh đại diện",Toast.LENGTH_LONG).show();
            }
            else {
                /*Lấy chuỗi String từ CSDL
                    Chuyển đổi String -> Byte[]
                    Byte[] -> Bitmap để hiển thị hình ảnh
                 */

                //Chuyển đổi String -> byte[]
                byte[] avatar = Avatar.getBytes();
                //chuyển đổi byte[] -> Bitmap
                Bitmap bitmap= BitmapFactory.decodeByteArray(avatar,0,avatar.length);
                imgAvt.setImageBitmap(bitmap);
            }

            if (Cover.equals("")){
//                Toast.makeText(getApplicationContext(),"Bạn chưa cập nhật ảnh bìa",Toast.LENGTH_LONG).show();
            }
            else {
                byte[] cover = Cover.getBytes();
                Bitmap bitmap= BitmapFactory.decodeByteArray(cover,0,cover.length);
                imgCoverPro.setImageBitmap(bitmap);
            }

        }

    private void addControls() {
        imgCoverPro= findViewById(R.id.imgCoverPro);
        imgAvt = findViewById(R.id.imgProHeaderAvt);

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

        tbThongTinNhanVien = findViewById(R.id.tbThongTinNhanVien);
        thongTinNhanVien = findViewById(R.id.thongTinNhanVien);
        anhDaiDien = findViewById(R.id.Avatar);

        btnCover= findViewById(R.id.btnCoverPro);
        btnAvt= findViewById(R.id.btnAvtPro);

        txtProHeaderName= findViewById(R.id.txtProHeaderName);
        txtProHeaderPosition= findViewById(R.id.txtProHeaderPosition);

        txtName= findViewById(R.id.txtName);
        txtBirthday= findViewById(R.id.txtBirthDay);
        txtHomeTown= findViewById(R.id.txtHomeTown);
        txtPhone= findViewById(R.id.txtPhone);
        txtEmail= findViewById(R.id.txtEmail);
        txtPosition= findViewById(R.id.txtPosition);
        txtPhongBan= findViewById(R.id.txtPhongBan);
    }

    //Đổ hình ảnh vào ImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if (btnSelected==btnCover){
                imgCoverPro.setImageBitmap(bitmap);
            } else if(btnSelected==btnAvt){
                imgAvt.setImageBitmap(bitmap);
            }
        } else if(requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (btnSelected==btnCover){
                    imgCoverPro.setImageBitmap(bitmap);
                } else if(btnSelected==btnAvt){
                    imgAvt.setImageBitmap(bitmap);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        UploadImage(Server.Duongdanupdateavatar, id_user, "anhdaidien", ConverBitMaptoString(imgCoverPro));
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String ConverBitMaptoString(ImageView picture) {
        /***
         * Hàm thay đổi ảnh đại diện hoặc ảnh bìa
         * CHUYỂN TỪ BITMAP ->> BYTE[]
         * UP ẢNH LÊN SQLite
         */

        //Chuyển hình ảnh từ Bitmap -> byte[]
        BitmapDrawable bitmapDrawable= (BitmapDrawable) picture.getDrawable();
        Bitmap bitmap= bitmapDrawable.getBitmap();
        Bitmap resize = Bitmap.createScaledBitmap(bitmap,(int) (bitmap.getWidth()*0.1), (int)(bitmap.getHeight()*0.1), true);
        ByteArrayOutputStream byteArray= new ByteArrayOutputStream();
        resize.compress(Bitmap.CompressFormat.PNG,100,byteArray);
        byte[] image= byteArray.toByteArray();
        //Chuyển hình ảnh từ byte[] -> String
        String hinhAnh = new String(image);
        return hinhAnh;

    }

    private void UploadImage(String link, final String id, final String key, final String avatar) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, link, new Response.Listener<String>() {
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
                hashMap.put(key, avatar);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }
}
