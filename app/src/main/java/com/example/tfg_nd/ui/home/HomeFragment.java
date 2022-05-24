package com.example.tfg_nd.ui.home;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.tfg_nd.HomeMenuActivity;
import com.example.tfg_nd.Klondike;
import com.example.tfg_nd.R;
import com.example.tfg_nd.StartGame;
import com.example.tfg_nd.databinding.FragmentHomeBinding;
import com.example.tfg_nd.manejadorPreferencias;
import com.example.tfg_nd.niveles;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private final String TAG = "HomeFragment.java";
    private manejadorPreferencias mPref;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private LinearLayout btEstadísticas, btTienda, btSolitario, btFlappy;
    private ImageView btPorcentaje, btMemory, btTresraya;

    ListenerRegistration listener;
    TextView dinero, tvEmail;
    String email;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btPorcentaje = root.findViewById(R.id.btPorcentaje);
        btMemory = root.findViewById(R.id.btMemory);
        btTresraya = root.findViewById(R.id.btTresraya);
        dinero = root.findViewById(R.id.dinero);
        tvEmail = root.findViewById(R.id.tvEmail);
        btEstadísticas = root.findViewById(R.id.btEstadisticas);
        btTienda = root.findViewById(R.id.btTienda);
        btFlappy = root.findViewById(R.id.btFlappy);
        btSolitario = root.findViewById(R.id.btSolitario);

        mAuth = FirebaseAuth.getInstance();
        mPref = new manejadorPreferencias("pref", getActivity());
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //Asignar la imagen del boton pulsado cuando le pulsas
        actionButton(btPorcentaje, R.drawable.boton_porcentaje, R.drawable.boton_porcentaje_pulsado);
        actionButton(btMemory, R.drawable.boton_memory, R.drawable.boton_memory_pulsado);
        actionButton(btTresraya, R.drawable.boton_tresraya, R.drawable.boton_tresraya_pulsado);

        if(currentUser != null){
            email = currentUser.getEmail();
            Log.d(TAG, "Current email: " + email);
            tvEmail.setText(email);
            DocumentReference docRef = db.collection("users").document(email);
            listener = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, "Current data: " + snapshot.getData());
                        dinero.setText(snapshot.getData().get("dinero")+"");
                        String dorso_actual="", ficha_actual="", pajaro_actual="";
                        try{
                            dorso_actual = snapshot.getData().get("current_dorso")+"";
                            ficha_actual = snapshot.getData().get("current_ficha")+"";
                            pajaro_actual = snapshot.getData().get("current_pajaro")+"";
                        }catch(NullPointerException ex){
                            Toast.makeText(getContext(), "Error al leer el dorso o ficha actual", Toast.LENGTH_SHORT).show();
                        }
                        mPref.put("dorso_memory", dorso_actual);
                        mPref.put("fichas_tresraya", ficha_actual);
                        mPref.put("current_pajaro", pajaro_actual);

                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });

            //Cargar los dorsos

        }else{
            Log.d(TAG, "No estás logeado");
            dinero.setText("????");
            tvEmail.setText("No estás logeado");
        }

        btPorcentaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser!=null){
                    mPref.put("gamemode", "porcentajes");
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.niveles);
                }else{
                    Toast.makeText(getContext(), "Debes iniciar sesión para los juegos con progreso", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser!=null){
                    mPref.put("gamemode", "memory");
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.niveles);
                }else{
                    Toast.makeText(getContext(), "Debes iniciar sesión para los juegos con progreso", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btTresraya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUser!=null){
                    mPref.put("gamemode", "tresraya");
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.niveles);
                }else{
                    Toast.makeText(getContext(), "Debes iniciar sesión para los juegos con progreso", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btEstadísticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.tabs_estadisticas);
            }
        });

        btTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.tienda);
            }
        });

        btFlappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), StartGame.class);
                startActivity(i);
            }
        });

        btSolitario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), Klondike.class);
                startActivity(i);
            }
        });



        dinero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.tienda);
            }
        });

        return root;
    }

    public static void actionButton (ImageView boton, int imagen1, int imagen2){
        boton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_UP:
                        boton.setImageResource(imagen1);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        boton.setImageResource(imagen2);
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT:
                        boton.setImageResource(imagen2);
                        break;

                }
                return false;
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        try{
            listener.remove();
        }catch(Exception e){
            Log.d(TAG, "Error al remover el listenner");
        }
    }
}