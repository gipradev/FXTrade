package com.example.fxcaptrade.PageAdaptor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.fxcaptrade.DashBoard.InvestAmount;
import com.example.fxcaptrade.DashBoard.InvestmentDetails;
import com.example.fxcaptrade.DashBoard.ReturnFragment;

public class InvestmentPageAdaptor extends FragmentStatePagerAdapter {


    public InvestmentPageAdaptor(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new InvestAmount();
                break;
            case 1:
                fragment = new InvestmentDetails();
                break;
            case 2:
                fragment = new ReturnFragment();
                break;
            default:
                return null;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Invest Amount";
            case 1:
                return "Investment Details";
            case 2:
                return "Return Details";
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
