package com.example.bookapp.Klase;

import java.util.ArrayList;

public class Knjiga
{
     private String id;
     private String predmet;
     private String naziv;
     private ArrayList<String> autori;
     private String izdavac;
     private int godinaIzdanja;

     public Knjiga(String id,String predmet,String naziv,String izdavac,int godinaIzdanja,ArrayList<String> autori)
     {
         this.id=id;
         this.predmet=predmet;
         this.naziv=naziv;
         this.izdavac=izdavac;
         this.godinaIzdanja=godinaIzdanja;
         this.autori=autori;
     }
     public Knjiga(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPredmet() { return predmet; }

    public void setPredmet(String predmet) { this.predmet = predmet; }

    public String getNaziv() { return naziv; }

    public void setNaziv(String naziv) { this.naziv = naziv; }

    public ArrayList<String> getAutori() { return autori; }

    public void setAutori(ArrayList<String> autori) { this.autori = autori; }

    public String getIzdavac() { return izdavac; }

    public void setIzdavac(String izdavac) { this.izdavac = izdavac; }

    public int getGodinaIzdanja() { return godinaIzdanja; }

    public void setGodinaIzdanja(int godinaIzdanja) { this.godinaIzdanja = godinaIzdanja; }

}
