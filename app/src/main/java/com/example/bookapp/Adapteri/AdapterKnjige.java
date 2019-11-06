package com.example.bookapp.Adapteri;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.KnjigaPregledActivity;
import com.example.bookapp.R;

import java.util.ArrayList;

public class AdapterKnjige extends RecyclerView.Adapter<AdapterKnjige.KnjigeHolder>{
    private Context context;
    private ArrayList<Bitmap> slike=new ArrayList<>();
    private ArrayList<String> nazivi=new ArrayList<>();
    private ArrayList<ArrayList<String>> autori=new ArrayList<>();
    private ArrayList<String> predmeti=new ArrayList<>();
    private ArrayList<String> izdavaci=new ArrayList<>();
    private ArrayList<String> godineIzdanja=new ArrayList<>();
    private ArrayList<String> cene=new ArrayList<>();
    private ArrayList<String> dodatniOpis=new ArrayList<>();
    private ArrayList<String> brojZainteresovanih=new ArrayList<>();

    public AdapterKnjige(Context context, ArrayList<Bitmap> slike, ArrayList<String> nazivi, ArrayList<ArrayList<String>> autori, ArrayList<String> predmeti, ArrayList<String> izdavaci, ArrayList<String> godineIzdanja, ArrayList<String> cene, ArrayList<String> dodatniOpis, ArrayList<String> brojZainteresovanih) {
        this.context = context;
        this.slike = slike;
        this.nazivi = nazivi;
        this.autori = autori;
        this.predmeti = predmeti;
        this.izdavaci = izdavaci;
        this.godineIzdanja = godineIzdanja;
        this.cene = cene;
        this.dodatniOpis = dodatniOpis;
        this.brojZainteresovanih = brojZainteresovanih;
    }

    @NonNull
    @Override
    public KnjigeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layout= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.knjige_item,null);
        KnjigeHolder myHolder=new KnjigeHolder(layout);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final KnjigeHolder viewHolder, final int i) {
        viewHolder.naziv.setText(nazivi.get(i));
        viewHolder.autor.setText(convertAutor(autori.get(i)));
        viewHolder.cena.setText(cene.get(i));
        viewHolder.predmet.setText(predmeti.get(i));
        viewHolder.godinaIzdanja.setText(godineIzdanja.get(i));
        viewHolder.izdavac.setText(izdavaci.get(i));
        viewHolder.opis.setText(dodatniOpis.get(i));
        //viewHolder.slika.setImageBitmap(slike.get(i));
        viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Na klik.

                Intent intent=new Intent(context, KnjigaPregledActivity.class);
                intent.putExtra("naziv",nazivi.get(i));
                //Za autora se salje array list, ako treba string stavite convert autor i parametar autori.get(i);
                intent.putExtra("autor",autori.get(i));
                intent.putExtra("cena",cene.get(i));
                intent.putExtra("predmet",predmeti.get(i));
                intent.putExtra("godinaIzdanja",godineIzdanja.get(i));
                intent.putExtra("izdavac",izdavaci.get(i));
                intent.putExtra("opis",dodatniOpis.get(i));
                context.startActivity(intent);
            }
        });
    }

    private String convertAutor(ArrayList<String> a)
    {
        StringBuilder stringBuilder=new StringBuilder();

        for(int i=0;i<a.size();i++)
        {
            stringBuilder.append(a.get(i));
        }

        return stringBuilder.toString();
    }

    @Override
    public int getItemCount() {
        return nazivi.size();
    }

    //Kreiranje holdera.
    public static class KnjigeHolder extends RecyclerView.ViewHolder{

        //Atributi koji se sastoje u grid_layout.
        private TextView naziv,autor,cena,izdavac,godinaIzdanja,predmet,opis;
        private ImageView slika;
        private ConstraintLayout itemLayout;

        public KnjigeHolder(@NonNull View itemView)
        {
            super(itemView);
            naziv=(TextView)itemView.findViewById(R.id.tNaziv);
            autor=(TextView)itemView.findViewById(R.id.tAutor);
            cena=(TextView)itemView.findViewById(R.id.tCena);
            izdavac=(TextView)itemView.findViewById(R.id.tIzdavac);
            godinaIzdanja=(TextView)itemView.findViewById(R.id.tGodIzdanja);
            predmet=(TextView)itemView.findViewById(R.id.tPredmet);
            opis=(TextView)itemView.findViewById(R.id.tOpis);
            itemLayout=(ConstraintLayout)itemView.findViewById(R.id.knjige_i);
        }
    }
}
