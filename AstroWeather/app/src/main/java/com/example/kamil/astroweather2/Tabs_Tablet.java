package com.example.kamil.astroweather2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kamil.astroweather2.R;

public class Tabs_Tablet extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView;
        rootView = inflater.inflate(R.layout.tablayoutexample, container, false);

        FragmentManager childFragMan = getChildFragmentManager();
        FragmentTransaction childFragTrans = childFragMan.beginTransaction();

        Tab_Sun fragment1 = new Tab_Sun ();
        Tab_Moon fragment2 = new Tab_Moon ();
        Tab_Pogoda1 fragment3 = new Tab_Pogoda1 ();
        Tab_Pogoda2 fragment4 = new Tab_Pogoda2 ();
        Tab_Prognoza fragment5 = new Tab_Prognoza ();

        childFragTrans.add(R.id.fragment1, fragment1);
        childFragTrans.add(R.id.fragment2, fragment2);
        childFragTrans.add(R.id.fragment3, fragment3);
        childFragTrans.add(R.id.fragment4, fragment4);
        childFragTrans.add(R.id.fragment5, fragment5);
        childFragTrans.addToBackStack("B");
        childFragTrans.commit();


        return rootView;
    }

}
