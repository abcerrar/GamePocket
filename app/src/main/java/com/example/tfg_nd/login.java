package com.example.tfg_nd;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class login extends Fragment {

    Button btnAcceder;
    TextView changeDisplay;
    EditText etEmail, contraseña, etName, contraseña2;
    FirebaseAuth mAuth;
    boolean login = true;
    private final String TAG = "login.java";

    public login() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        btnAcceder = v.findViewById(R.id.btAcceder);
        etEmail = v.findViewById(R.id.etEmail);
        contraseña = v.findViewById(R.id.etPass);
        changeDisplay = v.findViewById(R.id.notengocuenta);
        etName = v.findViewById(R.id.etName);
        contraseña2 = v.findViewById(R.id.etPass2);
        mAuth = FirebaseAuth.getInstance();


        changeDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login){
                    login = false;
                    btnAcceder.setText("Registrarse");
                    changeDisplay.setText("Ya tengo cuenta");
                    etName.setVisibility(View.VISIBLE);
                    contraseña2.setVisibility(View.VISIBLE);
                }else{
                    login = true;
                    btnAcceder.setText("Iniciar sesión");
                    changeDisplay.setText("No tengo cuenta");
                    etName.setVisibility(View.INVISIBLE);
                    contraseña2.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String pass = contraseña.getText().toString();
                //Habria que validar la contraseña y el email
                if(login){
                    iniciarSesion(email, pass);
                }else{
                    String nombre = etName.getText().toString();
                    String pass2 = contraseña2.getText().toString();

                    if(!pass.equals(pass2)){
                        Toast.makeText(getContext(), "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
                        contraseña.setText("");
                        contraseña2.setText("");
                    }else if(nombre.length()<3) {
                        Toast.makeText(getContext(), "Nombre demasiado corto.", Toast.LENGTH_SHORT).show();
                        etName.setText("");
                    }else{
                        registrarUsuario(email, pass);
                    }
                }
            }
        });

        return v;
    }

    public void iniciarSesion(String email, String pass){
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void registrarUsuario(String email, String pass){
        mAuth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                 @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        String nombre = etName.getText().toString();

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        Map<String, Object> usuario = new HashMap<>();;
                        usuario.put("nombre", nombre);
                        usuario.put("dinero", 0);
                        usuario.put("exp", 0);
                        usuario.put("nivel", 0);

                        db.collection("users").document(email).set(usuario)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error en addToUser", e);
                                }
                            });

                        Log.d("TAG", "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
    }

    public void updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(getContext(),"You Signed In successfully",Toast.LENGTH_LONG).show();
            startActivity(new Intent(getContext(), HomeMenuActivity.class));
            //findNavController(getView()).navigate(R.id.login_home);
        }else {
            Toast.makeText(getContext(),"You Didnt signed in",Toast.LENGTH_LONG).show();
        }
    }


}