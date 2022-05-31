package com.example.tfg_nd;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser;
    manejadorPreferencias mPref;
    static public int width;
    static public int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        currentUser = mAuth.getCurrentUser();
        String email = "sin_email";
        if(currentUser!=null) email = currentUser.getEmail();
        mPref = new manejadorPreferencias(email, this);

        setSupportActionBar(binding.appBarHomeMenu.toolbar);


        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.login, R.id.perfil, R.id.test, R.id.niveles, R.id.tienda)
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

    //Botones del navigation drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.w(TAG, item.getItemId()+"");


        int id = item.getItemId();
        //Se pueden añadir animaciones para el menu
        switch (id){
            case R.id.perfil:
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
            case R.id.porcentajes:
                if(currentUser!=null){
                    mPref.put("gamemode", "porcentajes");
                    navController.navigate(R.id.niveles);
                }else{
                    mPref.put("nivel", "1");
                    navController.navigate(R.id.puzzle_porcenaje);
                }
                break;
            case R.id.memory:
                if(currentUser!=null){
                    mPref.put("gamemode", "memory");
                    navController.navigate(R.id.niveles);
                }else{
                    mPref.put("nivel", "1");
                    navController.navigate(R.id.puzzle_memory);
                }
                break;
            case R.id.tresenraya:
                if(currentUser!=null){
                    mPref.put("gamemode", "tresraya");
                    navController.navigate(R.id.niveles);
                }else {
                    mPref.put("nivel", "-1");
                    navController.navigate(R.id.puzzle_tresraya);
                }
                break;
            case R.id.tienda:
                navController.navigate(R.id.tienda);
                break;
            case R.id.flappy:
                HomeMenuActivity.this.startActivity(new Intent(this, StartGame.class));
                break;
            case R.id.klondike:
                HomeMenuActivity.this.startActivity(new Intent(this, Klondike.class));
                break;
            case R.id.estadisticas:
                if(currentUser != null) navController.navigate(R.id.tabs_estadisticas);
                else Toast.makeText(HomeMenuActivity.this, "Debes iniciar sesión para ver las estadísticas", Toast.LENGTH_SHORT).show();
                break;


        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    //Botones del menu normal
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Aún no hay ajustes", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_home:
                startActivity(new Intent(getApplicationContext(), HomeMenuActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}