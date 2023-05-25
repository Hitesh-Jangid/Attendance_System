package com.hiteshjangid.attendance.excel;

import android.os.Environment;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hiteshjangid.attendance.model.Attendance_Students_List;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelExporter {

    static WritableSheet sheetA;
    static WritableWorkbook workbook;

    public static void export(String date, String classname, String subjecname) {
        File sd = Environment.getExternalStorageDirectory();
        String monthName = getMonthName(date);
        String excelFile = monthName + "_" + classname + "studentData.xls";

        File directory = new File(sd.getAbsolutePath());

        // create directory if not exist
        if (!directory.isDirectory()) {
            //noinspection ResultOfMethodCallIgnored
            directory.mkdirs();
        }

        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Classes")
                    .child(classname + subjecname).child("Attendance").child(date);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<String> regNoList = new ArrayList<>();
                    List<String> nameList = new ArrayList<>();
                    List<String> attendanceList = new ArrayList<>();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Attendance_Students_List attendance = dataSnapshot.getValue(Attendance_Students_List.class);
                        if (attendance != null) {
                            regNoList.add(attendance.getStudentRegNo());
                            nameList.add(attendance.getStudentName());
                            attendanceList.add(attendance.getAttendance());
                        }
                    }

                    File file = new File(directory, excelFile);
                    WorkbookSettings workbookSettings = new WorkbookSettings();
                    workbookSettings.setLocale(new Locale(Locale.ENGLISH.getLanguage(), Locale.ENGLISH.getCountry()));

                    try {
                        workbook = Workbook.createWorkbook(file, workbookSettings);
                        sheetA = workbook.createSheet("SheetA", 0);

                        int numDays = getNumberOfDaysInMonth(date);

                        // Write header row
                        sheetA.addCell(new Label(0, 0, "Student Reg No"));
                        sheetA.addCell(new Label(1, 0, "Student Name"));

                        int column = 2;
                        for (int day = 1; day <= numDays; day++) {
                            String dayLabel = String.format(Locale.ENGLISH, "%02d%s", day, getOrdinalSuffix(day));
                            sheetA.addCell(new Label(column, 0, dayLabel));
                            column++;
                        }

                        sheetA.addCell(new Label(column, 0, "Total Attendance"));

                        int row = 1;
                        for (int i = 0; i < regNoList.size(); i++) {
                            sheetA.addCell(new Label(0, row, regNoList.get(i)));
                            sheetA.addCell(new Label(1, row, nameList.get(i)));

                            String[] attendanceArray = attendanceList.get(i).split(",");
                            column = 2;
                            for (int j = 0; j < numDays; j++) {
                                if (attendanceArray.length > j) {
                                    sheetA.addCell(new Label(column, row, attendanceArray[j]));
                                }
                                column++;
                            }

                            row++;
                        }

                        workbook.write();
                        workbook.close();
                    } catch (IOException | WriteException e) {
                        e.printStackTrace();
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

    private static String getMonthName(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy", Locale.getDefault());
        Date parsedDate;
        try {
            parsedDate = sdf.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parsedDate);
        return new SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.getTime());
    }

    private static int getNumberOfDaysInMonth(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(date.substring(3)));
        calendar.set(Calendar.MONTH, Integer.parseInt(date.substring(0, 2)) - 1);

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private static String getOrdinalSuffix(int number) {
        int remainder10 = number % 10;
        int remainder100 = number % 100;

        if (remainder10 == 1 && remainder100 != 11) {
            return "st";
        } else if (remainder10 == 2 && remainder100 != 12) {
            return "nd";
        } else if (remainder10 == 3 && remainder100 != 13) {
            return "rd";
        } else {
            return "th";
        }
    }
}
