package com.example.thithu2_and103;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.thithu2_and103.server.HttpRequest;
import com.example.thithu2_and103.server.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AddActivity extends AppCompatActivity {
    ImageView ivChosseImage;
    EditText etTenXe, etMauSac, etGiaBan, etMoTa;
    Button btnAdd, btnBack;
    HttpRequest httpRequest;
    Uri selectedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ivChosseImage = findViewById(R.id.ivChoseImage);

        etTenXe = findViewById(R.id.etTenXe);
        etMauSac = findViewById(R.id.etMauSac);
        etGiaBan = findViewById(R.id.edtGiaBan);
        etMoTa = findViewById(R.id.edtMoTa);
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);
        httpRequest = new HttpRequest();

        ivChosseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String , RequestBody> mapRequestBody = new HashMap<>();
                String ten_xe = etTenXe.getText().toString().trim();
                String mau_sac = etMauSac.getText().toString().trim();
                String gia_ban = etGiaBan.getText().toString().trim();
//                String _status = etStatus.getText().toString().trim();
                String mo_ta = etMoTa.getText().toString().trim();

                mapRequestBody.put("ten_xe", getRequestBody(ten_xe));
                mapRequestBody.put("mau_sac", getRequestBody(mau_sac));
                mapRequestBody.put("gia_ban", getRequestBody(gia_ban));
                mapRequestBody.put("mo_ta", getRequestBody(mo_ta));
                MultipartBody.Part multipartBody = null;
                if (selectedImageUri != null) {
                    File file = createFileFormUri(selectedImageUri, "hinh_anh"); // Tạo File từ Uri
                    if (file != null) {
                        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                        multipartBody = MultipartBody.Part.createFormData("hinh_anh", file.getName(), requestFile);
                    }
                }

                if(multipartBody != null){
                    httpRequest.callAPI().add(mapRequestBody,multipartBody).enqueue(new Callback<Response<XeMay>>() {
                        @Override
                        public void onResponse(Call<Response<XeMay>> call, retrofit2.Response<Response<XeMay>> response) {
                            if(response.isSuccessful()){
                                if(response.body().getStatus() == 200){
                                    Toast.makeText(AddActivity.this, "Thêm thành công !!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Response<XeMay>> call, Throwable t) {

                        }
                    });
                }


            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getImage.launch(intent);
    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        // Lấy Uri của ảnh được chọn
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            displaySelectedImage(selectedImageUri);
                        }
                    }
                }
            });

    private void displaySelectedImage(Uri imageUri) {
        Glide.with(this)
                .load(imageUri)
                .centerCrop()
                .into(ivChosseImage);
    }

    private File createFileFormUri(Uri uri, String name) {
        File file = new File(getCacheDir(), name + ".png");
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            Log.d("FILE_CREATE", "File created: " + file.getAbsolutePath());
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private RequestBody getRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }
}