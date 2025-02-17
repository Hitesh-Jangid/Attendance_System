package com.hiteshjangid.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hiteshjangid.attendance.databinding.ActivityInsertGradeBinding;

import java.util.HashMap;
import java.util.Objects;

public class Insert_Grade_Activity extends AppCompatActivity {

    private ActivityInsertGradeBinding binding;
    private final String position_bg = "1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertGradeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarInsertClass);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        binding.buttonCreateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValid()) {

                    final ProgressDialog progressDialog = new ProgressDialog(Insert_Grade_Activity.this);
                    progressDialog.setMessage("Creating grade..");
                    progressDialog.show();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Grade");
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id",binding.gradeCreateClass.getText().toString());
                    hashMap.put("name_grade",binding.gradeCreateClass.getText().toString());
                    hashMap.put("position_bg",position_bg);
                    reference.child(binding.gradeCreateClass.getText().toString()).setValue(hashMap);

                    progressDialog.dismiss();
                    Toast.makeText(Insert_Grade_Activity.this, "Successfully created", Toast.LENGTH_SHORT).show();
                    finish();

                }else{
                    Toast.makeText(Insert_Grade_Activity.this, "Fill all details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isValid(){
        return  !binding.gradeCreateClass.getText().toString().isEmpty();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}