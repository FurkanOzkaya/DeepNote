package com.abms.af.projeversion02.Models;

public class Sikayetetmesonuc{
	private String Sikayetetmesonuc;

	public void setSikayetetmesonuc(String sikayetetmesonuc){
		this.Sikayetetmesonuc = sikayetetmesonuc;
	}

	public String getSikayetetmesonuc(){
		return Sikayetetmesonuc;
	}

	@Override
 	public String toString(){
		return 
			"Sikayetetmesonuc{" + 
			"sikayetetmesonuc = '" + Sikayetetmesonuc + '\'' +
			"}";
		}
}
