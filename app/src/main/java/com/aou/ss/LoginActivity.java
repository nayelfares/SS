package com.aou.ss;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import uz.jamshid.lib.WinterLayout;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        WinterLayout winter=(WinterLayout) findViewById(R.id.winter);
        winter.startWinter();
    }

    public void login(View v){
        startActivity(new Intent(this,MainActivity.class));
    }


}