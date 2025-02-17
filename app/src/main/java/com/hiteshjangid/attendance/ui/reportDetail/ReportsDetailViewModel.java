package com.hiteshjangid.attendance.ui.reportDetail;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hiteshjangid.attendance.callback.IReportDetailCallbackListener;
import com.hiteshjangid.attendance.model.Attendance_Students_List;

import java.util.ArrayList;
import java.util.List;

public class ReportsDetailViewModel extends ViewModel implements IReportDetailCallbackListener {

    private MutableLiveData<List<Attendance_Students_List>> listMutableLiveData ;
    private final IReportDetailCallbackListener listener;
    private final MutableLiveData<String> messageError = new MutableLiveData<>();

    public ReportsDetailViewModel(){
        listener = this;
    }

    public MutableLiveData<List<Attendance_Students_List>> getStudentList(String classname,String subjectName , String data){
        if (listMutableLiveData == null){
            listMutableLiveData = new MutableLiveData<>();
            readReportsDetail(classname,subjectName,data);
        }
        return listMutableLiveData;
    }


    private void readReportsDetail(String classname, String subjName, String date) {
//        attendance_students_lists.clear();
        List<Attendance_Students_List> attendance_students_lists = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Classes").child(classname+subjName).child("Attendance").child(date);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                attendance_students_lists.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Attendance_Students_List attendanceStudentsList = dataSnapshot.getValue(Attendance_Students_List.class);
                    attendance_students_lists.add(attendanceStudentsList);

                } if (attendance_students_lists.size() > 0) {
                    listener.onReportDetailLoadSuccess(attendance_students_lists);
                } else {
                    listener.onReportDetailLoadFailed("Empty");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onReportDetailLoadSuccess(List<Attendance_Students_List> attendance_students_lists) {
        listMutableLiveData.setValue(attendance_students_lists);
    }

    @Override
    public void onReportDetailLoadFailed(String message) {
        messageError.setValue(message);
    }
}