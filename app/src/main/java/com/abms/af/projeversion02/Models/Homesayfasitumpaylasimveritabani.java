package com.abms.af.projeversion02.Models;

public class Homesayfasitumpaylasimveritabani{
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

	public void setUniversite(String universite){
		this.universite = universite;
	}

	public String getUniversite(){
		return universite;
	}

	public void setAciklama(String aciklama){
		this.aciklama = aciklama;
	}

	public String getAciklama(){
		return aciklama;
	}

	public void setAdsoyad(String adsoyad){
		this.adsoyad = adsoyad;
	}

	public String getAdsoyad(){
		return adsoyad;
	}

	public void setIdkullanici(String idkullanici){
		this.idkullanici = idkullanici;
	}

	public String getIdkullanici(){
		return idkullanici;
	}

	public void setDosyayolu(String dosyayolu){
		this.dosyayolu = dosyayolu;
	}

	public String getDosyayolu(){
		return dosyayolu;
	}

	public void setDers(String ders){
		this.ders = ders;
	}

	public String getDers(){
		return ders;
	}

	public void setBolum(String bolum){
		this.bolum = bolum;
	}

	public String getBolum(){
		return bolum;
	}

	public void setPaylasimid(String paylasimid){
		this.paylasimid = paylasimid;
	}

	public String getPaylasimid(){
		return paylasimid;
	}

	@Override
 	public String toString(){
		return 
			"Homesayfasitumpaylasimveritabani{" + 
			"universite = '" + universite + '\'' + 
			",aciklama = '" + aciklama + '\'' + 
			",adsoyad = '" + adsoyad + '\'' + 
			",idkullanici = '" + idkullanici + '\'' + 
			",dosyayolu = '" + dosyayolu + '\'' + 
			",ders = '" + ders + '\'' + 
			",bolum = '" + bolum + '\'' +
			",dosyaturu = '" + dosyaturu + '\'' +
			",paylasimid = '" + paylasimid + '\'' +
			"}";
		}
}
