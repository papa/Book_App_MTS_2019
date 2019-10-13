package com.example.bookapp.Fragmenti;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Oglas
{
    private String id;
    private String idKnjige;
    private String idUsera;
    private int cena;
    private String dodatniOpis;
    private int brojZainteresovanih;

    public Oglas(String id, String idKnjige, String idUsera, int cena, String dodatniOpis) {
        this.id = id;
        this.idKnjige = idKnjige;
        this.idUsera = idUsera;
        this.cena = cena;
        this.dodatniOpis=dodatniOpis;
        this.brojZainteresovanih=0;
    }

    public int getBrojZainteresovanih() {
        return brojZainteresovanih;
    }

    public void setBrojZainteresovanih(int brojZainteresovanih) {
        this.brojZainteresovanih = brojZainteresovanih;
    }

    public String getDodatniOpis() {
        return dodatniOpis;
    }

    public void setDodatniOpis(String dodatniOpis) {
        this.dodatniOpis = dodatniOpis;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdKnjige() {
        return idKnjige;
    }

    public void setIdKnjige(String idKnjige) {
        this.idKnjige = idKnjige;
    }

    public String getIdUsera() {
        return idUsera;
    }

    public void setIdUsera(String idUsera) {
        this.idUsera = idUsera;
    }

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    public void povecajZainteresovane()
    {
        this.brojZainteresovanih++;
    }

    public void smanjiZainteresovane()
    {
        this.brojZainteresovanih--;
    }

    public void ubaciKodKnjige(String idKnjigeUbaci)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Knjige").child(idKnjigeUbaci);
        ref.child("oglasi").child(this.id).setValue(this.id);
    }

    public void ubaciKodKorisnika()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Korisnici").child(this.idUsera);
        ref.child("oglasi").child(this.id).setValue(this.id);
    }

    public void ubaciOglas()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("SviOglasi");
        ref.child(this.id).setValue(this);
    }


}
