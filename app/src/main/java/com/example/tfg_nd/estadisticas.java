package com.example.tfg_nd;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class estadisticas extends Fragment {

    ArrayList<String> nombres = new ArrayList<>();
    ArrayList<String> numeros = new ArrayList<>();
    ArrayList<String> imagenes = new ArrayList<>();
    Spinner spinner;
    RecyclerView recyclerView;
    TextView titulo_spinner;

    private TextView tvNumero, tvNobre;

    private manejadorPreferencias mPref;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String email = "sin_email";



    public estadisticas() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_estadisticas, container, false);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) email = currentUser.getEmail();
        mPref = new manejadorPreferencias(email, getActivity());

        tvNobre = v.findViewById(R.id.st_nombre);
        tvNumero = v.findViewById(R.id.st_numero);
        recyclerView = v.findViewById(R.id.lista);
        spinner = v.findViewById(R.id.spinner);
        titulo_spinner = v.findViewById(R.id.titulo_spinner);

        spinneador(spinner, new String[]{"", "Porcentajes", "Memory", "Tres en raya", "Solitario", "Flappy bids", "Dinero"});
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(spinner.getSelectedItem().toString()){
                    case "Porcentajes":
                        consulta_puzzle("porcentajes");
                        titulo_spinner.setText("Porcentajes");
                        break;
                    case "Memory":
                        consulta_puzzle("memory");
                        titulo_spinner.setText("Memory");
                        break;
                    case "Tres en raya":
                        consulta_puzzle("tresraya");
                        titulo_spinner.setText("Tres en raya");
                        break;
                    case "Solitario":
                        titulo_spinner.setText("Solitario");
                        nombres.clear();
                        numeros.clear();
                        imagenes.clear();
                        tvNumero.setText("Tiempo record");
                        db.collection("solitario").orderBy("record_tiempo").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> documentos =  queryDocumentSnapshots.getDocuments();
                                for(int i=0; i<documentos.size(); i++){
                                    nombres.add(documentos.get(i).getId());
                                    numeros.add(documentos.get(i).getData().get("record_tiempo")+"");
                                    imagenes.add(R.drawable.time+"");
                                }
                                cargarRecycler();
                            }
                        });
                        break;
                    case "Flappy bids":
                        titulo_spinner.setText("Flappy Birds");
                        tvNumero.setText("Máxima puntuación");
                        nombres.clear();
                        numeros.clear();
                        imagenes.clear();
                        db.collection("flappy").orderBy("record", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> documentos =  queryDocumentSnapshots.getDocuments();
                                for(int i=0; i<documentos.size(); i++){
                                    nombres.add(documentos.get(i).getId());
                                    numeros.add(documentos.get(i).getData().get("record")+"");
                                    imagenes.add(R.drawable.piperesized+"");
                                }
                                cargarRecycler();
                            }
                        });
                        break;
                    case "Dinero":
                        tvNumero.setText("Dinero");
                        nombres.clear();
                        numeros.clear();
                        imagenes.clear();
                        db.collection("users").orderBy("dinero", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> documentos =  queryDocumentSnapshots.getDocuments();
                                for(int i=0; i<documentos.size(); i++){
                                    nombres.add(documentos.get(i).getId());
                                    numeros.add(documentos.get(i).getData().get("dinero")+"");
                                    imagenes.add(R.drawable.coin+"");
                                }
                                cargarRecycler();
                            }
                        });
                        break;
                }
                spinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    public void consulta_puzzle(String juego){
        tvNumero.setText("Nº total estrellas");
        nombres.clear();
        numeros.clear();
        imagenes.clear();
        db.collection(juego).orderBy("total_estrellas", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentos =  queryDocumentSnapshots.getDocuments();
                for(int i=0; i<documentos.size(); i++){
                    nombres.add(documentos.get(i).getId());
                    numeros.add(documentos.get(i).getData().get("total_estrellas")+"");
                    imagenes.add(R.drawable.star+"");
                }
                cargarRecycler();
            }
        });
    }





    public void cargarRecycler(){
        LinearLayoutManager layout = new LinearLayoutManager(getContext());

        //DividerItemDecoration linea = new DividerItemDecoration(getContext(), layout.getOrientation());
        //recyclerView.addItemDecoration(linea);

        recyclerView.setLayoutManager(layout);

        Adapter_stats adapter = new Adapter_stats(getActivity(), getContext(), getParentFragment(), nombres, numeros, imagenes);
        recyclerView.setAdapter(adapter);

    };


    public void spinneador (Spinner sp1, String[] opciones){
        ArrayAdapter adaptador = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, opciones);
        adaptador.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        sp1.setAdapter(adaptador);
    }
}