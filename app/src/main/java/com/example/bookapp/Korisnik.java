package com.example.bookapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Korisnik {

    private String id;
    private String ime;
    private String prezime;
    private String mail;
    private double prosecnaOcena;
    private int brojOcena;

    public Korisnik(String id,String ime,String prezime,String mail)
    {
        this.id=id;
        this.ime=ime;
        this.prezime=prezime;
        this.mail=mail;
        this.prosecnaOcena=0.0;
        this.brojOcena=0;
    }

    public int getBrojOcena() {
        return brojOcena;
    }

    public void setBrojOcena(int brojOcena) {
        this.brojOcena = brojOcena;
    }

    public double getProsecnaOcena() {
        return prosecnaOcena;
    }

    public void setProsecnaOcena(double prosecnaOcena) {
        this.prosecnaOcena = prosecnaOcena;
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
        DatabaseReference knjigeProdaja = FirebaseDatabase.getInstance().getReference("Korisnici").child(this.id).child("KnjigeProdaja");
        knjigeProdaja.child(id).setValue(id);
    }

    public void dodajKnjiguZainteresovan(String id)
    {
        DatabaseReference knjigeZainteresovan = FirebaseDatabase.getInstance().getReference("Korisnici").child(this.id).child("KnjigeZainteresovan");
        knjigeZainteresovan.child(id).setValue(id);
    }

    public void skloniKnjiguProdaja(String id)
    {
        DatabaseReference knjigeProdaja = FirebaseDatabase.getInstance().getReference("Korisnici").child(this.id).child("KnjigeProdaja");
        knjigeProdaja.child(id).removeValue();
    }

    public void skloniKnjiguZainteresovan(String id)
    {
        DatabaseReference knjigeZainteresovan = FirebaseDatabase.getInstance().getReference("Korisnici").child(this.id).child("KnjigeZainteresovan");
        knjigeZainteresovan.child(id).removeValue();
    }

}
