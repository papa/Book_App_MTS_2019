package com.example.bookapp.Fragmenti;

import java.util.ArrayList;

public class Oglas {

    private String id;
    private String idKnjige;
    private String idUsera;
    private int cena;
    private ArrayList<String> zainteresovaniKorisnici;

    public Oglas(String id, String idKnjige, String idUsera, int cena, ArrayList<String> zainteresovaniKorisnici) {
        this.id = id;
        this.idKnjige = idKnjige;
        this.idUsera = idUsera;
        this.cena = cena;
        this.zainteresovaniKorisnici = zainteresovaniKorisnici;
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

    public ArrayList<String> getZainteresovaniKorisnici() {
        return zainteresovaniKorisnici;
    }

    public void setZainteresovaniKorisnici(ArrayList<String> zainteresovaniKorisnici) {
        this.zainteresovaniKorisnici = zainteresovaniKorisnici;
    }
}
