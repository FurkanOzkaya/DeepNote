package com.abms.af.projeversion02.Models;

public class GonderiSil {

    private String Result;

    public GonderiSil(String result) {
        Result = result;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    @Override
    public String toString() {
        return "GonderiSil{" +
                "Result='" + Result + '\'' +
                '}';
    }
}
