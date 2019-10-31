package com.abms.af.projeversion02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abms.af.projeversion02.Adapters.Yorumadapter;
import com.abms.af.projeversion02.Models.Yorumlarigetirsonuc;
import com.abms.af.projeversion02.Models.Yorumyapmasonuc;
import com.abms.af.projeversion02.RestApi.ManagerAll;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class profil_paylasimlari_ayrinti extends AppCompatActivity {


    TextView ayrintili_ad_soyad, ayrintili_universite, ayrintili_bolum, ayrintili_ders, ayrintili_aciklama, aytinti_profilsayfasi_gonderi_bolum_adi;
    ImageView ayrıntılı_resim;
    ImageButton ayrıntılı_sikayet_et, profil_ayrıntı_yorum_yapmabutonu;
    String id_kullanici_string, paylasim_id_string, ad_soyad_string, universite_string, bolum_string, ders_string, aciklama_string, dosyayolu_string, dosyaturu_string, profilfoto_string;
    WebView Pdfview;
    CircularImageView profil_foto;
    int gosterme;
    EditText profil_ayrıntı_yorum;
    ListView profil_yorumlar_listview;
    TextView profil_ayrıntı_yorumlar_uyari;
    List<Yorumlarigetirsonuc> gelenyorumlar;
    Yorumadapter yorumadapter;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    String email = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_paylasimlari_ayrinti);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        bilgilerial();
        tanımla();
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
        gosterme = bundle.getInt("gosterme");
    }

    public void tanımla() {

        ayrintili_ad_soyad = findViewById(R.id.ayrıntı_profilsayfasi_ad_soyad);
        ayrintili_universite = findViewById(R.id.ayrıntı_profilsayfasi_universite);
        ayrintili_bolum = findViewById(R.id.ayrıntı_profilsayfasi_bolum);
        ayrintili_ders = findViewById(R.id.ayrıntı_profilsayfasi_ders);
        ayrintili_aciklama = findViewById(R.id.ayrıntı_profilsayfasi_aciklama);
        ayrıntılı_resim = findViewById(R.id.ayrıntı_profilsayfasi_resim);
        Pdfview = findViewById(R.id.ayrıntı_profilsayfasi_pdf_webview);
        profil_foto = findViewById(R.id.ayrıntı_profilsayfasi_profilfotosu);
        profil_ayrıntı_yorum = findViewById(R.id.ayrıntı_profilsayfasi_yorum);
        profil_ayrıntı_yorum_yapmabutonu = findViewById(R.id.ayrıntı_profil_yorum_buton);
        profil_yorumlar_listview = findViewById(R.id.ayrıntı_profil_listview);
        profil_ayrıntı_yorumlar_uyari = findViewById(R.id.ayrıntı_profil_yorumlar_lisview_uyarı);
        aytinti_profilsayfasi_gonderi_bolum_adi = findViewById(R.id.aytinti_profilsayfasi_gonderi_bolum_adi);

        progressBar = findViewById(R.id.profil_ayrıntı_progressbar);

        ayrintili_ad_soyad.setText(ad_soyad_string);
        ayrintili_universite.setText(universite_string);
        ayrintili_bolum.setText(bolum_string);
        ayrintili_ders.setText(ders_string);
        ayrintili_aciklama.setText(aciklama_string);
        aytinti_profilsayfasi_gonderi_bolum_adi.setText(bolum_string);

        if (profilfoto_string.equals("default")) {
            Picasso.get().load(R.drawable.flat_ogrenci).resize(200, 200).into(profil_foto);
        } else {
            Picasso.get().load(getString(R.string.site_adresi) + profilfoto_string).error(R.drawable.flat_ogrenci).into(profil_foto);
        }
    }

    public void islevver() {

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

        if (dosyaturu_string.equals("pdf")) {
            ayrıntılı_resim.setVisibility(View.GONE);
            Pdfview.setVisibility(View.VISIBLE);
            Pdfview.getSettings().setJavaScriptEnabled(true);
            Pdfview.loadUrl("https://docs.google.com/gview?embedded=true&url=" + getString(R.string.site_adresi) + dosyayolu_string);

        } else {
            ayrıntılı_resim.setVisibility(View.VISIBLE);
            Picasso.get().load(getString(R.string.site_adresi) + dosyayolu_string).error(R.drawable.ic_launcher_background).into(ayrıntılı_resim);
        }


        // G O S T E R M E   K A P A L I Y S A
        if (gosterme == 0) {
            Toast.makeText(getApplicationContext(), "Bu paylasım engellenmiştir! Daha fazla bilgi almak için mail atınız.", Toast.LENGTH_LONG).show();
        }

        profil_ayrıntı_yorum_yapmabutonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String yorum = profil_ayrıntı_yorum.getText().toString();
                final int paylasim_id = Integer.valueOf(paylasim_id_string);
                profil_ayrıntı_yorum.setText("");
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
                        Call<Yorumyapmasonuc> x = ManagerAll.webyonet().yorumyap(email, id, paylasim_id, yorum);
                        x.enqueue(new Callback<Yorumyapmasonuc>() {
                            @Override
                            public void onResponse(Call<Yorumyapmasonuc> call, Response<Yorumyapmasonuc> response) {

                                if (response.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Yorum yapıldı", Toast.LENGTH_LONG).show();
                                    yorumGetir(paylasim_id);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Yorum yapılamadı", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Yorumyapmasonuc> call, Throwable t) {

                                SweetAlertDialog sa = new SweetAlertDialog(profil_paylasimlari_ayrinti.this, SweetAlertDialog.WARNING_TYPE);
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


    }

    public void yorumGetir(int paylasim_id) {
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
                        profil_yorumlar_listview.setVisibility(View.VISIBLE);
                        profil_ayrıntı_yorumlar_uyari.setVisibility(View.GONE);
                        // Toast.makeText(getApplicationContext(),"Yorumalar geldi"+response.body(),Toast.LENGTH_LONG).show();
                        gelenyorumlar = response.body();
                        yorumadapter = new Yorumadapter(gelenyorumlar, profil_paylasimlari_ayrinti.this, getApplicationContext());
                        profil_yorumlar_listview.setAdapter(yorumadapter);
                        setListViewHeightBasedOnItems(profil_yorumlar_listview);

                        /////////////////////////////////////
                        progressBar.setVisibility(View.GONE);
                        ///////////////////////////   P R O G R E S S   B A R   /////////
                    }
                }

                @Override
                public void onFailure(Call<List<Yorumlarigetirsonuc>> call, Throwable t) {

                    profil_yorumlar_listview.setVisibility(View.GONE);
                    profil_ayrıntı_yorumlar_uyari.setVisibility(View.VISIBLE);

                    /////////////////////////////////////
                    progressBar.setVisibility(View.GONE);
                    ///////////////////////////   P R O G R E S S   B A R   /////////
                }
            });
        } catch (Exception e) {
            Log.e("TAG", "yorumGetir: ", e);
        }
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;

        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
