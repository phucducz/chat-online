package com.firebaseclound.chatonline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import gun0912.tedbottompicker.TedRxBottomPicker;

public class MainActivity extends AppCompatActivity {

    private TextView hoten,taikhoan;
    private ImageView avatar,thoat;
    private String urlAvatar = "https://daohieu.com/wp-content/uploads/2020/05/meo-con.jpg";
    private static String username = "";
    private String name = "";
    private static GridView gvMessage;
    private static ArrayList<Message> arrayMessage;
    private static AdapterMessage adapterMessage;
    private ImageView imgSend;
    private EditText edtMessage;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    int REQUEST_CODE_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!isDangNhap()){
            RootApplication.setChuyenMangHinh(this,DangNhap.class);
            finish();
        }
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://chat-online-bb7f3.appspot.com");

        AnhXa();

        TaiThongTin();

        SuKien();
    }

    private void SuKien() {
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtMessage.getText().toString().trim().equals("")){
                    Map<String,String> map = new HashMap<>();
                    map.put("avatar",urlAvatar);
                    map.put("taikhoan",username);
                    map.put("noidung",edtMessage.getText().toString().trim());
                    map.put("hoten",name);
                    map.put("thoigian",RootApplication.getThoiGianHienTai());
                    RootApplication.firebase
                            .child("message")
                            .child(RootApplication.ID())
                            .setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            edtMessage.setText("");

                        }
                    });

                }
            }
        });

        thoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Bạn thật sự muốn đăng xuất");
                builder.setTitle("Cảnh Báo");
                builder.setNegativeButton("Từ chối", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RootApplication.sharedPreferences = getSharedPreferences("cookie",MODE_PRIVATE);
                        RootApplication.editor = RootApplication.sharedPreferences.edit();
                        RootApplication.editor.clear();
                        RootApplication.editor.commit();
                        RootApplication.setChuyenMangHinh(MainActivity.this,DangNhap.class);
                        finish();
                    }
                });
                builder.show();
            }

        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        ChonAnh();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }


                };
                TedPermission.with(MainActivity.this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();

            }
        });
    }

    private void ChonAnh() {
        TedBottomPicker.with(MainActivity.this)
                .show( new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                            avatar.setImageBitmap(bitmap);
                            Upload();
                        }catch (Exception e){
                            Toast.makeText(MainActivity.this,"Cập nhật ảnh thất bại",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        username = RootApplication.sharedPreferences.getString("taikhoan","null");
        name =  RootApplication.sharedPreferences.getString("hoten","null");
    }

    void Upload(){
        String filename = username + ".png";
        StorageReference storageRef = storage.getReference();
        StorageReference mountainsRef = storageRef.child(filename);

        avatar.setDrawingCacheEnabled(true);
        avatar.buildDrawingCache();
        Bitmap bitmap = avatar.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(MainActivity.this,"Cập nhật ảnh đại diện thất bại!",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                String uri = "https://firebasestorage.googleapis.com/v0/b/chat-online-bb7f3.appspot.com/o/" + taskSnapshot.getMetadata().getPath() + "?alt=media&";
                RootApplication.firebase
                        .child("taikhoan")
                        .child(username)
                        .child("avatar")
                        .setValue(uri)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this,"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void TaiThongTin() {
        if(RootApplication.sharedPreferences.getString("taikhoan",null)!=null){
            username = RootApplication.sharedPreferences.getString("taikhoan",null);
            RootApplication.firebase
                    .child("taikhoan")
                    .child(username)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child("avatar").getValue()!=null){
                                urlAvatar = snapshot.child("avatar").getValue().toString();
                            }else{
                                urlAvatar = "https://daohieu.com/wp-content/uploads/2020/05/meo-con.jpg";
                            }
                            RootApplication.setHinhAnh(MainActivity.this,urlAvatar,avatar,R.drawable.ic_action_image,R.drawable.ic_action_image);
                            hoten.setText(name);
                            taikhoan.setText(username);
                            LoadMessage();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

    }

    public static void LoadMessage(){
        RootApplication
                .firebase
                .child("message")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot!=null){
                            String hinhanh = "https://daohieu.com/wp-content/uploads/2020/05/meo-con.jpg";
                            if(snapshot.child("avatar").getValue()!=null){
                                hinhanh = snapshot.child("avatar").getValue().toString();
                            }
                            arrayMessage.add(new Message(
                                    hinhanh,
                                    snapshot.child("hoten").getValue().toString(),
                                    username,
                                    snapshot.child("taikhoan").getValue().toString(),
                                    snapshot.child("noidung").getValue().toString(),
                                    snapshot.child("thoigian").getValue().toString()
                            ));

                            adapterMessage.notifyDataSetChanged();
                            gvMessage.smoothScrollToPosition(arrayMessage.size()-1);
                            return;
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void AnhXa() {
        hoten = (TextView) findViewById(R.id.txtHoTen);
        taikhoan = (TextView) findViewById(R.id.txtTaiKhoan);
        avatar = (ImageView) findViewById(R.id.imgIconAvatar);
        thoat = (ImageView) findViewById(R.id.imgThoat);

        gvMessage = (GridView) findViewById(R.id.gvMessage);
        imgSend = (ImageView) findViewById(R.id.imgSend);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        arrayMessage = new ArrayList<>();
        adapterMessage = new AdapterMessage(MainActivity.this,R.layout.element_message_group,arrayMessage);
        gvMessage.setAdapter(adapterMessage);

        RootApplication.firebaseDatabase  = FirebaseDatabase.getInstance();
        RootApplication.firebase = RootApplication.firebaseDatabase.getReference();
    }

    private boolean isDangNhap() {
        RootApplication.sharedPreferences = getSharedPreferences("cookie",MODE_PRIVATE);
         if(RootApplication.sharedPreferences.getString("taikhoan",null)!=null){
             return true;
         }else{
             return false;
         }
    }
}