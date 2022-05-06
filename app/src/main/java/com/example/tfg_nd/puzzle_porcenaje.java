package com.example.tfg_nd;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class puzzle_porcenaje extends Fragment {

    private TextView tvColor, tvNumero, tvResultado, tvTitulo, tvRelleno;
    private int num, nivel_actual, num_estrellas = 0;
    private SeekBar sb;
    private int[] dimensiones;
    private AlertDialog dialog;
    private ImageView contenedor;
    private User user;

    private final String TAG = "puzzle_porcentaje.java";
    private final String gamemode = "porcentajes";
    private String email;
    private manejadorPreferencias mPref;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public puzzle_porcenaje() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_puzzle_porcenaje, container, false);

        tvColor = v.findViewById(R.id.tvColor);
        tvNumero = v.findViewById(R.id.tvNumero);
        tvResultado = v.findViewById(R.id.tvResultado);
        tvTitulo = v.findViewById(R.id.titulo);
        tvRelleno = v.findViewById(R.id.tvRelleno);
        sb = v.findViewById(R.id.seekBar2);
        contenedor = v.findViewById(R.id.contenedor);
        mPref = new manejadorPreferencias("pref", getActivity());

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            email = currentUser.getEmail();
            user = new User(email);
        }

        //seguramente haya una forma mas eficiente de hacer esto, pero me estaba dando problemas el conversor de px a dp y asi se ha quedado
        dimensiones = new int[]{
                R.dimen.pc1, R.dimen.pc2, R.dimen.pc3, R.dimen.pc4, R.dimen.pc5, R.dimen.pc6, R.dimen.pc7, R.dimen.pc8, R.dimen.pc9, R.dimen.pc10,
                R.dimen.pc11, R.dimen.pc12, R.dimen.pc13, R.dimen.pc14, R.dimen.pc15, R.dimen.pc16, R.dimen.pc17, R.dimen.pc18, R.dimen.pc19, R.dimen.pc20,
                R.dimen.pc21, R.dimen.pc22, R.dimen.pc23, R.dimen.pc24, R.dimen.pc25, R.dimen.pc26, R.dimen.pc27, R.dimen.pc28, R.dimen.pc29, R.dimen.pc30,
                R.dimen.pc31, R.dimen.pc32, R.dimen.pc33, R.dimen.pc34, R.dimen.pc35, R.dimen.pc36, R.dimen.pc37, R.dimen.pc38, R.dimen.pc39, R.dimen.pc40,
                R.dimen.pc41, R.dimen.pc42, R.dimen.pc43, R.dimen.pc44, R.dimen.pc45, R.dimen.pc46, R.dimen.pc47, R.dimen.pc48, R.dimen.pc49, R.dimen.pc50,
                R.dimen.pc51, R.dimen.pc52, R.dimen.pc53, R.dimen.pc54, R.dimen.pc55, R.dimen.pc56, R.dimen.pc57, R.dimen.pc58, R.dimen.pc59, R.dimen.pc60,
                R.dimen.pc61, R.dimen.pc62, R.dimen.pc63, R.dimen.pc64, R.dimen.pc65, R.dimen.pc66, R.dimen.pc67, R.dimen.pc68, R.dimen.pc69, R.dimen.pc70,
                R.dimen.pc71, R.dimen.pc72, R.dimen.pc73, R.dimen.pc74, R.dimen.pc75, R.dimen.pc76, R.dimen.pc77, R.dimen.pc78, R.dimen.pc79, R.dimen.pc80,
                R.dimen.pc81, R.dimen.pc82, R.dimen.pc83, R.dimen.pc84, R.dimen.pc85, R.dimen.pc86, R.dimen.pc87, R.dimen.pc88, R.dimen.pc89, R.dimen.pc90,
                R.dimen.pc91, R.dimen.pc92, R.dimen.pc93, R.dimen.pc94, R.dimen.pc95, R.dimen.pc96, R.dimen.pc97, R.dimen.pc98, R.dimen.pc99, R.dimen.pc100
        };
        nivel_actual = Integer.parseInt(mPref.get("nivel", "0"));



        tvTitulo.setText("Nivel: " + nivel_actual);
        pintarFigura();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvNumero.setText(progress+"");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                int porcentaje = 100-num;
                int respuesta = seekBar.getProgress();
                int resultado = porcentaje - respuesta;
                if(respuesta !=  0){
                    if(respuesta == porcentaje){
                        num_estrellas = 3;
                        alertFinalPartida("Perfecto", "Puedes pasar al siguiente nivel", num_estrellas*2, 10, 20);
                        subirNivel();
                        user.incrementarDinero(10);
                        user.incrementarExperiencia(20);
                    }else if(resultado == 1 || resultado == -1){
                        num_estrellas = 2;
                        alertFinalPartida("Muy bien", "Has fallado solo por uno, era " + porcentaje, num_estrellas*2, 5, 10);
                        subirNivel();
                        user.incrementarDinero(5);
                        user.incrementarExperiencia(10);
                    }else if(resultado == 2 || resultado == -2 || resultado == 3 || resultado == -3){
                        num_estrellas = 1;
                        alertFinalPartida("Has estado cerca", "puedes volver a intentarlo,\n era " + porcentaje, num_estrellas*2, 1, 2);
                        subirNivel();
                        user.incrementarDinero(1);
                        user.incrementarExperiencia(2);
                    }else{
                        num_estrellas = 0;
                        alertFinalPartida("Has fallado", "Puedes volver a intentarlo,\n era: " + porcentaje, num_estrellas*2, 0, 0);
                    }
                    sb.setEnabled(false);
                }
            }
        });
        return v;
    }

    public void alertFinalPartida(String titulo, String titulo2, int num_estrellas, int dinero, int experiencia){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View custom_layout = getLayoutInflater().inflate(R.layout.end_game, null);
        ImageView reloadGame, backMenu, nextLevel;
        TextView tit, tit2, tvDinero, tvExperiencia;
        RatingBar rb;

        reloadGame = custom_layout.findViewById(R.id.reloadGame);
        backMenu = custom_layout.findViewById(R.id.backMenu);
        nextLevel = custom_layout.findViewById(R.id.nextLevel);
        tit = custom_layout.findViewById(R.id.tvTitulo);
        tit2 = custom_layout.findViewById(R.id.tvTitulo2);
        tvDinero = custom_layout.findViewById(R.id.tvGanaDinero);
        tvExperiencia = custom_layout.findViewById(R.id.tvGanaExperiencia);
        rb = custom_layout.findViewById(R.id.ratingBar);

        tit.setText(titulo);
        tit2.setText(titulo2);
        tvDinero.setText("+ " + dinero);
        tvExperiencia.setText("+ " +    experiencia+"");
        rb.setProgress(num_estrellas);

        reloadGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadGame();
            }
        });

        if(num_estrellas==0){
            nextLevel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Tu flipas", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            nextLevel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reloadGame();
                    if(nivel_actual==12){
                        Toast.makeText(getContext(), "Ya te has pasado todos los niveles", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.niveles);
                    }else{
                        mPref.put("nivel", (nivel_actual+1)+"");
                        nivel_actual++;
                        tvTitulo.setText("Nivel: " + nivel_actual);
                        reloadGame();
                    }
                }
            });
        }

        backMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.niveles);
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);
        builder.setView(custom_layout);
        dialog = builder.create();
        dialog.show();
    }

    public void reloadGame(){
        pintarFigura();
        sb.setEnabled(true);
        sb.setProgress(0);
        tvNumero.setText("");
        dialog.dismiss();
    }

    public void pintarFigura(){
        num = (int)(Math.random()*99+1);
        int altura = getResources().getDimensionPixelSize(dimensiones[num])*3;
        tvColor.getLayoutParams().height = altura;
        switch (nivel_actual){
            case 5: case 6: case 7: case 8:
                contenedor.setImageResource(R.drawable.circle);
                tvRelleno.setBackgroundColor(getResources().getColor(R.color.azul));
                break;
            case 9: case 10: case 11: case 12:
                contenedor.setImageResource(R.drawable.triangle);
                tvRelleno.setBackgroundColor(getResources().getColor(R.color.verde));
                break;
        }
    }

    public void subirNivel(){
        DocumentReference docRef = db.collection(gamemode).document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Subir un nivel
                        int nivel;
                        nivel = Integer.parseInt(document.get("nivel")+"");
                        Log.w(TAG, nivel_actual +"--"+nivel);
                        if(nivel == nivel_actual){
                            Log.w(TAG, email);
                            //Poner este nivel en completado
                            db.collection(gamemode).document(email).collection("datos_nivel").document(nivel+"").update("completado", true);
                            nivel++;
                            db.collection(gamemode).document(email).update("nivel", nivel+"");
                        }
                        actualizarEstrellas();
                    } else {
                        //Si no existe crea un usuario nuevo con ese nombre
                        Toast.makeText(getContext(), "Error en updateUser()", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
    public void actualizarEstrellas(){
        DocumentReference docRef = db.collection(gamemode).document(email).collection("datos_nivel").document(nivel_actual+"");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try{
                    int estrellas = Integer.parseInt(documentSnapshot.getData().get("estrellas")+"");
                    if(num_estrellas > estrellas){
                        db.collection(gamemode).document(email).collection("datos_nivel").document(nivel_actual+"").update("estrellas", num_estrellas);

                    }
                }catch(Exception e){
                    Log.w(TAG, "Error al acceder a las etrellas");
                }
            }
        });
    }


}