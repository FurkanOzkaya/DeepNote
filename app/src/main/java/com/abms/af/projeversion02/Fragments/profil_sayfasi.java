package com.abms.af.projeversion02.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abms.af.projeversion02.Adapters.Profilkullanicipaylasimadapter;
import com.abms.af.projeversion02.MainActivity;
import com.abms.af.projeversion02.Models.Profilbilgilerigetir;
import com.abms.af.projeversion02.Models.Profilsayfasikullanicipaylasimlari;
import com.abms.af.projeversion02.R;
import com.abms.af.projeversion02.RestApi.ManagerAll;
import com.abms.af.projeversion02.profil_resmi_pop_up;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class profil_sayfasi extends Fragment {

    TextView profil_adi, profil_universite, profil_bolum;
    int id;
    View view;
    LinearLayout profil_bilgiler_layout;
    ListView listview_profil;
    Profilkullanicipaylasimadapter profilkullaniciadapter;
    List<Profilsayfasikullanicipaylasimlari> kullanici_paylasimlari;
    SharedPreferences sharedPreferences;
    String profil_ad_soyad_gelen, profil_bolum_gelen, profil_universite_gelen,Profil_foto_gelen;
    ImageView profil_foto;
    ProgressBar paylasımlar_progresbar,bilgiler_progress_bar;
    ImageView ayarlarbutonu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profil_sayfasi, container, false);

        Window window = this.getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        tanımla();
        islev_ver();
        return view;
    }



    void tanımla() {
        profil_adi = view.findViewById(R.id.profil_adi);
        profil_universite = view.findViewById(R.id.profil_universite);
        profil_bolum = view.findViewById(R.id.profil_bolum);
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("giris", 0);
        id = sharedPreferences.getInt("uye_id", 0);
        //Toast.makeText(getActivity().getApplicationContext(), "id = "+id, Toast.LENGTH_SHORT).show();
        listview_profil = view.findViewById(R.id.profilsayfası_listview);
        profil_bilgiler_layout=view.findViewById(R.id.profil_bilgileri_kısmı);
        profil_foto=view.findViewById(R.id.profil_resmi);
        paylasımlar_progresbar=view.findViewById(R.id.profil_sayfasi_paylasımlar_progress_bar);
        bilgiler_progress_bar=view.findViewById(R.id.profil_sayfasi_bilgiler_progress_bar);
        ayarlarbutonu=view.findViewById(R.id.profil_sayfası_ayarlar_butonu);
    }


    void islev_ver() {


        Call<Profilbilgilerigetir> a = ManagerAll.webyonet().profilgetir(id);
        //////////////////////////////// P R O G R E S S   B A R    //////////////////////
        bilgiler_progress_bar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ////////////////////////////////////////////////////////////////////////////////////
        a.enqueue(new Callback<Profilbilgilerigetir>() {
            @Override
            public void onResponse(Call<Profilbilgilerigetir> call, Response<Profilbilgilerigetir> response) {
                if (response.body().getSonuc() == 1) {
                    profil_ad_soyad_gelen = response.body().getAd_soyad().toString();
                    profil_bolum_gelen = response.body().getBolum().toString();
                    profil_universite_gelen = response.body().getUniversite().toString();
                    Profil_foto_gelen=response.body().getProfil_foto();

                    /////////////////////////////////////
                    bilgiler_progress_bar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    ///////////////////////////   P R O G R E S S   B A R   /////////


                    profil_adi.setText(profil_ad_soyad_gelen);
                    profil_universite.setText(profil_universite_gelen);
                    profil_bolum.setText(profil_bolum_gelen);

                    if (response.body().getProfil_foto().equals("default"))
                    {
                        Picasso.get().load(R.drawable.flat_ogrenci).resize(200,200).into(profil_foto);
                    }
                    else
                    {
                        ///////////////////////////////////
                        Picasso.get().load(getString(R.string.site_adresi)+response.body().getProfil_foto()).resize(3000,3000).error(R.drawable.main_activity_profil).into(profil_foto);
                        /////////////////////////////////////
                    }



                }

            }

            @Override
            public void onFailure(Call<Profilbilgilerigetir> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "bilgiler gelirken  hata" + t.getMessage(), Toast.LENGTH_LONG).show();


                /////////////////////////////////////
                bilgiler_progress_bar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                ///////////////////////////   P R O G R E S S   B A R   /////////
            }
        });


        Call<List<Profilsayfasikullanicipaylasimlari>> kullanicipaylasım = ManagerAll.webyonet().kullancigönderigetir(id);
        //////////////////////////////// P R O G R E S S   B A R    //////////////////////
        paylasımlar_progresbar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ////////////////////////////////////////////////////////////////////////////////////
        kullanicipaylasım.enqueue(new Callback<List<Profilsayfasikullanicipaylasimlari>>() {
            @Override
            public void onResponse(Call<List<Profilsayfasikullanicipaylasimlari>> call, Response<List<Profilsayfasikullanicipaylasimlari>> response) {
                //Toast.makeText(getActivity().getApplicationContext(), "SONUCLAR GELDİ "+response.body(), Toast.LENGTH_LONG).show();
                if (response.isSuccessful()) {
                    //Toast.makeText(getActivity().getApplicationContext(), "SONUCLAR GELDİ "+response.body(), Toast.LENGTH_LONG).show();

                    kullanici_paylasimlari = response.body();
                    profilkullaniciadapter = new Profilkullanicipaylasimadapter(kullanici_paylasimlari, getActivity().getApplicationContext(),getActivity());
                    listview_profil.setAdapter(profilkullaniciadapter);

                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "herhangi bir paylasımınız bulunmamaktadır", Toast.LENGTH_LONG).show();

                }

                /////////////////////////////////////
                paylasımlar_progresbar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                ////////////////////////////////////
            }

            @Override
            public void onFailure(Call<List<Profilsayfasikullanicipaylasimlari>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "HATA OLUSTU " + t.getMessage(), Toast.LENGTH_LONG).show();

                /////////////////////////////////////
                paylasımlar_progresbar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                ////////////////////////////////////

            }
        });

        profil_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplication(), profil_resmi_pop_up.class);
                i.putExtra("id_kullanici",id);
                startActivity(i);

            }
        });


        ayarlarbutonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext()).setIcon(android.R.drawable.ic_dialog_info).setTitle("Çıkış")
                        .setMessage("Oturumu kapatmak istediğinize emin misiniz?")
                        .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SharedPreferences pref =getActivity().getApplicationContext().getSharedPreferences("giris",0);
                                if(pref.getInt("uye_id",0) != 0)
                                {
                                    SharedPreferences.Editor editor=pref.edit();
                                    editor.clear().commit();
                                }
                                Intent intent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }).setNegativeButton("Hayır", null).show();
            }
        });








    }

}
