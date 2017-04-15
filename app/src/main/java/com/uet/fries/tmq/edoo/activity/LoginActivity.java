package com.uet.fries.tmq.edoo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import com.uet.fries.tmq.edoo.R;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends Activity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @OnClick(R.id.btnLogin)
    void submit() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
    }
}
