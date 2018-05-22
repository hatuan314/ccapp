package com.example.hatuan.cc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DoiMatKhauActivity extends AppCompatActivity {
    EditText etMatKhauCu;
    EditText etMatKhauMoi;
    Button btnXacNhan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String matKhauCu = DangNhapActivity.danhSachNhanVien.get(0).getPassword();
                String mMatKhauCu = etMatKhauCu.getText().toString();
                String mMatKhauCuMD5 = DangNhapActivity.convertPassMd5(mMatKhauCu);
                if (mMatKhauCuMD5.equals(matKhauCu)){
                    String matKhauMoi = etMatKhauMoi.getText().toString();
                    String id = DangNhapActivity.danhSachNhanVien.get(0).getId();
                    QuenMatKhauActivity.UpdatePassword(id, getApplicationContext(), matKhauMoi);
                    Toast.makeText(getApplicationContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addControls() {
        etMatKhauCu = findViewById(R.id.etMatKhauCu);
        etMatKhauMoi = findViewById(R.id.etMatKhauMoi);
        btnXacNhan = findViewById(R.id.btnXacNhan);
    }
}
