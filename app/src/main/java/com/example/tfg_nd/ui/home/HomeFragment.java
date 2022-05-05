package com.example.tfg_nd.ui.home;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.tfg_nd.R;
import com.example.tfg_nd.databinding.FragmentHomeBinding;
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

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    ListenerRegistration listener;
    TextView dinero, tvEmail;
    String email;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dinero = root.findViewById(R.id.dinero);
        tvEmail = root.findViewById(R.id.tvEmail);

        mAuth = FirebaseAuth.getInstance();
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
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        }else{
            Log.d(TAG, "No estás logeado");
            dinero.setText("????");
            tvEmail.setText("No estás logeado");
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        listener.remove();
    }
}