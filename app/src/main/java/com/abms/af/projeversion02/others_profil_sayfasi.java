package com.abms.af.projeversion02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abms.af.projeversion02.Adapters.Profilkullanicipaylasimadapter;
import com.abms.af.projeversion02.Fragments.home_sayfasi;
import com.abms.af.projeversion02.Models.NotTakipTakipciSayisi;
import com.abms.af.projeversion02.Models.Profilsayfasikullanicipaylasimlari;
import com.abms.af.projeversion02.Models.Takibibırak;
import com.abms.af.projeversion02.Models.TakipDurumu;
import com.abms.af.projeversion02.Models.Takipet;
import com.abms.af.projeversion02.RestApi.ManagerAll;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class others_profil_sayfasi extends AppCompatActivity {


    TextView DeepNoteBaslik,others_profil_adi, others_profil_universite, others_profil_bolum, other_notSayisi, other_takipciSayisi, other_takipSayisi;
    ListView others_profilsayfası_listview;
    Profilkullanicipaylasimadapter profilkullaniciadapter;
    List<Profilsayfasikullanicipaylasimlari> others_kullanici_paylasimlari;
    CircularImageView others_profil_foto;
    ProgressBar others_paylasımlar_progresbar;
    String others_id_kullanici_string, others_paylasim_id_string, others_ad_soyad_string, others_universite_string, others_bolum_string, others_ders_string, others_aciklama_string, others_dosyayolu_string, others_dosyaturu_string, others_profilfoto_string;
    SharedPreferences sharedPreferences;
    Button other_btnTakipet, other_btnTakipbırak;
    String email = "";
    Typeface tf1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profil_sayfasi);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        bilgilerial();
        tanımla();
        islev_ver();
    }

    public void bilgilerial() {

        Bundle bundle = getIntent().getExtras();
        others_id_kullanici_string = bundle.getString("id_kullanici");
        others_paylasim_id_string = bundle.getString("paylasim_id");
        others_ad_soyad_string = bundle.getString("ad_soyad");
        others_universite_string = bundle.getString("universite");
        others_bolum_string = bundle.getString("bolum");
        others_ders_string = bundle.getString("ders");
        others_aciklama_string = bundle.getString("aciklama");
        others_dosyayolu_string = bundle.getString("dosyayolu");
        others_dosyaturu_string = bundle.getString("dosyaturu");
        others_profilfoto_string = bundle.getString("profilfoto");
    }

    void tanımla() {

        others_profil_adi = findViewById(R.id.others_profil_adi);
        others_profil_universite = findViewById(R.id.others_profil_universite);
        others_profil_bolum = findViewById(R.id.others_profil_bolum);
        others_profilsayfası_listview = findViewById(R.id.others_profilsayfası_listview);
        others_profil_foto = findViewById(R.id.others_profil_resmi);
        others_paylasımlar_progresbar = findViewById(R.id.others_profil_sayfasi_paylasımlar_progress_bar);
        other_notSayisi = findViewById(R.id.Notsayisi);
        other_takipciSayisi = findViewById(R.id.TakipciSayisi);
        other_takipSayisi = findViewById(R.id.TakipSayisi);
        other_btnTakipet = findViewById(R.id.BtnTakipEt);
        other_btnTakipbırak = findViewById(R.id.BtnTakipBırak);
        DeepNoteBaslik = findViewById(R.id.DeepNoteBaslik);
    }

    void islev_ver() {

        tf1 = Typeface.createFromAsset(getAssets(),"fonts/DamionRegular.ttf");
        DeepNoteBaslik.setTypeface(tf1);

        final int ınteger_other_kullanici_id = Integer.valueOf(others_id_kullanici_string);
        NotTakipTakipciSayisi(ınteger_other_kullanici_id);

        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);
        final int id_kullanici = sharedPreferences.getInt("uye_id", 0);

        TakipDurumu(id_kullanici,ınteger_other_kullanici_id);

        if (sharedPreferences.getInt("uye_id", 0) != 0) {
            email = sharedPreferences.getString("email", "");

        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().commit();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        others_profil_adi.setText(others_ad_soyad_string);
        others_profil_universite.setText(others_universite_string);
        others_profil_bolum.setText(others_bolum_string);

        if (others_profilfoto_string.equals("default")) {
            Picasso.get().load(R.drawable.flat_ogrenci).resize(200, 200).into(others_profil_foto);
        } else {
            Picasso.get().load(getString(R.string.site_adresi) + others_profilfoto_string).resize(200, 200).error(R.drawable.flat_ogrenci).into(others_profil_foto);
        }
        int id = Integer.valueOf(others_id_kullanici_string);

      try {
          Call<List<Profilsayfasikullanicipaylasimlari>> otherspaylasim = ManagerAll.webyonet().kullancigönderigetir(email, id);

          //////////////////////////////// P R O G R E S S   B A R    //////////////////////
          others_paylasımlar_progresbar.setVisibility(View.VISIBLE);
          getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                  WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
          ////////////////////////////////////////////////////////////////////////////////////

          otherspaylasim.enqueue(new Callback<List<Profilsayfasikullanicipaylasimlari>>() {
              @Override
              public void onResponse(Call<List<Profilsayfasikullanicipaylasimlari>> call, Response<List<Profilsayfasikullanicipaylasimlari>> response) {
                  if (response.isSuccessful()) {

                      others_kullanici_paylasimlari = response.body();
                      profilkullaniciadapter = new Profilkullanicipaylasimadapter(others_kullanici_paylasimlari, getApplicationContext(), others_profil_sayfasi.this);
                      others_profilsayfası_listview.setAdapter(profilkullaniciadapter);

                  } else {
                      Toast.makeText(getApplicationContext(), "Kullanıcıya ait paylaşım bulunmamaktadır", Toast.LENGTH_LONG).show();
                  }

                  /////////////////////////////////////
                  others_paylasımlar_progresbar.setVisibility(View.GONE);
                  getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                  ////////////////////////////////////
              }

              @Override
              public void onFailure(Call<List<Profilsayfasikullanicipaylasimlari>> call, Throwable t) {

                  final SweetAlertDialog sa = new SweetAlertDialog(others_profil_sayfasi.this,SweetAlertDialog.WARNING_TYPE);
                  sa.setTitleText("Dikkat");
                  sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                  sa.setConfirmText("Tamam");
                  sa.show();

                  /////////////////////////////////////
                  others_paylasımlar_progresbar.setVisibility(View.GONE);
                  getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                  ////////////////////////////////////

              }
          });
      }catch (Exception e)
      {
          Log.e("TAG", "islev_ver: ",e );
      }

      other_btnTakipet.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Tabipet(id_kullanici,ınteger_other_kullanici_id);
              NotTakipTakipciSayisi(ınteger_other_kullanici_id);
          }
      });

      other_btnTakipbırak.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              Takibibırak(id_kullanici,ınteger_other_kullanici_id);
              NotTakipTakipciSayisi(ınteger_other_kullanici_id);
          }
      });

    }

    public void NotTakipTakipciSayisi(int id_kullanici)
    {
        Call<NotTakipTakipciSayisi> request = ManagerAll.webyonet().NotTakipTakipciSayisi(id_kullanici);
        request.enqueue(new Callback<NotTakipTakipciSayisi>() {
            @Override
            public void onResponse(Call<NotTakipTakipciSayisi> call, Response<NotTakipTakipciSayisi> response) {

                String NotSayisi = String.valueOf(response.body().getNot());
                String TakipSayisi = String.valueOf(response.body().getTakip());
                String TakipciSayisi = String.valueOf(response.body().getTakipci());

                other_notSayisi.setText(NotSayisi);
                other_takipSayisi.setText(TakipSayisi);
                other_takipciSayisi.setText(TakipciSayisi);

            }

            @Override
            public void onFailure(Call<NotTakipTakipciSayisi> call, Throwable t) {

            }
        });
    }

    public void TakipDurumu(int id_kullanici, int id_other_kullanici)
    {
        Call<TakipDurumu> request = ManagerAll.webyonet().TakipDurumu(id_kullanici,id_other_kullanici);
        request.enqueue(new Callback<TakipDurumu>() {
            @Override
            public void onResponse(Call<TakipDurumu> call, Response<TakipDurumu> response) {

                if (response.body().getResult().equals("True"))
                {
                    other_btnTakipet.setVisibility(View.GONE);
                    other_btnTakipbırak.setVisibility(View.VISIBLE);
                }
                else
                {
                    other_btnTakipet.setVisibility(View.VISIBLE);
                    other_btnTakipbırak.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<TakipDurumu> call, Throwable t) {

            }
        });
    }

    public void Tabipet(int id_kullanici, int id_other_kullanici)
    {
        Call<Takipet> request = ManagerAll.webyonet().Takipet(id_kullanici,id_other_kullanici);
        request.enqueue(new Callback<Takipet>() {
            @Override
            public void onResponse(Call<Takipet> call, Response<Takipet> response) {

                if (response.body().getResult().equals("True"))
                {
                    other_btnTakipet.setVisibility(View.GONE);
                    other_btnTakipbırak.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Takipet> call, Throwable t) {

            }
        });
    }

    public void Takibibırak(int id_kullanici, int id_other_kullanici)
    {
        Call<Takibibırak> request = ManagerAll.webyonet().Takibibırak(id_kullanici,id_other_kullanici);
        request.enqueue(new Callback<Takibibırak>() {
            @Override
            public void onResponse(Call<Takibibırak> call, Response<Takibibırak> response) {

                if (response.body().getResult().equals("True"))
                {
                    other_btnTakipet.setVisibility(View.VISIBLE);
                    other_btnTakipbırak.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Takibibırak> call, Throwable t) {

            }
        });
    }
}
