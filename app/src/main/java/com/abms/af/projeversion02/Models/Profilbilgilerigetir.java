package com.abms.af.projeversion02.Models;

public class Profilbilgilerigetir {
    private Integer sonuc;
    private String ad_soyad;
    private String universite;
    private String bolum;
    private String profil_foto;

    public String getProfil_foto() {
        return profil_foto;
    }

    public void setProfil_foto(String profil_foto) {
        this.profil_foto = profil_foto;
    }

    public Integer getSonuc() {
        return sonuc;
    }

    public void setSonuc(Integer sonuc) {
        this.sonuc = sonuc;
    }



    public String getAd_soyad() {
        return ad_soyad;
    }

    public void setAd_soyad(String ad_soyad) {
        this.ad_soyad = ad_soyad;
    }

    public String getUniversite() {
        return universite;
    }

    public void setUniversite(String universite) {
        this.universite = universite;
    }

    public String getBolum() {
        return bolum;
    }

    public void setBolum(String bolum) {
        this.bolum = bolum;
    }
}
