package com.app.bersihmasjid;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.bersihmasjid.dashboard.UpdateDiary;
import com.app.bersihmasjid.databinding.ActivityDashboardBinding;
import com.app.bersihmasjid.databinding.AdapterDiaryBinding;
import com.app.bersihmasjid.databinding.DeleteDiaryBinding;
import com.app.bersihmasjid.databinding.EditDiaryBinding;
import com.app.bersihmasjid.databinding.ActivityMapBinding;
import com.app.bersihmasjid.model.ModelDiary;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AdapterDiary extends RecyclerView.Adapter<AdapterDiary.AdapterHolder> {

    Context context;
    ArrayList<ModelDiary> arrayList;
    AdapterDiaryBinding binding;
    ActivityMapBinding mapBinding;

    FirebaseAuth auth;

    DatabaseReference referencedata;
    SharedPreferences preferences;

    String keyId ="";
    String date;
    UpdateDiary diary;
    SharedPreferences.Editor editor;
    String email;

    String uniqueauth;
    String uniques;



    public AdapterDiary(Context context, ArrayList<ModelDiary> arrayList){
        this.context = context;
        this.arrayList = arrayList;
        diary = (UpdateDiary) context;
    }

    @NonNull
    @Override
    public AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new AdapterHolder(AdapterDiaryBinding.inflate(layoutInflater));

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHolder holder, @SuppressLint("RecyclerView") int position) {
        ModelDiary modelDiary = arrayList.get(position);
        auth = FirebaseAuth.getInstance();
        preferences = context.getSharedPreferences("uisumbar",MODE_PRIVATE);
        editor = preferences.edit();
        FirebaseUser user = auth.getCurrentUser();
        uniqueauth = auth.getUid();
        email = user.getEmail();
        uniques = preferences.getString("unique", "");




        String title = modelDiary.getTitle();
        String description = modelDiary.getDescription();
        String date = modelDiary.getDate();
        String unique = modelDiary.getUserid();
        String Latd = modelDiary.getLat();
        String Lond = modelDiary.getLon();

        holder.binding.title.setText(title);
        holder.binding.description.setText(description);
        holder.binding.date.setText("Update: " +date);

        holder.binding.maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putString("unique",unique);
                editor.putString("Latd",Latd);
                editor.putString("Lond",Lond);
                if (description.length() >=100) {
                    editor.putString("desc", description.substring(0, 100) + "...");
                }else{
                    editor.putString("desc",description);
                }
                editor.apply();
                editor.commit();
                context.startActivity(new Intent(context, MapActivity.class));

            }

        });

        holder.binding.delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                ModelDiary modelDiary2 = arrayList.get(position);
                LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);
                DeleteDiaryBinding binding2 = DeleteDiaryBinding.inflate(layoutInflater);
                deleteDiary(modelDiary2, binding2);
            }
        });

        holder.binding.edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ModelDiary modelDiary1 = arrayList.get(position);
                LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);
                EditDiaryBinding binding1 = EditDiaryBinding.inflate(layoutInflater);
                editDiary(modelDiary1,binding1);
            }
        });
    }



    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public static class AdapterHolder extends RecyclerView.ViewHolder{
        AdapterDiaryBinding binding;

        public AdapterHolder(@NonNull AdapterDiaryBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }




    private void editDiary(ModelDiary md, EditDiaryBinding editBinding){
        Dialog dialog = new Dialog(context);
        View view = editBinding.getRoot();
        dialog.setContentView(view);
        dialog.show();

        editBinding.title.setText(md.getTitle());
        editBinding.description.setText(md.getDescription());
        editBinding.lat.setText(md.getLat());
        editBinding.lon.setText(md.getLon());
        keyId = md.getUserid();
        Log.d("DeleteBerhasil",keyId);
        Log.d("DeleteBerhasilll",uniques);

       if (!uniqueauth.equals(uniques)){
           editBinding.edit.setEnabled(false);
       }
        editBinding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat( "dd MMMM yyyy");
                Calendar calendar = Calendar.getInstance();

                date = dateFormat.format(calendar.getTime());

                String title = editBinding.title.getText().toString();
                String description = editBinding.description.getText().toString();
                String lat = editBinding.lat.getText().toString();
                String lon = editBinding.lon.getText().toString();

                ModelDiary mdEdit = new ModelDiary(title, description, date, keyId,lat,lon);
                diary.UpdateDiary(mdEdit);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("editBerhasil","edit confirmed");
                        dialog.dismiss();

                    }

                },360);
            }
        });
    }


    private void deleteDiary(ModelDiary mdelete, DeleteDiaryBinding deleteBinding){
        Dialog dialog = new Dialog(context);
        View view = deleteBinding.getRoot();
        dialog.setContentView(view);
        dialog.show();

        deleteBinding.title.setText(mdelete.getTitle());
        deleteBinding.description.setText(mdelete.getDescription());
        keyId = mdelete.getUserid();
        Log.d("DeleteBerhasil",keyId);

        if (!keyId.equals(uniques)){
            deleteBinding.delete.setEnabled(false);
        }
        deleteBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat( "dd MMMM yyyy");
                Calendar calendar = Calendar.getInstance();

                date = dateFormat.format(calendar.getTime());

                String title = deleteBinding.title.getText().toString();
                String description = deleteBinding.description.getText().toString();
                String lat = "-0.891741";
                String lon = "100.354692";
                ModelDiary mDel = new ModelDiary(title, description, date, keyId,lat,lon);
                diary.DeleteDiary(mDel);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("DeleteBerhasil","delete confirmed");
                        dialog.dismiss();

                    }

                },360);
            }
        });

    }
}
