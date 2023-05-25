package com.hiteshjangid.attendance.excel;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hiteshjangid.attendance.model.Class_Names;

import java.util.ArrayList;
import java.util.List;

public class ExportGrade {

    public static void export(String gradeName, String date) {
        try {
            FirebaseDatabase.getInstance().getReference("Classes")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            List<String> classNames = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Class_Names classNamesModel = dataSnapshot.getValue(Class_Names.class);
                                if (classNamesModel != null && classNamesModel.getGradeType().equals(gradeName)) {
                                    classNames.add(classNamesModel.getName_class() + classNamesModel.getName_subject());
                                }
                            }

                            for (String className : classNames) {
                                ExcelExporter.export(date, className, "");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
