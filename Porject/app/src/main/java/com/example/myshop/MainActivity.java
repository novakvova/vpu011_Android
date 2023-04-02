package com.example.myshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void onClickBtn(View view) {
        Toast.makeText(this, "Привіт! Нажав на мене :)", Toast.LENGTH_LONG).show();
    }
}