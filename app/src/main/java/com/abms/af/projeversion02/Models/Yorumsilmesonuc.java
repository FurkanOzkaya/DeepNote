package com.abms.af.projeversion02.Models;

public class Yorumsilmesonuc{
	private String yorumsilmesonuc;

	public void setYorumsilmesonuc(String yorumsilmesonuc){
		this.yorumsilmesonuc = yorumsilmesonuc;
	}

	public String getYorumsilmesonuc(){
		return yorumsilmesonuc;
	}

	@Override
 	public String toString(){
		return 
			"Yorumsilmesonuc{" + 
			"yorumsilmesonuc = '" + yorumsilmesonuc + '\'' + 
			"}";
		}
}
