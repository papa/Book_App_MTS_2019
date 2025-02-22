package com.example.bookapp.Fragmenti;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.provider.MediaStore;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bookapp.Adapteri.AdapterKnjige;
import com.example.bookapp.Klase.Knjiga;
import com.example.bookapp.Klase.Oglas;
import com.example.bookapp.Klase.Oglasi.CitanjeOglasa;
import com.example.bookapp.KnjigaDodavanjeActivity;
import com.example.bookapp.PodesavanjaNalogaActivity;
import com.example.bookapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FragmentKnjige extends Fragment implements View.OnClickListener
{
    private ArrayList<Bitmap> slike = new ArrayList<Bitmap>();
    private ArrayList<String> izdavaci = new ArrayList<String>();

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private CheckBox cenaRB;
    private CheckBox predmetRB;
    private CheckBox izdavacRB;
    private EditText cenaET;
    private EditText predmetET;
    private EditText izdavacET;
    private Button filter;
    private FloatingActionButton filtriranjeFloatingButton;
    SwipeRefreshLayout refreshKnjige;

    boolean f1 = false;
    boolean f2 = false;
    boolean f3 = false;
    int cenaf=0;
    String predmetf="";
    String izdavacf="";

    boolean popunjeno=false;


    //Moje promenljive
    private ArrayList<String> idOglasa=new ArrayList<>();


    private ImageView slikeKnjige;

    private int[] imageArray;
    private int currentIndex;
    private int startIndex;
    private int endIndex;

    FirebaseUser user;

    AdapterKnjige adapterKnjige;

   ArrayList<Oglas> oglasii;
   ArrayList<Knjiga> knjigee;

   FirebaseUser userr;

   private Dialog dialog;
   private Spinner spIzdavaci;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.filterButtonMenu){
            prikaziPopUp();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        refreshKnjige=view.findViewById(R.id.refresherKnjige);
        refreshKnjige.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(userr!=null)
                {
                    oglasii.clear();
                    knjigee.clear();
                    slike.clear();
                    ucitajIzBaze();
                }
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_knjige, container, false);
        setHasOptionsMenu(true);
        initialize(view);

        userr = FirebaseAuth.getInstance().getCurrentUser();

        //nextImage();

        //optimizovano
        if(userr!=null)
        {
            ucitajIzBaze();
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actbar, menu);
    }

    //<editor-fold desc="Ovo mi je animacija da se menjaju slike, koristicu je na drugom mesto al neka je, samo smanjite ovaj deo da ne smeta">
    private void nextImage()
    {
        slikeKnjige.setImageResource(imageArray[currentIndex]);
        //Animation rotateimage = AnimationUtils.loadAnimation(this, R.anim.custom_anim);
        //slikeKnjige.startAnimation(rotateimage);
        currentIndex++;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(currentIndex>endIndex){
                    currentIndex--;
                    previousImage();
                }else{
                    nextImage();
                }

            }
        },1000); // here 1000(1 second) interval to change from current  to next image

    }

    private void previousImage()
    {
        slikeKnjige.setImageResource(imageArray[currentIndex]);
        //Animation rotateimage = AnimationUtils.loadAnimation(this, R.anim.custom_anim);
        //slikeKnjige.startAnimation(rotateimage);
        currentIndex--;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(currentIndex<startIndex){
                    currentIndex++;
                    nextImage();
                }else{
                    previousImage(); // here 1000(1 second) interval to change from current  to previous image
                }
            }
        },1000);

    }
    //</editor-fold>

    //<editor-fold desc="postavilistener">
    private void postaviListenere()
    {
        /*dodavanjeKnjigeFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KnjigaDodavanjeActivity.class);
                startActivity(intent);
            }
        });*/
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                 if(cenaRB.isChecked())
                 {
                     f1  = true;
                     if(cenaET.getText().toString().trim().isEmpty()) cenaf = 100000000;
                     else cenaf = Integer.valueOf(cenaET.getText().toString().trim());
                 }

                 if(predmetRB.isChecked())
                 {
                     f2 = true;
                     if(predmetET.getText().toString().trim().isEmpty())
                     {
                         f2 = false;
                         Toast.makeText(getContext(),"Morate uneti odredjeni predmet",Toast.LENGTH_LONG).show();
                     }
                     else
                     {
                         predmetf = predmetET.getText().toString().trim();
                     }
                 }
                if(izdavacRB.isChecked())
                {
                    f3 = true;
                    if(izdavacf.equals(""))
                    {
                        f3 = false;
                        Toast.makeText(getContext(),"Morate uneti odredjenog izdavaca",Toast.LENGTH_LONG).show();
                    }
                }

                if(!f1 && !f2 && !f3)
                {
                    layoutManager = new GridLayoutManager(getContext(), 1);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(layoutManager);
                    AdapterKnjige adapterKnjige = new AdapterKnjige(getContext(), slike,oglasii,knjigee);
                    recyclerView.setAdapter(adapterKnjige);
                    adapterKnjige.notifyDataSetChanged();
                    return;
                }

                ArrayList<Oglas> of = new ArrayList<>();
                ArrayList<Knjiga> kf = new ArrayList<>();

                layoutManager = new GridLayoutManager(getContext(), 1);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                AdapterKnjige adapterKnjige = new AdapterKnjige(getContext(), slike,of,kf);
                recyclerView.setAdapter(adapterKnjige);
                adapterKnjige.notifyDataSetChanged();

                for(int i=0;i<oglasii.size();i++)
                {
                    Oglas o  = oglasii.get(i);
                    Knjiga k = knjigee.get(i);
                    boolean prolazi = true;
                    if(f1)
                    {
                        if(o.getCena() >  cenaf)
                        {
                            prolazi=false;
                        }
                    }
                    if(f2)
                    {
                        if(!k.getPredmet().equals(predmetf))
                        {
                            prolazi=false;
                        }
                    }
                    if(f3)
                    {
                        if(!k.getIzdavac().equals(izdavacf))
                        {
                            prolazi=false;
                        }
                    }

                    if(prolazi)
                    {
                        of.add(o);
                        kf.add(k);
                        adapterKnjige.notifyDataSetChanged();
                    }

                    dialog.dismiss();

                }
            }
        });

    }
    //</editor-fold>

    private void prikaziPopUp()
    {
        dialog.setContentView(R.layout.filteri_pop_up);
        cenaRB=(CheckBox)dialog.findViewById(R.id.cenaCheckBox);
        predmetRB=(CheckBox)dialog.findViewById(R.id.predmetCheckBox);
        izdavacRB=(CheckBox)dialog.findViewById(R.id.izdavacCheckBox);
        cenaET=(EditText)dialog.findViewById(R.id.cenaFilterText);
        predmetET=(EditText)dialog.findViewById(R.id.predmetFilterText);
        spIzdavaci=(Spinner)dialog.findViewById(R.id.spIzdavac);
        filter=(Button)dialog.findViewById(R.id.filterButton);

        setSpinner(spIzdavaci);

        postaviListenere();

        dialog.show();

    }

    private void setSpinner(final Spinner spinner)
    {
        if(!popunjeno) {
            for (int i = 0; i < knjigee.size(); i++) {
                izdavaci.add(knjigee.get(i).getIzdavac());
            }
            popunjeno=true;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,izdavaci);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                izdavacf = String.valueOf(spinner.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filtriranjeFloatingButton:prikaziPopUp();break;
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

    //<editor-fold desc="Baza">
    private void ucitajIzBaze()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Oglasi");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                     Oglas o = snapshot.getValue(Oglas.class);
                     if(!o.getIdUsera().equals(user.getUid()))
                     {
                         oglasii.add(o);
                         procitajKnjigu(o.getIdKnjige());
                     }
                }

                ucitajSliku(oglasii);

               // procitajKnjige();

                //citanje();
                refreshKnjige.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void citanje()
    {
        CitanjeOglasa citanjeOglasa = new CitanjeOglasa(2);

        if(idOglasa.isEmpty()) Toast.makeText(getContext(),"Nema nista bla bla",Toast.LENGTH_LONG).show();
        else Toast.makeText(getContext(),"Ima svasta bla bla",Toast.LENGTH_LONG).show();

        citanjeOglasa.procitaj(idOglasa, recyclerView, getContext());
        oglasii = citanjeOglasa.uzmiOglase();
        knjigee = citanjeOglasa.uzmiKnjige();
        Toast.makeText(getContext(), "kknjige "+String.valueOf(knjigee.size())+" oglasi "+String.valueOf(oglasii.size()), Toast.LENGTH_SHORT).show();
    }
    //</editor-fold>


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


    private void initialize(View view)
    {
        //Toast.makeText(getContext(),"Fragment knjige",Toast.LENGTH_SHORT).show();
        slike = new ArrayList<Bitmap>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        oglasii =new ArrayList<>();
        knjigee = new ArrayList<>();
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerKnjige);
        layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapterKnjige = new AdapterKnjige(getContext(), slike,oglasii,knjigee);
        recyclerView.setAdapter(adapterKnjige);

        filtriranjeFloatingButton=(FloatingActionButton)view.findViewById(R.id.filtriranjeFloatingButton);
        filtriranjeFloatingButton.setOnClickListener(this);
        dialog=new Dialog(getContext());
        imageArray = new int[8];
        imageArray[0] = R.drawable.ic_launcher_background;
        imageArray[1] = R.drawable.ic_launcher_foreground;
        imageArray[2] = R.drawable.ic_launcher_background;
        imageArray[3] = R.drawable.ic_launcher_background;
        imageArray[4] = R.drawable.ic_launcher_background;
        imageArray[5] = R.drawable.ic_launcher_background;
        imageArray[6] = R.drawable.ic_launcher_background;
        imageArray[7] = R.drawable.ic_launcher_background;

        startIndex = 0;
        endIndex = 7;

    }
}
