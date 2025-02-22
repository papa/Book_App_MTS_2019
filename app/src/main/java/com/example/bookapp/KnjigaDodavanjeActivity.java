package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bookapp.Klase.Knjiga;
import com.example.bookapp.Klase.Korisnik;
import com.example.bookapp.Klase.Oglas;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.InternalTokenProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ivan200.photobarcodelib.PhotoBarcodeScanner;
import com.ivan200.photobarcodelib.PhotoBarcodeScannerBuilder;

import java.util.ArrayList;

public class KnjigaDodavanjeActivity extends AppCompatActivity {
    private ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();
    private ArrayList<String> knjigeString = new ArrayList<String>();
    private Spinner spinner;
    private Button novaKnjiga, btnUpisi, barkodSkenerButton, navbar;
    private DatabaseReference databaseReference,databaseReference2;
    private EditText cenaKnjige,opisKnjige;

    private String opis,id,idKnjiga,idUser,idKnjige = "nema";
    private ImageView slikeKnjige,slikeKnjige2,slikeKnjige3;
    private int PICK_IMAGE_REQUEST = 111,br=0,cena;
    private Uri filePath;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://knjigeapp.appspot.com");

    private ProgressDialog progressDialog;

    private StorageReference childRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knjiga_dodavanje);

        initialize();

        postaviListenere();
    }

    private void postaviListenere()
    {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Knjiga added = dataSnapshot1.getValue(Knjiga.class);

                    knjige.add(added);
                }

                buildString();

                setSpinner(spinner, "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        novaKnjiga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(KnjigaDodavanjeActivity.this, NovaVrstaKnjigeActivity.class);
                KnjigaDodavanjeActivity.this.startActivityForResult(myIntent, 1);
            }
        });

        btnUpisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(idKnjige.equals("nema"))
                {
                    Toast.makeText(KnjigaDodavanjeActivity.this, "Odaberite knjigu iz liste ili dodajte novu", Toast.LENGTH_SHORT).show();
                }
                else if (filePath != null)
                {
                    boolean b = citajEditTextove();
                    if(b) upisiSliku();
                    //dodajOglas();
                }
                else
                {
                    Toast.makeText(KnjigaDodavanjeActivity.this,"Morate dodati barem jednu sliku!",Toast.LENGTH_LONG).show();
                }

            }
        });
        barkodSkenerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeBarcode();
            }
        });
        slikeKnjige.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickBook();
            }
        });
        slikeKnjige2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickBook();
            }
        });
        slikeKnjige3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickBook();
            }
        });
    }

    private void pickBook()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }
    private ArrayList<String> buildString()
    {
        for(Knjiga k:knjige)
        {
            StringBuilder stringBuilder=new StringBuilder();

            stringBuilder.append(k.getNaziv());
            for(String s: k.getAutori())
            {
                stringBuilder.append(" "+s);
            }

            knjigeString.add(stringBuilder.toString());
        }

        return knjigeString;
    }

    private void initialize()
    {

        novaKnjiga = findViewById(R.id.novaKnjigaActivityButton);
        btnUpisi=(Button)findViewById(R.id.btUpisi);
        barkodSkenerButton=(Button)findViewById(R.id.barkodSkenerButton);
        //navbar = findViewById(R.id.newNavBarYay);

        spinner = findViewById(R.id.youSpinMeRightRound);

        cenaKnjige=(EditText)findViewById(R.id.cenaK);
        opisKnjige=(EditText)findViewById(R.id.dodOpisK);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Knjige");
        databaseReference2 = FirebaseDatabase.getInstance().getReference();

        slikeKnjige=(ImageView)findViewById(R.id.slikaKnjige);
        slikeKnjige2=(ImageView)findViewById(R.id.slikaKnjige2);
        slikeKnjige3=(ImageView)findViewById(R.id.slikaKnjige3);
        progressDialog=new ProgressDialog(this);
    }

    private void setSpinner(Spinner spinner, String izabran)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, knjigeString);
        adapter.setDropDownViewResource(R.layout.spinneritem);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idKnjige = knjige.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(!izabran.isEmpty()){
            spinner.setSelection(adapter.getPosition(izabran));
        }
    }

    private void dodajOglas()
    {
            id=databaseReference2.push().getKey();
            idKnjiga = idKnjige;
            idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DatabaseReference knjige = FirebaseDatabase.getInstance().getReference("Knjige").child(idKnjiga).child("oglasi");

            Oglas oglas = new Oglas(id, idKnjiga, idUser, cena, opis);

            knjige.child(id).setValue(id);

            databaseReference2.child("Oglasi").child(id).setValue(oglas);

            databaseReference2.child("Korisnici").child(idUser).child("oglasi").child(id).setValue(id);
    }

    private boolean citajEditTextove()
    {
        if(!cenaKnjige.getText().toString().trim().equals("") && !opisKnjige.getText().toString().trim().equals(""))
        {
            cena=Integer.valueOf(cenaKnjige.getText().toString().trim());
            opis=opisKnjige.getText().toString().trim();
            return true;
        }


        Toast.makeText(KnjigaDodavanjeActivity.this,"Popunite sva polja za unos",Toast.LENGTH_SHORT).show();

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            br++;

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                switch (br) {
                    case 1:
                        slikeKnjige.setImageBitmap(bitmap);
                        break;
                    case 2:
                        slikeKnjige2.setImageBitmap(bitmap);
                        break;
                    default:
                        slikeKnjige3.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (requestCode == 1) {
            if (resultCode == KnjigaDodavanjeActivity.RESULT_OK) {
                idKnjige = data.getStringExtra("idknjige");
                databaseReference.child(idKnjige).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            Knjiga nova;
                            nova = dataSnapshot.getValue(Knjiga.class);
                            knjige.add(nova);
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(nova.getNaziv());
                            for (String s : nova.getAutori()) {
                                stringBuilder.append(" " + s);
                            }
                            knjigeString.add(stringBuilder.toString());
                            setSpinner(spinner, stringBuilder.toString());
                        }
                        catch (Exception e){
                            Toast.makeText(KnjigaDodavanjeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    private void upisiSliku()
    {
        progressDialog.setMessage("Upisivanje...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        dodajOglas();

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        for(int i=0;i<br;i++) {
            childRef = storageRef.child(user.getUid()).child("Knjiga").child(id + "/" + i + "image.jpg");
            UploadTask uploadTask = childRef.putFile(filePath);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(KnjigaDodavanjeActivity.this, "Uspesno dodavanje knjige", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(KnjigaDodavanjeActivity.this, "Greska prilikom upisivanja " + e, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void takeBarcode() {
        PhotoBarcodeScanner photoBarcodeScanner = new PhotoBarcodeScannerBuilder(this)
                .withCenterTracker(true)
                .withResultListener((Barcode barcode) -> {
                    nadjiKnjiguPoBarkodu(barcode.rawValue);
                })
                .build();
        photoBarcodeScanner.start();
    }
    private void nadjiKnjiguPoBarkodu(String kod) {
        int indexTemp = 0;
        while (indexTemp < knjige.size()) {
            if (knjige.get(indexTemp).getBarkod().equals(kod)) {
                spinner.setSelection(indexTemp);
                break;
            }
            indexTemp++;
        }
        if (indexTemp == knjige.size())
        {
            Toast.makeText(this, "uso", Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(KnjigaDodavanjeActivity.this)
                    .setTitle("Knjiga nije nadjena")
                    .setMessage("Da li zelis da dodas knjigu?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intentTemp=new Intent(KnjigaDodavanjeActivity.this,NovaVrstaKnjigeActivity.class);
                            intentTemp.putExtra("skeniraniBarkod",kod.toString());
                            //Intent myIntent = new Intent(KnjigaDodavanjeActivity.this, NovaVrstaKnjigeActivity.class);
                            KnjigaDodavanjeActivity.this.startActivityForResult(intentTemp, 1);
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("Ne", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}
