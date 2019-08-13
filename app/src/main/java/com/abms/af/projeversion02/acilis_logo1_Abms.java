package com.abms.af.projeversion02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class acilis_logo1_Abms extends AppCompatActivity {


    private TextView text,t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acilis_logo1__abms);


        //text = findViewById(R.id.txt);
        //t = findViewById(R.id.txtt);

        Animation animasyon = AnimationUtils.loadAnimation(this,R.anim.transition);
        //text.startAnimation(animasyon);
        //t.startAnimation(animasyon);

        final Intent i = new Intent(this,acilis_logo2_deepnote.class);

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
