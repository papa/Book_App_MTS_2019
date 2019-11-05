package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bookapp.Klase.Knjiga;
import com.example.bookapp.Klase.Korisnik;
import com.example.bookapp.Klase.Oglas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KnjigaDodavanjeActivity extends AppCompatActivity {

    private String idKnjige = "nema";
    private ArrayList<Knjiga> knjige = new ArrayList<Knjiga>();
    private ArrayList<String> knjigeString = new ArrayList<String>();
    private Spinner spinner;
    private Button novaKnjiga, btnUpisi;
    private DatabaseReference databaseReference,databaseReference2;
    private EditText cenaKnjige,opisKnjige;

    private String opis,id,idKnjiga,idUser;
    private int cena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //boki pravio
        //bravo bogdane -ANDRIJA-
        //hvala andrija -BOGDAN-
        //bravo i tebi andrija -BOGDAN-

        //Popravio sam malo kod (ulepsao ga i sredio)
        //Dodao sam funkcije za upis oglasa u bazu
        //-ANDRIJA-

        //Popravio sam jos malo kod (ulepsao ga i sredio)
        //Dodao sam vracanje id-ja nove knjige u stari activity vrlo efikasno i clean
        //-BOGDAN-
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knjiga_dodavanje);

        initialize();

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
                dodajOglas();
            }
        });

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

    private void initialize() {

        novaKnjiga = findViewById(R.id.novaKnjigaActivityButton);
        btnUpisi=(Button)findViewById(R.id.btUpisi);

        spinner = findViewById(R.id.youSpinMeRightRound);

        cenaKnjige=(EditText)findViewById(R.id.cenaK);
        opisKnjige=(EditText)findViewById(R.id.dodOpisK);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Knjige");
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
    }

    private void setSpinner(Spinner spinner, String izabran) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, knjigeString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        citajEditTextove();


        if(idKnjige.equals("nema"))
            Toast.makeText(KnjigaDodavanjeActivity.this,"Odaberite knjigu iz liste ili dodajte novu", Toast.LENGTH_SHORT).show();
        else {
            id=databaseReference2.push().getKey();
            idKnjiga = idKnjige;
            idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Oglas oglas = new Oglas(id, idKnjiga, idUser, cena, opis);

            databaseReference2.child("Oglasi").child(id).setValue(oglas);

            databaseReference2.child("Korisnici").child(idUser).child("oglasi").child(id).setValue(id);
        }

    }

    private void citajEditTextove()
    {

        if(!cenaKnjige.getText().toString().trim().equals("") && !opisKnjige.getText().toString().trim().equals(""))
        {
            cena=Integer.valueOf(cenaKnjige.getText().toString().trim());
            opis=opisKnjige.getText().toString().trim();
        }
        else
            Toast.makeText(KnjigaDodavanjeActivity.this,"Greska prilikom unosa dodatnih podataka",Toast.LENGTH_SHORT).show();

    }@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

}
