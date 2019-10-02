package com.abms.af.projeversion02.Fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abms.af.projeversion02.Adapters.Paylasimtumverileradapter;
import com.abms.af.projeversion02.MainActivity;
import com.abms.af.projeversion02.Models.Homesayfasitumpaylasimveritabani;
import com.abms.af.projeversion02.R;
import com.abms.af.projeversion02.RestApi.ManagerAll;
import com.abms.af.projeversion02.anasayfa_pop_up_arama;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
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
    LinearLayout loadnextpage,loadprevpage;
    LinearLayout home_listview_layout;
    int pageCount=0;
    int pageListSize=0;
    int rowcount=0;

    String universite;
    String bolum;
    String dersadi;
    boolean searchActive=false;

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
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
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
        loadnextpage=view.findViewById(R.id.loadPage);
        loadprevpage = view.findViewById(R.id.loadprevpage);
    }



    private void islevver() {

        // H O M E    S A Y F A S I    V E R İ    C E K M E
       loadListForHome(pageCount);


        listView_homesayfasi.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }


            @Override
            public void onScroll(AbsListView absListView, int firstvisibleitem, int visibleItemCount, int totalItem) {
                if (listView_homesayfasi.getAdapter() == null)
                {
                    return;
                }
                if (listView_homesayfasi.getAdapter().getCount() == 0)
                {
                    return;
                }
                int edge = visibleItemCount + firstvisibleitem;
                if (listView_homesayfasi.getLastVisiblePosition() == totalItem-1 && listView_homesayfasi.getChildAt(listView_homesayfasi.getChildCount() - 1).getBottom() <= listView_homesayfasi.getHeight())
                {
                    //Toast.makeText(getActivity().getApplicationContext(),"Sayfa Sonu Load:",Toast.LENGTH_LONG).show();
                    loadnextpage.setVisibility(View.VISIBLE);

                }
                else
                {
                    loadnextpage.setVisibility(View.GONE);
                }

                if ( firstvisibleitem == 0 && pageCount!=0 )
                {
                    loadprevpage.setVisibility(View.VISIBLE);
                }
                else
                {
                    loadprevpage.setVisibility(View.GONE);
                }

            }
        });

        loadnextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int page= rowcount / pageListSize;
                if (page > pageCount)
                {
                    pageCount++;
                }
                else if(page <= pageCount)
                {
                    pageCount=0;
                }
                Toast.makeText(getActivity().getApplicationContext(),"page: "+page+" pagecount: "+pageCount+" rowcount: "+rowcount + " pagelistsize: "+pageListSize,Toast.LENGTH_LONG).show();
                loadList(pageCount);
            }
        });

        loadprevpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pageCount > 0) {
                    pageCount--;
                }
                loadList(pageCount);
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
                searchActive=false;
                pageCount=0;
                home_sayfası_listview_uyarı.setVisibility(View.GONE);
                loadListForHome(pageCount);
            }
        });





    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 99 && resultCode == RESULT_OK && data != null) {

            universite=data.getStringExtra("universite");
            bolum=data.getStringExtra("bolum");
            dersadi=data.getStringExtra("dersadi");


            searchActive=true;
            loadSearchPage(0);
        }


    }

    void loadList(int pageCount)
    {
       loadnextpage.setVisibility(View.GONE);
       loadprevpage.setVisibility(View.GONE);
      if (searchActive)
      {
          loadSearchPage(pageCount);
      }
      else
      {
          loadListForHome(pageCount);
      }
        int page= rowcount / pageListSize;
        if (page > pageCount)
        {
            pageCount++;
        }
        else if(page <= pageCount)
        {
            pageCount=0;
        }
        //Toast.makeText(getActivity().getApplicationContext(),"page: "+page+" pagecount: "+pageCount+" rowcount: "+rowcount + " pagelistsize: "+pageListSize,Toast.LENGTH_LONG).show();
       loadListForHome(pageCount);


    }

    void loadListForHome(int page)
    {

        retrofit2.Call<List<Homesayfasitumpaylasimveritabani>> tumveriler = ManagerAll.webyonet().paylasimlartumugetir(getString(R.string.jsongüvenlikkod),page);
        //////////////////////////////// P R O G R E S S   B A R    //////////////////////
        progressBar.setVisibility(View.VISIBLE);


        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ////////////////////////////////////////////////////////////////////////////////////
        tumveriler.enqueue(new Callback<List<Homesayfasitumpaylasimveritabani>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Homesayfasitumpaylasimveritabani>> call, Response<List<Homesayfasitumpaylasimveritabani>> response) {
                 //Toast.makeText(getActivity().getApplicationContext(),"bilgiler geldi"+response.body(),Toast.LENGTH_SHORT).show();
                if (response.isSuccessful()) {
                   if (response.body() != null )
                   {
                       rowcount=response.body().get(0).getRowCount();
                       pageListSize=response.body().get(0).getPageListSize();
                       // Toast.makeText(getActivity().getApplicationContext(),"bilgiler geldi",Toast.LENGTH_SHORT).show();
                       tum_veriler_liste = response.body();
                       if (rowcount > 0)
                       {
                           paylasimtumverileradapter = new Paylasimtumverileradapter(tum_veriler_liste, getActivity().getApplicationContext(), getActivity());
                           listView_homesayfasi.setAdapter(paylasimtumverileradapter);
                       }
                       else
                       {
                           home_sayfası_listview_uyarı.setVisibility(View.VISIBLE);
                       }
                       /////////////////////////////////////
                       progressBar.setVisibility(View.GONE);
                       refesh_home.setRefreshing(false);
                       getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                       ///////////////////////////   P R O G R E S S   B A R   /////////
                   }
                   else
                   {

                       new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                               .setTitleText("Beklenmedik bir hata oluştu, Lütfen daha sonra tekrar deneyiniz")
                               .show();

                       //Toast.makeText(getActivity().getApplicationContext(), "Beklenmeyen bir hata olustu Tekrar Deneyiniz /n Eger Devam ederse Bildiriniz ", Toast.LENGTH_SHORT).show();

                       //////////////////////////////////////////////////////////////
                       progressBar.setVisibility(View.GONE);
                       refesh_home.setRefreshing(false);
                       getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                       /////////////////////////////////////   P R O G R E S S   B A R    /////////////////////////////////
                   }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Homesayfasitumpaylasimveritabani>> call, Throwable t) {

                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Beklenmedik bir hata oluştu, Lütfen daha sonra tekrar deneyiniz")
                        .show();

                //Toast.makeText(getActivity().getApplicationContext(), "Beklenmeyen bir hata olustu Tekrar Deneyiniz /n Eger Devam ederse Bildiriniz "+t.getMessage(), Toast.LENGTH_SHORT).show();

                //////////////////////////////////////////////////////////////
                progressBar.setVisibility(View.GONE);
                refesh_home.setRefreshing(false);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                /////////////////////////////////////   P R O G R E S S   B A R    /////////////////////////////////
            }
        });

    }

    void loadSearchPage(int pageCount)
    {


        if (universite.equals(getString(R.string.universite_listesi__arama_hepsi)))
        {
            universite="Hepsi";
        }

        // H O M E    S A Y F A S I    V E R İ    C E K M E
        Call<List<Homesayfasitumpaylasimveritabani>> f = ManagerAll.webyonet().aramagonderigetir(universite,bolum,dersadi,pageCount);
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
                    if (response.body() != null )
                    {
                        // Toast.makeText(getActivity().getApplicationContext(),"bilgiler geldi",Toast.LENGTH_SHORT).show();
                        tum_veriler_liste = response.body();
                        rowcount = response.body().get(0).getRowCount();
                        pageListSize=response.body().get(0).getPageListSize();
                       if (rowcount > 0)
                       {
                           paylasimtumverileradapter = new Paylasimtumverileradapter(tum_veriler_liste, getActivity().getApplicationContext(), getActivity());
                           listView_homesayfasi.setAdapter(paylasimtumverileradapter);
                           home_sayfası_listview_uyarı.setVisibility(View.GONE);
                       }
                       else
                       {
                           home_sayfası_listview_uyarı.setVisibility(View.VISIBLE);
                       }
                    }
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
