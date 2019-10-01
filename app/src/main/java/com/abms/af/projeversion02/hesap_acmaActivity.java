package com.abms.af.projeversion02;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
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

    TextView ad_soyad_uyarı, universite_uyarı, bolum_uyarı, dogum_tarihi_uyarı, e_posta_uyarı, sifre_uyarı, sifre_dogrulama_uyarı, buton_altı_bilgilendirme;
    EditText ad_soyad, dogum_Tarihi, e_posta, sifre, sifre_dogrulama;
    Button kayıt_ol_butonu;
    CheckBox sozlesme;
    Spinner universite, bolum;
    ArrayAdapter universite_adapter, bolum_adapter;
    String[] universite_listesi, bolum_listesi;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    String ad_soyad_text,dogum_tarihi_text,e_posta_text,sifre_text,sifre_dogrulama_text,universite_text,bolum_text;

    DatePickerDialog datePickerDialog;
    Calendar calendar;

    int universite_int,bolum_int;


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
        //STATUS BARRRRRR

        tanimla();
        islevver();

    }


    private void tanimla() {
        ad_soyad = (EditText) findViewById(R.id.ad_soyad);
        dogum_Tarihi = (EditText) findViewById(R.id.dogum_tarihi);
        universite = (Spinner) findViewById(R.id.universite);
        bolum = (Spinner) findViewById(R.id.bolum);
        e_posta = (EditText) findViewById(R.id.e_posta);
        sifre = (EditText) findViewById(R.id.giris_sifre);
        sifre_dogrulama = (EditText) findViewById(R.id.sifre_dogrulama);
        kayıt_ol_butonu = (Button) findViewById(R.id.kayıt_ol_buton);
        ad_soyad_uyarı = (TextView) findViewById(R.id.ad_soyad_uyarı);
        universite_uyarı = (TextView) findViewById(R.id.universite_uyarı);
        bolum_uyarı = (TextView) findViewById(R.id.bolum_uyarı);
        dogum_tarihi_uyarı = (TextView) findViewById(R.id.dogum_tarihi_uyarı);
        e_posta_uyarı = (TextView) findViewById(R.id.e_posta_uyarı);
        sifre_uyarı = (TextView) findViewById(R.id.giris_sifre_uyarı);
        sifre_dogrulama_uyarı = (TextView) findViewById(R.id.sifre_dogrulama_uyarı);
        sozlesme = (CheckBox) findViewById(R.id.sozlesme);
        buton_altı_bilgilendirme = (TextView) findViewById(R.id.buton_altı_bilgilendirme);


        // Spinnerlara eleman ekleme
        universite_listesi = getResources().getStringArray(R.array.universite_listesi);
        universite_adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, universite_listesi);
        universite.setAdapter(universite_adapter);

        bolum_listesi = getResources().getStringArray(R.array.Bolum_listesi);
        bolum_adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, bolum_listesi);
        bolum.setAdapter(bolum_adapter);
        //Spinnerlara eleman ekleme sonu


    }

    private void islevver() {

        kayıt_ol_butonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getApplicationContext(),"butona tıklandı",Toast.LENGTH_LONG).show();
                boolean sifre_uyusması;


                ad_soyad_text=ad_soyad.getText().toString();
                dogum_tarihi_text=dogum_Tarihi.getText().toString();
                e_posta_text=e_posta.getText().toString();
                sifre_text=sifre.getText().toString();
                sifre_dogrulama_text=sifre_dogrulama.getText().toString();
                universite_int=universite.getSelectedItemPosition();
                bolum_int=bolum.getSelectedItemPosition();
                universite_text=universite.getSelectedItem().toString();
                bolum_text= bolum.getSelectedItem().toString();


                if (ad_soyad_text.equals("") || dogum_tarihi_text.equals("") || universite_int == 0 ||bolum_int == 0 || e_posta_text.equals("") || sifre_text.equals("") || sifre_dogrulama_text.equals("") )
                {
                    if (ad_soyad_text.equals("")) {
                        ad_soyad_uyarı.setVisibility(View.VISIBLE);
                        buton_altı_bilgilendirme.setVisibility(View.VISIBLE);
                    } else {
                        ad_soyad_uyarı.setVisibility(View.INVISIBLE);
                    }
                    if (dogum_tarihi_text.equals("")) {
                        dogum_tarihi_uyarı.setVisibility(View.VISIBLE);
                        buton_altı_bilgilendirme.setVisibility(View.VISIBLE);
                    } else {
                        dogum_tarihi_uyarı.setVisibility(View.INVISIBLE);
                    }
                    if (universite_int == 0) {
                        universite_uyarı.setVisibility(View.VISIBLE);
                        buton_altı_bilgilendirme.setVisibility(View.VISIBLE);
                    } else {
                        universite_uyarı.setVisibility(View.INVISIBLE);
                    }
                    if (bolum_int == 0) {
                        bolum_uyarı.setVisibility(View.VISIBLE);
                        buton_altı_bilgilendirme.setVisibility(View.VISIBLE);
                    } else {
                        bolum_uyarı.setVisibility(View.INVISIBLE);
                    }
                    if (e_posta_text.equals("")) {
                        e_posta_uyarı.setVisibility(View.VISIBLE);
                        buton_altı_bilgilendirme.setVisibility(View.VISIBLE);
                    } else {
                        e_posta_uyarı.setVisibility(View.INVISIBLE);
                    }
                    if (sifre_text.equals("")) {
                        sifre_uyarı.setVisibility(View.VISIBLE);
                        buton_altı_bilgilendirme.setVisibility(View.VISIBLE);
                    } else {
                        sifre_uyarı.setVisibility(View.INVISIBLE);
                    }
                    if (sifre_dogrulama_text.equals("")) {
                        sifre_dogrulama_uyarı.setVisibility(View.VISIBLE);
                        buton_altı_bilgilendirme.setVisibility(View.VISIBLE);
                    } else {
                        sifre_dogrulama_uyarı.setVisibility(View.INVISIBLE);
                    }
                }
                else
                {

                    ad_soyad_uyarı.setVisibility(View.INVISIBLE);
                    dogum_tarihi_uyarı.setVisibility(View.INVISIBLE);
                    universite_uyarı.setVisibility(View.INVISIBLE);
                    bolum_uyarı.setVisibility(View.INVISIBLE);
                    e_posta_uyarı.setVisibility(View.INVISIBLE);
                    sifre_uyarı.setVisibility(View.INVISIBLE);
                    sifre_dogrulama_uyarı.setVisibility(View.INVISIBLE);
                    buton_altı_bilgilendirme.setVisibility(View.INVISIBLE);
                        if (sifre_text.equals(sifre_dogrulama_text)) {
                            // Toast.makeText(getApplicationContext(),"sifreler dogrulandı",Toast.LENGTH_SHORT).show();
                            sifre_uyusması = true;
                            webservis_kullanicikayıt();

                        } else {
                            sifre_uyusması = false;
                            // Toast.makeText(getApplicationContext(),"sifreler yanlıs",Toast.LENGTH_SHORT).show();
                            String uyarı=getResources().getString(R.string.hesap_acma_sayfası_Sifre_dogrulama_kısmı_uyarı);
                            sifre_dogrulama_uyarı.setText(uyarı);
                            sifre_dogrulama_uyarı.setVisibility(View.VISIBLE);
                        }
                }
            }}
        );

        sozlesme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sozlesme.isChecked()) {
                    kayıt_ol_butonu.setEnabled(true);
                    //Toast.makeText(getApplicationContext(), "sözlesme işaretli", Toast.LENGTH_SHORT).show();
                } else {
                    kayıt_ol_butonu.setEnabled(false);
                    //Toast.makeText(getApplicationContext(), "sözleşme iptal", Toast.LENGTH_SHORT).show();
                }
            }
        });




        dogum_Tarihi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        hesap_acmaActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener,year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();*/
                calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(hesap_acmaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int yil, int ay, int gun) {

                        dogum_Tarihi.setText(gun + "/" + (ay+1) + "/" + yil);

                    }
                },year,month,day);
                datePickerDialog.show();

            }
        });
    }





    /*
    *Web servis baglanmak için oluşturulan bölüm
    */
    public void webservis_kullanicikayıt()
    {
       // Log.i("nkjn","istek:"+ ad_soyad.getText().toString()+dogum_Tarihi.getText().toString()+universite.getSelectedItem().toString()+bolum.getSelectedItem().toString()+e_posta.getText().toString()+sifre.getText().toString());

        Call<Kullanicikayitsonuc> a= ManagerAll.webyonet().kullaniciekle(ad_soyad_text,dogum_tarihi_text,universite_text,bolum_text,e_posta_text,sifre_text);
        a.enqueue(new Callback<Kullanicikayitsonuc>() {
            @Override
            public void onResponse(Call<Kullanicikayitsonuc> call, Response<Kullanicikayitsonuc> response) {
                //Toast.makeText(getApplicationContext(),response.body().getKullanicikayitsonuc(),Toast.LENGTH_LONG).show();

                if (response.body().getKullanicikayitsonuc().toString().equals("E_posta kullaniliyor"))
                {
                    e_posta_uyarı.setVisibility(View.VISIBLE);
                    e_posta_uyarı.setText(R.string.hesap_acma_sayfası_e_posta_kısmı_aynı_olması_durumu);
                    buton_altı_bilgilendirme.setVisibility(View.VISIBLE);
                }

                else if (response.body().getKullanicikayitsonuc().toString().equals("Ekleme basarilidir"))
                {
                    //Toast.makeText(getApplicationContext(),"kayıt oldunuz",Toast.LENGTH_LONG).show();

                    new SweetAlertDialog(hesap_acmaActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Tebrikler")
                            .setContentText("Kayıt işleminiz gerçekleşmiştir")
                            .show();

                    Intent main=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(main);
                }

            }

            @Override
            public void onFailure(Call<Kullanicikayitsonuc> call, Throwable t) {
                //Log.i("HATA VERDİİİİ", "onFailure:"+t.getMessage());
                //Toast.makeText(getApplicationContext(),"kayıt olurken hata"+t.getMessage() ,Toast.LENGTH_LONG).show();

                new SweetAlertDialog(hesap_acmaActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Dikkat!")
                        .setContentText("Beklenmedik bir hata oluştu, Lütfen daha sonra tekrar deneyiniz")
                        .show();

            }
        });
    }
}
