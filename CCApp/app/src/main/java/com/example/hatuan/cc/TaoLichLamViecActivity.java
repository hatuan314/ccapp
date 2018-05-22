package com.example.hatuan.cc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hatuan.adapter.TaoLichLamViecAdapter;
import com.example.hatuan.download.DownloadMission;
import com.example.hatuan.model.NhiemVu;
import com.example.hatuan.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TaoLichLamViecActivity extends AppCompatActivity {
    java.util.Calendar calendar= Calendar.getInstance();  // Trả về ngày giờ hiện tại
    SimpleDateFormat sdf1= new SimpleDateFormat("dd/MM/yyyy");
    public String next_week = ""; //Biến trả về tuần của năm
    public String ngayTrongNam = ""; // Biến trả về ngày của năm
    public String ngayTrongTuan = ""; // Biến trả về ngày của tuần

    private TextView txtDateTime, txtCountMission, txtNext_Week;
    private EditText txtEditMission;
    private Button btnAddMision, btnSaveEdit, btnCancel;

    private Spinner spDayWeek;
    private ArrayList<String> arrDayWeek;
    private ArrayAdapter <String> adapterSp;

    private ListView lvMission;
    private TaoLichLamViecAdapter adapterMission;
    private ArrayList <NhiemVu> arrMission;

    private String idMission_Selected="";
    private String contentMission_Selected="";
    String nhiemVu = "";
    String checkNhiemVu = "";

    String idnhanvien = DangNhapActivity.manhanvien;

    NhiemVu mission;
    ArrayList<NhiemVu> danhSachNhiemVu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_lich_lam_viec);
        setTitle("Tạo mới lịch");

        int present_week= calendar.get(Calendar.WEEK_OF_YEAR);//Lấy tuần hiện tại
        calendar.add(Calendar.DAY_OF_WEEK, calendar.MONDAY);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        next_week= String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));//Lấy tuần tiếp theo

        addControls();
        addEvents();
    }

    private void addEvents() {
        spDayWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                /** HIỂN THỊ MÀN HÌNH TẠO NHIỆM VỤ KHI NGƯỜI DÙNG CHỌN THỨ Ở SPINNER   **/

                if (position==0) {
                    calendar.set(Calendar.DAY_OF_WEEK, calendar.MONDAY);
                    ngayTrongTuan = "Thứ " + String.valueOf(calendar.MONDAY);
                    ngayTrongNam = String.valueOf(sdf1.format(calendar.getTime()));
                    displayProcessingItem(position, ngayTrongNam);
                }
                else if (position==1) {
                    calendar.set(Calendar.DAY_OF_WEEK, calendar.TUESDAY);
                    ngayTrongTuan = "Thứ " + String.valueOf(calendar.TUESDAY);
                    ngayTrongNam = String.valueOf(sdf1.format(calendar.getTime()));
                    displayProcessingItem(position, ngayTrongNam);
                }
                else if (position==2) {
                    calendar.set(Calendar.DAY_OF_WEEK, calendar.WEDNESDAY);
                    ngayTrongTuan = "Thứ " + String.valueOf(calendar.WEDNESDAY);
                    ngayTrongNam = String.valueOf(sdf1.format(calendar.getTime()));

                    displayProcessingItem(position, ngayTrongNam);
                }
                else if (position==3) {
                    calendar.set(Calendar.DAY_OF_WEEK, calendar.THURSDAY);
                    ngayTrongTuan = "Thứ " + String.valueOf(calendar.THURSDAY);
                    ngayTrongNam = String.valueOf(sdf1.format(calendar.getTime()));

                    displayProcessingItem(position, ngayTrongNam);
                }
                else if (position==4) {
                    calendar.set(Calendar.DAY_OF_WEEK, calendar.FRIDAY);
                    ngayTrongTuan = "Thứ " + String.valueOf(calendar.FRIDAY);
                    ngayTrongNam = String.valueOf(sdf1.format(calendar.getTime()));

                    displayProcessingItem(position, ngayTrongNam);
                }
                else if (position==5) {
                    calendar.set(Calendar.DAY_OF_WEEK, calendar.SATURDAY);
                    ngayTrongTuan = "Thứ " + String.valueOf(calendar.SATURDAY);
                    ngayTrongNam = String.valueOf(sdf1.format(calendar.getTime()));

                    displayProcessingItem(position, ngayTrongNam);
                }
                else if (position==6) {
                    calendar.set(Calendar.DAY_OF_WEEK, calendar.SUNDAY);
                    ngayTrongTuan = "Thứ " + String.valueOf(calendar.SUNDAY);
                    ngayTrongNam = String.valueOf(sdf1.format(calendar.getTime()));
                    displayProcessingItem(position, ngayTrongNam);
                }

                lvMission.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        idMission_Selected= arrMission.get(position).getId();
                        contentMission_Selected= arrMission.get(position).getNhiemvu();
                        return false;
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Chưa chọn thứ !",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayProcessingItem(final int position, final String ngayTrongNam) {
        /** HÀM LOAD NHIỆM VỤ ĐÃ TẠO TẠI MÀN HÌNH*/


        txtDateTime.setText(ngayTrongNam);

        loadNewMission(idnhanvien,ngayTrongNam,ngayTrongTuan,next_week);


        // XỬ LÝ KHI NGƯỜI DÙNG ẤN BUTTON TẠO MỚI/ INSERT SQL
        btnAddMision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertNewCalendar(idnhanvien, position, ngayTrongNam,ngayTrongTuan, next_week);
                txtEditMission.setText("");
                Toast.makeText(getApplicationContext(), "Thêm mới thành công",Toast.LENGTH_SHORT).show();
                loadNewMission(idnhanvien,ngayTrongNam,ngayTrongTuan,next_week);
            }
        });
    }

    private void insertNewCalendar(final String idnhanvien, int position, final String ngayTrongNam, final String ngayTrongTuan, final String tuanTrongNam) {
        nhiemVu = txtEditMission.getText().toString();
        checkNhiemVu = "0";
        String id = LichLamViecActivity.danhSachNhiemVu.get(LichLamViecActivity.danhSachNhiemVu.size()-1).getId() + 1;
        LichLamViecActivity.danhSachNhiemVu.add(new NhiemVu(id,nhiemVu,checkNhiemVu,tuanTrongNam,ngayTrongTuan,ngayTrongNam,idnhanvien));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.Duongdannhiemvumoi, new Response.Listener<String>() {
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
                hashMap.put("nhiemvu", nhiemVu);
                hashMap.put("checknhiemvu", checkNhiemVu);
                hashMap.put("tuantrongnam", tuanTrongNam);
                hashMap.put("ngaytrongtuan", ngayTrongTuan);
                hashMap.put("ngaytrongthang", ngayTrongNam);
                hashMap.put("idnhanvien", idnhanvien);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void loadNewMission(String idnhanvien, String ngayTrongNam, String ngayTrongTuan, String tuanTrongNam) {
        // HÀM XỬ LÝ TẢI NHIỆM VỤ ĐÃ TẠO TẠI MÀN HÌNH
        String url = Server.Duongdannhiemvu;
        danhSachNhiemVu = new ArrayList<>();
        arrMission = new ArrayList<>();
        DownloadMission downloadMission = new DownloadMission();
        //Lấy chuỗi JSON từ server về app
        downloadMission.execute(url,idnhanvien);

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
                danhSachNhiemVu.add(new NhiemVu(id, nhiemvu, checknhiemvu, tuantrongnam, ngaytrongtuan,ngaytrongthang, idnhanvien));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i=0; i<danhSachNhiemVu.size(); i++){
            mission = danhSachNhiemVu.get(i);
            if(ngayTrongNam.equals(mission.getNgaytrongthang())){
                arrMission.add(mission);
            }
        }
//        database= openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE,null);
//        Cursor query = database.query(
//                "calendar", null,
//                "id_user=? AND date=?"
//                , new String[]{id_user,dateTime},
//                null, null, null, null);
//        arrMission = new ArrayList<>();
//        arrMission.clear();
//        while (query.moveToNext()) {
//            int id = query.getInt(0);
//            String content = query.getString(4);
//            int checked = query.getInt(3);
//            if (id != -1 &&  !content.isEmpty())
//                arrMission.add(new NhiemVu(id, content, 0));  // content
//            else
//                Log.e("ERR", "Lỗi cập nhật");
//        }
//        query.close();

        adapterMission = new TaoLichLamViecAdapter(TaoLichLamViecActivity.this, R.layout.item_nhiem_vu_trong_ngay, arrMission);
        adapterMission.notifyDataSetChanged();
        lvMission.setAdapter(adapterMission);
    }

    private void addControls() {
        spDayWeek= findViewById(R.id.spDayWeek_Create);
        arrDayWeek= new ArrayList<>();
        arrDayWeek= new ArrayList<>();
        arrDayWeek.add("Thứ 2");
        arrDayWeek.add("Thứ 3");
        arrDayWeek.add("Thứ 4");
        arrDayWeek.add("Thứ 5");
        arrDayWeek.add("Thứ 6");
        arrDayWeek.add("Thứ 7");
        arrDayWeek.add("Chủ Nhật");
        adapterSp= new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,arrDayWeek);
        adapterSp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDayWeek.setAdapter(adapterSp);

        txtDateTime= findViewById(R.id.txtDateTime_Create);
        txtNext_Week= findViewById(R.id.txtNext_Week);
        txtNext_Week.setText("Tuần: "+next_week);  // Hiển thị STT tuần tới

        txtEditMission= findViewById(R.id.txtEditMission);
        btnAddMision= findViewById(R.id.btnAddMission);
        btnSaveEdit= findViewById(R.id.btnSaveEdit);
        btnCancel= findViewById(R.id.btnCancel);


        lvMission= findViewById(R.id.lvMission);
        registerForContextMenu(lvMission);
    }
}
