package com.example.tfg_nd;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;

public class Adapter_tabs extends FragmentStateAdapter {



    public Adapter_tabs(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                comp_estadisticas comp_estadisticas = new comp_estadisticas();
                return comp_estadisticas;
            case 1:
                estadisticas estadisticas = new estadisticas();
                return estadisticas;
            default:
                return null;
        }
    }
    @Override
    public int getItemCount() {
        return 2;
    }

    public CharSequence getTabTitle(int position){
        switch (position) {
            case 0:
                return "Estadísticas personales";
            case 1:
                return "Ranking global";
            default:
                return "PRUEBA";
        }
    }
}

