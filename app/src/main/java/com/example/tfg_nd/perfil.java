package com.example.tfg_nd;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class perfil extends Fragment {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String email;
    private final String TAG = "Perfil.java";

    FirebaseUser currentUser;
    Button btnCerrarSesion, btCambiar;
    TextView tvPerfil, tvDinero, tvNombre, tvNivel, tvExp;
    ProgressBar pb;
    private ImageView img1, img2, img3;

    private String current_dorso, current_ficha;

    public perfil() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);
        currentUser = mAuth.getCurrentUser();

        btnCerrarSesion = v.findViewById(R.id.profile_signout);
        tvPerfil = v.findViewById(R.id.textView6);
        tvDinero = v.findViewById(R.id.profile_dinero);
        tvNombre = v.findViewById(R.id.profile_nombre);
        tvNivel = v.findViewById(R.id.profile_nivel);
        tvExp = v.findViewById(R.id.profile_exp);
        pb = v.findViewById(R.id.profile_pb);
        img1 = v.findViewById(R.id.dorso_memory);
        img2 = v.findViewById(R.id.fichas_tresraya);
        img3 = v.findViewById(R.id.pajaro);
        btCambiar = v.findViewById(R.id.btCambiar);

        if(currentUser!=null){
            email = currentUser.getEmail();
            manejadorPreferencias mPref = new manejadorPreferencias(email, getActivity());
            //Si hay otro correo aqui es que se quiere consultar el perfil de otro usuario
            String email_externo = mPref.get("email_externo", email);
            if(!email_externo.equals(email)) {
                email = email_externo;
                //Se pone de nuevo el email del usuario logeado por si quiere consultar su perfil
                mPref.put("email_externo", currentUser.getEmail());
            }

            //Cargar las imagenes
            String dorso_actual = mPref.get("dorso_memory", "dorso_rata");
            cargarImagen_memory(dorso_actual);
            String ficha_actual = mPref.get("fichas_tresraya", "fichas_normales");
            cargarFicha(ficha_actual);
            String pajaro_actual = mPref.get("pajaro", "pou");
            cargarPajaro(pajaro_actual);

            DocumentReference docRef = db.collection("users").document(email);
            tvPerfil.setText(email);

            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot snapshot) {
                    if(snapshot!=null && snapshot.exists()){
                        Log.d(TAG, "Current data: " + snapshot.getData());
                        String dinero, nombre, nivel, exp;

                        dinero = snapshot.getData().get("dinero")+"";
                        if(dinero.equals("null")) dinero = "0";
                        nombre = snapshot.getData().get("nombre")+"";
                        if(nombre.equals("null")) nombre = "error";
                        nivel = snapshot.getData().get("nivel")+"";
                        if(nivel.equals("null")) nivel = "0";
                        exp = snapshot.getData().get("exp")+"";
                        if(exp.equals("null")) exp = "0";

                        tvDinero.setText(dinero);
                        tvNombre.setText(nombre);
                        tvNivel.setText(nivel);
                        tvExp.setText(exp+"/100");
                        pb.setProgress(Integer.parseInt(exp));
                    }
                }
            });
        }

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        btCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.tienda);
            }
        });

        return v;
    }

    public void cargarImagen_memory(String dorso_actual){
        switch (dorso_actual){
            case "dorso_dollar":
                img1.setImageResource(R.drawable.dorsodollar);
                break;
            case "dorso_sombra":
                img1.setImageResource(R.drawable.dorsosombra);
                break;
            default:
                img1.setImageResource(R.drawable.dorso);
                break;
        }
    }

    public void cargarFicha(String ficha_actual){
        switch (ficha_actual){
            case "fichas_rojas":
                img2.setImageResource(R.drawable.xo_rojo);
                break;
            case "fichas_rosazul":
                img2.setImageResource(R.drawable.fichas_rosazul);
                break;
            default:
                img2.setImageResource(R.drawable.xo_negro);
                break;
        }
    }

    public void cargarPajaro(String pajaro_actual){
        switch (pajaro_actual){
            case "pajaro_pou":
                img3.setImageResource(R.drawable.flying_pouresized);
                break;
            case "pajaro_hamb":
                img3.setImageResource(R.drawable.pajaro_hamb1);
                break;
            case "cohete":
                img3.setImageResource(R.drawable.cohete1);
                break;
            default:
                img3.setImageResource(R.drawable.pajaro1);
                break;
        }
    }

    public void cerrarSesion(){
        if(currentUser==null){
            Toast.makeText(getContext(), "No hay ninguna sesión iniciada", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.signOut();
            startActivity(new Intent(getContext(), HomeMenuActivity.class));
        }
    }

}