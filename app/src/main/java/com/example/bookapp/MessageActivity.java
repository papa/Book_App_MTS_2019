package com.example.bookapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bookapp.APINotification.APIService;
import com.example.bookapp.Klase.Korisnik;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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

    Button ugovorButton;

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
    }
}
