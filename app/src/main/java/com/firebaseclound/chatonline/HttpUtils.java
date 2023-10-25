package com.firebaseclound.chatonline;

import android.util.Log;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class HttpUtils {
    public static SSLContext sslContext;
    public static HostnameVerifier hostnameVerifier;

    public static void setUpHostnameVerifier(){
        hostnameVerifier=new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
    }
    public static void setupSSLContext(int id){
        try {

            /*Load file .crt to InputStream*/
            InputStream inputStream= RootApplication.getContext().getResources().openRawResource(id);

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certificateFactory
                    .generateCertificate(inputStream);
            String alias = cert.getSubjectX500Principal().getName();

            // Create keystore and add to ssl context
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null);
            trustStore.setCertificateEntry(alias, cert);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
            kmf.init(trustStore, null);
            KeyManager[] keyManagers = kmf.getKeyManagers();

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(trustStore);
            TrustManager[] trustManagers = tmf.getTrustManagers();

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, trustManagers, null);


        }catch (Exception e){
            e.printStackTrace();
            Log.d("AAA","Error X509: " + e.getMessage());
        }
    }
}
