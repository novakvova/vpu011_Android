package com.example.myshop.catalog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.myshop.BaseActivity;
import com.example.myshop.R;
import com.example.myshop.catalog.categorycard.CategoriesAdapter;
import com.example.myshop.dto.category.CategoryItemDTO;
import com.example.myshop.service.CategoryNetwork;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogActivity extends BaseActivity {

    CategoriesAdapter categoriesAdapter;
    private RecyclerView rcvCategories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        rcvCategories = findViewById(R.id.rcvCategories);
        rcvCategories.setHasFixedSize(true);
        rcvCategories.setLayoutManager(
                new GridLayoutManager(this, 1, LinearLayoutManager.VERTICAL, false));
        requestServer();
    }

    private void requestServer() {
        CategoryNetwork
                .getInstance()
                .getJSONApi()
                .list()
                .enqueue(new Callback<List<CategoryItemDTO>>() {
                    @Override
                    public void onResponse(Call<List<CategoryItemDTO>> call, Response<List<CategoryItemDTO>> response) {
                        List<CategoryItemDTO> data = response.body();
                        //CategoryItemDTO one = data.get(0);
                        categoriesAdapter = new CategoriesAdapter(data);
                        rcvCategories.setAdapter(categoriesAdapter);
                    }
                    @Override
                    public void onFailure(Call<List<CategoryItemDTO>> call, Throwable t) {
                    }
                });
    }
}