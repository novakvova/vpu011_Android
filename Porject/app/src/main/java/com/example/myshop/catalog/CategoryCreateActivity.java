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
import com.example.myshop.R;
import com.example.myshop.dto.category.CategoryCreateDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CategoryCreateActivity extends BaseActivity {

    int SELECT_CROPPER = 300;
    Uri uri=null;
    ImageView IVPreviewImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_create);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
    }

    public void handleCreateCategoryClick(View view) {
        CategoryCreateDTO create = new CategoryCreateDTO();
        create.setName("sfdsdf");
        create.setDescription("sdfsdf");
        create.setPriority(1);
        create.setImageBase64(uriGetBase64(uri));
        int i =0;
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