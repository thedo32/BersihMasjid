package com.app.bersihmasjid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.bersihmasjid.dashboard.Dashboard;
import com.app.bersihmasjid.databinding.ActivitySigninBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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

        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                storeLoginUser(email, password);

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

    private void storeLoginUser(String email, String password){

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();
                    String unique = user.getUid();
                    String email  = user.getEmail();
                    editor.putString("unique",unique);
                    editor.putString("email",email);
                    //editor.putBoolean("autologin",true);
                    editor.apply();
                    editor.commit();
                    startActivity(new Intent(SignInActivity.this, Dashboard.class));
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
}
