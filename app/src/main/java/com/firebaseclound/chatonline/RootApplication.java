package com.firebaseclound.chatonline;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import static java.util.Calendar.getInstance;

public class RootApplication extends Application {
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    private static Context context;

    public static FirebaseDatabase firebaseDatabase;
    public static DatabaseReference firebase;

    private static Calendar time = getInstance();

    private static Intent intent ;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static String ID(){
        String result = "";
        Calendar thoigian =  getInstance();
        String gio = thoigian.get(Calendar.HOUR_OF_DAY) + "";
        if(thoigian.get(Calendar.HOUR_OF_DAY)<10){
            gio = "0" + thoigian.get(Calendar.HOUR_OF_DAY);
        }
        String phut = thoigian.get(Calendar.MINUTE) + "";
        if(thoigian.get(Calendar.MINUTE)<10){
            phut = "0" + thoigian.get(Calendar.MINUTE);
        }
        String giay = thoigian.get(Calendar.SECOND) + "";
        if(thoigian.get(Calendar.SECOND)<10){
            giay = "0" + thoigian.get(Calendar.SECOND);
        }
        String ngay = thoigian.get(Calendar.DAY_OF_MONTH) + "";
        if(thoigian.get(Calendar.DAY_OF_MONTH)<10){
            ngay = "0" + thoigian.get(Calendar.DAY_OF_MONTH);
        }
        String thang = thoigian.get(Calendar.MONTH) + "";
        if(thoigian.get(Calendar.MONTH)<10){
            thang = "0" + thoigian.get(Calendar.MONTH);
        }
        String nam = thoigian.get(Calendar.YEAR) + "";
        result = (nam) + (thang) + (ngay) + (gio)  + (phut) + (giay);

        return result;
    }

    public static String getThoiGianHienTai() {
        String result = time.get(Calendar.HOUR) + ":" + time.get(Calendar.MINUTE) + ":" + time.get(Calendar.SECOND)
                + " - "
                + time.get(Calendar.DAY_OF_MONTH) + "/"
                + time.get(Calendar.MONTH) + "/"
                + time.get(Calendar.YEAR);
        return result;
    }

    public static void setHinhAnh(Context context, String url, ImageView imageView, int pictureHolder, int pictureError){
        if(url == null){
            url = "https://daohieu.com/wp-content/uploads/2020/05/meo-con.jpg";
        }
        Picasso.with(context)
                .load(url)
                .placeholder(pictureHolder)
                .error(pictureError)
                .into(imageView);
    }

    public static void setChuyenMangHinh(Context activityFrom, Class activityTo){
        intent = new Intent(activityFrom,activityTo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activityFrom.startActivity(intent);
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        RootApplication.context = context;
    }
}
