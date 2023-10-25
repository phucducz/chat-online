package com.firebaseclound.chatonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DangNhap extends AppCompatActivity {

    EditText taikhoan, matkhau;
    Button dangnhap;
    String hotenTamp = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        AnhXa();

        SuKien();

    }

    private void SuKien() {
        dangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDangNhap();
            }
        });
    }

    private void AnhXa() {
        taikhoan = (EditText) findViewById(R.id.edtUsername);
        matkhau = (EditText) findViewById(R.id.edtPassword);
        dangnhap = (Button) findViewById(R.id.btnDangNhap);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private void checkDangNhap(){
        RootApplication.firebaseDatabase  = FirebaseDatabase.getInstance();
        RootApplication.firebase = RootApplication.firebaseDatabase.getReference();
        RootApplication.sharedPreferences = getSharedPreferences("cookie",MODE_PRIVATE);
        RootApplication.editor = RootApplication.sharedPreferences.edit();
        EmsConnect.checkLogin(this,taikhoan.getText().toString(),matkhau.getText().toString());
        EmsConnect.getJsonWebsite(this,taikhoan.getText().toString(),true);

        if (EmsConnect.isLogin()){
            if(taikhoan.length()>8){
                hotenTamp = EmsConnect.getTenCMND();
            }else{
               hotenTamp =  EmsConnect.getHo() + " " + EmsConnect.getTen();
            }
            RootApplication.firebase
                    .child("taikhoan")
                    .child(taikhoan.getText().toString())
                    .child("hoten")
                    .setValue(hotenTamp)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(DangNhap.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                            RootApplication.editor.putString("taikhoan",taikhoan.getText().toString());
                            RootApplication.editor.putString("hoten",hotenTamp);
                            RootApplication.editor.commit();
                            RootApplication.setChuyenMangHinh(DangNhap.this,MainActivity.class);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DangNhap.this,"Kết nối Cloud thất bại",Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            Toast.makeText(this,"Đăng nhập thất bại",Toast.LENGTH_SHORT).show();
            matkhau.setText("");
        }
    }
}