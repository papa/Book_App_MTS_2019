package com.example.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookapp.APINotification.APIService;
import com.example.bookapp.Klase.Korisnik;
import com.example.bookapp.Notifications.Client;
import com.example.bookapp.Notifications.Data;
import com.example.bookapp.Notifications.MyResponse;
import com.example.bookapp.Notifications.Sender;
import com.example.bookapp.Notifications.Token;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    TextView imeiprezime;
    static String drugiId;
    ImageButton posaljiPoruku;
    EditText tekstporuke;

    MessageAdapter messageAdapter;
    List<Chat> chats;
    RecyclerView recyclerView;

    ValueEventListener seenListener;

    DatabaseReference reference;

    int dan;
    int mesec;
    int godina;
    int sati;
    int minuti;

    APIService apiService;

    boolean notify=false;

    //firebase za slike
    private FirebaseAuth firebaseAuth;
    static StorageReference mStorage;
    static FirebaseUser user;
    String idchat;
    LinearLayout infoKorisnik;
    Korisnik drugiKorisnik;
    CircleImageView profilna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        ucitaj();
        postaviListener();
        ucitajImeiPrezime(drugiId);
        //todo
        //drugi id se skuplja iz liste sa koje se dolazi na ovaj acc
    }

    private void ucitajImeiPrezime(final String id)
    {
        DatabaseReference drugi= FirebaseDatabase.getInstance().getReference("Korisnici").child(id);

        drugi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                drugiKorisnik=dataSnapshot.getValue(Korisnik.class);
                String s = drugiKorisnik.getIme()+ " " + drugiKorisnik.getPrezime();
                imeiprezime.setText(s);

                firebaseAuth= FirebaseAuth.getInstance();
                user=firebaseAuth.getCurrentUser();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                mStorage = storageReference.child("Photos").child(user.getUid());

                final CircleImageView circleImageView= findViewById(R.id.pslika);

                StorageReference filepath = mStorage.child("Photos").child(user.getUid());
                filepath.getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                //todo
                                //book app
                                //ovo je neka glupost za majstore
                                //jer se radi preko glide a ne piccasso

                                //Picasso.with(getActivity()).load(uri).fit().centerCrop().into(circleImageView);
                                //TODO
                                // kresuje..
                                // Glide.with(MessageActivity.this).load(uri).apply(RequestOptions.circleCropTransform()).into(circleImageView);

                                // Got the download URL for 'users/me/profile.png'
                            }})
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception exception) {
//                                                       Handle any errors
                            }
                        });

                procitajPoruku(ProfileActivity.trenutniKorisnik.getId(),id,"imageurl");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void postaviListener()
    {
        posaljiPoruku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notify=true;

                String t=tekstporuke.getText().toString();
                if(!t.equals(""))
                {
                    posaljiPoruku(ProfileActivity.userr.getUid(),drugiId,t);
                }
                else
                {
                    Toast.makeText(MessageActivity.this,"Ne mozete da posaljete praznu poruku",Toast.LENGTH_LONG).show();
                }

                tekstporuke.setText("");

            }
        });

        infoKorisnik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO
                //da ode na info o profilu tom

                /*if(!Pocetna.currentKorisnik.isMajstor())
                {
                    InfoMajstor.majstor=drugiKorisnik;
                    startActivity(new Intent(getApplicationContext(),InfoMajstor.class));
                }*/
            }
        });
    }

    private void sendNotification(String prima, final String imeiprez, final String poruka)
    {
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=tokens.orderByKey().equalTo(prima);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Token token=snapshot.getValue(Token.class);

                    //todo
                    //ovaj camera flash ne treba
                    //nego nesto iz majstora da se vidi sta tacno
                    Data data=new Data(ProfileActivity.userr.getUid(),R.drawable.ic_camera_flash_on,imeiprez+": "+poruka,"Nova poruka",drugiId);

                    Sender sender=new Sender(data,token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                    if(response.code()==200)
                                    {
                                        if(response.body().success!=1)
                                        {
                                            Toast.makeText(MessageActivity.this,"Neuspesno",Toast.LENGTH_LONG).show();
                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void posaljiPoruku(String salje, final String prima, String poruka)
    {
        DatabaseReference databaseReference2=FirebaseDatabase.getInstance().getReference();

        datumVreme();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("prima",prima);
        hashMap.put("salje",salje);
        hashMap.put("poruka",poruka);
        hashMap.put("dan",dan);
        hashMap.put("mesec",mesec);
        hashMap.put("godina",godina);
        hashMap.put("sati",sati);
        hashMap.put("minuti",minuti);
        // hashMap.put("isseen",false);

        databaseReference2.child("Chats").child(idchat).push().setValue(hashMap);

        final DatabaseReference chatref=FirebaseDatabase.getInstance().getReference("Chatlist").child(idchat).child(ProfileActivity.userr.getUid()).child(drugiId);

        chatref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists())
                {
                    chatref.child("id").setValue(drugiId);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final String msg=poruka;

       databaseReference2=FirebaseDatabase.getInstance().getReference("Korisnici").child(ProfileActivity.userr.getUid());

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Korisnik k=dataSnapshot.getValue(Korisnik.class);
                if(notify)
                {
                    String s = k.getIme() + " " + k.getPrezime();
                    sendNotification(prima,s,msg);
                }
                notify=false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void generisiId()
    {
        /*if(Pocetna.currentKorisnik.isMajstor())
        {
            idchat=drugiId+Pocetna.currentFirebaseUser.getUid()+Pocetna.prenosBida.getIdposla();
        }
        else
        {
            idchat=Pocetna.currentFirebaseUser.getUid()+drugiId+Pocetna.prenosBida.getIdposla();
        }*/
    }

    private void procitajPoruku(final String mojid,final String drid,final String imgurl)
    {
        chats=new ArrayList<>();

        DatabaseReference reference;

        reference=FirebaseDatabase.getInstance().getReference("Chats").child(idchat);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                chats.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat c=snapshot.getValue(Chat.class);

                    if(proveri(c,mojid,drugiId))
                    {
                        chats.add(c);
                    }

                    messageAdapter=new MessageAdapter(MessageActivity.this,chats,imgurl);
                    recyclerView.setAdapter(messageAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void ucitaj()
    {
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


       toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        recyclerView=findViewById(R.id.poruke);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profilna= findViewById(R.id.profilna);
        imeiprezime= findViewById(R.id.imeiprezime);
        posaljiPoruku= findViewById(R.id.posaljiporuku);
        tekstporuke= findViewById(R.id.tekstporuke);

        infoKorisnik= findViewById(R.id.infoKorisnikChat);

        ucitajSliku();
    }

    private void ucitajSliku()
    {
        //TODO
        //Ovde treba slika da se ucita da vidim sa Andrijom kako
        //u majstorima je preko Pikasa
    }

    private void currentUser(String userid)
    {
        SharedPreferences.Editor editor=getSharedPreferences("PREFS",MODE_PRIVATE).edit();

        editor.putString("currentuser",userid);
        editor.apply();
    }

    private void datumVreme()
    {
        Calendar cal=Calendar.getInstance();

        mesec=cal.get(Calendar.MONTH);
        dan=cal.get(Calendar.DAY_OF_MONTH);
        godina=cal.get(Calendar.YEAR);

        sati=cal.get(Calendar.HOUR_OF_DAY);
        minuti=cal.get(Calendar.MINUTE);

    }

    private boolean proveri(Chat c,String id1,String id2)
    {
        return (c.getPrima().equals(id1) && c.getSalje().equals(id2)) ||
                (c.getPrima().equals(id2) && c.getSalje().equals(id1));
    }

    @Override
    protected void onPause() {
        super.onPause();
        //databaseReference.removeEventListener(seenListener);
        currentUser("none");
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser(drugiId);
    }


}


