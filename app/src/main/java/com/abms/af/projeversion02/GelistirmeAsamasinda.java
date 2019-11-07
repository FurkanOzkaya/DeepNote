package com.abms.af.projeversion02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.abms.af.projeversion02.Models.GelistirmeDurumu;
import com.abms.af.projeversion02.RestApi.ManagerAll;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GelistirmeAsamasinda extends AppCompatActivity {

    TextView Metin;
    ImageView Resim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gelistirme_asamasinda);

        Metin = (TextView) findViewById(R.id.GelistirmeAsamasindaMetin);
        Resim = (ImageView) findViewById(R.id.GelistirmeAsamasindaResim);

        Call<GelistirmeDurumu> request = ManagerAll.webyonet().GelistirmeDurumu(getString(R.string.jsongüvenlikkod));
        request.enqueue(new Callback<GelistirmeDurumu>() {
            @Override
            public void onResponse(Call<GelistirmeDurumu> call, Response<GelistirmeDurumu> response) {

                Picasso.get().load(getString(R.string.site_adresi) + response.body().getResim()).into(Resim);
                Metin.setText(response.body().getMetin());
            }

            @Override
            public void onFailure(Call<GelistirmeDurumu> call, Throwable t) {

            }
        });

    }
}
