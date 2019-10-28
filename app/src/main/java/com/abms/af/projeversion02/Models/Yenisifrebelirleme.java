package com.abms.af.projeversion02.Models;

public class Yenisifrebelirleme {

    private String Result;

    public Yenisifrebelirleme(String result) {
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
        return "Yenisifrebelirleme{" +
                "Result='" + Result + '\'' +
                '}';
    }
}
