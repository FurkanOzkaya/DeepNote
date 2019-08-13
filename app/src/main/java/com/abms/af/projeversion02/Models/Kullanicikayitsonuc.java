package com.abms.af.projeversion02.Models;

public class Kullanicikayitsonuc {
    private String kullanicikayitsonuc;

    public void setKullanicikayitsonuc(String kullanicikayitsonuc){
        this.kullanicikayitsonuc = kullanicikayitsonuc;
    }

    public String getKullanicikayitsonuc(){
        return kullanicikayitsonuc;
    }

    @Override
    public String toString(){
        return
                "Response{" +
                        "kullanicikayitsonuc = '" + kullanicikayitsonuc + '\'' +
                        "}";
    }
}
