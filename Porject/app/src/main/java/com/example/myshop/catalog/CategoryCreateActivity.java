package com.example.myshop.catalog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.example.myshop.BaseActivity;
import com.example.myshop.ChangeImageActivity;
import com.example.myshop.MainActivity;
import com.example.myshop.R;
import com.example.myshop.dto.category.CategoryCreateDTO;
import com.example.myshop.service.CategoryNetwork;
import com.example.myshop.utils.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryCreateActivity extends BaseActivity {

    int SELECT_CROPPER = 300;
    Uri uri=null;
    ImageView IVPreviewImage;

    TextInputEditText txtCategoryName;
    TextInputEditText txtCategoryPriority;
    TextInputEditText txtCategoryDescription;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_create);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);

        txtCategoryName=findViewById(R.id.txtCategoryName);
        txtCategoryPriority=findViewById(R.id.txtCategoryPriority);
        txtCategoryDescription=findViewById(R.id.txtCategoryDescription);
    }

    public void handleCreateCategoryClick(View view) {
        CategoryCreateDTO create = new CategoryCreateDTO();
        create.setName(txtCategoryName.getText().toString());
        create.setDescription(txtCategoryDescription.getText().toString());
        create.setPriority(Integer.parseInt(txtCategoryPriority.getText().toString()));
        create.setImageBase64(uriGetBase64(uri));
        CommonUtils.showLoading();
        CategoryNetwork.getInstance()
                .getJSONApi()
                .create(create)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        CommonUtils.hideLoading();
                        Intent intent = new Intent(CategoryCreateActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        CommonUtils.hideLoading();
                    }
                });
    }

    private String uriGetBase64(Uri uri)
    {
        try{
            Bitmap bitmap= null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // initialize byte stream
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            // compress Bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            // Initialize byte array
            byte[] bytes=stream.toByteArray();
            // get base64 encoded string
            String sImage= Base64.encodeToString(bytes, Base64.DEFAULT);
            return sImage;
        }
        catch (Exception ex) {
            return null;
        }
    }

    public void handleSelectImageClick(View view) {
        Intent intent = new Intent(this, ChangeImageActivity.class);
        startActivityForResult(intent, SELECT_CROPPER);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SELECT_CROPPER) {
            uri = (Uri) data.getParcelableExtra("croppedUri");
            IVPreviewImage.setImageURI(uri);
        }
    }
}