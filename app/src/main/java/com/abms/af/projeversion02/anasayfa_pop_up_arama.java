package com.abms.af.projeversion02;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abms.af.projeversion02.Adapters.Paylasimtumverileradapter;
import com.abms.af.projeversion02.Models.Homesayfasitumpaylasimveritabani;

import java.util.List;


public class anasayfa_pop_up_arama extends AppCompatActivity {

    Spinner arama_universite,arama_bolum;
    EditText arama_dersadi;
    Button arama_buton;
    String universite,bolum,dersadi;
    TextView bolum_altı_bilgilendirme,dersadi_alti_bilgilendirme;
    ArrayAdapter universite_adapter, bolum_adapter;
    String[] universite_listesi, bolum_listesi;
    Activity activity;
    List<Homesayfasitumpaylasimveritabani> tum_veriler_liste;
    Paylasimtumverileradapter paylasimtumverileradapter;
    ListView listView_homesayfasi;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa_pop_up_arama);




        DisplayMetrics d = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(d);

        int genislik = d.widthPixels;
        int yukseklik = d.heightPixels;

        getWindow().setLayout((int)(genislik*.7),(int)(yukseklik*.5));

        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.gravity = Gravity.CENTER;
        p.x = 0;
        p.y= 0;

        getWindow().setAttributes(p);

        tanımla();
        islevver();
    }

    public void tanımla()
    {
        arama_universite=findViewById(R.id.arama_universite);
        arama_bolum=findViewById(R.id.arama_bolum);
        arama_buton=findViewById(R.id.arama_buton);
        arama_dersadi=findViewById(R.id.arama_dersadi);
        bolum_altı_bilgilendirme=findViewById(R.id.arama_bolum_altı_bilgilendirme);
        listView_homesayfasi=findViewById(R.id.listview_homesayfasi);
        progressBar=findViewById(R.id.anasayfa_progress_bar);

        // Spinnerlara eleman ekleme
        universite_listesi = getResources().getStringArray(R.array.universite_listesi_arama_için);
        universite_adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, universite_listesi);

        arama_universite.setAdapter(universite_adapter);

        bolum_listesi = getResources().getStringArray(R.array.Bolum_listesi);
        bolum_adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, bolum_listesi);
        arama_bolum.setAdapter(bolum_adapter);
        //Spinnerlara eleman ekleme sonu

    }

    public void islevver()
    {


        arama_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                universite=arama_universite.getSelectedItem().toString();
                bolum=arama_bolum.getSelectedItem().toString();
                dersadi=arama_dersadi.getText().toString();

                if(arama_bolum.getSelectedItemPosition()==0)
                {

                    if (arama_bolum.getSelectedItemPosition()==0) {
                        bolum_altı_bilgilendirme.setVisibility(View.VISIBLE);

                    } else {
                        bolum_altı_bilgilendirme.setVisibility(View.GONE);
                    }
                }
                else
                {
                    if (dersadi.equals("")) {
                        dersadi="UyarıBos";
                    }
                    //Toast.makeText(getApplicationContext(), "dersadi"+dersadi, Toast.LENGTH_SHORT).show();
                    bolum_altı_bilgilendirme.setVisibility(View.GONE);
                    Intent i = new Intent();
                    i.putExtra("universite", universite);
                    i.putExtra("bolum", bolum);
                    i.putExtra("dersadi", dersadi);
                    setResult(Activity.RESULT_OK,i);
                    finish();
                }
            }
        });
    }
}
