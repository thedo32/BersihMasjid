package com.app.bersihmasjid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.app.bersihmasjid.databinding.ActivitySignupBinding;
import com.app.bersihmasjid.model.UserDiary;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    DatabaseReference reference;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String mobile;


    /*    @Override
        public void onStart() {
            super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = auth.getCurrentUser();
            if(currentUser != null){
                currentUser.reload();
            }
        }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        FirebaseApp.initializeApp(SignUpActivity.this);
        View view = binding.getRoot();
        setContentView(view);
        dialog = new ProgressDialog( SignUpActivity.this);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference( "UserDiary");
        preferences = getSharedPreferences( "uisumbar",MODE_PRIVATE);
        editor = preferences.edit();

     /*   binding.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.title.setVisibility(View.GONE);*/

        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class ));

            }
        });


        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.name.getText().toString();
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                mobile = binding.mobile.getText().toString();

                storeDatatoFirebase(name, email, password);

            }
        });
    }

    private void storeDatatoFirebase(String name, String email, String password){
        dialog.setMessage("Tunggu Sebentar...");
        dialog.show();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
//                    Log.d("sukses", "onComplete: Data Terkirim");
                    FirebaseUser user = auth.getCurrentUser();
                    String unique = user.getUid();

                    UserDiary diary = new UserDiary(name,email,mobile);

                    reference.child("user").child(unique).setValue(diary).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                editor.putString("unique",unique);
                                editor.commit();
                                Toast.makeText(SignUpActivity.this, "Pendaftaran Sukses",
                                        Toast.LENGTH_SHORT).show();

                                finish();
                            }else{
                                Toast.makeText(SignUpActivity.this, "Mengirim ke Database Gagal",
                                        Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }

                    });

                 }else{
                    Toast.makeText(SignUpActivity.this, "Pendaftaran Gagal",
                            Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

    }
}