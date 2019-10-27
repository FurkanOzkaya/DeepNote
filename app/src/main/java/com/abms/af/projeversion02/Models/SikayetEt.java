package com.abms.af.projeversion02.Models;

public class SikayetEt {

    private String Result;

    public SikayetEt(String result) {
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
        return "SikayetEt{" +
                "Result='" + Result + '\'' +
                '}';
    }
}
