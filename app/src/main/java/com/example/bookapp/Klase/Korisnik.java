package com.example.bookapp.Klase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Korisnik {

    private String id;
    private String ime;
    private String prezime;
    private String mail;
    private double prosecnaOcena;
    private int brojOcena;
    private String oglasID;
    int brojProdatihKnjiga;
    int brojPostavljenihOglasa;

    public Korisnik(String id,String ime,String prezime,String mail)
    {
        this.id=id;
        this.ime=ime;
        this.prezime=prezime;
        this.mail=mail;
        this.prosecnaOcena=0.0;
        this.brojOcena=0;
        this.oglasID="";
        this.brojPostavljenihOglasa=0;
        this.brojProdatihKnjiga=0;
    }

    public Korisnik(){}

    public int getBrojOcena() {
        return brojOcena;
    }

    public void setOglasID(String ogId) {
        this.oglasID=ogId;
    }
    public String getOglasID() {
        return oglasID;
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

    public void uvecajBrojProdatihKnjiga(int br)
    {
        this.brojProdatihKnjiga++;
        DatabaseReference kor = FirebaseDatabase.getInstance().getReference("Korisnici").child(this.id);
        kor.child("brojProdatihKnjiga").setValue(br+1);
    }

    public void uvecajBrojPostavlejnih(int br)
    {
        this.brojPostavljenihOglasa++;
        DatabaseReference kor = FirebaseDatabase.getInstance().getReference("Korisnici").child(this.id);
        kor.child("brojPostavljenihOglasa").setValue(br+1);
    }

    public void oceni(int ocena)
    {
        double ukupna = this.brojOcena*this.prosecnaOcena;
        double nova  = ukupna+ocena;
        this.brojOcena++;
        this.prosecnaOcena = (nova)/brojOcena;
        DatabaseReference kor = FirebaseDatabase.getInstance().getReference("Korisnici").child(this.id);
        kor.child("brojOcena").setValue(brojOcena);
        kor.child("prosecnaOcena").setValue(prosecnaOcena);
    }
}
