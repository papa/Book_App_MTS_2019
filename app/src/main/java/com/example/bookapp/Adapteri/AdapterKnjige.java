package com.example.bookapp.Adapteri;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.Klase.Knjiga;
import com.example.bookapp.Klase.Oglas;
import com.example.bookapp.KnjigaPregledActivity;
import com.example.bookapp.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdapterKnjige extends RecyclerView.Adapter<AdapterKnjige.KnjigeHolder>{
    private Context context;
    private ArrayList<Bitmap> slike=new ArrayList<>();

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://knjigeapp.appspot.com");
    private StorageReference storageReference;

    private ArrayList<Oglas> oglasi;
    private ArrayList<Knjiga> knjige;


    public AdapterKnjige(Context context, ArrayList<Bitmap> slike,ArrayList<Oglas> og,ArrayList<Knjiga> knj)
    {
        this.context = context;
        this.slike = slike;
        this.oglasi=og;
        this.knjige=knj;
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

        Oglas oo  = oglasi.get(i);
        Knjiga kk = knjige.get(i);

        //TODO
        viewHolder.naziv.setText(kk.getNaziv());
        //viewHolder.autor.setText(convertAutor(kk.getAutori()));
        viewHolder.cena.setText(String.valueOf(oo.getCena()));
        viewHolder.predmet.setText(kk.getPredmet());
        //viewHolder.godinaIzdanja.setText(String.valueOf(kk.getGodinaIzdanja()));
        viewHolder.izdavac.setText(kk.getIzdavac());
        //viewHolder.opis.setText(oo.getDodatniOpis());
        if(slike.size()>i)
            viewHolder.slika.setImageBitmap(slike.get(i));
        else
            viewHolder.slika.setImageResource(R.drawable.googleg_disabled_color_18);
        viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Na klik.

                Intent intent=new Intent(context, KnjigaPregledActivity.class);
                intent.putExtra("idOglasa",oglasi.get(i).getId());
                intent.putExtra("idKorisnika",oglasi.get(i).getIdUsera());
                intent.putExtra("naziv",knjige.get(i).getNaziv());
                //Za autora se salje array list, ako treba string stavite convert autor i parametar autori.get(i);
                intent.putExtra("autor",knjige.get(i).getAutori());
                intent.putExtra("cena",oglasi.get(i).getCena());
                intent.putExtra("predmet",knjige.get(i).getPredmet());
                intent.putExtra("godinaIzdanja",knjige.get(i).getGodinaIzdanja());
                intent.putExtra("izdavac",knjige.get(i).getIzdavac());
                intent.putExtra("opis",oglasi.get(i).getDodatniOpis());

                KnjigaPregledActivity.oglas = oglasi.get(i);
                KnjigaPregledActivity.knjiga = knjige.get(i);

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
        return oglasi.size();
    }

    //Kreiranje holdera.
    public static class KnjigeHolder extends RecyclerView.ViewHolder
    {

        //Atributi koji se sastoje u grid_layout.
        private TextView naziv,autor,cena,izdavac,godinaIzdanja,predmet,opis;
        private ImageView slika;
        private ConstraintLayout itemLayout;

        public KnjigeHolder(@NonNull View itemView)
        {
            super(itemView);
            naziv=(TextView)itemView.findViewById(R.id.tNaziv);
            //autor=(TextView)itemView.findViewById(R.id.tAutor);
            cena=(TextView)itemView.findViewById(R.id.tCena);
            slika=(ImageView)itemView.findViewById(R.id.iSlika);
            izdavac=(TextView)itemView.findViewById(R.id.tIzdavac);
            //godinaIzdanja=(TextView)itemView.findViewById(R.id.tGodIzdanja);
            predmet=(TextView)itemView.findViewById(R.id.tPredmet);
            //opis=(TextView)itemView.findViewById(R.id.tOpis);
            itemLayout=(ConstraintLayout)itemView.findViewById(R.id.knjige_i);
        }
    }
}
