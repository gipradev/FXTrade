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

import com.example.fxcaptrade.RecyclerAdaptor.TabAdaptor;
import com.example.fxcaptrade.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveInactiveFragment extends Fragment {
    private static final String TAG = "ActiveInactiveFragment";

    private ViewPager viewPager;
    private TabLayout tabLayout;

    public ActiveInactiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_active_inactive, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        TabAdaptor adapter = new TabAdaptor(getChildFragmentManager());
        adapter.addFragment(new ActiveFragment(), "Active Members");
        adapter.addFragment(new InactiveFragment(), "Inactive Members");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


}
