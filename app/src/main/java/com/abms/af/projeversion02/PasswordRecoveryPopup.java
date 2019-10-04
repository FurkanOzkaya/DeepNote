package com.abms.af.projeversion02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abms.af.projeversion02.Models.PHPMailersifregonderme;
import com.abms.af.projeversion02.RestApi.ManagerAll;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordRecoveryPopup extends AppCompatActivity {

    SharedPreferences sharedPref;
    EditText Email;
    Button Send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery_popup);

        DisplayMetrics d = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(d);

        int genislik = d.widthPixels;
        int yukseklik = d.heightPixels;

        getWindow().setLayout((int)(genislik*.8),(int)(yukseklik*.5));

        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.gravity = Gravity.CENTER;
        p.x = 0;
        p.y= 0;

        getWindow().setAttributes(p);

        tanımla();
        islevver();

    }

    public void tanımla()
    {
        Email = (EditText) findViewById(R.id.Email);
        Send = (Button) findViewById(R.id.Send);

    }

    public void islevver()
    {

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = Email.getText().toString();
                //Toast.makeText(getApplicationContext(), email ,Toast.LENGTH_LONG).show();

                final Call<PHPMailersifregonderme> request = ManagerAll.webyonet().PHPMailersifregonderme(getString(R.string.key_for_protection_create_user),email);
                request.enqueue(new Callback<PHPMailersifregonderme>() {
                    @Override
                    public void onResponse(Call<PHPMailersifregonderme> call, Response<PHPMailersifregonderme> response) {

                        if (response.isSuccessful())
                        {

                            Toast.makeText(getApplicationContext(), "Şifre sıfırlama kodu email adresinize gönderilmiştir", Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(), response.body().getKod(), Toast.LENGTH_LONG).show();

                            sharedPref = getApplicationContext().getSharedPreferences("sifre",0);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("Kod", response.body().getKod()); //string değer ekleniyor
                            editor.putString("Email", response.body().getEmail()); //string değer ekleniyor
                            editor.commit(); //Kayıt

                            Intent intent = new Intent(getApplicationContext(), PasswordRecoveryPopup_2.class);
                            startActivity(intent);

                        }

                        else
                            Toast.makeText(getApplicationContext(), "Hata ile Karşılaşıldı, Daha Sonra Tekrar Deneyiniz", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<PHPMailersifregonderme> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Hata ile Karşılaşıldı, Daha Sonra Tekrar Deneyiniz", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }

}
