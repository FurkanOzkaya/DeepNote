package com.abms.af.projeversion02;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.abms.af.projeversion02.Models.NotTakipTakipciSayisi;
import com.abms.af.projeversion02.Models.Takibibırak;
import com.abms.af.projeversion02.Models.TakipDurumu;
import com.abms.af.projeversion02.Models.Takipet;
import com.abms.af.projeversion02.RestApi.ManagerAll;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KullaniciArama extends AppCompatActivity {

    Button other_btnTakipet, other_btnTakipbırak;
    ImageView kullaniciArama_profil_resmi;
    TextView kullaniciArama_profil_adi, kullaniciArama_profil_universite, kullaniciArama_profil_bolum, KullaniciAramaNotsayisi, KullaniciAramaProfilTakipciSayisi, KullaniciAramaProfilTakipSayisi, DeepNoteBaslik;
    String ad_soyad = "";
    int other_id_kullanici = 0;
    SharedPreferences sharedPreferences;
    Typeface tf1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_arama);

        kullaniciArama_profil_resmi = findViewById(R.id.kullaniciArama_profil_resmi);
        kullaniciArama_profil_adi = findViewById(R.id.kullaniciArama_profil_adi);
        kullaniciArama_profil_bolum = findViewById(R.id.kullaniciArama_profil_bolum);
        KullaniciAramaNotsayisi = findViewById(R.id.KullaniciAramaNotsayisi);
        KullaniciAramaProfilTakipciSayisi = findViewById(R.id.KullaniciAramaProfilTakipciSayisi);
        KullaniciAramaProfilTakipSayisi = findViewById(R.id.KullaniciAramaProfilTakipSayisi);
        kullaniciArama_profil_universite = findViewById(R.id.kullaniciArama_profil_universite);
        other_btnTakipet = findViewById(R.id.KullaniciAramaBtnTakipEt);
        other_btnTakipbırak = findViewById(R.id.KullaniciAramaBtnTakipBırak);
        DeepNoteBaslik = findViewById(R.id.DeepNoteBaslik);

        tf1 = Typeface.createFromAsset(getAssets(),"fonts/DamionRegular.ttf");
        DeepNoteBaslik.setTypeface(tf1);

        Bundle bundle = getIntent().getExtras();
        other_id_kullanici = bundle.getInt("ka_id_kullanici");
        ad_soyad = bundle.getString("ka_ad_soyad");
        String universite = bundle.getString("ka_universite");
        String bolum = bundle.getString("ka_bolum");
        String profilfoto = bundle.getString("ka_profilfoto");

        kullaniciArama_profil_adi.setText(ad_soyad);
        kullaniciArama_profil_universite.setText(universite);
        kullaniciArama_profil_bolum.setText(bolum);

        if (profilfoto.equals("default")) {
            Picasso.get().load(R.drawable.flat_ogrenci).resize(200, 200).into(kullaniciArama_profil_resmi);
        } else {
            try {
                ///////////////////////////////////
                Picasso.get().load(getString(R.string.site_adresi) + profilfoto).resize(200, 200).error(R.drawable.flat_ogrenci).into(kullaniciArama_profil_resmi);
                /////////////////////////////////////
            } catch (Exception e) {
                Log.e("TAG", "Profilepicasso: ", e);
            }
        }

        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);
        int Kullanici_id = sharedPreferences.getInt("uye_id", 0);

        NotTakipTakipciSayisi(other_id_kullanici);
        TakipDurumu(Kullanici_id, other_id_kullanici);



        other_btnTakipet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Tabipet(Kullanici_id, other_id_kullanici);

            }
        });

        other_btnTakipbırak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Takibibırak(Kullanici_id, other_id_kullanici);

            }
        });

    }


    public void NotTakipTakipciSayisi(int id_kullanici) {
        Call<NotTakipTakipciSayisi> request = ManagerAll.webyonet().NotTakipTakipciSayisi(id_kullanici);
        request.enqueue(new Callback<NotTakipTakipciSayisi>() {
            @Override
            public void onResponse(Call<NotTakipTakipciSayisi> call, Response<NotTakipTakipciSayisi> response) {

                String NotSayisi = String.valueOf(response.body().getNot());
                String TakipSayisi = String.valueOf(response.body().getTakip());
                String TakipciSayisi = String.valueOf(response.body().getTakipci());

                KullaniciAramaNotsayisi.setText(NotSayisi);
                KullaniciAramaProfilTakipSayisi.setText(TakipSayisi);
                KullaniciAramaProfilTakipciSayisi.setText(TakipciSayisi);

            }

            @Override
            public void onFailure(Call<NotTakipTakipciSayisi> call, Throwable t) {
                SweetAlertDialog sa = new SweetAlertDialog(KullaniciArama.this, SweetAlertDialog.WARNING_TYPE);
                sa.setTitleText("Dikkat!");
                sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                sa.setConfirmText("Tamam");
                sa.show();
            }
        });
    }


    public void TakipDurumu(int id_kullanici, int id_other_kullanici) {
        Call<TakipDurumu> request = ManagerAll.webyonet().TakipDurumu(id_kullanici, id_other_kullanici);
        request.enqueue(new Callback<TakipDurumu>() {
            @Override
            public void onResponse(Call<TakipDurumu> call, Response<TakipDurumu> response) {

                if (response.body().getResult().equals("True")) {
                    other_btnTakipet.setVisibility(View.GONE);
                    other_btnTakipbırak.setVisibility(View.VISIBLE);
                } else {
                    other_btnTakipet.setVisibility(View.VISIBLE);
                    other_btnTakipbırak.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<TakipDurumu> call, Throwable t) {
                SweetAlertDialog sa = new SweetAlertDialog(KullaniciArama.this, SweetAlertDialog.WARNING_TYPE);
                sa.setTitleText("Dikkat!");
                sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                sa.setConfirmText("Tamam");
                sa.show();
            }
        });
    }

    public void Tabipet(int id_kullanici, int id_other_kullanici) {
        Call<Takipet> request = ManagerAll.webyonet().Takipet(id_kullanici, id_other_kullanici);
        request.enqueue(new Callback<Takipet>() {
            @Override
            public void onResponse(Call<Takipet> call, Response<Takipet> response) {

                if (response.body().getResult().equals("True")) {
                    other_btnTakipet.setVisibility(View.GONE);
                    other_btnTakipbırak.setVisibility(View.VISIBLE);
                    int Takipci = Integer.valueOf((String) KullaniciAramaProfilTakipciSayisi.getText()) + 1;
                    KullaniciAramaProfilTakipciSayisi.setText(String.valueOf(Takipci));
                }
            }

            @Override
            public void onFailure(Call<Takipet> call, Throwable t) {
                SweetAlertDialog sa = new SweetAlertDialog(KullaniciArama.this, SweetAlertDialog.WARNING_TYPE);
                sa.setTitleText("Dikkat!");
                sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                sa.setConfirmText("Tamam");
                sa.show();
            }
        });
    }

    public void Takibibırak(int id_kullanici, int id_other_kullanici) {

        SweetAlertDialog sa = new SweetAlertDialog(KullaniciArama.this, SweetAlertDialog.NORMAL_TYPE);
        sa.setTitleText("Dikkat!");
        sa.setContentText(ad_soyad + " adlı kullanıcıyı takipten çıkmak istediğinize emin misiniz?");
        sa.setConfirmText("Evet");
        sa.setCancelClickListener(null);
        sa.setCancelText("Hayır");
        sa.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                Call<Takibibırak> request = ManagerAll.webyonet().Takibibırak(id_kullanici, id_other_kullanici);
                request.enqueue(new Callback<Takibibırak>() {
                    @Override
                    public void onResponse(Call<Takibibırak> call, Response<Takibibırak> response) {

                        if (response.body().getResult().equals("True")) {
                            other_btnTakipet.setVisibility(View.VISIBLE);
                            other_btnTakipbırak.setVisibility(View.GONE);
                            sa.cancel();
                            int Takipci = Integer.valueOf((String) KullaniciAramaProfilTakipciSayisi.getText()) - 1;
                            KullaniciAramaProfilTakipciSayisi.setText(String.valueOf(Takipci));
                        }

                    }

                    @Override
                    public void onFailure(Call<Takibibırak> call, Throwable t) {
                        sa.cancel();
                        SweetAlertDialog sb = new SweetAlertDialog(KullaniciArama.this, SweetAlertDialog.WARNING_TYPE);
                        sb.setTitleText("Dikkat!");
                        sb.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                        sb.setConfirmText("Tamam");
                        sb.show();
                    }
                });

            }
        });
        sa.show();
    }

}
