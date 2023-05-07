package com.example.myshop.catalog;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myshop.BaseActivity;
import com.example.myshop.R;
import com.example.myshop.catalog.categorycard.CategoriesAdapter;
import com.example.myshop.dto.category.CategoryItemDTO;
import com.example.myshop.service.CategoryNetwork;
import com.example.myshop.utils.CommonUtils;

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
        CommonUtils.showLoading();
        CategoryNetwork
                .getInstance()
                .getJSONApi()
                .list()
                .enqueue(new Callback<List<CategoryItemDTO>>() {
                    @Override
                    public void onResponse(Call<List<CategoryItemDTO>> call, Response<List<CategoryItemDTO>> response) {
                        List<CategoryItemDTO> data = response.body();
                        //CategoryItemDTO one = data.get(0);
                        categoriesAdapter = new CategoriesAdapter(data,
                                CatalogActivity.this::onClickDeleteCategory,
                                CatalogActivity.this::onClickEditCategory);
                        rcvCategories.setAdapter(categoriesAdapter);
                        CommonUtils.hideLoading();
                    }
                    @Override
                    public void onFailure(Call<List<CategoryItemDTO>> call, Throwable t) {
                        CommonUtils.hideLoading();
                    }
                });
    }

    private void onClickDeleteCategory(CategoryItemDTO category) {
        //Toast.makeText(this, "Видаляємо "+category.getId(), Toast.LENGTH_SHORT).show();
        CommonUtils.showLoading();
        CategoryNetwork
                .getInstance()
                .getJSONApi()
                .delete(category.getId())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Intent intent = new Intent(CatalogActivity.this, CatalogActivity.class);
                        startActivity(intent);
                        finish();
                        CommonUtils.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        CommonUtils.hideLoading();
                    }
                });
    }

    private void onClickEditCategory(CategoryItemDTO category) {
        //Toast.makeText(this, "Редагуємо "+ category.getId(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(CatalogActivity.this, CategoryEditActivity.class);
        Bundle b = new Bundle();
        b.putInt("id", category.getId());
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }
}