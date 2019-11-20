package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class PodesavanjaNalogaActivity extends AppCompatActivity {

    private TextView imet,prezimet,mailt,passt;
    private EditText imee,prezimee,maile,passe;
    private Button update;
    private ImageView slika;
    private int PICK_IMAGE_REQUEST = 111;
    private Uri filePath;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://knjigeapp.appspot.com");
    private StorageReference storageReference;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podesavanja_naloga);

        init();

        ucitajSliku();

        postaviListener();
    }
    private void ucitajSliku() {

        progressDialog.setMessage("Ucitavanje...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference().child(user.getUid() + "/Korisnik/" + "image.jpg");

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(PodesavanjaNalogaActivity.this).load(uri).into(slika);
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(PodesavanjaNalogaActivity.this, "Nemate profilnu sliku!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    void postaviListener()
    {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Korisnici").child(firebaseUser.getUid());

                if(!checkEditText(imee))
                {
                    ref.child("ime").setValue(imee.getText().toString().trim());
                }

                if(!checkEditText(prezimee))
                {
                    ref.child("prezime").setValue(prezimee.getText().toString().trim());
                }

                if(!checkEditText(maile))
                {
                    firebaseUser.updateEmail(maile.getText().toString().trim());
                }

                if(!checkEditText(passe))
                {
                    firebaseUser.updatePassword(passe.getText().toString().trim());
                }

                if(filePath!=null)
                    upisiSliku();



            }
        });

        slika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });
    }

    private void upisiSliku()
    {
        progressDialog.setMessage("Upisivanje...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        StorageReference childRef = storageRef.child(user.getUid() + "/Korisnik/" + "image.jpg");
        UploadTask uploadTask = childRef.putFile(filePath);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Toast.makeText(PodesavanjaNalogaActivity.this,"Uspesno podesen nalog",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PodesavanjaNalogaActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PodesavanjaNalogaActivity.this, "Greska prilikom upisivanja " + e, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
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

    private void init()
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

        slika=(ImageView)findViewById(R.id.slikaKorisnikaPodesavanje);

        progressDialog=new ProgressDialog(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                slika.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
