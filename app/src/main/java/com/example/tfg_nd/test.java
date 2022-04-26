package com.example.tfg_nd;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class test extends Fragment {

    EditText username;
    EditText edDinero;
    Button agregar;
    Button verDinero;
    TextView tvDinero;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final String TAG = "test.java";

    public test() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_test, container, false);

        username = v.findViewById(R.id.etUsername);
        edDinero = v.findViewById(R.id.etDinero);
        agregar = v.findViewById(R.id.btAgregar);
        tvDinero = v.findViewById(R.id.tvDinero);
        verDinero = v.findViewById(R.id.btVerDinero);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        username.setText(currentUser.getEmail());
        DocumentReference docRef = db.collection("users").document(currentUser.getEmail());

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    tvDinero.setText(snapshot.getData().get("dinero").toString());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> user;
                user = new HashMap<>();
                int dinero = Integer.parseInt(edDinero.getText().toString());
                user.put("dinero", dinero);

                //Si usas add te crea un documento con un ID autogenerado
                db.collection("users").document(currentUser.getEmail()).set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "setDinero realizado correctamente");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error en setDinero()", e);
                        }
                    });
            }
        });

        verDinero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return v;
    }
}