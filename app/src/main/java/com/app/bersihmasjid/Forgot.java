package com.app.bersihmasjid;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.bersihmasjid.databinding.ForgotPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot extends AppCompatActivity {

 ForgotPasswordBinding binding;
 FirebaseAuth auth;

 @Override
 protected void onCreate(@Nullable Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);

  binding = ForgotPasswordBinding.inflate(getLayoutInflater());
  View view = binding.getRoot();
  setContentView(view);

  auth = FirebaseAuth.getInstance();

  binding.reset.setOnClickListener(new View.OnClickListener() {
   @Override
   public void onClick(View view) {
     String email = binding.email.getText().toString();
     resetPassword(email);
   }
  });
 }
 private void resetPassword(String email){
  auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
   @Override
   public void onComplete(@NonNull Task<Void> task) {
    if (task.isSuccessful()){

     Toast.makeText(Forgot.this, "Reset Email Sent",
             Toast.LENGTH_SHORT).show();

     finish();
    }else{
     Toast.makeText(Forgot.this, "Unsent, Unknown Address",
             Toast.LENGTH_SHORT).show();
    }
   }
  });

 }
}
