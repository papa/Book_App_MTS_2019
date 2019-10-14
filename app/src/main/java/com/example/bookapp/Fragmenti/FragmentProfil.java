package com.example.bookapp.Fragmenti;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookapp.Klase.Korisnik;
import com.example.bookapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentProfil extends Fragment implements View.OnClickListener {

    private TextView tvIme,tvPrezime,tvEmail,tvBrojOcena,tvProsecnaOcena;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String ime,prezime,email;
    private int brojOcena;
    private double prosecnaOcena;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        initialize(view);

        citajBazu();

        return view;
    }

    private void initialize(View view)
    {
        tvIme=(TextView)view.findViewById(R.id.tpIme);
        tvPrezime=(TextView)view.findViewById(R.id.tpPrezime);
        tvEmail=(TextView)view.findViewById(R.id.tpEmail);
        tvBrojOcena=(TextView)view.findViewById(R.id.tpBroj);
        tvProsecnaOcena=(TextView)view.findViewById(R.id.tpProsek);
    }

    private void citajBazu()
    {
        user= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Korisnici").child(user.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Korisnik korisnik=dataSnapshot.getValue(Korisnik.class);

                ime=korisnik.getIme();
                prezime=korisnik.getPrezime();
                email=korisnik.getMail();
                brojOcena=korisnik.getBrojOcena();
                prosecnaOcena=korisnik.getProsecnaOcena();

                upisi();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void upisi()
    {
        tvIme.setText(ime);
        tvPrezime.setText(prezime);
        tvProsecnaOcena.setText(String.valueOf(prosecnaOcena));
        tvBrojOcena.setText(String.valueOf(brojOcena));
        tvEmail.setText(email);
    }


    @Override
    public void onClick(View v) {

    }
}
