package com.abms.af.projeversion02.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.abms.af.projeversion02.Models.NotTakipTakipciSayisi;
import com.abms.af.projeversion02.Models.Profilbilgilerigetir;
import com.abms.af.projeversion02.Models.Profilsayfasikullanicipaylasimlari;
import com.abms.af.projeversion02.Models.TakipKoduGetir;
import com.abms.af.projeversion02.R;
import com.abms.af.projeversion02.RestApi.ManagerAll;
import com.abms.af.projeversion02.ana_sayfa;
import com.abms.af.projeversion02.profil_resmi_pop_up;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.supercharge.shimmerlayout.ShimmerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class profil_sayfasi extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout yenileme_nesnesi;
    TextView profil_adi, profil_universite, profil_bolum, DeepNoteBaslik, notSayisi, takipciSayisi, takipSayisi;
    int id;
    View view;
    LinearLayout profil_bilgiler_layout;
    ListView listview_profil;
    Profilkullanicipaylasimadapter profilkullaniciadapter;
    List<Profilsayfasikullanicipaylasimlari> kullanici_paylasimlari;
    SharedPreferences sharedPreferences;
    String profil_ad_soyad_gelen, profil_bolum_gelen, profil_universite_gelen, Profil_foto_gelen;
    ImageView profil_foto, Takipkodu;
    ImageView ayarlarbutonu;
    String email = "";
    Typeface tf1;
    ShimmerLayout profil_bilgi_placeholder, profil_listview_placeholder;

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
        listview_profil = view.findViewById(R.id.profilsayfası_listview);
        profil_bilgiler_layout = view.findViewById(R.id.profil_bilgileri_kısmı);
        profil_foto = view.findViewById(R.id.profil_resmi);
        ayarlarbutonu = view.findViewById(R.id.profil_sayfası_ayarlar_butonu);
        yenileme_nesnesi = (SwipeRefreshLayout) view.findViewById(R.id.profil_sayfasi_refesh); // nesnemizi tanıttık
        yenileme_nesnesi.setOnRefreshListener(this);
        DeepNoteBaslik = view.findViewById(R.id.DeepNoteBaslik);
        profil_bilgi_placeholder = view.findViewById(R.id.profil_bilgi_placeholder);
        profil_listview_placeholder = view.findViewById(R.id.profil_listview_placeholder);
        notSayisi = view.findViewById(R.id.ProfilNotsayisi);
        takipciSayisi = view.findViewById(R.id.ProfilTakipciSayisi);
        takipSayisi = view.findViewById(R.id.ProfilTakipSayisi);
        Takipkodu = view.findViewById(R.id.TakipKodu);
    }


    void islev_ver() {

        tf1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DamionRegular.ttf");
        DeepNoteBaslik.setTypeface(tf1);

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("giris", 0);
        int id_kullanici = sharedPreferences.getInt("uye_id", 0);
        NotTakipTakipciSayisi(id_kullanici);

        if (sharedPreferences.getInt("uye_id", 0) != 0) {
            email = sharedPreferences.getString("email", "");

        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().commit();
            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }


        CallProfilePage();


        profil_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplication(), profil_resmi_pop_up.class);
                i.putExtra("id_kullanici", id);
                startActivity(i);

            }
        });


        ayarlarbutonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SweetAlertDialog sa = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                sa.setTitleText("Dikkat!");
                sa.setContentText("Oturumu kapatmak istediğinize emin misiniz?");
                sa.setConfirmText("Evet");
                sa.setCancelClickListener(null);
                sa.setCancelText("Hayır");
                sa.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("giris", 0);
                        if (pref.getInt("uye_id", 0) != 0) {
                            SharedPreferences.Editor editor = pref.edit();
                            editor.clear().commit();
                        }
                        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                });
                sa.show();
            }
        });


        Takipkodu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sharedPreferences = getContext().getSharedPreferences("TakipciKodu", 0);
                String TKodString = sharedPreferences.getString("TKod", "");
                if (TKodString != "") {

                    SweetAlertDialog sa = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                    sa.setTitleText("Kullanıcı Kodu");
                    sa.setContentText("Kullanıcı kodu diğer kullanıcıların sizi daha kolay bulmasını sağlar. Kullanıcı Kodunuz: " + TKodString);
                    sa.setConfirmText("Kapat");
                    sa.show();

                } else {

                    Call<TakipKoduGetir> request = ManagerAll.webyonet().TakipKoduGetir(getString(R.string.jsongüvenlikkod), id_kullanici);
                    request.enqueue(new Callback<TakipKoduGetir>() {
                        @Override
                        public void onResponse(Call<TakipKoduGetir> call, Response<TakipKoduGetir> response) {

                            sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("TakipciKodu", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("TKod", response.body().getTakipKodu());
                            editor.commit();

                            SweetAlertDialog sa = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                            sa.setTitleText("Kullanıcı Kodu");
                            sa.setContentText("Kullanıcı kodu diğer kullanıcıların sizi daha kolay bulmasını sağlar. Kullanıcı Kodunuz: " + response.body().getTakipKodu());
                            sa.setConfirmText("Kapat");
                            sa.show();

                        }

                        @Override
                        public void onFailure(Call<TakipKoduGetir> call, Throwable t) {

                        }
                    });

                }
            }
        });

    }


    @Override
    public void onRefresh() {

        CallProfilePage();
        yenileme_nesnesi.setRefreshing(false);
    }

    public void NotTakipTakipciSayisi(int id_kullanici) {
        Call<NotTakipTakipciSayisi> request = ManagerAll.webyonet().NotTakipTakipciSayisi(id_kullanici);
        request.enqueue(new Callback<NotTakipTakipciSayisi>() {
            @Override
            public void onResponse(Call<NotTakipTakipciSayisi> call, Response<NotTakipTakipciSayisi> response) {

                String NotSayisi = String.valueOf(response.body().getNot());
                String TakipSayisi = String.valueOf(response.body().getTakip());
                String TakipciSayisi = String.valueOf(response.body().getTakipci());

                Log.i("ppppp", TakipciSayisi);

                notSayisi.setText(NotSayisi);
                takipSayisi.setText(TakipSayisi);
                takipciSayisi.setText(TakipciSayisi);

            }

            @Override
            public void onFailure(Call<NotTakipTakipciSayisi> call, Throwable t) {

                SweetAlertDialog sa = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                sa.setTitleText("Dikkat!");
                sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                sa.setConfirmText("Tamam");
                sa.show();

            }
        });
    }

    public void CallProfilePage() {
        try {
            Call<Profilbilgilerigetir> a = ManagerAll.webyonet().profilgetir(email, id);

            profil_bilgiler_layout.setVisibility(View.GONE);
            profil_bilgi_placeholder.setVisibility(View.VISIBLE);
            profil_bilgi_placeholder.startShimmerAnimation();

            a.enqueue(new Callback<Profilbilgilerigetir>() {
                @Override
                public void onResponse(Call<Profilbilgilerigetir> call, Response<Profilbilgilerigetir> response) {

                    profil_bilgiler_layout.setVisibility(View.VISIBLE);
                    profil_bilgi_placeholder.setVisibility(View.GONE);
                    profil_bilgi_placeholder.stopShimmerAnimation();

                    if (response.isSuccessful()) {
                        if (response.body().getSonuc() == 1) {
                            profil_ad_soyad_gelen = response.body().getAd_soyad().toString();
                            profil_bolum_gelen = response.body().getBolum().toString();
                            profil_universite_gelen = response.body().getUniversite().toString();
                            Profil_foto_gelen = response.body().getProfil_foto();


                            profil_adi.setText(profil_ad_soyad_gelen);
                            profil_universite.setText(profil_universite_gelen);
                            profil_bolum.setText(profil_bolum_gelen);

                            if (response.body().getProfil_foto().equals("default")) {
                                Picasso.get().load(R.drawable.flat_ogrenci).resize(200, 200).into(profil_foto);
                            } else {
                                try {
                                    ///////////////////////////////////
                                    Picasso.get().load(getString(R.string.site_adresi) + response.body().getProfil_foto()).resize(200, 200).error(R.drawable.flat_ogrenci).into(profil_foto);
                                    /////////////////////////////////////
                                } catch (Exception e) {
                                    Log.e("TAG", "Profilepicasso: ", e);
                                }
                            }


                        }
                    }

                }

                @Override
                public void onFailure(Call<Profilbilgilerigetir> call, Throwable t) {

                    SweetAlertDialog sa = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                    sa.setTitleText("Dikkat!");
                    sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                    sa.setConfirmText("Tamam");
                    sa.show();

                    profil_bilgiler_layout.setVisibility(View.VISIBLE);
                    profil_bilgi_placeholder.setVisibility(View.GONE);
                    profil_bilgi_placeholder.stopShimmerAnimation();
                }
            });
        } catch (Exception e) {
            Log.e("TAG", "CallProfilePage: ", e);
        }
        try {
            Call<List<Profilsayfasikullanicipaylasimlari>> kullanicipaylasım = ManagerAll.webyonet().kullancigönderigetir(email, id);

            listview_profil.setVisibility(View.GONE);
            profil_listview_placeholder.setVisibility(View.VISIBLE);
            profil_listview_placeholder.startShimmerAnimation();

            kullanicipaylasım.enqueue(new Callback<List<Profilsayfasikullanicipaylasimlari>>() {
                @Override
                public void onResponse(Call<List<Profilsayfasikullanicipaylasimlari>> call, Response<List<Profilsayfasikullanicipaylasimlari>> response) {
                    listview_profil.setVisibility(View.VISIBLE);
                    profil_listview_placeholder.setVisibility(View.GONE);
                    profil_listview_placeholder.stopShimmerAnimation();

                    if (response.isSuccessful()) {

                        try {
                            kullanici_paylasimlari = response.body();
                            profilkullaniciadapter = new Profilkullanicipaylasimadapter(kullanici_paylasimlari, getActivity().getApplicationContext(), getActivity());
                            listview_profil.setAdapter(profilkullaniciadapter);

                        } catch (Exception e) {
                            Log.e("TAG", "profilepage: ", e);
                        }

                    } else {

                    }
                }

                @Override
                public void onFailure(Call<List<Profilsayfasikullanicipaylasimlari>> call, Throwable t) {

                    SweetAlertDialog sa = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                    sa.setTitleText("Dikkat!");
                    sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                    sa.setConfirmText("Tamam");
                    sa.show();

                    listview_profil.setVisibility(View.VISIBLE);
                    profil_listview_placeholder.setVisibility(View.GONE);
                    profil_listview_placeholder.stopShimmerAnimation();
                }
            });
        } catch (Exception e) {
            Log.e("TAG", "CallProfilePage: ", e);
        }
    }

}
