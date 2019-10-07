package com.example.bookapp.Fragmenti;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.Adapteri.AdapterKnjige;
import com.example.bookapp.R;

import java.util.ArrayList;

public class FragmentKnjige extends Fragment implements View.OnClickListener{
    private ArrayList<Bitmap> slike=new ArrayList<>();
    private ArrayList<String> nazivi=new ArrayList<>();
    private ArrayList<String> autori=new ArrayList<>();
    private ArrayList<String> cene=new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_knjige, container, false);

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerKnjige);

        //citanje iz baze da se popune nizovi

        //Za testiranje
        nazivi.add("Veneova zbirka");
        nazivi.add("Filosofija");
        autori.add("Zavod");
        autori.add("Hristina profesorka");
        cene.add("350");
        cene.add("1900");

        setRecycler();

        return view;
    }

    @Override
    public void onClick(View v) {
    }

    private void setRecycler()
    {
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        AdapterKnjige adapterKnjige= new AdapterKnjige(getContext(),nazivi,autori,cene);
        recyclerView.setAdapter(adapterKnjige);
    }
}
