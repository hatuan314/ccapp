package com.example.hatuan.cc;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hatuan.model.NhanVien;
import com.example.hatuan.util.Server;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static java.lang.Math.sqrt;

public class QRActivity extends FragmentActivity implements ZXingScannerView.ResultHandler {

    public String imeiphone = "";
    private String linkdefault = "https://www.facebook.com/groups/156689748317923/"; //Link dùng để so sánh vs nội dung QR
    //Link có thể thay đổi
    private double kd = MainActivity.kd;
    private double vd = MainActivity.vd;
    public String check = "";

    public static double kdgoc = 20.9744837; //Kinh độ lấy làm mốc < địa điểm cty >
    public static double vdgoc = 105.7637948; //Vĩ độ lấy làm mốc < địa điểm cty >

    private ZXingScannerView zXingScannerView;


    private int kt = 0;
    private Boolean flag = false; //Boolean Kiểm tra máy đã bật GPS hay chưa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        Sosanglocation();
        scanQRCode();
    }


    private void scanQRCode() {
        //--------- quét QR -----------//
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
        Date currentTime = Calendar.getInstance().getTime();
        final Date day = new Date();
        final CharSequence s  = DateFormat.format("d MMM yyyy ", day.getTime());
        String time = String.valueOf(currentTime);
        String dateArray[] = time.split(" ");
        final String thoigian = dateArray[3];
        Log.d("thoigian", thoigian);
        String timeArray[] = thoigian.split(":");
        if (Integer.parseInt(timeArray[0])>8){
            check = "0";
        }else if(Integer.parseInt(timeArray[0])==8 && Integer.parseInt(timeArray[1])>0){
            check = "0";
        } else {
            check = "1";
        }

        ArrayList<NhanVien> danhSachNhanVien = DangNhapActivity.danhSachNhanVien;
        final NhanVien nhanVien = danhSachNhanVien.get(0);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.Duongdandiemdanh, new Response.Listener<String>() {
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
                hashMap.put("idnhanvien", nhanVien.getId());
                hashMap.put("tenhanvien", nhanVien.getFullname());
                hashMap.put("ngay", String.valueOf(s));
                hashMap.put("thoigian", thoigian);
                hashMap.put("checkdiemdanh", check);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void handleResult(Result result) {
        //-------- Lấy thông tin QR tại đây ----------//
        String link = result.getText();
        if (link.equals(linkdefault) && kt == 1){ //Nếu đúng so với Link dc gán trong QR thì gửi thông tin lên DataBase
            Toast.makeText(QRActivity.this,"Điểm danh thành công",Toast.LENGTH_SHORT).show();
            Toast.makeText(QRActivity.this,"Life And Technology",Toast.LENGTH_LONG).show();
            //------ So sánh và gửi thông tin tại đây ------//

        }
        else { //Nếu Khác so với Link dc gán trong QR thì gửi thông tin lên DataBase
            Toast.makeText(QRActivity.this,"Điểm danh không thành công",Toast.LENGTH_SHORT).show();
        }
        zXingScannerView.resumeCameraPreview(this);
        zXingScannerView.stopCamera();
        finish();
    }

//    private void getImeiPhone() {
//        //--------- lấy imei phone --------/
//        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        telephonyManager.getDeviceId();
//        imeiphone = telephonyManager.getDeviceId(); //Imei phone String
//        Log.d("TINHDZ", "Imei: " + imeiphone);
//        Toast.makeText(QRActivity.this,"Imei Phone: "+imeiphone,Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //So sánh vị trí thông qua tính bán kính
    //R = sqrt (a*a + b*b)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void Sosanglocation() {
        double a = 0;
        double b = 0;
        double c = 0;
        double d = 0;
        a = (float) (kd - kdgoc);
        b = (float) (vd - vdgoc);
        d = (float) a*a + b*b;
        c = (float) sqrt(d);
        //Log xem trong Logcat
        Log.d("TINHDZ","ala: " + a);
        Log.d("TINHDZ","bla: " + b);
        Log.d("TINHDZ","dla: " + d);
        Log.d("TINHDZ","cla: " + c);
        c *= 10000; //* 10000 để giảm sai số do bán kính quá bé
        if (c < 1.3){
            // SenNotification là tạo thông báo ra màn hình
            // String thứ nhất là tiêu đề thông báo String thứ 2 là nội dung thông báo

            kt = 1; // Nếu < 1 tứ điểm danh thành công và sẽ trả về 1
            //Cái cần nhận ở đây là kt
        } else {
            //Ngược lại thì kt = 0
            kt = 0;
            Toast.makeText(QRActivity.this,"Điểm danh không thành công" +"\n"+ c,Toast.LENGTH_SHORT).show();
        }
    }

}

