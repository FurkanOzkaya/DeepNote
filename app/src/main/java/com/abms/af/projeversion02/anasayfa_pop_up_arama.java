package com.abms.af.projeversion02;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abms.af.projeversion02.Adapters.Paylasimtumverileradapter;
import com.abms.af.projeversion02.Models.Homesayfasitumpaylasimveritabani;
import com.abms.af.projeversion02.Models.TakipKoduEmailGetir;
import com.abms.af.projeversion02.RestApi.ManagerAll;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class anasayfa_pop_up_arama extends AppCompatActivity {

    private static final String[] OKULLAR = new String[]{
            "okul1","okul2","okul3"
    };

    SharedPreferences sharedPreferences;
    EditText arama_dersadi, arama_kisiadi;
    Button arama_buton, arama_buton_not, arama_buton_kisi, arama_buton2;
    String universite,bolum,dersadi;
    ArrayAdapter universite_adapter, bolum_adapter;
    String[] universite_listesi, bolum_listesi;
    Activity activity;
    List<Homesayfasitumpaylasimveritabani> tum_veriler_liste;
    Paylasimtumverileradapter paylasimtumverileradapter;
    ListView listView_homesayfasi;
    ProgressBar progressBar;
    AutoCompleteTextView arama_universite,arama_bolum;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa_pop_up_arama);

        DisplayMetrics d = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(d);

        int genislik = d.widthPixels;
        int yukseklik = d.heightPixels;

        getWindow().setLayout((int)(genislik),(int)(yukseklik*.6));

        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.gravity = Gravity.TOP;
        p.x = 0;
        p.y= 100;

        getWindow().setAttributes(p);

        tanımla();
        islevver();
    }

    public void tanımla()
    {

        arama_universite=findViewById(R.id.arama_universite);
        arama_bolum=findViewById(R.id.arama_bolum);
        arama_buton=findViewById(R.id.arama_buton);
        arama_dersadi=findViewById(R.id.arama_dersadi);
        listView_homesayfasi=findViewById(R.id.listview_homesayfasi);
        progressBar=findViewById(R.id.anasayfa_progress_bar);
        arama_kisiadi = findViewById(R.id.arama_kisi);
        arama_buton_not = findViewById(R.id.arama_buton_not);
        arama_buton_kisi = findViewById(R.id.arama_buton_kisi);
        arama_buton2 = findViewById(R.id.arama_buton2);


        universite_listesi = getResources().getStringArray(R.array.universite_listesi_arama_için);
        bolum_listesi = getResources().getStringArray(R.array.Bolum_listesi);
    }

    public void islevver()
    {

        arama_buton_not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arama_universite.setVisibility(View.VISIBLE);
                arama_bolum.setVisibility(View.VISIBLE);
                arama_dersadi.setVisibility(View.VISIBLE);
                arama_buton.setVisibility(View.VISIBLE);

                arama_kisiadi.setVisibility(View.GONE);
                arama_buton_not.setVisibility(View.GONE);
                arama_buton_kisi.setVisibility(View.GONE);
                arama_buton2.setVisibility(View.GONE);

            }
        });

        arama_buton_kisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arama_universite.setVisibility(View.GONE);
                arama_bolum.setVisibility(View.GONE);
                arama_dersadi.setVisibility(View.GONE);
                arama_buton.setVisibility(View.GONE);

                arama_kisiadi.setVisibility(View.VISIBLE);
                arama_buton_not.setVisibility(View.GONE);
                arama_buton_kisi.setVisibility(View.GONE);
                arama_buton2.setVisibility(View.VISIBLE);

            }
        });

        arama_buton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String TKod = arama_kisiadi.getText().toString();

                Call<TakipKoduEmailGetir> request = ManagerAll.webyonet().TakipKoduEmailGetir(getString(R.string.jsongüvenlikkod),TKod);
                request.enqueue(new Callback<TakipKoduEmailGetir>() {
                    @Override
                    public void onResponse(Call<TakipKoduEmailGetir> call, Response<TakipKoduEmailGetir> response) {

                        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);
                        int Kullanici_id = sharedPreferences.getInt("uye_id", 0);

                        if (Kullanici_id != response.body().getId_kullanici())
                        {
                            if (response.body().getE_posta() != null)
                            {
                                Intent i = new Intent(anasayfa_pop_up_arama.this, others_profil_sayfasi.class);
                                i.putExtra("id_kullanici", String.valueOf(response.body().getId_kullanici()));
                                i.putExtra("ad_soyad", response.body().getAd_soyad());
                                i.putExtra("universite", response.body().getUniversite());
                                i.putExtra("bolum", response.body().getBolum());
                                i.putExtra("profilfoto", response.body().getProfil_foto());
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                SweetAlertDialog sa = new SweetAlertDialog(anasayfa_pop_up_arama.this, SweetAlertDialog.WARNING_TYPE);
                                sa.setTitleText("Dikkat!");
                                sa.setContentText("Böyle bir kişi mevcut değildir");
                                sa.setConfirmText("Tamam");
                                sa.show();
                            }
                        }
                        else
                        {
                            SweetAlertDialog sa = new SweetAlertDialog(anasayfa_pop_up_arama.this, SweetAlertDialog.WARNING_TYPE);
                            sa.setTitleText("Dikkat!");
                            sa.setContentText("Kullanıcı kodunuzdan farklı bir kullanıcı kodu girmeyi deneyin");
                            sa.setConfirmText("Tamam");
                            sa.show();
                        }


                    }

                    @Override
                    public void onFailure(Call<TakipKoduEmailGetir> call, Throwable t) {

                        SweetAlertDialog sa = new SweetAlertDialog(anasayfa_pop_up_arama.this, SweetAlertDialog.WARNING_TYPE);
                        sa.setTitleText("Dikkat!");
                        sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                        sa.setConfirmText("Tamam");
                        sa.show();

                    }
                });

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.okullar,R.id.okultextitem,universite_listesi);
        arama_universite.setAdapter(adapter);

        ArrayAdapter<String> adapterbolum = new ArrayAdapter<String>(this,R.layout.bolumler,R.id.bolumtextitem,bolum_listesi);
        arama_bolum.setAdapter(adapterbolum);

        arama_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                universite=arama_universite.getText().toString();
                bolum=arama_bolum.getText().toString();
                dersadi=arama_dersadi.getText().toString();

                if (universite.equals(""))
                {
                    universite=getString(R.string.universite_listesi__arama_hepsi);
                }

                if(bolum.matches(""))
                {
                    arama_bolum.setError("Bölüm bilgisi gereklidir");
                    arama_bolum.requestFocus();
                }
                else
                {
                    if (dersadi.equals("")) {
                        dersadi="UyarıBos";
                    }
                    //Toast.makeText(getApplicationContext(), "dersadi"+dersadi, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent();
                    i.putExtra("universite", universite);
                    i.putExtra("bolum", bolum);
                    i.putExtra("dersadi", dersadi);
                    setResult(Activity.RESULT_OK,i);
                    finish();
                }
            }
        });
    }
}
