package com.abms.af.projeversion02.Models;

public class Kullanicigirissonuc{
	private String kullaniciid;
	private String kullanicigirissonuc;
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setKullaniciid(String kullaniciid){
		this.kullaniciid = kullaniciid;
	}

	public String getKullaniciid(){
		return kullaniciid;
	}

	public void setKullanicigirissonuc(String kullanicigirissonuc){
		this.kullanicigirissonuc = kullanicigirissonuc;
	}

	public String getKullanicigirissonuc(){
		return kullanicigirissonuc;
	}
	@Override
	public String toString() {
		return "Kullanicigirissonuc{" +
				"kullaniciid='" + kullaniciid + '\'' +
				", kullanicigirissonuc='" + kullanicigirissonuc + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}
