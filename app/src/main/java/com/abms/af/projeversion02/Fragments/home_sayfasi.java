package com.abms.af.projeversion02.Fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abms.af.projeversion02.Adapters.Paylasimtumverileradapter;
import com.abms.af.projeversion02.Models.Homesayfasitumpaylasimveritabani;
import com.abms.af.projeversion02.R;
import com.abms.af.projeversion02.RestApi.ManagerAll;
import com.abms.af.projeversion02.anasayfa_pop_up_arama;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class home_sayfasi extends Fragment {

    List<Homesayfasitumpaylasimveritabani> tum_veriler_liste;
    Paylasimtumverileradapter paylasimtumverileradapter;
    View view;
    ListView listView_homesayfasi;
    ImageView arama;
    ProgressBar progressBar;
    TextView home_sayfası_listview_uyarı;
    SwipeRefreshLayout refesh_home;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_home_sayfasi, container, false);
        //STATUS BARRRRR
        Window window = this.getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.homeStatus));
        }
        //STATUS BARRRRRR
        tanımla();
        islevver();
        return view;
    }


    private void tanımla() {
        listView_homesayfasi=view.findViewById(R.id.listview_homesayfasi);
        arama = view.findViewById(R.id.arama_butonu);
        progressBar=view.findViewById(R.id.anasayfa_progress_bar);
        home_sayfası_listview_uyarı=view.findViewById(R.id.home_sayfası_lisview_uyarı_mesajı);
        refesh_home=view.findViewById(R.id.home_sayfasi_refesh);

    }



    private void islevver() {

        // H O M E    S A Y F A S I    V E R İ    C E K M E
        retrofit2.Call<List<Homesayfasitumpaylasimveritabani>> tumveriler = ManagerAll.webyonet().paylasimlartumugetir(getString(R.string.jsongüvenlikkod));
        //////////////////////////////// P R O G R E S S   B A R    //////////////////////
        progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ////////////////////////////////////////////////////////////////////////////////////
        tumveriler.enqueue(new Callback<List<Homesayfasitumpaylasimveritabani>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Homesayfasitumpaylasimveritabani>> call, Response<List<Homesayfasitumpaylasimveritabani>> response) {
                // Toast.makeText(getActivity().getApplicationContext(),"bilgiler geldi"+response.body(),Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                    // Toast.makeText(getActivity().getApplicationContext(),"bilgiler geldi",Toast.LENGTH_SHORT).show();
                    tum_veriler_liste = response.body();
                    paylasimtumverileradapter = new Paylasimtumverileradapter(tum_veriler_liste, getActivity().getApplicationContext(), getActivity());
                    listView_homesayfasi.setAdapter(paylasimtumverileradapter);

                    /////////////////////////////////////
                    progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    ///////////////////////////   P R O G R E S S   B A R   /////////
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Homesayfasitumpaylasimveritabani>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Beklenmeyen bir hata olustu Tekrar Deneyiniz /n Eger Devam ederse Bildiriniz "+t.getMessage(), Toast.LENGTH_SHORT).show();

                //////////////////////////////////////////////////////////////
                progressBar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                /////////////////////////////////////   P R O G R E S S   B A R    /////////////////////////////////
            }
        });




        //P O P   U P    F O N K S İ Y O N U N U
        arama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplication(), anasayfa_pop_up_arama.class);
                startActivityForResult(i,99);
            }
        });






        //   R E F E S H


        refesh_home.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh() {
                retrofit2.Call<List<Homesayfasitumpaylasimveritabani>> tumveriler = ManagerAll.webyonet().paylasimlartumugetir(getString(R.string.jsongüvenlikkod));
                //////////////////////////////// P R O G R E S S   B A R    //////////////////////
                progressBar.setVisibility(View.VISIBLE);
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                ////////////////////////////////////////////////////////////////////////////////////
                tumveriler.enqueue(new Callback<List<Homesayfasitumpaylasimveritabani>>() {
                    @Override
                    public void onResponse(retrofit2.Call<List<Homesayfasitumpaylasimveritabani>> call, Response<List<Homesayfasitumpaylasimveritabani>> response) {
                        // Toast.makeText(getActivity().getApplicationContext(),"bilgiler geldi"+response.body(),Toast.LENGTH_SHORT).show();
                        if (response.isSuccessful()) {
                            // Toast.makeText(getActivity().getApplicationContext(),"bilgiler geldi",Toast.LENGTH_SHORT).show();
                            tum_veriler_liste = response.body();
                            paylasimtumverileradapter = new Paylasimtumverileradapter(tum_veriler_liste, getActivity().getApplicationContext(), getActivity());
                            listView_homesayfasi.setAdapter(paylasimtumverileradapter);

                            /////////////////////////////////////
                            progressBar.setVisibility(View.GONE);
                            refesh_home.setRefreshing(false);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            ///////////////////////////   P R O G R E S S   B A R   /////////
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<List<Homesayfasitumpaylasimveritabani>> call, Throwable t) {
                        Toast.makeText(getActivity().getApplicationContext(), "Beklenmeyen bir hata olustu Tekrar Deneyiniz /n Eger Devam ederse Bildiriniz "+t.getMessage(), Toast.LENGTH_SHORT).show();

                        //////////////////////////////////////////////////////////////
                        progressBar.setVisibility(View.GONE);
                        refesh_home.setRefreshing(false);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        /////////////////////////////////////   P R O G R E S S   B A R    /////////////////////////////////
                    }
                });

            }
        });





    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 99 && resultCode == RESULT_OK && data != null) {

            String universite=data.getStringExtra("universite");
            String bolum=data.getStringExtra("bolum");
            String dersadi=data.getStringExtra("dersadi");

            // H O M E    S A Y F A S I    V E R İ    C E K M E
            Call<List<Homesayfasitumpaylasimveritabani>> f = ManagerAll.webyonet().aramagonderigetir(universite,bolum,dersadi);
            //////////////////////////////// P R O G R E S S   B A R    //////////////////////
            progressBar.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            ////////////////////////////////////////////////////////////////////////////////////
            f.enqueue(new Callback<List<Homesayfasitumpaylasimveritabani>>() {
                @Override
                public void onResponse(retrofit2.Call<List<Homesayfasitumpaylasimveritabani>> call, Response<List<Homesayfasitumpaylasimveritabani>> response) {
                    //Toast.makeText(getActivity().getApplicationContext(),"bilgiler geldi"+response.body(),Toast.LENGTH_SHORT).show();
                    if(response.body().isEmpty())
                    {
                        home_sayfası_listview_uyarı.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        home_sayfası_listview_uyarı.setVisibility(View.GONE);
                    }
                     if (response.isSuccessful()) {
                        // Toast.makeText(getActivity().getApplicationContext(),"bilgiler geldi",Toast.LENGTH_SHORT).show();
                        tum_veriler_liste = response.body();
                        paylasimtumverileradapter = new Paylasimtumverileradapter(tum_veriler_liste, getActivity().getApplicationContext(), getActivity());
                        listView_homesayfasi.setAdapter(paylasimtumverileradapter);

                        /////////////////////////////////////
                        progressBar.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        ///////////////////////////   P R O G R E S S   B A R   /////////
                    }

                }

                @Override
                public void onFailure(retrofit2.Call<List<Homesayfasitumpaylasimveritabani>> call, Throwable t) {
                    Toast.makeText(getActivity().getApplicationContext(), "Beklenmeyen bir hata olustu Tekrar Deneyiniz /n Eger Devam ederse Bildiriniz "+t.getMessage(), Toast.LENGTH_SHORT).show();

                    //////////////////////////////////////////////////////////////
                    progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    /////////////////////////////////////   P R O G R E S S   B A R    /////////////////////////////////
                }
            });
        }


    }



}
