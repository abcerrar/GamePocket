package com.example.tfg_nd;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class puzzle_porcenaje extends Fragment {

    private TextView tvColor, tvNumero, tvTitulo, tvRelleno;
    private int num, nivel_actual, num_estrellas = 0;
    private SeekBar sb;
    private int[] dimensiones;
    private AlertDialog dialog;
    private ImageView contenedor;
    private User user;
    private View.OnClickListener listenerReload, listenerMenu, listenerNext, listenerNext2;
    private FirebaseUser currentUser;

    private final String TAG = "puzzle_porcentaje.java";
    private final String gamemode = "porcentajes";
    private manejadorPreferencias mPref;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
        tvTitulo = v.findViewById(R.id.titulo);
        tvRelleno = v.findViewById(R.id.tvRelleno);
        sb = v.findViewById(R.id.seekBar2);
        contenedor = v.findViewById(R.id.contenedor);

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

        currentUser = mAuth.getCurrentUser();
        String email = "sin_email";
        if(currentUser!=null){
            email = currentUser.getEmail();
            user = new User(email);
            tvTitulo.setText("Nivel: " + nivel_actual);
        }else{
            user = new User();
            nivel_actual = -1;
            tvTitulo.setText("");
        }
        mPref = new manejadorPreferencias(email, getActivity());
        nivel_actual = Integer.parseInt(mPref.get("nivel", "0"));

        listenerReload = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadGame();
            }
        };
        listenerNext2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Tu flipas", Toast.LENGTH_SHORT).show();
            }
        };
        listenerNext = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadGame();
                if(nivel_actual==12){
                    Toast.makeText(getContext(), "Ya te has pasado todos los niveles", Toast.LENGTH_SHORT).show();
                    assert getParentFragment() != null;
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.niveles);
                }else{
                    mPref.put("nivel", (nivel_actual+1)+"");
                    nivel_actual++;
                    tvTitulo.setText("Nivel: " + nivel_actual);
                    reloadGame();
                }
            }
        };
        listenerMenu = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser == null){
                    Intent i = new Intent(getContext(), HomeMenuActivity.class);
                    startActivity(i);
                }else{
                    assert getParentFragment() != null;
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.niveles);
                    dialog.dismiss();
                }
            }
        };

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

                String titulo, titulo2;
                int porcentaje = 100-num;
                int respuesta = seekBar.getProgress();
                int resultado = porcentaje - respuesta;
                int dinero, exp;
                if(respuesta !=  0){
                    if(respuesta == porcentaje){
                        titulo = "Perfecto";
                        titulo2 = "Puedes pasar al siguiente nivel";
                        num_estrellas = 3;
                        dinero = 5;
                        exp = 10;
                    }else if(resultado == 1 || resultado == -1){
                        titulo = "Muy bien";
                        titulo2 = "Has fallado solo por uno, era " + porcentaje;
                        num_estrellas = 2;
                        dinero = 2;
                        exp = 5;
                    }else if(resultado == 2 || resultado == -2 || resultado == 3 || resultado == -3){
                        titulo = "Has estado cerca";
                        titulo2 = "Has estado cerca, puedes volver a intentarlo, era " + porcentaje;
                        num_estrellas = 1;
                        dinero = 1;
                        exp = 1;
                    }else{
                        titulo = "Has fallado";
                        titulo2 = "Puedes volver a intentarlo, era: " + porcentaje;
                        num_estrellas = 0;
                        dinero = 0;
                        exp = 0;
                    }

                    if(currentUser != null){
                        if(num_estrellas > 0) user.subirNivel(gamemode, nivel_actual);
                        user.incrementarDinero(dinero);
                        user.incrementarExperiencia(exp);
                        user.actualizarEstrellas(num_estrellas, gamemode, nivel_actual);
                    }else{
                        titulo = "Era " + porcentaje;
                        titulo2 = "No obienes recompensas sin iniciar sesión";
                        dinero = 0;
                        exp = 0;
                        listenerNext = null;
                    }
                    dialog = user.alertFinalPartida(titulo, titulo2, num_estrellas*2, dinero, exp, getActivity(), listenerReload, listenerNext2, listenerNext, listenerMenu, dialog, getContext());
                    dialog.show();
                    sb.setEnabled(false);
                }
            }
        });
        return v;
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
        int altura = getResources().getDimensionPixelSize(dimensiones[num])*3;
        tvColor.getLayoutParams().height = altura;
    }

}