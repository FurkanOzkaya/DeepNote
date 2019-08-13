package com.abms.af.projeversion02;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abms.af.projeversion02.Adapters.Profilkullanicipaylasimadapter;
import com.abms.af.projeversion02.Models.Profilbilgilerigetir;
import com.abms.af.projeversion02.Models.Profilsayfasikullanicipaylasimlari;
import com.abms.af.projeversion02.RestApi.ManagerAll;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class others_profil_sayfasi extends AppCompatActivity {



    TextView others_profil_adi, others_profil_universite, others_profil_bolum;
    LinearLayout profil_bilgiler_layout;
    ListView others_profilsayfası_listview;
    Profilkullanicipaylasimadapter profilkullaniciadapter;
    List<Profilsayfasikullanicipaylasimlari> others_kullanici_paylasimlari;
    CircularImageView others_profil_foto;
    ProgressBar others_paylasımlar_progresbar;
    String others_id_kullanici_string, others_paylasim_id_string, others_ad_soyad_string, others_universite_string, others_bolum_string, others_ders_string, others_aciklama_string, others_dosyayolu_string, others_dosyaturu_string,others_profilfoto_string;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profil_sayfasi);

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
        profil_bilgiler_layout=findViewById(R.id.others_profil_bilgileri_kısmı);
        others_profil_foto=findViewById(R.id.others_profil_resmi);
        others_paylasımlar_progresbar=findViewById(R.id.others_profil_sayfasi_paylasımlar_progress_bar);
    }


    void islev_ver() {

            others_profil_adi.setText(others_ad_soyad_string);
            others_profil_universite.setText(others_universite_string);
            others_profil_bolum.setText(others_bolum_string);

            if (others_profilfoto_string.equals("default"))
            {
                Picasso.get().load(R.drawable.main_activity_profil).resize(200,200).into(others_profil_foto);
            }
            else
                {
                    ///////////////////////////////////
                    Picasso.get().load(getString(R.string.site_adresi)+others_profilfoto_string).resize(200,200).error(R.drawable.main_activity_profil).into(others_profil_foto);
                    /////////////////////////////////////
                }
        int id=Integer.valueOf(others_id_kullanici_string);
        Call<List<Profilsayfasikullanicipaylasimlari>> otherspaylasim = ManagerAll.webyonet().kullancigönderigetir(id);
        //////////////////////////////// P R O G R E S S   B A R    //////////////////////
        others_paylasımlar_progresbar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ////////////////////////////////////////////////////////////////////////////////////
        otherspaylasim.enqueue(new Callback<List<Profilsayfasikullanicipaylasimlari>>() {
            @Override
            public void onResponse(Call<List<Profilsayfasikullanicipaylasimlari>> call, Response<List<Profilsayfasikullanicipaylasimlari>> response) {
                //Toast.makeText(getActivity().getApplicationContext(), "SONUCLAR GELDİ "+response.body(), Toast.LENGTH_LONG).show();
                if (response.isSuccessful()) {
                    //Toast.makeText(getActivity().getApplicationContext(), "SONUCLAR GELDİ "+response.body(), Toast.LENGTH_LONG).show();

                    others_kullanici_paylasimlari = response.body();
                    profilkullaniciadapter = new Profilkullanicipaylasimadapter(others_kullanici_paylasimlari,getApplicationContext(),others_profil_sayfasi.this);
                    others_profilsayfası_listview.setAdapter(profilkullaniciadapter);

                } else {
                    Toast.makeText(getApplicationContext(), "herhangi bir paylasım bulunmamaktadır", Toast.LENGTH_LONG).show();

                }

                /////////////////////////////////////
                others_paylasımlar_progresbar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                ////////////////////////////////////
            }

            @Override
            public void onFailure(Call<List<Profilsayfasikullanicipaylasimlari>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "HATA OLUSTU " + t.getMessage(), Toast.LENGTH_LONG).show();

                /////////////////////////////////////
                others_paylasımlar_progresbar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                ////////////////////////////////////

            }
        });


    }



}
