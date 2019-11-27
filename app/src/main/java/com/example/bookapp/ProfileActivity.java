package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.bookapp.Fragmenti.FragmentKnjige;
import com.example.bookapp.Fragmenti.FragmentProfil;
import com.example.bookapp.Klase.Korisnik;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private String name, surname, id,email;
    private FirebaseUser user;

    //bottom navigation bar deo
    private BottomNavigationView bottomNavigationView;

    static Korisnik trenutniKorisnik;
    static FirebaseUser userr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        prijem();

        uzmiPodatkeTrenutnog();

        //Ovo ispod je za bottomnavbar, treba da se dovrsi jer nemamo plan unapred al ugl tjt

        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentProfil()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment=null;

                    switch (menuItem.getItemId())
                    {
                        case R.id.navprofil:
                            selectedFragment=new FragmentProfil();
                            break;
                        case R.id.navlista:
                            selectedFragment=new FragmentKnjige();
                            break;
                        case R.id.navnesto:
                            selectedFragment=new FragmentKnjige();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();


                    return true;
                }
            };


    private void prijem()
    {
        if(getIntent().hasExtra("name")) {
            //ubacivanje podataka u bazu sada kada je korisnik kreiran
            name = getIntent().getStringExtra("name");
            surname=getIntent().getStringExtra("surname");
            email=getIntent().getStringExtra("email");

            user=FirebaseAuth.getInstance().getCurrentUser();

            dodajKorisnika(user.getUid(),email,name,surname);

        }
        else if(getIntent().hasExtra("userId")) {
            //ako se loguje da imamo id i znamo ga u bazi koji je
            id = getIntent().getStringExtra("userId");
            //zar ne moze nekako da se izvuce kao trenutno ulogovan user
        }
    }

    private void dodajKorisnika(String id,String email,String ime,String prezime)
    {
        DatabaseReference korisnici = FirebaseDatabase.getInstance().getReference("Korisnici");
        Korisnik k  =  new Korisnik(id,ime,prezime,email);
        korisnici.child(id).setValue(k);
    }

    private void uzmiPodatkeTrenutnog()
    {
        userr = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Korisnici").child(userr.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                trenutniKorisnik = dataSnapshot.getValue(Korisnik.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
