package com.app.ikafeunand;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.ikafeunand.dashboard.Dashboard;
import com.app.ikafeunand.dashboard.DashboardAdmin;
import com.app.ikafeunand.databinding.ActivitySigninBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    ActivitySigninBinding binding;
    FirebaseAuth auth;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();


        preferences = getSharedPreferences( "uisumbar",MODE_PRIVATE);
        editor = preferences.edit();

        if (ContextCompat.checkSelfPermission(SignInActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignInActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(SignInActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions(SignInActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }





        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String hemail = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                storeLoginUser(hemail, password);

            }
        });

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(SignInActivity.this,SignUpActivity.class ));

            }
        });

        binding.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(SignInActivity.this, Forgot.class));
            }
        });
    }

    private void storeLoginUser(String hemail, String password){

        auth.signInWithEmailAndPassword(hemail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();
                    String unique = user.getUid();
                    String hemail  = user.getEmail();
                    editor.putString("unique", unique);
                    editor.putString("email", hemail);
                    //editor.putBoolean("autologin",true);
                    editor.apply();
                    editor.commit();


                    if (hemail.equals("jeffriargon@gmail.com")) {
                        startActivity(new Intent(SignInActivity.this, DashboardAdmin.class));
                    }else{
                        startActivity(new Intent(SignInActivity.this, Dashboard.class));
                    }

                    Toast.makeText(SignInActivity.this, "Login Sukses",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(SignInActivity.this, "Login Gagal",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(SignInActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Izin Lokasi Diberikan", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Izin Lokasi Tidak Diberikan", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

}
