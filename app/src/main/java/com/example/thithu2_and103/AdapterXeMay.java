package com.example.thithu2_and103;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thithu2_and103.server.HttpRequest;
import com.example.thithu2_and103.server.Response;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class AdapterXeMay extends RecyclerView.Adapter<AdapterXeMay.XeMayHolder> {

    Context context;
    ArrayList<XeMay> list;
    XeMayClick xeMayClick;
    HttpRequest httpRequest;


    public AdapterXeMay(Context context, ArrayList<XeMay> list, XeMayClick xeMayClick) {
        this.context = context;
        this.list = list;
        this.xeMayClick = xeMayClick;
    }

    @NonNull
    @Override
    public XeMayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new XeMayHolder(((Activity)context).getLayoutInflater().inflate(R.layout.item_xemay,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull XeMayHolder holder, int position) {
        XeMay xeMay = list.get(position);
        holder.tvTenXe.setText(xeMay.getTen_xe());
        holder.tvMauSac.setText(xeMay.getMau_sac());
        holder.tvGiaBan.setText(xeMay.getGia_ban());
        holder.tvMoTa.setText(xeMay.getMo_ta());
        httpRequest = new HttpRequest();

        String newUrl = xeMay.getHinh_anh().replace("localhost", "192.168.0.100");

        Glide.with(context).
                load(newUrl).
                into(holder.imgHinh);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDetal(xeMay);
            }
        });

        holder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xeMayClick.edit(xeMay);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa người dùng")
                        .setMessage("Bạn có chắc chắn muốn xóa người dùng này không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            httpRequest.callAPI().delete(xeMay.get_id()).enqueue(new Callback<Response<ArrayList<XeMay>>>() {
                                @Override
                                public void onResponse(Call<Response<ArrayList<XeMay>>> call, retrofit2.Response<Response<ArrayList<XeMay>>> response) {
                                    Log.d("zzzzz", "onResponse: " + xeMay.get_id())  ;
                                    if(response.isSuccessful()){
                                        if(response.body().getStatus() == 200){
                                            list.clear();
                                            list.addAll(response.body().getData());
                                            notifyDataSetChanged();
                                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Response<ArrayList<XeMay>>> call, Throwable t) {
                                    Log.d("bbbb", "onFailure: " + t.getMessage());
                                }
                            });
                        })
                        .setNegativeButton("Hủy", (dialog, which) -> {
                            dialog.dismiss();
                        });
                builder.create().show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    void showDialogDetal(XeMay xeMay){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.dialogdetail,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        //anh xa
        ImageView imgHinh = view.findViewById(R.id.ivHinh);
        TextView tvTenXe = view.findViewById(R.id.tvTenXe);
        TextView tvMauSac = view.findViewById(R.id.tvMauSac);
        TextView tvGiaBan = view.findViewById(R.id.tvGiaBan);
        TextView tvMoTa = view.findViewById(R.id.tvMoTa);

        tvTenXe.setText(xeMay.getTen_xe());
        tvMauSac.setText(xeMay.getMau_sac());
        tvGiaBan.setText(xeMay.getGia_ban());
        tvMoTa.setText(xeMay.getMo_ta());
        String newUrl = xeMay.getHinh_anh().replace("localhost", "192.168.0.100");
        Glide.with(context).
                load(newUrl).
                into(imgHinh);


    }


    public interface XeMayClick {
        void delete(XeMay xeMay);
        void edit(XeMay xeMay);

        void showDetail(XeMay xeMay);

    }
    class XeMayHolder extends RecyclerView.ViewHolder {
        ImageView imgHinh;
        TextView tvTenXe,tvMauSac,tvGiaBan,tvMoTa;
        Button btnSua;
        public XeMayHolder(@NonNull View itemView) {
            super(itemView);
            imgHinh = itemView.findViewById(R.id.ivHinh);
            tvTenXe = itemView.findViewById(R.id.tvTenXe);
            tvMauSac = itemView.findViewById(R.id.tvMauSac);
            tvGiaBan = itemView.findViewById(R.id.tvGiaBan);
            tvMoTa = itemView.findViewById(R.id.tvMoTa);
            btnSua = itemView.findViewById(R.id.btnSua);
        }
    }
}
