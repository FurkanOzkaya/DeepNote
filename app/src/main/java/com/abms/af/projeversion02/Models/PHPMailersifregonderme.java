package com.abms.af.projeversion02.Models;

public class PHPMailersifregonderme {

    private String Result;
    private String Email;
    private String Kod;

    public PHPMailersifregonderme(String result, String email, String kod) {
        Result = result;
        Email = email;
        Kod = kod;
    }

    public String getResult() {
        return Result;
    }
    public void setResult(String result) {
        Result = result;
    }
    public String getEmail() {
        return Email;
    }
    public void setEmail(String email) {
        Email = email;
    }
    public String getKod() {
        return Kod;
    }
    public void setKod(String kod) {
        Kod = kod;
    }

    @Override
    public String toString() {
        return "PHPMailersifregonderme{" +
                "Result='" + Result + '\'' +
                ", Email='" + Email + '\'' +
                ", Kod='" + Kod + '\'' +
                '}';
    }
}
