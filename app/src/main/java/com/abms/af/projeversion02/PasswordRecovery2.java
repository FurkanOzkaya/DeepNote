package com.abms.af.projeversion02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.abms.af.projeversion02.Models.AdSoyadProfilfoto;
import com.abms.af.projeversion02.RestApi.ManagerAll;
import com.bumptech.glide.Glide;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordRecovery2 extends AppCompatActivity {

    EditText Kod;
    Button Send2;
    TextView textView, AdSoyad;
    ImageView profil_foto;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery2);

        final SweetAlertDialog pDialog = new SweetAlertDialog(PasswordRecovery2.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Yükleniyor");
        pDialog.setCancelable(false);
        pDialog.show();

        Kod = (EditText) findViewById(R.id.Kod);
        Send2 = (Button) findViewById(R.id.Send2);
        textView = (TextView) findViewById(R.id.textView);
        AdSoyad = (TextView) findViewById(R.id.AdSoyad);
        profil_foto = (ImageView) findViewById(R.id.profil_foto);

        sharedPref = getApplicationContext().getSharedPreferences("sifre",0);
        String SharedEmail = sharedPref.getString("Email", "Kayit Yok");

       try {
           Call<AdSoyadProfilfoto> request = ManagerAll.webyonet().AdSoyadProfilfoto(SharedEmail);
           request.enqueue(new Callback<AdSoyadProfilfoto>() {
               @Override
               public void onResponse(Call<AdSoyadProfilfoto> call, Response<AdSoyadProfilfoto> response) {

                   if (response.isSuccessful())
                   {
                       AdSoyad.setText(response.body().getAd_soyad());


                       if (response.body().getProfil_foto().equals("default"))
                       {
                           Glide.with(getApplicationContext()).load(R.drawable.flat_ogrenci).into(profil_foto);
                       }
                       else
                       {
                           ///////////////////////////////////
                           Glide.with(getApplicationContext()).load(getString(R.string.site_adresi)+response.body().getProfil_foto()).error(R.drawable.flat_ogrenci).into(profil_foto);
                           /////////////////////////////////////
                       }

                       pDialog.cancel();

                   }
                   else
                   {
                       pDialog.cancel();

                       pDialog.cancel();

                       final SweetAlertDialog sa = new SweetAlertDialog(PasswordRecovery2.this,SweetAlertDialog.WARNING_TYPE);
                       sa.setTitleText("Dikkat");
                       sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                       sa.setConfirmText("Tamam");
                       sa.show();
                   }
               }

               @Override
               public void onFailure(Call<AdSoyadProfilfoto> call, Throwable t) {

                   pDialog.cancel();

                   final SweetAlertDialog sa = new SweetAlertDialog(PasswordRecovery2.this,SweetAlertDialog.WARNING_TYPE);
                   sa.setTitleText("Dikkat");
                   sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                   sa.setConfirmText("Tamam");
                   sa.show();

               }
           });
       }catch (Exception e)
       {
           Log.e("TAG", "onCreate: ",e );
       }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sandystreamtr.com/"));
                startActivity(browserIntent);
            }
        });

        Send2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPref = getApplicationContext().getSharedPreferences("sifre",0);
                String SharedKod = sharedPref.getString("Kod", "Kayit Yok");
                String kod = Kod.getText().toString();

                if (SharedKod.equals(kod))
                {
                    Intent intent = new Intent(PasswordRecovery2.this, PasswordRecovery3.class);
                    startActivity(intent);

                }
                else
                {
                    final SweetAlertDialog sa = new SweetAlertDialog(PasswordRecovery2.this,SweetAlertDialog.ERROR_TYPE);
                    sa.setTitleText("Hata");
                    sa.setContentText("Gridiğin kod yanlış,e-posta adresine gönderdiğimiz kodu kontrol ederek tekrar dene");
                    sa.setConfirmText("Tamam");
                    sa.show();
                }
            }
        });

    }
}
