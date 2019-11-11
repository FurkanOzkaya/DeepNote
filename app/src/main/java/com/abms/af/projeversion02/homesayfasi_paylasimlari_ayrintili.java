package com.abms.af.projeversion02;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abms.af.projeversion02.Adapters.Yorumadapter;
import com.abms.af.projeversion02.Models.Homesayfasitumpaylasimveritabani;
import com.abms.af.projeversion02.Models.SikayetEt;
import com.abms.af.projeversion02.Models.Sikayetetmesonuc;
import com.abms.af.projeversion02.Models.Yorumlarigetirsonuc;
import com.abms.af.projeversion02.Models.Yorumyapmasonuc;
import com.abms.af.projeversion02.RestApi.ManagerAll;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class homesayfasi_paylasimlari_ayrintili extends AppCompatActivity {

    TextView DeepNoteBaslik, ayrıntili_indirme, ayrintili_ad_soyad, ayrintili_universite, ayrintili_bolum, ayrintili_ders, ayrintili_aciklama, listview_yorumlar_uyarı, ayrıntılı_sikayet_et, listview__gonderi_bolum_adi;
    ImageView ayrıntılı_resim;
    ImageButton ayrıntı_yorum_yapmabutonu;
    String gonderi_bolum_string, id_kullanici_string, paylasim_id_string, ad_soyad_string, universite_string, bolum_string, ders_string, aciklama_string, dosyayolu_string, dosyaturu_string, profilfoto_string;
    WebView Pdfview;
    SwipeRefreshLayout swipeRefesh;
    CircularImageView profil_foto;
    EditText ayrıntı_yorum;
    ListView listview_yorumlar;
    LinearLayout other_profil_kullanici;
    List<Yorumlarigetirsonuc> gelenyorumlar;
    Yorumadapter yorumadapter;
    ProgressBar progressBar;
    Button openpdf;
    SharedPreferences sharedPreferences;
    String email;
    Typeface tf1;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homesayfasi_paylasimlari_ayrintili);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        bilgilerial();
        tanımla();

        /// id is ca-app-pub-4925490463007858/3573267940 when deploying use this id for money
        MobileAds.initialize(this, "ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        islevver();
    }


    public void bilgilerial() {

        Bundle bundle = getIntent().getExtras();
        id_kullanici_string = bundle.getString("id_kullanici");
        paylasim_id_string = bundle.getString("paylasim_id");
        ad_soyad_string = bundle.getString("ad_soyad");
        universite_string = bundle.getString("universite");
        bolum_string = bundle.getString("bolum");
        ders_string = bundle.getString("ders");
        aciklama_string = bundle.getString("aciklama");
        dosyayolu_string = bundle.getString("dosyayolu");
        dosyaturu_string = bundle.getString("dosyaturu");
        profilfoto_string = bundle.getString("profilfoto");
    }

    public void tanımla() {

        ayrintili_ad_soyad = findViewById(R.id.ayrıntı_homesayfasi_ad_soyad);
        ayrintili_universite = findViewById(R.id.ayrıntı_homesayfasi_universite);
        ayrintili_bolum = findViewById(R.id.ayrıntı_homesayfasi_bolum);
        ayrintili_ders = findViewById(R.id.ayrıntı_homesayfasi_ders);
        ayrintili_aciklama = findViewById(R.id.ayrıntı_homesayfasi_aciklama);
        ayrıntılı_resim = findViewById(R.id.ayrıntı_homesayfasi_resim);
        Pdfview = findViewById(R.id.ayrıntı_homesayfasi_pdf_webview);
        openpdf = findViewById(R.id.openpdf);
        profil_foto = findViewById(R.id.ayrıntı_homesayfası_profilfotosu);
        ayrıntı_yorum = findViewById(R.id.ayrıntı_homesayfasi_yorum);
        ayrıntı_yorum_yapmabutonu = findViewById(R.id.ayrıntı_homesayfasi_yorum_buton);
        listview_yorumlar = findViewById(R.id.ayrıntı_homesayfasi_yorum_listview);
        ayrıntili_indirme = findViewById(R.id.ayrıntı_homesayfasi_indirme);
        swipeRefesh = findViewById(R.id.paylasimlar_ayrinti_refesh);
        listview__gonderi_bolum_adi = findViewById(R.id.listview__gonderi_bolum_adi);
        DeepNoteBaslik = findViewById(R.id.DeepNoteBaslik);


        progressBar = findViewById(R.id.home_ayrıntı_progressbar);

        /////////// profil acmak için//////////////////
        other_profil_kullanici = findViewById(R.id.home_sayfası_profil_gonderme);
        ///////////////////////////////////
        /////////////////////////////
        ayrıntılı_sikayet_et = findViewById(R.id.ayrıntı_homesayfasi_sikayet_et);
        //////////////////////////////////////
        listview_yorumlar_uyarı = findViewById(R.id.yorumlar_lisview_uyarı);

        ayrintili_ad_soyad.setText(ad_soyad_string);
        ayrintili_universite.setText(universite_string);
        ayrintili_bolum.setText(bolum_string);
        ayrintili_ders.setText(ders_string);
        ayrintili_aciklama.setText(aciklama_string);
        listview__gonderi_bolum_adi.setText(bolum_string);

        if (profilfoto_string.equals("default")) {
            Picasso.get().load(R.drawable.flat_ogrenci).resize(200, 200).into(profil_foto);
        } else {
            Picasso.get().load(getString(R.string.site_adresi) + profilfoto_string).error(R.drawable.flat_ogrenci).into(profil_foto);
        }
    }


    public void islevver() {

        tf1 = Typeface.createFromAsset(getAssets(), "fonts/DamionRegular.ttf");
        DeepNoteBaslik.setTypeface(tf1);

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

        openpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                Intent intent = new Intent(getApplicationContext(), pdfViewer.class);
                intent.putExtra("url", getString(R.string.site_adresi) + dosyayolu_string);
                startActivity(intent);
            }
        });

        if (dosyaturu_string.equals("pdf")) {
            ayrıntılı_resim.setVisibility(View.GONE);
            Pdfview.setVisibility(View.VISIBLE);

            Pdfview.loadUrl("https://docs.google.com/gview?embedded=true&url=" + getString(R.string.site_adresi) + dosyayolu_string);

        } else {
            openpdf.setVisibility(View.GONE);
            ayrıntılı_resim.setVisibility(View.VISIBLE);
            Log.i("TAG", "islevver: " + dosyayolu_string);
            Picasso.get().load(getString(R.string.site_adresi) + dosyayolu_string).error(R.drawable.ic_launcher_background).into(ayrıntılı_resim);
        }

        ayrıntılı_sikayet_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final SweetAlertDialog p = new SweetAlertDialog(homesayfasi_paylasimlari_ayrintili.this, SweetAlertDialog.WARNING_TYPE);
                p.setTitleText("Dikkat!");
                p.setContentText("Şikayet etmek istediğinize emin misiniz?");
                p.setConfirmText("Evet");
                p.setCancelText("Hayır");
                p.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        p.cancel();

                        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);

                        final Integer paylasımid = Integer.parseInt(paylasim_id_string);
                        int kullanici_id = sharedPreferences.getInt("uye_id", 0);

                        Call<SikayetEt> request = ManagerAll.webyonet().SikayetEt(kullanici_id, paylasımid);
                        request.enqueue(new Callback<SikayetEt>() {
                            @Override
                            public void onResponse(Call<SikayetEt> call, Response<SikayetEt> response) {

                                if (response.body().getResult().equals("Basarili")) {

                                    //Bu kod paylasim tablosundaki sikayet sayısını arttırır, admin panelinin işleyişini bozmamak için geçici süre çalışacaktır
                                    /////////////////////////////////////////////////////////////////////////////////
                                            /*
                                            Call<Sikayetetmesonuc> s = ManagerAll.webyonet().sikayetet(email, paylasımid);
                                            s.enqueue(new Callback<Sikayetetmesonuc>() {
                                                @Override
                                                public void onResponse(Call<Sikayetetmesonuc> call, Response<Sikayetetmesonuc> response) {

                                                    Toast.makeText(getApplicationContext(), response.body().getSikayetetmesonuc(), Toast.LENGTH_LONG).show();

                                                    ayrıntılı_sikayet_et.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onFailure(Call<Sikayetetmesonuc> call, Throwable t) {
                                                    Toast.makeText(getApplicationContext(), "Şikayet edilemedi", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                            */
                                    //////////////////////////////////////////////////////////////////////////////////

                                    final SweetAlertDialog sa = new SweetAlertDialog(homesayfasi_paylasimlari_ayrintili.this, SweetAlertDialog.SUCCESS_TYPE);
                                    sa.setTitleText("Başarılı");
                                    sa.setContentText("Uygunsuz olabileceğini düşündüğün bu gönderiyi bize bilgilendirdiğin için teşekkür ederiz");
                                    sa.setConfirmText("Tamam");
                                    sa.show();
                                } else if (response.body().getResult().equals("Basarisiz")) {
                                    final SweetAlertDialog sa = new SweetAlertDialog(homesayfasi_paylasimlari_ayrintili.this, SweetAlertDialog.WARNING_TYPE);
                                    sa.setTitleText("Dikkat!");
                                    sa.setContentText("Bu gönderiyi daha önce şikayet etmiş bulunmaktasın");
                                    sa.setConfirmText("Tamam");
                                    sa.show();
                                } else {
                                    final SweetAlertDialog sa = new SweetAlertDialog(homesayfasi_paylasimlari_ayrintili.this, SweetAlertDialog.WARNING_TYPE);
                                    sa.setTitleText("Dikkat!");
                                    sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                                    sa.setConfirmText("Tamam");
                                    sa.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<SikayetEt> call, Throwable t) {

                                final SweetAlertDialog sa = new SweetAlertDialog(homesayfasi_paylasimlari_ayrintili.this, SweetAlertDialog.WARNING_TYPE);
                                sa.setTitleText("Dikkat!");
                                sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                                sa.setConfirmText("Tamam");
                                sa.show();

                            }
                        });

                    }
                });
                p.show();

            }
        });

        final int paylasim_id = Integer.valueOf(paylasim_id_string);
        ayrıntı_yorum_yapmabutonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String yorum = ayrıntı_yorum.getText().toString();
                ayrıntı_yorum.setText("");
                if (!yorum.equals("")) {
                    int id = 0;
                    SharedPreferences sharedPreferences;
                    sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);
                    if (sharedPreferences.getInt("uye_id", 0) != 0) {
                        id = sharedPreferences.getInt("uye_id", 0);
                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear().commit();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                    try {
                        ////////// Y O R U M   Y A P M A   K I S M I
                        Call<Yorumyapmasonuc> x = ManagerAll.webyonet().yorumyap(email, id, paylasim_id, yorum);
                        x.enqueue(new Callback<Yorumyapmasonuc>() {
                            @Override
                            public void onResponse(Call<Yorumyapmasonuc> call, Response<Yorumyapmasonuc> response) {

                                if (response.isSuccessful()) {
                                    //Toast.makeText(getApplicationContext(),"Yorum yapıldı",Toast.LENGTH_LONG).show();
                                    //////////// Y O R U M  G E T İ R M E   Y O R U M   Y A P T I K T A N  S O N R A

                                    final Call<List<Yorumlarigetirsonuc>> yorumgetir = ManagerAll.webyonet().yorumgetir(email, paylasim_id);
                                    //////////////////////////////// P R O G R E S S   B A R    //////////////////////
                                    progressBar.setVisibility(View.VISIBLE);
                                    ////////////////////////////////////////////////////////////////////////////////////
                                    yorumgetir.enqueue(new Callback<List<Yorumlarigetirsonuc>>() {
                                        @Override
                                        public void onResponse(Call<List<Yorumlarigetirsonuc>> call, Response<List<Yorumlarigetirsonuc>> response) {
                                            if (response.isSuccessful()) {
                                                listview_yorumlar.setVisibility(View.VISIBLE);
                                                listview_yorumlar_uyarı.setVisibility(View.GONE);
                                                //Toast.makeText(getApplicationContext(),"Yorumalar geldi"+response.body(),Toast.LENGTH_LONG).show();
                                                gelenyorumlar = response.body();
                                                yorumadapter = new Yorumadapter(gelenyorumlar, homesayfasi_paylasimlari_ayrintili.this, getApplicationContext());
                                                listview_yorumlar.setAdapter(yorumadapter);
                                                setListViewHeightBasedOnItems(listview_yorumlar);

                                                /////////////////////////////////////
                                                progressBar.setVisibility(View.GONE);
                                                ///////////////////////////   P R O G R E S S   B A R   /////////
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<List<Yorumlarigetirsonuc>> call, Throwable t) {
                                            // Toast.makeText(getApplicationContext(),"Yorumalar gelmedi"+t.getMessage(),Toast.LENGTH_LONG).show();
                                            listview_yorumlar.setVisibility(View.GONE);
                                            listview_yorumlar_uyarı.setVisibility(View.VISIBLE);
                                            /////////////////////////////////////
                                            progressBar.setVisibility(View.GONE);
                                            ///////////////////////////   P R O G R E S S   B A R   /////////
                                        }
                                    });
                                } else {

                                    SweetAlertDialog sa = new SweetAlertDialog(homesayfasi_paylasimlari_ayrintili.this, SweetAlertDialog.WARNING_TYPE);
                                    sa.setTitleText("Dikkat!");
                                    sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                                    sa.setConfirmText("Tamam");
                                    sa.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Yorumyapmasonuc> call, Throwable t) {

                                SweetAlertDialog sa = new SweetAlertDialog(homesayfasi_paylasimlari_ayrintili.this, SweetAlertDialog.WARNING_TYPE);
                                sa.setTitleText("Dikkat!");
                                sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                                sa.setConfirmText("Tamam");
                                sa.show();
                            }
                        });
                    } catch (Exception e) {
                        Log.e("TAG", "onClick: ", e);
                    }
                }
            }
        });


        try {
            //////////// Y O R U M  G E T İ R M E
            final Call<List<Yorumlarigetirsonuc>> yorumgetir = ManagerAll.webyonet().yorumgetir(email, paylasim_id);
            //////////////////////////////// P R O G R E S S   B A R    //////////////////////
            progressBar.setVisibility(View.VISIBLE);
            ////////////////////////////////////////////////////////////////////////////////////
            yorumgetir.enqueue(new Callback<List<Yorumlarigetirsonuc>>() {
                @Override
                public void onResponse(Call<List<Yorumlarigetirsonuc>> call, Response<List<Yorumlarigetirsonuc>> response) {
                    if (response.isSuccessful()) {
                        listview_yorumlar.setVisibility(View.VISIBLE);
                        listview_yorumlar_uyarı.setVisibility(View.GONE);
                        // Toast.makeText(getApplicationContext(),"Yorumalar geldi"+response.body(),Toast.LENGTH_LONG).show();
                        gelenyorumlar = response.body();
                        yorumadapter = new Yorumadapter(gelenyorumlar, homesayfasi_paylasimlari_ayrintili.this, getApplicationContext());
                        listview_yorumlar.setAdapter(yorumadapter);
                        setListViewHeightBasedOnItems(listview_yorumlar);

                        /////////////////////////////////////
                        progressBar.setVisibility(View.GONE);
                        ///////////////////////////   P R O G R E S S   B A R   /////////
                    }
                }

                @Override
                public void onFailure(Call<List<Yorumlarigetirsonuc>> call, Throwable t) {
                    //Toast.makeText(getApplicationContext(),"Yorumalar gelmedi"+t.getMessage(),Toast.LENGTH_LONG).show();
                    listview_yorumlar.setVisibility(View.GONE);
                    listview_yorumlar_uyarı.setVisibility(View.VISIBLE);
                    /////////////////////////////////////
                    progressBar.setVisibility(View.GONE);
                    ///////////////////////////   P R O G R E S S   B A R   /////////
                }
            });
        } catch (Exception e) {
            Log.e("TAG", "islevver: ", e);
        }

        other_profil_kullanici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent others = new Intent(homesayfasi_paylasimlari_ayrintili.this, others_profil_sayfasi.class);
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
                finish();
                homesayfasi_paylasimlari_ayrintili.this.startActivity(others);
            }
        });



        swipeRefesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                islevver();
                swipeRefesh.setRefreshing(false);
            }
        });


        ayrıntili_indirme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ayrıntili_indirme.setVisibility(View.GONE);

                final SweetAlertDialog pDialog = new SweetAlertDialog(homesayfasi_paylasimlari_ayrintili.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("İndiriliyor");
                pDialog.setCancelable(false);
                pDialog.show();

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                try {
                    Call<ResponseBody> down = ManagerAll.webyonet().indirr(dosyayolu_string);
                    down.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            //Toast.makeText(homesayfasi_paylasimlari_ayrintili.this, "Dosya İndiriliyor Dosya Yöneticisinden Bulabilirsiniz", Toast.LENGTH_SHORT).show();

                            Boolean dosya = dosyayıdiskeyaz(response.body());
                            if (dosya == true) {

                                pDialog.cancel();

                                try {
                                    new SweetAlertDialog(homesayfasi_paylasimlari_ayrintili.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("İndirme Başarılı!")
                                            .setContentText("Dosya yöneticinizden indirdiklerinizi bulabilirsiniz")
                                            .show();
                                }catch (Exception e)
                                {
                                    Log.e("TAG", "ondownload: ", e);
                                }

                            } else {

                                pDialog.cancel();

                                try {
                                    new SweetAlertDialog(homesayfasi_paylasimlari_ayrintili.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Dikkat!")
                                            .setContentText("İndirme işlemi sırasında hata oluştu, Lütfen daha sonra tekrar deneyiniz")
                                            .show();
                                }catch (Exception e)
                                {
                                    Log.e("TAG", "ondownload: ", e);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                            pDialog.cancel();

                            SweetAlertDialog sa = new SweetAlertDialog(homesayfasi_paylasimlari_ayrintili.this, SweetAlertDialog.WARNING_TYPE);
                            sa.setTitleText("Dikkat!");
                            sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                            sa.setConfirmText("Tamam");
                            sa.show();
                        }
                    });
                } catch (Exception e) {
                    Log.e("TAG", "onClick: ", e);
                }

            }
        });

    }


    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;

        } else {
            return false;
        }

    }


    /*
                Bu fonksiyon indirilen dosyanın kaydedilmesi için yazılmıstır..
     */
    private boolean dosyayıdiskeyaz(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");// you can add z for zone gmt +3
            String currentDateandTime = sdf.format(new Date());
            //  File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator +""+universite_string+ders_string+random1+random2+".jpeg");

            File root = android.os.Environment.getExternalStorageDirectory();

            File dir;
            String fileName;
            if (dosyaturu_string.equals("pdf")) {
                fileName = universite_string + ders_string + currentDateandTime + ".pdf";
                dir = new File(root.getAbsolutePath() + "/DeepNote_pdf");
            } else {
                fileName = universite_string + ders_string + currentDateandTime + ".jpeg";
                dir = new File(root.getAbsolutePath() + "/DeepNote_resim");
            }
            if (dir.exists() == false) {
                dir.mkdirs();
            }

            File file = new File(dir, fileName);
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("TAG", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                Log.i("TAG", "dosyayıdiskeyaz: " + e.getMessage());
                Toast.makeText(this, "hata" + e.getMessage(), Toast.LENGTH_SHORT).show();
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            Log.i("TAG", "dosyayıdiskeyaz: " + e.getMessage());
            Toast.makeText(this, "hata" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}


