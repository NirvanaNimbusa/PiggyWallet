package com.thailam.piggywallet.ui.walletdetail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.thailam.piggywallet.R;

public class WalletDetailActivity extends AppCompatActivity {
    private static final String EXTRA_WALLET_ID = "com.thailam.piggywallet.extras.EXTRA_WALLET_ID";

    public static Intent getIntent(Context context, int walletId) {
        Intent intent = new Intent(context, WalletDetailActivity.class);
        intent.putExtra(EXTRA_WALLET_ID, walletId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_detail);
        //
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_wallet_detail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
    }
}