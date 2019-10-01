package com.abms.af.projeversion02;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abms.af.projeversion02.Models.Kullanicigirissonuc;
import com.abms.af.projeversion02.RestApi.ManagerAll;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView uyelik,genel_uyarı, sifremi_unutttum;
    EditText giris_mail,giris_sifre;
    Button giris_buton;
    SharedPreferences sharedPreferences;
    int Kullanici_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        /*
            İzin almak içi olan kısım
         */

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Depolama izni");
                alertBuilder.setIcon(android.R.drawable.ic_dialog_info);
                alertBuilder.setMessage("Yükleme ve İndirme işlemleri için kullanilacaktir!");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                });

                AlertDialog alert = alertBuilder.create();
                alert.show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                // MY_PERMISSIONS_REQUEST_CAMERA is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }




        /*
        session için olusturulan kullanıcı zaten giriş yapmısmı kısmı
         */
        sharedPreferences =getApplicationContext().getSharedPreferences("giris",0);
        if(sharedPreferences.getInt("uye_id",0) != 0)
        {
            Kullanici_id=sharedPreferences.getInt("uye_id",0);
            Intent anaekran=new Intent(getApplicationContext(),ana_sayfa.class);
            anaekran.putExtra("Kullanici_id",Kullanici_id);
            startActivity(anaekran);
        }
        tanimla();
        islevver();

    }
    private void tanimla()
    {
        uyelik=(TextView) findViewById(R.id.hesap_acma);
        giris_mail=(EditText) findViewById(R.id.giris_mail);
        giris_sifre=(EditText) findViewById(R.id.giris_sifre);
        giris_buton=(Button) findViewById(R.id.giris_buton);
        genel_uyarı=(TextView) findViewById(R.id.giris_genel_uyarı);
        sifremi_unutttum = (TextView) findViewById(R.id.sifremi_unuttum);

    }
    private void islevver()
    {
        uyelik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hesap_acma();
            }
        });

        giris_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webservis_kullanicigiris();
            }
        });

        sifremi_unutttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PasswordRecoveryPopup.class);
                startActivityForResult(i,99);
            }
        });

    }
    /*
    *kullanıcı hesabı olusturma sayfası göndermesi
     */
    private void hesap_acma()
    {
        Intent hesap_kayıt=new Intent(getApplicationContext(),hesap_acmaActivity.class);
        startActivity(hesap_kayıt);
    }
    /*
    *Web servis kullanıcı giriş kontrolü
    */
    private void webservis_kullanicigiris()
    {
        final SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Yükleniyor");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<Kullanicigirissonuc> kontrol= ManagerAll.webyonet().kontrolet(giris_mail.getText().toString(),giris_sifre.getText().toString());
        kontrol.enqueue(new Callback<Kullanicigirissonuc>() {
            @Override
            public void onResponse(Call<Kullanicigirissonuc> call, Response<Kullanicigirissonuc> response) {
                //Toast.makeText(getApplicationContext(),"id"+response.body().getKullaniciid(),Toast.LENGTH_LONG).show();
                //Log.i("düzenli gidiyoo", "onFailure:"+response.body().getKullaniciid());
                if(response.body().getKullanicigirissonuc().toString().equals("Giris Basarili"))
                {
                    /*
                    kullanıcı id paylasılıyor bidaha girdiğinde tekrar giriş istenmemesi için
                     */
                    Kullanici_id=Integer.parseInt(response.body().getKullaniciid().toString());
                    if(Kullanici_id !=0)
                    {
                        sharedPreferences = getApplicationContext().getSharedPreferences("giris",0);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putInt("uye_id",Integer.parseInt(response.body().getKullaniciid().toString()));
                        editor.commit();
                       // Toast.makeText(getApplicationContext(),"Basarili bir sekilde giris yaptınız",Toast.LENGTH_LONG).show();
                        pDialog.cancel();
                        Intent anaekran=new Intent(getApplicationContext(),ana_sayfa.class);
                        anaekran.putExtra("Kullanici_id",Kullanici_id);
                        startActivity(anaekran);
                        genel_uyarı.setVisibility(View.INVISIBLE);

                    }

                }
                else if(response.body().getKullanicigirissonuc().toString().equals("Giris Basarisiz"))
                {
                   // Toast.makeText(getApplicationContext(),"giris basarisiz",Toast.LENGTH_LONG).show();
                    genel_uyarı.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<Kullanicigirissonuc> call, Throwable t) {
               Log.i("HATA VERDİİİİ", "onFailure:"+t.getMessage());
               Toast.makeText(getApplicationContext(),"hata olustu giris kısmı "+t.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }






    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    MainActivity.this.finish();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    //Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }

    }
}
