package com.example.bookapp.Fragmenti;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.Adapteri.AdapterKnjige;
import com.example.bookapp.Adapteri.AdapterPoruke;
import com.example.bookapp.Klase.Knjiga;
import com.example.bookapp.Klase.Korisnik;
import com.example.bookapp.Klase.Oglas;
import com.example.bookapp.Klase.Oglasi.CitanjeOglasa;
import com.example.bookapp.KnjigaDodavanjeActivity;
import com.example.bookapp.Notifications.Token;
import com.example.bookapp.ProfileActivity;
import com.example.bookapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class FragmentPoruke extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseUser us;
    ArrayList<Korisnik> korisnici;
    ArrayList<Bitmap> slike;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<Oglas> oglasi;
    AdapterPoruke adapterPoruke;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_poruke, container, false);
        setHasOptionsMenu(true);
        initialize(view);

        //nextImage();

        //optimizovano

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actbar, menu);
    }

    private void postaviAdapter()
    {
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapterPoruke = new AdapterPoruke(getContext(), slike,oglasi);
        recyclerView.setAdapter(adapterPoruke);
        ucitajIzBaze();

       // adapterPoruke.notifyDataSetChanged();
    }

    private void postaviListenere()
    {


    }

    public void updateToken(String token)
    {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1=new Token(token);
        reference.child(us.getUid()).setValue(token1);
    }

    private void ocitaj(String id)
    {
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("Korisnici").child(id);

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                korisnici.add(dataSnapshot.getValue(Korisnik.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ucitajIzBaze()
    {
        if(us!=null)
        {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Korisnici").child(us.getUid()).child("porukeNudi");
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot sn : dataSnapshot.getChildren())
                    {
                        for (DataSnapshot og : sn.getChildren())
                        {
                            oglasi.add(og.getValue(Oglas.class));
                            //adapterPoruke.notifyDataSetChanged();
                        }

                        Collections.reverse(oglasi);
                        adapterPoruke.notifyDataSetChanged();
                        //  adapterPoruke.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            updateToken(FirebaseInstanceId.getInstance().getToken());

      /*  db = FirebaseDatabase.getInstance().getReference().child("Korisnici").child(us.getUid()).child("porukeNudi");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot og : dataSnapshot.getChildren())
                {
                    oglasi.add(og.getValue(Oglas.class));
                }

                if(oglasi.size()==0)
                {
                    //todo
                    //prikazi poruku da nema nista
                }
                else
                {
                    Collections.reverse(oglasi);
                    adapterPoruke.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        }
    }



    private void initialize(View view)
    {
        Toast.makeText(getContext(),"Fragment poruke",Toast.LENGTH_SHORT).show();

        oglasi= new ArrayList<>();
        //todo
        //prepravi da prihvata oglase
       // adapterPoruke = new AdapterPoruke(oglasi);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerPoruke);
        us = FirebaseAuth.getInstance().getCurrentUser();
        slike = new ArrayList<>();
        postaviAdapter();
    }

}
