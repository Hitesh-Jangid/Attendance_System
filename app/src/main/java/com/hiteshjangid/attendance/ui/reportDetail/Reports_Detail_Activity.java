package com.hiteshjangid.attendance.ui.reportDetail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hiteshjangid.attendance.Adapter.Reports_Detail_NewAdapter;
import com.hiteshjangid.attendance.R;
import com.hiteshjangid.attendance.common.Common;
import com.hiteshjangid.attendance.databinding.ActivityReportsDetailBinding;

import java.util.Objects;

public class Reports_Detail_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    static final Integer WRITE_EXST = 0x3;
    static final Integer READ_EXST = 0x4;

    private ActivityReportsDetailBinding binding;

    Reports_Detail_NewAdapter reportsNewAdapter;

    private DatabaseReference attendanceReportsRef;
    private DatabaseReference classAttendanceRef;

    private String classname;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportsDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ReportsDetailViewModel reportsDetailViewModel = ViewModelProviders.of(this).get(ReportsDetailViewModel.class);

        String room_ID = getIntent().getStringExtra("ID");
        classname = getIntent().getStringExtra("class");
        String subjName = getIntent().getStringExtra("subject");
        date = getIntent().getStringExtra("date");

        setSupportActionBar(binding.toolbarReportsDetail);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recyclerView = binding.recyclerViewReportsDetail;
        binding.toolbarTitle.setText(date);
        binding.subjNameReportDetail.setText(subjName);
        binding.classnameReportDetail.setText(classname);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reportsDetailViewModel.getStudentList(classname, subjName, date).observe(this, studentList -> {
            reportsNewAdapter = new Reports_Detail_NewAdapter(Reports_Detail_Activity.this, studentList);
            recyclerView.setAdapter(reportsNewAdapter);
        });

        // Initialize the Firebase Realtime Database references
        attendanceReportsRef = FirebaseDatabase.getInstance().getReference("Attendance_Reports");
        classAttendanceRef = FirebaseDatabase.getInstance().getReference("Classes")
                .child(classname)
                .child("Attendance")
                .child(date);
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Reports_Detail_Activity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    Reports_Detail_Activity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case, I am just asking the permission again
                ActivityCompat.requestPermissions(Reports_Detail_Activity.this,
                        new String[]{permission}, requestCode);

            } else {
                ActivityCompat.requestPermissions(Reports_Detail_Activity.this,
                        new String[]{permission}, requestCode);
            }
        } else {
            // Permission already granted
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.only_dot, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.menu_delete) {
            deleteAttendance();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Attendance");
        builder.setMessage("Are you sure you want to delete the attendance records for this day?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAttendance();
            }
        });
        builder.setNegativeButton("No", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void deleteAttendance() {
        String date = getIntent().getStringExtra("date");
        String classname = getIntent().getStringExtra("class");

        FirebaseDatabase.getInstance().getReference("Attendance_Reports")
                .child(date + Common.currentClassName)
                .removeValue()
                .addOnSuccessListener(unused ->
                        Toast.makeText(Reports_Detail_Activity.this, "Processing...", Toast.LENGTH_SHORT).show());

        FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClassName)
                .child("Attendance").child(date)
                .removeValue()
                .addOnSuccessListener(unused ->
                        Toast.makeText(Reports_Detail_Activity.this, "Done !", Toast.LENGTH_SHORT).show());
        Intent intent = new Intent();
        intent.putExtra("deletedDate", date);
        setResult(RESULT_OK, intent);
        finish();

    }

}