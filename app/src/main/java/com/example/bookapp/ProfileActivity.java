package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private String name, surname, id,email;
    private FirebaseUser user;

    //bottom navigation bar deo
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        prijem();

        //Ovo ispod je za bottomnavbar, treba da se dovrsi jer nemamo plan unapred al ugl tjt
        //o da da da

        /*bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FragmentHome()).commit();
         */
    }

    /*private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment=null;

                    switch (menuItem.getItemId())
                    {
                        case R.id.nav_home:
                            selectedFragment=new FragmentHome();
                            break;
                        case R.id.nav_graph:
                            selectedFragment=new FragmentGraph();
                            break;
                        case R.id.nav_calendar:
                            selectedFragment=new FragmentCalendar();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

                    return true;
                }
            };
     */

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
        }
    }

    private void dodajKorisnika(String id,String email,String ime,String prezime)
    {
        DatabaseReference korisnici = FirebaseDatabase.getInstance().getReference("Korisnici");
        Korisnik k  =  new Korisnik(id,ime,prezime,email);
        korisnici.child(id).setValue(k);
    }
}
