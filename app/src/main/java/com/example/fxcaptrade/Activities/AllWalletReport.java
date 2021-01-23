package com.example.fxcaptrade.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.example.fxcaptrade.PageAdaptor.WalletPageAdaptor;
import com.example.fxcaptrade.RecyclerAdaptor.TabAdaptor;
import com.example.fxcaptrade.R;
import com.example.fxcaptrade.WalletReports.IncomeWallet;
import com.example.fxcaptrade.WalletReports.InvestmentWallet;
import com.example.fxcaptrade.WalletReports.RewardWallet;
import com.example.fxcaptrade.WalletReports.RoiWallet;
import com.example.fxcaptrade.WalletReports.SponsorshipWallet;
import com.google.android.material.tabs.TabLayout;

public class AllWalletReport extends AppCompatActivity {
    private static final String TAG = "AllWalletReport";

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_wallet_report);
        Toolbar toolbar = findViewById(R.id.toolbarDashBord);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setTitle("Reports");
        changeNotificationShadeColor(33,24,81);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Bundle extras;
        String tabIndex=null;

        if (savedInstanceState == null) {
            /*fetching extra data passed with intents in a Bundle type variable*/
            extras = getIntent().getExtras();
            if (extras == null) {
                tabIndex = null;
            } else {
                /* fetching the string passed with intent using ‘extras’*/
                tabIndex = extras.getString("index");
            }
        }


        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        WalletPageAdaptor adapter = new WalletPageAdaptor(getSupportFragmentManager(), 5);


        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(Integer.parseInt(tabIndex)).select();

    }

    private void changeNotificationShadeColor(int red,int green,int blue) {

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.rgb(red, green, blue));
        }
    }
}
