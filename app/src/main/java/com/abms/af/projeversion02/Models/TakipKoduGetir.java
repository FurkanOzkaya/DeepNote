package com.abms.af.projeversion02.Models;

public class TakipKoduGetir {

    public String TakipKodu;

    public String getTakipKodu() {
        return TakipKodu;
    }

    public void setTakipKodu(String takipKodu) {
        TakipKodu = takipKodu;
    }

    @Override
    public String toString() {
        return "TakipKoduGetir{" +
                "TakipKodu='" + TakipKodu + '\'' +
                '}';
    }
}
