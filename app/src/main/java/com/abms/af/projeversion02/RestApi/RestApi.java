package com.abms.af.projeversion02.RestApi;

import com.abms.af.projeversion02.Models.GonderiSil;
import com.abms.af.projeversion02.Models.Homesayfasitumpaylasimveritabani;
import com.abms.af.projeversion02.Models.Kullanicigirissonuc;
import com.abms.af.projeversion02.Models.Kullanicikayitsonuc;
import com.abms.af.projeversion02.Models.PHPMailersifregonderme;
import com.abms.af.projeversion02.Models.Pdfyuklemesonuc;
import com.abms.af.projeversion02.Models.Profilfotosilmesonuc;
import com.abms.af.projeversion02.Models.Profilfotoyuklemesonuc;
import com.abms.af.projeversion02.Models.Profilsayfasikullanicipaylasimlari;
import com.abms.af.projeversion02.Models.Resimyuklemesonuc;
import com.abms.af.projeversion02.Models.Profilbilgilerigetir;
import com.abms.af.projeversion02.Models.Sikayetetmesonuc;
import com.abms.af.projeversion02.Models.Yenisifrebelirleme;
import com.abms.af.projeversion02.Models.Yorumlarigetirsonuc;
import com.abms.af.projeversion02.Models.Yorumsilmesonuc;
import com.abms.af.projeversion02.Models.Yorumyapmasonuc;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

public interface RestApi {

    @FormUrlEncoded
    @POST("/kullaniciekleme.php")
    Call<Kullanicikayitsonuc> kullaniciekle(@Field("protectionKey") String  key ,@Field("ad_soyad") String ad_soyad, @Field("dogum_tarihi") String dogum_tarihi, @Field("universite") String universite, @Field("bolum") String bolum, @Field("e_posta") String e_posta, @Field("sifre") String sifre);

    @FormUrlEncoded
    @POST("/kullanicilogiN.php")
    Call<Kullanicigirissonuc> kontrol(@Field("giris_mail") String giris_mail, @Field("giris_sifre") String giris_sifre);

    @Multipart
    @POST("/Resimyukleme.php")
    Call<Resimyuklemesonuc> resimekle(@Part("email") String email,@Part("id_kullanici") Integer id_kullanici, @Part("ders") String ders, @Part("aciklama") String aciklama, @Part("bolum") String bolum, @PartMap Map<String, RequestBody> file);

    @FormUrlEncoded
    @POST("/Profilbilgigetir.php")
    Call<Profilbilgilerigetir> profilgetir(@Field("email") String email,@Field("id_kullanici") Integer  id_kullanici);

    @FormUrlEncoded
    @POST("/Paylasimveritabanigetir.php")
    Call<List<Homesayfasitumpaylasimveritabani>> paylasımlarintumunugetir(@Field("email") String email,@Field("jsonguvenlik") String jsonguvenlik,@Field("page") int page);

    @FormUrlEncoded
    @POST("/Profilsayfasiveritabanigetir.php")
    Call<List<Profilsayfasikullanicipaylasimlari>> kullanicigönderigetir(@Field("email") String email,@Field("id") Integer id_kullanici);


    @Multipart
    @POST("/Pdfyukleme.php")
    Call<Pdfyuklemesonuc> pdfekle(@Part("email") String email,@Part("id_kullanici") Integer  id_kullanici, @Part("ders") String  ders, @Part("aciklama") String  aciklama, @Part("bolum") String  bolum, @PartMap Map<String, RequestBody> file);

    @Multipart
    @POST("/Profilfotoyukleme.php")
    Call<Profilfotoyuklemesonuc> profilfotoyukle(@Field("email") String email,@Part("id_kullanici") Integer  id_kullanici,@PartMap Map<String, RequestBody> file);


    @FormUrlEncoded
    @POST("/Sikayetetme.php")
    Call<Sikayetetmesonuc> sikayetet(@Field("email") String email,@Field("paylasım_id") Integer paylasımid);

    @FormUrlEncoded
    @POST("/Aramapopupverigetir.php")
    Call<List<Homesayfasitumpaylasimveritabani>> aramagonderigetir(@Field("email") String email,@Field("universite") String universite,@Field("bolum") String bolum ,@Field("dersadi") String dersadi,@Field("page") int page );

    @FormUrlEncoded
    @POST("/Yorumyapma.php")
    Call<Yorumyapmasonuc> yorumyap(@Field("email") String email,@Field("id_kullanici") int  id_kullanici,@Field("paylasim_id") int  paylasim_id,@Field("yorum") String  yorum);

    @FormUrlEncoded
    @POST("/Yorumlarigetir.php")
    Call<List<Yorumlarigetirsonuc>> yorumgetir(@Field("email") String email,@Field("paylasim_id") int paylasim_id);

    @FormUrlEncoded
    @POST("/Yorumsilme.php")
    Call<Yorumsilmesonuc> yorumsil(@Field("email") String email,@Field("id_kullanici") int id_kullanici,@Field("paylasim_id") int paylasim_id,@Field("id_yorum") int id_yorum);


    @FormUrlEncoded
    @POST("/ProfilfotoSilme.php")
    Call<Profilfotosilmesonuc> profilfotosil(@Field("email") String email,@Field("id_kullanici") Integer id_kullanici);

    @GET
    Call<ResponseBody> indir(@Field("email") String email,@Url String url);

    @FormUrlEncoded
    @POST("/kodgonderme.php")
    Call<PHPMailersifregonderme> RestPHPMailer(@Field("protectionKey") String  key ,@Field("email") String email);

    @FormUrlEncoded
    @POST("/YeniSifreBelirleme.php")
    Call<Yenisifrebelirleme> RestYeniSifreBekirleme(@Field("protectionKey") String  key ,@Field("email") String email, @Field("sifre") String sifre);

    @FormUrlEncoded
    @POST("/GonderiSil.php")
    Call<GonderiSil> RestGonderiSil(@Field("email") String email,@Field("id") Integer id);

}
