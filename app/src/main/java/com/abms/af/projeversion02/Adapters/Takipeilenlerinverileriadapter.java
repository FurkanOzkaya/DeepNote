package com.abms.af.projeversion02.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abms.af.projeversion02.Models.Homesayfasitumpaylasimveritabani;
import com.abms.af.projeversion02.Models.TakipedilenlerinVerileri;
import com.abms.af.projeversion02.R;
import com.abms.af.projeversion02.homesayfasi_paylasimlari_ayrintili;
import com.abms.af.projeversion02.others_profil_sayfasi;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Takipeilenlerinverileriadapter extends BaseAdapter {

    /*
    ANA SAYFAYA  TÜM PAYLASIMLARI GETİRİR
     */
    List<TakipedilenlerinVerileri> tumverilerliste;
    Context context;
    LinearLayout layoutlist, other_profil_kullanici;
    Activity activity;




    public Takipeilenlerinverileriadapter(List<TakipedilenlerinVerileri> tumverilerliste, Context context, Activity activity) {
        this.tumverilerliste = tumverilerliste;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return tumverilerliste.size();
    }

    @Override
    public Object getItem(int position) {
        return tumverilerliste.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.listview_goruntu_takipsayfasi_paylasimlari, viewGroup, false);
        TextView ad_soyad, universite, bolum, aciklama, ders, listview__gonderi_bolum_adi;
        final ImageView gelendosya;
        CircularImageView profil_foto;
        ad_soyad = view.findViewById(R.id.listview_ad_soyad);
        universite = view.findViewById(R.id.listview_universite);
        listview__gonderi_bolum_adi = view.findViewById(R.id.listview__gonderi_bolum_adi);
        bolum = view.findViewById(R.id.listview_bolum);
        aciklama = view.findViewById(R.id.listview_aciklama);
        ders = view.findViewById(R.id.listview_ders);
        layoutlist = view.findViewById(R.id.anasayfa_listview_layout);
        gelendosya = view.findViewById(R.id.gelendosya);
        profil_foto = view.findViewById(R.id.listview_profil_fotosu);
        other_profil_kullanici = view.findViewById(R.id.anasayfa_listview_kullanici_profil_sayfasi);


        final String id_kullanici_string, paylasim_id_string, ad_soyad_string, universite_string, bolum_string, ders_string, aciklama_string, dosyayolu_string, dosyaturu_string, profilfoto_string;
        id_kullanici_string = tumverilerliste.get(position).getIdkullanici();
        paylasim_id_string = tumverilerliste.get(position).getPaylasimid();
        ad_soyad_string = tumverilerliste.get(position).getAdsoyad();
        universite_string = tumverilerliste.get(position).getUniversite();
        bolum_string = tumverilerliste.get(position).getBolum();
        ders_string = tumverilerliste.get(position).getDers();
        aciklama_string = tumverilerliste.get(position).getAciklama();
        dosyayolu_string = tumverilerliste.get(position).getDosyayolu();
        dosyaturu_string = tumverilerliste.get(position).getDosyaturu();
        profilfoto_string = tumverilerliste.get(position).getProfilfoto();


        ad_soyad.setText(tumverilerliste.get(position).getAdsoyad());
        universite.setText(tumverilerliste.get(position).getUniversite());
        listview__gonderi_bolum_adi.setText(tumverilerliste.get(position).getBolum());
        ders.setText(tumverilerliste.get(position).getDers());
        bolum.setText(tumverilerliste.get(position).getBolum());
        aciklama.setText(tumverilerliste.get(position).getAciklama());

        final int  finalposition = position;
        if (tumverilerliste.get(position).getProfilfoto().equals("default")) {
            Picasso.get().load(R.drawable.flat_ogrenci).resize(200, 200).into(profil_foto);
        } else {
            ///////////////////////////////////
            Picasso.get().load(activity.getString(R.string.site_adresi) + tumverilerliste.get(position).getProfilfoto()).error(R.drawable.flat_ogrenci).into(profil_foto);
            /////////////////////////////////////
        }

        if (dosyaturu_string.equals("pdf")) {
            gelendosya.setImageResource(R.drawable.flat_pdf2);
        } else {
            Picasso.get().load(activity.getString(R.string.site_adresi) + tumverilerliste.get(position).getDosyayolu()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.loading).error(R.drawable.ic_launcher_background).into(gelendosya, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(activity.getString(R.string.site_adresi) + tumverilerliste.get(finalposition).getDosyayolu()).error(R.drawable.ic_launcher_background).into(gelendosya);
                }
            });
        }


        layoutlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                // Toast.makeText(context,"bilgiler:"+aciklama_string+bolum_string+ders_string,Toast.LENGTH_LONG).show();
                // Toast.makeText(context,"paylasımid:"+paylasim_id_string,Toast.LENGTH_LONG).show();
                activity.startActivity(ayrintili);
            }
        });

        other_profil_kullanici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent others = new Intent(activity, others_profil_sayfasi.class);
                others.putExtra("id_kullanici", id_kullanici_string);
                others.putExtra("paylasim_id", paylasim_id_string);
                others.putExtra("ad_soyad", ad_soyad_string);
                others.putExtra("universite", universite_string);
                others.putExtra("bolum", bolum_string);
                others.putExtra("ders", ders_string);
                others.putExtra("aciklama", aciklama_string);
                others.putExtra("dosyayolu", dosyayolu_string);
                others.putExtra("dosyaturu", dosyaturu_string);
                others.putExtra("profilfoto", profilfoto_string);
                activity.startActivity(others);
            }
        });


        return view;
    }

}
