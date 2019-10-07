package com.example.bookapp.Adapteri;

import android.content.Context;
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

import com.example.bookapp.R;

import java.util.ArrayList;

public class AdapterKnjige extends RecyclerView.Adapter<AdapterKnjige.KnjigeHolder>{
    private Context context;
    private ArrayList<Bitmap> slike=new ArrayList<>();
    private ArrayList<String> nazivi=new ArrayList<>();
    private ArrayList<String> autori=new ArrayList<>();
    private ArrayList<String> cene=new ArrayList<>();

    public AdapterKnjige(Context context, ArrayList<String> nazivi, ArrayList<String> autori, ArrayList<String> cene) {
        this.context = context;
        //this.slike = slike;
        this.nazivi = nazivi;
        this.autori = autori;
        this.cene = cene;
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
        viewHolder.autor.setText(autori.get(i));
        viewHolder.cena.setText(cene.get(i));
        //viewHolder.slika.setImageBitmap(slike.get(i));
        viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Na klik.
            }
        });
    }

    @Override
    public int getItemCount() {
        return nazivi.size();
    }

    //Kreiranje holdera.
    public static class KnjigeHolder extends RecyclerView.ViewHolder{

        //Atributi koji se sastoje u grid_layout.
        private TextView naziv,autor,cena;
        private ImageView slika;
        private ConstraintLayout itemLayout;

        public KnjigeHolder(@NonNull View itemView) {
            super(itemView);
            naziv=(TextView)itemView.findViewById(R.id.tNaziv);
            autor=(TextView)itemView.findViewById(R.id.tAutor);
            cena=(TextView)itemView.findViewById(R.id.tCena);
            itemLayout=(ConstraintLayout)itemView.findViewById(R.id.knjige_i);
        }
    }
}
