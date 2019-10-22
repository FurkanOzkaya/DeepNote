package com.abms.af.projeversion02;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abms.af.projeversion02.Models.Kullanicikayitsonuc;
import com.abms.af.projeversion02.RestApi.ManagerAll;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class hesap_acmaActivity extends AppCompatActivity {

    TextView sozlesme_goster;
    EditText ad_soyad, dogum_Tarihi, e_posta, sifre, sifre_dogrulama;
    Button kayıt_ol_butonu;
    CheckBox sozlesme;
    ArrayAdapter universite_adapter, bolum_adapter;
    String[] universite_listesi, bolum_listesi;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    String ad_soyad_text, dogum_tarihi_text, e_posta_text, sifre_text, sifre_dogrulama_text, universite_text, bolum_text;
    AutoCompleteTextView universite, bolum;
    SharedPreferences sharedPreferences;
    DatePickerDialog datePickerDialog;
    Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hesap_acma);
        //STATUS BARRRRR
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.girisStatus));
        }

        tanimla();
        islevver();

    }

    private void tanimla() {
        ad_soyad = (EditText) findViewById(R.id.ad_soyad);
        dogum_Tarihi = (EditText) findViewById(R.id.dogum_tarihi);
        universite = findViewById(R.id.universite);
        bolum = findViewById(R.id.bolum);
        e_posta = (EditText) findViewById(R.id.e_posta);
        sifre = (EditText) findViewById(R.id.giris_sifre);
        sifre_dogrulama = (EditText) findViewById(R.id.sifre_dogrulama);
        kayıt_ol_butonu = (Button) findViewById(R.id.kayıt_ol_buton);
        sozlesme = (CheckBox) findViewById(R.id.sozlesme);
        sozlesme_goster = (TextView) findViewById(R.id.sozlesme_goster);

        universite_listesi = getResources().getStringArray(R.array.universite_listesi);
        bolum_listesi = getResources().getStringArray(R.array.Bolum_listesi);


        ArrayAdapter<String> a = new ArrayAdapter<String>(this, R.layout.okullar, R.id.okultextitem, universite_listesi);
        universite.setAdapter(a);

        ArrayAdapter<String> a2 = new ArrayAdapter<String>(this, R.layout.bolumler, R.id.bolumtextitem, bolum_listesi);
        bolum.setAdapter(a2);
    }

    private void islevver() {

        sozlesme_goster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Intent i = new Intent(getApplicationContext(), SozlesmePopUp.class);
            startActivity(i);

            }
        });

        kayıt_ol_butonu.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {

                                                   ad_soyad_text = ad_soyad.getText().toString();
                                                   dogum_tarihi_text = dogum_Tarihi.getText().toString();
                                                   e_posta_text = e_posta.getText().toString();
                                                   sifre_text = sifre.getText().toString();
                                                   sifre_dogrulama_text = sifre_dogrulama.getText().toString();
                                                   universite_text = universite.getText().toString();
                                                   bolum_text = bolum.getText().toString();

                                                   if (ad_soyad_text.equals("") || dogum_tarihi_text.equals("") || universite_text.matches("") || universite_text.equals(getString(R.string.universite_listesi__arama_hepsi)) || bolum_text.matches("") || e_posta_text.equals("") || sifre_text.equals("") || sifre_dogrulama_text.equals("")) {
                                                       if (ad_soyad_text.equals("")) {
                                                           //ad_soyad_uyarı.setVisibility(View.VISIBLE);
                                                           //buton_altı_bilgilendirme.setVisibility(View.VISIBLE);
                                                           ad_soyad.setError("Ad ve Soyad bilgisi gereklidir");
                                                       } else {
                                                           //ad_soyad_uyarı.setVisibility(View.INVISIBLE);
                                                       }
                                                       if (dogum_tarihi_text.equals("")) {
                                                           //dogum_tarihi_uyarı.setVisibility(View.VISIBLE);
                                                           //buton_altı_bilgilendirme.setVisibility(View.VISIBLE);
                                                           dogum_Tarihi.setError("Doğum Tarihi bilgisi gereklidir");
                                                       } else {
                                                           //dogum_tarihi_uyarı.setVisibility(View.INVISIBLE);
                                                       }
                                                       if (universite_text.matches("")) {
                                                           //universite_uyarı.setVisibility(View.VISIBLE);
                                                           //buton_altı_bilgilendirme.setVisibility(View.VISIBLE);
                                                           universite.setError("Üniversite bilgisi gereklidir");
                                                       } else {
                                                           //universite_uyarı.setVisibility(View.INVISIBLE);
                                                       }
                                                       if (bolum_text.matches("")) {
                                                           //bolum_uyarı.setVisibility(View.VISIBLE);
                                                           //buton_altı_bilgilendirme.setVisibility(View.VISIBLE);
                                                           bolum.setError("Bölüm bilgisi gereklidir");
                                                       } else {
                                                           //bolum_uyarı.setVisibility(View.INVISIBLE);
                                                       }
                                                       if (e_posta_text.equals("")) {
                                                           //e_posta_uyarı.setVisibility(View.VISIBLE);
                                                           //buton_altı_bilgilendirme.setVisibility(View.VISIBLE);
                                                           e_posta.setError("E-posta bilgisi gereklidir");
                                                       } else {
                                                           //e_posta_uyarı.setVisibility(View.INVISIBLE);
                                                       }
                                                       if (sifre_text.equals("")) {
                                                           //sifre_uyarı.setVisibility(View.VISIBLE);
                                                           //buton_altı_bilgilendirme.setVisibility(View.VISIBLE);
                                                           sifre.setError("Şifre bilgisi gereklidir");
                                                       } else {
                                                           //sifre_uyarı.setVisibility(View.INVISIBLE);
                                                       }
                                                       if (sifre_dogrulama_text.equals("")) {
                                                           //sifre_dogrulama_uyarı.setVisibility(View.VISIBLE);
                                                           //buton_altı_bilgilendirme.setVisibility(View.VISIBLE);
                                                           sifre_dogrulama.setError("Şifre doğrulama bilgisi gereklidir");
                                                       } else {
                                                           //sifre_dogrulama_uyarı.setVisibility(View.INVISIBLE);
                                                       }
                                                   } else {
                                                       /*
                                                       ad_soyad_uyarı.setVisibility(View.INVISIBLE);
                                                       dogum_tarihi_uyarı.setVisibility(View.INVISIBLE);
                                                       universite_uyarı.setVisibility(View.INVISIBLE);
                                                       bolum_uyarı.setVisibility(View.INVISIBLE);
                                                       e_posta_uyarı.setVisibility(View.INVISIBLE);
                                                       sifre_uyarı.setVisibility(View.INVISIBLE);
                                                       sifre_dogrulama_uyarı.setVisibility(View.INVISIBLE);
                                                       buton_altı_bilgilendirme.setVisibility(View.INVISIBLE);
                                                       */
                                                       if (sifre_text.equals(sifre_dogrulama_text)) {
                                                           //Toast.makeText(getApplicationContext(),"sifreler dogrulandı",Toast.LENGTH_SHORT).show();
                                                           webservis_kullanicikayıt();

                                                       } else {
                                                           // Toast.makeText(getApplicationContext(),"sifreler yanlıs",Toast.LENGTH_SHORT).show();
                                                           //String uyarı = getResources().getString(R.string.hesap_acma_sayfası_Sifre_dogrulama_kısmı_uyarı);
                                                           //sifre_dogrulama_uyarı.setText(uyarı);
                                                           //sifre_dogrulama_uyarı.setVisibility(View.VISIBLE);
                                                           sifre_dogrulama.setError("Girdiğiniz şifreler aynı değil");
                                                       }
                                                   }
                                               }
                                           }
        );

        sozlesme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sozlesme.isChecked()) {
                    kayıt_ol_butonu.setEnabled(true);
                } else {
                    kayıt_ol_butonu.setEnabled(false);
                }
            }
        });

        dogum_Tarihi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(hesap_acmaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int yil, int ay, int gun) {

                        dogum_Tarihi.setText(gun + "/" + (ay + 1) + "/" + yil);

                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }


    /*
     *Web servis baglanmak için oluşturulan bölüm
     */
    public void webservis_kullanicikayıt() {
        try {

            Call<Kullanicikayitsonuc> a = ManagerAll.webyonet().kullaniciekle(getString(R.string.key_for_protection_create_user), ad_soyad_text, dogum_tarihi_text, universite_text, bolum_text, e_posta_text, sifre_text);
            a.enqueue(new Callback<Kullanicikayitsonuc>() {
                @Override
                public void onResponse(Call<Kullanicikayitsonuc> call, Response<Kullanicikayitsonuc> response) {

                    Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_LONG).show();

                    if (response.body().getKullanicikayitsonuc().toString().equals("E_posta kullaniliyor")) {
                        //e_posta_uyarı.setVisibility(View.VISIBLE);
                        //e_posta_uyarı.setText(R.string.hesap_acma_sayfası_e_posta_kısmı_aynı_olması_durumu);
                        //buton_altı_bilgilendirme.setVisibility(View.VISIBLE);
                        e_posta.setError("E-posta kullanılıyor");
                    } else if (response.body().getKullanicikayitsonuc().toString().equals("Ekleme basarilidir")) {
                        //Toast.makeText(getApplicationContext(),"kayıt oldunuz",Toast.LENGTH_LONG).show();
                        sharedPreferences = getApplicationContext().getSharedPreferences("protection", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", response.body().getToken().toString());
                        editor.commit();
                        Intent main = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(main);
                    }
                }

                @Override
                public void onFailure(Call<Kullanicikayitsonuc> call, Throwable t) {

                    final SweetAlertDialog sa = new SweetAlertDialog(hesap_acmaActivity.this, SweetAlertDialog.WARNING_TYPE);
                    sa.setTitleText("Dikkat");
                    sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                    sa.setConfirmText("Tamam");
                    sa.show();
                }
            });
        } catch (Exception e) {
            Log.e("TAG", "webservis_kullanicikayıt: ", e);
        }
    }
}
