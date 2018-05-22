package com.example.hatuan.cc;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hatuan.download.LogIn;
import com.example.hatuan.model.NhanVien;
import com.example.hatuan.util.CheckConnection;
import com.example.hatuan.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DangNhapActivity extends AppCompatActivity {
    EditText etUserName, etPassword;
    Button btnLogIn, btnForgotPassword;
    TextView tvLanguage;

    public static int check;
    public static String manhanvien;
    NhanVien nhanVien;
    public static ArrayList<NhanVien> danhSachNhanVien = new ArrayList<>();

    String mIMEI = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        //Ánh xạ
        addControls();
        addEvents();
    }
    private void addEvents() {
        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
            EventLogin();
            EventForgetPassword();
        }else {
            CheckConnection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra lại kết nối internet");
        }
    }

    private void EventForgetPassword() {
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangNhapActivity.this, QuenMatKhauActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void EventLogin() {
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String username = etUserName.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                //convert mật khẩu ra md5
                String passwordMD5 = convertPassMd5(password);
                String url = Server.Duongdandangnhap;
                LogIn logIn = new LogIn();
                //Lấy chuỗi JSON từ server về app
                logIn.execute(url,username);

                try {
                    //Nhận về chuỗi JSON
                    String data = logIn.get();
                    if(data.equals("{\"nhanvien\":[]}")){
                        Toast.makeText(getApplicationContext(), "Tên đăng nhập không đúng",Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Vui lòng nhập lại",Toast.LENGTH_SHORT).show();
                        etUserName.setText("");
                        return;
                    }

                    String id = "";
                    String tendangnhap = "";
                    String mk = "";
                    String email = "";
                    String tennhanvien = "";
                    String sdt = "";
                    String ngaysinh = "";
                    String quequan = "";
                    String phongban = "";
                    String chucvu = "";
                    String IMEI = "";
                    String anhdaidien = "";
                    String anhbia = "";
                    //Đọc chuỗi JSON
                    danhSachNhanVien = new ArrayList<>();
                    JSONObject mainObject = new JSONObject(data);
                    JSONArray listNhanVien = mainObject.getJSONArray("nhanvien");
                    for (int i=0;i<listNhanVien.length(); i++){
                        JSONObject nhanVienObject = listNhanVien.getJSONObject(i);
                        id = nhanVienObject.getString("id");
                        tendangnhap = nhanVienObject.getString("tendangnhap");
                        mk = nhanVienObject.getString("password");
                        email = nhanVienObject.getString("email");
                        tennhanvien = nhanVienObject.getString("tennhanvien");
                        sdt = nhanVienObject.getString("sodienthoai");
                        ngaysinh = nhanVienObject.getString("ngaysinh");
                        quequan = nhanVienObject.getString("quequan");
                        phongban = nhanVienObject.getString("phongban");
                        chucvu = nhanVienObject.getString("chucvu");
                        IMEI = nhanVienObject.getString("imei");
                        anhdaidien = nhanVienObject.getString("anhdaidien");
                        anhbia = nhanVienObject.getString("anhbia");
                        danhSachNhanVien.add(new NhanVien(id, tendangnhap, mk, email, tennhanvien,sdt, ngaysinh, quequan, phongban, chucvu,IMEI,anhdaidien,anhbia));
                    }
                    nhanVien = danhSachNhanVien.get(0);
                    if(!passwordMD5.equals(nhanVien.getPassword())){
                        CheckConnection.ShowToast_Short(getApplicationContext(), "Mật khẩu không đúng");
                        etPassword.setText("");
                        return;
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Check IMEI của máy
                final String imei = nhanVien.getImei();
                manhanvien = nhanVien.getId();
                getImeiPhone();
                check = 1;
                if(imei.length()==4){
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.Duongdanupdateimei, new Response.Listener<String>() {
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
                            hashMap.put("id", manhanvien);
                            hashMap.put("IMEI", mIMEI);
                            return hashMap;
                        }
                    };
                    requestQueue.add(stringRequest);
                } else if(!imei.equals(mIMEI)){
                    check = 0;
                    CheckConnection.ShowToast_Short(getApplicationContext(),"Bạn không phải " + nhanVien.getFullname());
                    CheckConnection.ShowToast_Short(getApplicationContext(),"Đếu cho điểm danh :v");

                }
                Intent intent = new Intent(getApplicationContext(), LichLamViecActivity.class);
                intent.putExtra("idnhanvien",manhanvien);
                startActivity(intent);
            }
        });
    }

    private void addControls() {
        tvLanguage = findViewById(R.id.tvLanguage);

        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);

        btnLogIn = findViewById(R.id.btnLogIn);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getImeiPhone();
        }
    }

    /*
    * hàm lấy imei của máy
    * */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getImeiPhone() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        telephonyManager.getDeviceId();
        mIMEI = telephonyManager.getDeviceId();

    }

    //Mã hóa mật khẩu bằng MD5
    public static String convertPassMd5(String pass) {
        String password = null;
        MessageDigest mdEnc;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1, mdEnc.digest()).toString(16);
            while (pass.length() < 32) {
                pass = "0" + pass;
            }
            password = pass;
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return password;
    }
}
