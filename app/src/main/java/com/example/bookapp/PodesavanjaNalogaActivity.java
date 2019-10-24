package com.example.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PodesavanjaNalogaActivity extends AppCompatActivity {

    TextView imet,prezimet,mailt,passt;
    EditText imee,prezimee,maile,passe;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podesavanja_naloga);

        init();
        postaviListener();
    }

    void postaviListener()
    {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(check())
                {
                    //TODO
                    //ostaje da se uradi update za sliku
                    //Andrijo ti si to pricao da znas sa onim bitmapama pa jos nesto

                    if(passe.getText().toString().trim().length() < 6)
                    {
                        Toast.makeText(PodesavanjaNalogaActivity.this,"Nova sifra mora da bude duza od 5 karaktera",Toast.LENGTH_LONG).show();
                    }
                    else {

                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Korisnici").child(firebaseUser.getUid());
                        ref.child("ime").setValue(imee.getText().toString());
                        ref.child("prezime").setValue(prezimee.getText().toString());
                        firebaseUser.updateEmail(imee.getText().toString());
                        firebaseUser.updatePassword(passe.getText().toString());
                    }
                }
                else
                {
                    Toast.makeText(PodesavanjaNalogaActivity.this,"Popunite sva polja kako bi mogli da sacuvamo vase promene",Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    boolean check()
    {
        return !(checkEditText(imee) || checkEditText(prezimee) || checkEditText(maile) || checkEditText(passe));
    }

    boolean checkEditText(EditText e)
    {
        return e.getText().toString().trim().isEmpty();
    }

    void init()
    {
        imet=(TextView)findViewById(R.id.imePTV);
        prezimet=(TextView)findViewById(R.id.prezimePTV);
        mailt=(TextView)findViewById(R.id.emailPTV);
        passt=(TextView)findViewById(R.id.sifraPTV);

        imee=(EditText)findViewById(R.id.imePodesavanje);
        prezimee=(EditText)findViewById(R.id.prezimePodesavanje);
        maile=(EditText)findViewById(R.id.emailPodesavanje);
        passe=(EditText)findViewById(R.id.sifraPodesavanje);

        update = (Button)findViewById(R.id.updatePodesavanja);
    }
}
