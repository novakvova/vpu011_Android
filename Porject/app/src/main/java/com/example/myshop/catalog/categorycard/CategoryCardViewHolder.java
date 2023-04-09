package com.example.myshop.catalog.categorycard;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myshop.R;

public class CategoryCardViewHolder extends RecyclerView.ViewHolder {
    private View view;
    public ImageView categoryImage;
    public TextView categoryName;
    public CategoryCardViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        categoryImage=itemView.findViewById(R.id.categoryImage);
        categoryName=itemView.findViewById(R.id.categoryName);
    }

    public View getView() {
        return view;
    }
}
