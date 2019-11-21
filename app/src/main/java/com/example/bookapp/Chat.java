package com.example.bookapp;

public class Chat
{
    private String salje;
    private String prima;
    private String poruka;
    private int dan;
    private int mesec;
    private int godina;
    private int sati;
    private int minuti;

    public Chat()
    {

    }

    public Chat(String s,String p,String por,int dan,int mesec,int godina,int sati,int minuti)
    {
        this.salje=s;
        this.prima=p;
        this.poruka=por;
        this.dan=dan;
        this.mesec=mesec;
        this.godina=godina;
        this.sati=sati;
        this.minuti=minuti;
    }

    public String getSalje() {
        return salje;
    }

    public void setSalje(String salje) {
        this.salje = salje;
    }

    public String getPrima() {
        return prima;
    }

    public void setPrima(String prima) {
        this.prima = prima;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public int getDan() {
        return dan;
    }

    public void setDan(int dan) {
        this.dan = dan;
    }

    public int getMesec() {
        return mesec;
    }

    public void setMesec(int mesec) {
        this.mesec = mesec;
    }

    public int getGodina() {
        return godina;
    }

    public void setGodina(int godina) {
        this.godina = godina;
    }

    public int getSati() {
        return sati;
    }

    public void setSati(int sati) {
        this.sati = sati;
    }

    public int getMinuti() {
        return minuti;
    }

    public void setMinuti(int minuti) {
        this.minuti = minuti;
    }
}
