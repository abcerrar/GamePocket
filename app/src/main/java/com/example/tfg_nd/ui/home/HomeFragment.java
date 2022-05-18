package com.example.tfg_nd.ui.home;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.tfg_nd.R;
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
    private LinearLayout btPorcentaje, btMemory, btTresraya, btEstadísticas, btTienda;

    ListenerRegistration listener;
    TextView dinero, tvEmail;
    String email;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btPorcentaje = root.findViewById(R.id.btPorcentajes);
        btMemory = root.findViewById(R.id.btMemory);
        btTresraya = root.findViewById(R.id.btTresraya);
        dinero = root.findViewById(R.id.dinero);
        tvEmail = root.findViewById(R.id.tvEmail);
        btEstadísticas = root.findViewById(R.id.btEstadisticas);
        btTienda = root.findViewById(R.id.btTienda);

        mAuth = FirebaseAuth.getInstance();
        mPref = new manejadorPreferencias("pref", getActivity());
        FirebaseUser currentUser = mAuth.getCurrentUser();

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
                        String dorso_actual="", ficha_actual="";
                        try{
                            dorso_actual = snapshot.getData().get("current_dorso")+"";
                            ficha_actual = snapshot.getData().get("current_ficha")+"";
                        }catch(NullPointerException ex){
                            Toast.makeText(getContext(), "Error al leer el dorso o ficha actual", Toast.LENGTH_SHORT).show();
                        }
                        mPref.put("dorso_memory", dorso_actual);
                        mPref.put("fichas_tresraya", ficha_actual);

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
                Toast.makeText(getContext(), "Aun no esta implementado", Toast.LENGTH_SHORT).show();
            }
        });

        btTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.tienda);
            }
        });

        return root;
    }
    /*
    public void botonGris (LinearLayout layout){
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_UP:
                        layout.setBackgroundColor(getResources().getColor(R.color.fondo3));
                        break;
                    case MotionEvent.ACTION_DOWN:
                        layout.setBackground(getResources().get
                        break;
                }
                return false;
            }
        });
    }
     */

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