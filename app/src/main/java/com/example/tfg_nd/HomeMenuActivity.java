package com.example.tfg_nd;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg_nd.databinding.ActivityHomeMenuBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private final String TAG = "HomeMenuActivity.java";
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeMenuBinding binding;
    NavController navController;
    DrawerLayout drawer;
    FirebaseAuth mAuth;
    manejadorPreferencias mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mPref = new manejadorPreferencias("pref", this);

        setSupportActionBar(binding.appBarHomeMenu.toolbar);

        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.login, R.id.perfil, R.id.test)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_menu);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.w(TAG, item.getItemId()+"");

        int id = item.getItemId();
        //Se pueden añadir animaciones para el menu
        switch (id){
            case R.id.perfil:
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser!=null){
                    navController.navigate(R.id.perfil);
                }else{
                    navController.navigate(R.id.login);
                }
                break;
            case R.id.nav_home:
                navController.navigate(R.id.nav_home);
                break;
            case R.id.test:
                navController.navigate(R.id.test);
                break;
            case R.id.puzzle_1:
                mPref.put("gamemode", "puzzle_1");
                navController.navigate(R.id.niveles);
                break;
            case R.id.puzzle_2:
                mPref.put("gamemode", "puzzle_2");
                navController.navigate(R.id.niveles);
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}