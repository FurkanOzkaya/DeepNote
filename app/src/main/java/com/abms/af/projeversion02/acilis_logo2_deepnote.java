package com.abms.af.projeversion02;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class acilis_logo2_deepnote extends AppCompatActivity {

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

                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}
