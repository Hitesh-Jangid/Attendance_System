package com.hiteshjangid.attendance.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hiteshjangid.attendance.BottomSheet.Student_Edit_Sheet;
import com.hiteshjangid.attendance.R;
import com.hiteshjangid.attendance.common.Common;
import com.hiteshjangid.attendance.model.Attendance_Reports;
import com.hiteshjangid.attendance.model.Attendance_Students_List;
import com.hiteshjangid.attendance.model.Students_List;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class StudentsListNewAdapter extends RecyclerView.Adapter<StudentsListNewAdapter.ViewHolder> {

    private Context mContext;
    private List<Students_List> students_lists;
    private final String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());

    public StudentsListNewAdapter(Context mContext, List<Students_List> students_lists) {
        this.mContext = mContext;
        this.students_lists = students_lists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_attendance_adapter, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Students_List students_list = students_lists.get(position);

        holder.student_name.setText(students_list.getName_student());
        holder.student_regNo.setText(students_list.getRegNo_student());

        checkAttendance(holder, students_list);

        FirebaseDatabase.getInstance().getReference("Attendance_Reports")
                .child(date + students_list.getClass_id())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Attendance_Reports attendance_reports = snapshot.getValue(Attendance_Reports.class);
                            assert attendance_reports != null;
                            if (attendance_reports.getDate().equals(date))
                                holder.radioGroup.setVisibility(View.GONE);
                        } else {
                            holder.radioGroup.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

        if (holder.radioButton_present.isChecked()) {
            if (!students_lists.isEmpty()) {
                final String attendance = "Present";

                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString(students_lists.get(holder.getAbsoluteAdapterPosition()).getRegNo_student(), attendance);
                editor.apply();

                HashMap<Object, String> hashMap = new HashMap<>();
                String uniqueId = students_lists.get(holder.getAbsoluteAdapterPosition()).getRegNo_student() + students_list.getName_student();
                hashMap.put("studentName", students_lists.get(holder.getAbsoluteAdapterPosition()).getName_student());
                hashMap.put("attendance", attendance);
                hashMap.put("studentRegNo", students_lists.get(holder.getAbsoluteAdapterPosition()).getRegNo_student());
                hashMap.put("classID", students_lists.get(holder.getAbsoluteAdapterPosition()).getClass_id());
                hashMap.put("date", date);
                hashMap.put("unique_ID", students_lists.get(holder.getAbsoluteAdapterPosition()).getRegNo_student() + students_list.getClass_id());

                FirebaseDatabase.getInstance().getReference("Classes")
                        .child(students_list.getClass_id())
                        .child("Attendance").child(date)
                        .child(uniqueId).setValue(hashMap);
            }
        }

        holder.radioButton_present.setOnClickListener(view -> {
            if (!students_lists.isEmpty()) {
                final String attendance = "Present";

                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString(students_lists.get(holder.getAbsoluteAdapterPosition()).getRegNo_student(), attendance);
                editor.apply();

                HashMap<Object, String> hashMap = new HashMap<>();
                String uniqueId = students_lists.get(holder.getAbsoluteAdapterPosition()).getRegNo_student() + students_list.getName_student();
                hashMap.put("studentName", students_lists.get(holder.getAbsoluteAdapterPosition()).getName_student());
                hashMap.put("attendance", attendance);
                hashMap.put("studentRegNo", students_lists.get(holder.getAbsoluteAdapterPosition()).getRegNo_student());
                hashMap.put("classID", students_lists.get(holder.getAbsoluteAdapterPosition()).getClass_id());
                hashMap.put("date", date);
                hashMap.put("unique_ID", students_lists.get(holder.getAbsoluteAdapterPosition()).getRegNo_student() + students_list.getClass_id());

                FirebaseDatabase.getInstance().getReference("Classes")
                        .child(students_list.getClass_id())
                        .child("Attendance").child(date)
                        .child(uniqueId).setValue(hashMap);
            }
        });

        holder.radioButton_absent.setOnClickListener(view -> {
            if (!students_lists.isEmpty()) {
                final String attendance = "Absent";

                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString(students_lists.get(holder.getAbsoluteAdapterPosition()).getRegNo_student(), attendance);
                editor.apply();

                HashMap<Object, String> hashMap = new HashMap<>();
                String uniqueId = students_lists.get(holder.getAbsoluteAdapterPosition()).getRegNo_student() + students_list.getName_student();
                hashMap.put("studentName", students_lists.get(holder.getAbsoluteAdapterPosition()).getName_student());
                hashMap.put("attendance", attendance);
                hashMap.put("studentRegNo", students_lists.get(holder.getAbsoluteAdapterPosition()).getRegNo_student());
                hashMap.put("classID", students_lists.get(holder.getAbsoluteAdapterPosition()).getClass_id());
                hashMap.put("date", date);
                hashMap.put("unique_ID", students_lists.get(holder.getAbsoluteAdapterPosition()).getRegNo_student() + students_list.getClass_id());

                FirebaseDatabase.getInstance().getReference("Classes")
                        .child(students_list.getClass_id()).child("Attendance")
                        .child(date).child(uniqueId).setValue(hashMap);
            }
        });

        holder.itemView.setOnClickListener(view -> {
            String studentId = students_lists.get(holder.getAbsoluteAdapterPosition()).getId();
            String stuName = students_lists.get(holder.getAbsoluteAdapterPosition()).getName_student();
            String regNo = students_lists.get(holder.getAbsoluteAdapterPosition()).getRegNo_student();
            String uniqueId = students_lists.get(holder.getAbsoluteAdapterPosition()).getRegNo_student() + students_list.getClass_id();
            Student_Edit_Sheet student_edit_sheet = new Student_Edit_Sheet(stuName, regNo, uniqueId, studentId);
            student_edit_sheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme);
            student_edit_sheet.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(), "BottomSheet");
        });
    }

    private void checkAttendance(ViewHolder holder, Students_List students_list) {
        String child2 = students_list.getRegNo_student() + students_list.getName_student();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Classes").child(students_list.getClass_id()).child("Attendance").child(date).child(child2);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Attendance_Students_List attendanceStudentsList = snapshot.getValue(Attendance_Students_List.class);
                    assert attendanceStudentsList != null;
                    if (attendanceStudentsList.getAttendance().equals("Absent")) {
                        holder.radioButton_absent.setChecked(true);
                    } else {
                        holder.radioButton_present.setChecked(true);
                    }
                } else if (Common.holidayOrNot.equals("no") && !students_lists.isEmpty()) {
                    holder.radioButton_present.setChecked(true);
                    final String attendance = "Present";
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString(students_lists.get(holder.getAbsoluteAdapterPosition()).getRegNo_student(), attendance);
                    editor.apply();

                    HashMap<Object, String> hashMap = new HashMap<>();
                    String uniqueId = students_lists.get(holder.getAbsoluteAdapterPosition()).getRegNo_student() + students_list.getName_student();
                    hashMap.put("studentName", students_lists.get(holder.getAbsoluteAdapterPosition()).getName_student());
                    hashMap.put("attendance", attendance);
                    hashMap.put("studentRegNo", students_lists.get(holder.getAbsoluteAdapterPosition()).getRegNo_student());
                    hashMap.put("classID", students_lists.get(holder.getAbsoluteAdapterPosition()).getClass_id());
                    hashMap.put("date", date);
                    hashMap.put("unique_ID", students_lists.get(holder.getAbsoluteAdapterPosition()).getRegNo_student() + students_list.getClass_id());

                    FirebaseDatabase.getInstance().getReference("Classes")
                            .child(students_list.getClass_id()).child("Attendance")
                            .child(date).child(uniqueId).setValue(hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return students_lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView student_name;
        public TextView student_regNo;
        public LinearLayout layout;
        public RadioGroup radioGroup;
        public RadioButton radioButton_present, radioButton_absent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            student_name = itemView.findViewById(R.id.student_name_adapter);
            student_regNo = itemView.findViewById(R.id.student_regNo_adapter);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            radioButton_present = itemView.findViewById(R.id.radio_present);
            radioButton_absent = itemView.findViewById(R.id.radio_absent);
            layout = itemView.findViewById(R.id.layout_click);
        }
    }
}
