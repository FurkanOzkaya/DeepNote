package com.abms.af.projeversion02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abms.af.projeversion02.Adapters.Profilkullanicipaylasimadapter;
import com.abms.af.projeversion02.Models.Profilsayfasikullanicipaylasimlari;
import com.abms.af.projeversion02.RestApi.ManagerAll;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class others_profil_sayfasi extends AppCompatActivity {


    TextView others_profil_adi, others_profil_universite, others_profil_bolum;
    ListView others_profilsayfası_listview;
    Profilkullanicipaylasimadapter profilkullaniciadapter;
    List<Profilsayfasikullanicipaylasimlari> others_kullanici_paylasimlari;
    CircularImageView others_profil_foto;
    ProgressBar others_paylasımlar_progresbar;
    String others_id_kullanici_string, others_paylasim_id_string, others_ad_soyad_string, others_universite_string, others_bolum_string, others_ders_string, others_aciklama_string, others_dosyayolu_string, others_dosyaturu_string, others_profilfoto_string;
    SharedPreferences sharedPreferences;
    String email = "";

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
    }

    void islev_ver() {

        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);
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
            Glide.with(getApplicationContext()).load(R.drawable.flat_ogrenci).into(others_profil_foto);
        } else {
            Glide.with(getApplicationContext()).load(getString(R.string.site_adresi) + others_profilfoto_string).error(R.drawable.flat_ogrenci).into(others_profil_foto);
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
    }
}
