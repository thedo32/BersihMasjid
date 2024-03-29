package com.app.ikafeunand;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.ikafeunand.dashboard.UpdateDiary;
import com.app.ikafeunand.databinding.AdapterDiaryBinding;
import com.app.ikafeunand.databinding.DeleteDiaryBinding;
import com.app.ikafeunand.databinding.EditDiaryBinding;
import com.app.ikafeunand.map.MapActivity;
import com.app.ikafeunand.model.ModelDiary;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AdapterDiary extends RecyclerView.Adapter<AdapterDiary.AdapterHolder> {

    Context context;
    ArrayList<ModelDiary> arrayList;
    AdapterDiaryBinding binding;


    FirebaseAuth auth;

    DatabaseReference referencedata;
    SharedPreferences preferences;

    String keyId ="";
    String date;
    UpdateDiary diary;
    SharedPreferences.Editor editor;
    String email;

    String uniqueauth;
    String uniques;

    String keys;



    public AdapterDiary(Context context, ArrayList<ModelDiary> arrayList){
        this.context = context;
        this.arrayList = arrayList;
        diary = (UpdateDiary) context;
    }

    @NonNull
    @Override
    public AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new AdapterHolder(AdapterDiaryBinding.inflate(layoutInflater));

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHolder holder, @SuppressLint("RecyclerView") int position) {
        ModelDiary modelDiary = arrayList.get(position);
        auth = FirebaseAuth.getInstance();
        preferences = context.getSharedPreferences("uisumbar",MODE_PRIVATE);
        editor = preferences.edit();
        FirebaseUser user = auth.getCurrentUser();
        uniqueauth = auth.getUid();
        email = user.getEmail();
        uniques = preferences.getString("unique", "");


        String title = modelDiary.getTitle();
        String description = modelDiary.getDescription();
        String date = modelDiary.getDate();
        String unique = modelDiary.getUserid();
        String Latd = modelDiary.getLat();
        String Lond = modelDiary.getLon();
        String update = modelDiary.getUpdater();

        holder.binding.title.setText(title);
        holder.binding.description.setText(description);
        holder.binding.date.setText(date);
        holder.binding.update.setText(update);

        holder.binding.calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendarEvent = Calendar.getInstance();
                String Latd = modelDiary.getLat();
                String Lond = modelDiary.getLon();
                String Desc = modelDiary.getDescription();
                String tmStart;
                String tmEnd;
                long start;
                long end;
                tmStart =  modelDiary.getStarttm();
                tmEnd = modelDiary.getEndtm();

                if (tmStart.equals("")) {
                    start = calendarEvent.getTimeInMillis();
                }else{
                    start = Time.parse(tmStart);
                }

                if (tmEnd.equals("")) {
                    end = calendarEvent.getTimeInMillis() + (60 * 60 * 1000);
                }else{
                    end = Time.parse(tmEnd);
                }


                Intent i = new Intent(Intent.ACTION_EDIT);
                i.setType("vnd.android.cursor.item/event");
                i.putExtra("beginTime", start);
                i.putExtra("allDay", false);
                i.putExtra("rule", "FREQ=YEARLY");
                i.putExtra("endTime", end);
                i.putExtra("description", Desc);
                i.putExtra("title", holder.binding.title.getText().toString());
                i.putExtra("eventLocation", Latd +","+Lond );
                context.startActivity(i);
            }
        });



        holder.binding.maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putString("unique",unique);

                if (description.length() >=60) {
                     editor.putString("desc", description.substring(0, 60) + "...");
                }else {
                    editor.putString("desc", description);
                }

                editor.putString("Latd",Latd);
                editor.putString("Lond",Lond);
                editor.apply();
                editor.commit();
                context.startActivity(new Intent(context, MapActivity.class));

            }

        });

        holder.binding.delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


                ModelDiary modelDiary2 = arrayList.get(position);
                LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);
                DeleteDiaryBinding binding2 = DeleteDiaryBinding.inflate(layoutInflater);
                deleteDiary(modelDiary2, binding2);
            }
        });

        holder.binding.edit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){


               /* String uniquess = referencedata.push().getKey();
                Log.d("TESSSST",uniquess +" " + unique + " " + uniqueauth + "  "+ uniques );*/
                ModelDiary modelDiary1 = arrayList.get(position);
                LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);
                EditDiaryBinding binding1 = EditDiaryBinding.inflate(layoutInflater);
                editDiary(modelDiary1,binding1);

            }
        });
        holder.binding.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int intdesc;
                intdesc = holder.binding.description.getVisibility();
                if (intdesc== 0x00000008){
                    holder.binding.description.setVisibility(View.VISIBLE);
                    holder.binding.calendar.setVisibility(View.VISIBLE);
                }else{
                    holder.binding.description.setVisibility(View.GONE);
                    holder.binding.calendar.setVisibility(View.GONE);
                }
            }
        });
    }



    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    public static class AdapterHolder extends RecyclerView.ViewHolder{
        AdapterDiaryBinding binding;

        public AdapterHolder(@NonNull AdapterDiaryBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    private void editDiary(ModelDiary md, EditDiaryBinding editBinding){
        Dialog dialog = new Dialog(context);
        View view = editBinding.getRoot();
        dialog.setContentView(view);
        dialog.show();

        editBinding.title.setText(md.getTitle());
        editBinding.description.setText(md.getDescription());
        editBinding.lat.setText(md.getLat());
        editBinding.lon.setText(md.getLon());
        editBinding.tmstart.setText(md.getStarttm());
        editBinding.tmend.setText(md.getEndtm());
        keyId = md.getUserid();

        if (!uniqueauth.equals("XDb6D7GO3zYOlTYkVbWI0aLvXKD2")) {
            editBinding.lat.setVisibility(View.GONE);
            editBinding.lon.setVisibility(View.GONE);
            editBinding.tmend.setVisibility(View.GONE);
            editBinding.tmstart.setVisibility(View.GONE);
        }

        referencedata = FirebaseDatabase.getInstance().getReference( "DataDiary").child(uniqueauth);

        referencedata.addListenerForSingleValueEvent(new ValueEventListener() {  //ambil data dari firebase

            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    keys   = ds.getKey();
                    if (keys.equals(keyId)){
                        editBinding.edit.setEnabled(true);
                        editBinding.label.setText("ANDA AKAN EDIT DATA");
                    }


        }
    }
    @Override
    public void onCancelled(@NonNull DatabaseError error) {
    }
});

        editBinding.edit.setOnClickListener(new View.OnClickListener() {
@Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();

                date = dateFormat.format(calendar.getTime());

                String title = editBinding.title.getText().toString();
                String description = editBinding.description.getText().toString();
                String lat = editBinding.lat.getText().toString();
                String lon = editBinding.lon.getText().toString();
                String starttm = editBinding.tmstart.getText().toString();
                String endtm = editBinding.tmend.getText().toString();
                String updater = "ADMIN Update:";

                if (!uniqueauth.equals("XDb6D7GO3zYOlTYkVbWI0aLvXKD2")) {
                    updater = "Update Tgl:";
                }

                ModelDiary mdEdit = new ModelDiary(title, description, date, keyId,
                        lat,lon,starttm, endtm, updater);
                diary.UpdateDiary(mdEdit);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("editBerhasil","edit confirmed");
                        dialog.dismiss();

                    }

                },360);
            }
        });

    }


    private void deleteDiary(ModelDiary mdelete, DeleteDiaryBinding deleteBinding){
        Dialog dialog = new Dialog(context);
        View view = deleteBinding.getRoot();
        dialog.setContentView(view);
        dialog.show();

        deleteBinding.title.setText(mdelete.getTitle());
        deleteBinding.description.setText(mdelete.getDescription());
        keyId = mdelete.getUserid();
        referencedata = FirebaseDatabase.getInstance().getReference( "DataDiary").child(uniqueauth);

        referencedata.addListenerForSingleValueEvent(new ValueEventListener() {  //ambil data dari firebase

            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    keys   = ds.getKey();
                    if (keys.equals(keyId)){
                        deleteBinding.delete.setEnabled(true);
                        deleteBinding.label.setText("ANDA AKAN HAPUS DATA");
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        deleteBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();

                date = dateFormat.format(calendar.getTime());

                String title = deleteBinding.title.getText().toString();
                String description = deleteBinding.description.getText().toString();
                String lat = "-0.891741";
                String lon = "100.354692";
                String starttm = "";
                String endtm = "";
                String updater = "ADMIN Update:";

                if (!uniqueauth.equals("XDb6D7GO3zYOlTYkVbWI0aLvXKD2")) {
                    updater = "Update Tgl:";
                }

                ModelDiary mDel = new ModelDiary(title, description, date, keyId,
                        lat,lon,starttm, endtm, updater);
                diary.DeleteDiary(mDel);


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
