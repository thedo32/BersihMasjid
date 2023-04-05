package com.app.bersihmasjid.dashboard;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.bersihmasjid.databinding.AddDiaryBinding;
import com.app.bersihmasjid.model.ModelDiary;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddDiary extends AppCompatActivity {
    AddDiaryBinding binding;
    SharedPreferences preferences;
    DatabaseReference reference;
    String uniqueId;
    String keyUnique;

    ProgressDialog dialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddDiaryBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        preferences = getSharedPreferences( "uisumbar",MODE_PRIVATE);
        uniqueId = preferences.getString("unique", "");
        reference = FirebaseDatabase.getInstance().getReference("DataDiary").child("data").child(uniqueId);

        dialog = new ProgressDialog( AddDiary.this);


        binding.add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String title = binding.title.getText().toString();
                String description = binding.description.getText().toString();
                String lat = binding.lat.getText().toString();
                String lon = binding.lon.getText().toString();

                SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                String fixDate = dateFormat.format(calendar.getTime());
                String updater = "Admin Update:";

                if (!uniqueId.equals("XDb6D7GO3zYOlTYkVbWI0aLvXKD2")) {
                    updater = "Update Tgl:";
                }

                dialog.setMessage("Tunggu Sebentar...");
                dialog.show();
                keyUnique = reference.push().getKey();

                ModelDiary modelDiary = new ModelDiary(title, description, fixDate, keyUnique,lat, lon, updater);
                reference.child(keyUnique).setValue(modelDiary).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddDiary.this, "Diary Added",
                                    Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(AddDiary.this, Dashboard.class));
                           finish();
                        }else {
                            Toast.makeText(AddDiary.this, "Fail to Added",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                    dialog.dismiss();
                // storeDatatoFirebase(name, email, password);
            }
        });
    }
}
