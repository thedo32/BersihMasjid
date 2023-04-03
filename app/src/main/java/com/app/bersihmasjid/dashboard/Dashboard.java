package com.app.bersihmasjid.dashboard;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.bersihmasjid.AdapterDiary;
import com.app.bersihmasjid.SignInActivity;
import com.app.bersihmasjid.databinding.ActivityDashboardBinding;
import com.app.bersihmasjid.databinding.AdapterDiaryBinding;
import com.app.bersihmasjid.model.ModelDiary;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//implements untk memnaggil interface
public class Dashboard extends AppCompatActivity implements UpdateDiary {

    ActivityDashboardBinding binding;

    AdapterDiaryBinding adapterDiaryBinding;
    ArrayList<ModelDiary> diaries;
    DatabaseReference reference;

    DatabaseReference referenceusr;
    SharedPreferences preferences;
    FirebaseAuth auth;
    SharedPreferences.Editor editor;
    LinearLayoutManager linearLayoutManager;
    AdapterDiary adapterDiary;
    ModelDiary modelDiary;
    ProgressDialog dialog;
    String unique;
    String uniqueId;
    String all;
    String email;



      @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        adapterDiaryBinding = AdapterDiaryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();
        preferences = getSharedPreferences( "uisumbar",MODE_PRIVATE);
        unique = preferences.getString("unique","");
        //email = preferences.getString("email","");
        //email = preferences.getString("email", "");
        all = "XDb6D7GO3zYOlTYkVbWI0aLvXKD2";

        referenceusr = FirebaseDatabase.getInstance
                  ("https://bersihmasjid-60595-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference
                  ( "UserDiary");
        reference = FirebaseDatabase.getInstance
                ("https://bersihmasjid-60595-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference
                ( "DataDiary").child(all);
        diaries = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(Dashboard.this);
        dialog = new ProgressDialog( Dashboard.this);

        binding.rvDiary.setLayoutManager(linearLayoutManager);



          binding.add.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  startActivity(new Intent(Dashboard.this,AddDiary.class ));
              }
          });



        showDiary(); //membuka diary

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Dashboard.this, SignInActivity.class ));
                finish();
            }
        });
    }
    private void showDiary(){
        dialog.setMessage("Tunggu Sebentar...");
        dialog.show();
        diaries.clear(); //bersihkan dahulu data diary biar tidak bertumpuk

        //get name for dashboard
        referenceusr.child(unique).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    binding.username.setText(String.valueOf(task.getResult().getValue()));
                }
                else {
                    Log.e("firebase", "Data Gagal Didapatkan", task.getException());
                }
            }
        });

        //get Auth user email
        FirebaseUser user = auth.getCurrentUser();
        email  = user.getEmail();



        reference.addListenerForSingleValueEvent(new ValueEventListener() {  //ambil data dari firebase

            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot ds : snapshot.getChildren()){
                modelDiary = ds.getValue(ModelDiary.class);
                diaries.add(modelDiary);
                }

                adapterDiary = new AdapterDiary( Dashboard.this, diaries);
                binding.rvDiary.setAdapter(adapterDiary);
                binding.noItem.setVisibility(View.GONE);

                if (email.equals("jeffriargon@gmail.com")){
                    binding.add.setEnabled(true);
                }

                dialog.dismiss();
            };

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            binding.noItem.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void UpdateDiary(ModelDiary diary) {
        uniqueId = preferences.getString("",diary.getUserid());
        reference.child(uniqueId).setValue(diary).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Dashboard.this, "Data telah diupdate",
                            Toast.LENGTH_SHORT).show();


                }else {
                    Toast.makeText(Dashboard.this, "Data gagal diupdate",
                            Toast.LENGTH_SHORT).show();

                }
                dialog.dismiss();
                showDiary();
            }
        });
    }

    @Override
    public void DeleteDiary(ModelDiary diary) {
        uniqueId = preferences.getString("",diary.getUserid());
        reference.child(uniqueId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Dashboard.this, "Data sudah dihapus",
                            Toast.LENGTH_SHORT).show();


                }else {
                    Toast.makeText(Dashboard.this, "Data gagal dihapus",
                            Toast.LENGTH_SHORT).show();

                }
                dialog.dismiss();
                showDiary();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showDiary();
    }
}
