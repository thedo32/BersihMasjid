package com.app.ikafeunand;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.ikafeunand.dashboard.Dashboard;
import com.app.ikafeunand.databinding.ActivitySignupBinding;
import com.app.ikafeunand.model.ModelDiary;
import com.app.ikafeunand.model.UserDiary;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    DatabaseReference reference;

    // TODO snackbar belum lengkap - CoordinatorLayout coordiLayout;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    int i;
    String apoint = "IKF-000001";
    String gmobile;
    String djurusan;
    String eangkatan;
    String cgender;
    String jalamat;
    String fpekerjaan;



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
        preferences = getSharedPreferences( "ikafeunand",MODE_PRIVATE);
        editor = preferences.edit();

        // TODO snakbar belum lengkap coordiLayout = (CoordinatorLayout)binding;

        reference.addListenerForSingleValueEvent(new ValueEventListener() {  //ambil data dari firebase utk nomor registrasi otomatis
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    i = ++i;
                }

                String stri = String.valueOf(i);
                if (stri.length()==1) {
                    apoint = "IKF-00000" + stri;
                }else if (stri.length()==2) {
                    apoint = "IKF-0000" + stri;
                }else if (stri.length()==3) {
                    apoint = "IKF-000" + stri;
                }
                else if (stri.length()==4) {
                    apoint = "IKF-00" + stri;
                }
                else if (stri.length()==5) {
                    apoint = "IKF-0" + stri;
                }
                else if (stri.length()==6) {
                    apoint = "IKF-" + stri;
                }

                Log.d("POIIINT", apoint);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //binding.noItem.setVisibility(View.VISIBLE);
            }
        });




        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class ));

            }
        });


        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bname = binding.name.getText().toString();
                String hemail = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                gmobile = binding.mobile.getText().toString();
                djurusan = binding.jurusan.getSelectedItem().toString();
                eangkatan = binding.angkatan.getSelectedItem().toString();
                cgender = binding.gender.getSelectedItem().toString();
                jalamat = binding.alamat.getText().toString();
                fpekerjaan = binding.pekerjaan.getText().toString();


                if(password.length()>=8)
                {
                    Pattern letter = Pattern.compile("[a-zA-z]");
                    Pattern digit = Pattern.compile("[0-9]");
                    Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
                    //Pattern eight = Pattern.compile (".{8}");


                    Matcher hasLetter = letter.matcher(password);
                    Matcher hasDigit = digit.matcher(password);
                    Matcher hasSpecial = special.matcher(password);

                    if ((hasLetter.find() && hasDigit.find() && hasSpecial.find())) {
                        storeDatatoFirebase(bname, hemail, password);
                    }else{

                        /*Snackbar snackbar = Snackbar
                                .make( TODO snakbar belum lengkap,"Password tidak boleh kurang dari 8 Karater, " +
                                        "\n"+ "harus ada karakter huruf kecil besar, angka," +
                                        " \n"+ "dan special seperti !@#$%&*()_+=|<>?{}[]~- ",Snackbar.LENGTH_SHORT);
                        snackbar.show();*/

                        Toast.makeText(SignUpActivity.this, "Password tidak boleh kurang dari 8 Karater dan...",
                                Toast.LENGTH_LONG).show();
                        Toast.makeText(SignUpActivity.this, "harus ada kombinasi huruf kecil besar, angka dan...",
                                Toast.LENGTH_LONG).show();
                        Toast.makeText(SignUpActivity.this, "special seperti !@#$%&*()_+=|<>?{}[]~- ",
                                Toast.LENGTH_LONG).show();

                    }

                }else {
                    Toast.makeText(SignUpActivity.this, "Password tidak boleh kurang dari 8 Karater",
                            Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    private void storeDatatoFirebase(String bname, String hemail, String password){
        dialog.setMessage("Tunggu Sebentar...");
        dialog.show();
        auth.createUserWithEmailAndPassword(hemail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
//                    Log.d("sukses", "onComplete: Data Terkirim");
                    FirebaseUser user = auth.getCurrentUser();
                    String unique = user.getUid();

                    UserDiary diary = new UserDiary(bname,hemail,gmobile, apoint,djurusan,
                            eangkatan, cgender, jalamat, fpekerjaan);

                    reference.child(unique).setValue(diary).addOnCompleteListener(new OnCompleteListener<Void>() {
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