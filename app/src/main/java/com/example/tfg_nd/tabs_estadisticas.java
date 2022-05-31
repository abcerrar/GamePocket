package com.example.tfg_nd;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class tabs_estadisticas extends Fragment {

    TabLayout my_tabLayout;
    ViewPager2 my_viewPager;

    public tabs_estadisticas() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tabs_estadisticas, container, false);

        my_tabLayout= (TabLayout) v.findViewById(R.id.tabs);
        my_viewPager= (ViewPager2) v.findViewById(R.id.vp);

        Adapter_tabs adapter=new Adapter_tabs(getActivity());
        my_viewPager.setAdapter(adapter);
        new TabLayoutMediator(my_tabLayout, my_viewPager,
                (tab, position) -> tab.setText(adapter.getTabTitle(position))
        ).attach();
        
        return v;
    }
}