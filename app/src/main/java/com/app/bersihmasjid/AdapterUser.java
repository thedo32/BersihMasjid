package com.app.bersihmasjid;

import static android.R.layout.simple_spinner_dropdown_item;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class AdapterUser extends AppCompatActivity {
    EditUserBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    DatabaseReference referenceall;

    UpdateUser users;
    FirebaseAuth auth;
    String uniqueId;
    String unique;
    String mobile;
    String email;
    String point="0";
    String addpoint="0";
    String account;
    String description;
    String url;

  /*  String[] namess = {
            "Karin",
            "Ingrid", "Helga",
            "Renate",
            "Novi",
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
        uniqueId = auth.getUid();
        View view = binding.getRoot();
        setContentView(view);
        preferences = getSharedPreferences( "uisumbar",MODE_PRIVATE);
        editor = preferences.edit();
        unique = preferences.getString("unique", "");
        //reference = FirebaseDatabase.getInstance().getReference("UserDiary").child(uniqueId);
        referenceall = FirebaseDatabase.getInstance().getReference("UserDiary");
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(AdapterUser.this,simple_spinner_dropdown_item, namess);
        dialog = new ProgressDialog( AdapterUser.this);
        url = "https://uisumbar.xyztem.xyz/2023/04/18/bersih-masjid-user-admin/";

        //tambahkan nama di spinner
        reloadname();

        //tambahkan nilai di textboxes
        binding.name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              /*  binding.point.setText("1");
                binding.account.setText("mandiri");
                binding.description.setText("keterangan");*/
                String name = binding.name.getSelectedItem().toString();


                referenceall.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot dss : snapshot.getChildren()) {
                            String keyss = dss.getKey();

                            assert keyss != null;
                            referenceall.child(keyss).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dsss : snapshot.getChildren()) {
                                        String keysss = dsss.getKey();

                                        assert keysss != null;
                                        if (keysss.equals("name")) {
                                            String namesss = Objects.requireNonNull(dsss.getValue()).toString();

                                            if (namesss.equals(name)) {

                                                binding.keys.setText(keyss);

                                                String keyss = binding.keys.getText().toString();

                                                referenceall.child(keyss).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot dssss : snapshot.getChildren()) {
                                                            String keysss = dssss.getKey();


                                                            if (Objects.equals(keysss, "mobile")) {
                                                                binding.mobile.setText(Objects.requireNonNull(dssss.getValue()).toString());
                                                            }
                                                            if (Objects.equals(keysss, "email")) {
                                                                binding.email.setText(Objects.requireNonNull(dssss.getValue()).toString());
                                                            }
                                                            if (Objects.equals(keysss, "point")) {
                                                                binding.point.setText(dssss.getValue().toString());
                                                            }
                                                            if (Objects.equals(keysss, "addpoint")) {
                                                                binding.addpoint.setText(dssss.getValue().toString());
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
                Log.e("ERRROR SPINNER","DATABASE EROOR" );
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
                String pointsss = binding.addpoint.getText().toString();

                int pointss = Integer.parseInt(points ) + Integer.parseInt( pointsss);
                point = String.valueOf(pointss);
                addpoint ="0";

                UserDiary uEdit = new UserDiary(name, email, mobile, point, addpoint, account, description);

                referenceall.child(keyss).setValue(uEdit).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            Toast.makeText(AdapterUser.this, "Pengeditan Sukses",
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

                String name = binding.name.getSelectedItem().toString();
                String keyss =  binding.keys.getText().toString();
                String email = binding.email.getText().toString();
                String description = binding.description.getText().toString();
                String account = binding.account.getText().toString();
                String mobile = binding.mobile.getText().toString();
                String point = binding.point.getText().toString();

                //users.DeleteUser(DelUser);

                referenceall.child(keyss).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdapterUser.this, "Data sudah dihapus",
                                    Toast.LENGTH_SHORT).show();


                        }else {
                            Toast.makeText(AdapterUser.this, "Data gagal dihapus",
                                    Toast.LENGTH_SHORT).show();

                        }
                        clearname();
                        reloadname();
                        dialog.dismiss();
                    }
                });

            }
        });

        binding.web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }
private void reloadname(){
    referenceall.addListenerForSingleValueEvent(new ValueEventListener() {  //ambil data dari firebase

        @SuppressLint("ResourceType")
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            for (DataSnapshot ds : snapshot.getChildren()) {
                String keys   = ds.getKey();

                assert keys != null;
                referenceall.child(keys).addListenerForSingleValueEvent(new ValueEventListener() {  //ambil data dari firebase

                    @SuppressLint("ResourceType")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            String key = ds.getKey();
                            assert key != null;
                            if (key.equals("name")) {

                                namess.add(Objects.requireNonNull(ds.getValue()).toString());
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
}
private void clearname(){
    namess.clear();
    ArrayAdapter<String> adapter = new ArrayAdapter<>(AdapterUser.this, simple_spinner_dropdown_item, namess);
    binding.name.setAdapter((adapter));
}
}