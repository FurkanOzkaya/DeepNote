package com.abms.af.projeversion02;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.abms.af.projeversion02.Models.GelistirmeDurumu;
import com.abms.af.projeversion02.Models.Kullanicigirissonuc;
import com.abms.af.projeversion02.RestApi.ManagerAll;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView uyelik, genel_uyarı, sifremi_unutttum,baslik;
    EditText giris_mail, giris_sifre;
    Button giris_buton;
    SharedPreferences sharedPreferences;
    int Kullanici_id;
    Typeface tf1;

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
                alertBuilder.setMessage("Yükleme ve indirme işlemleri için kullanılacaktır!");
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
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }


        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);
        if (sharedPreferences.getInt("uye_id", 0) != 0) {
            Kullanici_id = sharedPreferences.getInt("uye_id", 0);
            Intent anaekran = new Intent(getApplicationContext(), ana_sayfa.class);
            anaekran.putExtra("Kullanici_id", Kullanici_id);
            startActivity(anaekran);
        }

        tanimla();
        islevver();


    }

    private void tanimla() {
        baslik = findViewById(R.id.loginbaslik);
        uyelik = (TextView) findViewById(R.id.hesap_acma);
        giris_mail = (EditText) findViewById(R.id.giris_mail);
        giris_sifre = (EditText) findViewById(R.id.giris_sifre);
        giris_buton = (Button) findViewById(R.id.giris_buton);
        genel_uyarı = (TextView) findViewById(R.id.giris_genel_uyarı);
        sifremi_unutttum = (TextView) findViewById(R.id.sifremi_unuttum);
    }

    private void islevver() {


        giris_buton.setEnabled(false);
        giris_buton.setBackgroundColor(getResources().getColor(R.color.holo_blue_dark_saydam));


        giris_sifre.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (giris_sifre.length() > 0 && giris_mail.length() >0 ){
                    giris_buton.setEnabled(true);
                    giris_buton.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
                }
                else{
                    giris_buton.setEnabled(false);
                    giris_buton.setBackgroundColor(getResources().getColor(R.color.holo_blue_dark_saydam));
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        giris_mail.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (giris_sifre.length() > 0 && giris_mail.length() >0 ){
                    giris_buton.setEnabled(true);
                    giris_buton.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
                }
                else{
                    giris_buton.setEnabled(false);
                    giris_buton.setBackgroundColor(getResources().getColor(R.color.holo_blue_dark_saydam));
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
        });


        tf1 = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/DamionRegular.ttf");
        baslik.setTypeface(tf1);

        uyelik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hesap_acma();
            }
        });

        giris_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (giris_mail.getText().toString().equals("") || giris_sifre.getText().toString().equals(""))
                {
                    SweetAlertDialog sa = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
                    sa.setTitleText("Dikkat!");
                    sa.setContentText("Bilgilerinizi eksiksiz ve doğru giriniz");
                    sa.setConfirmText("Tekrar Dene");
                    sa.show();
                }
                else
                {
                    webservis_kullanicigiris();
                }
            }
        });

        sifremi_unutttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), PasswordRecovery.class);
                startActivityForResult(i, 99);
            }
        });
    }

    private void hesap_acma() {
        Intent hesap_kayıt = new Intent(getApplicationContext(), hesap_acmaActivity.class);
        startActivity(hesap_kayıt);
    }

    private void webservis_kullanicigiris() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Yükleniyor");
        pDialog.setCancelable(false);
        pDialog.show();

       try {
           Call<Kullanicigirissonuc> kontrol = ManagerAll.webyonet().kontrolet(giris_mail.getText().toString(), giris_sifre.getText().toString());
           kontrol.enqueue(new Callback<Kullanicigirissonuc>() {
               @Override
               public void onResponse(Call<Kullanicigirissonuc> call, Response<Kullanicigirissonuc> response) {

                   if (response.body().getKullanicigirissonuc().toString().equals("Giris Basarili")) {

                       Kullanici_id = Integer.parseInt(response.body().getKullaniciid().toString());
                       if (Kullanici_id != 0) {

                           sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);
                           SharedPreferences.Editor editor = sharedPreferences.edit();
                           editor.putInt("uye_id", Integer.parseInt(response.body().getKullaniciid().toString()));
                           editor.putString("email", response.body().getEmail().toString());
                           editor.commit();

                           pDialog.cancel();

                           Intent anaekran = new Intent(getApplicationContext(), ana_sayfa.class);
                           anaekran.putExtra("Kullanici_id", Kullanici_id);
                           startActivity(anaekran);

                           genel_uyarı.setVisibility(View.INVISIBLE);
                       }

                   } else if (response.body().getKullanicigirissonuc().toString().equals("Giris Basarisiz")) {

                       genel_uyarı.setVisibility(View.VISIBLE);

                       pDialog.cancel();

                       SweetAlertDialog sa = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
                       sa.setTitleText("Dikkat!");
                       sa.setContentText("Girdiğiniz e-posta veya şifre yanlış, Lütfen bilgilerinizi kontrol edip tekrar deneyiniz");
                       sa.setConfirmText("Tekrar Dene");
                       sa.show();

                       giris_sifre.setText("");
                   }
               }

               @Override
               public void onFailure(Call<Kullanicigirissonuc> call, Throwable t) {

                   pDialog.cancel();

                   SweetAlertDialog sa = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
                   sa.setTitleText("Dikkat!");
                   sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                   sa.setConfirmText("Tamam");
                   sa.show();
               }
           });
       }catch (Exception e)
       {
           Log.e("TAG", "webservis_kullanicigiris: ",e );
       }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    MainActivity.this.finish();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                return;
            }
        }
    }
}
