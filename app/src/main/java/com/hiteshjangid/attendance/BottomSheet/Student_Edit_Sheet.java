package com.hiteshjangid.attendance.BottomSheet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hiteshjangid.attendance.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hiteshjangid.attendance.StudentProfile;
import com.hiteshjangid.attendance.common.Common;
import com.hiteshjangid.attendance.model.Attendance_Reports;
import com.hiteshjangid.attendance.model.Attendance_Students_List;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Student_Edit_Sheet extends BottomSheetDialogFragment {

    public String _name;
    public TextView totalDaysOff;
    public TextView totalDays;
    public TextView totalDaysOn;
    public String _regNo;
    public String _uniqueId;
    public String _studentId;
    public View save;
    public EditText name_student, regNo_student, mobNo_student;
    public CardView detail;
    private int INITIAL_DAYS_OFF = 0;
    private int INITIAL_DAYS_ON = 0;
    private List<String> classesid;

    public Student_Edit_Sheet(String stuName, String regNo,String uniqueId,String studentId) {
        _name = stuName;
        _regNo = regNo;
        _uniqueId = uniqueId;
        _studentId = studentId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.bottomsheet_student_edit, container, false);

        totalDaysOn = v.findViewById(R.id.total_days_on);
        totalDays = v.findViewById(R.id.total_days);
        totalDaysOff = v.findViewById(R.id.total_days_off);
        name_student = v.findViewById(R.id.stu_name_edit);
        regNo_student = v.findViewById(R.id.stu_regNo_edit);
        detail = v.findViewById(R.id.detail);
        //save = v.findViewById(R.id.save);

        name_student.setText(_name);
        regNo_student.setText(_regNo);
        classesid = new ArrayList<>();



//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Toast.makeText(getContext(),"Updated",Toast.LENGTH_SHORT).show();
//            }
//        });


        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on clicking on a specific student to open their profile, store his/her id to call later from the StudentProfile Activity
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("id", _studentId);
                editor.apply();

                // open the StudentProfile Activity and pass the required info to there
                Intent intent = new Intent(getContext(), StudentProfile.class);
                intent.putExtra("id",_uniqueId);
                intent.putExtra("studentId", _studentId);
                intent.putExtra("name",_name);
                //intent.putExtra("class_id",attendanceStudentsList.getClassID());
                startActivity(intent);
            }
        });

        downloadeINFO();
        downloadReport();
        return v;
    }

    private void downloadeINFO() {
        FirebaseDatabase.getInstance().getReference("Attendance_Reports").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classesid.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Attendance_Reports attendance_reports = dataSnapshot.getValue(Attendance_Reports.class);
                    if (attendance_reports.getClassId().equals(Common.currentClassName)){
                        classesid.add(attendance_reports.getDate());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void downloadReport() {
        // refer to the Correct Reference
        FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClassName)
                .child("Attendance")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // you already guessed it :)
                        if (snapshot.exists()) {

                            int TotalDays = (int) snapshot.getChildrenCount();
                            totalDays.setText(""+classesid.size());
                            // for each item under the reference "attendance" do the following :
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                // iterator to iterate over each object under the refernce
                                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                                while (iterator.hasNext()) {
                                    // declaring the data and storing them in a StudentAttendanceModel object
                                    DataSnapshot dataSnapshot1 = iterator.next();
                                    Attendance_Students_List list = dataSnapshot1.getValue(Attendance_Students_List.class);

                                    // spend all your life avoiding nullPointerExceptions
                                    assert list != null;
                                    // download only the days off for this student
                                    if (list.getUnique_ID().equals(_uniqueId)
                                            && list.getAttendance().equals("Absent") && classesid.contains(list.getDate())) {

                                        // add their value to 0 and setting the TextView of the days off
                                        INITIAL_DAYS_OFF += 1;
                                        totalDaysOff.setText(" " + INITIAL_DAYS_OFF);


                                    }else if (list.getUnique_ID().equals(_uniqueId)
                                            && list.getAttendance().equals("Present") && classesid.contains(list.getDate())){
                                        INITIAL_DAYS_ON +=1;
                                        totalDaysOn.setText(""+INITIAL_DAYS_ON);
                                    }

                                }
                            }
                        } else {
                            Toast.makeText(getContext(), "err", Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
}