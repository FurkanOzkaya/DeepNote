package com.abms.af.projeversion02.Fragments;


import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abms.af.projeversion02.MainActivity;
import com.abms.af.projeversion02.Models.Pdfyuklemesonuc;
import com.abms.af.projeversion02.Models.Resimyuklemesonuc;
import com.abms.af.projeversion02.PasswordRecovery;
import com.abms.af.projeversion02.R;
import com.abms.af.projeversion02.RestApi.ManagerAll;
import com.developer.filepicker.controller.DialogSelectionListener;
import com.developer.filepicker.model.DialogConfigs;
import com.developer.filepicker.model.DialogProperties;
import com.developer.filepicker.view.FilePickerDialog;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class share_sayfasi extends Fragment {

    Map<String, RequestBody> mappdf, mapresim;
    Button yukleme_butonu;
    EditText ders_adi, acıklama;
    TextView ders_adi_uyarı, bolum_uyarı, acıklama_uyarı, DeepNoteBaslik, textView,ilkp,sonp;
    AutoCompleteTextView bolum_adi;
    String ders_string, acıklama_string, email = "";
    int bolum_string_int;
    //FloatingActionButton ekle_buton, pdf_buton, resim_buton;
    LinearLayout resimleregit, dosyalaragit, secmekismi, yollamakismi;
    Animation bounce, rtol;
    View view;
    Integer id;
    String[] bolum_listesi;
    SharedPreferences sharedPreferences;
    ProgressBar progressBar;
    FilePickerDialog dialog;
    Typeface tf1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_share_sayfasi, container, false);

        Window window = this.getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        tanimla();
        islevver();

        return view;

    }

    public void tanimla() {

        bounce = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        rtol = AnimationUtils.loadAnimation(getContext(), R.anim.righttoleft);

        secmekismi = view.findViewById(R.id.dosyasecmekismi);
        yollamakismi = view.findViewById(R.id.dosyayollamakismi);
        resimleregit = view.findViewById(R.id.resimsec);
        dosyalaragit = view.findViewById(R.id.pdfsec);
        ders_adi = view.findViewById(R.id.ders_adi);
        bolum_adi = view.findViewById(R.id.bolum_adi);
        acıklama = view.findViewById(R.id.acıklama);
        yukleme_butonu = view.findViewById(R.id.yukleme_butonu);
        ders_adi_uyarı = view.findViewById(R.id.share_sayfası_ders_uyarı);
        bolum_uyarı = view.findViewById(R.id.share_sayfası_bolum_uyarı);
        acıklama_uyarı = view.findViewById(R.id.share_sayfası_acıklama_uyarı);
        textView = (TextView) view.findViewById(R.id.textView);
        ilkp = view.findViewById(R.id.ilkparagraf);
        sonp = view.findViewById(R.id.ortaparagraf);

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("giris", 0);
        id = sharedPreferences.getInt("uye_id", 0);

        bolum_listesi = getResources().getStringArray(R.array.Bolum_listesi);
        ArrayAdapter<String> a2 = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.bolumler, R.id.bolumtextitem, bolum_listesi);
        bolum_adi.setAdapter(a2);

        progressBar = view.findViewById(R.id.share_sayafası_progress_bar);

        DeepNoteBaslik = view.findViewById(R.id.DeepNoteBaslik);
    }

    public void islevver() {

        tf1 = Typeface.createFromAsset(getActivity().getAssets(),"fonts/DamionRegular.ttf");
        DeepNoteBaslik.setTypeface(tf1);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lmgtfy.com/?q=jpg+to+pdf&s=g&t=w"));
                startActivity(browserIntent);
            }
        });

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("giris", 0);
        if (sharedPreferences.getInt("uye_id", 0) != 0) {
            email = sharedPreferences.getString("email", "");

        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().commit();
            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        resimleregit.startAnimation(bounce);
        dosyalaragit.startAnimation(rtol);
        ilkp.startAnimation(bounce);
        sonp.startAnimation(rtol);
        textView.startAnimation(rtol);

        resimleregit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yukleme_butonu.setText("Resim Yükle");
                resimGoster();
            }
        });

        dosyalaragit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yukleme_butonu.setText("Pdf Yükle");
                pdf_goster();
            }
        });

        yukleme_butonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ders_string = ders_adi.getText().toString();
                acıklama_string = acıklama.getText().toString();
                if (ders_string.equals("") || bolum_adi.getText().toString().matches("") || acıklama_string.equals("")) {
                    if (ders_string.equals("")) {
                        ders_adi_uyarı.setVisibility(View.VISIBLE);
                    } else {
                        ders_adi_uyarı.setVisibility(View.INVISIBLE);
                    }
                    if (bolum_adi.getText().toString().matches("")) {
                        bolum_uyarı.setVisibility(View.VISIBLE);
                    } else {
                        bolum_uyarı.setVisibility(View.INVISIBLE);
                    }
                    if (acıklama.getText().toString().equals("")) {
                        acıklama_uyarı.setVisibility(View.VISIBLE);
                    } else {
                        acıklama_uyarı.setVisibility(View.INVISIBLE);
                    }

                } else {
                    dosya_yukle();
                    secmekismi.setVisibility(View.VISIBLE);
                    yollamakismi.setVisibility(View.INVISIBLE);
                    ders_adi.setVisibility(View.INVISIBLE);
                    bolum_adi.setVisibility(View.INVISIBLE);
                    acıklama.setVisibility(View.INVISIBLE);
                    yukleme_butonu.setVisibility(View.INVISIBLE);
                    ders_adi_uyarı.setVisibility(View.INVISIBLE);
                    bolum_uyarı.setVisibility(View.INVISIBLE);
                    acıklama_uyarı.setVisibility(View.INVISIBLE);
                    ders_adi.setText("");
                    acıklama.setText("");
                }
            }
        });
    }

    /*
     *resimlere ulasıp secmek için yazılan fonksiyon
     *
     */
    public void resimGoster() {

        ImagePicker.Companion.with(this)
                .galleryOnly()
                .compress(500)
                .maxResultSize(1080, 1080)
                .start();
    }

    private void pdf_goster() {
        DialogProperties properties = new DialogProperties();

        // String [] extensions={"txt:pdf:"}; extensions doesnt work
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;

        dialog = new FilePickerDialog(getActivity(), properties);
        dialog.setTitle("Pdf Seçiniz");
        dialog.show();

        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {

                ders_adi.setVisibility(View.VISIBLE);
                bolum_adi.setVisibility(View.VISIBLE);
                acıklama.setVisibility(View.VISIBLE);
                yukleme_butonu.setVisibility(View.VISIBLE);
                secmekismi.setVisibility(View.INVISIBLE);
                yollamakismi.setVisibility(View.VISIBLE);

                try {
                    String yol = files[0];
                    mappdf = new HashMap<>();
                    File file = new File(yol);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("application/pdf"), file);
                    mappdf.put("file\"; filename=\"" + file.getName() + "\"", requestFile);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /*
     * resimi sunucuya yüklemek için bilgilerin aldıgı ve yüklendiği kısım
     */

    public void dosya_yukle() {

        //////////////////////////////// P R O G R E S S   B A R    //////////////////////
        progressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        ////////////////////////////////////////////////////////////////////////////////////

        if (yukleme_butonu.getText().equals("Resim Yükle")) {

            final SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Yükleniyor");
            pDialog.setCancelable(false);
            pDialog.show();

            Integer id_kullanici = id;
            String ders = ders_adi.getText().toString();
            String bolum = bolum_adi.getText().toString();
            String aciklama = acıklama.getText().toString();

            try {
                Call<Resimyuklemesonuc> a = ManagerAll.webyonet().resim_yukle(email, id_kullanici, ders, aciklama, bolum, mapresim);
                //Toast.makeText(getActivity().getApplicationContext(), (CharSequence) mapresim, Toast.LENGTH_LONG).show();

                a.enqueue(new Callback<Resimyuklemesonuc>() {
                    @Override
                    public void onResponse(Call<Resimyuklemesonuc> call, Response<Resimyuklemesonuc> response) {

                        pDialog.cancel();

                        final SweetAlertDialog sa = new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE);
                        sa.setTitleText("Başarılı");
                        sa.setContentText("Notunuz başarıyla paylaşıldı, teşekkür ederiz");
                        sa.setConfirmText("Tamam");
                        sa.show();

                        /////////////////////////////////////
                        progressBar.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        ////////////////////////////////////
                    }

                    @Override
                    public void onFailure(Call<Resimyuklemesonuc> call, Throwable t) {

                        pDialog.cancel();

                        final SweetAlertDialog sa = new SweetAlertDialog(getContext(),SweetAlertDialog.WARNING_TYPE);
                        sa.setTitleText("Dikkat");
                        sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                        sa.setConfirmText("Tamam");
                        sa.show();

                        /////////////////////////////////////
                        progressBar.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        ////////////////////////////////////
                    }
                });
            }catch (Exception e)
            {
                Log.e("TAG", "dosya_yukle: ",e );
            }

        } else if (yukleme_butonu.getText().equals("Pdf Yükle")) {

            final SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Yükleniyor");
            pDialog.setCancelable(false);
            pDialog.show();

            Integer id_kullanici = id;
            String ders = ders_adi.getText().toString();
            String bolum = bolum_adi.getText().toString();
            String aciklama = acıklama.getText().toString();
            // Toast.makeText(getActivity().getApplicationContext(),mappdf,Toast.LENGTH_LONG).show();

            try {
                Call<Pdfyuklemesonuc> ee = ManagerAll.webyonet().pdf_yukle(email, id_kullanici, ders, aciklama, bolum, mappdf);
                ee.enqueue(new Callback<Pdfyuklemesonuc>() {
                    @Override
                    public void onResponse(Call<Pdfyuklemesonuc> call, Response<Pdfyuklemesonuc> response) {

                        pDialog.cancel();

                        final SweetAlertDialog sa = new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE);
                        sa.setTitleText("Başarılı");
                        sa.setContentText("Notunuz başarıyla paylaşıldı, teşekkür ederiz");
                        sa.setConfirmText("Tamam");
                        sa.show();

                        //Toast.makeText(getActivity().getApplicationContext(), "Dosya Yuklendi"+response.body().getPdfyuklemesonuc(), Toast.LENGTH_LONG).show();

                        /////////////////////////////////////
                        progressBar.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        ////////////////////////////////////
                    }

                    @Override
                    public void onFailure(Call<Pdfyuklemesonuc> call, Throwable t) {

                        pDialog.cancel();

                        final SweetAlertDialog sa = new SweetAlertDialog(getContext(),SweetAlertDialog.WARNING_TYPE);
                        sa.setTitleText("Dikkat");
                        sa.setContentText("Bir şeyler yolunda gitmedi, internet bağlantınızı kontrol ederek tekrar deneyiniz");
                        sa.setConfirmText("Tamam");
                        sa.show();

                        /////////////////////////////////////
                        progressBar.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        ////////////////////////////////////
                    }
                });
            }catch (Exception e)
            {
                Log.e("TAG", "dosya_yukle: ",e );
            }
        }
    }

    /*
     *resimin alındıgı ve alınacak bilgiler için edittextlerin gösterildiği bölüm
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri path = data.getData();
            String uri = String.valueOf(path);
            ders_adi.setVisibility(View.VISIBLE);
            bolum_adi.setVisibility(View.VISIBLE);
            acıklama.setVisibility(View.VISIBLE);
            yukleme_butonu.setVisibility(View.VISIBLE);
            secmekismi.setVisibility(View.INVISIBLE);
            yollamakismi.setVisibility(View.VISIBLE);

            String denee = ImagePicker.Companion.getFilePath(data);

            try {
                String resim = ImagePicker.Companion.getFilePath(data);

                mapresim = new HashMap<>();

                File file = new File(resim);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                mapresim.put("file\"; filename=\"" + file.getName() + "\"", requestFile);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
