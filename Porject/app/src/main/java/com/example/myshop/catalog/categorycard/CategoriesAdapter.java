package com.example.myshop.catalog.categorycard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myshop.R;
import com.example.myshop.application.HomeApplication;
import com.example.myshop.constants.Urls;
import com.example.myshop.dto.category.CategoryItemDTO;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoryCardViewHolder> {
    private List<CategoryItemDTO> categories;
    private final OnCategoryClickListener onDeleteListener;
    private final OnCategoryClickListener onEditListener;

    public CategoriesAdapter(List<CategoryItemDTO> categories, OnCategoryClickListener onDeleteListener,
                             OnCategoryClickListener onEditListener) {
        this.categories = categories;
        this.onDeleteListener = onDeleteListener;
        this.onEditListener = onEditListener;
    }

    @NonNull
    @Override
    public CategoryCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout= LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.category_view, parent, false);
        return new CategoryCardViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryCardViewHolder holder, int position) {
        if(categories!=null && position<categories.size())
        {
            CategoryItemDTO categoryItemDTO = categories.get(position);
            holder.categoryName.setText(categoryItemDTO.getName());
            String url = Urls.BASE + categoryItemDTO.getImage();
            Glide.with(HomeApplication.getAppContext())
                    .load(url)
                    .apply(new RequestOptions().override(600))
                    .into(holder.categoryImage);
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDeleteListener.onButtonClick(categoryItemDTO);
                }
            });

            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onEditListener.onButtonClick(categoryItemDTO);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
