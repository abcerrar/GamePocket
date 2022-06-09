package com.example.tfg_nd;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class comp_estadisticas extends Fragment {
    private manejadorPreferencias mPref;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String email = "sin_email";

    //Variables para el fragment extra
    int star1, star2, star3, recordFlappy, nivel, dinero;

    String recordSolitario, nombre, temporal_email;
    TextView tvStar1, tvStar2, tvStar3, tvSolitario, tvFlappy, tvNombre, tvNivel, tvDinero, tvEmail;

    //animaciones
    Dialog dialog;

    public comp_estadisticas() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_comp_estadisticas, container, false);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) email = currentUser.getEmail();

        mPref = new manejadorPreferencias(email, getActivity());

        tvStar1 = v.findViewById(R.id.progress_porcentaje);
        tvStar2 = v.findViewById(R.id.progress_memory);
        tvStar3 = v.findViewById(R.id.progress_tresraya);
        tvSolitario = v.findViewById(R.id.tvRecordSolitario);
        tvFlappy = v.findViewById(R.id.tvRecordFlappy);
        tvNombre = v.findViewById(R.id.tvStatsNombre);
        tvNivel = v.findViewById(R.id.tvStatsNivel);
        tvDinero = v.findViewById(R.id.tvStatsDinero);
        tvEmail = v.findViewById(R.id.email);


        dialog = getAnim(getContext());
        dialog.show();

        cargarStatsGenerales(email);

        return v;
    }

    //La idea es que este metodo esté en una clase "Utils"
    public Dialog getAnim(Context context){
        Dialog dialog;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_progressbar);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public void cargarStatsGenerales(String email){
        temporal_email = email;
        db.collection("porcentajes").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try{
                    star1 = Integer.parseInt(documentSnapshot.getData().get("total_estrellas")+"");
                }catch (NumberFormatException e){
                    star1=1;
                }catch (NullPointerException e){
                    star1 = 1;
                }
                db.collection("memory").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        try{
                            star2 = Integer.parseInt(documentSnapshot.getData().get("total_estrellas")+"");
                        }catch (NumberFormatException e){
                            star2=0;
                        }catch(NullPointerException e){
                            star2 = 0;
                        }
                        db.collection("tresraya").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                try{
                                    star3 = Integer.parseInt(documentSnapshot.getData().get("total_estrellas")+"");
                                }catch (NumberFormatException e){
                                    star3=0;
                                }catch(NullPointerException e){
                                    star3 = 0;
                                }
                                db.collection("solitario").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot == null || !documentSnapshot.exists()) recordSolitario = "sin record";
                                        else recordSolitario = documentSnapshot.getData().get("record_tiempo")+"";
                                        db.collection("flappy").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if(documentSnapshot == null) recordFlappy = 0;
                                                else try{
                                                    recordFlappy = Integer.parseInt(documentSnapshot.getData().get("record")+"");
                                                }catch (NumberFormatException e){
                                                    recordFlappy=0;
                                                }catch (NullPointerException e){
                                                    recordFlappy=0;
                                                }
                                                db.collection("users").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        try{
                                                            nivel = Integer.parseInt(documentSnapshot.getData().get("nivel")+"");
                                                            dinero = Integer.parseInt(documentSnapshot.getData().get("dinero")+"");
                                                            nombre = documentSnapshot.getData().get("nombre")+"";
                                                        }catch (NumberFormatException e){
                                                            nivel = -1;
                                                            dinero = -1;
                                                            nombre = "error";
                                                        }catch (NullPointerException e){
                                                            nivel = -1;
                                                            dinero = -1;
                                                            nombre = "error";
                                                        }
                                                        dialog.dismiss();
                                                        pintarStats();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }
    public void pintarStats(){
        int[] dimensiones = new int[]{
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

        tvEmail.setText(temporal_email);
        tvStar1.getLayoutParams().width = getResources().getDimensionPixelSize(dimensiones[star1])*4;
        if(star1>4) tvStar1.setText(star1+"★");
        tvStar2.getLayoutParams().width = getResources().getDimensionPixelSize(dimensiones[star2])*4;
        if(star2>4) tvStar2.setText(star2+"★");
        tvStar3.getLayoutParams().width = getResources().getDimensionPixelSize(dimensiones[star3])*4;
        if(star3>4) tvStar3.setText(star3+"★");
        tvSolitario.setText(recordSolitario+"");
        tvFlappy.setText(recordFlappy+"");
        tvNombre.setText(nombre+"");
        tvNivel.setText(nivel+"");
        tvDinero.setText(dinero+"");
    }

}