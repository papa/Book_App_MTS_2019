package com.example.bookapp.Klase.Oglasi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.Adapteri.AdapterKnjige;
import com.example.bookapp.Klase.Knjiga;
import com.example.bookapp.Klase.Oglas;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

import static android.app.Activity.RESULT_OK;

public class CitanjeOglasa {

    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Bitmap> slike = new ArrayList<>();

    private DatabaseReference databaseReference1;
    private ArrayList<Knjiga> knjige = new ArrayList<>();
    private String ID;
    private int brojOglasa = 0;

    private Context context;

    private ArrayList<Oglas> oglasi = new ArrayList<>();

    ProgressDialog progressDialog;


    public CitanjeOglasa() {
    }

    public interface MyCallback {
        void onCallback(Knjiga value);
    }

    public interface CallbackA {
        void onCallback(ArrayList<Knjiga> value);
    }

    public interface CallbackSlika {
        void onCallback(ArrayList<Bitmap> value);
    }

    private void citajKnjigaInfo(final MyCallback myCallback)
    {
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Knjiga knjiga = dataSnapshot.getValue(Knjiga.class);

                myCallback.onCallback(knjiga);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getOglas(final CallbackA myCallback) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Oglasi").child(ID);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Oglas oglas = dataSnapshot.getValue(Oglas.class);

                oglasi.add(oglas);

                databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Knjige").child(oglas.getIdKnjige());

                citajKnjigaInfo(new MyCallback() {
                    @Override
                    public void onCallback(Knjiga value) {
                        knjige.add(value);

                        myCallback.onCallback(knjige);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void procitaj(ArrayList<String> idOglasa, final RecyclerView recyclerView2, final Context c)
    {
        brojOglasa = idOglasa.size();
        context=c;

//        progressDialog=new ProgressDialog(context);
//        progressDialog.show();
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Ucitavanje...");

        for (int i = 0; i < brojOglasa; i++)
        {
            ID = idOglasa.get(i);

            getOglas(new CallbackA() {
                @Override
                public void onCallback(ArrayList<Knjiga> value) {
                    if(value.size()==brojOglasa) {
                        ArrayList<Knjiga> knjigas = value;
                        ucitajSliku(oglasi, new CallbackSlika() {
                            @Override
                            public void onCallback(ArrayList<Bitmap> value) {
                                if (brojOglasa == value.size())
                                    prikaziOglase(knjigas, recyclerView2, c);
                            }
                        });
                    }

                }
            });
        }

    }

    private void ucitajSliku(ArrayList<Oglas> oglass, final CallbackSlika callbackSlika) {

        Log.d("ID",String.valueOf(oglass.size()));
        for (int j = 0; j < oglass.size(); j++) {
            //Ovaj for je ako se citaju sve tri slika(provera se da li ih ima al ono)
            //kom jer mi ne trebaju tri nego samo prva slika
            //for (int i = 0; i < 3; i++) {

            final Bitmap[] my_image = new Bitmap[1];
            //StorageReference ref = FirebaseStorage.getInstance().getReference().child(user.getUid()).child("Knjiga").child(oglass.get(j).getId()).child("0image.jpg");
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(oglass.get(j).getIdUsera()).child("Knjiga").child(oglass.get(j).getId()).child("0image.jpg");
            Log.d("PUTANJA2",ref.getPath());
            try {
                final File localFile = File.createTempFile("Images", "jpg");
                ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        my_image[0] = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        slike.add(my_image[0]);

                        callbackSlika.onCallback(slike);
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
        //progressDialog.dismiss();
    }

    private void prikaziOglase(ArrayList<Knjiga> knjige, RecyclerView recyclerView, Context context)
    {
        if (knjige.size() == brojOglasa)
        {
            layoutManager = new GridLayoutManager(context, 1);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            AdapterKnjige adapterKnjige = new AdapterKnjige(context, slike,oglasi,knjige);
            recyclerView.setAdapter(adapterKnjige);
        }
    }

    public ArrayList<Oglas> uzmiOglase()
    {
        return oglasi;
    }

    public ArrayList<Knjiga> uzmiKnjige()
    {
        return knjige;
    }
    public ArrayList<Bitmap> uzmiSlike(){
        return slike;
    }
}
