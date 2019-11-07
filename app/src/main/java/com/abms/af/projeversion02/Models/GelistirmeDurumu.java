package com.abms.af.projeversion02.Models;

public class GelistirmeDurumu {

    public String Metin;
    public String Resim;
    public String Durum;

    public String getMetin() {
        return Metin;
    }

    public void setMetin(String metin) {
        Metin = metin;
    }

    public String getResim() {
        return Resim;
    }

    public void setResim(String resim) {
        Resim = resim;
    }

    public String getDurum() {
        return Durum;
    }

    public void setDurum(String durum) {
        Durum = durum;
    }

    @Override
    public String toString() {
        return "GelistirmeDurumu{" +
                "Metin='" + Metin + '\'' +
                ", Resim='" + Resim + '\'' +
                ", Durum='" + Durum + '\'' +
                '}';
    }
}
