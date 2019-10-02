package com.example.bookapp;

public class Korisnik {

    String id;
    String ime;
    String prezime;
    String mail;

    public Korisnik(String id,String ime,String prezime,String mail)
    {
        this.id=id;
        this.ime=ime;
        this.prezime=prezime;
        this.mail=mail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void dodajKnjiguProdaja(String id)
    {

    }

    public void dodajKnjiguZainteresovan(String id)
    {

    }

}
