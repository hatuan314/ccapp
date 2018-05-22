package com.example.hatuan.cc;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hatuan.download.DownloadSecurityQuestion;
import com.example.hatuan.download.LogIn;
import com.example.hatuan.model.CauHoiBaoMat;
import com.example.hatuan.model.NhanVien;
import com.example.hatuan.util.CheckConnection;
import com.example.hatuan.util.SendMail;
import com.example.hatuan.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class QuenMatKhauActivity extends AppCompatActivity {
    Spinner spQuestion1, spQuestion2;
    EditText etAnswer1,etAnswer2, etAccount, etEmail;
    Button btnXacNhan;
    public static String newPassword = "";

    NhanVien nhanVien;
    CauHoiBaoMat cauHoiBaoMat;
    ArrayList<NhanVien> danhSachNhanVien = new ArrayList<>();
    ArrayList<CauHoiBaoMat> danhSachCauHoiBaoMat = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);
        addControls();
        addEvents();
    }

    private void addEvents() {
        CatchEventSpinnerOne();
        CatchEventSpinnerTwo();
        CatchEventButtonXacNhan();
    }

    private void CatchEventButtonXacNhan() {
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckConnection.haveNetworkConnection(getApplicationContext())){
                    String username = etAccount.getText().toString().trim();
                    String mEmail = etEmail.getText().toString().trim();
                    String answer1 = etEmail.getText().toString().trim();
                    String answer2 = etEmail.getText().toString().trim();
                    String url = Server.Duongdandangnhap;
                    LogIn logIn = new LogIn();
                    //Lấy chuỗi JSON từ server về app
                    logIn.execute(url,username);
                    //Nhận về chuỗi JSON
                    String data = null;
                    try {
                        data = logIn.get();
                        if(data.equals("{\"nhanvien\":[]}")){
                            Toast.makeText(getApplicationContext(), "Không tồn tại nhân viên" + username,Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra lại",Toast.LENGTH_SHORT).show();
                            etAccount.setText("");
                            return;
                        }

                        String id = "";
                        String tendangnhap = "";
                        String email = "";
                        //Đọc chuỗi JSON
                        danhSachNhanVien = new ArrayList<>();
                        JSONObject mainObject = new JSONObject(data);
                        JSONArray listNhanVien = mainObject.getJSONArray("nhanvien");
                        for (int i=0;i<listNhanVien.length(); i++){
                            JSONObject nhanVienObject = listNhanVien.getJSONObject(i);
                            id = nhanVienObject.getString("id");
                            tendangnhap = nhanVienObject.getString("tendangnhap");
                            email = nhanVienObject.getString("email");
                            danhSachNhanVien.add(new NhanVien(id, tendangnhap, email));
                        }
                        nhanVien = danhSachNhanVien.get(0);
                        //Kiểm tra câu hỏi bảo mật
                        CheckSecurityQuestion(nhanVien);
                        boolean flag = false;
                        flag = CheckSecurityQuestion(nhanVien);
                        if(flag==true){
                            //Gửi mật khẩu mới về mail của người dùng
                            SendEmail(nhanVien);
                            //Cập nhật lại mật khẩu lên CSDL
                            UpdatePassword(nhanVien.getId(), getApplicationContext(), newPassword);
                        } else {
                            etAnswer1.setText("");
                            etAnswer2.setText("");
                            CheckConnection.ShowToast_Short(getApplicationContext(),"Câu trả lời không đúng");
                            return;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    CheckConnection.ShowToast_Short(getApplicationContext(),"Hãy kiểm tra mail để nhận lại mật khẩu của bạn");
                }else {
                    CheckConnection.ShowToast_Short(getApplicationContext(), "Không có kết nối mạng");
                }
            }
        });
    }

    public static void UpdatePassword(final String id, Context context, String password) {
        final String newPasswordMD5 = DangNhapActivity.convertPassMd5(password);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.Duongdanupdatepassword, new Response.Listener<String>() {
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
                hashMap.put("password", newPasswordMD5);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    //Hàm gửi mail
    private void SendEmail(NhanVien nhanVien) {
    /*
        *  String email là tài khoản Email đắng kí tài khoản trên MySQ
        *
        *  mật khẩu sau khi được random thì phải  cập nhập mật khẩu trên MySQ
        * */

        String email = nhanVien.getEmail();
        String subject = "Cấp lại mật khẩu mới";

        newPassword = String.valueOf( rand(100000, 999999));

        String message="Mật khẩu mới của bạn là "+newPassword+"\n bạn hay đổi mật khẩu ở phần đổi mật khẩu trong ứng dụng";



        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }

    /*
    * hầm lấy mật khẩu ngẫu nhiên
    * */
    public int rand(int min, int max) {
        try {
            Random rn = new Random();
            int range = max - min + 1;
            int randomNum = min + rn.nextInt(range);
            return randomNum;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private boolean CheckSecurityQuestion(NhanVien nhanVien) {
        boolean flag = false;
        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
            String manhanvien = nhanVien.getId();
            String url = Server.Duongdancauhoibaomat;
            DownloadSecurityQuestion downloadSecurityQuestion = new DownloadSecurityQuestion();
            downloadSecurityQuestion.execute(url,manhanvien);
            String data = null;
            try {
                data = downloadSecurityQuestion.get();

                String mQuestion1 = spQuestion1.getSelectedItem().toString().trim();
                String mQuestion2 = spQuestion2.getSelectedItem().toString().trim();
                String mAnswer1 = etAnswer1.getText().toString().trim();
                String mAnswer2 = etAnswer2.getText().toString().trim();
                mAnswer1 = mAnswer1.toUpperCase();
                mAnswer2 = mAnswer2.toUpperCase();

                String id = "";
                String cauhoi = "";
                String cautraloi = "";
                String idnhanvien = "";


                ///Đọc chuỗi JSON
                danhSachCauHoiBaoMat = new ArrayList<>();
                JSONObject mainObject = new JSONObject(data);
                JSONArray listCauHoiBaoMat = mainObject.getJSONArray("cauhoibaomat");
                for (int i=0;i<listCauHoiBaoMat.length(); i++){
                    JSONObject cauHoiBaoMatObject = listCauHoiBaoMat.getJSONObject(i);
                    id = cauHoiBaoMatObject.getString("id");
                    cauhoi = cauHoiBaoMatObject.getString("cauhoi");
                    cautraloi = cauHoiBaoMatObject.getString("cautraloi");
                    idnhanvien = cauHoiBaoMatObject.getString("idnhanvien");
                    danhSachCauHoiBaoMat.add(new CauHoiBaoMat(id, cauhoi, cautraloi,idnhanvien));
                }
                /*Kiểm tra câu hỏi bảo mât:
                    - Kiểm tra 2 câu hỏi có trùng nhau không
                    - Kiểm tra 2 câu hỏi có trùng với dữ liệu trên CSDL không
                 */
                if(!mQuestion1.equals(mQuestion2)){
                    for (int i = 0; i<danhSachCauHoiBaoMat.size(); i++){
                        cauHoiBaoMat = danhSachCauHoiBaoMat.get(i);
                        String question = cauHoiBaoMat.getCauhoi();
                        if(mQuestion1.equals(question)){
                            flag = true;
                            continue;
                        }else if(mQuestion2.equals(question)){
                            flag = true;
                            continue;
                        }else {
                            CheckConnection.ShowToast_Short(getApplicationContext(),"Câu hỏi bảo mật không đúng");
                            flag = false;
                            break;
                        }
                    }
                } else {
                    flag = false;
                }
                /*Kiểm tra câu trả lời:
                    - Kiểm tra 2 câu trả lời có trùng với dữ liệu trên CSDL không
                 */
                if(flag==true){
                    for (int j = 0; j<danhSachCauHoiBaoMat.size();j++){
                        cauHoiBaoMat = danhSachCauHoiBaoMat.get(j);
                        String answer = cauHoiBaoMat.getCautraloi().toUpperCase();
                        if(mAnswer1.equals(answer)){
                            flag = true;
                            continue;
                        } else if (mAnswer2.equals(answer)){
                            flag = true;
                            continue;
                        }else {
                            CheckConnection.ShowToast_Short(getApplicationContext(),"Câu trả lời không đúng");
                            flag = false;
                            break;
                        }
                    }
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            CheckConnection.ShowToast_Short(getApplicationContext(), "Không có kết nối mạng");
        }
        return flag;
    }

    private void CatchEventSpinnerTwo() {
        ArrayList<String> QuestionTwo = new ArrayList<>();
        QuestionTwo.add("Bạn có người yêu chưa?");
        QuestionTwo.add("Link Facebook của người yêu bạn là gì?");
        QuestionTwo.add("Số điện thoại của người yêu bạn là gì?");
        QuestionTwo.add("Bạn xếp hình trong bao lâu?");
        QuestionTwo.add("Con số yêu thích của bạn là gì?");
        QuestionTwo.add("Tên người yêu cũ của bạn là gì?");
        QuestionTwo.add("Tại sao bạn và người yêu cũ lại chia tay?");
        QuestionTwo.add("Thú cưng của bạn tên gì?");
        QuestionTwo.add("Trường cấp 3 bạn từng theo học tên là gì?");
        QuestionTwo.add("Bạn sinh sống ở đâu?");
        QuestionTwo.add("Món ăn yêu thích của bạn là gì?");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,QuestionTwo);
        spQuestion2.setAdapter(arrayAdapter);
    }

    private void CatchEventSpinnerOne() {
        ArrayList<String> QuestionOne = new ArrayList<>();
        QuestionOne.add("Bạn có người yêu chưa?");
        QuestionOne.add("Link Facebook của người yêu bạn là gì?");
        QuestionOne.add("Số điện thoại của người yêu bạn là gì?");
        QuestionOne.add("Bạn xếp hình trong bao lâu?");
        QuestionOne.add("Con số yêu thích của bạn là gì?");
        QuestionOne.add("Tên người yêu cũ của bạn là gì?");
        QuestionOne.add("Tại sao bạn và người yêu cũ lại chia tay?");
        QuestionOne.add("Thú cưng của bạn tên gì?");
        QuestionOne.add("Trường cấp 3 bạn từng theo học tên là gì?");
        QuestionOne.add("Bạn sinh sống ở đâu?");
        QuestionOne.add("Món ăn yêu thích của bạn là gì?");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,QuestionOne);
        spQuestion1.setAdapter(arrayAdapter);
    }

    private void addControls() {
        spQuestion1 = findViewById(R.id.spQuestion1);
        spQuestion2 = findViewById(R.id.spQuestion2);

        etAnswer1 = findViewById(R.id.etAnswer1);
        etAnswer2 = findViewById(R.id.etAnswer2);
        etAccount = findViewById(R.id.etAccount);
        etEmail = findViewById(R.id.etEmail);

        btnXacNhan = findViewById(R.id.btnXacNhan);
    }
}
