package com.app.bersihmasjid;

import static android.R.layout.simple_spinner_dropdown_item;

import android.annotation.SuppressLint;
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

import com.app.bersihmasjid.databinding.EditUserBinding;
import com.app.bersihmasjid.model.UserDiary;
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

import java.util.ArrayList;
import java.util.Collections;

//TODO Adapter User belum selesai

public class AdapterUser extends AppCompatActivity {
    EditUserBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
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
        editor = preferences.edit();
        unique = preferences.getString("unique", "");
        //reference = FirebaseDatabase.getInstance().getReference("UserDiary").child("user").child(uniqueId);
        referenceall = FirebaseDatabase.getInstance().getReference("UserDiary").child("user");
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(AdapterUser.this,simple_spinner_dropdown_item, namess);
        dialog = new ProgressDialog( AdapterUser.this);
        binding.point.setText("1"); //menambah point 1

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

            //TODO Adapter User edit belum selesai
        binding.name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.point.setText("1");
                binding.account.setText("mandiri");
                binding.description.setText("keterangan");
                String name = binding.name.getSelectedItem().toString();


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
                                                Log.d ("NAMESSS1", namesss);
                                                Log.d ("NAMESSS2", name);
                                                binding.keys.setText(keyss);
                                                Log.d ("UNIQUESSSSS1", unique);
                                                Log.d ("UNIQUESSSSS2", keyss);
                                                String keyss = binding.keys.getText().toString();
                                                Log.d("UNIQUESSSS3", keyss);
                                                referenceall.child(keyss).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot dssss : snapshot.getChildren()) {
                                                            String keysss = dssss.getKey();
                                                            Log.d("KEYSSs",keysss);

                                                            if (keysss.equals("mobile")) {
                                                                binding.mobile.setText(dssss.getValue().toString());
                                                            }
                                                            if (keysss.equals("email")) {
                                                                binding.email.setText(dssss.getValue().toString());
                                                            }
                                                            if (keysss.equals("point")) {
                                                                binding.point.setText(dssss.getValue().toString());
                                                            }
                                                            if (keysss.equals("account")) {
                                                                binding.account.setText(dssss.getValue().toString());
                                                            }
                                                            if (keysss.equals("description")) {
                                                                binding.description.setText(dssss.getValue().toString());
                                                            }
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                binding.point.setText("1");
            }
        });



        binding.edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {



                String name = binding.name.getSelectedItem().toString();
                String keyss =  binding.keys.getText().toString();
                String email = binding.email.getText().toString();
                String description = binding.description.getText().toString();
                String account = binding.account.getText().toString();
                String mobile = binding.mobile.getText().toString();
                String points = binding.point.getText().toString();

                int pointss = Integer.parseInt(points)+1;
                point = String.valueOf(pointss);


                Log.d("SELECTED NAME",name);

                UserDiary uEdit = new UserDiary(name, email, mobile, point, account, description);
                //Log.d("UNIQUE",unique);
                referenceall.child(keyss).setValue(uEdit).addOnCompleteListener(new OnCompleteListener<Void>() {

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
