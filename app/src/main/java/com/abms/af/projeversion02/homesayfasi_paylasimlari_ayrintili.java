package com.abms.af.projeversion02;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.abms.af.projeversion02.Models.Sikayetetmesonuc;
import com.abms.af.projeversion02.Models.Yorumlarigetirsonuc;
import com.abms.af.projeversion02.Models.Yorumyapmasonuc;
import com.abms.af.projeversion02.RestApi.ManagerAll;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class homesayfasi_paylasimlari_ayrintili extends AppCompatActivity {

    TextView  ayrıntili_indirme,ayrintili_ad_soyad, ayrintili_universite, ayrintili_bolum, ayrintili_ders, ayrintili_aciklama,listview_yorumlar_uyarı,ayrıntılı_sikayet_et;
    ImageView ayrıntılı_resim;
    ImageButton ayrıntı_yorum_yapmabutonu;
    String id_kullanici_string, paylasim_id_string, ad_soyad_string, universite_string, bolum_string, ders_string, aciklama_string, dosyayolu_string, dosyaturu_string,profilfoto_string;
    WebView Pdfview;
    CircularImageView profil_foto;
    EditText ayrıntı_yorum;
    ListView listview_yorumlar;
    LinearLayout other_profil_kullanici;
    List<Yorumlarigetirsonuc> gelenyorumlar;
    Yorumadapter yorumadapter;
    ProgressBar progressBar;
    Button openpdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homesayfasi_paylasimlari_ayrintili);

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

    }

    public void tanımla() {

        ayrintili_ad_soyad = findViewById(R.id.ayrıntı_homesayfasi_ad_soyad);
        ayrintili_universite = findViewById(R.id.ayrıntı_homesayfasi_universite);
        ayrintili_bolum=findViewById(R.id.ayrıntı_homesayfasi_bolum);
        ayrintili_ders=findViewById(R.id.ayrıntı_homesayfasi_ders);
        ayrintili_aciklama = findViewById(R.id.ayrıntı_homesayfasi_aciklama);
        ayrıntılı_resim = findViewById(R.id.ayrıntı_homesayfasi_resim);
        Pdfview = findViewById(R.id.ayrıntı_homesayfasi_pdf_webview);
        openpdf = findViewById(R.id.openpdf);
        profil_foto=findViewById(R.id.ayrıntı_homesayfası_profilfotosu);
        ayrıntı_yorum=findViewById(R.id.ayrıntı_homesayfasi_yorum);
        ayrıntı_yorum_yapmabutonu=findViewById(R.id.ayrıntı_homesayfasi_yorum_buton);
        listview_yorumlar=findViewById(R.id.ayrıntı_homesayfasi_yorum_listview);
        ayrıntili_indirme=findViewById(R.id.ayrıntı_homesayfasi_indirme);

        progressBar=findViewById(R.id.home_ayrıntı_progressbar);

        /////////// profil acmak için//////////////////
        other_profil_kullanici=findViewById(R.id.home_sayfası_profil_gonderme);
        ///////////////////////////////////
        /////////////////////////////
        ayrıntılı_sikayet_et=findViewById(R.id.ayrıntı_homesayfasi_sikayet_et);
        //////////////////////////////////////
        listview_yorumlar_uyarı=findViewById(R.id.yorumlar_lisview_uyarı);




        ayrintili_ad_soyad.setText(ad_soyad_string);
        ayrintili_universite.setText(universite_string);
        ayrintili_bolum.setText(bolum_string);
        ayrintili_ders.setText(ders_string);
        ayrintili_aciklama.setText(aciklama_string);

        if (profilfoto_string.equals("default"))
        {
            Picasso.get().load(R.drawable.main_activity_profil).resize(200,200).into(profil_foto);
        }
        else
        {
            ///////////////////////////////////
            Picasso.get().load(getString(R.string.site_adresi)+profilfoto_string).resize(200,200).error(R.drawable.main_activity_profil).into(profil_foto);
            /////////////////////////////////////
        }
    }


    public void islevver() {
        //Toast.makeText(getApplicationContext(),dosyaturu_string,Toast.LENGTH_LONG).show();
        //Log.i("TAG", "islevver: "+dosyayolu_string);
        //Toast.makeText(getApplicationContext(),dosyayolu_string,Toast.LENGTH_LONG).show();

        openpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), pdfViewer.class);
                intent.putExtra("url",getString(R.string.site_adresi)+dosyayolu_string);
                startActivity(intent);
            }
        });

        if (dosyaturu_string.equals("pdf")) {
            ayrıntılı_resim.setVisibility(View.GONE);
            Pdfview.setVisibility(View.VISIBLE);

            Pdfview.loadUrl("https://docs.google.com/gview?embedded=true&url="+getString(R.string.site_adresi)+dosyayolu_string);



        } else {
            ayrıntılı_resim.setVisibility(View.VISIBLE);
            Log.i("TAG", "islevver: "+dosyayolu_string);
            Picasso.get().load(getString(R.string.site_adresi)+dosyayolu_string).resize(200, 200).error(R.drawable.ic_launcher_background).into(ayrıntılı_resim);
        }



        ayrıntılı_sikayet_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(homesayfasi_paylasimlari_ayrintili.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("UYARI")
                        .setMessage("Şikayet etmek istediğinize emin misiniz?")
                        .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Integer paylasımid=Integer.parseInt(paylasim_id_string);
                                Call<Sikayetetmesonuc> s= ManagerAll.webyonet().sikayetet(paylasımid);
                                s.enqueue(new Callback<Sikayetetmesonuc>() {
                                    @Override
                                    public void onResponse(Call<Sikayetetmesonuc> call, Response<Sikayetetmesonuc> response) {

                                        Toast.makeText(getApplicationContext(),response.body().getSikayetetmesonuc(),Toast.LENGTH_LONG).show();

                                        ayrıntılı_sikayet_et.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onFailure(Call<Sikayetetmesonuc> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(),"Şikayet edilemedi",Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        }).setNegativeButton("Hayır", null).show();
            }
        });



        final int paylasim_id=Integer.valueOf(paylasim_id_string);
        ayrıntı_yorum_yapmabutonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String yorum=ayrıntı_yorum.getText().toString();
                ayrıntı_yorum.setText("");
                int id = 0;
                SharedPreferences sharedPreferences;
                sharedPreferences =getApplicationContext().getSharedPreferences("giris",0);
                if(sharedPreferences.getInt("uye_id",0) != 0)
                {
                   id=sharedPreferences.getInt("uye_id",0);
                }
                else
                {
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.clear().commit();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);

                }
        ////////// Y O R U M   Y A P M A   K I S M I
                Call<Yorumyapmasonuc> x= ManagerAll.webyonet().yorumyap(id,paylasim_id,yorum);
                x.enqueue(new Callback<Yorumyapmasonuc>() {
                    @Override
                    public void onResponse(Call<Yorumyapmasonuc> call, Response<Yorumyapmasonuc> response) {

                        if (response.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Yorum yapıldı",Toast.LENGTH_LONG).show();
                            //////////// Y O R U M  G E T İ R M E   Y O R U M   Y A P T I K T A N  S O N R A

                            final Call<List<Yorumlarigetirsonuc>> yorumgetir=ManagerAll.webyonet().yorumgetir(paylasim_id);
                            //////////////////////////////// P R O G R E S S   B A R    //////////////////////
                            progressBar.setVisibility(View.VISIBLE);
                            ////////////////////////////////////////////////////////////////////////////////////
                            yorumgetir.enqueue(new Callback<List<Yorumlarigetirsonuc>>() {
                                @Override
                                public void onResponse(Call<List<Yorumlarigetirsonuc>> call, Response<List<Yorumlarigetirsonuc>> response) {
                                    if (response.isSuccessful())
                                    {
                                        listview_yorumlar.setVisibility(View.VISIBLE);
                                        listview_yorumlar_uyarı.setVisibility(View.GONE);
                                        //Toast.makeText(getApplicationContext(),"Yorumalar geldi"+response.body(),Toast.LENGTH_LONG).show();
                                        gelenyorumlar=response.body();
                                        yorumadapter=new Yorumadapter(gelenyorumlar,homesayfasi_paylasimlari_ayrintili.this,getApplicationContext());
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
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Yorum yapılamadı",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Yorumyapmasonuc> call, Throwable t) {

                        Toast.makeText(getApplicationContext(),"Yorum yapılamadı"+t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });




            }
        });



        //////////// Y O R U M  G E T İ R M E

        final Call<List<Yorumlarigetirsonuc>> yorumgetir=ManagerAll.webyonet().yorumgetir(paylasim_id);
        //////////////////////////////// P R O G R E S S   B A R    //////////////////////
        progressBar.setVisibility(View.VISIBLE);
        ////////////////////////////////////////////////////////////////////////////////////
        yorumgetir.enqueue(new Callback<List<Yorumlarigetirsonuc>>() {
            @Override
            public void onResponse(Call<List<Yorumlarigetirsonuc>> call, Response<List<Yorumlarigetirsonuc>> response) {
                if (response.isSuccessful())
                {
                    listview_yorumlar.setVisibility(View.VISIBLE);
                    listview_yorumlar_uyarı.setVisibility(View.GONE);
                   // Toast.makeText(getApplicationContext(),"Yorumalar geldi"+response.body(),Toast.LENGTH_LONG).show();
                    gelenyorumlar=response.body();
                    yorumadapter=new Yorumadapter(gelenyorumlar,homesayfasi_paylasimlari_ayrintili.this,getApplicationContext());
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



        other_profil_kullanici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent others=new Intent(homesayfasi_paylasimlari_ayrintili.this,others_profil_sayfasi.class);
                others.putExtra("id_kullanici",id_kullanici_string);
                others.putExtra("paylasim_id",paylasim_id_string);
                others.putExtra("ad_soyad",ad_soyad_string);
                others.putExtra("universite",universite_string);
                others.putExtra("bolum",bolum_string);
                others.putExtra("ders",ders_string);
                others.putExtra("aciklama",aciklama_string);
                others.putExtra("dosyayolu",dosyayolu_string);
                others.putExtra("dosyaturu",dosyaturu_string);
                others.putExtra("profilfoto",profilfoto_string);
                finish();
                homesayfasi_paylasimlari_ayrintili.this.startActivity(others);
            }
        });


        ayrıntili_indirme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ayrıntili_indirme.setVisibility(View.GONE);
/*
                downloadManager= (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri=Uri.parse(getString(R.string.site_adresi)+dosyayolu_string);
                DownloadManager.Request request=new DownloadManager.Request(uri);
                request.setTitle("My File");
                request.setDescription("Downloading");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationUri(Uri.parse("file://" + folderName + "/myfile.mp3"));
                downloadmanager.enqueue(request);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long reference=downloadManager.enqueue(request);
*/

                Call<ResponseBody> down=ManagerAll.webyonet().indirr(dosyayolu_string);
                down.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(homesayfasi_paylasimlari_ayrintili.this, "Dosya İndiriliyor Dosya Yöneticisinden Bulabilirsiniz", Toast.LENGTH_SHORT).show();

                        Boolean dosya=dosyayıdiskeyaz(response.body());
                        if (dosya==true)
                        {
                            new AlertDialog.Builder(homesayfasi_paylasimlari_ayrintili.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Dosya İndirme Bilgisi")
                            .setMessage("İndirme Başarılı!" +"\n"+
                                    " dosya yöneticinizden indirdiklerinizi bulabilirsiniz!!")
                            .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();

                        }
                        else
                        {
                            new AlertDialog.Builder(homesayfasi_paylasimlari_ayrintili.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Dosya İndirme Bilgisi")
                                .setMessage("İndirme Başarısız! " +"\n"+
                                        "tekrar deneyiniz, Sorun devam ederse bildiriniz!!")
                                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(homesayfasi_paylasimlari_ayrintili.this, "Hata olustu", Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(homesayfasi_paylasimlari_ayrintili.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Dosya İndirme Bilgisi")
                                .setMessage("İndirme Başarısız! " +"\n"+
                                        "tekrar deneyiniz, Sorun devam ederse bildiriniz!!")
                                .setNeutralButton("Tamam",null);
                    }
                });

            }
        });

    }
/*

    @Override
    public void onBackPressed() {


                        Intent intent = new Intent(homesayfasi_paylasimlari_ayrintili.this,ana_sayfa.class);
                        startActivity(intent);

    }
*/

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int)px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
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
        if (dosyaturu_string.equals("pdf"))
        {
            fileName=universite_string+ders_string+currentDateandTime+".pdf";
             dir = new File(root.getAbsolutePath() + "/DeepNote_pdf");
        }
        else
        {
            fileName=universite_string+ders_string+currentDateandTime+".jpeg";
            dir = new File(root.getAbsolutePath() + "/DeepNote_resim");
        }
        if(dir.exists() == false){
            dir.mkdirs();
        }

        File file = new File(dir,fileName);
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
            Log.i("TAG", "dosyayıdiskeyaz: "+e.getMessage());
            Toast.makeText(this, "hata"+e.getMessage(), Toast.LENGTH_SHORT).show();
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
        Log.i("TAG", "dosyayıdiskeyaz: "+e.getMessage());
        Toast.makeText(this, "hata"+e.getMessage(), Toast.LENGTH_SHORT).show();
        return false;
    }
}
}


