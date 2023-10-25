package com.firebaseclound.chatonline;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class EmsConnect extends Application {

    private static int id;
    private static String ho;
    private static String ten;
    private static String tenCMND;
    private static String maSV;
    private static String maLopCN;
    private static int size;
    private static boolean login = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void checkLogin(final Context context, final String username, final String password) {


        final String url = context.getResources().getString(R.string.login, username, password);

        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            List<Callable<String>> listCallable = new ArrayList<Callable<String>>();

            listCallable.add(new Callable<String>() {
                @Override
                public String call() throws Exception {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                        loadWebsite(context, url, false, true);
                    }
                    return null;
                }
            });

            executorService.invokeAll(listCallable);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void getJsonWebsite(final Context context, final String masv, final boolean network){

        final String url = context.getString(R.string.json_infor,masv);
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            List<Callable<String>> listCallable = new ArrayList<Callable<String>>();

            listCallable.add(new Callable<String>() {
                @Override
                public String call() throws Exception {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                        loadWebsite(context,url,true,network);
                    }
                    return null;
                }
            });

            executorService.invokeAll(listCallable);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String loadWebsite(Context context,String url,boolean isJson,boolean network) {

        StringBuffer stringBuffer = new StringBuffer();
        int idPath = R.string.path_html_shedule;
        if (isJson) {
            idPath = R.string.path_json_info;
        }
        try {
            URL link = new URL(url);
            SecuritySSL.allowAllSSL();
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) link.openConnection();
            InputStream inputStream = httpsURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = "";

            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

            if (isJson) {
                JSONArray array = new JSONArray(stringBuffer.toString().trim());
                JSONObject object = array.getJSONObject(0);
                if (array.length() > 0) {
                    id = object.getInt("id");
                    ho = object.getString("ho");
                    ten = object.getString("ten");
                    maSV = object.getString("maSV");
                    maLopCN = object.getString("maLopCN");
                    size = array.length();
                } else {
                    resetData();
                }
            } else {
                //Log.d("AAA","Size: " + stringBuffer.toString().trim().length());
                if (stringBuffer.toString().trim().length() > context.getResources().getInteger(R.integer.login_sussec)) {
                    login = true;
                    Document document = Jsoup.parse(stringBuffer.toString());
                    String element = document.select("a").get(6).text();
                    String tenjsoup = element.substring(9).trim();
                    setTenCMND(tenjsoup);
                } else {
                    login = false;
                }
            }
        } catch (IOException e) {
            Log.d("AAA", "Error doInBG: " + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("AAA", "Error read: " + e.getMessage());
        }
        return stringBuffer.toString();
    }

    public static void resetData(){
        id = -1;
        ho = "empty";
        ten = "empty";
        maSV = "empty";
        maLopCN = "empty";
        login = false;
        size = 0;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        EmsConnect.id = id;
    }

    public static String getHo() {
        return ho;
    }

    public static void setHo(String ho) {
        EmsConnect.ho = ho;
    }

    public static String getTen() {
        return ten;
    }

    public static void setTen(String ten) {
        EmsConnect.ten = ten;
    }

    public static String getMaSV() {
        return maSV;
    }

    public static void setMaSV(String maSV) {
        EmsConnect.maSV = maSV;
    }

    public static String getMaLopCN() {
        return maLopCN;
    }

    public static void setMaLopCN(String maLopCN) {
        EmsConnect.maLopCN = maLopCN;
    }

    public static int getSize() {
        return size;
    }

    public static void setSize(int size) {
        EmsConnect.size = size;
    }

    public static boolean isLogin() {
        return login;
    }

    public static void setLogin(boolean login) {
        EmsConnect.login = login;
    }

    public static String getTenCMND() {
        return tenCMND;
    }

    public static void setTenCMND(String tenCMND) {
        EmsConnect.tenCMND = tenCMND;
    }
}
