package com.example.hatuan.cc;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;

import pl.droidsonroids.gif.GifImageView;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GifImageView gifimage, gifloading;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int MY_PERMISSION_REQUEST_CORE = 27798;

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private static Location mLastLocation;


    public static double kd = 20.9744837;
    public static double vd = 105.7637948;

    private int kt = 0;
    private Boolean flag = false; //Boolean Kiểm tra máy đã bật GPS hay chưa

    private static int UPDATE_INTERVAL = 5000;
    private static int FATEST_INTERVAL = 3000;
    private static int DISPLACEMRNT = 10;

    Marker mCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUplacation(); //Lấy địa điểm của điện thoại
        gifimage = findViewById(R.id.gifimage);
        gifloading = findViewById(R.id.gifloading);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.mytransition);
        gifimage.startAnimation(animation);
        gifloading.startAnimation(animation);
        final Intent intent = new Intent(MainActivity.this,DangNhapActivity.class);
        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
        thread.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case  MY_PERMISSION_REQUEST_CORE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayServices()) {
                        builGoogleApiClient();
                        createLocationRequets();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            displayLocation();
                        }
                    }
                }
                break;
        }
    }

    //Cài đặt vị trí
    private void setUplacation() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[] {
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            },MY_PERMISSION_REQUEST_CORE);
        } else {
            if (checkPlayServices()) {
                builGoogleApiClient();
                createLocationRequets();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    displayLocation();
                }
            }
        }
    }

    // Hiển thị tọa độ
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            kd = mLastLocation.getLatitude(); //Kinh độ
            vd = mLastLocation.getLongitude(); //Vĩ độ

            //So sánh vị trí thông qua tính bán kính
            //Sosanglocation(latitude, longtitude);
            Toast.makeText(MainActivity.this,"Vị trí của bạn: " + "\n" + kd + "\n" +vd,Toast.LENGTH_SHORT).show();
            Log.d("TINHDZ", "Vị trí của bạn: " + "\n" + kd + "\n" +vd);
        }
        else
            Log.d("TINHDZ", "Đang lấy vị trị của bạn!");
    }

    //So sánh vị trí thông qua tính bán kính
    //R = sqrt (a*a + b*b)
    /*@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void Sosanglocation(double latitude, double longtitude) {
        double a = 0;
        double b = 0;
        double c = 0;
        double d = 0;
        a = (float) (mLastLocation.getLatitude() - kdgoc);
        b = (float) (mLastLocation.getLongitude() - vdgoc);
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
            //senNotification("TINHDEPZAI","Điểm danh thành công");
            Toast.makeText(MainActivity.this,"Điểm danh thành công" +"\n"+ c,Toast.LENGTH_SHORT).show();
            kt = 1; // Nếu < 1 tứ điểm danh thành công và sẽ trả về 1
        } else {
            //Ngược lại thì kt = 0
            kt = 0;
            //senNotification("TINHDEPZAI","Sorry");
            Toast.makeText(MainActivity.this,"Điểm danh không thành công" +"\n"+ c,Toast.LENGTH_SHORT).show();
        }
    }*/

    private void createLocationRequets() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMRNT);
    }

    private void builGoogleApiClient() {
        mGoogleApiClient = new  GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    //Kiểm tra thiết bị có hỗ trợ GPS hay không
    private boolean checkPlayServices() {
        int resultcode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultcode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultcode))
                GooglePlayServicesUtil.getErrorDialog(resultcode,this,PLAY_SERVICES_RESOLUTION_REQUEST).show();
            else {
                Toast.makeText(this,"Thiết bị không hỗ trợ", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdate();
    }

    //Cập nhật vị trí
    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
    }

}
