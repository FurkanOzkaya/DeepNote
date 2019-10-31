package com.abms.af.projeversion02.Models;

public class TakipDurumu {

    public String Result;

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    @Override
    public String toString() {
        return "TakipDurumu{" +
                "Result='" + Result + '\'' +
                '}';
    }
}
