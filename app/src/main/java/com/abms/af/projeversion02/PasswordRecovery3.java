package com.abms.af.projeversion02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.abms.af.projeversion02.Models.Yenisifrebelirleme;
import com.abms.af.projeversion02.RestApi.ManagerAll;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordRecovery3 extends AppCompatActivity {

    SharedPreferences sharedPref;
    EditText Sifre, SifreTekrar;
    Button Send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tanımla();
        islevver();

    }

    public void tanımla() {
        Sifre = (EditText) findViewById(R.id.Sifre);
        SifreTekrar = (EditText) findViewById(R.id.SifreTekrar);
        Send = (Button) findViewById(R.id.SifreGonder);

    }

    public void islevver() {

        Send.setEnabled(false);
        Send.setBackgroundColor(getResources().getColor(R.color.holo_blue_dark_saydam));

        sharedPref = getApplicationContext().getSharedPreferences("sifre", 0);
        final String email = sharedPref.getString("Email", "Kayit Yok");

        Sifre.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (Sifre.length() > 0 && SifreTekrar.length() > 0) {
                    Send.setEnabled(true);
                    Send.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
                } else {
                    Send.setEnabled(false);
                    Send.setBackgroundColor(getResources().getColor(R.color.holo_blue_dark_saydam));
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        SifreTekrar.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (Sifre.length() > 0 && SifreTekrar.length() > 0) {
                    Send.setEnabled(true);
                    Send.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
                } else {
                    Send.setEnabled(false);
                    Send.setBackgroundColor(getResources().getColor(R.color.holo_blue_dark_saydam));
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
        });

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final SweetAlertDialog pDialog = new SweetAlertDialog(PasswordRecovery3.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Yükleniyor");
                pDialog.setCancelable(false);
                pDialog.show();

                String sifre = Sifre.getText().toString();
                String sifreTekrar = SifreTekrar.getText().toString();

                if (sifre.equals(sifreTekrar)) {
                    try {
                        Call<Yenisifrebelirleme> request = ManagerAll.webyonet().YeniSfireBelirleme(getString(R.string.key_for_protection_create_user), email, sifre);
                        request.enqueue(new Callback<Yenisifrebelirleme>() {
                            @Override
                            public void onResponse(Call<Yenisifrebelirleme> call, Response<Yenisifrebelirleme> response) {

                                if (response.isSuccessful()) {
                                    if (response.body().getResult().equals("Basarili")) {
                                        //Toast.makeText(getApplicationContext(), "Şifreniz Değiştirlmiştir", Toast.LENGTH_LONG).show();

                                        pDialog.cancel();

                                        new SweetAlertDialog(PasswordRecovery3.this, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("Şifre Değiştirildi")
                                                .setContentText("Şifreniz başarıyla değiştirldi, yeni şifrenizle giriş yapabilirsiniz")
                                                .setConfirmText("Tamam")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        //sDialog.dismissWithAnimation();

                                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .show();
                                    } else {
                                        //Toast.makeText(getApplicationContext(), "Hata ile karşılaşıldı daha sonra tekrar deneyiniz" , Toast.LENGTH_LONG).show();

                                        pDialog.cancel();

                                        SweetAlertDialog sa = new SweetAlertDialog(PasswordRecovery3.this, SweetAlertDialog.WARNING_TYPE);
                                        sa.setTitleText("Dikkat!");
                                        sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                                        sa.setConfirmText("Tamam");
                                        sa.show();

                                    }
                                } else {
                                    //Toast.makeText(getApplicationContext(), "Hata ile karşılaşıldı daha sonra tekrar deneyiniz" , Toast.LENGTH_LONG).show();

                                    pDialog.cancel();

                                    SweetAlertDialog sa = new SweetAlertDialog(PasswordRecovery3.this, SweetAlertDialog.WARNING_TYPE);
                                    sa.setTitleText("Dikkat!");
                                    sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                                    sa.setConfirmText("Tamam");
                                    sa.show();
                                }

                            }

                            @Override
                            public void onFailure(Call<Yenisifrebelirleme> call, Throwable t) {
                                pDialog.cancel();

                                SweetAlertDialog sa = new SweetAlertDialog(PasswordRecovery3.this, SweetAlertDialog.WARNING_TYPE);
                                sa.setTitleText("Dikkat!");
                                sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                                sa.setConfirmText("Tamam");
                                sa.show();
                            }
                        });
                    } catch (Exception e) {
                        pDialog.cancel();
                        Log.e("TAG", "onClick: ", e);
                    }
                } else {
                    //Toast.makeText(getApplicationContext(), "Girdiğiniz Şifreler Aynı Değil" , Toast.LENGTH_LONG).show();

                    pDialog.cancel();

                    Sifre.setText("");
                    SifreTekrar.setText("");

                    SweetAlertDialog sa = new SweetAlertDialog(PasswordRecovery3.this, SweetAlertDialog.ERROR_TYPE);
                    sa.setTitleText("Hata!");
                    sa.setContentText("Girdiğiniz şifreler birbiriyle uyuşmuyor, kontrol ederek tekrar deneyiniz");
                    sa.setConfirmText("Tamam");
                    sa.show();
                }

            }
        });

    }

}
