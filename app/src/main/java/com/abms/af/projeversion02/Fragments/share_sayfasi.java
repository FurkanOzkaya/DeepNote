package com.abms.af.projeversion02.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abms.af.projeversion02.Models.Pdfyuklemesonuc;
import com.abms.af.projeversion02.Models.Resimyuklemesonuc;
import com.abms.af.projeversion02.R;
import com.abms.af.projeversion02.RestApi.ManagerAll;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class share_sayfasi extends Fragment {

    Map<String, RequestBody> mappdf,mapresim;
    Button yukleme_butonu;
    EditText ders_adi , acıklama;
    TextView ders_adi_uyarı,bolum_uyarı,acıklama_uyarı;
    Spinner bolum_adi;
    String ders_string,acıklama_string;
    int bolum_string_int;
    FloatingActionButton ekle_buton, pdf_buton, resim_buton;
    Animation open, close, rotate, rotate_back;
    boolean isopen = false;
    View view;
    Integer id;
    String[]  bolum_listesi;
    ArrayAdapter  bolum_adapter;
    private FloatingActionButton boommenu_ana_buton;
    SharedPreferences sharedPreferences;
    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_share_sayfasi, container, false);

        tanimla();
        islevver();

        return view;

    }

    public void tanimla() {
        boommenu_ana_buton = view.findViewById(R.id.ana_buton);
        pdf_buton = view.findViewById(R.id.pdf_buton);
        resim_buton = view.findViewById(R.id.resim_buton);
        ders_adi = view.findViewById(R.id.ders_adi);
        bolum_adi = view.findViewById(R.id.bolum_adi);
        acıklama = view.findViewById(R.id.acıklama);
        yukleme_butonu = view.findViewById(R.id.yukleme_butonu);
        ders_adi_uyarı=view.findViewById(R.id.share_sayfası_ders_uyarı);
        bolum_uyarı=view.findViewById(R.id.share_sayfası_bolum_uyarı);
        acıklama_uyarı=view.findViewById(R.id.share_sayfası_acıklama_uyarı);

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("giris", 0);
        id  =sharedPreferences.getInt("uye_id",0);


        bolum_listesi = getResources().getStringArray(R.array.Bolum_listesi);
        bolum_adapter = new ArrayAdapter(getActivity().getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, bolum_listesi);
        bolum_adi.setAdapter(bolum_adapter);


        progressBar=view.findViewById(R.id.share_sayafası_progress_bar);
    }

    public void islevver()
    {
        animasyon_yükle_boommenu_ana_buton();
        boommenu_ana_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isopen) {
                    resim_buton.startAnimation(close);
                    pdf_buton.startAnimation(close);
                    boommenu_ana_buton.startAnimation(rotate_back);
                    pdf_buton.setClickable(false);
                    resim_buton.setClickable(false);
                    isopen = false;
                    ders_adi.setVisibility(View.INVISIBLE);
                    bolum_adi.setVisibility(View.INVISIBLE);
                    acıklama.setVisibility(View.INVISIBLE);
                    yukleme_butonu.setVisibility(View.INVISIBLE);
                    // verileri sıfırlamak için kullanıldı suanlık spinner için yok
                    ders_adi.setText("");
                    acıklama.setText("");
                } else {
                    resim_buton.startAnimation(open);
                    pdf_buton.startAnimation(open);
                    boommenu_ana_buton.startAnimation(rotate);
                    pdf_buton.setClickable(true);
                    resim_buton.setClickable(true);
                    isopen = true;

                }


            }
        });
        resim_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yukleme_butonu.setText("Resim Yükle");
                resimGoster();
            }
        });
        pdf_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yukleme_butonu.setText("Pdf Yükle");
                pdf_goster();
            }
        });


        yukleme_butonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ders_string=ders_adi.getText().toString();
                bolum_string_int=bolum_adi.getSelectedItemPosition();
                acıklama_string=acıklama.getText().toString();
                if (ders_string.equals("") || bolum_string_int==0 || acıklama_string.equals(""))
                {
                    if (ders_string.equals(""))
                    {
                        ders_adi_uyarı.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        ders_adi_uyarı.setVisibility(View.INVISIBLE);
                    }
                    if (bolum_adi.getSelectedItemPosition()==0)
                    {
                        bolum_uyarı.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        bolum_uyarı.setVisibility(View.INVISIBLE);
                    }
                    if (acıklama.getText().toString().equals(""))
                    {
                        acıklama_uyarı.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        acıklama_uyarı.setVisibility(View.INVISIBLE);
                    }

                }
                else
                {
                    dosya_yukle();
                    ders_adi.setVisibility(View.INVISIBLE);
                    bolum_adi.setVisibility(View.INVISIBLE);
                    acıklama.setVisibility(View.INVISIBLE);
                    yukleme_butonu.setVisibility(View.INVISIBLE);
                    ders_adi_uyarı.setVisibility(View.INVISIBLE);
                    bolum_uyarı.setVisibility(View.INVISIBLE);
                    acıklama_uyarı.setVisibility(View.INVISIBLE);
                    // verileri sıfırlamak için kullanıldı suanlık spinner için yok
                    ders_adi.setText("");
                    acıklama.setText("");
                }

            }
        });


    }




    public void animasyon_yükle_boommenu_ana_buton() {
        open = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_open);
        close = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_close);
        rotate = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate);
        rotate_back = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.rotate_ters);
    }

    /*
     *resimlere ulasıp secmek için yazılan fonksiyon
     *
     */
    public void resimGoster() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private void pdf_goster()
    {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 2);

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

        if(yukleme_butonu.getText().equals("Resim Yükle"))
        {

            Integer id_kullanici = id;
            String ders = ders_adi.getText().toString();
            String bolum = bolum_adi.getSelectedItem().toString();
            String aciklama = acıklama.getText().toString();
            Call<Resimyuklemesonuc> a = ManagerAll.webyonet().resim_yukle(id_kullanici, ders, aciklama, bolum, mapresim);
            //Toast.makeText(getActivity().getApplicationContext(), id + ders + aciklama + bolum, Toast.LENGTH_LONG).show();

            a.enqueue(new Callback<Resimyuklemesonuc>() {
                @Override
                public void onResponse(Call<Resimyuklemesonuc> call, Response<Resimyuklemesonuc> response) {
                    Toast.makeText(getActivity().getApplicationContext(), "Dosya Yüklendi:"+response.body().getResimyuklemesonuc(), Toast.LENGTH_LONG).show();

                    /////////////////////////////////////
                    progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    ////////////////////////////////////
                }

                @Override
                public void onFailure(Call<Resimyuklemesonuc> call, Throwable t) {
                    Toast.makeText(getActivity().getApplicationContext(), "Hata olustu tekrar deneyiniz", Toast.LENGTH_LONG).show();

                    /////////////////////////////////////
                    progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    ////////////////////////////////////
                }
            });

        }
       else if (yukleme_butonu.getText().equals("Pdf Yükle"))
        {

            Integer id_kullanici = id;
            String ders = ders_adi.getText().toString();
            String bolum = bolum_adi.getSelectedItem().toString();
            String aciklama = acıklama.getText().toString();
            Call<Pdfyuklemesonuc> ee=ManagerAll.webyonet().pdf_yukle(id_kullanici, ders, aciklama, bolum,mappdf);
            ee.enqueue(new Callback<Pdfyuklemesonuc>() {
                @Override
                public void onResponse(Call<Pdfyuklemesonuc> call, Response<Pdfyuklemesonuc> response) {

                    Toast.makeText(getActivity().getApplicationContext(), "Dosya Yuklendi"+response.body().getPdfyuklemesonuc(), Toast.LENGTH_LONG).show();

                    /////////////////////////////////////
                    progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    ////////////////////////////////////
                }

                @Override
                public void onFailure(Call<Pdfyuklemesonuc> call, Throwable t) {
                    Toast.makeText(getActivity().getApplicationContext(), "HATA CIKTI", Toast.LENGTH_LONG).show();
                    Log.i("TAG", "onFailure: "+t.getMessage());

                    /////////////////////////////////////
                    progressBar.setVisibility(View.GONE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    ////////////////////////////////////
                }
            });

        }

    }


    /*
    *resimin alındıgı ve alınacak bilgiler için edittextlerin gösterildiği bölüm
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null) {
            Uri path = data.getData();
            String uri=String.valueOf(path);
            ders_adi.setVisibility(View.VISIBLE);
            bolum_adi.setVisibility(View.VISIBLE);
            acıklama.setVisibility(View.VISIBLE);
            yukleme_butonu.setVisibility(View.VISIBLE);
            try {
                // use the FileUtils to get the actual file by uri
                String yol=getPath(getActivity().getApplicationContext(),path);
                Toast.makeText(getActivity().getApplicationContext(),""+yol,Toast.LENGTH_LONG).show();

                String resim=compressImage(uri);
                ////////////////////////////////////////////////////////
/*
                Bitmap bitmapImage = BitmapFactory.decodeFile(yol);
                int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
                Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
*/
                /////////////////////////////////////////////////////////
                mapresim = new HashMap<>();

                File file = new File(resim);
                RequestBody requestFile =RequestBody.create(MediaType.parse("image/*"),file);
                mapresim.put("file\"; filename=\"" + file.getName() + "\"", requestFile);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (requestCode == 2 && resultCode == getActivity().RESULT_OK && data != null) {
            Uri path = data.getData();
            Toast.makeText(getActivity().getApplicationContext(),"yol"+path.toString(),Toast.LENGTH_LONG).show();
            ders_adi.setVisibility(View.VISIBLE);
            bolum_adi.setVisibility(View.VISIBLE);
            acıklama.setVisibility(View.VISIBLE);
            yukleme_butonu.setVisibility(View.VISIBLE);
            try {
                // use the FileUtils to get the actual file by uri
                String yol=getPath(getActivity().getApplicationContext(),path);
                Toast.makeText(getActivity().getApplicationContext(),""+yol,Toast.LENGTH_LONG).show();

                mappdf = new HashMap<>();

                File file = new File(yol);
                RequestBody requestFile =RequestBody.create(MediaType.parse("application/pdf"),file);
                mappdf.put("file\"; filename=\"" + file.getName() + "\"", requestFile);
                //yuklenecekpdf = MultipartBody.Part.createFormData("pdf",file.getName(),requestFile);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        }





    ////////
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }





    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}
