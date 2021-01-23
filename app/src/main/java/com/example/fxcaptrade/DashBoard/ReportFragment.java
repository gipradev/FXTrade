package com.example.fxcaptrade.DashBoard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fxcaptrade.RecyclerAdaptor.TabAdaptor;
import com.example.fxcaptrade.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        TabAdaptor adapter = new TabAdaptor(getChildFragmentManager());
        adapter.addFragment(new InvestmentDetails(), "Investment Wallet");
        adapter.addFragment(new InvestAmount(), "Income Wallet");
        adapter.addFragment(new ReturnFragment(), "ROI Wallet");
        adapter.addFragment(new ReturnFragment(), "Sponsorship Wallet");
        adapter.addFragment(new ReturnFragment(), "Rewards Wallet");
        tabLayout.setupWithViewPager(viewPager);
    }
}
