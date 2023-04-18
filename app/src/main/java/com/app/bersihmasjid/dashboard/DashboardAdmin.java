package com.app.bersihmasjid.dashboard;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.bersihmasjid.AdapterDiary;
import com.app.bersihmasjid.AdapterUser;
import com.app.bersihmasjid.SignInActivity;
import com.app.bersihmasjid.databinding.ActivityDashboardAdminBinding;
import com.app.bersihmasjid.databinding.AdapterDiaryBinding;
import com.app.bersihmasjid.model.ModelDiary;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

//implements untk memnaggil interface
public class DashboardAdmin extends AppCompatActivity implements UpdateDiary {

    ActivityDashboardAdminBinding binding;

    AdapterDiaryBinding adapterDiaryBinding;
    ArrayList<ModelDiary> diaries;
    DatabaseReference referenceall;

    SharedPreferences preferences;
    FirebaseAuth auth;
    LinearLayoutManager linearLayoutManager;
    AdapterDiary adapterDiary;
    ModelDiary modelDiary;
    ProgressDialog dialog;
    String unique;
    String uniqueId;
    String uniqueauth;



      @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardAdminBinding.inflate(getLayoutInflater());
        adapterDiaryBinding = AdapterDiaryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();
        uniqueauth = auth.getUid();
        preferences = getSharedPreferences( "uisumbar",MODE_PRIVATE);
        unique = preferences.getString("unique","");
        referenceall = FirebaseDatabase.getInstance().getReference("DataDiary");
        diaries = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(DashboardAdmin.this);
        dialog = new ProgressDialog( DashboardAdmin.this);

        binding.rvDiary.setLayoutManager(linearLayoutManager);

          binding.useradmin.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                 startActivity(new Intent(DashboardAdmin.this, AdapterUser.class));

              }
          });

          binding.add.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                 startActivity(new Intent(DashboardAdmin.this, AddDiary.class));
              }
          });



        showDiary(); //membuka diary

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(DashboardAdmin.this, SignInActivity.class ));
                finish();
            }
        });
    }
    private void showDiary(){
        dialog.setMessage("Tunggu Sebentar...");
        dialog.show();
        diaries.clear(); //bersihkan dahulu data diary biar tidak bertumpuk



            referenceall.addListenerForSingleValueEvent(new ValueEventListener() {  //ambil data dari firebase

                @SuppressLint("ResourceType")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String keys   = ds.getKey();

                        referenceall.child(keys).orderByChild("date").addListenerForSingleValueEvent(new ValueEventListener() {  //ambil data dari firebase

                            @SuppressLint("ResourceType")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    modelDiary = ds.getValue(ModelDiary.class);
                                    diaries.add(modelDiary);
                                }

                                adapterDiary = new AdapterDiary(DashboardAdmin.this, diaries);
                                binding.rvDiary.setAdapter(adapterDiary);
                                binding.noItem.setVisibility(View.GONE);



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                binding.noItem.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }
             @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    binding.noItem.setVisibility(View.VISIBLE);
                }
            });


        dialog.dismiss();
    }

    @Override
    public void UpdateDiary(ModelDiary diary) {
        uniqueId = preferences.getString("",diary.getUserid());

        referenceall.child(uniqueauth).child(uniqueId).setValue(diary).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(DashboardAdmin.this, "Data telah diupdate",
                            Toast.LENGTH_SHORT).show();


                }else {
                    Toast.makeText(DashboardAdmin.this, "Data gagal diupdate",
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
        referenceall.child(uniqueauth).child(uniqueId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(DashboardAdmin.this, "Data sudah dihapus",
                            Toast.LENGTH_SHORT).show();


                }else {
                    Toast.makeText(DashboardAdmin.this, "Data gagal dihapus",
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
