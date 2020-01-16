package com.example.bookapp.Adapteri;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.Klase.Korisnik;
import com.example.bookapp.Klase.Oglas;
import com.example.bookapp.KnjigaDodavanjeActivity;
import com.example.bookapp.KnjigaPregledActivity;
import com.example.bookapp.MessageActivity;
import com.example.bookapp.R;
import com.google.android.gms.vision.text.Line;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdapterPoruke extends RecyclerView.Adapter<AdapterPoruke.PorukeHolder>
{
    private Context context;
    private ArrayList<Bitmap> slike;
    private ArrayList<Oglas> oglasi;
    private FirebaseStorage storage;

    public AdapterPoruke(Context context,ArrayList<Bitmap> slike,ArrayList<Oglas> oglasi)
    {
        this.context = context;
        this.slike = slike;
        this.oglasi = oglasi;
    }


    @NonNull
    @Override
    public PorukeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //todo
        //treba poruke item a ne knjige item
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.poruke_kartica,null);
        PorukeHolder porukeHolder = new PorukeHolder(layout);
        return porukeHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PorukeHolder viewHolder, int position) {

            Oglas o = oglasi.get(position);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Korisnici").child(o.getIdUsera());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Korisnik k = dataSnapshot.getValue(Korisnik.class);
                    String s = k.getIme() + " " + k.getPrezime();
                    viewHolder.imePrezime.setText(s);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            //todo
            //Andrija da pogleda za slike
        /*if(slike.size()>position)
            viewHolder.slika.setImageBitmap(slike.get(position));
        else
            viewHolder.slika.setImageResource(R.drawable.googleg_disabled_color_18);*/

            viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Na klik.

                    Intent intent = new Intent(context, MessageActivity.class);
                    MessageActivity.ogg = o;
                    intent.putExtra("drugiId", o.getIdUsera());
                    context.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return oglasi.size();
    }

    public static class PorukeHolder extends RecyclerView.ViewHolder
    {
       private TextView imePrezime;
       private TextView zadnjaPoruka;
       private TextView datum;
       private ImageView slika;
        private LinearLayout layout;

       public PorukeHolder(@NonNull View itemView)
       {
           super(itemView);
           imePrezime = (TextView)itemView.findViewById(R.id.imePoruke);
           slika = (ImageView)itemView.findViewById(R.id.slikaPoruke);
           layout=(LinearLayout) itemView.findViewById(R.id.poruke_i);
       }
    }
}
