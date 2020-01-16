package com.example.bookapp.Fragmenti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.Adapteri.AdapterKnjige;
import com.example.bookapp.Klase.Knjiga;
import com.example.bookapp.Klase.Korisnik;
import com.example.bookapp.Klase.Oglas;
import com.example.bookapp.Klase.Oglasi.CitanjeOglasa;
import com.example.bookapp.KnjigaDodavanjeActivity;
import com.example.bookapp.MainActivity;
import com.example.bookapp.PodesavanjaNalogaActivity;
import com.example.bookapp.ProfileActivity;
import com.example.bookapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FragmentProfil extends Fragment implements View.OnClickListener {

    private TextView tvIme,tvEmail,tvBrojOcena,tvProsecnaOcena;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String ime,prezime,email;
    private int brojOcena;
    private double prosecnaOcena;
    private ImageView slika;
    private FloatingActionButton dodavanjeKnjigeFloatingButton;

    ArrayList<Bitmap> slike;

    AdapterKnjige adapterKnjige;
    private RecyclerView.LayoutManager layoutManager;

    private RecyclerView recyclerView;

    private ArrayList<String> idOglasa=new ArrayList<>();

    private Button nalogP;
    ArrayList<Oglas> oglasii;
    ArrayList<Knjiga> knjigee;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        initialize(view);

        citajBazu();

        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nalog:{
                Intent intent = new Intent(getActivity(), PodesavanjaNalogaActivity.class);
                startActivity(intent);
            }break;
            case R.id.dodajKnjiguFloatingButton:{
                Intent intent = new Intent(getActivity(), KnjigaDodavanjeActivity.class);
                startActivity(intent);
            }break;
        }
    }

    private void procitajKnjigu(String idk)
    {
        DatabaseReference datab = FirebaseDatabase.getInstance().getReference().child("Knjige").child(idk);

        datab.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Knjiga k = dataSnapshot.getValue(Knjiga.class);
                knjigee.add(k);

                //citanje();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ucitajIzBaze()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Oglasi");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Oglas o = snapshot.getValue(Oglas.class);
                    if(o.getIdUsera().equals(user.getUid()))
                    {
                        oglasii.add(o);
                        procitajKnjigu(o.getIdKnjige());
                    }
                }

                ucitajSliku(oglasii);

                // procitajKnjige();

                //citanje();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initialize(View view)
    {
        Toast.makeText(getContext(),"Fragment profil",Toast.LENGTH_SHORT).show();

        tvIme=(TextView)view.findViewById(R.id.tpIme);
        tvEmail=(TextView)view.findViewById(R.id.tpEmail);
        tvBrojOcena=(TextView)view.findViewById(R.id.tpBroj);
        tvProsecnaOcena=(TextView)view.findViewById(R.id.tpProsek);

        slike = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        oglasii =new ArrayList<>();
        knjigee = new ArrayList<>();
        recyclerView=(RecyclerView)view.findViewById(R.id.recViewProfile);
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapterKnjige = new AdapterKnjige(getContext(), slike,oglasii,knjigee);
        recyclerView.setAdapter(adapterKnjige);

        slika=(ImageView)view.findViewById(R.id.slikaProfil);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Korisnici").child(user.getUid());
        dodavanjeKnjigeFloatingButton=view.findViewById(R.id.dodajKnjiguFloatingButton);
        dodavanjeKnjigeFloatingButton.setOnClickListener(this);
        nalogP=view.findViewById(R.id.nalog);
        nalogP.setOnClickListener(this);
    }

    private void citajBazu()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                citajInfoKorisnik(dataSnapshot);

                Log.d("USO","USO");

                upisi();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ucitajSliku(ArrayList<Oglas> oglass)
    {

        for (int j = 0; j < oglass.size(); j++)
        {
            //Ovaj for je ako se citaju sve tri slika(provera se da li ih ima al ono)
            //kom jer mi ne trebaju tri nego samo prva slika
            //for (int i = 0; i < 3; i++) {

            final Bitmap[] my_image = new Bitmap[1];
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(oglass.get(j).getIdUsera()).child("Knjiga").child(oglass.get(j).getId()).child("0image.jpg");
            Log.d("PUTANJA2", ref.getPath());
            try {
                final File localFile = File.createTempFile("Images", "jpg");
                ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        my_image[0] = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        slike.add(my_image[0]);
                        adapterKnjige.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(context, e.getMessage()+"ojsa", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            //}
        }
    }

    private void citajInfoKorisnik(DataSnapshot dataSnapshot)
    {
        Korisnik korisnik = dataSnapshot.getValue(Korisnik.class);

        if(korisnik!=null)
        {
            ime = korisnik.getIme();
            prezime = korisnik.getPrezime();
            email = korisnik.getMail();
            brojOcena = korisnik.getBrojOcena();
            prosecnaOcena = korisnik.getProsecnaOcena();

            if (dataSnapshot.hasChild("oglasi"))
            {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("oglasi").getChildren()) {
                    if (!idOglasa.contains(dataSnapshot1.getKey()))
                        idOglasa.add(dataSnapshot1.getKey());
                }
                ucitajIzBaze();
            }
            else
                Log.d("NIJE USO", "NEMAJU OGLASE");
        }
        else
        {
            startActivity(new Intent(getContext(), MainActivity.class));
            getActivity().finish();
        }
    }

    private void citanje()
    {

        CitanjeOglasa citanjeOglasa = new CitanjeOglasa(1);

        Log.d("USO3","USO3");

        //if(idOglasa.isEmpty()) Toast.makeText(getContext(),"Nema nista",Toast.LENGTH_LONG).show();
        //else Toast.makeText(getContext(),"Ima svasta",Toast.LENGTH_LONG).show();
        citanjeOglasa.procitaj(idOglasa, recyclerView, getContext());
    }

    private void upisi()
    {
        String outputIme = ime + " " + prezime;
        tvIme.setText(outputIme);
        tvProsecnaOcena.setText(String.valueOf(prosecnaOcena));
        tvBrojOcena.setText(String.valueOf(brojOcena));
        tvEmail.setText(email);
    }
}
