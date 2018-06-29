package com.example.kamil.astroweather2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kamil.astroweather2.R;


public class Tabs_astro1 extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView;
        rootView = inflater.inflate(R.layout.landscape, container, false);

        FragmentManager childFragMan = getChildFragmentManager();
        FragmentTransaction childFragTrans = childFragMan.beginTransaction();
        Tab_Sun fragment1 = new Tab_Sun ();
        Tab_Moon fragment2 = new Tab_Moon ();

        childFragTrans.add(R.id.fragment, fragment1);
        childFragTrans.add(R.id.fragment2, fragment2);
        childFragTrans.addToBackStack("B");
        childFragTrans.commit();


        return rootView;
    }

}
