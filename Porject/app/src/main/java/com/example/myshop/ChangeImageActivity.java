package com.example.myshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.oginotihiro.cropview.CropView;

public class ChangeImageActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    public static String base64;
    private CropView cropView;
    private Bitmap croppedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_image);
        cropView = findViewById(R.id.cropView);
        Intent i =new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE) {
            if(resultCode == RESULT_OK && data!=null){
                Uri selectedImage = data.getData();
                cropView.of(selectedImage).asSquare().initialize(this);
            }

        }
    } //onActivityResult

    public void RotateRightImage(View view) {

    }

    public void RotateLeftImage(View view) {

    }

    public void ChangeImage(View view) {

    }
}