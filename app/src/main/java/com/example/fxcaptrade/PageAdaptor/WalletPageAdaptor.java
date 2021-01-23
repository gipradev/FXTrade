package com.example.fxcaptrade.PageAdaptor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.fxcaptrade.DashBoard.InvestAmount;
import com.example.fxcaptrade.DashBoard.InvestmentDetails;
import com.example.fxcaptrade.DashBoard.ReturnFragment;
import com.example.fxcaptrade.WalletReports.IncomeWallet;
import com.example.fxcaptrade.WalletReports.InvestmentWallet;
import com.example.fxcaptrade.WalletReports.RewardWallet;
import com.example.fxcaptrade.WalletReports.RoiWallet;
import com.example.fxcaptrade.WalletReports.SponsorshipWallet;

public class WalletPageAdaptor extends FragmentStatePagerAdapter {

    public WalletPageAdaptor(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new InvestmentWallet();
            case 1:
                return new IncomeWallet();
            case 2:
                return new RoiWallet();
            case 3:
                return new SponsorshipWallet();
            case 4:
                return new RewardWallet();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Investment Wallet";
            case 1:
                return "Income Wallet";
            case 2:
                return "ROI Wallet";
            case 3:
                return "Sponsorship Wallet";
            case 4:
                return "Rewards Wallet";
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}

