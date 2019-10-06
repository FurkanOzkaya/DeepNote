package com.abms.af.projeversion02;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.abms.af.projeversion02.Models.Profilfotosilmesonuc;
import com.abms.af.projeversion02.Models.Profilfotoyuklemesonuc;
import com.abms.af.projeversion02.RestApi.ManagerAll;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class profil_resmi_pop_up extends AppCompatActivity {

    Button profil_foto_yukleme, profil_foto_silme;
    Integer id_kullanici;
    Map<String, RequestBody> profilfoto;
    SharedPreferences sharedPreferences;
    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_resmi_pop_up);

        // P O P   U P   A C I L M A S I
        DisplayMetrics d = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(d);

        int genislik = d.widthPixels;
        int yukseklik = d.heightPixels;

        getWindow().setLayout((int) (genislik * .7), (int) (yukseklik * .5));

        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.gravity = Gravity.CENTER;
        p.x = 0;
        p.y = 0;
        getWindow().setAttributes(p);
        //////////////////////
        tanımla();
        islevver();

    }

    public void tanımla() {
        profil_foto_yukleme = findViewById(R.id.profil_resmi_yukleme);
        profil_foto_silme = findViewById(R.id.profil_resmi_silme);
        Bundle extras = getIntent().getExtras();
        id_kullanici = extras.getInt("id_kullanici");

    }

    public void islevver() {
        sharedPreferences = getApplicationContext().getSharedPreferences("giris", 0);
        if (sharedPreferences.getInt("uye_id", 0) != 0) {
            email = sharedPreferences.getString("email", "");

        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().commit();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        profil_foto_yukleme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resimsec();

            }
        });

        profil_foto_silme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profil_resmi_pop_up.this.finish();
              try {
                  Call<Profilfotosilmesonuc> fotosill = ManagerAll.webyonet().fotosil(email, id_kullanici);
                  fotosill.enqueue(new Callback<Profilfotosilmesonuc>() {
                      @Override
                      public void onResponse(Call<Profilfotosilmesonuc> call, Response<Profilfotosilmesonuc> response) {
                          if (response.isSuccessful()) {
                              Toast.makeText(getApplicationContext(), "Profil Fotoğrafınız Silindi ", Toast.LENGTH_LONG).show();
                          }
                      }

                      @Override
                      public void onFailure(Call<Profilfotosilmesonuc> call, Throwable t) {

                          final SweetAlertDialog sa = new SweetAlertDialog(profil_resmi_pop_up.this,SweetAlertDialog.WARNING_TYPE);
                          sa.setTitleText("Dikkat");
                          sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                          sa.setConfirmText("Tamam");
                          sa.show();
                      }
                  });
              }catch (Exception e)
              {
                  Log.e("TAG", "onClick: ",e );
              }
            }
        });
    }

    public void resimsec() {
        ImagePicker.Companion.with(this)
                .galleryOnly()
                .crop(1f, 1f)                //Crop Square image(Optional)
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        profil_resmi_pop_up.this.finish();

        if (requestCode == ImagePicker.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {
                Toast.makeText(getApplicationContext(),"foto secildi ",Toast.LENGTH_LONG).show();

                String resim = ImagePicker.Companion.getFilePath(data);
                profilfoto = new HashMap<>();

                File file = new File(resim);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                profilfoto.put("file\"; filename=\"" + file.getName() + "\"", requestFile);

                Call<Profilfotoyuklemesonuc> c = ManagerAll.webyonet().ppyukle(email, id_kullanici, profilfoto);
                c.enqueue(new Callback<Profilfotoyuklemesonuc>() {
                    @Override
                    public void onResponse(Call<Profilfotoyuklemesonuc> call, Response<Profilfotoyuklemesonuc> response) {
                        if (response.body().getProfilfotoyuklemesonuc().equals("Dosya eklendi")) {
                            Toast.makeText(getApplicationContext(), "Profil Resminiz değiştirildi.", Toast.LENGTH_LONG).show();
                        } else {
                            final SweetAlertDialog sa = new SweetAlertDialog(profil_resmi_pop_up.this, SweetAlertDialog.WARNING_TYPE);
                            sa.setTitleText("Dikkat");
                            sa.setContentText("Profil fotoğrafı eklenirken bir hata oluştu lütfen daha sonra tekrar deneyiniz");
                            sa.setConfirmText("Tamam");
                            sa.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Profilfotoyuklemesonuc> call, Throwable t) {

                        final SweetAlertDialog sa = new SweetAlertDialog(profil_resmi_pop_up.this,SweetAlertDialog.WARNING_TYPE);
                        sa.setTitleText("Dikkat");
                        sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                        sa.setConfirmText("Tamam");
                        sa.show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
