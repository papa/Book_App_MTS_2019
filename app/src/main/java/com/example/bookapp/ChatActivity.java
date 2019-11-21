package com.example.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.renderscript.ScriptIntrinsicResize;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    ImageView posaljiPoruku;
    EditText tekst;
    DatabaseReference poruke;
    String idPoruke;
    String idKorisnika;
    int dan,mesec,godina;
    int sat,minut;
    LinearLayout linLayout;
    RelativeLayout relLayout;
    ScrollView scrollView;
    EditText messageArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
        postaviListener();
    }

    private void init()
    {
        posaljiPoruku = findViewById(R.id.sendButton);
        poruke = FirebaseDatabase.getInstance().getReference("Poruke");
        //idKorisnika
        linLayout = findViewById(R.id.linearlayout);
        relLayout = findViewById(R.id.relativelayout);
        scrollView = findViewById(R.id.scrollView);
        messageArea =  findViewById(R.id.messageArea);
    }

    private void postaviListener()
    {
        posaljiPoruku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tekst = messageArea.getText().toString();

                if(!tekst.equals(""))
                {
                    Map<String,String> map = new HashMap<>();
                    map.put("message",tekst);
                    messageArea.setText("");
                }

            }
        });
    }

    public void addMessageBox(String message,int type)
    {
        TextView textView = new TextView(ChatActivity.this);
        textView.setText(message);

        LinearLayout.LayoutParams linpar2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        linpar2.weight = 1.0f;

        if(type==1)
        {
            linpar2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bg_transparent);
        }
        else
        {
            linpar2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bg_transparent);
        }
    }

}
