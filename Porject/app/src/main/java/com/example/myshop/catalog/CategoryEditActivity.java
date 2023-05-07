package com.example.myshop.catalog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myshop.BaseActivity;
import com.example.myshop.ChangeImageActivity;
import com.example.myshop.R;
import com.example.myshop.application.HomeApplication;
import com.example.myshop.constants.Urls;
import com.example.myshop.dto.category.CategoryCreateDTO;
import com.example.myshop.dto.category.CategoryItemDTO;
import com.example.myshop.dto.category.CategoryUpdateDTO;
import com.example.myshop.service.CategoryNetwork;
import com.example.myshop.utils.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryEditActivity extends BaseActivity {

    int SELECT_CROPPER = 300;
    int id=0;
    Uri uri=null;
    ImageView IVPreviewImage;

    TextInputEditText txtCategoryName;
    TextInputEditText txtCategoryPriority;
    TextInputEditText txtCategoryDescription;


    TextInputLayout txtFieldCategoryName;
    TextInputLayout txtFieldCategoryPriority;
    TextInputLayout txtFieldCategoryDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_edit);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);

        txtCategoryName=findViewById(R.id.txtCategoryName);
        txtCategoryPriority=findViewById(R.id.txtCategoryPriority);
        txtCategoryDescription=findViewById(R.id.txtCategoryDescription);

        txtFieldCategoryName = (TextInputLayout) findViewById(R.id.txtFieldCategoryName);
        txtFieldCategoryPriority = (TextInputLayout) findViewById(R.id.txtFieldCategoryPriority);
        txtFieldCategoryDescription = (TextInputLayout) findViewById(R.id.txtFieldCategoryDescription);

        Bundle b = getIntent().getExtras();
        if(b!=null)
            id=b.getInt("id");
        initialInputValue();

        setupError();
    }

    private void initialInputValue() {
        CommonUtils.showLoading();
        CategoryNetwork
                .getInstance()
                .getJSONApi()
                .getById(id)
                .enqueue(new Callback<CategoryItemDTO>() {
                    @Override
                    public void onResponse(Call<CategoryItemDTO> call, Response<CategoryItemDTO> response) {
                        if(response.isSuccessful())
                        {
                            CategoryItemDTO category= response.body();
                            txtCategoryName.setText(category.getName());
                            txtCategoryDescription.setText(category.getDescription());
                            txtCategoryPriority.setText(Integer.toString(category.getPriority()));
                            String url= Urls.BASE+category.getImage();
                            Glide.with(HomeApplication.getAppContext())
                                    .load(url)
                                    .apply(new RequestOptions().override(600))
                                    .into(IVPreviewImage);
                        }
                        CommonUtils.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<CategoryItemDTO> call, Throwable t) {
                        CommonUtils.hideLoading();
                    }
                });
    }

    public void handleUpdateCategoryClick(View view) {

        if(!validationForm()) {
            Toast.makeText(this, "Дані вказно не коректно", Toast.LENGTH_LONG).show();
            return;
        }

        CategoryUpdateDTO update = new CategoryUpdateDTO();
        update.setId(id);
        update.setName(txtCategoryName.getText().toString());
        update.setDescription(txtCategoryDescription.getText().toString());
        update.setPriority(Integer.parseInt(txtCategoryPriority.getText().toString()));
        if(uri!=null)
            update.setImageBase64(uriGetBase64(uri));
        CommonUtils.showLoading();
        CategoryNetwork.getInstance()
                .getJSONApi()
                .update(update)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        CommonUtils.hideLoading();
                        Intent intent = new Intent(CategoryEditActivity.this, CatalogActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        CommonUtils.hideLoading();
                    }
                });
    }


    public boolean validationForm() {
        boolean isValid=true;
        String name = txtCategoryName.getText().toString();
        if(name.isEmpty() || name.length()<=2) {
            txtFieldCategoryName.setError(getString(R.string.category_name_required));
            isValid=false;
        }
        String priority = txtCategoryPriority.getText().toString();
        int number=0;
        try {
            number = Integer.parseInt(priority.toString());
        }
        catch (Exception ex) {

        }
        if (number<=0) {
            txtFieldCategoryPriority.setError(getString(R.string.category_priority_required));
            txtFieldCategoryPriority.setErrorEnabled(true);
            isValid=false;
        }
        String description = txtCategoryDescription.getText().toString();
        if(description.isEmpty() || description.length()<=2) {
            txtFieldCategoryDescription.setError(getString(R.string.category_description_required));
            txtFieldCategoryDescription.setErrorEnabled(true);
            isValid=false;
        }

        return isValid;
    }
    private void setupError() {

        txtFieldCategoryName.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() >= 0 && text.length() <= 2) {
                    txtFieldCategoryName.setError(getString(R.string.category_name_required));
                    txtFieldCategoryName.setErrorEnabled(true);
                } else {
                    txtFieldCategoryName.setErrorEnabled(false);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtFieldCategoryPriority.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                int number=0;
                try {
                    number = Integer.parseInt(text.toString());
                }
                catch (Exception ex) {

                }
                if (number<=0) {
                    txtFieldCategoryPriority.setError(getString(R.string.category_priority_required));
                    txtFieldCategoryPriority.setErrorEnabled(true);
                } else {
                    txtFieldCategoryPriority.setErrorEnabled(false);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtFieldCategoryDescription.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() >= 0 && text.length() <= 2) {
                    txtFieldCategoryDescription.setError(getString(R.string.category_description_required));
                    txtFieldCategoryDescription.setErrorEnabled(true);
                } else {
                    txtFieldCategoryDescription.setErrorEnabled(false);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

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