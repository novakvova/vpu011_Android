package com.example.myshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myshop.application.HomeApplication;
import com.example.myshop.constants.Urls;
import com.example.myshop.dto.category.CategoryItemDTO;
import com.example.myshop.service.CategoryNetwork;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.myshop.catalog.CatalogActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView avatar = (ImageView) findViewById(R.id.iv_avatar);
        String url= Urls.BASE+"/images/3.jpg";
        Glide.with(HomeApplication.getAppContext())
                .load(url)
                .apply(new RequestOptions().override(600))
                .into(avatar);
    }
    
    public void onClickBtn(View view) {
        //Toast.makeText(this, "Привіт! Нажав на мене :)", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this, CatalogActivity.class);
        startActivity(intent);
        finish();
    }


}