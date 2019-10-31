package com.abms.af.projeversion02.Models;

public class NotTakipTakipciSayisi {

    public int Not;
    public int Takip;
    public int Takipci;

    public int getNot() {
        return Not;
    }

    public void setNot(int not) {
        Not = not;
    }

    public int getTakip() {
        return Takip;
    }

    public void setTakip(int takip) {
        Takip = takip;
    }

    public int getTakipci() {
        return Takipci;
    }

    public void setTakipci(int takipci) {
        Takipci = takipci;
    }

    @Override
    public String toString() {
        return "NotTakipTakipciSayisi{" +
                "Not=" + Not +
                ", Takip=" + Takip +
                ", Takipci=" + Takipci +
                '}';
    }
}
