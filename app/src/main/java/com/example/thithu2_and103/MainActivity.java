package com.example.thithu2_and103;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thithu2_and103.server.HttpRequest;
import com.example.thithu2_and103.server.Response;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements AdapterXeMay.XeMayClick {

    AdapterXeMay adapter;
    ArrayList<XeMay> list;
    RecyclerView rycList;
    HttpRequest httpRequest;
    EditText edtSearch;
    FloatingActionButton btnAdd;
    Uri imageUri;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rycList = findViewById(R.id.rycList);
        list = new ArrayList<>();
        adapter = new AdapterXeMay(this,list,this);
        rycList.setAdapter(adapter);
        httpRequest = new HttpRequest();
        edtSearch = findViewById(R.id.edtSearch);
        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }

        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String key = edtSearch.getText().toString();
                httpRequest.callAPI().search(key).enqueue(new Callback<Response<ArrayList<XeMay>>>() {
                    @Override
                    public void onResponse(Call<Response<ArrayList<XeMay>>> call, retrofit2.Response<Response<ArrayList<XeMay>>> response) {
                        if(response.isSuccessful()){
                            if(response.body().getStatus() == 200){
                                list.clear();
                                list.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<ArrayList<XeMay>>> call, Throwable t) {

                    }
                });
                return false;
            }
        });

        httpRequest.callAPI().getList().enqueue(new Callback<Response<ArrayList<XeMay>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<XeMay>>> call, retrofit2.Response<Response<ArrayList<XeMay>>> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        list.clear();
                        list.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<XeMay>>> call, Throwable t) {
                Log.d("zzzz", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        httpRequest.callAPI().getList().enqueue(new Callback<Response<ArrayList<XeMay>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<XeMay>>> call, retrofit2.Response<Response<ArrayList<XeMay>>> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 200){
                        list.clear();
                        list.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<XeMay>>> call, Throwable t) {

            }
        });
    }

    @Override
    public void delete(XeMay xeMay) {

    }

    @Override
    public void edit(XeMay xeMay) {
        Intent intent =new Intent(MainActivity.this, UpdataActivity.class);
        intent.putExtra("XeMay", xeMay);
        startActivity(intent);
    }

    @Override
    public void showDetail(XeMay xeMay) {

    }
}