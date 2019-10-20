package com.example.bookapp.Klase.Oglasi;

import android.content.Context;
import android.os.AsyncTask;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.Adapteri.AdapterKnjige;
import com.example.bookapp.Klase.Knjiga;
import com.example.bookapp.Klase.Oglas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrikazOglasa extends AsyncTask<Object,String,String>
{
    private RecyclerView recyclerView;
    private Context context;
    private ArrayList<String> ids;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<String> nazivi=new ArrayList<>();
    private ArrayList<String> autori=new ArrayList<>();
    private ArrayList<String> predmeti=new ArrayList<>();
    private ArrayList<String> izdavaci=new ArrayList<>();
    private ArrayList<String> godineIzdanja=new ArrayList<>();
    private ArrayList<String> cene=new ArrayList<>();

    @Override
    protected String doInBackground(Object... objects) {
        context=(Context)objects[0];
        recyclerView=(RecyclerView)objects[1];
        ids=(ArrayList<String>)objects[2];

        return "";
    }

    @Override
    protected void onPostExecute(String s) {

        List<HashMap<String,String>> oglasi=null;

        CitanjeOglasa citanjeOglasa=new CitanjeOglasa();

        for(int i=0;i<ids.size();i++) {
            //oglasi = citanjeOglasa.konvertuj(ids.get(i));
        }

        prikaziOglase(oglasi);

    }

    public void prikaziOglase(List<HashMap<String,String>> oglasi)
    {
        //StringBuilder stringBuilder=new StringBuilder();



        for(int i=0;i<oglasi.size();i++) {
            HashMap<String, String> oglas = oglasi.get(i);
            nazivi.add(oglas.get("naziv"));
            predmeti.add(oglas.get("predmet"));
            izdavaci.add(oglas.get("izdavac"));
            godineIzdanja.add(oglas.get("godinaIzdanja"));
        }

        layoutManager = new GridLayoutManager(context, 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        AdapterKnjige adapterKnjige = new AdapterKnjige(context,nazivi,izdavaci,godineIzdanja);
        recyclerView.setAdapter(adapterKnjige);
        //Log.d("Letoviiii",stringBuilder.toString());
    }
}
