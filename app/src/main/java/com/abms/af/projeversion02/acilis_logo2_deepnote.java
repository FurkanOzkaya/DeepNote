package com.abms.af.projeversion02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class acilis_logo2_deepnote extends AppCompatActivity {

    private TextView textView;
    private ImageView imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acilis_logo2_deepnote);

        textView = findViewById(R.id.txt2);
        imageview = findViewById(R.id.resim2);

        Animation animasyon = AnimationUtils.loadAnimation(this,R.anim.transition);
        textView.startAnimation(animasyon);
        imageview.startAnimation(animasyon);
        final Intent i = new Intent(this,MainActivity.class);

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
