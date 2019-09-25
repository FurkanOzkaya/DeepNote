package com.abms.af.projeversion02;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordRecoveryPopup_2 extends AppCompatActivity {

    SharedPreferences sharedPref;
    EditText Kod;
    Button Send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery_popup_2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DisplayMetrics d = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(d);

        int genislik = d.widthPixels;
        int yukseklik = d.heightPixels;

        getWindow().setLayout((int)(genislik*.9),(int)(yukseklik*.7));

        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.gravity = Gravity.TOP;
        p.x = 0;
        p.y= 250;

        getWindow().setAttributes(p);

        tanımla();
        islevver();

        //sharedPref = getApplicationContext().getSharedPreferences("sifre",0);
        //String SharedKod = sharedPref.getString("Kod", "Kayit Yok");
        //Toast.makeText(getApplicationContext(), SharedKod , Toast.LENGTH_LONG).show();

    }

    public void tanımla()
    {
        Kod = (EditText) findViewById(R.id.Kod);
        Send = (Button) findViewById(R.id.Send);

    }

    public void islevver()
    {

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPref = getApplicationContext().getSharedPreferences("sifre",0);
                String SharedKod = sharedPref.getString("Kod", "Kayit Yok");
                String kod = Kod.getText().toString();

                if (SharedKod.equals(kod))
                {
                    Intent intent = new Intent(getApplicationContext(), PasswordRecoveryPopup_3.class);
                    startActivity(intent);

                }

            }
        });

    }

}
