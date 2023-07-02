package com.hiteshjangid.attendance.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiteshjangid.attendance.R;
import com.hiteshjangid.attendance.model.Attendance_Reports;
import com.hiteshjangid.attendance.ui.reportDetail.Reports_Detail_Activity;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;

public class ReportsNewAdapter extends RecyclerView.Adapter<ReportsNewAdapter.ViewHolder> {
    private Context mContext;
    private List<Attendance_Reports> attendance_reports;
    private int month;
    private int year;

    public ReportsNewAdapter(Context mContext, List<Attendance_Reports> attendance_reports, int month, int year) {
        this.mContext = mContext;
        this.attendance_reports = attendance_reports;
        this.month = month;
        this.year = year;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reports_adapter_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Attendance_Reports attendanceReport = attendance_reports.get(position);

        holder.month.setText(attendanceReport.getMonthOnly());
        holder.date.setText(attendanceReport.getDateOnly());

        int dayOfMonth = Integer.parseInt(attendanceReport.getDateOnly().split("-")[0]);
        int monthOfYear = month - 1;
        int yearValue = year;
        String dayOfWeek = getDayOfWeek(yearValue, monthOfYear, dayOfMonth);
        holder.day.setText(dayOfWeek);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Reports_Detail_Activity.class);
                intent.putExtra("ID", attendance_reports.get(holder.getAbsoluteAdapterPosition()).getDate_and_classID());
                intent.putExtra("date", attendance_reports.get(holder.getAbsoluteAdapterPosition()).getDate());
                intent.putExtra("subject", attendance_reports.get(holder.getAbsoluteAdapterPosition()).getSubjName());
                intent.putExtra("class", attendance_reports.get(holder.getAbsoluteAdapterPosition()).getClassname());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attendance_reports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView month;
        public TextView date;
        public TextView day;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            month = itemView.findViewById(R.id.month_report_adapter);
            date = itemView.findViewById(R.id.date_report_adapter);
            day = itemView.findViewById(R.id.date_day_report_adapter);
        }
    }

    private String getDayOfWeek(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day - 1);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String[] daysOfWeek = new DateFormatSymbols().getWeekdays();

        return daysOfWeek[dayOfWeek];
    }
}