package com.abms.af.projeversion02.Models;

public class Kullanicikayitsonuc {
    private String kullanicikayitsonuc;
    private String token;

    public String getKullanicikayitsonuc() {
        return kullanicikayitsonuc;
    }

    public void setKullanicikayitsonuc(String kullanicikayitsonuc) {
        this.kullanicikayitsonuc = kullanicikayitsonuc;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Kullanicikayitsonuc{" +
                "kullanicikayitsonuc='" + kullanicikayitsonuc + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
