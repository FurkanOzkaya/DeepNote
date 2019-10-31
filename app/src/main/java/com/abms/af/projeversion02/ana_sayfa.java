package com.abms.af.projeversion02;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.abms.af.projeversion02.Fragments.home_sayfasi;
import com.abms.af.projeversion02.Fragments.profil_sayfasi;
import com.abms.af.projeversion02.Fragments.share_sayfasi;
import com.abms.af.projeversion02.Fragments.takip_sayfasi;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ana_sayfa extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView alt_butonlar;
    int id;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }
        tanımla();
        islevver();
    }

    public void tanımla() {
        alt_butonlar = (BottomNavigationView) findViewById(R.id.alt_butonlar);
        alt_butonlar.setOnNavigationItemSelectedListener(this);
        loadFragment(new home_sayfasi());
        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);
        if (sharedPreferences.getInt("uye_id", 0) == 0) {
            Intent girisyapma = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(girisyapma);
        }

    }

    public void islevver() {
    }

    /*
    fragmenti yüklemek için yazılan metod
     */
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.ana_sayfa_fragment, fragment)
                    .commit();
            return true;
        }

        return false;
    }

    /*
    fragmentin hangisi olacagı seciliyor
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.home_icon1:
                // Toast.makeText(getApplicationContext(), "Home sayfası", Toast.LENGTH_LONG).show();
                fragment = new home_sayfasi();
                break;
            case R.id.takip_edilenler:
                fragment = new takip_sayfasi();
                break;
            case R.id.ekle_icon1:
                // Toast.makeText(getApplicationContext(), "Ekle sayfası", Toast.LENGTH_LONG).show();
                fragment = new share_sayfasi();
                break;
            case R.id.profil_icon1:
                //  Toast.makeText(getApplicationContext(), "Profil sayfası", Toast.LENGTH_LONG).show();
                fragment = new profil_sayfasi();
                break;
        }

        return loadFragment(fragment);
    }

    @Override
    public void onBackPressed() {

        final SweetAlertDialog sa = new SweetAlertDialog(ana_sayfa.this, SweetAlertDialog.WARNING_TYPE);
        sa.setTitleText("Dikkat!");
        sa.setContentText("Çıkmak istediğinize emin misiniz?");
        sa.setConfirmText("Evet");
        sa.setCancelClickListener(null);
        sa.setCancelText("Hayır");
        sa.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                sa.cancel();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        sa.show();

    }
}