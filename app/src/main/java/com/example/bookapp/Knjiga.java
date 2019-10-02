package com.example.bookapp;

import java.util.ArrayList;

public class Knjiga {

    String id;
     String predmet;
     String naziv;
     ArrayList<String> autori;
     int brojstranica;
     String izdavac;
     int godinaIzdanja;
     int brojZainteresovanih;
     String dodatniOpis;

     //korisnik treba da ima i listu knjiga koje je izbacio
    //i listu knjiga za koje je zainteresovan verovatno
    //to nam nije tolko bitno da cuvamo kao parametar
    //mozemo i samo da nakacimo u firebase

     public Knjiga(String id,String predmet,String naziv,String izdavac,int brojstranica,int godinaIzdanja,String dodatniOpis,ArrayList<String> autori)
     {
         this.dodatniOpis=dodatniOpis;
         this.brojZainteresovanih=0;
         this.id=id;
         this.predmet=predmet;
         this.naziv=naziv;
         this.izdavac=izdavac;
         this.brojstranica=brojstranica;
         this.godinaIzdanja=godinaIzdanja;
         this.autori=autori;
     }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPredmet() {
        return predmet;
    }

    public void setPredmet(String predmet) {
        this.predmet = predmet;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public ArrayList<String> getAutori() {
        return autori;
    }

    public void setAutori(ArrayList<String> autori) {
        this.autori = autori;
    }

    public int getBrojstranica() {
        return brojstranica;
    }

    public void setBrojstranica(int brojstranica) {
        this.brojstranica = brojstranica;
    }

    public String getIzdavac() {
        return izdavac;
    }

    public void setIzdavac(String izdavac) {
        this.izdavac = izdavac;
    }

    public int getGodinaIzdanja() {
        return godinaIzdanja;
    }

    public void setGodinaIzdanja(int godinaIzdanja) {
        this.godinaIzdanja = godinaIzdanja;
    }
}
