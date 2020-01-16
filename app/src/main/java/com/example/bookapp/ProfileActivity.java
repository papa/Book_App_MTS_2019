package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bookapp.Fragmenti.FragmentKnjige;
import com.example.bookapp.Fragmenti.FragmentPoruke;
import com.example.bookapp.Fragmenti.FragmentProfil;
import com.example.bookapp.Klase.Korisnik;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private String name, surname, id,email;
    private FirebaseUser user;

    static Korisnik trenutniKorisnik;
    static FirebaseUser userr;

    private static final int NUM_PAGES = 3;
    //private ViewPager mPager;
    private PagerAdapter pagerAdapter;
    private int pozicijaActual;



    public final class adapterSlide extends FragmentPagerAdapter {
        private final ArrayList<Fragment> fragmentList;
        public int getCount(){return this.fragmentList.size();}

        public Fragment getItem(int position)
        {
            if(position>=0 && position < this.fragmentList.size())
            {
                return fragmentList.get(position);
            }
            return new FragmentKnjige();
        }
        public adapterSlide(@NonNull ArrayList<Fragment> fragmentList, @NonNull FragmentManager fm)
        {
            super(fm);
            this.fragmentList=fragmentList;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        prijem();

        uzmiPodatkeTrenutnog();

        ArrayList<Fragment> fragList = new ArrayList<>();
        fragList.add(new FragmentProfil());
        fragList.add(new FragmentKnjige());
        fragList.add(new FragmentPoruke());
        pagerAdapter = new adapterSlide(fragList, getSupportFragmentManager());
        final BubbleNavigationLinearView bubbleNavigationLinearView = findViewById(R.id.bottom_navigation_view_linear);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Toast.makeText(ProfileActivity.this, "bbb "+String.valueOf(position), Toast.LENGTH_SHORT).show();
                bubbleNavigationLinearView.setCurrentActiveItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                //Toast.makeText(ProfileActivity.this, "aaa "+String.valueOf(position), Toast.LENGTH_SHORT).show();
                viewPager.setCurrentItem(position, true);
            }
        });

        /*mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        bubbleNavigationLinearView.setCurrentActiveItem(1);
        mPager.setCurrentItem(1,true);

        bubbleNavigationLinearView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                mPager.setCurrentItem(position, true);
            }
        });
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bubbleNavigationLinearView.setCurrentActiveItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
    }

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
