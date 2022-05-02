package com.example.tfg_nd;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class puzzle_porcenaje extends Fragment {

    TextView tvColor, tvNumero, tvResultado;
    int num;
    SeekBar sb;

    private final String TAG = "puzzle_porcentaje.java";
    String email;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
        sb = v.findViewById(R.id.seekBar2);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) email = currentUser.getEmail();

        //seguramente haya una forma mas eficiente de hacer esto, pero me estaba dando problemas el conversor de px a dp y asi se ha quedado
        int dimensiones[] = new int[]{
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

        num = (int)(Math.random()*99+1);
        int altura = getResources().getDimensionPixelSize(dimensiones[num])*3;
        tvColor.getLayoutParams().height = altura;

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvNumero.setText(progress+"");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sb.setEnabled(false);
                int porcentaje = 100-num;
                if(seekBar.getProgress() == porcentaje){
                    tvResultado.setText("Acertaste!! era " + porcentaje);
                    victoria();
                }else{
                    tvResultado.setText("Fallaste... :( era " + porcentaje);
                    alertFinalPartida("prueba");
                }

            }
        });
        return v;
    }

    public void alertFinalPartida(String titulo){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View custom_layout = getLayoutInflater().inflate(R.layout.end_game, null);



        builder.setView(custom_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void victoria(){
        DocumentReference docRef = db.collection("puzzle_1").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        int nivel;
                        nivel = Integer.parseInt(document.get("nivel")+"");
                        nivel++;
                        db.collection("puzzle_1").document(email).update("nivel", nivel);

                    } else {
                        //Si no existe crea un usuario nuevo con ese nombre
                        Toast.makeText(getContext(), "Error en Victoria()", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

}