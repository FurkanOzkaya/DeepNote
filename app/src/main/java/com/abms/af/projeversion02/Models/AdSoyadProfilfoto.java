package com.abms.af.projeversion02.Models;

public class AdSoyadProfilfoto {

    private String Result;
    private String ad_soyad;
    private String profil_foto;

    public AdSoyadProfilfoto(String result, String ad_soyad, String profil_foto) {
        Result = result;
        this.ad_soyad = ad_soyad;
        this.profil_foto = profil_foto;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getAd_soyad() {
        return ad_soyad;
    }

    public void setAd_soyad(String ad_soyad) {
        this.ad_soyad = ad_soyad;
    }

    public String getProfil_foto() {
        return profil_foto;
    }

    public void setProfil_foto(String profil_foto) {
        this.profil_foto = profil_foto;
    }

    @Override
    public String toString() {
        return "AdSoyadProfilfoto{" +
                "Result='" + Result + '\'' +
                ", ad_soyad='" + ad_soyad + '\'' +
                ", profil_foto='" + profil_foto + '\'' +
                '}';
    }
}
