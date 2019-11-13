package com.abms.af.projeversion02;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.abms.af.projeversion02.Models.GelistirmeDurumu;
import com.abms.af.projeversion02.RestApi.ManagerAll;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class acilis_logo2_deepnote extends AppCompatActivity {

    private static final String LOG_TAG = "Otomatik internet Kontrol¸";
    private NetworkChangeReceiver receiver;//Network dinleyen receiver objemizin referans˝

    private TextView textView;
    private ImageView imageview;
    Typeface tf1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acilis_logo2_deepnote);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        //Receiverımızı register ediyoruz
        //Yani Çalıştırıyoruz
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);

        textView = findViewById(R.id.txt2);
        imageview = findViewById(R.id.resim2);

        tf1 = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/DamionRegular.ttf");
        textView.setTypeface(tf1);

        Animation animasyon = AnimationUtils.loadAnimation(this,R.anim.transition);
        textView.startAnimation(animasyon);
        imageview.startAnimation(animasyon);
        final Intent i = new Intent(this,IntroActivity.class);

        Thread timer = new Thread(){

            public void run(){
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {

                    Call<GelistirmeDurumu> request = ManagerAll.webyonet().GelistirmeDurumu(getString(R.string.jsongüvenlikkod));
                    request.enqueue(new Callback<GelistirmeDurumu>() {
                        @Override
                        public void onResponse(Call<GelistirmeDurumu> call, Response<GelistirmeDurumu> response) {

                            if (response.body().getDurum().equals("1"))
                            {
                                Intent i = new Intent(acilis_logo2_deepnote.this,GelistirmeAsamasinda.class);
                                startActivity(i);
                            }
                            else
                            {
                                startActivity(i);
                                finish();
                            }

                        }

                        @Override
                        public void onFailure(Call<GelistirmeDurumu> call, Throwable t) {

                        }
                    });

                }
            }
        };
        timer.start();
    }

    @Override
    protected void onDestroy() { //Activity Kapatıldığı zaman receiver durduralacak.Uygulama arka plana alındığı zamanda receiver çalışmaya devam eder
        //Log.v(LOG_TAG, "onDestory");
        super.onDestroy();

        unregisterReceiver(receiver);//receiver durduruluyor

    }

}
