package com.example.tfg_nd;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class Adapter_stats extends RecyclerView.Adapter<Adapter_stats.ViewHolder> {

    private List<String> nombre;
    private List<String> numero;
    private List<String> imagen;
    private LayoutInflater mInflater;
    private Activity activity;
    private Context context;
    private androidx.fragment.app.Fragment fragment;
    Dialog dialog;

    //Variables para el fragment extra
    int star1, star2, star3, recordFlappy, nivel, dinero;
    String recordSolitario, nom_stats, temporal_email;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Constructor
    public Adapter_stats(Activity activity, Context context, Fragment fragment, List<String> nombre, List<String> numero, List<String> imagen){
        this.mInflater= LayoutInflater.from(context);
        this.context = context;
        this.nombre = nombre;
        this.numero = numero;
        this.imagen = imagen;
        this.activity = activity;
        this.fragment = fragment;
        this.dialog = new comp_estadisticas().getAnim(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.row_stats, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder (ViewHolder holder, int position){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String email = "";
        if(user != null) email = user.getEmail();

        String nom = nombre.get(holder.getAdapterPosition());
        holder.tvNombre.setText(nom);

        String num = numero.get(holder.getAdapterPosition());
        holder.tvNumero.setText(num);

        if(email.equals(nom)) {
            holder.tvNombre.setTextColor(activity.getResources().getColor(R.color.amarillo));
            holder.tvNumero.setTextColor(activity.getResources().getColor(R.color.amarillo));
        }

        int img = Integer.parseInt(imagen.get(holder.getAdapterPosition()));
        holder.img.setImageResource(img);

        holder.tvNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = nombre.get(holder.getAdapterPosition());
                //mPref.put("email_externo", nom);
                //NavHostFragment.findNavController(fragment).navigate(R.id.perfil);
                dialog.show();
                cargarStatsGenerales(nom);
            }
        });
    }

    @Override
    public int getItemCount(){
        return nombre.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNombre;
        TextView tvNumero;
        ImageView img;
        LinearLayout plantilla;
        ViewHolder (View itenView){
            super(itenView);
            tvNombre = itenView.findViewById(R.id.row_name);
            tvNumero = itenView.findViewById(R.id.row_num);
            img = itenView.findViewById(R.id.row_img);
            plantilla = itenView.findViewById(R.id.plantilla);
        }
    }
    public void cargarStatsGenerales(String email){
        temporal_email = email;
        db.collection("porcentajes").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try{
                    star1 = Integer.parseInt(documentSnapshot.getData().get("total_estrellas")+"");
                }catch (NumberFormatException e){
                    star1=0;
                }catch (NullPointerException e){
                    star1=0;
                }
                db.collection("memory").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        try{
                            star2 = Integer.parseInt(documentSnapshot.getData().get("total_estrellas")+"");
                        }catch (NumberFormatException e){
                            star2=0;
                        }catch (NullPointerException e){
                            star2=0;
                        }
                        db.collection("tresraya").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                try{
                                    star3 = Integer.parseInt(documentSnapshot.getData().get("total_estrellas")+"");
                                }catch (NumberFormatException e){
                                    star3=0;
                                }catch (NullPointerException e){
                                    star3=0;
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
                                                    recordFlappy= 0;
                                                }catch (NullPointerException e){
                                                    recordFlappy= 0;
                                                }
                                                db.collection("users").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        try{
                                                            nivel = Integer.parseInt(documentSnapshot.getData().get("nivel")+"");
                                                            dinero = Integer.parseInt(documentSnapshot.getData().get("dinero")+"");
                                                            nom_stats = documentSnapshot.getData().get("nombre")+"";
                                                        }catch (NumberFormatException e){
                                                            nivel = -1;
                                                            dinero = -1;
                                                            nom_stats = "error";
                                                        }catch (NullPointerException e){
                                                            nivel = -1;
                                                            dinero = -1;
                                                            nom_stats = "error";
                                                        }
                                                        pintarStats();
                                                        dialog.dismiss();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View custom_layout = activity.getLayoutInflater().inflate(R.layout.fragment_comp_estadisticas, null);
        TextView tvStar1, tvStar2, tvStar3, tvSolitario, tvFlappy, tvNombre, tvNivel, tvDinero, tvEmail;

        tvStar1 = custom_layout.findViewById(R.id.progress_porcentaje);
        tvStar2 = custom_layout.findViewById(R.id.progress_memory);
        tvStar3 = custom_layout.findViewById(R.id.progress_tresraya);
        tvSolitario = custom_layout.findViewById(R.id.tvRecordSolitario);
        tvFlappy = custom_layout.findViewById(R.id.tvRecordFlappy);
        tvNombre = custom_layout.findViewById(R.id.tvStatsNombre);
        tvNivel = custom_layout.findViewById(R.id.tvStatsNivel);
        tvDinero = custom_layout.findViewById(R.id.tvStatsDinero);
        tvEmail = custom_layout.findViewById(R.id.email);


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
        tvStar1.getLayoutParams().width = activity.getResources().getDimensionPixelSize(dimensiones[star1])*4;
        if(star1>4) tvStar1.setText(star1+"★");
        tvStar2.getLayoutParams().width = activity.getResources().getDimensionPixelSize(dimensiones[star2])*4;
        if(star2>4) tvStar2.setText(star2+"★");
        tvStar3.getLayoutParams().width = activity.getResources().getDimensionPixelSize(dimensiones[star3])*4;
        if(star3>4) tvStar3.setText(star3+"★");
        tvSolitario.setText(recordSolitario+"");
        tvFlappy.setText(recordFlappy+"");
        tvNombre.setText(nom_stats+"");
        tvNivel.setText(nivel+"");
        tvDinero.setText(dinero+"");

        builder.setCancelable(true);
        builder.setView(custom_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

    }


}

