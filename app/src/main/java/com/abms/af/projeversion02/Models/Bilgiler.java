package com.abms.af.projeversion02.Models;

public class Bilgiler{
	private String adSoyad;
	private String universite;
	private String sifre;
	private String ePosta;
	private String bolum;
	private String dogumTarihi;

	public Bilgiler(String adSoyad, String universite, String sifre, String ePosta, String bolum, String dogumTarihi) {
		this.adSoyad = adSoyad;
		this.universite = universite;
		this.sifre = sifre;
		this.ePosta = ePosta;
		this.bolum = bolum;
		this.dogumTarihi = dogumTarihi;
	}

	public void setAdSoyad(String adSoyad){
		this.adSoyad = adSoyad;
	}

	public String getAdSoyad(){
		return adSoyad;
	}

	public void setUniversite(String universite){
		this.universite = universite;
	}

	public String getUniversite(){
		return universite;
	}

	public void setSifre(String sifre){
		this.sifre = sifre;
	}

	public String getSifre(){
		return sifre;
	}

	public void setEPosta(String ePosta){
		this.ePosta = ePosta;
	}

	public String getEPosta(){
		return ePosta;
	}

	public void setBolum(String bolum){
		this.bolum = bolum;
	}

	public String getBolum(){
		return bolum;
	}

	public void setDogumTarihi(String dogumTarihi){
		this.dogumTarihi = dogumTarihi;
	}

	public String getDogumTarihi(){
		return dogumTarihi;
	}

	@Override
 	public String toString(){
		return 
			"Bilgiler{" + 
			"ad_soyad = '" + adSoyad + '\'' + 
			",universite = '" + universite + '\'' + 
			",sifre = '" + sifre + '\'' + 
			",e_posta = '" + ePosta + '\'' + 
			",bolum = '" + bolum + '\'' + 
			",dogum_tarihi = '" + dogumTarihi + '\'' + 
			"}";
		}
}
