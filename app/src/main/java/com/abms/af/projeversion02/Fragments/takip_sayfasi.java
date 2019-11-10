package com.abms.af.projeversion02.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
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
import com.abms.af.projeversion02.Adapters.Takipeilenlerinverileriadapter;
import com.abms.af.projeversion02.MainActivity;
import com.abms.af.projeversion02.Models.Homesayfasitumpaylasimveritabani;
import com.abms.af.projeversion02.Models.TakipedilenlerinVerileri;
import com.abms.af.projeversion02.R;
import com.abms.af.projeversion02.RestApi.ManagerAll;
import com.abms.af.projeversion02.ana_sayfa;
import com.abms.af.projeversion02.anasayfa_pop_up_arama;

import java.sql.SQLTransactionRollbackException;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.supercharge.shimmerlayout.ShimmerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class takip_sayfasi extends Fragment {

    List<TakipedilenlerinVerileri> tum_veriler_liste;
    Takipeilenlerinverileriadapter takipedilenlerinverileriadapter;
    View view;
    ListView listview_takipsayfasi;
    ImageView Gonderi_yok;
    ProgressBar progressBar;
    TextView takip_sayfası_listview_uyarı, DeepNoteBaslik, altsatıricin;
    SwipeRefreshLayout refesh_home;
    LinearLayout loadnextpage;
    int pageCount = 0;
    int pageListSize = 0;
    int rowcount = 0;
    int id_takipeden = 0;
    SharedPreferences sharedPreferences;
    String email = "";
    Typeface tf1;
    ShimmerLayout shimmerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_takip_sayfasi, container, false);
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
        listview_takipsayfasi = view.findViewById(R.id.listview_takipsayfasi);
        progressBar = view.findViewById(R.id.anasayfa_progress_bar);
        takip_sayfası_listview_uyarı = view.findViewById(R.id.takip_sayfası_lisview_uyarı_mesajı);
        refesh_home = view.findViewById(R.id.home_sayfasi_refesh);
        loadnextpage = view.findViewById(R.id.loadPage);
        shimmerLayout = view.findViewById(R.id.shimmer_layout);
        DeepNoteBaslik = view.findViewById(R.id.DeepNoteBaslik);
        Gonderi_yok = view.findViewById(R.id.gonderi_yok);
        altsatıricin = view.findViewById(R.id.altsatıricin);
    }


    private void islevver() {

        tf1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/DamionRegular.ttf");
        DeepNoteBaslik.setTypeface(tf1);

        // H O M E    S A Y F A S I    V E R İ    C E K M E
        loadListForHome(pageCount);


        listview_takipsayfasi.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }


            @Override
            public void onScroll(AbsListView absListView, int firstvisibleitem, int visibleItemCount, int totalItem) {
                if (listview_takipsayfasi.getAdapter() == null) {
                    return;
                }
                if (listview_takipsayfasi.getAdapter().getCount() == 0) {
                    return;
                }
                int edge = visibleItemCount + firstvisibleitem;
                if (listview_takipsayfasi.getLastVisiblePosition() == totalItem - 1 && listview_takipsayfasi.getChildAt(listview_takipsayfasi.getChildCount() - 1).getBottom() <= listview_takipsayfasi.getHeight()) {
                    //Toast.makeText(getActivity().getApplicationContext(),"Sayfa Sonu Load:",Toast.LENGTH_LONG).show();
                    loadnextpage.setVisibility(View.VISIBLE);

                } else {
                    loadnextpage.setVisibility(View.GONE);
                }


            }
        });

        loadnextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int page = rowcount / pageListSize;
                if (page > pageCount) {
                    pageCount++;
                } else if (page <= pageCount) {
                    pageCount = 0;
                    tum_veriler_liste = null;
                    // Toast.makeText(getActivity().getApplicationContext(),"Bütün gönderiler görüldü. Başa Dönülüyor.",Toast.LENGTH_LONG).show();
                }
                loadList(pageCount);
            }
        });


        //   R E F E S H
        refesh_home.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageCount = 0;
                tum_veriler_liste = null;
                takip_sayfası_listview_uyarı.setVisibility(View.GONE);
                loadListForHome(pageCount);
            }
        });
    }


    void loadList(int pageCount) {
        loadnextpage.setVisibility(View.GONE);
        loadListForHome(pageCount);
    }

    void loadListForHome(int page) {

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("giris", 0);
        if (sharedPreferences.getInt("uye_id", 0) != 0) {
            email = sharedPreferences.getString("email", "");
            id_takipeden = sharedPreferences.getInt("uye_id", 0);

        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().commit();
            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        try {
            retrofit2.Call<List<TakipedilenlerinVerileri>> tumveriler = ManagerAll.webyonet().Takipedilenlerinverileri(email, getString(R.string.jsongüvenlikkod), page, id_takipeden);
            //////////////////////////////// P R O G R E S S   B A R    //////////////////////
            shimmerLayout.setVisibility(View.VISIBLE);
            shimmerLayout.startShimmerAnimation();
            listview_takipsayfasi.setVisibility(View.GONE);
            ////////////////////////////////////////////////////////////////////////////////////
            tumveriler.enqueue(new Callback<List<TakipedilenlerinVerileri>>() {
                @Override
                public void onResponse(retrofit2.Call<List<TakipedilenlerinVerileri>> call, Response<List<TakipedilenlerinVerileri>> response) {
                    //Toast.makeText(getActivity().getApplicationContext(),"bilgiler geldi"+response.body(),Toast.LENGTH_SHORT).show();
                    //////////////////////////////
                    shimmerLayout.setVisibility(View.GONE);
                    shimmerLayout.stopShimmerAnimation();
                    listview_takipsayfasi.setVisibility(View.VISIBLE);
                    refesh_home.setRefreshing(false);
                    //////////////////////////////////////////////
                    if (response.isSuccessful()) {

                        if (response.body() != null) {
                            if (response.body().size() > 0) {
                                rowcount = response.body().get(0).getRowCount();
                                pageListSize = response.body().get(0).getPageListSize();
                                // Toast.makeText(getActivity().getApplicationContext(),"bilgiler geldi",Toast.LENGTH_SHORT).show();
                                if (response.body().size() > 0) {
                                    if (tum_veriler_liste == null) {
                                        tum_veriler_liste = response.body();
                                    } else {

                                        for (int i = 0; i < response.body().size(); i++) {
                                            tum_veriler_liste.add(response.body().get(i));
                                        }
                                    }
                                }

                                if (rowcount >= 0) {
                                    try {
                                        takipedilenlerinverileriadapter = new Takipeilenlerinverileriadapter(tum_veriler_liste, getActivity().getApplicationContext(), getActivity());
                                        //paylasimtumverileradapter = new Paylasimtumverileradapter(tum_veriler_liste, getActivity().getApplicationContext(), getActivity());
                                        listview_takipsayfasi.setAdapter(takipedilenlerinverileriadapter);
                                    } catch (Exception e) {
                                        Log.e("TAG", "loadSearchPage: " + e);
                                    }


                                } else {
                                    takip_sayfası_listview_uyarı.setVisibility(View.VISIBLE);
                                    Gonderi_yok.setVisibility(View.VISIBLE);
                                    altsatıricin.setVisibility(View.VISIBLE);
                                }
                                int a = pageCount * pageListSize;
                                listview_takipsayfasi.setSelection(a);
                            }
                            else {
                                takip_sayfası_listview_uyarı.setVisibility(View.VISIBLE);
                                Gonderi_yok.setVisibility(View.VISIBLE);
                                altsatıricin.setVisibility(View.VISIBLE);
                            }
                        }
                        else {

                            SweetAlertDialog sa = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                            sa.setTitleText("Dikkat!");
                            sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                            sa.setConfirmText("Tamam");
                            sa.show();

                        }
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<List<TakipedilenlerinVerileri>> call, Throwable t) {

                    SweetAlertDialog sa = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                    sa.setTitleText("Dikkat!");
                    sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                    sa.setConfirmText("Tamam");
                    sa.show();
                    //////////////////////////////
                    shimmerLayout.setVisibility(View.GONE);
                    shimmerLayout.stopShimmerAnimation();
                    refesh_home.setRefreshing(false);
                    // listView_homesayfasi.setVisibility(View.VISIBLE);
                    //////////////////////////////////////////////
                    /////////////////////////////////////   P R O G R E S S   B A R    /////////////////////////////////
                }
            });
        } catch (Exception e) {
            Log.e("TAG", "loadSearchPage: " + e);
        }

    }
}
