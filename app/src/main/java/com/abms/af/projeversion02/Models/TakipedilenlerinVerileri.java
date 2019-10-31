package com.abms.af.projeversion02.Models;

public class TakipedilenlerinVerileri {

    private String universite;
    private String aciklama;
    private String adsoyad;
    private String idkullanici;
    private String dosyayolu;
    private String ders;
    private String bolum;
    private String paylasimid;
    private String profilfoto;
    private String dosyaturu;
    private int yorumsayisi;
    private int gosterme;
    private int rowCount;
    private int pageListSize;

    public String getUniversite() {
        return universite;
    }

    public void setUniversite(String universite) {
        this.universite = universite;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getAdsoyad() {
        return adsoyad;
    }

    public void setAdsoyad(String adsoyad) {
        this.adsoyad = adsoyad;
    }

    public String getIdkullanici() {
        return idkullanici;
    }

    public void setIdkullanici(String idkullanici) {
        this.idkullanici = idkullanici;
    }

    public String getDosyayolu() {
        return dosyayolu;
    }

    public void setDosyayolu(String dosyayolu) {
        this.dosyayolu = dosyayolu;
    }

    public String getDers() {
        return ders;
    }

    public void setDers(String ders) {
        this.ders = ders;
    }

    public String getBolum() {
        return bolum;
    }

    public void setBolum(String bolum) {
        this.bolum = bolum;
    }

    public String getPaylasimid() {
        return paylasimid;
    }

    public void setPaylasimid(String paylasimid) {
        this.paylasimid = paylasimid;
    }

    public String getProfilfoto() {
        return profilfoto;
    }

    public void setProfilfoto(String profilfoto) {
        this.profilfoto = profilfoto;
    }

    public String getDosyaturu() {
        return dosyaturu;
    }

    public void setDosyaturu(String dosyaturu) {
        this.dosyaturu = dosyaturu;
    }

    public int getYorumsayisi() {
        return yorumsayisi;
    }

    public void setYorumsayisi(int yorumsayisi) {
        this.yorumsayisi = yorumsayisi;
    }

    public int getGosterme() {
        return gosterme;
    }

    public void setGosterme(int gosterme) {
        this.gosterme = gosterme;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getPageListSize() {
        return pageListSize;
    }

    public void setPageListSize(int pageListSize) {
        this.pageListSize = pageListSize;
    }

    @Override
    public String toString() {
        return "TakipedilenlerinVerileri{" +
                "universite='" + universite + '\'' +
                ", aciklama='" + aciklama + '\'' +
                ", adsoyad='" + adsoyad + '\'' +
                ", idkullanici='" + idkullanici + '\'' +
                ", dosyayolu='" + dosyayolu + '\'' +
                ", ders='" + ders + '\'' +
                ", bolum='" + bolum + '\'' +
                ", paylasimid='" + paylasimid + '\'' +
                ", profilfoto='" + profilfoto + '\'' +
                ", dosyaturu='" + dosyaturu + '\'' +
                ", yorumsayisi=" + yorumsayisi +
                ", gosterme=" + gosterme +
                ", rowCount=" + rowCount +
                ", pageListSize=" + pageListSize +
                '}';
    }
}
