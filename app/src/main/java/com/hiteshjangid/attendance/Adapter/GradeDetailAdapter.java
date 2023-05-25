package com.hiteshjangid.attendance.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.hiteshjangid.attendance.ui.main.MainActivity;
import com.hiteshjangid.attendance.R;
import com.hiteshjangid.attendance.model.Grade_Names;

import java.util.List;

public class GradeDetailAdapter extends RecyclerView.Adapter<GradeDetailAdapter.ViewHolder> {

    private Context mContext;
    private List<Grade_Names> gradeNamesList;
    private CardView cardView;

    public GradeDetailAdapter(Context mContext, List<Grade_Names> gradeNamesList) {
        this.mContext = mContext;
        this.gradeNamesList = gradeNamesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Grade_Names grade_names = gradeNamesList.get(position);
        holder.class_name.setVisibility(View.GONE);
        holder.subject_name.setText(grade_names.getName_grade());
        holder.subject_name.setGravity(View.TEXT_ALIGNMENT_CENTER);
        holder.total_students.setVisibility(View.GONE);

        // Convert 100dp to pixels
        float density = holder.itemView.getContext().getResources().getDisplayMetrics().density;
        int minHeightPx = (int) (100 * density + 0.5f);
        holder.frameLayout.setMinimumHeight(minHeightPx);
        // Set gravity of holder.frameLayout to center
        holder.frameLayout.setGravity(Gravity.CENTER);
        holder.subject_name.setGravity(Gravity.CENTER);
        holder.class_name.setGravity(Gravity.CENTER);
        holder.total_students.setGravity(Gravity.CENTER);

        if (grade_names.getPosition_bg().equals("2")) {
            int[] colors = {0xFF33CCFF, 0xFFFF99CC};
            GradientDrawable gradientDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.BL_TR, colors);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            gradientDrawable.setGradientCenter(0.5f, 0.5f);
            gradientDrawable.setGradientRadius(1);
            holder.frameLayout.setBackground(gradientDrawable);
        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            intent.putExtra("theme", gradeNamesList.get(holder.getAdapterPosition()).getPosition_bg());
            intent.putExtra("gradeDetailName", gradeNamesList.get(holder.getAdapterPosition()).getName_grade());
            intent.putExtra("gradeDetailroom_ID", gradeNamesList.get(holder.getAdapterPosition()).getId());
            Pair<View, String> p1 = Pair.create(holder.cardView, "ExampleTransition");
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return gradeNamesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView class_name;
        public TextView subject_name;
        public TextView total_students;
        public ImageView imageView_bg;
        public RelativeLayout frameLayout;
        public CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            class_name = itemView.findViewById(R.id.className_adapter);
            subject_name = itemView.findViewById(R.id.subjectName_adapter);
            imageView_bg = itemView.findViewById(R.id.imageClass_adapter);
            frameLayout = itemView.findViewById(R.id.frame_bg);
            cardView = itemView.findViewById(R.id.cardView_adapter);
            total_students = itemView.findViewById(R.id.totalStudents_adapter);
        }
    }
}
