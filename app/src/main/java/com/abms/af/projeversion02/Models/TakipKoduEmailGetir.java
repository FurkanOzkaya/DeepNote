package com.abms.af.projeversion02.Models;

public class TakipKoduEmailGetir {

    public int id_kullanici;
    public String e_posta;
    public String ad_soyad;
    public String bolum;
    public String universite;
    public String profil_foto;


    public int getId_kullanici() {
        return id_kullanici;
    }

    public void setId_kullanici(int id_kullanici) {
        this.id_kullanici = id_kullanici;
    }

    public String getE_posta() {
        return e_posta;
    }

    public void setE_posta(String e_posta) {
        this.e_posta = e_posta;
    }

    public String getAd_soyad() {
        return ad_soyad;
    }

    public void setAd_soyad(String ad_soyad) {
        this.ad_soyad = ad_soyad;
    }

    public String getBolum() {
        return bolum;
    }

    public void setBolum(String bolum) {
        this.bolum = bolum;
    }

    public String getUniversite() {
        return universite;
    }

    public void setUniversite(String universite) {
        this.universite = universite;
    }

    public String getProfil_foto() {
        return profil_foto;
    }

    public void setProfil_foto(String profil_foto) {
        this.profil_foto = profil_foto;
    }

    @Override
    public String toString() {
        return "TakipKoduEmailGetir{" +
                "id_kullanici=" + id_kullanici +
                ", e_posta='" + e_posta + '\'' +
                ", ad_soyad='" + ad_soyad + '\'' +
                ", bolum='" + bolum + '\'' +
                ", universite='" + universite + '\'' +
                ", profil_foto='" + profil_foto + '\'' +
                '}';
    }
}
