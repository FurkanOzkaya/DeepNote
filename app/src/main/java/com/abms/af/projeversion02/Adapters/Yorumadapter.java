package com.abms.af.projeversion02.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.abms.af.projeversion02.MainActivity;
import com.abms.af.projeversion02.Models.Yorumlarigetirsonuc;
import com.abms.af.projeversion02.Models.Yorumsilmesonuc;
import com.abms.af.projeversion02.R;
import com.abms.af.projeversion02.RestApi.ManagerAll;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Yorumadapter extends BaseAdapter {

    List<Yorumlarigetirsonuc> gelenyorumlar;
    Context context;
    Activity activity;
    CircularImageView yorumcufotosu;


    public Yorumadapter(List<Yorumlarigetirsonuc> gelenyorumlar, Activity activity, Context context) {
        this.gelenyorumlar = gelenyorumlar;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return gelenyorumlar.size();
    }

    @Override
    public Object getItem(int position) {
        return gelenyorumlar.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        TextView yorumcu_adi, tarih, yorum;
        ImageButton yorum_silme_butonu;
        SharedPreferences sharedPreferences;
        String email = "";

        view = LayoutInflater.from(context).inflate(R.layout.listview_goruntu_yorumlar, viewGroup, false);

        yorumcufotosu = view.findViewById(R.id.yorumlar_listview_profil_fotosu);
        yorumcu_adi = view.findViewById(R.id.yorumlar_ad);
        tarih = view.findViewById(R.id.yorumlar_tarih);
        yorum = view.findViewById(R.id.yorumlar_yorum);
        yorum_silme_butonu = view.findViewById(R.id.yorumlar_yorumsil);


        int id = 0;
        sharedPreferences = context.getSharedPreferences("giris", 0);
        if (sharedPreferences.getInt("uye_id", 0) != 0) {
            id = sharedPreferences.getInt("uye_id", 0);
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().commit();
            Intent intent = new Intent(context, MainActivity.class);
            activity.startActivity(intent);
        }


        if (id == Integer.valueOf(gelenyorumlar.get(position).getId_kullanici())) {
            yorum_silme_butonu.setVisibility(View.VISIBLE);

        } else {
            yorum_silme_butonu.setVisibility(View.GONE);
        }


        // taking email for email validate
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


        Log.i("TAG", "yorumlar: " + gelenyorumlar.get(position).getAdsoyad() + gelenyorumlar.get(position).getTarih() + gelenyorumlar.get(position).getYorum());

        if (gelenyorumlar.get(position).getId_kullanici() != null && gelenyorumlar.get(position).getProfilfoto() != null && gelenyorumlar.get(position).getAdsoyad() != null && gelenyorumlar.get(position).getYorum() != null && gelenyorumlar.get(position).getTarih() != null) {
            yorumcu_adi.setText(gelenyorumlar.get(position).getAdsoyad().toString());
            tarih.setText(gelenyorumlar.get(position).getTarih().toString());
            yorum.setText(gelenyorumlar.get(position).getYorum().toString());

            if (gelenyorumlar.get(position).getProfilfoto().equals("default")) {
                Picasso.get().load(R.drawable.flat_ogrenci).resize(200, 200).into(yorumcufotosu);
            } else {
                ///////////////////////////////////
                Picasso.get().load(activity.getString(R.string.site_adresi) + gelenyorumlar.get(position).getProfilfoto()).error(R.drawable.flat_ogrenci).into(yorumcufotosu);
                /////////////////////////////////////
            }
        }


        final String finalEmail = email;
        yorum_silme_butonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(activity).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Yorum Sil")
                        .setMessage("Yorumu silmek  istediğinize emin misiniz?")
                        .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int id_kullanici = Integer.valueOf(gelenyorumlar.get(position).getId_kullanici());
                                int paylasim_id = Integer.valueOf(gelenyorumlar.get(position).getPaylasim_id());
                                final int id_yorum = Integer.valueOf(gelenyorumlar.get(position).getId_yorum());

                                Call<Yorumsilmesonuc> yorumsil = ManagerAll.webyonet().yorumsil(finalEmail, id_kullanici, paylasim_id, id_yorum);
                                yorumsil.enqueue(new Callback<Yorumsilmesonuc>() {
                                    @Override
                                    public void onResponse(Call<Yorumsilmesonuc> call, Response<Yorumsilmesonuc> response) {
                                        if (response.isSuccessful()) {
                                            gelenyorumlar.remove(position);
                                            notifyDataSetChanged();
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<Yorumsilmesonuc> call, Throwable t) {

                                        final SweetAlertDialog sa = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE);
                                        sa.setTitleText("Dikkat");
                                        sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                                        sa.setConfirmText("Tamam");
                                        sa.show();

                                    }
                                });


                            }
                        }).setNegativeButton("Hayır", null).show();
            }
        });


        return view;
    }
}
