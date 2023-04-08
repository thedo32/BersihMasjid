package com.app.bersihmasjid;

import static android.R.layout.simple_spinner_dropdown_item;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.bersihmasjid.databinding.EditDiaryBinding;
import com.app.bersihmasjid.databinding.EditUserBinding;
import com.app.bersihmasjid.model.ModelDiary;
import com.app.bersihmasjid.model.UserDiary;
import com.app.bersihmasjid.model.UserEdit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

//TODO Adapter User belum selesai

public class AdapterUser extends AppCompatActivity {
    EditUserBinding binding;
    SharedPreferences preferences;
    DatabaseReference reference;

    DatabaseReference referenceall;

    UpdateUser users;
    FirebaseAuth auth;
    String uniqueId;
    String unique;
    String mobile;
    String email;
    String point;
    String account;
    String description;
    String points;

  /*  String[] namess = {
            "Karin",
            "Ingrid", "Helga",
            "Renate",
            "Elke",
            "Ursula",
            "Erika",
            "Christa",
            "Gisela",
            "Monika"
    };*/

    ArrayList<String> namess = new ArrayList<String>();

    ProgressDialog dialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = EditUserBinding.inflate(getLayoutInflater());
        FirebaseApp.initializeApp(AdapterUser.this);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        uniqueId = auth.getUid();
        View view = binding.getRoot();
        setContentView(view);
        preferences = getSharedPreferences( "uisumbar",MODE_PRIVATE);
        unique = preferences.getString("unique", "");
        //reference = FirebaseDatabase.getInstance().getReference("UserDiary").child("user").child(uniqueId);
        referenceall = FirebaseDatabase.getInstance().getReference("UserDiary").child("user");
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(AdapterUser.this,simple_spinner_dropdown_item, namess);
        dialog = new ProgressDialog( AdapterUser.this);

        referenceall.addListenerForSingleValueEvent(new ValueEventListener() {  //ambil data dari firebase

            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String keys   = ds.getKey();

                    referenceall.child(keys).addListenerForSingleValueEvent(new ValueEventListener() {  //ambil data dari firebase

                        @SuppressLint("ResourceType")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {

                                String key = ds.getKey();

                                if (key.equals("name")) {
                                    //Log.d("NAMESSS", ds.getValue().toString());
                                    namess.add(ds.getValue().toString());
                                    Collections.sort(namess);

                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(AdapterUser.this, simple_spinner_dropdown_item, namess);
                                binding.name.setAdapter((adapter));



                                //ArrayAdapter<String> adapter = new ArrayAdapter<>(AdapterUser.this, simple_spinner_dropdown_item, namess);


                            }
                            //final ArrayAdapter<String> adapter = new ArrayAdapter<>(AdapterUser.this,simple_spinner_dropdown_item, names);

                           //binding.name.setAdapter((adapter));



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //TODO Adapter User onclick spinner name belum selesai
        binding.name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.point.setText("1");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                binding.point.setText("1");
            }
        });


         binding.edit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                referenceall.child(uniqueId).addListenerForSingleValueEvent(new ValueEventListener() {  //ambil data dari firebase

                    @SuppressLint("ResourceType")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            String key = ds.getKey();
                            Log.d("POINTSSS",key);

                            if (key.equals("point")) {
                                points = ds.getValue().toString();
                                Log.d("POINTSSS",points);
                                Log.d("POINT",points);

                                int pointss = Integer.parseInt(points) +1;
                                point = String.valueOf(pointss);

                                String name = binding.name.getSelectedItem().toString();

                                Log.d("NAME",name);
                                String email ="jeffriargon@gmail.com";
                                String mobile="08994659530";
                                String account = "Test";
                                String description = "Test";

                                referenceall.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        //TODO di isi dengan parent dari name - belum selesai
                                        for (DataSnapshot dss : snapshot.getChildren()) {
                                            String keyss = dss.getKey().toString();
                                            Log.d("KEYSS",keyss);
                                            referenceall.child(keyss).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot dsss : snapshot.getChildren()) {
                                                        String keysss = dsss.getKey();
                                                        Log.d("KEYSSs",keysss);
                                                        if (keysss.equals("name")) {
                                                            String namesss = dsss.getValue().toString();
                                                            if (namesss.equals(name)) {
                                                                //Log.d("NAMAUSER",name);
                                                                //Log.d("NAMESSS",namesss);
                                                                unique = keyss;
                                                                //Log.d("UNIQUESS",unique);
                                                                UserDiary uEdit = new UserDiary(name, email, mobile, point, account, description);
                                                                //Log.d("UNIQUE",unique);
                                                                referenceall.child(unique).setValue(uEdit).addOnCompleteListener(new OnCompleteListener<Void>() {

                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()){

                                                                            Toast.makeText(AdapterUser.this, "Pendaftaran Sukses",
                                                                                    Toast.LENGTH_SHORT).show();

                                                                            finish();
                                                                        }else{
                                                                            Toast.makeText(AdapterUser.this, "Mengirim ke Database Gagal",
                                                                                    Toast.LENGTH_SHORT).show();
                                                                        }
                                                                        dialog.dismiss();

                                                                    }

                                                                });


                                                                break;

                                                            }
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });




                            }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }



                });
                //int pointss = Integer.parseInt(points + 1);

                //point = String.valueOf(pointss);

                //users.UpdateUser(uEdit);


                // storeDatatoFirebase(name, email, password);
            }

        });

        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = binding.name.getAdapter().toString();
                String description = binding.description.getText().toString();

                UserDiary DelUser = new UserDiary(name, email, mobile, point, account, description);
                users.DeleteUser(DelUser);


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
