package com.abms.af.projeversion02.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.abms.af.projeversion02.MainActivity;
import com.abms.af.projeversion02.Models.GonderiSil;
import com.abms.af.projeversion02.Models.Profilsayfasikullanicipaylasimlari;
import com.abms.af.projeversion02.R;
import com.abms.af.projeversion02.RestApi.ManagerAll;
import com.abms.af.projeversion02.homesayfasi_paylasimlari_ayrintili;
import com.abms.af.projeversion02.profil_paylasimlari_ayrinti;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profilkullanicipaylasimadapter extends BaseAdapter {

    List<Profilsayfasikullanicipaylasimlari> kullanicipaylasim;
    Context context;
    LinearLayout layoutlist;
    Activity activity;
    ImageView ButonSil;

    public Profilkullanicipaylasimadapter(List<Profilsayfasikullanicipaylasimlari> kullanicipaylasim, Context context, Activity activity) {
        this.kullanicipaylasim = kullanicipaylasim;
        this.context = context;
        this.activity = activity;
    }

    @Override

    public int getCount() {
        return kullanicipaylasim.size();
    }

    @Override
    public Object getItem(int position) {
        return kullanicipaylasim.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.listview_goruntu_profil_paylasimlari, viewGroup, false);
        final TextView aciklama, ders;
        String email = "";


        aciklama = view.findViewById(R.id.listview_profil_aciklama);
        ders = view.findViewById(R.id.listview_profil_ders);
        layoutlist = view.findViewById(R.id.profilsayfasi_listview_tıklama);
        ButonSil = view.findViewById(R.id.ButonSil);
        ders.setText(kullanicipaylasim.get(position).getDers());
        aciklama.setText(kullanicipaylasim.get(position).getAciklama());

        final String id_kullanici_string, paylasim_id_string, ad_soyad_string, universite_string, bolum_string, ders_string, aciklama_string, dosyayolu_string, dosyaturu_string, profilfoto_string;
        final int gosterme;
        id_kullanici_string = kullanicipaylasim.get(position).getIdkullanici();
        paylasim_id_string = kullanicipaylasim.get(position).getPaylasimid();
        ad_soyad_string = kullanicipaylasim.get(position).getAdsoyad();
        universite_string = kullanicipaylasim.get(position).getUniversite();
        bolum_string = kullanicipaylasim.get(position).getBolum();
        ders_string = kullanicipaylasim.get(position).getDers();
        aciklama_string = kullanicipaylasim.get(position).getAciklama();
        dosyayolu_string = kullanicipaylasim.get(position).getDosyayolu();
        dosyaturu_string = kullanicipaylasim.get(position).getDosyaturu();
        profilfoto_string = kullanicipaylasim.get(position).getProfilfoto();
        gosterme = kullanicipaylasim.get(position).getGosterme();


        int id_yorum = 0;
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences("giris", 0);
        if (sharedPreferences.getInt("uye_id", 0) != 0) {
            id_yorum = sharedPreferences.getInt("uye_id", 0);
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().commit();
            Intent intent = new Intent(context, MainActivity.class);
            activity.startActivity(intent);
        }

        sharedPreferences = activity.getApplicationContext().getSharedPreferences("giris", 0);
        if (sharedPreferences.getInt("uye_id", 0) != 0) {
            email = sharedPreferences.getString("email", "");

        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().commit();
            Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
        }

        if (id_yorum == Integer.valueOf(kullanicipaylasim.get(position).getIdkullanici())) {
            ButonSil.setVisibility(View.VISIBLE);

        } else {
            ButonSil.setVisibility(View.GONE);
        }

        final String finalEmail = email;
        ButonSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final SweetAlertDialog sa = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
                sa.setTitleText("Dikkat!");
                sa.setContentText("Bu gönderiyi silmek istediğinize emin misiniz?");
                sa.setConfirmText("Evet");
                sa.setCancelClickListener(null);
                sa.setCancelText("Hayır");
                sa.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        Integer id = Integer.valueOf(paylasim_id_string);

                        Call<GonderiSil> request = ManagerAll.webyonet().GonderiSil(finalEmail, id);
                        request.enqueue(new Callback<GonderiSil>() {
                            @Override
                            public void onResponse(Call<GonderiSil> call, Response<GonderiSil> response) {

                                kullanicipaylasim.remove(position);
                                notifyDataSetChanged();

                                sa.cancel();

                                final SweetAlertDialog sa = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE);
                                sa.setTitleText("Başarılı");
                                sa.setContentText("Gönderi Başarıyla Silindi");
                                sa.setConfirmText("Tamam");
                                sa.show();

                            }

                            @Override
                            public void onFailure(Call<GonderiSil> call, Throwable t) {

                                sa.cancel();

                                SweetAlertDialog sa = new SweetAlertDialog(activity,SweetAlertDialog.WARNING_TYPE);
                                sa.setTitleText("Dikkat!");
                                sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                                sa.setConfirmText("Tamam");
                                sa.show();
                            }
                        });
                    }
                });
                sa.show();

            }
        });

        layoutlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences;
                sharedPreferences = context.getSharedPreferences("giris", 0);
                int kendiid = sharedPreferences.getInt("uye_id", 0);
                int othersid = Integer.valueOf(id_kullanici_string);
                if (othersid == kendiid) {
                    Intent ayrintili = new Intent(activity, profil_paylasimlari_ayrinti.class);
                    ayrintili.putExtra("id_kullanici", id_kullanici_string);
                    ayrintili.putExtra("paylasim_id", paylasim_id_string);
                    ayrintili.putExtra("ad_soyad", ad_soyad_string);
                    ayrintili.putExtra("universite", universite_string);
                    ayrintili.putExtra("bolum", bolum_string);
                    ayrintili.putExtra("ders", ders_string);
                    ayrintili.putExtra("aciklama", aciklama_string);
                    ayrintili.putExtra("dosyayolu", dosyayolu_string);
                    ayrintili.putExtra("dosyaturu", dosyaturu_string);
                    ayrintili.putExtra("profilfoto", profilfoto_string);
                    ayrintili.putExtra("gosterme", gosterme);

                    activity.startActivity(ayrintili);
                } else {
                    Intent ayrintili = new Intent(activity, homesayfasi_paylasimlari_ayrintili.class);
                    ayrintili.putExtra("id_kullanici", id_kullanici_string);
                    ayrintili.putExtra("paylasim_id", paylasim_id_string);
                    ayrintili.putExtra("ad_soyad", ad_soyad_string);
                    ayrintili.putExtra("universite", universite_string);
                    ayrintili.putExtra("bolum", bolum_string);
                    ayrintili.putExtra("ders", ders_string);
                    ayrintili.putExtra("aciklama", aciklama_string);
                    ayrintili.putExtra("dosyayolu", dosyayolu_string);
                    ayrintili.putExtra("dosyaturu", dosyaturu_string);
                    ayrintili.putExtra("profilfoto", profilfoto_string);
                    ayrintili.putExtra("gosterme", gosterme);
                    //activity.finish();
                    activity.startActivity(ayrintili);
                }
            }
        });
        return view;
    }
}
