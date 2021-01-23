package com.example.fxcaptrade.DashBoard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fxcaptrade.PageAdaptor.InvestmentPageAdaptor;
import com.example.fxcaptrade.RecyclerAdaptor.TabAdaptor;
import com.example.fxcaptrade.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class InvestmentFragment extends Fragment {
    private static final String TAG = "InvestmentFragment";
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public InvestmentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle("");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_investment, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        InvestmentPageAdaptor investmentPageAdaptor = new InvestmentPageAdaptor(getChildFragmentManager(),3);
        viewPager.setAdapter(investmentPageAdaptor);
        tabLayout.setupWithViewPager(viewPager);



    }


}
