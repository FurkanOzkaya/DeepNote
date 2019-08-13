package com.abms.af.projeversion02.Models;

public class Yorumlarigetirsonuc{
	private String adsoyad;
	private String tarih;
	private String profilfoto;
	private String yorum;
	private String id_kullanici;
	private String id_yorum;
	private String paylasim_id;

	public String getPaylasim_id() {
		return paylasim_id;
	}

	public void setPaylasim_id(String paylasim_id) {
		this.paylasim_id = paylasim_id;
	}

	public String getId_kullanici() {
		return id_kullanici;
	}

	public void setId_kullanici(String id_kullanici) {
		this.id_kullanici = id_kullanici;
	}

	public String getId_yorum() {
		return id_yorum;
	}

	public void setId_yorum(String id_yorum) {
		this.id_yorum = id_yorum;
	}

	public void setAdsoyad(String adsoyad){
		this.adsoyad = adsoyad;
	}

	public String getAdsoyad(){
		return adsoyad;
	}

	public void setTarih(String tarih){
		this.tarih = tarih;
	}

	public String getTarih(){
		return tarih;
	}

	public void setProfilfoto(String profilfoto){
		this.profilfoto = profilfoto;
	}

	public String getProfilfoto(){
		return profilfoto;
	}

	public void setYorum(String yorum){
		this.yorum = yorum;
	}

	public String getYorum(){
		return yorum;
	}

	@Override
 	public String toString(){
		return 
			"Yorumlarigetirsonuc{" + 
			"adsoyad = '" + adsoyad + '\'' + 
			",tarih = '" + tarih + '\'' + 
			",profilfoto = '" + profilfoto + '\'' + 
			",yorum = '" + yorum + '\'' + 
			",id_kullanici = '" + id_kullanici + '\'' +
			"}";
		}
}
