package com.example.ramiro.testfirestore;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateRTDB();
        readRTDB();
        populateFirestore();
        readFirestore();
    }

    private void populateRTDB() {
        for (int i = 1; i <= 100; i++){
            FirebaseDatabase.getInstance().getReference(String.valueOf(i)).child(String.valueOf(i)).setValue(String.valueOf(i));
        }
    }

    private void populateFirestore() {
        for (int i = 1; i <= 100; i++){
            Map<String, Object> data = new HashMap<>();
            data.put(String.valueOf(i), String.valueOf(i));
            FirebaseFirestore.getInstance().collection("firestore").document(String.valueOf(i)).set(data);
        }
    }

    private void readRTDB() {
        for (int i = 1; i <= 100; i++) {
            final long start = System.currentTimeMillis();
            FirebaseDatabase.getInstance().getReference(String.valueOf(i)).child(String.valueOf(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long finish = System.currentTimeMillis();
                    long time = finish - start;
                    String data = (String) dataSnapshot.getValue();
                    Log.d(TAG, "Data: " + data + ". Took: " + time + " ms.");
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }

    private void readFirestore() {
        for (int i = 1; i <= 100; i++) {
            final long start = System.currentTimeMillis();
            FirebaseFirestore.getInstance().collection("firestore").document(String.valueOf(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    long finish = System.currentTimeMillis();
                    long time = finish - start;
                    Map data = task.getResult().getData();
                    Log.d(TAG, "Data: " + data + ". Took: " + time + " ms.");
                }
            });
        }
    }
}
