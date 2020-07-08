package com.example.demoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class Home extends AppCompatActivity {
    DatabaseReference databaseReference;
    ListView listView_items;
    List<Data> registrationList;
    TextView greetingMessage;
    EditText my_name_text;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        listView_items = findViewById(R.id.list_iview_of_items);
        greetingMessage = findViewById(R.id.greetingMessage);
        auth = FirebaseAuth.getInstance();
        my_name_text = findViewById(R.id.save_me_text_view);
        String userEmail = auth.getCurrentUser().getEmail().toString();
        greetingMessage.setText("Welcome "+userEmail);
        databaseReference = FirebaseDatabase.getInstance().getReference("default");
        registrationList = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                registrationList.clear();
                for (DataSnapshot eventSnapshot:dataSnapshot.getChildren()){
                    Data mydata = eventSnapshot.getValue(Data.class);
                    registrationList.add(mydata);
                }
                DataList adapter=new DataList(Home.this,registrationList);
                listView_items.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void logMeout (View v){
        new AlertDialog.Builder(this)
                .setTitle("Do You Want To LogOut ?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        auth.signOut();
                        startActivity(new Intent(Home.this, Login.class));
                        finish();
                    }
                }).create().show();
    }

    public void save_to_db(View v){
        final String stringName = my_name_text.getText().toString().trim();
        if(!stringName.isEmpty()){
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("default");
            String id = db.push().getKey();
            Data data = new Data(stringName);
            db.child(id).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Home.this, "Uploaded SuccessFully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Home.this, "Check InterNet", Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Toast.makeText(Home.this, "Something Went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}