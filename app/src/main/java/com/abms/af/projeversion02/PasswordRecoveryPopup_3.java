package com.abms.af.projeversion02;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abms.af.projeversion02.Models.Yenisifrebelirleme;
import com.abms.af.projeversion02.RestApi.ManagerAll;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordRecoveryPopup_3 extends AppCompatActivity {

    SharedPreferences sharedPref;
    EditText Sifre, SifreTekrar;
    Button Send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery_popup_3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DisplayMetrics d = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(d);

        int genislik = d.widthPixels;
        int yukseklik = d.heightPixels;

        getWindow().setLayout((int)(genislik*.8),(int)(yukseklik*.5));

        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.gravity = Gravity.TOP;
        p.x = 0;
        p.y= 0;

        getWindow().setAttributes(p);

        tanımla();
        islevver();

    }

    public void tanımla()
    {
        Sifre = (EditText) findViewById(R.id.Sifre);
        SifreTekrar = (EditText) findViewById(R.id.SifreTekrar);
        Send = (Button) findViewById(R.id.Send);

    }

    public void islevver()
    {

        sharedPref = getApplicationContext().getSharedPreferences("sifre",0);
        final String email = sharedPref.getString("Email", "Kayit Yok");

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sifre = Sifre.getText().toString();
                String sifreTekrar = SifreTekrar.getText().toString();

                if (sifre.equals(sifreTekrar))
                {
                    Call<Yenisifrebelirleme> request = ManagerAll.webyonet().YeniSfireBelirleme(email,sifre);
                    request.enqueue(new Callback<Yenisifrebelirleme>() {
                        @Override
                        public void onResponse(Call<Yenisifrebelirleme> call, Response<Yenisifrebelirleme> response) {

                            if (response.isSuccessful())
                            {
                                if (response.body().getResult().equals("Basarili"))
                                {
                                    Toast.makeText(getApplicationContext(), "Şifreniz Değiştirlmiştir", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Hata ile karşılaşıldı daha sonra tekrar deneyiniz" , Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Hata ile karşılaşıldı daha sonra tekrar deneyiniz" , Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<Yenisifrebelirleme> call, Throwable t) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Girdiğiniz Şifreler Aynı Değil" , Toast.LENGTH_LONG).show();
                }

            }
        });

    }

}
